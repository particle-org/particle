package com.particle.game.world.level.loader;

import com.particle.core.aoi.SceneManager;
import com.particle.executor.thread.IScheduleThread;
import com.particle.game.world.level.ChunkService;
import com.particle.model.level.Chunk;
import com.particle.model.level.Level;
import com.particle.model.level.LevelProvider;
import com.particle.model.level.chunk.ChunkData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChunkIOPool {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChunkIOPool.class);

    private static final int THREAD_AMOUNT = 4;
    private static final ExecutorService[] EXECUTORS = new ExecutorService[THREAD_AMOUNT];

    private static final long CHUNK_OPERATE_TIME_LIMIT = 100;

    private ChunkService chunkService;

    private boolean isRunning = true;

    public ChunkIOPool(ChunkService chunkService) {
        this.chunkService = chunkService;

        for (int i = 0; i < EXECUTORS.length; i++) {
            int index = i;
            EXECUTORS[i] = Executors.newSingleThreadExecutor(r -> new Thread(r, "ChunkIOPool_" + index));
        }
    }

    /**
     * 读取区块接口
     *
     * @param level
     * @param chunkX
     * @param chunkZ
     * @return
     */
    public ChunkLoadTask loadChunk(Level level, int chunkX, int chunkZ) {
        // 构造chunk
        Chunk chunk = new Chunk(
                SceneManager.getInstance().getGridNode(
                        level.getScene(),
                        chunkX,
                        chunkZ,
                        true),
                level.getLevelProviderMapper().getLevelProvider(chunkX, chunkZ),
                chunkX,
                chunkZ);

        // 构造存储任务
        ChunkLoadTask chunkLoadTask = new ChunkLoadTask(level, chunk);

        // 提交任务
        if (this.isRunning) {
            EXECUTORS[react(chunkX, chunkZ)].execute(() -> doLoad(chunkLoadTask));
        }

        return chunkLoadTask;
    }

    /**
     * 添加保存任务
     *
     * @param provider
     * @param snapshot
     * @param release
     * @param callback
     * @param callbackThread
     */
    public void saveChunk(LevelProvider provider, ChunkData snapshot, boolean release, Runnable callback, IScheduleThread callbackThread) {
        // 构造保存任务
        ChunkSaveTask chunkSaveTask = new ChunkSaveTask(provider, snapshot, release);
        chunkSaveTask.setCallback(callback);
        chunkSaveTask.setCallbackThread(callbackThread);

        // 提交任务，如果运行中，则提交到线程池中，否则直接同步执行（关机状态）
        if (this.isRunning) {
            EXECUTORS[react(snapshot.getxPos(), snapshot.getzPos())].execute(() -> doSave(chunkSaveTask));
        } else {
            doSave(chunkSaveTask);
        }
    }

    private void doLoad(ChunkLoadTask task) {
        // 若任务取消，则跳过执行
        if (task.isCancelled()) {
            return;
        }

        Chunk chunk = task.getChunk();
        int chunkX = task.getChunkX();
        int chunkZ = task.getChunkZ();

        // 标记任务已启动
        task.start();

        LOGGER.debug("Load chunk {},{} from level provider", chunkX, chunkZ);

        // 读取区块
        Level level = task.getLevel();
        try {
            long timestamp = System.currentTimeMillis();
            ChunkData chunkData = chunk.getProvider().loadChunk(chunkX, chunkZ);
            timestamp = System.currentTimeMillis() - timestamp;
            if (timestamp > CHUNK_OPERATE_TIME_LIMIT) {
                LOGGER.warn("Chunk loaded tooooo long! at {}ms.", timestamp);
            }

            if (chunkData == null) {
                chunkData = level.getChunkGenerate().getEmptyChunk(chunkX, chunkZ);
            }

            ChunkData finalChunkData = chunkData;
            level.getLevelSchedule().scheduleSimpleTask("Chunk init", () -> this.chunkService.enableChunk(level, chunk, finalChunkData));
        } catch (Exception e) {
            LOGGER.error("Fail to load chunk {},{}", chunkX, chunkZ);
            LOGGER.error("Exception : ", e);

            // 放到队尾重新加载一次区块
            if (!task.isRetry()) {
                task.setRetry(true);

                if (this.isRunning) {
                    EXECUTORS[react(task.getChunkX(), task.getChunkZ())].execute(() -> doLoad(task));
                }
            }
        }
    }

    /**
     * 区块保存任务
     *
     * @throws InterruptedException
     */
    private void doSave(ChunkSaveTask task) {
        // 可能没有任务
        if (task == null) {
            return;
        }

        ChunkData chunkData = task.getChunk();

        LOGGER.debug("Save chunk {},{} from level provider", chunkData.getxPos(), chunkData.getzPos());

        //保存区块数据
        try {
            long timestamp = System.currentTimeMillis();
            task.getProvider().saveChunk(chunkData, task.isRelease());
            timestamp = System.currentTimeMillis() - timestamp;
            if (timestamp > CHUNK_OPERATE_TIME_LIMIT) {
                LOGGER.warn("Chunk saved tooooo long! at {}ms.", timestamp);
            }

            // 回调
            if (task.getCallback() != null && task.getCallbackThread() != null) {
                task.getCallbackThread().scheduleSimpleTask("Chunk load callback", task.getCallback()::run);
            }
        } catch (Exception e) {
            LOGGER.error("Fail to save chunk at {},{}", task.getChunkX(), task.getChunkZ(), e);
        }
    }

    private int react(int chunkX, int chunkZ) {
        return Math.abs((chunkX + chunkZ) % THREAD_AMOUNT);
    }

    /**
     * 关闭所有线程
     */
    public void shutdown() {
        this.isRunning = false;

        for (ExecutorService executor : EXECUTORS) {
            executor.shutdown();
        }
    }
}
