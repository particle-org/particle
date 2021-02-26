package com.particle.game.player.uuid.model;

public class PlayerBaseInfo {
    // 玩家uuid
    private String playerUuid;
    // 玩家名称
    private String playerName;
    // 玩家角色id
    private long roleId;
    // 最近登录时间
    private long lastLoginTime;

    public PlayerBaseInfo(String str) {
        String[] arr = str.split("#");
        this.playerUuid = arr[0];
        this.playerName = arr[1];
        this.roleId = Long.valueOf(arr[2]);
        this.lastLoginTime = Long.valueOf(arr[3]);
    }

    public PlayerBaseInfo(String playerUuid, String playerName, long roleId, long lastLoginTime) {
        this.playerUuid = playerUuid;
        this.playerName = playerName;
        this.roleId = roleId;
        this.lastLoginTime = lastLoginTime;
    }

    public String getPlayerUuid() {
        return playerUuid;
    }

    public void setPlayerUuid(String playerUuid) {
        this.playerUuid = playerUuid;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public long getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    @Override
    public String toString() {
        return String.format("%s#%s#%s#%s", playerUuid, playerName, roleId, lastLoginTime);
    }
}
