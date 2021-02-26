package com.particle.game.world.physical;

import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.world.level.ChunkService;
import com.particle.game.world.level.LevelService;
import com.particle.game.world.physical.collider.AABBCollider;
import com.particle.game.world.physical.collider.PointCollider;
import com.particle.game.world.physical.modules.BoxColliderModule;
import com.particle.game.world.physical.modules.PointColliderModule;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.Entity;
import com.particle.model.level.Chunk;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.LinkedHashSet;
import java.util.Set;

@Singleton
public class BlockColliderCheckService {

    private static final ECSModuleHandler<BoxColliderModule> BOX_COLLIDER_MODULE_HANDLER = ECSModuleHandler.buildHandler(BoxColliderModule.class);
    private static final ECSModuleHandler<PointColliderModule> POINT_COLLIDER_MODULE_HANDLER = ECSModuleHandler.buildHandler(PointColliderModule.class);

    @Inject
    private LevelService levelService;
    @Inject
    private ChunkService chunkService;

    @Inject
    private PositionService positionService;

    /**
     * 检查指定位置是否有生物发生碰撞
     *
     * @param level
     * @param position
     * @return
     */
    public boolean checkEntityEnter(Level level, Vector3 position) {
        Chunk chunk = this.chunkService.indexChunk(level, position);

        for (Player player : chunk.getPlayersCollection().getEntitiesViewer()) {
            if (doCheckEnter(player, position)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 检测与该生物碰撞的方块
     *
     * @param entity
     * @return
     */
    public Set<BlockPrototype> checkEntityColliderWithBlocks(Entity entity) {
        Vector3f position = this.positionService.getPosition(entity);

        BoxColliderModule boxColliderModule = BOX_COLLIDER_MODULE_HANDLER.getModule(entity);
        if (boxColliderModule != null) {
            // 如果是AABB碰撞箱，需要确定所有的方块
            AABBCollider aabbCollider = boxColliderModule.getAABBCollider();

            Vector3f center = position.add(aabbCollider.getCenter());
            int xmin = (int) Math.floor(center.getX() - aabbCollider.getSize().getX() / 2);
            int xmax = (int) Math.floor(center.getX() + aabbCollider.getSize().getX() / 2);
            int ymin = (int) (Math.floor(center.getY() - aabbCollider.getSize().getY() / 2));
            int ymax = (int) (Math.floor(center.getY() + aabbCollider.getSize().getY() / 2));
            int zmin = (int) Math.floor(center.getZ() - aabbCollider.getSize().getZ() / 2);
            int zmax = (int) Math.floor(center.getZ() + aabbCollider.getSize().getZ() / 2);

            Set<BlockPrototype> blockPrototypes = new LinkedHashSet<>();
            for (int x = xmin; x <= xmax; x++) {
                for (int y = ymin; y <= ymax; y++) {
                    for (int z = zmin; z <= zmax; z++) {
                        blockPrototypes.add(this.levelService.getBlockTypeAt(entity.getLevel(), x, y, z));
                    }
                }
            }
            return blockPrototypes;
        }

        PointColliderModule pointColliderModule = POINT_COLLIDER_MODULE_HANDLER.getModule(entity);
        if (pointColliderModule != null) {
            // 如果是点碰撞箱，只需要确定目标地点的方块
            PointCollider pointCollider = pointColliderModule.getPointCollider();

            Vector3f centerPosition = position.add(pointCollider.getCenter());

            BlockPrototype blockTypeAt = this.levelService.getBlockTypeAt(entity.getLevel(), centerPosition.getFloorX(), centerPosition.getFloorY(), centerPosition.getFloorZ());

            Set<BlockPrototype> blockPrototypes = new LinkedHashSet<>();
            blockPrototypes.add(blockTypeAt);
            return blockPrototypes;
        }

        return null;
    }

    private boolean doCheckEnter(Entity entity, Vector3 position) {
        BoxColliderModule boxColliderModule = BOX_COLLIDER_MODULE_HANDLER.getModule(entity);
        if (boxColliderModule == null) {
            return false;
        }

        AABBCollider colliderFrom = boxColliderModule.getAABBCollider();
        Vector3f offset = new Vector3f(position).add(0.5f, 0.5f, 0.5f).subtract(colliderFrom.getCenter().add(this.positionService.getPosition(entity)));
        return (Math.abs(offset.getX()) < colliderFrom.getXPadding()
                && Math.abs(offset.getY()) < colliderFrom.getYPadding()
                && Math.abs(offset.getZ()) < colliderFrom.getZPadding());
    }

}
