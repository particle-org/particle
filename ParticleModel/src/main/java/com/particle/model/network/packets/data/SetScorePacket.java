package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.ui.score.ScorePacketInfo;
import com.particle.model.ui.score.ScorePacketType;

import java.util.ArrayList;
import java.util.List;

public class SetScorePacket extends DataPacket {

    private ScorePacketType scorePacketType;

    private List<ScorePacketInfo> scorePacketInfos = new ArrayList<>();

    @Override
    public int pid() {
        return ProtocolInfo.SET_SCORE_PACKET;
    }

    public ScorePacketType getScorePacketType() {
        return scorePacketType;
    }

    public void setScorePacketType(ScorePacketType scorePacketType) {
        this.scorePacketType = scorePacketType;
    }

    public List<ScorePacketInfo> getScorePacketInfos() {
        return scorePacketInfos;
    }

    public void setScorePacketInfos(List<ScorePacketInfo> scorePacketInfos) {
        this.scorePacketInfos = scorePacketInfos;
    }
}
