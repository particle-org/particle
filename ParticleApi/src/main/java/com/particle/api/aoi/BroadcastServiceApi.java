package com.particle.api.aoi;

import com.particle.model.entity.Entity;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.player.Player;

import java.util.List;

public interface BroadcastServiceApi {

    void register(Level level, Entity entity);

    void refreshBroadcastList(Entity sender);

    void broadcast(Entity sender, DataPacket dataPacket);

    void broadcast(Entity sender, DataPacket dataPacket, boolean selfBroadcast);

    void broadcast(Entity sender, List<DataPacket> dataPackets);

    void broadcast(Entity sender, List<DataPacket> dataPackets, boolean selfBroadcast);

    void broadcast(Level level, Vector3f position, DataPacket dataPacket);

    void broadcast(Level level, Vector3f position, DataPacket dataPacket, Player player);

    void broadcast(Level level, Vector3 position, DataPacket dataPacket);

    void broadcast(Level level, Vector3 position, DataPacket dataPacket, Player player);

    void broadcast(Level level, Vector3 position, DataPacket dataPacket, int chunkRadius);

    void broadcast(Level level, DataPacket dataPacket);
}
