package com.particle.model.ui.score;

public class ScorePacketInfo {

    /**
     * 自增的boardId
     */
    private static long BOARD_ID = 0;

    /**
     * 唯一性
     */
    private long scoreBoardId;

    private String objectiveName;

    private int scoreValue;

    /**
     * 类型
     */
    private IdentityDefinitionType identityDefinitionType;

    /**
     * depends on #{link} IdentityDefinitionType
     * if 0, valid
     */
    private long playerUniqueId;

    /**
     * depends on #{link} IdentityDefinitionType
     * if 1, valid
     */
    private long actorId;

    /**
     * depends on #{link} IdentityDefinitionType
     * if 2, valid
     */
    private String fakePlayerName;

    public ScorePacketInfo() {
        this.scoreBoardId = BOARD_ID++;
    }

    public ScorePacketInfo(long scoreBoardId) {
        this.scoreBoardId = scoreBoardId;
    }

    public long getScoreBoardId() {
        return scoreBoardId;
    }

    public String getObjectiveName() {
        return objectiveName;
    }

    public void setObjectiveName(String objectiveName) {
        this.objectiveName = objectiveName;
    }

    public int getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(int scoreValue) {
        this.scoreValue = scoreValue;
    }

    public IdentityDefinitionType getIdentityDefinitionType() {
        return identityDefinitionType;
    }

    public void setIdentityDefinitionType(IdentityDefinitionType identityDefinitionType) {
        this.identityDefinitionType = identityDefinitionType;
    }

    public long getPlayerUniqueId() {
        return playerUniqueId;
    }

    public void setPlayerUniqueId(long playerUniqueId) {
        this.playerUniqueId = playerUniqueId;
    }

    public long getActorId() {
        return actorId;
    }

    public void setActorId(long actorId) {
        this.actorId = actorId;
    }

    public String getFakePlayerName() {
        return fakePlayerName;
    }

    public void setFakePlayerName(String fakePlayerName) {
        this.fakePlayerName = fakePlayerName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (!(obj instanceof ScorePacketInfo)) {
            return false;
        }
        return this.scoreBoardId == ((ScorePacketInfo) obj).getScoreBoardId();
    }


    @Override
    public int hashCode() {
        return (int) (scoreBoardId % Integer.MAX_VALUE);
    }
}
