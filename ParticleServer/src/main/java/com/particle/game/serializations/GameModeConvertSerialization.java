package com.particle.game.serializations;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.serialization.IStringSerialization;
import com.particle.game.player.state.EntityGameModeModule;
import com.particle.model.player.GameMode;

public class GameModeConvertSerialization implements IStringSerialization<EntityGameModeModule> {
    @Override
    public String serialization(GameObject gameObject, EntityGameModeModule entityGameModeModule) {
        return "";
    }

    @Override
    public void deserialization(GameObject gameObject, String data, EntityGameModeModule entityGameModeModule) {
        int gamemode = Integer.parseInt(data.substring(data.length() - 1));

        entityGameModeModule.setGameMode(GameMode.valueOf(gamemode));
    }
}
