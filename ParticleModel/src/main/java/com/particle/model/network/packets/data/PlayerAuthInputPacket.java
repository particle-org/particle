package com.particle.model.network.packets.data;

import com.particle.model.math.Vector2f;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.player.ClientPlayMode;
import com.particle.model.player.InputMode;

public class PlayerAuthInputPacket extends DataPacket {
    private Vector2f playerRotation;
    private Vector3f playerPosition;
    private Vector2f moveVector;
    private float headRotation;
    private long inputData;
    private InputMode inputMode;
    private ClientPlayMode playMode;
    private Vector3f VrGazeDirection;
    private long clientTick;
    private Vector3f positionDelta;
    private boolean cameraDeparted;


    @Override
    public int pid() {
        return ProtocolInfo.PLAYER_AUTH_INPUT_PACKET;
    }

    public Vector2f getPlayerRotation() {
        return playerRotation;
    }

    public void setPlayerRotation(Vector2f playerRotation) {
        this.playerRotation = playerRotation;
    }

    public Vector3f getPlayerPosition() {
        return playerPosition;
    }

    public void setPlayerPosition(Vector3f playerPosition) {
        this.playerPosition = playerPosition;
    }

    public Vector2f getMoveVector() {
        return moveVector;
    }

    public void setMoveVector(Vector2f moveVector) {
        this.moveVector = moveVector;
    }

    public float getHeadRotation() {
        return headRotation;
    }

    public void setHeadRotation(float headRotation) {
        this.headRotation = headRotation;
    }

    public long getInputData() {
        return inputData;
    }

    public void setInputData(long inputData) {
        this.inputData = inputData;
    }

    public InputMode getInputMode() {
        return inputMode;
    }

    public void setInputMode(InputMode inputMode) {
        this.inputMode = inputMode;
    }

    public ClientPlayMode getPlayMode() {
        return playMode;
    }

    public void setPlayMode(ClientPlayMode playMode) {
        this.playMode = playMode;
    }

    public Vector3f getVrGazeDirection() {
        return VrGazeDirection;
    }

    public void setVrGazeDirection(Vector3f vrGazeDirection) {
        VrGazeDirection = vrGazeDirection;
    }

    public boolean isCameraDeparted() {
        return cameraDeparted;
    }

    public void setCameraDeparted(boolean cameraDeparted) {
        this.cameraDeparted = cameraDeparted;
    }

    public long getClientTick() {
        return clientTick;
    }

    public void setClientTick(long clientTick) {
        this.clientTick = clientTick;
    }

    public Vector3f getPositionDelta() {
        return positionDelta;
    }

    public void setPositionDelta(Vector3f positionDelta) {
        this.positionDelta = positionDelta;
    }
}
