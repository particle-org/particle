package com.particle.game.entity.service.template;

import com.particle.api.entity.IEntityTemplateCreator;
import com.particle.api.entity.IEntityTemplateServiceApi;
import com.particle.model.entity.Entity;
import com.particle.model.entity.EntityType;
import com.particle.model.math.Vector3f;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class EntityTemplateService implements IEntityTemplateServiceApi {

    private Map<String, IEntityTemplateCreator> entityTemplateRecorder = new HashMap<>();

    @Inject
    public void registerDefault(HolographTemplate holographTemplate,
                                VirtualBlockInteractiveTemplate virtualBlockInteractiveTemplate,
                                MobEntityTemplateFactory mobEntityTemplateFactory) {
        this.entityTemplateRecorder.put("Holograph", holographTemplate);
        this.entityTemplateRecorder.put("VirtualBlockInteractive", virtualBlockInteractiveTemplate);

        // 注册默认的生物模型
        for (EntityType entityType : EntityType.values()) {
            this.entityTemplateRecorder.put(entityType.actorType(), mobEntityTemplateFactory.buildTemplateCreator(entityType.type(), entityType.actorType()));

            this.entityTemplateRecorder.put(entityType.actorType() + "_baby", mobEntityTemplateFactory.buildTemplateCreator(entityType.type(), entityType.actorType(), true));
        }
    }

    @Override
    public void registerEntity(String id, IEntityTemplateCreator template) {
        this.entityTemplateRecorder.put(id, template);
    }

    @Override
    public Entity createEntityFromTemplate(String id, Vector3f position) {
        IEntityTemplateCreator iEntityTemplateCreator = this.entityTemplateRecorder.get(id);

        if (iEntityTemplateCreator == null) {
            return null;
        }

        return iEntityTemplateCreator.getEntity(position);
    }
}
