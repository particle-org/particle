package com.particle.game.server.command.impl;

import com.particle.api.command.CommandSource;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.model.entity.component.player.PermissionModule;
import com.particle.model.network.packets.data.TextPacket;
import com.particle.model.permission.CommandPermissionConstant;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;

import java.util.Set;

public class PlayerCommandSource implements CommandSource {

    private static final ECSModuleHandler<PermissionModule> PERMISSION_MODULE_HANDLER = ECSModuleHandler.buildHandler(PermissionModule.class);

    private Player player;

    private NetworkManager networkManager;

    public PlayerCommandSource(Player player, NetworkManager networkManager) {
        this.player = player;
        this.networkManager = networkManager;
    }

    @Override
    public long getClientId() {
        return player.getRuntimeId();
    }

    @Override
    public void sendMessage(String message) {
        TextPacket textPacket = new TextPacket();
        textPacket.setMessageType(TextPacket.RawType);
        textPacket.setMessage(message);
        networkManager.sendMessage(player.getClientAddress(), textPacket);
    }

    @Override
    public void sendError(String message) {
        TextPacket textPacket = new TextPacket();
        textPacket.setMessageType(TextPacket.RawType);
        textPacket.setMessage(message);
        networkManager.sendMessage(player.getClientAddress(), textPacket);
    }

    @Override
    public boolean hasPermission(String permission) {
        // 控制台权限
        if (CommandPermissionConstant.CONSOLE.equals(permission)) {
            return false;
        }
        return PERMISSION_MODULE_HANDLER.getModule(player).hasPermission(permission);
    }

    @Override
    public boolean hasPermission(Set<String> permissions) {
        if (permissions == null || permissions.isEmpty()) {
            return true;
        }
        // 控制台权限
        if (permissions.contains(CommandPermissionConstant.CONSOLE)) {
            return false;
        }
        return PERMISSION_MODULE_HANDLER.getModule(player).hasAllPermission(permissions);
    }

    /**
     * 获取玩家
     *
     * @return
     */
    @Override
    public Player getPlayer() {
        return player;
    }
}
