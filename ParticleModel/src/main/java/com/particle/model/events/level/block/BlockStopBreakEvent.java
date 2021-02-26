package com.particle.model.events.level.block;


import com.particle.model.player.Player;

public class BlockStopBreakEvent extends LevelBlockEvent {
    public BlockStopBreakEvent(Player player) {
        super(player.getLevel());
    }
}
