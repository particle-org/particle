package com.particle.game.world.map;

import com.particle.api.ui.map.IMapGenerateServiceApi;
import com.particle.game.item.ItemAttributeService;
import com.particle.model.item.ItemStack;
import com.particle.model.item.types.ItemPrototype;
import com.particle.model.level.settings.Dimension;
import com.particle.model.math.Vector3f;

import javax.inject.Singleton;

@Singleton
public class MapGenerateService implements IMapGenerateServiceApi {

    private static final short CURRENT_NEW_SURVIVAL_MAP_ID = 1;

    /**
     * 地图生成的id结构：
     * 总共64bit
     * - 高 16bit id类型
     * - 低 48bit id值
     * <p>
     * 类型使用情况
     * 0x00 : 主世界地图，基于ID换算坐标
     * 0x01 ：地狱世界地图，基于ID换算坐标
     * 0x02 ：末地世界地图，基于ID换算坐标
     * 0x0f ：未知世界地图，基于ID换算坐标
     * <p>
     * 0x10 ：新端世界地图，基于ID直接发放不换算
     * 0x20 : 藏寶圖系統
     */
    // 类型定义
    public static final long TYPE_OVER_WORLD = 0x00;
    public static final long TYPE_NETHER = 0x01;
    public static final long TYPE_THE_END = 0x02;
    public static final long TYPE_MISSMATCH = 0x0f;

    public static final long TYPE_NEW_SURVIVAL = 0x10;
    public static final long TYPE_TREASURE_MAP = 0x20;

    // 快捷操作变量
    public static final long MAP_ID_MASK = 0xffffffffffffL;

    private static final long OVER_WORLD_MASK = (TYPE_OVER_WORLD << 48) | MAP_ID_MASK;
    private static final long NETHER_MASK = (TYPE_NETHER << 48) | MAP_ID_MASK;
    private static final long THE_END_MASK = (TYPE_THE_END << 48) | MAP_ID_MASK;
    private static final long MISSMATCH_MASK = (TYPE_MISSMATCH << 48) | MAP_ID_MASK;

    private static final long NEW_SURVIVAL = (TYPE_NEW_SURVIVAL << 48) | MAP_ID_MASK;

    private static final long TREASURE_MAP = (TYPE_TREASURE_MAP << 48) | MAP_ID_MASK;

    /**
     * 通过坐标生成地图
     *
     * @param displayPlayers
     * @param position
     * @return
     */
    public ItemStack generateMapFromPosition(Vector3f position, boolean displayPlayers, Dimension dimension) {
        long index = this.generateMapIdFromPosition(position.getFloorX(), position.getFloorZ());
        if (dimension == Dimension.Overworld) {
            index &= OVER_WORLD_MASK;
        } else if (dimension == Dimension.Nether) {
            index &= NETHER_MASK;
        } else if (dimension == Dimension.TheEnd) {
            index &= THE_END_MASK;
        } else {
            index &= MISSMATCH_MASK;
        }

        ItemStack itemMap = ItemStack.getItem(ItemPrototype.MAP_FILLED);
        itemMap.setNbtData("map_uuid", index);
        itemMap.setNbtData("map_display_players", displayPlayers);

        return itemMap;
    }

    /**
     * 通过坐标生成地图
     *
     * @param displayPlayers
     * @return
     */
    @Override
    public ItemStack generateSingleContentMap(boolean displayPlayers, String mapName) {
        ItemStack itemMap = ItemStack.getItem(ItemPrototype.MAP_FILLED);
        itemMap.setNbtData("map_uuid", CURRENT_NEW_SURVIVAL_MAP_ID | (TYPE_NEW_SURVIVAL << 48));
        itemMap.setNbtData("map_display_players", displayPlayers);
        itemMap.setNbtData("map_treasure", false);
        ItemAttributeService.setDisplayName(itemMap, mapName);

        return itemMap;
    }

    /**
     * 通过坐标换算地图id
     *
     * @param x
     * @param z
     * @return
     */
    public long generateMapIdFromPosition(int x, int z) {
        long indexX = x >> 8;
        long indexZ = z >> 8;

        return (indexX << 24) | indexZ;
    }
}
