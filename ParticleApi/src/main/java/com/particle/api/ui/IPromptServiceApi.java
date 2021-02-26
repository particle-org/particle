package com.particle.api.ui;

import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

public interface IPromptServiceApi {
    void prompt(Player player, Vector3f position, long ttl, String... content);
}
