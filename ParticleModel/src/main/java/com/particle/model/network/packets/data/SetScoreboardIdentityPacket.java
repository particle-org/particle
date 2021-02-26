package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.ui.score.ScoreIdentityInfo;
import com.particle.model.ui.score.ScoreboardIdentityPacketType;

import java.util.ArrayList;
import java.util.List;

public class SetScoreboardIdentityPacket extends DataPacket {

    private ScoreboardIdentityPacketType scoreboardIdentityPacketType;

    private List<ScoreIdentityInfo> identityInfos = new ArrayList<>();

    @Override
    public int pid() {
        return ProtocolInfo.SET_SCOREBOARD_IDENTITY_PACKET;
    }

    public ScoreboardIdentityPacketType getScoreboardIdentityPacketType() {
        return scoreboardIdentityPacketType;
    }

    public void setScoreboardIdentityPacketType(ScoreboardIdentityPacketType scoreboardIdentityPacketType) {
        this.scoreboardIdentityPacketType = scoreboardIdentityPacketType;
    }

    public List<ScoreIdentityInfo> getIdentityInfos() {
        return identityInfos;
    }

    public void setIdentityInfos(List<ScoreIdentityInfo> identityInfos) {
        this.identityInfos = identityInfos;
    }
}
