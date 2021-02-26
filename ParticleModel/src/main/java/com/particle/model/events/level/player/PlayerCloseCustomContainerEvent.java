package com.particle.model.events.level.player;

import com.particle.model.player.Player;

public class PlayerCloseCustomContainerEvent extends LevelPlayerEvent {

    private int containerId;

    public PlayerCloseCustomContainerEvent(Player player, int containerId) {
        super(player);
        this.containerId = containerId;
    }

    public int getContainerId() {
        return containerId;
    }
}
