package com.particle.model.player;

import com.particle.model.entity.IEntity;
import com.particle.model.entity.LivingEntity;
import com.particle.model.entity.id.IDAllocation;
import com.particle.model.events.EventSource;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;

public class Player extends LivingEntity implements IEntity, EventSource {

    // TODO 客户端可以通过UI改变GameRule里面的变量，核心是通过命令的方式通知到服务端。
    // todo 需要每个玩家存储对应的GameRuleComponent，并存储在玩家存档中，以便后面玩家跨服传送的时候，能保持GameRule设置不变。
    // TODO 2019.03.22 GameRule不是世界规则吗?玩家跨服时不应该直接使用新世界的规则吗？

    private InetSocketAddress clientAddress;

    private static AtomicInteger index = new AtomicInteger();

    /**
     * 客户端版本号
     */
    private int protocolVersion;

    private String identifiedStr;

    /**
     * 玩家状态
     */
    private PlayerState playerState;
    /**
     * 玩家的操作
     */
    private Vector3 lastOperation;

    /**
     * 玩家的spawn点
     */
    private Vector3f spawnPosition;

    /**
     * 最后一次存档时间
     */
    public long lastSaveTime = 0;


    /**
     * 操作弓的时间
     */
    private long operationBowTime = 0;

    /**
     * 操作弩的时间
     */
    private boolean operationCrossbow = false;

    /**
     * 是否可存檔
     */
    private boolean saveLoaded = false;

    /**
     * 表示是否玩家背包打开状态
     */
    private boolean inventoryOpen;

    public Player(InetSocketAddress clientAddress) {
        this.clientAddress = clientAddress;
        this.playerState = PlayerState.CONNECTING;
    }

    public InetSocketAddress getClientAddress() {
        return clientAddress;
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String getIdentifiedStr() {
        return this.identifiedStr;
    }

    public void setIdentifiedStr(String identifiedName) {
        this.identifiedStr = identifiedName;
    }

    public boolean isSpawned() {
        return this.playerState == PlayerState.SPAWNED;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }

    public void setPlayerState(PlayerState playerState) {
        this.playerState = playerState;
    }

    public Vector3 getLastOperation() {
        return lastOperation;
    }

    public void setLastOperation(Vector3 lastOperation) {
        this.lastOperation = lastOperation;
    }

    public Vector3f getSpawnPosition() {
        return spawnPosition;
    }

    public void setSpawnPosition(Vector3f spawnPosition) {
        this.spawnPosition = spawnPosition;
    }

    public long getOperationBowTime() {
        return operationBowTime;
    }

    public void setOperationBowTime(long operationBowTime) {
        this.operationBowTime = operationBowTime;
    }

    public boolean isOperationCrossbow() {
        return operationCrossbow;
    }

    public void setOperationCrossbow(boolean operationCrossbow) {
        this.operationCrossbow = operationCrossbow;
    }

    public boolean isSaveLoaded() {
        return saveLoaded;
    }

    public void setSaveLoaded(boolean saveLoaded) {
        this.saveLoaded = saveLoaded;
    }

    @Override
    public boolean equals(Object player) {
        if (player instanceof Player) {
            return this.getIdentifiedStr().equals(((Player) player).getIdentifiedStr());
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.getClientAddress().hashCode();
    }

    @Override
    public int getIdentifiedId() {
        return this.getClientAddress().hashCode();
    }

    @Override
    protected void onInit() {

    }

    public long getLastSaveTime() {
        return lastSaveTime;
    }

    public void setLastSaveTime(long lastSaveTime) {
        this.lastSaveTime = lastSaveTime;
    }

    public void updateRuntimeIdByUuid(long entityId) {
        this.entityId = IDAllocation.PLAYER_BASE_ID + (entityId & Long.MAX_VALUE);
    }

    @Override
    protected long generateRuntimeId() {
        return IDAllocation.PLAYER_BASE_ID + index.getAndIncrement();
    }

    public boolean isInventoryOpen() {
        return inventoryOpen;
    }

    public void setInventoryOpen(boolean inventoryOpen) {
        this.inventoryOpen = inventoryOpen;
    }
}
