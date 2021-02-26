package com.particle.model.level;

public enum BiomeType {

    // Snowy Biomes
    SNOWY_TUNDRA((byte) 12),
    ICE_SPIKES((byte) 140),
    SNOWY_TAIGA((byte) 30),
    SNOWY_TAIGA_MOUNTAINS((byte) 158),
    FROZEN_RIVER((byte) 11),
    SNOWY_BEACH((byte) 26),

    // Cold biomes
    MOUNTAINS((byte) 3),
    GRAVELLY_MOUNTAINS((byte) 131),
    WOODED_MOUNTAINS((byte) 34),
    TAIGA((byte) 5),
    TAIGA_MOUNTAINS((byte) 133),
    GIANT_TREE_TAIGA((byte) 32),
    GIANT_SPRUCE_TAIGA((byte) 160),
    STONE_SHORE((byte) 25),

    // Temperate/Lush Biomes
    PLAINS((byte) 1),
    SUNFLOWER_PLAINS((byte) 129),
    FOREST((byte) 4),
    FLOWER_FOREST((byte) 132),
    BIRCH_FOREST((byte) 27),
    TALL_BIRCH_FOREST((byte) 155),
    DARK_FOREST((byte) 29),
    DARK_FOREST_HILLS((byte) 157),
    SWAMP((byte) 6),
    SWAMP_HILLS((byte) 134),
    JUNGLE((byte) 21),
    MODIFIED_JUNGLE((byte) 149),
    JUNGLE_EDGE((byte) 23),
    MODIFIED_JUNGLE_EDGE((byte) 151),
    BAMBOO_JUNGLE((byte) 151),
    RIVER((byte) 7),
    BEACH((byte) 16),
    MUSHROOM_FIELDS((byte) 14),
    MUSHROOM_FIELD_SHORE((byte) 15),

    // The End Biomes
    THE_END((byte) 9),
    SMALL_END_ISLANDS((byte) 40),
    END_MIDLANDS((byte) 41),
    END_HIGHLANDS((byte) 42),
    END_BARRENS((byte) 43),

    // Dry/Warm Biomes
    DESERT((byte) 2),
    DESERT_LAKES((byte) 130),
    SAVANNA((byte) 35),
    SHATTERED_SAVANNA((byte) 163),
    BADLANDS((byte) 37),
    ERODED_BADLANDS((byte) 165),
    WOODED_BADLANDS_PLATEAU((byte) 38),
    MODIFIED_WOODED_BADLANDS_PLATEAU((byte) 166),
    PLATEAU((byte) 36),
    MODIFIED_PLATEAU((byte) 164),
    NETHER((byte) 8),

    // Ocean Biomes
    WARM_OCEAN((byte) 44),
    LUKEWARM_OCEAN((byte) 45),
    DEEP_LUKEWARM_OCEAN((byte) 48),
    OCEAN((byte) 0),
    DEEP_OCEAN((byte) 24),
    COLD_OCEAN((byte) 46),
    DEEP_COLD_OCEAN((byte) 49),
    FROZEN_OCEAN((byte) 10),
    DEEP_FROZEN_OCEAN((byte) 50),

    // Neutral Biomes
    HILLS((byte) 13);

    private byte id;

    BiomeType(byte id) {
        this.id = id;
    }

    public byte getId() {
        return id;
    }
}
