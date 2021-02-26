package com.particle.game.world.level;

import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.block.attribute.BlockAttributeService;
import com.particle.game.block.interactor.BlockWorldService;
import com.particle.game.item.DurabilityService;
import com.particle.game.sound.SoundService;
import com.particle.game.world.aoi.BroadcastServiceProxy;
import com.particle.game.world.nav.NavMeshService;
import com.particle.game.world.particle.ParticleService;
import com.particle.game.world.physical.BlockColliderCheckService;
import com.particle.model.block.Block;
import com.particle.model.block.element.BlockElement;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.events.level.block.*;
import com.particle.model.item.ItemStack;
import com.particle.model.level.Level;
import com.particle.model.level.LevelEventType;
import com.particle.model.math.BlockFace;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.data.LevelEventPacket;
import com.particle.model.network.packets.data.UpdateBlockPacket;
import com.particle.model.player.Player;
import com.particle.model.sound.SoundId;
import com.particle.network.NetworkManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BlockService {

    @Inject
    private BroadcastServiceProxy broadcastServiceProxy;
    @Inject
    private NetworkManager networkManager;

    @Inject
    private DurabilityService durabilityService;

    @Inject
    private BlockColliderCheckService blockColliderCheckService;
    @Inject
    private LevelService levelService;
    @Inject
    private ChunkService chunkService;
    @Inject
    private BlockWorldService blockWorldService;

    @Inject
    private SoundService soundService;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Inject
    private ParticleService particleService;

    @Inject
    private NavMeshService navMeshService;

    @Inject
    private BlockAttributeService blockAttributeService;

    /**
     * 方块破坏粒子效果
     *
     * @param player
     * @param position
     * @param holdItem
     */
    public void blockDamagedByPlayer(Player player, Vector3 position, ItemStack holdItem, BlockFace blockFace) {
        BlockDamageEvent blockDamageEvent = new BlockDamageEvent(player, holdItem);
        blockDamageEvent.setPosition(position);

        eventDispatcher.dispatchEvent(blockDamageEvent);

        BlockPrototype blockPrototype = levelService.getBlockTypeAt(player.getLevel(), position);

        LevelEventPacket levelEventPacket = new LevelEventPacket();
        levelEventPacket.setEventType(LevelEventType.StartBlockCracking.getType());
        levelEventPacket.setPosition(new Vector3f(position));

        // 若上方有火且在上方挖方塊，則撲滅
        Vector3 upVector = position.up();
        BlockPrototype fireType = levelService.getBlockTypeAt(player.getLevel(), upVector);
        if (fireType != null && fireType == BlockPrototype.FIRE && blockFace == BlockFace.UP) {
            levelService.setBlockAt(player.getLevel(), Block.getBlock(BlockPrototype.AIR), upVector);
        }

        int baseTime = (int) (65535 / this.blockAttributeService.getBaseBreakTime(blockPrototype)) / 20;
        if (!blockPrototype.getBlockElement().getToolName().equals(BlockElement.DEFAULT)) {

            switch (blockPrototype.getBlockElement()) {
                case PICKAXE:
                case ICE:
                case RAIL:
                case METAL1:
                case ROCK1:
                    switch (holdItem.getItemType()) {
                        case WOODEN_PICKAXE:
                            baseTime = baseTime * 6;
                            break;
                        case STONE_PICKAXE:
                            baseTime = baseTime * 12;
                            break;
                        case IRON_PICKAXE:
                            baseTime = baseTime * 18;
                            break;
                        case GOLDEN_PICKAXE:
                            baseTime = baseTime * 36;
                            break;
                        case DIAMOND_PICKAXE:
                            baseTime = baseTime * 24;
                            break;
                    }
                    break;
                case METAL2:
                case ROCK2:
                    switch (holdItem.getItemType()) {
                        case WOODEN_PICKAXE:
                            baseTime = baseTime * 2;
                            break;
                        case STONE_PICKAXE:
                            baseTime = baseTime * 12;
                            break;
                        case IRON_PICKAXE:
                            baseTime = baseTime * 18;
                            break;
                        case GOLDEN_PICKAXE:
                            baseTime = baseTime * 36;
                            break;
                        case DIAMOND_PICKAXE:
                            baseTime = baseTime * 24;
                            break;
                    }
                    break;
                case METAL3:
                case ROCK3:
                    switch (holdItem.getItemType()) {
                        case WOODEN_PICKAXE:
                            baseTime = baseTime * 2;
                            break;
                        case STONE_PICKAXE:
                            baseTime = baseTime * 4;
                            break;
                        case IRON_PICKAXE:
                            baseTime = baseTime * 18;
                            break;
                        case GOLDEN_PICKAXE:
                            baseTime = baseTime * 36;
                            break;
                        case DIAMOND_PICKAXE:
                            baseTime = baseTime * 24;
                            break;
                    }
                    break;
                case ROCK4:
                    switch (holdItem.getItemType()) {
                        case WOODEN_PICKAXE:
                            baseTime = baseTime * 2;
                            break;
                        case STONE_PICKAXE:
                            baseTime = baseTime * 4;
                            break;
                        case IRON_PICKAXE:
                            baseTime = baseTime * 18;
                            break;
                        case GOLDEN_PICKAXE:
                            baseTime = baseTime * 36;
                            break;
                        case DIAMOND_PICKAXE:
                            baseTime = baseTime * 24;
                            break;
                    }
                    break;
                case SAND:
                case SNOW:
                    switch (holdItem.getItemType()) {
                        case WOODEN_SHOVEL:
                            baseTime = baseTime * 6;
                            break;
                        case STONE_SHOVEL:
                            baseTime = baseTime * 12;
                            break;
                        case IRON_SHOVEL:
                            baseTime = baseTime * 18;
                            break;
                        case GOLDEN_SHOVEL:
                            baseTime = baseTime * 36;
                            break;
                        case DIAMOND_SHOVEL:
                            baseTime = baseTime * 24;
                            break;
                    }
                    break;
                case PLANT:
                case WOOD:
                    switch (holdItem.getItemType()) {
                        case WOODEN_AXE:
                            baseTime = baseTime * 6;
                            break;
                        case STONE_AXE:
                            baseTime = baseTime * 12;
                            break;
                        case IRON_AXE:
                            baseTime = baseTime * 18;
                            break;
                        case GOLDEN_AXE:
                            baseTime = baseTime * 36;
                            break;
                        case DIAMOND_AXE:
                            baseTime = baseTime * 24;
                            break;
                    }
            }
        }
        levelEventPacket.setData(baseTime);

        this.broadcastServiceProxy.broadcast(player.getLevel(), position, levelEventPacket);
    }

    /**
     * 方块停止被玩家破坏
     *
     * @param player
     * @param position
     */
    public void blockStopDamagedByPlayer(Player player, Vector3 position) {
        BlockStopBreakEvent blockStopBreakEvent = new BlockStopBreakEvent(player);
        blockStopBreakEvent.setPosition(position);

        this.eventDispatcher.dispatchEvent(blockStopBreakEvent);

        LevelEventPacket levelEventPacket = new LevelEventPacket();
        levelEventPacket.setEventType(LevelEventType.StopBlockCracking.getType());
        levelEventPacket.setPosition(new Vector3f(position));
        levelEventPacket.setData(0);

        this.broadcastServiceProxy.broadcast(player.getLevel(), position, levelEventPacket);
    }

    /**
     * 方块生长
     *
     * @param level
     * @param position
     */
    public boolean blockGrowUp(Level level, Vector3 position) {
        BlockGrowEvent blockGrowEvent = new BlockGrowEvent(level);
        blockGrowEvent.setPosition(position);
        this.eventDispatcher.dispatchEvent(blockGrowEvent);

        if (blockGrowEvent.isCancelled()) {
            return false;
        }

        Block blockAt = this.levelService.getBlockAt(level, position);

        // 如果是草方块，有概率长出花来
        if (blockAt.getType() == BlockPrototype.GRASS || blockAt.getType() == BlockPrototype.GRASS_PATH) {
            this.levelService.setBlockAt(level, blockAt, position);
            return true;
        }

        if (blockAt.getType().getMaxMetadata() > 0 && blockAt.getMeta() + 1 < blockAt.getType().getMaxMetadata()) {
            blockAt.setMeta(blockAt.getMeta() + 1);
            this.levelService.setBlockAt(level, blockAt, position);
            return true;
        }

        return false;
    }

    /**
     * 生物放置方块
     *
     * @param player
     * @param block
     * @param position
     * @param clickPosition
     * @param clickOffsetPosition
     * @return
     */
    public boolean placeBlockByPlayer(Player player, Block block, ItemStack handItem, Vector3 position, Vector3 clickPosition, Vector3f clickOffsetPosition) {

        // 更新方块信息
        boolean state = this.blockWorldService.onBlockPrePlaced(player.getLevel(), player, block, position, clickPosition, clickOffsetPosition);
        if (!state) {
            return false;
        }

        BlockPlaceEvent blockPlaceEvent = new BlockPlaceEvent(player.getLevel(), player, handItem);
        blockPlaceEvent.setPosition(position);
        blockPlaceEvent.setItemInHand(handItem);
        blockPlaceEvent.setBlock(block);
        blockPlaceEvent.setClickPosition(clickPosition);

        this.eventDispatcher.dispatchEvent(blockPlaceEvent);
        // 位置合法性检查
        if (!this.blockColliderCheckService.checkEntityEnter(player.getLevel(), position)) {
            blockPlaceEvent.cancel();
        }

        Block targetBlock = this.levelService.getBlockAt(player.getLevel(), position);
        if (blockPlaceEvent.isCancelled()) {
            this.levelService.setBlockAt(player.getLevel(), targetBlock, position);
            return false;
        }

        //更新方块
        state = this.blockWorldService.handleBlockPlaced(player.getLevel(), player, blockPlaceEvent.getBlock(), position, clickPosition, clickOffsetPosition);
        if (!state) {
            return false;
        }

        // 更新NavSquare
        this.navMeshService.refreshLayout(player.getLevel(), this.chunkService.indexChunk(player.getLevel(), position), position.getY());
        this.navMeshService.refreshLayout(player.getLevel(), this.chunkService.indexChunk(player.getLevel(), position), position.getY() - 1);

        // 广播声音
        this.soundService.broadcastLevelSound(player.getLevel(), new Vector3f(position), SoundId.Place, targetBlock.getRuntimeId());
        // 初始化对应的blockEntity
        this.blockWorldService.onBlockPlaced(player.getLevel(), player, blockPlaceEvent.getBlock(), position);

        return true;
    }

    /**
     * 方块被放置
     *
     * @param level
     * @param block
     * @param handItem
     * @param position
     * @param clickPosition
     * @return
     */
    public boolean placeBlockByLevel(Level level, Block block, ItemStack handItem, Vector3 position, Vector3 clickPosition) {
        // 检查目标方块的合法性
        Block targetBlock = this.levelService.getBlockAt(level, position);
        if (!targetBlock.getType().getBlockGeometry().canPassThrow()) {
            return false;
        }

        BlockPlaceEvent blockPlaceEvent = new BlockPlaceEvent(level, null, handItem);
        blockPlaceEvent.setPosition(position);
        blockPlaceEvent.setItemInHand(handItem);
        blockPlaceEvent.setBlock(block);
        blockPlaceEvent.setClickPosition(clickPosition);

        this.eventDispatcher.dispatchEvent(blockPlaceEvent);

        if (blockPlaceEvent.isCancelled()) {
            this.levelService.setBlockAt(level, targetBlock, position);

            return false;
        } else {
            // 更新方块信息
            boolean state = this.blockWorldService.onBlockPrePlaced(level, null, targetBlock, position, clickPosition, new Vector3f(0.5f, 0.5f, 0.5f));
            if (!state) {
                return false;
            }

            //更新方块
            state = this.levelService.setBlockAt(level, block, position);
            if (!state) {
                return false;
            }

            // 更新NavSquare
            this.navMeshService.refreshLayout(level, this.chunkService.indexChunk(level, position), position.getY());
            this.navMeshService.refreshLayout(level, this.chunkService.indexChunk(level, position), position.getY() - 1);

            // 广播声音
            this.soundService.broadcastLevelSound(level, new Vector3f(position), SoundId.Place, targetBlock.getRuntimeId());
            // 初始化对应的blockEntity
            this.blockWorldService.onBlockPlaced(level, null, targetBlock, position);

            return true;
        }
    }

    /**
     * 方块被玩家破坏
     *
     * @param player
     * @param position
     * @param caused
     * @return
     */
    public boolean breakBlockByPlayer(Player player, Vector3 position, BlockBreakEvent.Caused caused) {
        Block blockAt = this.levelService.getBlockAt(player.getLevel(), position);

        if (blockAt.getType() == BlockPrototype.AIR) {
            return false;
        }

        BlockBreakEvent blockBreakEvent = new BlockBreakEvent(player.getLevel());
        blockBreakEvent.setPlayer(player);
        blockBreakEvent.setPosition(position);
        blockBreakEvent.setBlock(blockAt);
        blockBreakEvent.setCaused(caused);
        this.eventDispatcher.dispatchEvent(blockBreakEvent);

        if (blockBreakEvent.isCancelled()) {
            // 取消破坏， 则还原物品，仅发送给本人即可
            UpdateBlockPacket updateBlockPacket = new UpdateBlockPacket();
            updateBlockPacket.setBlock(blockAt);
            updateBlockPacket.setVector3(position);
            updateBlockPacket.setRuntimeId(blockAt.getRuntimeId());
            updateBlockPacket.setLayer(0);
            updateBlockPacket.setFlag(UpdateBlockPacket.All);
            this.networkManager.sendMessage(player.getClientAddress(), updateBlockPacket);

            return false;
        }

        // 播放粒子效果
        this.particleService.playParticle(player.getLevel(), LevelEventType.ParticlesDestroyBlock, blockAt, new Vector3f(position), blockAt.getRuntimeId());

        // 处理预破坏逻辑，计算掉落物
        boolean state = this.blockWorldService.onBlockPreDestroy(player.getLevel(), player, blockAt, position);
        if (!state) {
            return false;
        }

        // 更新方块
        this.levelService.setBlockAt(player.getLevel(), Block.getBlock(BlockPrototype.AIR), position);

        // 更新NavSquare
        this.navMeshService.refreshLayout(player.getLevel(), this.chunkService.indexChunk(player.getLevel(), position), position.getY());
        this.navMeshService.refreshLayout(player.getLevel(), this.chunkService.indexChunk(player.getLevel(), position), position.getY() - 1);

        // 装备耐久度
        this.durabilityService.consumptionItem(player);

        //处理破坏完成逻辑
        this.blockWorldService.onBlockDestroyed(player.getLevel(), player, blockAt, position);
        return true;
    }

    /**
     * 方块被世界破坏
     *
     * @param level
     * @param position
     * @param caused
     * @return
     */
    public boolean breakBlockByLevel(Level level, Vector3 position, BlockBreakEvent.Caused caused) {
        Block blockAt = this.levelService.getBlockAt(level, position);

        BlockBreakEvent blockBreakEvent = new BlockBreakEvent(level);
        blockBreakEvent.setPlayer(null);
        blockBreakEvent.setPosition(position);
        blockBreakEvent.setBlock(blockAt);
        blockBreakEvent.setCaused(caused);
        this.eventDispatcher.dispatchEvent(blockBreakEvent);

        if (blockBreakEvent.isCancelled()) {
            return false;
        }

        //处理预破坏逻辑，计算掉落物
        boolean state = this.blockWorldService.onBlockPreDestroy(level, null, blockAt, position);
        if (!state) {
            return false;
        }

        //更新方块
        this.levelService.setBlockAt(level, Block.getBlock(BlockPrototype.AIR), position);

        // 更新NavSquare
        this.navMeshService.refreshLayout(level, this.chunkService.indexChunk(level, position), position.getY());
        this.navMeshService.refreshLayout(level, this.chunkService.indexChunk(level, position), position.getY() - 1);

        //处理破坏完成逻辑
        this.blockWorldService.onBlockDestroyed(level, null, blockAt, position);
        return true;
    }
}
