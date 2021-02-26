package com.particle.model.network.packets.data;

import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.ui.score.ScoreObjective;

public class SetDisplayObjecivePacket extends DataPacket {

    private ScoreObjective scoreObjective = new ScoreObjective();

    @Override
    public int pid() {
        return ProtocolInfo.SET_DISPLAY_OBJECTIVE_PACKET;
    }


    public ScoreObjective getScoreObjective() {
        return scoreObjective;
    }

    public void setScoreObjective(ScoreObjective scoreObjective) {
        this.scoreObjective = scoreObjective;
    }
}
