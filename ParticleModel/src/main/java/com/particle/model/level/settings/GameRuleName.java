package com.particle.model.level.settings;

public enum GameRuleName {
    COMMAND_BLOCK_OUTPUT("commandblockoutput"),
    DO_DAYLIGHT_CYCLE("dodaylightcycle"),
    DO_ENTITY_DROPS("doentitydrops"),
    DO_FIRE_TICK("dofiretick"),
    DO_MOB_LOOT("domobloot"),
    DO_MOB_SPAWNING("domobspawning"),
    DO_TILE_DROPS("dotiledrops"),
    DO_WEATHER_CYCLE("doweathercycle"),
    DROWNING_DAMAGE("drowningdamage"),
    FALL_DAMAGE("falldamage"),
    FIRE_DAMAGE("firedamage"),
    KEEP_INVENTORY("keepinventory"),
    MOB_GRIEFING("mobgriefing"),
    NATURAL_REGENERATION("naturalregeneration"),
    PVP("pvp"),
    SEND_COMMAND_FEEDBACK("sendcommandfeedback"),
    SHOW_COORDINATES("showcoordinates"),
    TNT_EXPLODES("tntexplodes"),
    SHOW_DEATH_MESSAGE("showdeathmessage");

    private final String name;

    GameRuleName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
