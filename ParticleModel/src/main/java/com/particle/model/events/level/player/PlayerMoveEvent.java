package com.particle.model.events.level.player;

import com.particle.model.math.Direction;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

public class PlayerMoveEvent extends LevelPlayerEvent {
    private Vector3f position;
    private Direction direction;

    public PlayerMoveEvent(Player player) {
        super(player);
    }

    public PlayerMoveEvent(Player player, Vector3f position, Direction direction) {
        super(player);
        this.position = position;
        this.direction = direction;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
