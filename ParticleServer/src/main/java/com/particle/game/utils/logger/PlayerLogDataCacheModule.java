package com.particle.game.utils.logger;

import com.particle.core.ecs.module.BehaviorModule;

public class PlayerLogDataCacheModule extends BehaviorModule {

    // 游戏账号的唯一标示符 取自ChainData的clientUUID
    private String uuid;
    // 设备UDID号 取自ChainData的deviceModel
    private String udid;
    // 角色唯一标识，在所有服唯一 取自ChainData的xuid
    private String roleId;
    // 游戏角色名称，多服可重复，单服唯一 取自ChainData的username
    private String roleName;
    // 客户端ip
    private String address;
    // 客户端端口
    private String port;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = String.valueOf(port);
    }
}
