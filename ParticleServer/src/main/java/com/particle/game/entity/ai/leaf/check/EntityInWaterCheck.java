package com.particle.game.entity.ai.leaf.check;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.ICondition;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.element.BlockElement;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;

import javax.inject.Inject;

public class EntityInWaterCheck implements ICondition {

    @Inject
    private PositionService positionService;

    @Inject
    private LevelService levelService;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        Vector3f position = this.positionService.getPosition(entity);

        BlockPrototype blockAt = this.levelService.getBlockTypeAt(entity.getLevel(), new Vector3(position));

        if (blockAt.getBlockElement().equals(BlockElement.LIQUID)) {
            return EStatus.SUCCESS;
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

    }
}
