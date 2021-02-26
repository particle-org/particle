package com.particle.game.ui.task;

import com.particle.game.ui.TextService;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.data.TextPacket;
import com.particle.model.player.Player;

public class MessageTask extends UITask {

    private DataPacket textPacket;

    private TextService textService;

    public MessageTask(TextService textService, Player player, TextPacket textPacket, int tickLive) {
        super(player, TaskType.fromTextMessage(textPacket.getMessageType()), tickLive);
        this.textPacket = textPacket.clone();
        this.textService = textService;
    }

    @Override
    protected void onTick() {
        this.textService.sendTextPacket(player, (TextPacket) textPacket);
    }
}
