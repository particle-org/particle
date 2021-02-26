package com.particle.model.block.types;


import com.particle.core.ecs.GameObject;
import com.particle.model.block.element.BlockElement;
import com.particle.model.block.geometry.BlockGeometry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public enum BlockPrototype {
    AIR(0, "minecraft:air", 1, BlockElement.DEFAULT, BlockGeometry.EMPTY),
    STONE(1, "minecraft:stone", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    GRASS(2, "minecraft:grass", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    DIRT(3, "minecraft:dirt", 2, BlockElement.DIRT, BlockGeometry.SOLID),
    COBBLESTONE(4, "minecraft:cobblestone", 1, BlockElement.ROCK1, BlockGeometry.SOLID),
    PLANKS(5, "minecraft:planks", 8, BlockElement.WOOD, BlockGeometry.SOLID),
    SAPLING(6, "minecraft:sapling", 16, BlockElement.DEFAULT, BlockGeometry.EMPTY),
    BEDROCK(7, "minecraft:bedrock", 2, BlockElement.DEFAULT, BlockGeometry.SOLID),
    FLOWING_WATER(8, "minecraft:flowing_water", 16, BlockElement.LIQUID, BlockGeometry.EMPTY),
    WATER(9, "minecraft:water", 16, BlockElement.LIQUID, BlockGeometry.EMPTY),
    FLOWING_LAVA(10, "minecraft:flowing_lava", 16, BlockElement.LIQUID, BlockGeometry.EMPTY),
    LAVA(11, "minecraft:lava", 16, BlockElement.LIQUID, BlockGeometry.EMPTY),
    SAND(12, "minecraft:sand", 2, BlockElement.SAND, BlockGeometry.SOLID),
    GRAVEL(13, "minecraft:gravel", 1, BlockElement.SAND, BlockGeometry.SOLID),
    GOLD_ORE(14, "minecraft:gold_ore", 1, BlockElement.ROCK3, BlockGeometry.SOLID),
    IRON_ORE(15, "minecraft:iron_ore", 1, BlockElement.ROCK2, BlockGeometry.SOLID),
    COAL_ORE(16, "minecraft:coal_ore", 1, BlockElement.ROCK1, BlockGeometry.SOLID),
    LOG(17, "minecraft:log", 12, BlockElement.WOOD, BlockGeometry.SOLID),
    LEAVES(18, "minecraft:leaves", 16, BlockElement.LEAVES, BlockGeometry.SOLID),
    SPONGE(19, "minecraft:sponge", 2, BlockElement.DEFAULT, BlockGeometry.SOLID),
    GLASS(20, "minecraft:glass", 1, BlockElement.DEFAULT, BlockGeometry.SOLID),
    LAPIS_ORE(21, "minecraft:lapis_ore", 1, BlockElement.ROCK2, BlockGeometry.SOLID),
    LAPIS_BLOCK(22, "minecraft:lapis_block", 1, BlockElement.METAL2, BlockGeometry.SOLID),
    DISPENSER(23, "minecraft:dispenser", 16, BlockElement.ROCK1, BlockGeometry.SOLID),
    SANDSTONE(24, "minecraft:sandstone", 4, BlockElement.ROCK1, BlockGeometry.SOLID),
    NOTEBLOCK(25, "minecraft:noteblock", 1, BlockElement.WOOD, BlockGeometry.SOLID),
    BED(26, "minecraft:bed", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    GOLDEN_RAIL(27, "minecraft:golden_rail", 16, BlockElement.RAIL, BlockGeometry.MODEL),
    DETECTOR_RAIL(28, "minecraft:detector_rail", 16, BlockElement.RAIL, BlockGeometry.MODEL),
    STICKY_PISTON(29, "minecraft:sticky_piston", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    WEB(30, "minecraft:web", 1, BlockElement.DEFAULT, BlockGeometry.MODEL),
    TALLGRASS(31, "minecraft:tallgrass", 4, BlockElement.DEFAULT, BlockGeometry.EMPTY),
    DEADBUSH(32, "minecraft:deadbush", 1, BlockElement.DEFAULT, BlockGeometry.EMPTY),
    PISTON(33, "minecraft:piston", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    PISTONARMCOLLISION(34, "minecraft:pistonArmCollision", 8, BlockElement.DEFAULT, BlockGeometry.SOLID),
    WOOL(35, "minecraft:wool", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_0(36, "minecraft:element_0", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    YELLOW_FLOWER(37, "minecraft:yellow_flower", 16, BlockElement.PLANT, BlockGeometry.EMPTY),
    RED_FLOWER(38, "minecraft:red_flower", 16, BlockElement.PLANT, BlockGeometry.EMPTY),
    BROWN_MUSHROOM(39, "minecraft:brown_mushroom", 1, BlockElement.DEFAULT, BlockGeometry.MODEL),
    RED_MUSHROOM(40, "minecraft:red_mushroom", 1, BlockElement.DEFAULT, BlockGeometry.MODEL),
    GOLD_BLOCK(41, "minecraft:gold_block", 1, BlockElement.METAL3, BlockGeometry.SOLID),
    IRON_BLOCK(42, "minecraft:iron_block", 1, BlockElement.METAL2, BlockGeometry.SOLID),
    DOUBLE_STONE_SLAB(43, "minecraft:double_stone_slab", 16, BlockElement.ROCK1, BlockGeometry.SOLID),
    STONE_SLAB(44, "minecraft:stone_slab", 16, BlockElement.ROCK1, BlockGeometry.SOLID),
    BRICK_BLOCK(45, "minecraft:brick_block", 1, BlockElement.ROCK1, BlockGeometry.SOLID),
    TNT(46, "minecraft:tnt", 4, BlockElement.DEFAULT, BlockGeometry.SOLID),
    BOOKSHELF(47, "minecraft:bookshelf", 1, BlockElement.WOOD, BlockGeometry.SOLID),
    MOSSY_COBBLESTONE(48, "minecraft:mossy_cobblestone", 1, BlockElement.ROCK1, BlockGeometry.SOLID),
    OBSIDIAN(49, "minecraft:obsidian", 1, BlockElement.ROCK4, BlockGeometry.SOLID),
    TORCH(50, "minecraft:torch", 8, BlockElement.DEFAULT, BlockGeometry.EMPTY),
    FIRE(51, "minecraft:fire", 16, BlockElement.DEFAULT, BlockGeometry.EMPTY),
    MOB_SPAWNER(52, "minecraft:mob_spawner", 1, BlockElement.ROCK1, BlockGeometry.SOLID),
    OAK_STAIRS(53, "minecraft:oak_stairs", 8, BlockElement.WOOD, BlockGeometry.SOLID),
    CHEST(54, "minecraft:chest", 8, BlockElement.WOOD, BlockGeometry.SOLID),
    REDSTONE_WIRE(55, "minecraft:redstone_wire", 16, BlockElement.DEFAULT, BlockGeometry.MODEL),
    DIAMOND_ORE(56, "minecraft:diamond_ore", 1, BlockElement.ROCK3, BlockGeometry.SOLID),
    DIAMOND_BLOCK(57, "minecraft:diamond_block", 1, BlockElement.METAL3, BlockGeometry.SOLID),
    CRAFTING_TABLE(58, "minecraft:crafting_table", 1, BlockElement.WOOD, BlockGeometry.SOLID),
    WHEAT(59, "minecraft:wheat", 8, BlockElement.DEFAULT, BlockGeometry.EMPTY),
    FARMLAND(60, "minecraft:farmland", 8, BlockElement.DIRT, BlockGeometry.SOLID),
    FURNACE(61, "minecraft:furnace", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    LIT_FURNACE(62, "minecraft:lit_furnace", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    STANDING_SIGN(63, "minecraft:standing_sign", 16, BlockElement.WOOD, BlockGeometry.EMPTY),
    WOODEN_DOOR(64, "minecraft:wooden_door", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    LADDER(65, "minecraft:ladder", 8, BlockElement.WOOD, BlockGeometry.MODEL),
    RAIL(66, "minecraft:rail", 16, BlockElement.RAIL, BlockGeometry.MODEL),
    STONE_STAIRS(67, "minecraft:stone_stairs", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    WALL_SIGN(68, "minecraft:wall_sign", 16, BlockElement.WOOD, BlockGeometry.EMPTY),
    LEVER(69, "minecraft:lever", 16, BlockElement.ROCK0, BlockGeometry.EMPTY),
    STONE_PRESSURE_PLATE(70, "minecraft:stone_pressure_plate", 16, BlockElement.ROCK1, BlockGeometry.MODEL),
    IRON_DOOR(71, "minecraft:iron_door", 16, BlockElement.METAL0, BlockGeometry.MODEL),
    WOODEN_PRESSURE_PLATE(72, "minecraft:wooden_pressure_plate", 16, BlockElement.WOOD, BlockGeometry.MODEL),
    REDSTONE_ORE(73, "minecraft:redstone_ore", 1, BlockElement.ROCK3, BlockGeometry.SOLID),
    LIT_REDSTONE_ORE(74, "minecraft:lit_redstone_ore", 1, BlockElement.ROCK3, BlockGeometry.SOLID),
    UNLIT_REDSTONE_TORCH(75, "minecraft:unlit_redstone_torch", 8, BlockElement.DEFAULT, BlockGeometry.EMPTY),
    REDSTONE_TORCH(76, "minecraft:redstone_torch", 8, BlockElement.DEFAULT, BlockGeometry.EMPTY),
    STONE_BUTTON(77, "minecraft:stone_button", 16, BlockElement.ROCK0, BlockGeometry.EMPTY),
    SNOW_LAYER(78, "minecraft:snow_layer", 16, BlockElement.SNOW, BlockGeometry.EMPTY),
    ICE(79, "minecraft:ice", 1, BlockElement.DEFAULT, BlockGeometry.SOLID),
    SNOW(80, "minecraft:snow", 1, BlockElement.DEFAULT, BlockGeometry.SOLID),
    CACTUS(81, "minecraft:cactus", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    CLAY(82, "minecraft:clay", 1, BlockElement.DIRT, BlockGeometry.SOLID),
    REEDS(83, "minecraft:reeds", 16, BlockElement.DEFAULT, BlockGeometry.MODEL),
    JUKEBOX(84, "minecraft:jukebox", 1, BlockElement.WOOD, BlockGeometry.SOLID),
    FENCE(85, "minecraft:fence", 8, BlockElement.WOOD, BlockGeometry.SOLID),
    PUMPKIN(86, "minecraft:pumpkin", 4, BlockElement.WOOD, BlockGeometry.SOLID),
    NETHERRACK(87, "minecraft:netherrack", 1, BlockElement.PICKAXE, BlockGeometry.SOLID),
    SOUL_SAND(88, "minecraft:soul_sand", 1, BlockElement.DIRT, BlockGeometry.SOLID),
    GLOWSTONE(89, "minecraft:glowstone", 1, BlockElement.DEFAULT, BlockGeometry.SOLID),
    PORTAL(90, "minecraft:portal", 4, BlockElement.DEFAULT, BlockGeometry.SOLID),
    LIT_PUMPKIN(91, "minecraft:lit_pumpkin", 4, BlockElement.WOOD, BlockGeometry.SOLID),
    CAKE(92, "minecraft:cake", 8, BlockElement.DEFAULT, BlockGeometry.SOLID),
    UNPOWERED_REPEATER(93, "minecraft:unpowered_repeater", 16, BlockElement.DEFAULT, BlockGeometry.MODEL),
    POWERED_REPEATER(94, "minecraft:powered_repeater", 16, BlockElement.DEFAULT, BlockGeometry.MODEL),
    INVISIBLEBEDROCK(95, "minecraft:invisibleBedrock", 1, BlockElement.DEFAULT, BlockGeometry.SOLID),
    TRAPDOOR(96, "minecraft:trapdoor", 16, BlockElement.WOOD, BlockGeometry.MODEL),
    MONSTER_EGG(97, "minecraft:monster_egg", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    STONEBRICK(98, "minecraft:stonebrick", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    BROWN_MUSHROOM_BLOCK(99, "minecraft:brown_mushroom_block", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    RED_MUSHROOM_BLOCK(100, "minecraft:red_mushroom_block", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    IRON_BARS(101, "minecraft:iron_bars", 1, BlockElement.METAL0, BlockGeometry.SOLID),
    GLASS_PANE(102, "minecraft:glass_pane", 1, BlockElement.GLASS, BlockGeometry.SOLID),
    MELON_BLOCK(103, "minecraft:melon_block", 1, BlockElement.WOOD, BlockGeometry.SOLID),
    PUMPKIN_STEM(104, "minecraft:pumpkin_stem", 8, BlockElement.PLANT, BlockGeometry.EMPTY),
    MELON_STEM(105, "minecraft:melon_stem", 8, BlockElement.PLANT, BlockGeometry.EMPTY),
    VINE(106, "minecraft:vine", 16, BlockElement.DEFAULT, BlockGeometry.EMPTY),
    FENCE_GATE(107, "minecraft:fence_gate", 16, BlockElement.WOOD, BlockGeometry.SOLID),
    BRICK_STAIRS(108, "minecraft:brick_stairs", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    STONE_BRICK_STAIRS(109, "minecraft:stone_brick_stairs", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    MYCELIUM(110, "minecraft:mycelium", 1, BlockElement.DIRT, BlockGeometry.SOLID),
    WATERLILY(111, "minecraft:waterlily", 1, BlockElement.DEFAULT, BlockGeometry.SOLID),
    NETHER_BRICK(112, "minecraft:nether_brick", 1, BlockElement.ROCK1, BlockGeometry.SOLID),
    NETHER_BRICK_FENCE(113, "minecraft:nether_brick_fence", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    NETHER_BRICK_STAIRS(114, "minecraft:nether_brick_stairs", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    NETHER_WART(115, "minecraft:nether_wart", 4, BlockElement.DEFAULT, BlockGeometry.EMPTY),
    ENCHANTING_TABLE(116, "minecraft:enchanting_table", 1, BlockElement.ROCK1, BlockGeometry.SOLID),
    BREWING_STAND(117, "minecraft:brewing_stand", 8, BlockElement.ROCK0, BlockGeometry.SOLID),
    CAULDRON(118, "minecraft:cauldron", 8, BlockElement.METAL1, BlockGeometry.SOLID),
    END_PORTAL(119, "minecraft:end_portal", 1, BlockElement.BEDROCK, BlockGeometry.EMPTY),
    END_PORTAL_FRAME(120, "minecraft:end_portal_frame", 8, BlockElement.BEDROCK, BlockGeometry.SOLID),
    END_STONE(121, "minecraft:end_stone", 1, BlockElement.ROCK1, BlockGeometry.SOLID),
    DRAGON_EGG(122, "minecraft:dragon_egg", 1, BlockElement.ROCK0, BlockGeometry.SOLID),
    REDSTONE_LAMP(123, "minecraft:redstone_lamp", 1, BlockElement.DEFAULT, BlockGeometry.SOLID),
    LIT_REDSTONE_LAMP(124, "minecraft:lit_redstone_lamp", 1, BlockElement.DEFAULT, BlockGeometry.SOLID),
    DROPPER(125, "minecraft:dropper", 16, BlockElement.ROCK1, BlockGeometry.SOLID),
    ACTIVATOR_RAIL(126, "minecraft:activator_rail", 16, BlockElement.RAIL, BlockGeometry.SOLID),
    COCOA(127, "minecraft:cocoa", 16, BlockElement.WOOD, BlockGeometry.SOLID),
    SANDSTONE_STAIRS(128, "minecraft:sandstone_stairs", 8, BlockElement.WOOD, BlockGeometry.SOLID),
    EMERALD_ORE(129, "minecraft:emerald_ore", 1, BlockElement.ROCK3, BlockGeometry.SOLID),
    ENDER_CHEST(130, "minecraft:ender_chest", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    TRIPWIRE_HOOK(131, "minecraft:tripwire_hook", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    TRIPWIRE(132, "minecraft:tripWire", 16, BlockElement.DEFAULT, BlockGeometry.MODEL),
    EMERALD_BLOCK(133, "minecraft:emerald_block", 1, BlockElement.METAL3, BlockGeometry.SOLID),
    SPRUCE_STAIRS(134, "minecraft:spruce_stairs", 8, BlockElement.WOOD, BlockGeometry.SOLID),
    BIRCH_STAIRS(135, "minecraft:birch_stairs", 8, BlockElement.WOOD, BlockGeometry.SOLID),
    JUNGLE_STAIRS(136, "minecraft:jungle_stairs", 8, BlockElement.WOOD, BlockGeometry.SOLID),
    COMMAND_BLOCK(137, "minecraft:command_block", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    BEACON(138, "minecraft:beacon", 1, BlockElement.METAL0, BlockGeometry.SOLID),
    COBBLESTONE_WALL(139, "minecraft:cobblestone_wall", 2, BlockElement.ROCK0, BlockGeometry.SOLID),
    FLOWER_POT(140, "minecraft:flower_pot", 2, BlockElement.DEFAULT, BlockGeometry.SOLID),
    CARROTS(141, "minecraft:carrots", 8, BlockElement.PLANT, BlockGeometry.EMPTY),
    POTATOES(142, "minecraft:potatoes", 8, BlockElement.PLANT, BlockGeometry.EMPTY),
    WOODEN_BUTTON(143, "minecraft:wooden_button", 16, BlockElement.WOOD, BlockGeometry.EMPTY),
    SKULL(144, "minecraft:skull", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ANVIL(145, "minecraft:anvil", 16, BlockElement.METAL1, BlockGeometry.SOLID),
    TRAPPED_CHEST(146, "minecraft:trapped_chest", 8, BlockElement.WOOD, BlockGeometry.SOLID),
    LIGHT_WEIGHTED_PRESSURE_PLATE(147, "minecraft:light_weighted_pressure_plate", 16, BlockElement.METAL0, BlockGeometry.MODEL),
    HEAVY_WEIGHTED_PRESSURE_PLATE(148, "minecraft:heavy_weighted_pressure_plate", 16, BlockElement.METAL0, BlockGeometry.MODEL),
    UNPOWERED_COMPARATOR(149, "minecraft:unpowered_comparator", 16, BlockElement.ROCK0, BlockGeometry.MODEL),
    POWERED_COMPARATOR(150, "minecraft:powered_comparator", 16, BlockElement.ROCK0, BlockGeometry.MODEL),
    DAYLIGHT_DETECTOR(151, "minecraft:daylight_detector", 16, BlockElement.WOOD, BlockGeometry.MODEL),
    REDSTONE_BLOCK(152, "minecraft:redstone_block", 1, BlockElement.METAL1, BlockGeometry.SOLID),
    QUARTZ_ORE(153, "minecraft:quartz_ore", 1, BlockElement.ROCK1, BlockGeometry.SOLID),
    HOPPER(154, "minecraft:hopper", 16, BlockElement.METAL1, BlockGeometry.SOLID),
    QUARTZ_BLOCK(155, "minecraft:quartz_block", 16, BlockElement.ROCK1, BlockGeometry.SOLID),
    QUARTZ_STAIRS(156, "minecraft:quartz_stairs", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    DOUBLE_WOODEN_SLAB(157, "minecraft:double_wooden_slab", 16, BlockElement.WOOD, BlockGeometry.SOLID),
    WOODEN_SLAB(158, "minecraft:wooden_slab", 16, BlockElement.WOOD, BlockGeometry.SOLID),
    STAINED_HARDENED_CLAY(159, "minecraft:stained_hardened_clay", 16, BlockElement.DIRT, BlockGeometry.SOLID),
    STAINED_GLASS_PANE(160, "minecraft:stained_glass_pane", 16, BlockElement.GLASS, BlockGeometry.SOLID),
    LEAVES2(161, "minecraft:leaves2", 16, BlockElement.LEAVES, BlockGeometry.SOLID),
    LOG2(162, "minecraft:log2", 16, BlockElement.WOOD, BlockGeometry.SOLID),
    ACACIA_STAIRS(163, "minecraft:acacia_stairs", 8, BlockElement.WOOD, BlockGeometry.SOLID),
    DARK_OAK_STAIRS(164, "minecraft:dark_oak_stairs", 8, BlockElement.WOOD, BlockGeometry.SOLID),
    SLIME(165, "minecraft:slime", 1, BlockElement.DEFAULT, BlockGeometry.SOLID),
    // 1.8之后，glow_stick不存在了，用氢元素代替，不能直接不发
    GLOW_STICK(166, "minecraft:glow_stick", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    IRON_TRAPDOOR(167, "minecraft:iron_trapdoor", 16, BlockElement.METAL0, BlockGeometry.MODEL),
    PRISMARINE(168, "minecraft:prismarine", 4, BlockElement.ROCK1, BlockGeometry.SOLID),
    SEALANTERN(169, "minecraft:seaLantern", 1, BlockElement.DEFAULT, BlockGeometry.SOLID),
    HAY_BLOCK(170, "minecraft:hay_block", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    CARPET(171, "minecraft:carpet", 16, BlockElement.DEFAULT, BlockGeometry.MODEL),
    HARDENED_CLAY(172, "minecraft:hardened_clay", 1, BlockElement.DEFAULT, BlockGeometry.SOLID),
    COAL_BLOCK(173, "minecraft:coal_block", 1, BlockElement.ROCK0, BlockGeometry.SOLID),
    PACKED_ICE(174, "minecraft:packed_ice", 1, BlockElement.ICE, BlockGeometry.SOLID),
    DOUBLE_PLANT(175, "minecraft:double_plant", 16, BlockElement.PLANT, BlockGeometry.EMPTY),
    STANDING_BANNER(176, "minecraft:standing_banner", 16, BlockElement.WOOD, BlockGeometry.EMPTY),
    WALL_BANNER(177, "minecraft:wall_banner", 8, BlockElement.WOOD, BlockGeometry.EMPTY),
    DAYLIGHT_DETECTOR_INVERTED(178, "minecraft:daylight_detector_inverted", 16, BlockElement.WOOD, BlockGeometry.MODEL),
    RED_SANDSTONE(179, "minecraft:red_sandstone", 4, BlockElement.ROCK1, BlockGeometry.SOLID),
    RED_SANDSTONE_STAIRS(180, "minecraft:red_sandstone_stairs", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    DOUBLE_STONE_SLAB2(181, "minecraft:double_stone_slab2", 16, BlockElement.ROCK1, BlockGeometry.SOLID),
    STONE_SLAB2(182, "minecraft:stone_slab2", 16, BlockElement.ROCK1, BlockGeometry.SOLID),
    SPRUCE_FENCE_GATE(183, "minecraft:spruce_fence_gate", 16, BlockElement.WOOD, BlockGeometry.SOLID),
    BIRCH_FENCE_GATE(184, "minecraft:birch_fence_gate", 16, BlockElement.WOOD, BlockGeometry.SOLID),
    JUNGLE_FENCE_GATE(185, "minecraft:jungle_fence_gate", 16, BlockElement.WOOD, BlockGeometry.SOLID),
    DARK_OAK_FENCE_GATE(186, "minecraft:dark_oak_fence_gate", 16, BlockElement.WOOD, BlockGeometry.SOLID),
    ACACIA_FENCE_GATE(187, "minecraft:acacia_fence_gate", 16, BlockElement.WOOD, BlockGeometry.SOLID),
    REPEATING_COMMAND_BLOCK(188, "minecraft:repeating_command_block", 16, BlockElement.BEDROCK, BlockGeometry.SOLID),
    CHAIN_COMMAND_BLOCK(189, "minecraft:chain_command_block", 16, BlockElement.BEDROCK, BlockGeometry.SOLID),
    HARD_GLASS_PANE(190, "minecraft:hard_glass_pane", 1, BlockElement.GLASS, BlockGeometry.SOLID),
    HARD_STAINED_GLASS_PANE(191, "minecraft:hard_stained_glass_pane", 16, BlockElement.GLASS, BlockGeometry.SOLID),
    CHEMICAL_HEAT(192, "minecraft:chemical_heat", 1, BlockElement.PICKAXE, BlockGeometry.SOLID),
    SPRUCE_DOOR(193, "minecraft:spruce_door", 16, BlockElement.WOOD, BlockGeometry.MODEL),
    BIRCH_DOOR(194, "minecraft:birch_door", 16, BlockElement.WOOD, BlockGeometry.MODEL),
    JUNGLE_DOOR(195, "minecraft:jungle_door", 16, BlockElement.WOOD, BlockGeometry.MODEL),
    ACACIA_DOOR(196, "minecraft:acacia_door", 16, BlockElement.WOOD, BlockGeometry.MODEL),
    DARK_OAK_DOOR(197, "minecraft:dark_oak_door", 16, BlockElement.WOOD, BlockGeometry.MODEL),
    GRASS_PATH(198, "minecraft:grass_path", 1, BlockElement.DIRT, BlockGeometry.SOLID),
    FRAME(199, "minecraft:frame", 8, BlockElement.DEFAULT, BlockGeometry.SOLID),
    CHORUS_FLOWER(200, "minecraft:chorus_flower", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    PURPUR_BLOCK(201, "minecraft:purpur_block", 16, BlockElement.ROCK1, BlockGeometry.SOLID),
    COLORED_TORCH_RG(202, "minecraft:colored_torch_rg", 16, BlockElement.DEFAULT, BlockGeometry.EMPTY),
    PURPUR_STAIRS(203, "minecraft:purpur_stairs", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    COLORED_TORCH_BP(204, "minecraft:colored_torch_bp", 16, BlockElement.DEFAULT, BlockGeometry.EMPTY),
    UNDYED_SHULKER_BOX(205, "minecraft:undyed_shulker_box", 1, BlockElement.ROCK1, BlockGeometry.SOLID),
    END_BRICKS(206, "minecraft:end_bricks", 1, BlockElement.ROCK1, BlockGeometry.SOLID),
    FROSTED_ICE(207, "minecraft:frosted_ice", 4, BlockElement.ICE, BlockGeometry.SOLID),
    END_ROD(208, "minecraft:end_rod", 8, BlockElement.DEFAULT, BlockGeometry.SOLID),
    END_GATEWAY(209, "minecraft:end_gateway", 1, BlockElement.BEDROCK, BlockGeometry.SOLID),
    // todo --用氢元素占位
    T_210(210, "minecraft:element_1", 1, BlockElement.DEFAULT, BlockGeometry.SOLID),
    // todo --用氢元素占位
    T_211(211, "minecraft:element_1", 1, BlockElement.DEFAULT, BlockGeometry.SOLID),
    // todo --用氢元素占位
    T_212(212, "minecraft:element_1", 1, BlockElement.DEFAULT, BlockGeometry.SOLID),
    MAGMA(213, "minecraft:magma", 1, BlockElement.ROCK1, BlockGeometry.SOLID),
    NETHER_WART_BLOCK(214, "minecraft:nether_wart_block", 1, BlockElement.ROCK1, BlockGeometry.SOLID),
    RED_NETHER_BRICK(215, "minecraft:red_nether_brick", 1, BlockElement.ROCK1, BlockGeometry.SOLID),
    BONE_BLOCK(216, "minecraft:bone_block", 16, BlockElement.ROCK1, BlockGeometry.SOLID),
    // todo --wiki上不存在此方块 --用氢元素占位
    T_217(217, "minecraft:element_1", 1, BlockElement.DEFAULT, BlockGeometry.SOLID),
    SHULKER_BOX(218, "minecraft:shulker_box", 16, BlockElement.ROCK1, BlockGeometry.SOLID),
    PURPLE_GLAZED_TERRACOTTA(219, "minecraft:purple_glazed_terracotta", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    WHITE_GLAZED_TERRACOTTA(220, "minecraft:white_glazed_terracotta", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    ORANGE_GLAZED_TERRACOTTA(221, "minecraft:orange_glazed_terracotta", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    MAGENTA_GLAZED_TERRACOTTA(222, "minecraft:magenta_glazed_terracotta", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    LIGHT_BLUE_GLAZED_TERRACOTTA(223, "minecraft:light_blue_glazed_terracotta", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    YELLOW_GLAZED_TERRACOTTA(224, "minecraft:yellow_glazed_terracotta", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    LIME_GLAZED_TERRACOTTA(225, "minecraft:lime_glazed_terracotta", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    PINK_GLAZED_TERRACOTTA(226, "minecraft:pink_glazed_terracotta", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    GRAY_GLAZED_TERRACOTTA(227, "minecraft:gray_glazed_terracotta", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    SILVER_GLAZED_TERRACOTTA(228, "minecraft:silver_glazed_terracotta", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    CYAN_GLAZED_TERRACOTTA(229, "minecraft:cyan_glazed_terracotta", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    // todo mod_ore部分客户端也不识别-- 用氢元素占位
    MOD_ORE_PLACE(230, "minecraft:element_1", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    BLUE_GLAZED_TERRACOTTA(231, "minecraft:blue_glazed_terracotta", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    BROWN_GLAZED_TERRACOTTA(232, "minecraft:brown_glazed_terracotta", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    GREEN_GLAZED_TERRACOTTA(233, "minecraft:green_glazed_terracotta", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    RED_GLAZED_TERRACOTTA(234, "minecraft:red_glazed_terracotta", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    BLACK_GLAZED_TERRACOTTA(235, "minecraft:black_glazed_terracotta", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    CONCRETE(236, "minecraft:concrete", 16, BlockElement.ROCK1, BlockGeometry.SOLID),
    CONCRETEPOWDER(237, "minecraft:concretePowder", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    CHEMISTRY_TABLE(238, "minecraft:chemistry_table", 16, BlockElement.ROCK1, BlockGeometry.SOLID),
    UNDERWATER_TORCH(239, "minecraft:underwater_torch", 8, BlockElement.DEFAULT, BlockGeometry.EMPTY),
    CHORUS_PLANT(240, "minecraft:chorus_plant", 1, BlockElement.PLANT, BlockGeometry.SOLID),
    STAINED_GLASS(241, "minecraft:stained_glass", 16, BlockElement.GLASS, BlockGeometry.SOLID),
    // todo --用氢元素占位
    T_242(242, "minecraft:element_1", 1, BlockElement.DEFAULT, BlockGeometry.SOLID),
    PODZOL(243, "minecraft:podzol", 1, BlockElement.DIRT, BlockGeometry.SOLID),
    BEETROOT(244, "minecraft:beetroot", 8, BlockElement.DEFAULT, BlockGeometry.EMPTY),
    STONECUTTER(245, "minecraft:stonecutter", 1, BlockElement.ROCK1, BlockGeometry.SOLID),
    GLOWINGOBSIDIAN(246, "minecraft:glowingobsidian", 1, BlockElement.ROCK1, BlockGeometry.SOLID),
    NETHERREACTOR(247, "minecraft:netherreactor", 1, BlockElement.ROCK1, BlockGeometry.SOLID),
    INFO_UPDATE(248, "minecraft:info_update", 1, BlockElement.DEFAULT, BlockGeometry.SOLID),
    INFO_UPDATE2(249, "minecraft:info_update2", 1, BlockElement.DEFAULT, BlockGeometry.SOLID),
    MOVINGBLOCK(250, "minecraft:movingBlock", 1, BlockElement.DEFAULT, BlockGeometry.SOLID),
    OBSERVER(251, "minecraft:observer", 16, BlockElement.ROCK1, BlockGeometry.SOLID),
    STRUCTURE_BLOCK(252, "minecraft:structure_block", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    HARD_GLASS(253, "minecraft:hard_glass", 1, BlockElement.GLASS, BlockGeometry.SOLID),
    HARD_STAINED_GLASS(254, "minecraft:hard_stained_glass", 16, BlockElement.GLASS, BlockGeometry.SOLID),
    RESERVED6(255, "minecraft:reserved6", 1, BlockElement.DEFAULT, BlockGeometry.SOLID),

    PRISMARINE_STAIRS(257, "minecraft:prismarine_stairs", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    DARK_PRISMARINE_STAIRS(258, "minecraft:dark_prismarine_stairs", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    PRISMARINE_BRICKS_STAIRS(259, "minecraft:prismarine_bricks_stairs", 8, BlockElement.ROCK1, BlockGeometry.SOLID),

    // 以下是新方块
    STRIPPED_SPRUCE_LOG(-5, "minecraft:stripped_spruce_log", 4, BlockElement.WOOD, BlockGeometry.SOLID),
    STRIPPED_BIRCH_LOG(-6, "minecraft:stripped_birch_log", 4, BlockElement.WOOD, BlockGeometry.SOLID),
    STRIPPED_JUNGLE_LOG(-7, "minecraft:stripped_jungle_log", 4, BlockElement.WOOD, BlockGeometry.SOLID),
    STRIPPED_ACACIA_LOG(-8, "minecraft:stripped_acacia_log", 4, BlockElement.WOOD, BlockGeometry.SOLID),
    STRIPPED_DARK_OAK_LOG(-9, "minecraft:stripped_dark_oak_log", 4, BlockElement.WOOD, BlockGeometry.SOLID),
    STRIPPED_OAK_LOG(-10, "minecraft:stripped_oak_log", 4, BlockElement.WOOD, BlockGeometry.SOLID),
    BLUE_ICE(-11, "minecraft:blue_ice", 1, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_1(-12, "minecraft:element_1", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_2(-13, "minecraft:element_2", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_3(-14, "minecraft:element_3", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_4(-15, "minecraft:element_4", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_5(-16, "minecraft:element_5", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_6(-17, "minecraft:element_6", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_7(-18, "minecraft:element_7", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_8(-19, "minecraft:element_8", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_9(-20, "minecraft:element_9", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_10(-21, "minecraft:element_10", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_11(-22, "minecraft:element_11", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_12(-23, "minecraft:element_12", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_13(-24, "minecraft:element_13", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_14(-25, "minecraft:element_14", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_15(-26, "minecraft:element_15", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_16(-27, "minecraft:element_16", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_17(-28, "minecraft:element_17", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_18(-29, "minecraft:element_18", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_19(-30, "minecraft:element_19", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_20(-31, "minecraft:element_20", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_21(-32, "minecraft:element_21", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_22(-33, "minecraft:element_22", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_23(-34, "minecraft:element_23", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_24(-35, "minecraft:element_24", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_25(-36, "minecraft:element_25", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_26(-37, "minecraft:element_26", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_27(-38, "minecraft:element_27", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_28(-39, "minecraft:element_28", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_29(-40, "minecraft:element_29", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_30(-41, "minecraft:element_30", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_31(-42, "minecraft:element_31", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_32(-43, "minecraft:element_32", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_33(-44, "minecraft:element_33", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_34(-45, "minecraft:element_34", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_35(-46, "minecraft:element_35", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_36(-47, "minecraft:element_36", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_37(-48, "minecraft:element_37", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_38(-49, "minecraft:element_38", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_39(-50, "minecraft:element_39", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_40(-51, "minecraft:element_40", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_41(-52, "minecraft:element_41", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_42(-53, "minecraft:element_42", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_43(-54, "minecraft:element_43", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_44(-55, "minecraft:element_44", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_45(-56, "minecraft:element_45", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_46(-57, "minecraft:element_46", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_47(-58, "minecraft:element_47", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_48(-59, "minecraft:element_48", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_49(-60, "minecraft:element_49", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_50(-61, "minecraft:element_50", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_51(-62, "minecraft:element_51", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_52(-63, "minecraft:element_52", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_53(-64, "minecraft:element_53", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_54(-65, "minecraft:element_54", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_55(-66, "minecraft:element_55", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_56(-67, "minecraft:element_56", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_57(-68, "minecraft:element_57", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_58(-69, "minecraft:element_58", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_59(-70, "minecraft:element_59", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_60(-71, "minecraft:element_60", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_61(-72, "minecraft:element_61", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_62(-73, "minecraft:element_62", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_63(-74, "minecraft:element_63", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_64(-75, "minecraft:element_64", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_65(-76, "minecraft:element_65", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_66(-77, "minecraft:element_66", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_67(-78, "minecraft:element_67", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_68(-79, "minecraft:element_68", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_69(-80, "minecraft:element_69", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_70(-81, "minecraft:element_70", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_71(-82, "minecraft:element_71", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_72(-83, "minecraft:element_72", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_73(-84, "minecraft:element_73", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_74(-85, "minecraft:element_74", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_75(-86, "minecraft:element_75", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_76(-87, "minecraft:element_76", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_77(-88, "minecraft:element_77", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_78(-89, "minecraft:element_78", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_79(-90, "minecraft:element_79", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_80(-91, "minecraft:element_80", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_81(-92, "minecraft:element_81", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_82(-93, "minecraft:element_82", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_83(-94, "minecraft:element_83", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_84(-95, "minecraft:element_84", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_85(-96, "minecraft:element_85", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_86(-97, "minecraft:element_86", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_87(-98, "minecraft:element_87", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_88(-99, "minecraft:element_88", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_89(-100, "minecraft:element_89", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_90(-101, "minecraft:element_90", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_91(-102, "minecraft:element_91", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_92(-103, "minecraft:element_92", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_93(-104, "minecraft:element_93", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_94(-105, "minecraft:element_94", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_95(-106, "minecraft:element_95", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_96(-107, "minecraft:element_96", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_97(-108, "minecraft:element_97", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_98(-109, "minecraft:element_98", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_99(-110, "minecraft:element_99", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_100(-111, "minecraft:element_100", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_101(-112, "minecraft:element_101", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_102(-113, "minecraft:element_102", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_103(-114, "minecraft:element_103", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_104(-115, "minecraft:element_104", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_105(-116, "minecraft:element_105", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_106(-117, "minecraft:element_106", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_107(-118, "minecraft:element_107", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_108(-119, "minecraft:element_108", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_109(-120, "minecraft:element_109", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_110(-121, "minecraft:element_110", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_111(-122, "minecraft:element_111", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_112(-123, "minecraft:element_112", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_113(-124, "minecraft:element_113", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_114(-125, "minecraft:element_114", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_115(-126, "minecraft:element_115", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_116(-127, "minecraft:element_116", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_117(-128, "minecraft:element_117", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    ELEMENT_118(-129, "minecraft:element_118", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    SEAGRASS(-130, "minecraft:seagrass", 4, BlockElement.PLANT, BlockGeometry.SOLID),
    CORAL(-131, "minecraft:coral", 16, BlockElement.PLANT, BlockGeometry.SOLID),
    CORAL_BLOCK(-132, "minecraft:coral_block", 16, BlockElement.PLANT, BlockGeometry.SOLID),
    CORAL_FAN(-133, "minecraft:coral_fan", 16, BlockElement.PLANT, BlockGeometry.SOLID),
    CORAL_FAN_DEAD(-134, "minecraft:coral_fan_dead", 16, BlockElement.PLANT, BlockGeometry.SOLID),
    CORAL_FAN_HANG(-135, "minecraft:coral_fan_hang", 16, BlockElement.PLANT, BlockGeometry.SOLID),
    CORAL_FAN_HANG2(-136, "minecraft:coral_fan_hang2", 16, BlockElement.PLANT, BlockGeometry.SOLID),
    CORAL_FAN_HANG3(-137, "minecraft:coral_fan_hang3", 16, BlockElement.PLANT, BlockGeometry.SOLID),
    KELP(-138, "minecraft:kelp", 16, BlockElement.PLANT, BlockGeometry.SOLID),
    DRIED_KELP_BLOCK(-139, "minecraft:dried_kelp_block", 1, BlockElement.ROCK1, BlockGeometry.SOLID),
    ACACIA_BUTTON(-140, "minecraft:acacia_button", 16, BlockElement.WOOD, BlockGeometry.EMPTY),
    BIRCH_BUTTON(-141, "minecraft:birch_button", 16, BlockElement.WOOD, BlockGeometry.EMPTY),
    DARK_OAK_BUTTON(-142, "minecraft:dark_oak_button", 16, BlockElement.WOOD, BlockGeometry.EMPTY),
    JUNGLE_BUTTON(-143, "minecraft:jungle_button", 16, BlockElement.WOOD, BlockGeometry.EMPTY),
    SPRUCE_BUTTON(-144, "minecraft:spruce_button", 16, BlockElement.WOOD, BlockGeometry.EMPTY),
    ACACIA_TRAPDOOR(-145, "minecraft:acacia_trapdoor", 16, BlockElement.WOOD, BlockGeometry.SOLID),
    BIRCH_TRAPDOOR(-146, "minecraft:birch_trapdoor", 16, BlockElement.WOOD, BlockGeometry.SOLID),
    DARK_OAK_TRAPDOOR(-147, "minecraft:dark_oak_trapdoor", 16, BlockElement.WOOD, BlockGeometry.SOLID),
    JUNGLE_TRAPDOOR(-148, "minecraft:jungle_trapdoor", 16, BlockElement.WOOD, BlockGeometry.SOLID),
    SPRUCE_TRAPDOOR(-149, "minecraft:spruce_trapdoor", 16, BlockElement.WOOD, BlockGeometry.SOLID),
    ACACIA_PRESSURE_PLATE(-150, "minecraft:acacia_pressure_plate", 16, BlockElement.WOOD, BlockGeometry.SOLID),
    BIRCH_PRESSURE_PLATE(-151, "minecraft:birch_pressure_plate", 16, BlockElement.WOOD, BlockGeometry.SOLID),
    DARK_OAK_PRESSURE_PLATE(-152, "minecraft:dark_oak_pressure_plate", 16, BlockElement.WOOD, BlockGeometry.SOLID),
    JUNGLE_PRESSURE_PLATE(-153, "minecraft:jungle_pressure_plate", 16, BlockElement.WOOD, BlockGeometry.SOLID),
    SPRUCE_PRESSURE_PLATE(-154, "minecraft:spruce_pressure_plate", 16, BlockElement.WOOD, BlockGeometry.SOLID),
    CARVED_PUMPKIN(-155, "minecraft:carved_pumpkin", 4, BlockElement.WOOD, BlockGeometry.SOLID),
    SEA_PICKLE(-156, "minecraft:sea_pickle", 8, BlockElement.PLANT, BlockGeometry.SOLID),
    CONDUIT(-157, "minecraft:conduit", 1, BlockElement.DEFAULT, BlockGeometry.SOLID),
    TURTLE_EGG(-159, "minecraft:turtle_egg", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    BUBBLE_COLUMN(-160, "minecraft:bubble_column", 2, BlockElement.DEFAULT, BlockGeometry.SOLID),
    BARRIER(-161, "minecraft:barrier", 1, BlockElement.DEFAULT, BlockGeometry.SOLID),
    // 1.8客户端新加的

    // 竹子系列
    BAMBOO(418, "minecraft:bamboo", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    BAMBOO_SAPLING(419, "minecraft:bamboo_sapling", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),
    SCAFFOLDING(420, "minecraft:scaffolding", 16, BlockElement.DEFAULT, BlockGeometry.SOLID),


    // 各種木質告示牌
    SPRUCE_STANDING_SIGN(436, "minecraft:spruce_standing_sign", 16, BlockElement.WOOD, BlockGeometry.EMPTY),
    SPRUCE_WALL_SIGN(437, "minecraft:spruce_wall_sign", 8, BlockElement.WOOD, BlockGeometry.EMPTY),
    SMOOTH_STONE(438, "minecraft:smooth_stone", 1, BlockElement.ROCK1, BlockGeometry.SOLID),
    RED_NETHER_BRICKS_STAIRS(439, "minecraft:red_nether_brick_stairs", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    SMOOTH_BRICKS_STAIRS(440, "minecraft:smooth_quartz_stairs", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    BIRCH_STANDING_SIGN(441, "minecraft:birch_standing_sign", 16, BlockElement.WOOD, BlockGeometry.EMPTY),
    BIRCH_WALL_SIGN(442, "minecraft:birch_wall_sign", 8, BlockElement.WOOD, BlockGeometry.EMPTY),
    JUNGLE_STANDING_SIGN(443, "minecraft:jungle_standing_sign", 16, BlockElement.WOOD, BlockGeometry.EMPTY),
    JUNGLE_WALL_SIGN(444, "minecraft:jungle_wall_sign", 8, BlockElement.WOOD, BlockGeometry.EMPTY),
    ACACIA_STANDING_SIGN(445, "minecraft:acacia_standing_sign", 16, BlockElement.WOOD, BlockGeometry.EMPTY),
    ACACIA_WALL_SIGN(446, "minecraft:acacia_wall_sign", 8, BlockElement.WOOD, BlockGeometry.EMPTY),
    DARKOAK_STANDING_SIGN(447, "minecraft:darkoak_standing_sign", 16, BlockElement.WOOD, BlockGeometry.EMPTY),
    DARKOAK_WALL_SIGN(448, "minecraft:darkoak_wall_sign", 8, BlockElement.WOOD, BlockGeometry.EMPTY),
    LECTERN(449, "minecraft:lectern", 1, BlockElement.WOOD, BlockGeometry.SOLID),
    GRINDSTONE(450, "minecraft:grindstone", 4, BlockElement.ROCK1, BlockGeometry.SOLID),
    BLAST_FURNACE(451, "minecraft:blast_furnace", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    STONECUTTER_BLOCK(452, "minecraft:stonecutter_block", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    SMOKER(453, "minecraft:smoker", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    LIT_SMOKER(454, "minecraft:lit_smoker", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    CARTOGRAPHY_TABLE(455, "minecraft:cartography_table", 1, BlockElement.ROCK1, BlockGeometry.SOLID),
    FLETCHING_TABLE(456, "minecraft:fletching_table", 1, BlockElement.ROCK1, BlockGeometry.SOLID),
    SMITHING_TABLE(457, "minecraft:smithing_table", 1, BlockElement.ROCK1, BlockGeometry.SOLID),
    BARREL(458, "minecraft:barrel", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    LOOM(459, "minecraft:loom", 4, BlockElement.ROCK1, BlockGeometry.SOLID),
    // todo --用氢元素占位
    T_460(460, "minecraft:element_1", 1, BlockElement.DEFAULT, BlockGeometry.SOLID),
    BELL(461, "minecraft:bell", 4, BlockElement.ROCK1, BlockGeometry.SOLID),
    SWEET_BERRY_BUSH(462, "minecraft:sweet_berry_bush", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    LANTERN(463, "minecraft:lantern", 2, BlockElement.ROCK1, BlockGeometry.SOLID),
    CAMPFIRE(464, "minecraft:campfire", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    LAVA_CAULDRON(465, "minecraft:lava_cauldron", 16, BlockElement.ROCK1, BlockGeometry.SOLID),
    JIGSAW(466, "minecraft:jigsaw", 8, BlockElement.ROCK1, BlockGeometry.SOLID),
    WOOD(467, "minecraft:wood", 16, BlockElement.ROCK1, BlockGeometry.SOLID),
    COMPOSTER(468, "minecraft:composter", 16, BlockElement.ROCK1, BlockGeometry.SOLID),
    LIT_BLAST_FURNACE(469, "minecraft:lit_blast_furnace", 8, BlockElement.ROCK1, BlockGeometry.SOLID),

    BEE_NEST(473, "minecraft:bee_nest", 4, BlockElement.ROCK1, BlockGeometry.SOLID),
    BEEHIVE(474, "minecraft:beehive", 4, BlockElement.ROCK1, BlockGeometry.SOLID),

    ;
    /**
     * id
     */
    private int id;
    /**
     * 名称
     */
    private String name;
    /**
     * 开始的runtimeId
     */
    private int startRuntimeId;
    /**
     * 支持最大的metaData
     */
    private int maxMetadata;

    /**
     * 组成元素
     */
    private BlockElement blockElement;

    /**
     * 方块骨架
     */
    private BlockGeometry blockGeometry;

    private GameObject bindGameObject;

    private Map<Integer, Integer> startRuntimeIdMap = new HashMap<>();
    private Map<Integer, List<Short>> missMetaListMap = new HashMap<>();

    /**
     * 初始化RuntimeId
     */
    static {
        AtomicInteger runtimeIdCount = new AtomicInteger();
        for (BlockPrototype blockPrototype : BlockPrototype.values()) {
            blockPrototype.startRuntimeId = runtimeIdCount.getAndAdd(blockPrototype.getMaxMetadata());
        }
    }

    BlockPrototype(int id, String name, int maxMetadata, BlockElement blockElement, BlockGeometry blockGeometry) {
        this.id = id;
        this.name = name;
        this.maxMetadata = maxMetadata;
        this.blockElement = blockElement;
        this.blockGeometry = blockGeometry;

        this.bindGameObject = new GameObject();
    }

    /**
     * a
     * 方块ID.
     *
     * @return 获取方块ID
     */
    public int getId() {
        return id;
    }

    /**
     * 方块名称.
     *
     * @return 获取方块名称
     */
    public String getName() {
        return name;
    }

    public int getStartRuntimeId() {
        return startRuntimeId;
    }

    public int getStartRuntimeId(int version) {
        return startRuntimeIdMap.get(version);
    }

    public int getMaxMetadata() {
        return maxMetadata;
    }

    public BlockElement getBlockElement() {
        return blockElement;
    }

    public BlockGeometry getBlockGeometry() {
        return blockGeometry;
    }

    public GameObject getBindGameObject() {
        return bindGameObject;
    }

    public Map<Integer, Integer> getStartRuntimeIdMap() {
        return startRuntimeIdMap;
    }

    public Map<Integer, List<Short>> getMissMetaListMap() {
        return missMetaListMap;
    }
}
