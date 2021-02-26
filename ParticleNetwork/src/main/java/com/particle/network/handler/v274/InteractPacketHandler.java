package com.particle.network.handler.v274;

import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.data.InteractPacket;
import com.particle.model.player.action.InteractPacketAction;
import com.particle.network.encoder.PositionFEncoder;
import com.particle.network.handler.AbstractPacketHandler;

public class InteractPacketHandler extends AbstractPacketHandler<InteractPacket> {

    private PositionFEncoder positionFEncoder = PositionFEncoder.getInstance();


    @Override
    protected void doDecode(InteractPacket dataPacket, int version) {
        dataPacket.setInteractPacketAction(InteractPacketAction.from(dataPacket.readByte()));
        dataPacket.setTargetRuntimeId(dataPacket.readUnsignedVarLong());
        if (dataPacket.getInteractPacketAction() == InteractPacketAction.InteractUpdate) {
            Vector3f position = positionFEncoder.decode(dataPacket, version);
            dataPacket.setPosition(position);
        }
    }

    @Override
    protected void doEncode(InteractPacket dataPacket, int version) {
        dataPacket.writeByte((byte) dataPacket.getInteractPacketAction().getAction());
        dataPacket.writeUnsignedVarLong(dataPacket.getTargetRuntimeId());
        if (dataPacket.getInteractPacketAction() == InteractPacketAction.InteractUpdate) {
            positionFEncoder.encode(dataPacket, dataPacket.getPosition(), version);
        }
    }
}
