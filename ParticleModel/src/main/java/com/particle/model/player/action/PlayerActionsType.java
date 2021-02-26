package com.particle.model.player.action;

public enum PlayerActionsType {
    ActionStartDestroyBlock(0),
    ActionAbortDestroyBlock(1),
    ActionStopDestroyBlock(2),
    ActionGetUpdatedBlock(3),
    ActionDropItem(4),
    ActionStartSleeping(5),
    ActionStopSleeping(6),
    ActionRespawn(7),

    // 1.16 換到 playerAuthInput
    ActionStartJump(8),
    ActionStartSprinting(9),
    ActionStopSprinting(10),
    ActionStartSneaking(11),
    ActionStopSneaking(12),


    ActionChangeDimension(13),
    ActionChangeDimensionAck(14),

    // 1.16 換到 playerAuthInput
    ActionStartGliding(15),
    ActionStopGliding(16),


    ActionDenyDestroyBlock(17),
    ActionCrackBlock(18),
    ActionChangeSkin(19),
    DeprecatedUpdatedEnchantingSeed(20),

    // 1.16 換到 playerAuthInput
    ActionStartSwimming(21),
    ActionStopSwimming(22),


    ActionStartSpinAttack(23),
    ActionStopSpinAttack(24),
    InteractWithBlock(25);

    private int type;

    private static PlayerActionsType[] types = PlayerActionsType.values();

    public static PlayerActionsType fromIntType(int type) {
        if (type > -1 && type < types.length) {
            return types[type];
        }

        return null;
    }

    PlayerActionsType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return "PlayerActionsType{" +
                "type=" + type +
                '}';
    }
}
