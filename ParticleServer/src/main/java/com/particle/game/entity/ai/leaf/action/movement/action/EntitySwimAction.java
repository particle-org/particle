package com.particle.game.entity.ai.leaf.action.movement.action;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.ai.components.Knowledge;
import com.particle.game.entity.ai.service.EntityDecisionServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;

import javax.inject.Inject;

public class EntitySwimAction implements IAction {

    @Inject
    private LevelService levelService;

    @Inject
    private PositionService positionService;

    @Inject
    private EntityDecisionServiceProxy entityDecisionServiceProxy;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        Boolean isSwimming = entityDecisionServiceProxy.getKnowledge(entity, Knowledge.SWIMMING_STATUS, Boolean.class);
        if (isSwimming == null) {
            isSwimming = false;
        }

        Vector3f entityPosition = this.positionService.getPosition(entity);

        if (isSwimming) {
            this.positionService.setPosition(entity, entityPosition.add(0, 0.3f, 0));
        }

        // 水下检测
        BlockPrototype entityHeadBlock = this.levelService.getBlockTypeAt(entity.getLevel(), new Vector3(entityPosition.add(0, 1.5f, 0)));
        if (entityHeadBlock == BlockPrototype.WATER || entityHeadBlock == BlockPrototype.FLOWING_WATER) {
            if (!isSwimming) {
                entityDecisionServiceProxy.addKnowledge(entity, Knowledge.SWIMMING_STATUS, true);
            }

            return EStatus.SUCCESS;
        }


        entityHeadBlock = this.levelService.getBlockTypeAt(entity.getLevel(), new Vector3(entityPosition.add(0, 0.5f, 0)));
        if (entityHeadBlock != BlockPrototype.WATER && entityHeadBlock != BlockPrototype.FLOWING_WATER) {
            if (isSwimming) {
                entityDecisionServiceProxy.addKnowledge(entity, Knowledge.SWIMMING_STATUS, false);
            }
        }

        return EStatus.SUCCESS;
    }

    @Override
    public void onTicked(Entity entity, EStatus status) {

    }

    @Override
    public void onRelease() {

    }

    @Override
    public void config(String key, Object val) {

    }
}
