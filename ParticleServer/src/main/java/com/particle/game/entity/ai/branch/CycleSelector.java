package com.particle.game.entity.ai.branch;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IBehaviour;
import com.particle.model.entity.Entity;

public class CycleSelector extends BaseSelector {

    private EStatus status = EStatus.INITIALIZATION;

    private int lastIndex = 0;

    @Override
    public EStatus tick(Entity entity) {
        int index = this.lastIndex + 1;
        if (index == super.children.size())
            index = 0;

        try {
            do {
                IBehaviour behaviour = super.children.get(index);

                EStatus status = behaviour.tick(entity);

                //若成功则直接返回
                if (status == EStatus.SUCCESS || status == EStatus.ABORTED) {
                    this.status = status;
                    return this.status;
                }

                if (++index == super.children.size())
                    index = 0;
            } while (index != this.lastIndex);
        } finally {
            this.lastIndex = index;
        }

        this.status = EStatus.FAILURE;
        return this.status;
    }

    @Override
    public void config(String key, Object val) {

    }
}
