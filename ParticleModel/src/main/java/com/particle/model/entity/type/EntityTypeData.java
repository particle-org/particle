package com.particle.model.entity.type;

public class EntityTypeData {

    private int type;

    private String actorType;

    private boolean hasSpawnEgg;
    private boolean experimental;
    private boolean summonable;
    private String bid;

    public EntityTypeData(int type, String actorType, boolean hasSpawnEgg, boolean experimental, boolean summonable, String bid) {
        this.type = type;
        this.actorType = actorType;
        this.hasSpawnEgg = hasSpawnEgg;
        this.experimental = experimental;
        this.summonable = summonable;
        this.bid = bid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getActorType() {
        return actorType;
    }

    public void setActorType(String actorType) {
        this.actorType = actorType;
    }

    public boolean hasSpawnEgg() {
        return hasSpawnEgg;
    }

    public void setHasSpawnEgg(boolean hasSpawnEgg) {
        this.hasSpawnEgg = hasSpawnEgg;
    }

    public boolean isExperimental() {
        return experimental;
    }

    public void setExperimental(boolean experimental) {
        this.experimental = experimental;
    }

    public boolean isSummonable() {
        return summonable;
    }

    public void setSummonable(boolean summonable) {
        this.summonable = summonable;
    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }
}
