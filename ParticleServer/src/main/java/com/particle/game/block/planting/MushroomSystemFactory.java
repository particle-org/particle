package com.particle.game.block.planting;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystem;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.game.common.modules.TransformModule;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.Entity;
import com.particle.model.entity.component.farming.MushroomModule;
import com.particle.model.level.Level;
import com.particle.model.math.MathUtils;
import com.particle.model.math.Vector3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Random;

@Singleton
public class MushroomSystemFactory implements ECSSystemFactory<MushroomSystemFactory.MushroomSystem> {

    private static final Logger LOGGER = LoggerFactory.getLogger(MushroomSystemFactory.class);

    private static final ECSModuleHandler<TransformModule> TRANSFORM_MODULE_HANDLER = ECSModuleHandler.buildHandler(TransformModule.class);

    @Inject
    private LevelService levelService;

    @Inject
    private MushroomService mushroomService;

    private final Random random = new Random();

    public class MushroomSystem implements ECSSystem {

        private Entity entity;
        private TransformModule transformModule;

        public MushroomSystem(Entity entity) {
            this.entity = entity;
            this.transformModule = TRANSFORM_MODULE_HANDLER.getModule(entity);
        }

        @Override
        public void tick(long deltaTime) {
            Vector3 targetPosition = new Vector3(transformModule.getPosition());
            BlockPrototype mushroomBlock = levelService.getBlockTypeAt(entity.getLevel(), targetPosition);
            if (mushroomBlock == null ||
                    (mushroomBlock != BlockPrototype.BROWN_MUSHROOM
                            && mushroomBlock != BlockPrototype.RED_MUSHROOM)) {
                return;
            }

            // 尝试在附近生成新的蘑菇
            tryCreateMushroomAround(entity.getLevel(), mushroomBlock, targetPosition);
        }

        private void tryCreateMushroomAround(Level level, BlockPrototype mushroomBlock, Vector3 mushroomPosition) {
            if (getMushroomNumAround(level, mushroomPosition) >= 5 || random.nextDouble() > 0.01) {
                return;
            }

            Vector3 targetPosition = mushroomPosition;

            for (int i = 0; i < 2; i++) {
                int randomX = targetPosition.getX() + MathUtils.getRandomNumberInRange(random, -1, 1);

                int randomY;
                double rand = random.nextDouble();
                if (rand < 0.5) {
                    randomY = 0;
                } else if (rand < 0.75) {
                    randomY = 1;
                } else {
                    randomY = -1;
                }

                int randomZ = targetPosition.getZ() + MathUtils.getRandomNumberInRange(random, -1, 1);

                Vector3 newPosition = new Vector3(randomX, randomY, randomZ);
                BlockPrototype targetBlock = levelService.getBlockTypeAt(level, newPosition);
                if (targetBlock == BlockPrototype.AIR
                        && mushroomService.checkEnvironment(level, newPosition)) {
                    targetPosition = newPosition;
                } else {
                    targetPosition = null;
                    break;
                }
            }

            if (targetPosition != null) {
                BlockPrototype targetBlock = levelService.getBlockTypeAt(level, targetPosition);
                if (targetBlock == BlockPrototype.AIR
                        && mushroomService.checkEnvironment(level, targetPosition)) {
                    levelService.setBlockAt(level, Block.getBlock(mushroomBlock), targetPosition);
                }
            }
        }

        private int getMushroomNumAround(Level level, Vector3 targetPosition) {
            int num = 0;

            for (int offsetX = -4; offsetX <= 4; offsetX++) {
                for (int offsetZ = -4; offsetZ <= 4; offsetZ++) {
                    for (int offsetY = -1; offsetY <= 1; offsetY++) {
                        int x = targetPosition.getX() + offsetX;
                        int y = targetPosition.getY() + offsetY;
                        int z = targetPosition.getZ() + offsetZ;

                        BlockPrototype blockAround = levelService.getBlockTypeAt(level, x, y, z);
                        if (blockAround == BlockPrototype.RED_MUSHROOM
                                || blockAround == BlockPrototype.BROWN_MUSHROOM) {
                            num++;
                        }
                    }
                }
            }

            return num;
        }
    }

    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{MushroomModule.class, TransformModule.class};
    }

    @Override
    public MushroomSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Entity) {
            return new MushroomSystem((Entity) gameObject);
        }

        return null;
    }

    @Override
    public Class<MushroomSystem> getSystemClass() {
        return MushroomSystem.class;
    }
}
