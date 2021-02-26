package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.SetScorePacket;
import com.particle.model.ui.score.IdentityDefinitionType;
import com.particle.model.ui.score.ScorePacketInfo;
import com.particle.model.ui.score.ScorePacketType;
import com.particle.network.handler.AbstractPacketHandler;

import java.util.List;

public class SetScorePacketHandler extends AbstractPacketHandler<SetScorePacket> {

    @Override
    protected void doDecode(SetScorePacket dataPacket, int version) {
        byte changeType = dataPacket.readByte();
        dataPacket.setScorePacketType(ScorePacketType.from(changeType));
        List<ScorePacketInfo> infos = dataPacket.getScorePacketInfos();
        int size = dataPacket.readUnsignedVarInt();
        for (int index = 0; index < size; index++) {
            ScorePacketInfo score = new ScorePacketInfo();
            score.setObjectiveName(dataPacket.readString());
            score.setScoreValue(dataPacket.readLInt());
            if (changeType == ScorePacketType.Change.getType()) {
                IdentityDefinitionType iDefType = IdentityDefinitionType.from(dataPacket.readByte());
                switch (iDefType) {
                    case Player:
                        score.setPlayerUniqueId(dataPacket.readSignedVarLong().longValue());
                        break;
                    case Entity:
                        score.setActorId(dataPacket.readSignedVarLong().longValue());
                        break;
                    case FakePlayer:
                        score.setFakePlayerName(dataPacket.readString());
                        break;
                    default:
                        throw new IllegalArgumentException("SetScorePacket参数错误");
                }
            }
            infos.add(score);
        }
    }

    @Override
    protected void doEncode(SetScorePacket dataPacket, int version) {
        byte changeType = (byte) dataPacket.getScorePacketType().getType();
        dataPacket.writeByte(changeType);
        List<ScorePacketInfo> infos = dataPacket.getScorePacketInfos();
        dataPacket.writeUnsignedVarInt(infos.size());
        for (ScorePacketInfo score : infos) {
            dataPacket.writeSignedVarLong(score.getScoreBoardId());
            dataPacket.writeString(score.getObjectiveName());
            dataPacket.writeLInt(score.getScoreValue());
            if (dataPacket.getScorePacketType() == ScorePacketType.Change) {
                dataPacket.writeByte((byte) score.getIdentityDefinitionType().getType());
                switch (score.getIdentityDefinitionType()) {
                    case Player:
                        dataPacket.writeSignedVarLong(score.getPlayerUniqueId());
                        break;
                    case Entity:
                        dataPacket.writeSignedVarLong(score.getActorId());
                        break;
                    case FakePlayer:
                        dataPacket.writeString(score.getFakePlayerName());
                        break;
                    default:
                        throw new IllegalArgumentException("SetScorePacket参数错误");
                }
            }
        }
    }
}
