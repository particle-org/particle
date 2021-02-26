package com.particle.game.serializations;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.serialization.IStringSerialization;
import com.particle.game.player.state.EntityGameModeModule;
import com.particle.model.player.GameMode;

public class GameModeSerialization implements IStringSerialization<EntityGameModeModule> {
    @Override
    public String serialization(GameObject gameObject, EntityGameModeModule entityGameModeModule) {
        return String.valueOf(entityGameModeModule.getGameMode().getMode());
    }

    @Override
    public void deserialization(GameObject gameObject, String data, EntityGameModeModule entityGameModeModule) {
        int gamemode = Integer.parseInt(data);

        entityGameModeModule.setGameMode(GameMode.valueOf(gamemode));
    }
}
