package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.SetDisplayObjecivePacket;
import com.particle.model.ui.score.ObjectiveSortOrder;
import com.particle.network.handler.AbstractPacketHandler;

public class SetDisplayObjecivePacketHandler extends AbstractPacketHandler<SetDisplayObjecivePacket> {

    @Override
    protected void doDecode(SetDisplayObjecivePacket dataPacket, int version) {
        dataPacket.getScoreObjective().setDisplaySlotName(dataPacket.readString());
        dataPacket.getScoreObjective().setObjectiveName(dataPacket.readString());
        dataPacket.getScoreObjective().setObjectiveDisplayName(dataPacket.readString());
        dataPacket.getScoreObjective().setCriteriaName(dataPacket.readString());
        dataPacket.getScoreObjective().setSortOrder(ObjectiveSortOrder.from(dataPacket.readByte()));
    }

    @Override
    protected void doEncode(SetDisplayObjecivePacket dataPacket, int version) {
        dataPacket.writeString(dataPacket.getScoreObjective().getDisplaySlotName());
        dataPacket.writeString(dataPacket.getScoreObjective().getObjectiveName());
        dataPacket.writeString(dataPacket.getScoreObjective().getObjectiveDisplayName());
        dataPacket.writeString(dataPacket.getScoreObjective().getCriteriaName());
        dataPacket.writeByte((byte) dataPacket.getScoreObjective().getSortOrder().getOrder());
    }
}
