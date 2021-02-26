package com.particle.model.entity.metadata;

import java.util.HashMap;
import java.util.Map;

public enum MetadataDataFlag {
    DATA_FLAG_ONFIRE(0),
    DATA_FLAG_SNEAKING(1),
    DATA_FLAG_RIDING(2),
    DATA_FLAG_SPRINTING(3),
    DATA_FLAG_ACTION(4),
    DATA_FLAG_INVISIBLE(5),
    DATA_FLAG_TEMPTED(6),
    DATA_FLAG_INLOVE(7),
    DATA_FLAG_SADDLED(8),
    DATA_FLAG_POWERED(9),
    DATA_FLAG_IGNITED(10),
    DATA_FLAG_BABY(11), //disable head scaling
    DATA_FLAG_CONVERTING(12),
    DATA_FLAG_CRITICAL(13),
    DATA_FLAG_CAN_SHOW_NAMETAG(14),
    DATA_FLAG_ALWAYS_SHOW_NAMETAG(15),
    DATA_FLAG_NO_AI(16),
    DATA_FLAG_SILENT(17),
    DATA_FLAG_WALLCLIMBING(18),
    DATA_FLAG_CAN_CLIMB(19),
    DATA_FLAG_SWIMMER(20),
    DATA_FLAG_CAN_FLY(21),
    DATA_FLAG_CAN_CANWALK(22),
    DATA_FLAG_RESTING(23),
    DATA_FLAG_SITTING(24),
    DATA_FLAG_ANGRY(25),
    DATA_FLAG_INTERESTED(26),
    DATA_FLAG_CHARGED(27),
    DATA_FLAG_TAMED(28),
    // add verison 1.7
    DATA_FLAG_ORPHANED(29),

    DATA_FLAG_LEASHED(30),
    DATA_FLAG_SHEARED(31),
    DATA_FLAG_GLIDING(32),
    DATA_FLAG_ELDER(33),
    DATA_FLAG_MOVING(34),
    DATA_FLAG_BREATHING(35),
    DATA_FLAG_CHESTED(36),
    DATA_FLAG_STACKABLE(37),
    DATA_FLAG_SHOW_BOTTOM(38),
    DATA_FLAG_STANDING(39),
    DATA_FLAG_SHAKING(40),
    DATA_FLAG_IDLING(41),
    DATA_FLAG_CASTING(42),
    DATA_FLAG_CHARGING(43),
    DATA_FLAG_WASD_CONTROLLED(44),
    DATA_FLAG_CAN_POWER_JUMP(45),
    DATA_FLAG_CAN_LINGERING(46),
    DATA_FLAG_HAS_COLLISION(47),
    DATA_FLAG_GRAVITY(48),
    FIRE_IMMUNE(49),
    DANCING(50),
    ENCHANTED(51),
    RETURNTRIDENT(52),
    CONTAINER_IS_PRIVATE(53),
    IS_TRANSFORMING(54),
    DAMAGENEARBYMOBS(55),
    SWIMMING(56),
    BRIBED(57),
    IS_PREGNANT(58),
    LAYING_EGG(59),
    RIDER_CAN_PICK(60),
    TRANSITION_SITTING(61),
    EATING(62),
    LAYING_DOWN(63),
    SNEEZING(64),
    TRUSTING(65),
    ROLLING(66),
    SCARED(67),
    IN_SCAFFOLDING(68),
    OVER_SCAFFOLDING(69),
    FALL_THROUGH_SCAFFOLDING(70),
    BLOCKING(71),
    TRANSITION_BLOCKING(72),
    BLOCKED_USING_SHIELD(73),
    BLOCKED_USING_DAMAGED_SHIELD(74),
    SLEEPING(75),
    WANTS_TO_WAKE(76),
    TRADE_INTEREST(77),
    DOOR_BREAKER(78),
    BREAKING_OBSTRUCTION(79),
    DOOR_OPENER(80),
    IS_ILLAGER_CAPTAIN(81),
    STUNNED(82),
    ROARING(83),
    DELAYED_ATTACK(84),
    IS_AVOIDING_MOBS(85),
    // add by version 1.16
    IS_AVOIDING_BLOCK(86),
    // desplace by version 1.16
    FACING_TARGET_TO_RANGE_ATTACK(87),
    // desplace by version 1.16
    HIDDEN_WHEN_INVISIBLE(88),
    // desplace by version 1.16
    IS_IN_UI(89),
    // desplace by version 1.16
    STALKING(90),
    // desplace by version 1.16
    EMOTING(91),
    // desplace by version 1.16
    CELEBRATING(92),
    ADMIRING(93),
    CELEBRATING_SPECIAL(94),
    // desplace by version 1.16
    COUNT(95);

    private static Map<Integer, MetadataDataFlag> dictionary = new HashMap<>();

    static {
        for (MetadataDataFlag metadataType : MetadataDataFlag.values()) {
            dictionary.put(metadataType.value(), metadataType);
        }
    }

    private int type;

    private MetadataDataFlag(int type) {
        this.type = type;
    }

    public int value() {
        return this.type;
    }

    public static MetadataDataFlag valueOf(int type) {
        return dictionary.get(type);
    }

}
