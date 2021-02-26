package com.particle.game.ui.task;

import com.particle.model.network.packets.data.TextPacket;

public enum TaskType {
    POPUP_KEEP,
    SYSTEM_KEEP,
    TIP_KEEP,
    SOUND_PLAYER,
    BOSSBAR,
    ACTIONBAR;

    /**
     * 根据TextPacket的类型返回
     *
     * @param messageTye
     * @return
     */
    static TaskType fromTextMessage(int messageTye) {
        if (messageTye == TextPacket.PopupType) {
            return POPUP_KEEP;
        } else if (messageTye == TextPacket.SystemMessageType) {
            return SYSTEM_KEEP;
        } else if (messageTye == TextPacket.TipType) {
            return TIP_KEEP;
        }
        return null;
    }


}
