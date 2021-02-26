package com.particle.api.entity;

public interface IMobEntityTemplateFactory {
    IEntityTemplateCreator buildTemplateCreator(int type, String actorType);

    IEntityTemplateCreator buildTemplateCreator(int type, String actorType, boolean isBaby);
}
