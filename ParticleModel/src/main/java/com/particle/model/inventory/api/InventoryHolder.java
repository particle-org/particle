package com.particle.model.inventory.api;

import com.particle.model.entity.Entity;
import com.particle.model.inventory.Inventory;
import com.particle.model.level.Level;
import com.particle.model.math.Vector3f;

public interface InventoryHolder {

    /**
     * 返回其所属的背包
     *
     * @return
     */
    Inventory getInventory();

    /**
     * 返回持有者的当前位置
     *
     * @return
     */
    Vector3f getPosition();

    /**
     * 返回所属的世界
     *
     * @return
     */
    Level getLevel();

    /**
     * 不再集成IEntity，而是自己定义该接口，原因在于
     * <p>
     * 1) InventoryHolder 本质不为Entity，继承会导致业务逻辑混乱
     * 2) 该RuntimeID的值与Entity的RuntimeID的意义也不一样
     *
     * @return
     */
    long getRuntimeId();

    Entity getOwn();
}
