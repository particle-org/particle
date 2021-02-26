package com.particle.game.world.map;

import com.particle.api.ui.map.IMapDecoratorProvider;
import com.particle.api.ui.map.IMapServiceApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.event.dispatcher.EventDispatcher;
import com.particle.game.world.map.decorator.PlayerDecoratorProvider;
import com.particle.game.world.map.model.PlayerMapUpdaterModule;
import com.particle.game.world.map.providers.IMapProvider;
import com.particle.game.world.map.providers.SingleContentMapProvider;
import com.particle.model.events.level.player.PlayerOpenMapEvent;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.level.settings.Dimension;
import com.particle.model.network.packets.data.ClientboundMapItemDataPacket;
import com.particle.model.player.Player;
import com.particle.model.ui.map.MapData;
import com.particle.model.ui.map.MapDecorator;
import com.particle.network.NetworkManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class MapService implements IMapServiceApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(MapService.class);

    private static final ECSModuleHandler<PlayerMapUpdaterModule> PLAYER_MAP_UPDATER_MODULE_HANDLER = ECSModuleHandler.buildHandler(PlayerMapUpdaterModule.class);


    private Map<Long, IMapProvider> mapProviders = new HashMap<>();

    @Inject
    private NetworkManager networkManager;

    @Inject
    private PlayerDecoratorProvider playerDecoratorProvider;

    private EventDispatcher eventDispatcher = EventDispatcher.getInstance();

    @Inject
    public void init(SingleContentMapProvider mapProvider) {
        this.mapProviders.put(MapGenerateService.TYPE_NEW_SURVIVAL, mapProvider);
    }

    public void updateHolderItem(Player player, ItemStack itemMap, int slot, boolean isRightHand) {
        PlayerMapUpdaterModule playerMapUpdaterModule = PLAYER_MAP_UPDATER_MODULE_HANDLER.getModule(player);

        if (playerMapUpdaterModule != null) {
            // 清缓存
            if (isRightHand) {
                playerMapUpdaterModule.setMainMapData(null);
                playerMapUpdaterModule.getMainDecoratorProviderList().clear();
            } else {
                playerMapUpdaterModule.setSideMapData(null);
                playerMapUpdaterModule.getSideDecoratorProviderList().clear();
            }

            // 非地圖
            if (itemMap.getItemType() != ItemPrototype.MAP_FILLED) {
                return;
            }

            // 缓存成map数据
            long map_uuid = itemMap.getNbt().getLong("map_uuid");
            if (map_uuid != 0) {
                IMapProvider mapProvider = this.mapProviders.get(map_uuid >>> 48);
                if (mapProvider != null) {
                    // 构造map数据
                    MapData mapData = mapProvider.getMapData(player, map_uuid & MapGenerateService.MAP_ID_MASK);
                    if (mapData != null) {
                        if (isRightHand) {
                            playerMapUpdaterModule.setMainMapData(mapData);
                        } else {
                            playerMapUpdaterModule.setSideMapData(mapData);
                        }

                        // 配置默认provider
                        if (itemMap.getNbt().getBoolean("map_display_players")) {
                            this.addMapDecoratorProvider(player, this.playerDecoratorProvider, isRightHand);
                        }

                        LOGGER.info("Player {} equip map", player.getIdentifiedStr());
                    }

                    // 发送事件提示
                    PlayerOpenMapEvent playerOpenMapEvent = new PlayerOpenMapEvent(player, itemMap, slot, isRightHand);
                    this.eventDispatcher.dispatchEvent(playerOpenMapEvent);
                }
            }
        }
    }

    /**
     * 初始化玩家组件
     *
     * @param player
     */
    public void initPlayerComponent(Player player) {
        PLAYER_MAP_UPDATER_MODULE_HANDLER.bindModule(player);
    }

    /**
     * 给玩家添加标识符编码器
     *
     * @param player
     * @param decoratorProvider
     * @param isRightHand
     */
    @Override
    public void addMapDecoratorProvider(Player player, IMapDecoratorProvider decoratorProvider, boolean isRightHand) {
        PlayerMapUpdaterModule playerMapUpdaterModule = PLAYER_MAP_UPDATER_MODULE_HANDLER.getModule(player);

        if (playerMapUpdaterModule != null) {
            if (isRightHand) {
                playerMapUpdaterModule.getMainDecoratorProviderList().add(decoratorProvider);
            } else {
                playerMapUpdaterModule.getSideDecoratorProviderList().add(decoratorProvider);
            }
        }
    }

    /**
     * 申请地图数据
     *
     * @param player
     * @param mapId
     */
    public void sendPlayerMapData(Player player, long mapId) {
        IMapProvider mapProvider = this.mapProviders.get(mapId >>> 48);
        if (mapProvider != null) {
            MapData mapData = mapProvider.getMapData(player, mapId & MapGenerateService.MAP_ID_MASK);

            ClientboundMapItemDataPacket clientboundMapItemDataPacket = new ClientboundMapItemDataPacket();
            this.updateMapBaseData(mapData, clientboundMapItemDataPacket);
            this.updateMapImageData(mapData, clientboundMapItemDataPacket);

            this.networkManager.sendMessage(player.getClientAddress(), clientboundMapItemDataPacket);
        }
    }

    /**
     * 申请地图坐标
     *
     * @param player
     */
    public void sendPlayerDecorator(Player player, MapData mapData, List<IMapDecoratorProvider> decoratorProviderList) {
        ClientboundMapItemDataPacket clientboundMapItemDataPacket = new ClientboundMapItemDataPacket();
        this.updateMapBaseData(mapData, clientboundMapItemDataPacket);
        this.updateMapDecorationData(mapData, player, decoratorProviderList, clientboundMapItemDataPacket);

        this.networkManager.sendMessage(player.getClientAddress(), clientboundMapItemDataPacket);
    }

    private void updateMapBaseData(MapData mapData, ClientboundMapItemDataPacket clientboundMapItemDataPacket) {
        clientboundMapItemDataPacket.setMapId(mapData.getMapId());
        clientboundMapItemDataPacket.setDimension((byte) Dimension.Overworld.getMode());
        clientboundMapItemDataPacket.setScale(mapData.getScale());
    }

    private void updateMapImageData(MapData mapData, ClientboundMapItemDataPacket clientboundMapItemDataPacket) {
        clientboundMapItemDataPacket.setTypeFlags(clientboundMapItemDataPacket.getTypeFlags() | ClientboundMapItemDataPacket.TEXTURE_UPDATE_MASK);
        clientboundMapItemDataPacket.setWidth(mapData.getWidth());
        clientboundMapItemDataPacket.setHeight(mapData.getHeight());
        clientboundMapItemDataPacket.setOffsetZ(mapData.getOffsetZ());
        clientboundMapItemDataPacket.setOffsetX(mapData.getOffsetX());
        clientboundMapItemDataPacket.setImage(mapData.getImage());
    }

    private void updateMapDecorationData(MapData mapData, Player player, List<IMapDecoratorProvider> decoratorProviderList, ClientboundMapItemDataPacket clientboundMapItemDataPacket) {
        clientboundMapItemDataPacket.setTypeFlags(clientboundMapItemDataPacket.getTypeFlags() | ClientboundMapItemDataPacket.DECORATION_UPDATE_MASK);

        int[] runtimeIds = new int[decoratorProviderList.size()];
        if (runtimeIds.length > 0) {
            runtimeIds[0] = (int) player.getRuntimeId();
            for (int i = 1; i < runtimeIds.length; i++) {
                runtimeIds[i] = i ^ Integer.MAX_VALUE;
            }
        }
        clientboundMapItemDataPacket.setEntityId(runtimeIds);

        MapDecorator[] mapDecorators = new MapDecorator[decoratorProviderList.size()];
        for (int i = 0; i < mapDecorators.length; i++) {
            mapDecorators[i] = decoratorProviderList.get(i).getDecorator(player, mapData);

        }
        clientboundMapItemDataPacket.setDecorators(mapDecorators);
    }
}
