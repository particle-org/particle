package com.particle.game.entity.ai.branch;

import com.particle.api.ai.behavior.EStatus;
import com.particle.model.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RootSelecter extends FollowSelector {

    private static final Logger LOGGER = LoggerFactory.getLogger(RootSelecter.class);

    @Override
    public EStatus tick(Entity entity) {
        EStatus status = super.tick(entity);
        this.onTicked(entity, status);
        return status;
    }
}
