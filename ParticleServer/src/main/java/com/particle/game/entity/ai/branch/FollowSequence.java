package com.particle.game.entity.ai.branch;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IBehaviour;
import com.particle.model.entity.Entity;

public class FollowSequence extends BaseSequence {

    private EStatus status = EStatus.INITIALIZATION;

    @Override
    public EStatus tick(Entity entity) {
        for (IBehaviour behaviour : this.children) {
            EStatus status = behaviour.tick(entity);

            //若失败则直接返回
            if (status == EStatus.FAILURE || status == EStatus.ABORTED) {
                this.status = status;
                return this.status;
            }
        }

        this.status = EStatus.SUCCESS;
        return this.status;
    }

    @Override
    public void config(String key, Object val) {

    }
}
