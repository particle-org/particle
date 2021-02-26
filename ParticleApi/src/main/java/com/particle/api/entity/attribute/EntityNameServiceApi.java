package com.particle.api.entity.attribute;

import com.particle.model.entity.Entity;

public interface EntityNameServiceApi {

    /**
     * 设置生物名称
     *
     * @param entity     操作的生物
     * @param entityName 生物名称
     */
    void setEntityName(Entity entity, String entityName);

    /**
     * 查询生物名
     *
     * @param entity 操作的生物
     * @return 生物名称
     */
    String getEntityName(Entity entity);

    /**
     * 查询生物显示名称
     *
     * @param entity 操作的生物
     * @return 生物显示名称
     */
    String getDisplayEntityName(Entity entity);

    /**
     * 设置生物显示名称
     *
     * @param entity 操作的生物
     * @return 生物显示名称
     */
    void setDisplayEntityName(Entity entity, String displayEntityName);
}

