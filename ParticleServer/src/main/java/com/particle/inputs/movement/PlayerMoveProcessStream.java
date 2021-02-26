package com.particle.inputs.movement;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.particle.game.entity.attribute.satisfaction.HungerService;
import com.particle.game.entity.movement.MovementServiceProxy;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.entity.spawn.EntitySpawnService;
import com.particle.game.utils.blueprint.BluePrintBuilder;
import com.particle.game.utils.blueprint.context.BackgroundContext;
import com.particle.game.utils.blueprint.node.RootNode;
import com.particle.game.utils.config.ServerConfigService;
import com.particle.game.world.level.LevelService;
import com.particle.model.events.level.player.PlayerMoveEvent;
import com.particle.model.math.Direction;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.MovePlayerPacket;
import com.particle.model.player.Player;
import com.particle.model.player.PositionMode;
import com.particle.network.handler.AbstractPacketHandler;

@Singleton
public class PlayerMoveProcessStream extends ProcessStream<MovePlayerPacket> {

    @Inject
    private EntitySpawnService entitySpawnService;

    @Inject
    private MovementServiceProxy movementServiceProxy;
    @Inject
    private PositionService positionService;
    @Inject
    private HungerService hungerService;

    @Inject
    private LevelService levelService;

    @Inject
    private ServerConfigService serverConfigService;

    private RootNode<PlayerMoveContext> rootNode;

    @Inject
    public void initProcessStream() {
        this.rootNode = new BluePrintBuilder<PlayerMoveContext>("PlayerMoveProcess")
                .check((context) -> movementServiceProxy.checkPlayerMove(context.getPlayer(), context.getPosition(), context.getDirection(), context.getPositionMode()))
                .event((context -> new PlayerMoveEvent(context.getPlayer(), context.getPosition(), context.getDirection())))
                .job((context -> hungerService.onPlayerMoveConsumption(context.getPlayer(), context.getPosition())))
                .job((context) -> positionService.setPositionAndDirection(context.getPlayer(), context.getPosition(), context.getDirection()))
                .job((context) -> entitySpawnService.respawn(context.getPlayer().getLevel(), context.getPlayer()))
                .onHandle()
                .job((context) -> movementServiceProxy.refreshPlayerPosition(context.getPlayer()))
                .onCancel()
                .dispatcher()
                .checked()
                .build();
    }

    @Override
    public int getTargetPacketID() {
        return ProtocolInfo.MOVE_PLAYER_PACKET;
    }

    @Override
    protected void process(Player player, MovePlayerPacket packet) {
        if (!AbstractPacketHandler.isChsVersion ||
                !serverConfigService.isMovementServerAuthoritative() ||
                player.getProtocolVersion() < AbstractPacketHandler.VERSION_1_16) {
            // 换算脚下坐标
            PlayerMoveContext playerMoveContext = new PlayerMoveContext(player);
            playerMoveContext.setPosition(packet.getVector3f().subtract(0, 1.62f, 0));
            playerMoveContext.setDirection(packet.getDirection());
            playerMoveContext.setPositionMode(packet.getMode());

            this.rootNode.run(playerMoveContext);
        }
    }

    private class PlayerMoveContext extends BackgroundContext {

        private Vector3f position;
        private Direction direction;
        private PositionMode positionMode;

        public PlayerMoveContext(Player player) {
            super(player);
        }

        public Vector3f getPosition() {
            return position;
        }

        public void setPosition(Vector3f position) {
            this.position = position;
        }

        public Direction getDirection() {
            return direction;
        }

        public void setDirection(Direction direction) {
            this.direction = direction;
        }

        public PositionMode getPositionMode() {
            return positionMode;
        }

        public void setPositionMode(PositionMode positionMode) {
            this.positionMode = positionMode;
        }
    }

    @Override
    protected boolean isRealTimeHandle() {
        return true;
    }
}
