package com.particle.game.block.liquid;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystem;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.game.block.attribute.BlockAttributeService;
import com.particle.game.block.tile.model.LavaTileEntity;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.world.level.BlockService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.Entity;
import com.particle.model.entity.component.liquid.LavaModule;
import com.particle.model.math.Vector3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

/**
 * 放置岩漿的處理
 */
public class LavaSystemFactory implements ECSSystemFactory<LavaSystemFactory.LavaSystem> {
    private static final Logger logger = LoggerFactory.getLogger(LavaSystemFactory.class);

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    @Inject
    private LevelService levelService;

    @Inject
    private EntitySpawnService entitySpawnService;

    @Inject
    private BlockService blockService;

    @Inject
    private LiquidService liquidService;

    @Inject
    private BlockAttributeService blockAttributeService;

    public class LavaSystem implements ECSSystem {

        private Entity entity;

        public LavaSystem(Entity entity) {
            this.entity = entity;
        }

        @Override
        public void tick(long deltaTime) {
            if (!(entity instanceof LavaTileEntity)) {
                return;
            }
            liquidService.addFlowLavaTick();

            // 每 45 tick 流動一次
            if (liquidService.isFlowLavaTime(45)) {
                TransformModule transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);
                Vector3 targetPosition = new Vector3(transformModule.getPosition());
                Block targetBlock = levelService.getBlockAt(entity.getLevel(), targetPosition);

                entity.getLevel().getLevelSchedule().scheduleSimpleTask("lavaRemove", () -> entitySpawnService.despawnTileEntity(entity.getLevel(), targetPosition));

                // 若本身是岩漿源 且 四周有水
                if (targetBlock.getType() == BlockPrototype.LAVA && liquidService.isAroundWater(entity.getLevel(), targetPosition)) {
                    liquidService.changeStoneBlock(entity.getLevel(), targetPosition, BlockPrototype.OBSIDIAN);
                    return;
                }

                // 有岩漿
                if (targetBlock.getMeta() <= liquidService.getDryLavaMeta()) {
                    // 若是岩漿流 且 若周圍沒有比自己高等的岩漿
                    if (targetBlock.getType() == BlockPrototype.FLOWING_LAVA
                            && targetBlock.getMeta() > 0
                            && !liquidService.checkAroundHighLevelLava(entity.getLevel(), targetBlock, targetPosition)) {
                        // 若下方是岩漿
                        BlockPrototype blockPrototype = levelService.getBlockTypeAt(entity.getLevel(), targetPosition.down());
                        if (blockPrototype == BlockPrototype.FLOWING_LAVA && targetBlock.getMeta() == 1) {
                            // 直接變空氣
                            levelService.setBlockAt(entity.getLevel(), Block.getBlock(BlockPrototype.AIR), targetPosition);
                            blockService.placeBlockByLevel(entity.getLevel(), Block.getBlock(BlockPrototype.AIR), null, targetPosition, targetPosition);
                            return;
                        }

                        // 開始乾涸
                        liquidService.changeAir(entity.getLevel(), targetBlock, targetPosition, targetBlock.getMeta(), liquidService.getDryLavaMeta(), BlockPrototype.FLOWING_LAVA, 1);
                        return;
                    } else {
                        // 檢查下方
                        Block block = levelService.getBlockAt(entity.getLevel(), targetPosition.down());
                        if (block.getType() != BlockPrototype.LAVA && blockAttributeService.isCanBeScour(block)) {
                            // 若是水
                            if (block.getType() == BlockPrototype.WATER || block.getType() == BlockPrototype.FLOWING_WATER) {
                                block = liquidService.changeStoneBlock(entity.getLevel(), targetPosition.down(), BlockPrototype.STONE);
                            }
                            // 差距兩階以上才需更新
                            else if (block.getType() != BlockPrototype.FLOWING_LAVA || block.getMeta() > 1) {
                                // 下方岩漿恢復成 全滿
                                targetBlock.setMeta(0);
                                block = liquidService.changeFlowingBlock(entity.getLevel(), block, targetPosition.down(), targetBlock.getMeta(), BlockPrototype.FLOWING_LAVA);
                            }
                        }

                        // 若不可沖刷 且 可以擴散   或是  岩漿源
                        if (!blockAttributeService.isCanBeScour(block) && targetBlock.getMeta() < liquidService.getDryLavaMeta() || targetBlock.getType() == BlockPrototype.LAVA) {
                            // 檢查四周可沖刷的環境 且 (非岩漿 或 岩漿位高2階的)
                            for (int i = 0; i < 4; i++) {
                                Vector3 position = liquidService.getPositionByNumber(targetPosition, i);
                                // 若是水
                                if (block.getType() == BlockPrototype.WATER || block.getType() == BlockPrototype.FLOWING_WATER) {
                                    // 變成圓石
                                    liquidService.changeStoneBlock(entity.getLevel(), targetPosition.down(), BlockPrototype.COBBLESTONE);
                                } else {
                                    block = levelService.getBlockAt(entity.getLevel(), position);
                                    if (blockAttributeService.isCanBeScour(block) && ((block.getType() != BlockPrototype.FLOWING_LAVA && block.getType() != BlockPrototype.LAVA) || targetBlock.getMeta() < block.getMeta() - 1)) {
                                        liquidService.changeFlowingBlock(entity.getLevel(), block, position, targetBlock.getMeta(), BlockPrototype.FLOWING_LAVA);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{LavaModule.class};
    }

    @Override
    public LavaSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Entity) {
            return new LavaSystem((Entity) gameObject);
        }

        return null;
    }

    @Override
    public Class<LavaSystem> getSystemClass() {
        return LavaSystem.class;
    }
}
