package com.particle.game.world.map.model;

import com.particle.api.ui.map.IMapDecoratorProvider;
import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.ui.map.MapData;

import java.util.ArrayList;
import java.util.List;

public class PlayerMapUpdaterModule extends BehaviorModule {

    private boolean showSelfPosition = true;
    private long lastUpdateTimestamp = 0;

    private MapData mainMapData;
    private MapData sideMapData;

    private List<IMapDecoratorProvider> mainDecoratorProviderList = new ArrayList<>();
    private List<IMapDecoratorProvider> sideDecoratorProviderList = new ArrayList<>();

    public boolean isShowSelfPosition() {
        return showSelfPosition;
    }

    public void setShowSelfPosition(boolean showSelfPosition) {
        this.showSelfPosition = showSelfPosition;
    }

    public long getLastUpdateTimestamp() {
        return lastUpdateTimestamp;
    }

    public void setLastUpdateTimestamp(long lastUpdateTimestamp) {
        this.lastUpdateTimestamp = lastUpdateTimestamp;
    }

    public MapData getMainMapData() {
        return mainMapData;
    }

    public void setMainMapData(MapData mainMapData) {
        this.mainMapData = mainMapData;
    }

    public MapData getSideMapData() {
        return sideMapData;
    }

    public void setSideMapData(MapData sideMapData) {
        this.sideMapData = sideMapData;
    }

    public List<IMapDecoratorProvider> getMainDecoratorProviderList() {
        return mainDecoratorProviderList;
    }

    public List<IMapDecoratorProvider> getSideDecoratorProviderList() {
        return sideDecoratorProviderList;
    }
}
