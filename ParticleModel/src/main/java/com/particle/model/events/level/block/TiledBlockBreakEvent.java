package com.particle.model.events.level.block;

import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.level.Level;
import com.particle.model.player.Player;

public class TiledBlockBreakEvent extends LevelBlockEvent {

    /**
     * 玩家
     */
    private Player player;

    private TileEntity tileEntity;

    public TiledBlockBreakEvent(Level level) {
        super(level);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public TileEntity getTileEntity() {
        return tileEntity;
    }

    public void setTileEntity(TileEntity tileEntity) {
        this.tileEntity = tileEntity;
    }
}
