package com.particle.game.entity.ai.branch;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IBehaviour;
import com.particle.model.entity.Entity;

public class FollowSelector extends BaseSelector {

    private EStatus status = EStatus.INITIALIZATION;

    @Override
    public EStatus tick(Entity entity) {
        for (IBehaviour behaviour : super.children) {
            EStatus status = behaviour.tick(entity);

            //若成功则直接返回
            if (status == EStatus.SUCCESS || status == EStatus.ABORTED) {
                this.status = status;
                return this.status;
            }
        }

        this.status = EStatus.FAILURE;
        return this.status;
    }

    @Override
    public void config(String key, Object val) {

    }
}
