package com.particle.game.entity.attribute.explevel;

import com.particle.api.entity.attribute.ExperienceServiceAPI;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.utils.ecs.ECSComponentService;
import com.particle.model.entity.Entity;
import com.particle.model.entity.attribute.EntityAttribute;
import com.particle.model.network.packets.data.UpdateAttributesPacket;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ExperienceService implements ExperienceServiceAPI {

    private static final ECSModuleHandler<EntityExperienceModule> ENTITY_EXPERIENCE_MODULE_HANDLER = ECSModuleHandler.buildHandler(EntityExperienceModule.class);

    @Inject
    private NetworkManager networkManager;

    @Inject
    private ECSComponentService ecsComponentService;

    @Override
    public void setEntityExperience(Entity entity, int value) {
        EntityExperienceModule module = ENTITY_EXPERIENCE_MODULE_HANDLER.getModule(entity);
        if (module != null) {
            module.setEntityExperience(value);
        }
    }

    @Override
    public void setEntityExperienceLevel(Entity entity, int value) {
        EntityExperienceModule module = ENTITY_EXPERIENCE_MODULE_HANDLER.getModule(entity);
        if (module != null) {
            module.setEntityExperienceLevel(value);
        }
    }

    /**
     * 获取生物经验
     *
     * @param entity
     * @return
     */
    public int getEntityExperience(Entity entity) {
        EntityExperienceModule module = ENTITY_EXPERIENCE_MODULE_HANDLER.getModule(entity);
        if (module != null) {
            return module.getEntityExperience();
        }

        return 0;
    }

    /**
     * 获取生物等级
     *
     * @param entity
     * @return
     */
    public int getEntityLevel(Entity entity) {
        EntityExperienceModule module = ENTITY_EXPERIENCE_MODULE_HANDLER.getModule(entity);
        if (module != null) {
            return module.getEntityLevel();
        }

        return 0;
    }

    /**
     * 花费等级，与直接操作的区别是会记录花销，用于附魔/修复场景
     *
     * @param entity
     * @param cost
     */
    public void spendLevel(Entity entity, int cost) {
        EntityExperienceModule module = ENTITY_EXPERIENCE_MODULE_HANDLER.getModule(entity);
        if (module != null) {
            module.spendLevel(cost);
        }
    }

    /**
     * 确认生物花销的等级
     *
     * @param entity
     * @return
     */
    public int confirmLevelSpaend(Entity entity) {
        EntityExperienceModule module = ENTITY_EXPERIENCE_MODULE_HANDLER.getModule(entity);
        if (module != null) {
            return module.confirmLevelSpend();
        }

        return 0;
    }

    /**
     * 给玩家增加经验
     *
     * @param entity
     * @param exp
     * @param isLevel
     * @return
     */
    @Override
    public boolean addExperience(Entity entity, int exp, boolean isLevel) {
        EntityExperienceModule module = ENTITY_EXPERIENCE_MODULE_HANDLER.getModule(entity);
        if (module == null) {
            return false;
        }

        if (isLevel) {
            module.addLevel(exp);
        } else {
            module.addExperience(exp);
        }

        if (entity instanceof Player) {
            this.updatePlayerExperience((Player) entity, module.getEntityExpAttribute(), module.getEntityExpLevelAttribute());
        }


        return true;
    }

    @Override
    public EntityAttribute getEntityExpAttribute(Entity entity) {
        EntityExperienceModule module = ENTITY_EXPERIENCE_MODULE_HANDLER.getModule(entity);
        if (module == null) {
            return null;
        }

        return module.getEntityExpAttribute();
    }

    @Override
    public EntityAttribute getEntityExpLevelAttribute(Entity entity) {
        EntityExperienceModule module = ENTITY_EXPERIENCE_MODULE_HANDLER.getModule(entity);
        if (module == null) {
            return null;
        }

        return module.getEntityExpLevelAttribute();
    }

    @Override
    public int levelUpgradeNeeded(Entity entity, int currentLevel) {
        EntityExperienceModule module = ENTITY_EXPERIENCE_MODULE_HANDLER.getModule(entity);
        if (module == null) {
            return 0;
        }
        return module.levelUpgradeNeeded(currentLevel);
    }

    /**
     * 设置玩家经验
     *
     * @param player
     */
    private void updatePlayerExperience(Player player, EntityAttribute expAttribute, EntityAttribute expLevelAttribute) {
        if (player.isSpawned()) {
            UpdateAttributesPacket updateAttributesPacket = new UpdateAttributesPacket();
            updateAttributesPacket.setEntityId(player.getRuntimeId());
            EntityAttribute[] attributes = new EntityAttribute[]{expAttribute, expLevelAttribute};
            updateAttributesPacket.setAttributes(attributes);
            this.networkManager.sendMessage(player.getClientAddress(), updateAttributesPacket);
        }
    }
}
