package com.particle.game.player.state;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.player.GameMode;

public class EntityGameModeModule extends BehaviorModule {

    private GameMode gameMode = GameMode.SURVIVE;

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }
}
