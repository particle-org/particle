package com.particle.game.player.permission;

import com.particle.api.permission.PermissionApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.player.PlayerService;
import com.particle.game.server.Server;
import com.particle.model.entity.component.player.PermissionModule;
import com.particle.model.permission.CommandPermissionConstant;
import com.particle.model.permission.NodeTree;
import com.particle.model.player.Player;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Singleton
public class PermissionService implements PermissionApi {

    private static final Logger logger = LoggerFactory.getLogger(PermissionService.class);

    private static final ECSModuleHandler<PermissionModule> PERMISSION_MODULE_HANDLER = ECSModuleHandler.buildHandler(PermissionModule.class);

    @Inject
    private PlayerService playerService;

    @Inject
    private Server server;

    @Override
    public NodeTree getPermissions(Player player) {
        PermissionModule permissionModule = PERMISSION_MODULE_HANDLER.getModule(player);
        return permissionModule.getPermissions();
    }


    @Override
    public boolean addPermissionToPlayer(Player player, String permission) {
        PermissionModule permissionModule = PERMISSION_MODULE_HANDLER.getModule(player);
        permissionModule.addPermission(permission);
        return true;
    }

    @Override
    public boolean removePermissionFromPlayer(Player player, String permission) {
        PermissionModule permissionModule = PERMISSION_MODULE_HANDLER.getModule(player);
        permissionModule.removePermission(permission);
        return true;
    }

    @Override
    public void reload(Player player) {
        Map<String, Boolean> permissions = new HashMap<>();
        if (isOp(player)) {
            permissions.put(CommandPermissionConstant.CONSOLE, true);
            permissions.put(CommandPermissionConstant.GAME_MODE, true);
            permissions.put(CommandPermissionConstant.GIVE, true);
            permissions.put(CommandPermissionConstant.CLEAR, true);
            permissions.put(CommandPermissionConstant.KILL, true);
            permissions.put(CommandPermissionConstant.PRISON, true);
            permissions.put(CommandPermissionConstant.KICK, true);
            permissions.put(CommandPermissionConstant.PERMISSION, true);
            permissions.put(CommandPermissionConstant.SAY, true);
            permissions.put(CommandPermissionConstant.SWITCH, true);
            permissions.put(CommandPermissionConstant.TIME, true);
            permissions.put(CommandPermissionConstant.XP, true);
            permissions.put(CommandPermissionConstant.TEST, true);
            permissions.put(CommandPermissionConstant.TELEPORT, true);
            permissions.put(CommandPermissionConstant.SCOREBOARD, true);
            permissions.put(CommandPermissionConstant.PLUGIN, true);
        }
        PermissionModule permissionModule = PERMISSION_MODULE_HANDLER.getModule(player);
        permissionModule.setPermissions(NodeTree.of(permissions));
    }

    @Override
    public void reloadAll() {
        Collection<Player> allPlayers = this.server.getAllPlayers();
        if (!CollectionUtils.isEmpty(allPlayers)) {
            for (Player player : allPlayers) {
                reload(player);
            }
        }
    }

    @Override
    public boolean hasPermission(Player player, String permission) {
        return PERMISSION_MODULE_HANDLER.getModule(player).hasPermission(permission);
    }

    @Override
    public boolean hasPermission(Player player, Set<String> permissions) {
        return PERMISSION_MODULE_HANDLER.getModule(player).hasAllPermission(permissions);
    }

    @Override
    public boolean isOp(Player player) {
        return PERMISSION_MODULE_HANDLER.getModule(player).isOp();
    }
}
