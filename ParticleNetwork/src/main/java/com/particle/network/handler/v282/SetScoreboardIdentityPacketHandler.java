package com.particle.network.handler.v282;

import com.particle.model.network.packets.data.SetScoreboardIdentityPacket;
import com.particle.model.ui.score.ScoreIdentityInfo;
import com.particle.model.ui.score.ScoreboardIdentityPacketType;
import com.particle.network.handler.AbstractPacketHandler;

import java.util.List;

public class SetScoreboardIdentityPacketHandler extends AbstractPacketHandler<SetScoreboardIdentityPacket> {

    @Override
    protected void doDecode(SetScoreboardIdentityPacket dataPacket, int version) {
        byte type = dataPacket.readByte();
        int size = dataPacket.readUnsignedVarInt();
        List<ScoreIdentityInfo> infos = dataPacket.getIdentityInfos();
        for (int index = 0; index < size; index++) {
            ScoreIdentityInfo score = new ScoreIdentityInfo();
            score.setScoreBoardId(dataPacket.readSignedVarLong().longValue());
            if (type == ScoreboardIdentityPacketType.Update.getType()) {
                score.setPlayerUniqueId(dataPacket.readSignedVarLong().longValue());
            }
            infos.add(score);
        }
    }

    @Override
    protected void doEncode(SetScoreboardIdentityPacket dataPacket, int version) {
        dataPacket.writeByte((byte) dataPacket.getScoreboardIdentityPacketType().getType());
        List<ScoreIdentityInfo> infos = dataPacket.getIdentityInfos();
        dataPacket.writeUnsignedVarInt(infos.size());
        for (ScoreIdentityInfo score : infos) {
            dataPacket.writeSignedVarLong(score.getScoreBoardId());
            if (dataPacket.getScoreboardIdentityPacketType() == ScoreboardIdentityPacketType.Update) {
                dataPacket.writeSignedVarLong(score.getPlayerUniqueId());
            }
        }
    }
}
