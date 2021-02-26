package com.particle.model.entity;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.model.ecs.ECSSystem;
import com.particle.model.entity.component.ECSComponent;
import com.particle.model.entity.model.tags.EntityTag;
import com.particle.model.level.Level;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public abstract class Entity extends GameObject implements IEntity {
    public static final int VERSION = 0;

    private Set<EntityTag> entityTags = new HashSet<>();

    protected ECSComponent[] components = new ECSComponent[64];
    protected List<ECSSystem> tickedSystem = new LinkedList<>();

    /**
     * 玩家所在Level
     */
    private Level level = null;

    /**
     * 生物ID
     */
    protected long entityId;

    protected String actorType = ":";

    public void init() {
        this.onInit();

        entityId = this.generateRuntimeId();
    }

    public boolean checkECSComponents(int[] ids) {
        for (int id : ids) {
            if (id > components.length || components[id] == null)
                return false;
        }

        return true;
    }

    @Deprecated
    public void setComponent(ECSComponent component) {
        synchronized (this) {
            while (this.components.length <= component.getId()) {
                this.extendComponentsContainer();
            }
        }

        this.components[component.getId()] = component;
    }

    public void resetComponent(int id) {
        if (id < this.components.length) {
            this.components[id] = null;
        }
    }

    @Deprecated
    public boolean hasComponent(int id) {
        if (id < this.components.length) {
            return this.components[id] != null;
        }

        return false;
    }

    @Deprecated
    public ECSComponent getComponent(int id) {
        if (id >= this.components.length) {
            return null;
        }

        return this.components[id];
    }

    @Deprecated
    public <T extends ECSComponent> T getComponent(int id, Class<T> clazz) {
        if (id > this.components.length) {
            return null;
        }

        if (this.components[id] != null) {
            try {
                return (T) this.components[id];
            } catch (Exception e) {
                throw new RuntimeException("Component not matched!", e);
            }
        } else {
            throw new RuntimeException("Component not exist!");
        }
    }

    @Deprecated
    public ECSComponent[] getComponents() {
        return components;
    }

    public List<ECSSystem> getTickedSystem() {
        return this.tickedSystem;
    }

    public void setTickedSystem(List<ECSSystem> tickedSystem) {
        this.tickedSystem = tickedSystem;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getActorType() {
        return actorType;
    }

    protected abstract void onInit();

    /**
     * 增加生物标签，目前仅针对子类使用
     *
     * @param entityTag
     */
    protected void addTag(EntityTag entityTag) {
        this.entityTags.add(entityTag);
    }

    /**
     * 判断生物是否有指定标签
     *
     * @param entityTag
     * @return
     */
    public boolean hasTag(EntityTag entityTag) {
        return this.entityTags.contains(entityTag);
    }

    @Override
    public long getRuntimeId() {
        return this.entityId;
    }

    public boolean canFly() {
        return false;
    }

    protected abstract long generateRuntimeId();

    private void extendComponentsContainer() {
        ECSComponent[] components = new ECSComponent[this.components.length << 1];

        System.arraycopy(this.components, 0, components, 0, this.components.length);

        this.components = components;
    }

    public boolean isSame(Entity entity) {
        return entity.getRuntimeId() == this.getRuntimeId();
    }

    public String getDetailInformation() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append("Type: ")
                .append(this.getClass().getSimpleName())
                .append("\n");
        stringBuilder.append("\n");

        stringBuilder.append("[Components]\n");
        int index = 1;
        for (com.particle.core.ecs.component.ECSComponent component : this.getComponentContainer().getComponents()) {
            stringBuilder.append(index++).append(" ").append(component.getClass().getSimpleName()).append("\n");
        }
        stringBuilder.append("\n");

        stringBuilder.append("[Modules]\n");
        index = 1;
        for (ECSModule module : this.getModuleContainer().getModules()) {
            stringBuilder.append(index++).append(" ").append(module.getClass().getSimpleName()).append("\n");
        }
        stringBuilder.append("\n");

        stringBuilder.append("[Systems]\n");
        index = 1;
        for (com.particle.core.ecs.system.ECSSystem ecsSystem : this.getSystemContainer().getSystemList()) {
            stringBuilder.append(index++).append(" ").append(ecsSystem.getClass().getSimpleName()).append("\n");
        }
        stringBuilder.append("\n");

        return stringBuilder.toString();
    }
}
