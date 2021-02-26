package com.particle.game.entity.ai.leaf.action;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.attribute.metadata.MetaDataService;
import com.particle.game.world.animation.EntityAnimationService;
import com.particle.model.entity.Entity;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.metadata.MetadataDataFlag;
import com.particle.model.network.packets.data.EntityEventPacket;

import javax.inject.Inject;
import java.util.Random;

public class EntityGrowAction implements IAction {

    private int growRate = 10;

    @Inject
    private Random random;

    @Inject
    private MetaDataService metaDataService;

    @Inject
    private EntityAnimationService entityAnimationService;

    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        int rand = random.nextInt(100);
        if (rand < growRate) {
            metaDataService.setDataFlag(entity, MetadataDataFlag.DATA_FLAG_BABY, false, true);
            metaDataService.setFloatData(entity, EntityMetadataType.SCALE, 1f, true);
        }

        entityAnimationService.sendAnimation(entity, EntityEventPacket.BABY_EAT);

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
        if (key.equalsIgnoreCase("growRate") && val instanceof Integer) {
            growRate = (int) val;
        }
    }
}