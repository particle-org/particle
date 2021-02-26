package com.particle.model.entity.type;

import com.alibaba.fastjson.JSON;
import com.particle.model.nbt.NBTTagCompound;
import com.particle.model.nbt.NBTTagList;
import com.particle.model.nbt.NBTToByteArray;
import com.particle.model.nbt.NBTToJsonObject;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class EntityTypeDictionary {

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityTypeDictionary.class);

    private static final Map<String, EntityTypeData> ENTITY_CONFIG_DATA = new HashMap<>();

    private static boolean isUpdate = true;
    private static ByteBuf encodeBuffer;

    /**
     * 获取指定生物类型的配置
     *
     * @param entityType
     * @return
     */
    public static EntityTypeData getMobEntityConfig(String entityType) {
        return ENTITY_CONFIG_DATA.get(entityType);
    }

    /**
     * 获取编码后的数据
     *
     * @return
     */
    public static ByteBuf getEncodeData() {
        if (EntityTypeDictionary.isUpdate) {
            NBTTagList entitiesNbtData = new NBTTagList();
            for (EntityTypeData entityTypeData : ENTITY_CONFIG_DATA.values()) {
                if (entityTypeData.getActorType().equals(":") || entityTypeData.getActorType().equals("")) {
                    continue;
                }

                NBTTagCompound entityNbtData = new NBTTagCompound();
                entityNbtData.setBoolean("hasspawnegg", entityTypeData.hasSpawnEgg());
                entityNbtData.setBoolean("experimental", entityTypeData.isExperimental());
                entityNbtData.setBoolean("summonable", entityTypeData.isSummonable());
                entityNbtData.setString("id", entityTypeData.getActorType());
                entityNbtData.setString("bid", entityTypeData.getBid());
                entityNbtData.setInteger("rid", entityTypeData.getType());

                entitiesNbtData.appendTag(entityNbtData);
            }

            NBTTagCompound nbtTagCompound = new NBTTagCompound();
            nbtTagCompound.setTag("idlist", entitiesNbtData);

            ByteBuf encodeBuffer = Unpooled.buffer(10000);
            try {
                encodeBuffer.writeBytes(NBTToByteArray.convertToByteArray(nbtTagCompound, true));
            } catch (IOException e) {
                LOGGER.error("Fail to encode entity nbt data", e);
            }

            EntityTypeDictionary.encodeBuffer = encodeBuffer;
            EntityTypeDictionary.isUpdate = false;
        }

        return Unpooled.wrappedBuffer(EntityTypeDictionary.encodeBuffer);
    }

    /**
     * 注册新的生物类型
     *
     * @param type
     * @param actorType
     * @param hasSpawnEgg
     * @param experimental
     * @param summonable
     * @param bid
     */
    public static void registerEntityType(int type, String actorType, boolean hasSpawnEgg, boolean experimental, boolean summonable, String bid) {
        ENTITY_CONFIG_DATA.put(actorType, new EntityTypeData(type, actorType, hasSpawnEgg, experimental, summonable, bid));

        isUpdate = true;
    }

    /**
     * 加载默认生物类型
     */
    static {
        // 1.16 讀取文件
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader;
        String fileName = "available_entity_identifiers.json";
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(EntityTypeDictionary.class.getClassLoader().getResourceAsStream(fileName)));
            String temp = null;
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuilder.append(temp);
            }

            NBTTagCompound nbtTagCompound = (NBTTagCompound) NBTToJsonObject.convertToTag(JSON.parseObject(stringBuilder.toString()));
            NBTTagList nbtTagList = nbtTagCompound.getTagList("idlist", 10);
            for (int i = 0; i < nbtTagList.tagCount(); i++) {
                NBTTagCompound compound = nbtTagList.getCompoundTagAt(i);
                ENTITY_CONFIG_DATA.put(compound.getString("id"), new EntityTypeData(compound.getInteger("rid"), compound.getString("id"), compound.getBoolean("hasspawnegg"), compound.getBoolean("experimental"), compound.getBoolean("summonable"), compound.getString("bid")));
            }

            bufferedReader.close();
        } catch (IOException e) {
            LOGGER.error("Fail to load block info of {}", fileName, e);
        }

        ENTITY_CONFIG_DATA.put("minecraft:chicken", new EntityTypeData(10, "minecraft:chicken", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:cow", new EntityTypeData(11, "minecraft:cow", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:pig", new EntityTypeData(12, "minecraft:pig", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:sheep", new EntityTypeData(13, "minecraft:sheep", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:wolf", new EntityTypeData(14, "minecraft:wolf", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:villager", new EntityTypeData(15, "minecraft:villager", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:mooshroom", new EntityTypeData(16, "minecraft:mooshroom", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:squid", new EntityTypeData(17, "minecraft:squid", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:rabbit", new EntityTypeData(18, "minecraft:rabbit", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:bat", new EntityTypeData(19, "minecraft:bat", false, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:iron_golem", new EntityTypeData(20, "minecraft:iron_golem", false, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:snow_golem", new EntityTypeData(21, "minecraft:snow_golem", false, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:ocelot", new EntityTypeData(22, "minecraft:ocelot", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:horse", new EntityTypeData(23, "minecraft:horse", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:donkey", new EntityTypeData(24, "minecraft:donkey", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:mule", new EntityTypeData(25, "minecraft:mule", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:skeleton_horse", new EntityTypeData(26, "minecraft:skeleton_horse", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:zombie_horse", new EntityTypeData(27, "minecraft:zombie_horse", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:polar_bear", new EntityTypeData(28, "minecraft:polar_bear", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:llama", new EntityTypeData(29, "minecraft:llama", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:parrot", new EntityTypeData(30, "minecraft:parrot", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:dolphin", new EntityTypeData(31, "minecraft:dolphin", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:zombie", new EntityTypeData(32, "minecraft:zombie", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:creeper", new EntityTypeData(33, "minecraft:creeper", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:skeleton", new EntityTypeData(34, "minecraft:skeleton", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:spider", new EntityTypeData(35, "minecraft:spider", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:zombie_pigman", new EntityTypeData(36, "minecraft:zombie_pigman", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:slime", new EntityTypeData(37, "minecraft:slime", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:enderman", new EntityTypeData(38, "minecraft:enderman", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:silverfish", new EntityTypeData(39, "minecraft:silverfish", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:cave_spider", new EntityTypeData(40, "minecraft:cave_spider", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:ghast", new EntityTypeData(41, "minecraft:ghast", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:magma_cube", new EntityTypeData(42, "minecraft:magma_cube", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:blaze", new EntityTypeData(43, "minecraft:blaze", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:zombie_villager", new EntityTypeData(44, "minecraft:zombie_villager", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:witch", new EntityTypeData(45, "minecraft:witch", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:stray", new EntityTypeData(46, "minecraft:stray", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:husk", new EntityTypeData(47, "minecraft:husk", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:wither_skeleton", new EntityTypeData(48, "minecraft:wither_skeleton", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:guardian", new EntityTypeData(49, "minecraft:guardian", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:elder_guardian", new EntityTypeData(50, "minecraft:elder_guardian", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:npc", new EntityTypeData(51, "minecraft:npc", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:wither", new EntityTypeData(52, "minecraft:wither", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:ender_dragon", new EntityTypeData(53, "minecraft:ender_dragon", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:shulker", new EntityTypeData(54, "minecraft:shulker", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:endermite", new EntityTypeData(55, "minecraft:endermite", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:agent", new EntityTypeData(56, "minecraft:agent", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:vindicator", new EntityTypeData(57, "minecraft:vindicator", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:phantom", new EntityTypeData(58, "minecraft:phantom", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:ravager", new EntityTypeData(58, "minecraft:ravager", true, false, true, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:armor_stand", new EntityTypeData(61, "minecraft:armor_stand", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:tripod_camera", new EntityTypeData(62, "minecraft:tripod_camera", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:player", new EntityTypeData(63, "minecraft:player", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:item", new EntityTypeData(64, "minecraft:item", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:tnt", new EntityTypeData(65, "minecraft:tnt", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:falling_block", new EntityTypeData(66, "minecraft:falling_block", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:xp_bottle", new EntityTypeData(68, "minecraft:xp_bottle", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:xp_orb", new EntityTypeData(69, "minecraft:xp_orb", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:eye_of_ender_signal", new EntityTypeData(70, "minecraft:eye_of_ender_signal", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:ender_crystal", new EntityTypeData(71, "minecraft:ender_crystal", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:fireworks_rocket", new EntityTypeData(72, "minecraft:fireworks_rocket", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:thrown_trident", new EntityTypeData(73, "minecraft:thrown_trident", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:turtle", new EntityTypeData(74, "minecraft:turtle", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:cat", new EntityTypeData(75, "minecraft:cat", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:shulker_bullet", new EntityTypeData(76, "minecraft:shulker_bullet", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:fishing_hook", new EntityTypeData(77, "minecraft:fishing_hook", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:dragon_fireball", new EntityTypeData(79, "minecraft:dragon_fireball", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:arrow", new EntityTypeData(80, "minecraft:arrow", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:snowball", new EntityTypeData(81, "minecraft:snowball", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:egg", new EntityTypeData(82, "minecraft:egg", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:painting", new EntityTypeData(83, "minecraft:painting", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:minecart", new EntityTypeData(84, "minecraft:minecart", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:fireball", new EntityTypeData(85, "minecraft:fireball", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:splash_potion", new EntityTypeData(86, "minecraft:splash_potion", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:ender_pearl", new EntityTypeData(87, "minecraft:ender_pearl", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:leash_knot", new EntityTypeData(88, "minecraft:leash_knot", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:wither_skull", new EntityTypeData(89, "minecraft:wither_skull", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:boat", new EntityTypeData(90, "minecraft:boat", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:wither_skull_dangerous", new EntityTypeData(91, "minecraft:wither_skull_dangerous", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:lightning_bolt", new EntityTypeData(93, "minecraft:lightning_bolt", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:small_fireball", new EntityTypeData(94, "minecraft:small_fireball", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:area_effect_cloud", new EntityTypeData(95, "minecraft:area_effect_cloud", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:hopper_minecart", new EntityTypeData(96, "minecraft:hopper_minecart", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:tnt_minecart", new EntityTypeData(97, "minecraft:tnt_minecart", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:chest_minecart", new EntityTypeData(98, "minecraft:chest_minecart", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:command_block_minecart", new EntityTypeData(100, "minecraft:command_block_minecart", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:lingering_potion", new EntityTypeData(101, "minecraft:lingering_potion", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:llama_spit", new EntityTypeData(102, "minecraft:llama_spit", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:evocation_fang", new EntityTypeData(103, "minecraft:evocation_fang", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:evocation_illager", new EntityTypeData(104, "minecraft:evocation_illager", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:vex", new EntityTypeData(105, "minecraft:vex", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:ice_bomb", new EntityTypeData(106, "minecraft:ice_bomb", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:balloon", new EntityTypeData(107, "minecraft:balloon", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:pufferfish", new EntityTypeData(108, "minecraft:pufferfish", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:salmon", new EntityTypeData(109, "minecraft:salmon", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:drowned", new EntityTypeData(110, "minecraft:drowned", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:tropicalfish", new EntityTypeData(111, "minecraft:tropicalfish", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:cod", new EntityTypeData(112, "minecraft:cod", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:panda", new EntityTypeData(113, "minecraft:panda", false, false, false, ":"));
        ENTITY_CONFIG_DATA.put("minecraft:pillager", new EntityTypeData(113, "minecraft:pillager", false, false, false, ":"));
    }
}
