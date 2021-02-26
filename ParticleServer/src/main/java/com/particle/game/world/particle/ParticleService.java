package com.particle.game.world.particle;

import com.particle.api.particle.ParticleServiceApi;
import com.particle.game.world.aoi.BroadcastServiceProxy;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.level.Level;
import com.particle.model.level.LevelEventType;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.data.LevelEventPacket;
import com.particle.model.network.packets.data.SpawnParticleEffectPacket;
import com.particle.model.particle.CustomParticleType;
import com.particle.model.particle.ParticleType;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ParticleService implements ParticleServiceApi {

    @Inject
    private NetworkManager networkManager;

    @Inject
    private BroadcastServiceProxy broadcastServiceProxy;


    @Override
    public void playParticle(Player player, LevelEventType levelEventType, Vector3f position, int data) {
        LevelEventPacket levelEventPacket = new LevelEventPacket();
        levelEventPacket.setEventType(levelEventType.getType());
        levelEventPacket.setPosition(position);
        levelEventPacket.setData(data);
        this.networkManager.sendMessage(player.getClientAddress(), levelEventPacket);
    }

    @Override
    public void playParticle(Player player, LevelEventType levelEventType, Vector3f position) {
        this.playParticle(player, levelEventType, position, 0);
    }

    @Override
    public void playParticle(Level level, LevelEventType levelEventType, Block block, Vector3f position, int data) {
        LevelEventPacket levelEventPacket = new LevelEventPacket();
        levelEventPacket.setBlock(block);
        levelEventPacket.setEventType(levelEventType.getType());
        levelEventPacket.setPosition(position);
        levelEventPacket.setData(data);
        this.broadcastServiceProxy.broadcast(level, new Vector3(position), levelEventPacket, 1);
    }

    @Override
    public void playParticle(Level level, LevelEventType levelEventType, Vector3f position) {
        this.playParticle(level, levelEventType, Block.getBlock(BlockPrototype.AIR), position, 0);
    }

    @Override
    public void playParticle(Player player, ParticleType particleType, Vector3f position, int data) {
        LevelEventPacket levelEventPacket = new LevelEventPacket();
        levelEventPacket.setEventType(LevelEventType.GlobalEvent.getType() | particleType.getType());
        levelEventPacket.setPosition(position);
        levelEventPacket.setData(data);
        this.networkManager.sendMessage(player.getClientAddress(), levelEventPacket);
    }

    @Override
    public void playParticle(Player player, ParticleType particleType, Vector3f position) {
        this.playParticle(player, particleType, position, 0);
    }

    @Override
    public void playParticle(Level level, ParticleType particleType, Vector3f position, int data) {
        LevelEventPacket levelEventPacket = new LevelEventPacket();
        levelEventPacket.setEventType(LevelEventType.GlobalEvent.getType() | particleType.getType());
        levelEventPacket.setPosition(position);
        levelEventPacket.setData(data);
        this.broadcastServiceProxy.broadcast(level, new Vector3(position), levelEventPacket, 1);
    }

    @Override
    public void playParticle(Level level, ParticleType particleType, Vector3f position) {
        this.playParticle(level, particleType, position, 0);
    }

    public void playParticle(Level level, CustomParticleType customParticleType, Vector3f position) {
        SpawnParticleEffectPacket spawnParticleEffectPacket = new SpawnParticleEffectPacket();
        spawnParticleEffectPacket.setEntityUniqueId(-1);
        spawnParticleEffectPacket.setDimensionId(level.getDimension().getMode());
        spawnParticleEffectPacket.setPosition(position);
        spawnParticleEffectPacket.setEffectName(customParticleType.getParticleName());

        this.broadcastServiceProxy.broadcast(level, new Vector3(position), spawnParticleEffectPacket, 1);
    }

    public void playParticle(Level level, CustomParticleType customParticleType, Player fromPlayer, Vector3f position) {
        SpawnParticleEffectPacket spawnParticleEffectPacket = new SpawnParticleEffectPacket();
        spawnParticleEffectPacket.setEntityUniqueId(fromPlayer.getIdentifiedId());
        spawnParticleEffectPacket.setDimensionId(level.getDimension().getMode());
        spawnParticleEffectPacket.setPosition(position);
        spawnParticleEffectPacket.setEffectName(customParticleType.getParticleName());

        this.broadcastServiceProxy.broadcast(fromPlayer, spawnParticleEffectPacket);
    }
}
