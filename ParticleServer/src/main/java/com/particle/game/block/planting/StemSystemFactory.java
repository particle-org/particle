package com.particle.game.block.planting;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystem;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.Entity;
import com.particle.model.entity.component.farming.CropsModule;
import com.particle.model.entity.component.farming.PlantGrowUpProgressModule;
import com.particle.model.entity.component.farming.StemModule;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedList;
import java.util.List;

@Singleton
public class StemSystemFactory implements ECSSystemFactory<StemSystemFactory.StemSystem> {

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);
    private static final ECSModuleHandler<PlantGrowUpProgressModule> PLANT_GROW_UP_PROGRESS_MODULE_HANDLER = ECSModuleHandler.buildHandler(PlantGrowUpProgressModule.class);
    private static final ECSModuleHandler<CropsModule> CROPS_MODULE_HANDLER = ECSModuleHandler.buildHandler(CropsModule.class);
    private static final ECSModuleHandler<StemModule> STEM_MODULE_HANDLER = ECSModuleHandler.buildHandler(StemModule.class);

    @Inject
    private LevelService levelService;

    @Inject
    private EntitySpawnService entitySpawnService;

    public class StemSystem implements ECSSystem {

        private Entity entity;
        private TransformModule transformModule;
        private PlantGrowUpProgressModule plantGrowupProgressModule;
        private CropsModule cropsModule;
        private StemModule stemModule;

        public StemSystem(Entity entity) {
            this.entity = entity;
            this.transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);
            this.plantGrowupProgressModule = PLANT_GROW_UP_PROGRESS_MODULE_HANDLER.getModule(entity);
            this.cropsModule = CROPS_MODULE_HANDLER.getModule(entity);
            this.stemModule = STEM_MODULE_HANDLER.getModule(entity);
        }

        @Override
        public void tick(long deltaTime) {
            Vector3 position = new Vector3(transformModule.getPosition());

            Block stemBlock = levelService.getBlockAt(entity.getLevel(), position);

            long now = System.currentTimeMillis();
            BlockPrototype stemType = stemModule.getStemType();
            BlockPrototype fruitType = stemModule.getFruitType();

            if (stemBlock.getType() != stemType || stemBlock.getMeta() < cropsModule.getMaxGrowMeta() || plantGrowupProgressModule.getFutureUpdateTime() > now) {
                return;
            }

            List<Vector3> createPosList = new LinkedList<>();

            Vector3 west = position.west();
            if (checkFruitEnvironment(entity.getLevel(), west)) {
                createPosList.add(west);
            }

            Vector3 east = position.east();
            if (checkFruitEnvironment(entity.getLevel(), east)) {
                createPosList.add(east);
            }

            Vector3 north = position.north();
            if (checkFruitEnvironment(entity.getLevel(), north)) {
                createPosList.add(north);
            }

            Vector3 south = position.south();
            if (checkFruitEnvironment(entity.getLevel(), south)) {
                createPosList.add(south);
            }

            if (createPosList.size() > 0) {
                Vector3 fruitPos = createPosList.get((int) (Math.random() * createPosList.size()));
                Block fruit = Block.getBlock(fruitType);
                levelService.setBlockAt(entity.getLevel(), fruit, fruitPos);
                entity.getLevel().getLevelSchedule().scheduleSimpleTask("stemRemove", () -> entitySpawnService.despawnTileEntity(entity.getLevel(), position));
                return;
            }

            plantGrowupProgressModule.randomFutureUpdateTime();
        }

        private boolean isFruit(Level level, Vector3 position, BlockPrototype fruitType) {
            BlockPrototype block = levelService.getBlockTypeAt(level, position);
            return block == fruitType;
        }

        private boolean checkFruitEnvironment(Level level, Vector3 position) {
            BlockPrototype blockPrototype = levelService.getBlockTypeAt(level, position);
            if (blockPrototype != BlockPrototype.AIR) {
                return false;
            }

            Vector3 down = position.down();
            BlockPrototype type = levelService.getBlockTypeAt(level, down);
            return type == BlockPrototype.DIRT
                    || type == BlockPrototype.FARMLAND
                    || type == BlockPrototype.GRASS;
        }
    }

    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{
                StemModule.class,
                CropsModule.class,
                PlantGrowUpProgressModule.class,
                TransformModule.class
        };
    }

    @Override
    public StemSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Entity) {
            return new StemSystem((Entity) gameObject);
        }

        return null;
    }

    @Override
    public Class<StemSystem> getSystemClass() {
        return StemSystem.class;
    }
}
