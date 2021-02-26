package com.particle.inputs.movement;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.entity.attribute.satisfaction.HungerService;
import com.particle.game.entity.movement.MovementServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.inputs.PlayerPacketHandle;
import com.particle.model.events.level.player.PlayerMoveEvent;
import com.particle.model.math.Direction;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.MovePlayerPacket;
import com.particle.model.player.Player;
import com.particle.model.player.PositionMode;

@Singleton
public class PlayerSimpleMoveProcessStream extends PlayerPacketHandle<MovePlayerPacket> {

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Inject
    private EntitySpawnService entitySpawnService;

    @Inject
    private MovementServiceProxy movementServiceProxy;
    @Inject
    private PositionService positionService;
    @Inject
    private HungerService hungerService;


    @Override
    public int getTargetPacketID() {
        return ProtocolInfo.MOVE_PLAYER_PACKET;
    }

    @Override
    public void handlePacket(Player player, MovePlayerPacket packet) {
        // 换算脚下坐标
        Vector3f position = packet.getVector3f().subtract(0, 1.62f, 0);
        Direction direction = packet.getDirection();
        PositionMode gamemode = packet.getMode();

        if (!this.movementServiceProxy.checkPlayerMove(player, position, direction, gamemode)) {
            return;
        }

        PlayerMoveEvent playerMoveEvent = new PlayerMoveEvent(player, position, direction);
        this.eventDispatcher.dispatchEvent(playerMoveEvent);

        if (playerMoveEvent.isCancelled()) {
            this.movementServiceProxy.refreshPlayerPosition(player);
            return;
        }

        this.hungerService.onPlayerMoveConsumption(player, position);
        this.positionService.setPositionAndDirection(player, position, direction);
        this.entitySpawnService.respawn(player.getLevel(), player);
    }
}
