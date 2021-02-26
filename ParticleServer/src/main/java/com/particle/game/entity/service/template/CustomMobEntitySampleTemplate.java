package com.particle.game.entity.service.template;

import com.particle.api.entity.IEntityTemplateCreator;
import com.particle.game.entity.service.MobEntityService;
import com.particle.model.entity.Entity;
import com.particle.model.entity.type.EntityTypeDictionary;
import com.particle.model.math.Vector3f;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class CustomMobEntitySampleTemplate implements IEntityTemplateCreator {

    @Inject
    private MobEntityService mobEntityService;

    @Inject
    public void init() {
        // 注册自定义生物
        EntityTypeDictionary.registerEntityType(128, "tool:particle_test1", false, false, false, ":");
    }

    @Override
    public Entity getEntity(Vector3f position) {
        return this.mobEntityService.createEntity("tool:particle_test1", position);
    }
}
