package com.particle.game.player.patch;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.particle.event.dispatcher.EventRank;
import com.particle.event.handle.AbstractLevelEventHandle;
import com.particle.game.player.PlayerService;
import com.particle.game.world.level.LevelService;
import com.particle.model.events.annotation.EventHandler;
import com.particle.model.events.level.player.PlayerMoveEvent;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;

@EventHandler
@Singleton
public class PlayerIllegalMoveCheck extends AbstractLevelEventHandle<PlayerMoveEvent> {

    @Inject
    private LevelService levelService;

    @Inject
    private PlayerService playerService;

    @Override
    protected void onHandle(Level level, PlayerMoveEvent playerMoveEvent) {
        if (playerMoveEvent.getPosition().getY() < 0) {
            Vector3f playerPosition = playerMoveEvent.getPosition();

            int topBlockHeightBelow = levelService.getTopBlockHeightBelow(level, new Vector3(playerPosition.getFloorX(), 255, playerPosition.getFloorZ()));

            playerPosition.setY(topBlockHeightBelow + 1);

            playerService.teleport(playerMoveEvent.getPlayer(), new Vector3f(level.getLevelSettings().getSpawn()));
        }
    }

    @Override
    public Class getListenerEvent() {
        return PlayerMoveEvent.class;
    }

    @Override
    public EventRank getEventRank() {
        return EventRank.DEFAULT;
    }
}
