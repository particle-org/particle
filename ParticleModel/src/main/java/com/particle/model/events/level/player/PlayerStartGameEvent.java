package com.particle.model.events.level.player;

import com.particle.model.level.Level;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

public class PlayerStartGameEvent extends LevelPlayerEvent {

    private Vector3f position;

    public PlayerStartGameEvent(Player player, Level level, Vector3f position) {
        super(player, level);
        this.position = position;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }
}
