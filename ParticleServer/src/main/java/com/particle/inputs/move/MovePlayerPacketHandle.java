package com.particle.inputs.move;

import com.particle.game.entity.movement.MovementServiceProxy;
import com.particle.game.utils.config.ServerConfigService;
import com.particle.inputs.PlayerPacketHandle;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.MovePlayerPacket;
import com.particle.model.player.Player;
import com.particle.network.handler.AbstractPacketHandler;

import javax.inject.Inject;

public class MovePlayerPacketHandle extends PlayerPacketHandle<MovePlayerPacket> {

    @Inject
    private MovementServiceProxy movementServiceProxy;

    @Inject
    private ServerConfigService serverConfigService;

    @Override
    public int getTargetPacketID() {
        return ProtocolInfo.MOVE_PLAYER_PACKET;
    }

    @Override
    protected void handlePacket(Player player, MovePlayerPacket packet) {
        // 若沒有開 將有正常移動包, 或者是国际版本
        if (!serverConfigService.isMovementServerAuthoritative() || !AbstractPacketHandler.isChsVersion) {
            movementServiceProxy.checkPlayerMove(player, packet.getVector3f(), packet.getDirection(), packet.getMode());
        }
    }
}
