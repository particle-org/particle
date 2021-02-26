package com.particle.game.world.level.grid;

import com.particle.api.inject.RequestStaticInject;
import com.particle.core.aoi.container.SceneDataProvider;
import com.particle.core.aoi.model.Grid;
import com.particle.core.aoi.model.Scene;
import com.particle.core.aoi.model.Subscriber;
import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystemManager;
import com.particle.game.scene.module.GridKeepAliveModule;
import com.particle.game.world.level.ChunkService;
import com.particle.model.level.Chunk;
import com.particle.model.level.Level;
import com.particle.model.network.packets.data.FullChunkDataPacket;
import com.particle.network.NetworkManager;

import javax.inject.Inject;

@RequestStaticInject
public class ChunkDataProvider extends SceneDataProvider<GameObject> {

    private static final ECSModuleHandler<GridKeepAliveModule> GRID_KEEP_ALIVE_MODULE_ECS_MODULE_HANDLER = ECSModuleHandler.buildHandler(GridKeepAliveModule.class);

    @Inject
    private static ChunkService chunkService;

    @Inject
    private static NetworkManager networkManager;

    private Level level;
    private Scene scene;

    public ChunkDataProvider(Level level, Scene scene) {
        this.level = level;
        this.scene = scene;
        this.scene.registerSceneDataProvider(this);
    }

    @Override
    public void onGridActive(Grid grid) {
        GameObject gameObject = new GameObject();
        GridKeepAliveModule gridKeepAliveModule = GRID_KEEP_ALIVE_MODULE_ECS_MODULE_HANDLER.bindModule(gameObject);
        gridKeepAliveModule.setGrid(grid);

        this.setData(grid, gameObject);

        ECSSystemManager.buildECSSystemTickList(gameObject);
    }

    @Override
    public void onGridInactive(Grid grid) {
        // TODO: 2019/12/26 注意异步场景下和onSubscripted的关系，是否会有内存泄漏
        chunkService.unloadChunk(level, grid.getX(), grid.getZ());
    }

    @Override
    public void onSubscribed(Grid grid, Subscriber subscriber) {
        Chunk chunk = chunkService.getChunk(this.level, grid.getX(), grid.getZ(), true);
        if (chunk.isRunning()) {
            FullChunkDataPacket fullChunkDataPacket = new FullChunkDataPacket();
            fullChunkDataPacket.setChunk(chunk);
            fullChunkDataPacket.setChunkX(chunk.getxPos());
            fullChunkDataPacket.setChunkZ(chunk.getzPos());
            // 發包即時處理
            fullChunkDataPacket.setHeightMap(chunkService.getHeightMap(chunk));
            fullChunkDataPacket.setBiomes(chunkService.getBiome(chunk));
            fullChunkDataPacket.setTileEntities(chunkService.getTileEntities(chunk));
            fullChunkDataPacket.setSubChunkCount(chunk.getChunkSections().length);
            fullChunkDataPacket.setCacheEnabled(false);
            networkManager.sendMessage(subscriber.getAddress(), fullChunkDataPacket);
        }
    }

    @Override
    public void onUnsubscribed(Grid grid, Subscriber subscriber) {

    }

    public Scene getScene() {
        return scene;
    }
}
