package com.particle.model.events.level.player;

import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;

public class PlayerRespawnEvent extends LevelPlayerEvent {

    private Vector3f spawnPosition;

    /**
     * 复活惩罚扣血
     */
    private float respawnLossHealth = 0f;
    /**
     * 复活惩罚扣经验
     */
    private int respawnLossExperience = 0;

    public PlayerRespawnEvent(Player player, Vector3f spawnPosition) {
        super(player);
    }

    public PlayerRespawnEvent(Player player) {
        super(player);
    }

    public Vector3f getSpawnPosition() {
        return spawnPosition;
    }

    public void setSpawnPosition(Vector3f spawnPosition) {
        this.spawnPosition = spawnPosition;
    }

    public float getRespawnLossHealth() {
        return respawnLossHealth;
    }

    public void setRespawnLossHealth(float respawnLossHealth) {
        this.respawnLossHealth = respawnLossHealth;
    }

    public int getRespawnLossExperience() {
        return respawnLossExperience;
    }

    public void setRespawnLossExperience(int respawnLossExperience) {
        this.respawnLossExperience = respawnLossExperience;
    }
}
