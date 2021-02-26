package com.particle.game.entity.ai.leaf.action;

import com.particle.api.ai.behavior.EStatus;
import com.particle.api.ai.behavior.IAction;
import com.particle.game.entity.movement.PositionService;
import com.particle.game.world.animation.EntityAnimationService;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.entity.Entity;
import com.particle.model.entity.model.mob.MobEntity;
import com.particle.model.math.Vector3;
import com.particle.model.network.packets.data.EntityEventPacket;

import javax.inject.Inject;
import java.util.Random;

public class EntityEatGrassAction implements IAction {

    // 吃草頻率 (100 為底)
    private int eatRate;

    @Inject
    private Random random;

    @Inject
    private LevelService levelService;

    @Inject
    private PositionService positionService;

    @Inject
    private EntityAnimationService entityAnimationService;


    @Override
    public void onInitialize() {

    }

    @Override
    public EStatus tick(Entity entity) {
        int rand = random.nextInt(100);
        Vector3 vector3 = positionService.getFloorPosition(entity).down();
        // 取得當前踩的方塊
        BlockPrototype block = levelService.getBlockTypeAt(entity.getLevel(), vector3);
        if (rand < eatRate && block != null && block == BlockPrototype.GRASS) {
            // 若是羊 , 則實際吃草
            if (entity instanceof MobEntity && ((MobEntity) entity).getActorType().equals("minecraft:sheep")) {
                levelService.setBlockAt(entity.getLevel(), Block.getBlock(BlockPrototype.DIRT), vector3);
            }

            entityAnimationService.sendAnimation(entity, EntityEventPacket.EAT_GRASS);
        }

        return EStatus.SUCCESS;
    }

    @Override
    public void onTicked(Entity entity, EStatus status) {

    }

    @Override
    public void onRelease() {

    }

    @Override
    public void config(String key, Object val) {
        if (key.equalsIgnoreCase("eatRate") && val instanceof Integer) {
            eatRate = (int) val;
        }
    }
}