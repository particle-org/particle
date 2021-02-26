package com.particle.model.events.level.player;

import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;
import com.particle.model.player.action.InteractPacketAction;

public class PlayerInteractActionEvent extends LevelPlayerEvent {

    private InteractPacketAction interactPacketAction;

    private long targetRuntimeId;

    private Vector3f position;

    public PlayerInteractActionEvent(Player player, InteractPacketAction interactPacketAction,
                                     long targetRuntimeId, Vector3f position) {
        super(player);
        this.interactPacketAction = interactPacketAction;
        this.targetRuntimeId = targetRuntimeId;
        this.position = position;
    }

    public PlayerInteractActionEvent(Player player) {
        super(player);
    }

    public InteractPacketAction getInteractPacketAction() {
        return interactPacketAction;
    }

    public void setInteractPacketAction(InteractPacketAction interactPacketAction) {
        this.interactPacketAction = interactPacketAction;
    }

    public long getTargetRuntimeId() {
        return targetRuntimeId;
    }

    public void setTargetRuntimeId(long targetRuntimeId) {
        this.targetRuntimeId = targetRuntimeId;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }
}
