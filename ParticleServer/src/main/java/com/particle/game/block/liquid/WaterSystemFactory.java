package com.particle.game.block.liquid;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.core.ecs.system.IntervalECSSystem;
import com.particle.game.block.attribute.BlockAttributeService;
import com.particle.game.block.tile.model.WaterTileEntity;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.world.level.BlockService;
import com.particle.game.world.level.ChunkService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.Entity;
import com.particle.model.entity.component.liquid.WaterModule;
import com.particle.model.math.Vector3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * 放置水的處理
 */
public class WaterSystemFactory implements ECSSystemFactory<WaterSystemFactory.WaterSystem> {
    private static final Logger LOGGER = LoggerFactory.getLogger(WaterSystemFactory.class);

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    @Inject
    private LevelService levelService;

    @Inject
    private ChunkService chunkService;

    @Inject
    private EntitySpawnService entitySpawnService;

    @Inject
    private LiquidService liquidService;

    @Inject
    private BlockService blockService;

    @Inject
    private BlockAttributeService blockAttributeService;

    public class WaterSystem extends IntervalECSSystem {

        private Entity entity;

        public WaterSystem(Entity entity) {
            this.entity = entity;
        }

        @Override
        protected int getInterval() {
            return 3;
        }

        @Override
        public void doTick(long deltaTime) {
            if (!(entity instanceof WaterTileEntity)) {
                return;
            }
            liquidService.addFlowWaterTick();

            TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);
            Vector3 targetPosition = new Vector3(transformModule.getPosition());
            Block targetBlock = levelService.getBlockAt(entity.getLevel(), targetPosition);


            entity.getLevel().getLevelSchedule().scheduleSimpleTask("waterRemove", () -> {
                /**
                 * 调试代码，分析为什么tile entity绑定的坐标区块不存在，以及是否有可能发生内存泄漏。
                 */
                if (chunkService.indexChunk(entity.getLevel(), targetPosition) == null) {
                    LOGGER.error("Tile entity missing chunk at {}", targetPosition);
                    return;
                }

                entitySpawnService.despawnTileEntity(entity.getLevel(), targetPosition);
            });

            // 有水
            if (targetBlock.getMeta() <= liquidService.getDryWaterMeta()) {
                // 若是流水  且 若周圍沒有比自己高等的水
                if (targetBlock.getType() == BlockPrototype.FLOWING_WATER
                        && targetBlock.getMeta() > 0
                        && !liquidService.checkAroundHighLevelWater(entity.getLevel(), targetBlock, targetPosition)) {
                    // 若下方是水 且 滿水
                    BlockPrototype blockPrototype = levelService.getBlockTypeAt(entity.getLevel(), targetPosition.down());
                    if (blockPrototype == BlockPrototype.FLOWING_WATER && targetBlock.getMeta() == 1) {
                        // 直接變空氣
                        levelService.setBlockAt(entity.getLevel(), Block.getBlock(BlockPrototype.AIR), targetPosition);
                        blockService.placeBlockByLevel(entity.getLevel(), Block.getBlock(BlockPrototype.AIR), null, targetPosition, targetPosition);
                        return;
                    }

                    // 開始乾涸
                    liquidService.changeAir(entity.getLevel(), targetBlock, targetPosition, targetBlock.getMeta(), liquidService.getDryWaterMeta(), BlockPrototype.FLOWING_WATER, 2);
                    return;
                } else {
                    // 檢查下方
                    Block block = levelService.getBlockAt(entity.getLevel(), targetPosition.down());
                    if (block.getType() != BlockPrototype.WATER && blockAttributeService.isCanBeScour(block)) {
                        // 若是岩漿源
                        if (block.getType() == BlockPrototype.LAVA) {
                            block = liquidService.changeStoneBlock(entity.getLevel(), targetPosition.down(), BlockPrototype.OBSIDIAN);
                        }
                        // 差距兩階以上才需更新
                        else if (block.getType() != BlockPrototype.FLOWING_WATER || block.getMeta() > 1) {
                            // 下方水恢復成 全滿
                            targetBlock.setMeta(0);
                            block = liquidService.changeFlowingBlock(entity.getLevel(), block, targetPosition.down(), targetBlock.getMeta(), BlockPrototype.FLOWING_WATER);
                        }
                    }
                    // 若不可沖刷且可以擴散   或是  水源
                    if (targetBlock.getType() == BlockPrototype.WATER) {
                        // 檢查四周可沖刷的環境 且 (非水 或 水位高2階的)
                        for (int i = 0; i < 4; i++) {
                            Vector3 position = liquidService.getPositionByNumber(targetPosition, i);
                            block = levelService.getBlockAt(entity.getLevel(), position);
                            if (blockAttributeService.isCanBeScour(block) && ((block.getType() != BlockPrototype.FLOWING_WATER && block.getType() != BlockPrototype.WATER) || targetBlock.getMeta() < block.getMeta() - 1)) {
                                // 若是岩漿源
                                if (block.getType() == BlockPrototype.LAVA) {
                                    liquidService.changeStoneBlock(entity.getLevel(), position, BlockPrototype.OBSIDIAN);
                                } else {
                                    liquidService.changeFlowingBlock(entity.getLevel(), block, position, targetBlock.getMeta(), BlockPrototype.FLOWING_WATER);
                                }
                            }
                        }
                    }

                    // 若四周有2個以上水源
                    if (targetBlock.getType() == BlockPrototype.FLOWING_WATER && liquidService.getAroundLiquidCount(entity.getLevel(), targetPosition, BlockPrototype.WATER) >= 2) {
                        // 自己變成水源
                        Block waterBlock = Block.getBlock(BlockPrototype.WATER);
                        levelService.setBlockAt(entity.getLevel(), waterBlock, targetPosition);
                        blockService.placeBlockByLevel(entity.getLevel(), waterBlock, null, targetPosition, targetPosition);
                    }
                }
            }
        }
    }

    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{WaterModule.class};
    }

    @Override
    public WaterSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Entity) {
            return new WaterSystem((Entity) gameObject);
        }

        return null;
    }

    @Override
    public Class<WaterSystem> getSystemClass() {
        return WaterSystem.class;
    }
}
