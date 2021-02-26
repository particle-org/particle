package com.particle.game.entity.link;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.entity.Entity;
import com.particle.model.math.Vector3f;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EntityVehicleModule extends BehaviorModule {

    /**
     * 乘客数据
     */
    private List<Entity> passengers = new ArrayList<>();

    /**
     * 给外界的乘客数据，不允许直接修改
     */
    private List<Entity> passengersView = Collections.unmodifiableList(passengers);

    private Vector3f sitOffset = new Vector3f(0, 0, 0);

    public void addPassengers(Entity entity) {
        this.passengers.add(entity);
    }

    public void removePassengers(Entity entity) {
        this.passengers.remove(entity);
    }

    public void clearPassengers() {
        this.passengers.clear();
    }

    public List<Entity> getPassengers() {
        return this.passengersView;
    }

    public Vector3f getSitOffset() {
        return sitOffset;
    }

    public void setSitOffset(Vector3f sitOffset) {
        this.sitOffset = sitOffset;
    }
}
