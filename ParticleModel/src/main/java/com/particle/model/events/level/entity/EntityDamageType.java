package com.particle.model.events.level.entity;

public enum EntityDamageType {
    Anvil("anvil"),
    BlockExplosion("block_explosion"),
    Contact("contact"),
    Drowning("drowning"),
    EntityAttack("entity_attack"),
    EntityExplosion("entity_explosion"),
    Fall("fall"),
    FallingBlock("falling_block"),
    Fire("fire"),
    FireTick("fire_tick"),
    Fireworks("fireworks"),
    FlyIntoWall("fly_into_wall"),
    Lava("lava"),
    Lightning("lightning"),
    Magic("magic"),
    Magma("magma"),
    NONE("none"),
    Override("override"),
    Piston("piston"),
    Projectile("projectile"),
    Starve("starve"),
    Suffocation("suffocation"),
    Suicide("suicide"),
    Thorns("thorns"),
    Void("void"),
    Wither("wither"),
    Cactus("cactus"),
    ;

    private final String name;

    EntityDamageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}