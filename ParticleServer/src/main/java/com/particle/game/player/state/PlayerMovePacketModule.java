package com.particle.game.player.state;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.math.Direction;
import com.particle.model.math.Vector3f;

public class PlayerMovePacketModule extends BehaviorModule {

    private Vector3f position = new Vector3f(0, 0, 0);
    private Direction direction = new Direction(0, 0, 0);

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
