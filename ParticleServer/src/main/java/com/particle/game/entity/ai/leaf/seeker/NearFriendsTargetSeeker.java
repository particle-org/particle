package com.particle.game.entity.ai.leaf.seeker;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.world.level.ChunkService;
import com.particle.model.entity.Entity;
import com.particle.model.entity.model.mob.MobEntity;
import com.particle.model.entity.model.others.MonsterEntity;
import com.particle.model.level.Chunk;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

import javax.inject.Inject;

public class NearFriendsTargetSeeker implements IAction {

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Inject
    private PositionService positionService;

    @Inject
    private ChunkService chunkService;

    private int friendsNetworkId;


    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        Vector3f entityPosition = this.positionService.getPosition(entity);

        int currentX = entityPosition.getFloorX() / 16;
        int currentZ = entityPosition.getFloorZ() / 16;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                Chunk chunk = this.chunkService.getChunk(entity.getLevel(), currentX - 1 + i, currentZ - 1 + i, false);

                for (MobEntity mobEntity : chunk.getMobEntitiesCollection().getEntitiesViewer()) {
                    if (mobEntity.getNetworkId() == this.friendsNetworkId) {
                        Player friendsTarget = this.entityDecisionServiceProxy.getKnowledge(mobEntity, Knowledge.ENTITY_TARGET, Player.class);

                        if (friendsTarget != null) {
                            this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.ENTITY_TARGET, friendsTarget);

                            return EStatus.SUCCESS;
                        }
                    }
                }

                for (MonsterEntity monsterEntity : chunk.getMonsterEntitiesCollection().getEntitiesViewer()) {
                    if (monsterEntity.getNetworkId() == this.friendsNetworkId) {
                        Player friendsTarget = this.entityDecisionServiceProxy.getKnowledge(monsterEntity, Knowledge.ENTITY_TARGET, Player.class);

                        if (friendsTarget != null) {
                            this.entityDecisionServiceProxy.addKnowledge(entity, Knowledge.ENTITY_TARGET, friendsTarget);

                            return EStatus.SUCCESS;
                        }
                    }
                }
            }
        }

        return EStatus.FAILURE;
    }

    @Override
    public void onTicked(Entity entity, EStatus status) {

    }

    @Override
    public void onRelease() {

    }

    @Override
    public void config(String key, Object val) {
        if (key.equals("FriendsNetworkId") && val.getClass() == Integer.class) {
            this.friendsNetworkId = (int) val;
        }
    }

}
