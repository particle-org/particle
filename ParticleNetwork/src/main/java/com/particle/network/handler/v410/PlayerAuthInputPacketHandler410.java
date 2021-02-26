package com.particle.network.handler.v410;

import com.particle.model.network.packets.data.PlayerAuthInputPacket;
import com.particle.model.player.ClientPlayMode;
import com.particle.model.player.InputMode;
import com.particle.network.encoder.Vector2FEncoder;
import com.particle.network.encoder.Vector3FEncoder;
import com.particle.network.handler.AbstractPacketHandler;

public class PlayerAuthInputPacketHandler410 extends AbstractPacketHandler<PlayerAuthInputPacket> {

    private Vector2FEncoder vector2FEncoder = Vector2FEncoder.getInstance();
    private Vector3FEncoder vector3FEncoder = Vector3FEncoder.getInstance();

    @Override
    protected void doDecode(PlayerAuthInputPacket dataPacket, int version) {
        dataPacket.setPlayerRotation(vector2FEncoder.decode(dataPacket, version));
        dataPacket.setPlayerPosition(vector3FEncoder.decode(dataPacket, version));
        dataPacket.setMoveVector(vector2FEncoder.decode(dataPacket, version));
        dataPacket.setHeadRotation(dataPacket.readLFloat());

        dataPacket.setInputData(dataPacket.readUnsignedVarLong());
        dataPacket.setInputMode(InputMode.fromValue(dataPacket.readUnsignedVarInt()));
        dataPacket.setPlayMode(ClientPlayMode.fromValue(dataPacket.readUnsignedVarInt()));

        if (dataPacket.getPlayMode() == ClientPlayMode.REALITY) {
            dataPacket.setVrGazeDirection(vector3FEncoder.decode(dataPacket, version));
        }

        dataPacket.setClientTick(dataPacket.readUnsignedVarLong());
        dataPacket.setPositionDelta(vector3FEncoder.decode(dataPacket, version));

        dataPacket.setCameraDeparted(dataPacket.readBoolean());
    }

    @Override
    protected void doEncode(PlayerAuthInputPacket dataPacket, int version) {

    }
}
