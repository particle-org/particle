package com.particle.game.world.map;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.module.ECSModule;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.core.ecs.system.ECSSystem;
import com.particle.core.ecs.system.ECSSystemFactory;
import com.particle.game.world.map.model.PlayerMapUpdaterModule;
import com.particle.model.entity.Entity;
import com.particle.model.player.Player;
import com.particle.model.ui.map.MapData;

import javax.inject.Inject;

public class MapDecorationUpdateSystemFactory implements ECSSystemFactory<MapDecorationUpdateSystemFactory.MapDecorationUpdateSystem> {

    private static final ECSModuleHandler<PlayerMapUpdaterModule> PLAYER_MAP_UPDATER_MODULE_HANDLER = ECSModuleHandler.buildHandler(PlayerMapUpdaterModule.class);


    private static final long UPDATE_INTERVAL = 500;

    @Inject
    private MapService mapService;

    public class MapDecorationUpdateSystem implements ECSSystem {

        private Entity entity;
        private PlayerMapUpdaterModule playerMapUpdaterModule;

        public MapDecorationUpdateSystem(Entity entity) {
            this.entity = entity;
            this.playerMapUpdaterModule = PLAYER_MAP_UPDATER_MODULE_HANDLER.getModule(entity);
        }

        @Override
        public void tick(long deltaTime) {
            if (!(entity instanceof Player)) {
                return;
            }

            Player player = (Player) entity;

            // 降低更新频率
            if (System.currentTimeMillis() - this.playerMapUpdaterModule.getLastUpdateTimestamp() < UPDATE_INTERVAL) {
                return;
            }
            this.playerMapUpdaterModule.setLastUpdateTimestamp(System.currentTimeMillis());

            // 更新数据
            MapData mainMapData = this.playerMapUpdaterModule.getMainMapData();
            if (mainMapData != null) {
                mapService.sendPlayerDecorator(player, mainMapData, this.playerMapUpdaterModule.getMainDecoratorProviderList());
            }

            MapData sideMapData = this.playerMapUpdaterModule.getSideMapData();
            if (sideMapData != null) {
                mapService.sendPlayerDecorator(player, sideMapData, this.playerMapUpdaterModule.getSideDecoratorProviderList());
            }
        }
    }

    @Override
    public Class<? extends ECSModule>[] getRequestServices() {
        return new Class[]{PlayerMapUpdaterModule.class};
    }

    @Override
    public MapDecorationUpdateSystem buildECSSystem(GameObject gameObject) {
        if (gameObject instanceof Entity) {
            return new MapDecorationUpdateSystem((Entity) gameObject);
        }

        return null;
    }

    @Override
    public Class<MapDecorationUpdateSystem> getSystemClass() {
        return MapDecorationUpdateSystem.class;
    }
}
