package com.particle.event.handle;

import com.particle.model.events.BaseEvent;
import com.particle.model.events.level.player.PlayerQuitGameEvent;
import com.particle.model.level.Level;
import com.particle.model.level.LevelStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractLevelEventHandle<T extends BaseEvent> implements LevelEventHandle {

    private static final Logger logger = LoggerFactory.getLogger(AbstractLevelEventHandle.class);

    @Override
    public void handle(Level level, BaseEvent baseEvent) {
        // 只有当level的status正常且非玩家退出的时候，事件才发触发
        LevelStatus levelStatus = level.getLevelStatus();
        if ((levelStatus == LevelStatus.DESTROYING || levelStatus == LevelStatus.CLOSED) && !(baseEvent instanceof PlayerQuitGameEvent)) {
            logger.warn("run level event task, the level status is {}!", levelStatus);
            return;
        }

        this.onHandle(level, (T) baseEvent);
    }

    protected abstract void onHandle(Level level, T t);
}
