package com.particle.inputs.connector;

import com.particle.api.ServerApi;
import com.particle.game.player.PlayerService;
import com.particle.game.server.Server;
import com.particle.game.world.level.WorldService;
import com.particle.inputs.ServerPacketHandle;
import com.particle.model.level.Level;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.OnCreateSessionPacket;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CreateSessionHandle extends ServerPacketHandle<OnCreateSessionPacket> {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateSessionHandle.class);

    @Inject
    private Server server;

    @Inject
    private PlayerService playerService;

    @Inject
    private WorldService worldService;

    @Override
    public int getTargetPacketID() {
        return ProtocolInfo.ON_CREATE_SESSION_HEAD;
    }

    @Override
    protected void handlePacket(Player player, OnCreateSessionPacket onCreateSessionPacket) {
        if (this.server.getStatus() != ServerApi.ServerStatus.RUNNING) {
            return;
        }

        Level defaultLevel = this.worldService.getDefaultLevel();

        // 保证Node线程中player已经构造完毕
        Player createdPlayer = this.playerService.createPlayer(onCreateSessionPacket.getClientAddress());
        createdPlayer.setLevel(defaultLevel);
        this.server.onPlayerConnectionEstablished(createdPlayer);

        LOGGER.info("Client {} connect.", onCreateSessionPacket.getClientAddress());

        defaultLevel.getLevelSchedule().scheduleSimpleTask("CreatePlayer", () -> this.playerService.initPlayer(createdPlayer));
    }
}
