package com.particle.core.ecs.system;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SimpleEcsSystem<T extends ECSModule> implements ECSSystemFactory<SimpleEcsSystem<T>>, ECSSystem {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleEcsSystem.class);

    protected T module;

    private Class<? extends ECSModule>[] requestServices = null;

    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        if (this.requestServices == null) {
            this.requestServices = new Class[]{this.getTypeClass()};
        }

        return this.requestServices;
    }

    @Override
    public SimpleEcsSystem<T> buildECSSystem(GameObject gameObject) {
        try {
            SimpleEcsSystem<T> simpleEcsSystem = this.getClass().newInstance();
            simpleEcsSystem.module = ECSModuleHandler.buildHandler(getTypeClass()).getModule(gameObject);

            return simpleEcsSystem;
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Fail to build ecs system");
        }
    }

    protected abstract Class<T> getTypeClass();
}
