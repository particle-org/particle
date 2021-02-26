package com.particle.model.ui.score;

public class ScoreIdentityInfo {

    private long scoreBoardId;

    /**
     * depends on #{link}ScoreboardIdentityPacketType
     * if 0, no data
     * if 1, playerUniqueId
     */
    private long playerUniqueId;

    public long getScoreBoardId() {
        return scoreBoardId;
    }

    public void setScoreBoardId(long scoreBoardId) {
        this.scoreBoardId = scoreBoardId;
    }

    public long getPlayerUniqueId() {
        return playerUniqueId;
    }

    public void setPlayerUniqueId(long playerUniqueId) {
        this.playerUniqueId = playerUniqueId;
    }
}
