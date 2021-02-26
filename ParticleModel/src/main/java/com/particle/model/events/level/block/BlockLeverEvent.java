package com.particle.model.events.level.block;

import com.particle.model.level.Level;
import com.particle.model.math.BlockLeverFace;
import com.particle.model.math.Vector3;
import com.particle.model.player.Player;

public class BlockLeverEvent extends LevelBlockEvent {
    /**
     * 玩家
     */
    private Player player;

    private boolean leverSwitch;

    private BlockLeverFace blockLeverFace;

    private Vector3 position;

    public BlockLeverEvent(Level level) {
        super(level);
    }

    public boolean isLeverSwitch() {
        return leverSwitch;
    }

    public void setLeverSwitch(boolean leverSwitch) {
        this.leverSwitch = leverSwitch;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public BlockLeverFace getBlockLeverFace() {
        return blockLeverFace;
    }

    public void setBlockLeverFace(BlockLeverFace blockLeverFace) {
        this.blockLeverFace = blockLeverFace;
    }

    @Override
    public Vector3 getPosition() {
        return position;
    }

    @Override
    public void setPosition(Vector3 position) {
        this.position = position;
    }
}
