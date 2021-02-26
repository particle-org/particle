package com.particle.model.level;

import java.util.HashMap;
import java.util.Map;

public enum LevelEventType {
    Undefined(0),
    SoundClick(1000),
    SoundClickFail(1001),
    SoundLaunch(1002),
    SoundOpenDoor(1003),
    SoundFizz(1004),
    SoundFuse(1005),
    SoundPlayRecording(1006),
    SoundGhastWarning(1007),
    SoundGhastFireball(1008),
    SoundBlazeFireball(1009),
    SoundZombieWoodenDoor(1010),
    SoundZombieDoorCrash(1012),
    SoundZombieInfected(1016),
    SoundZombieConverted(1017),
    SoundEndermanTeleport(1018),
    SoundAnvilBroken(1020),
    SoundAnvilUsed(1021),
    SoundAnvilLand(1022),
    SoundInfinityArrowPickup(1030),
    SoundTeleportEnderPearl(1032),
    SoundItemFrameAddItem(1040),
    SoundItemFrameBreak(1041),
    SoundItemFramePlace(1042),
    SoundItemFrameRemoveItem(1043),
    SoundItemFrameRotateItem(1044),
    SoundExperienceOrbPickup(1051),
    SoundTotemUsed(1052),
    SoundArmorStandBreak(1060),
    SoundArmorStandHit(1061),
    SoundArmorStandLand(1062),
    SoundArmorStandPlace(1063),
    ParticlesShoot(2000),
    ParticlesDestroyBlock(2001),
    ParticlesPotionSplash(2002),
    ParticlesEyeOfEnderDeath(2003),
    ParticlesMobBlockSpawn(2004),
    ParticleCropGrowth(2005),
    ParticleSoundGuardianGhost(2006),
    ParticleDeathSmoke(2007),
    ParticleDenyBlock(2008),
    ParticleGenericSpawn(2009),
    ParticlesDragonEgg(2010),
    ParticlesCropEaten(2011),
    ParticlesCrit(2012),
    ParticlesTeleport(2013),
    ParticlesCrackBlock(2014),
    ParticlesBubble(2015),
    ParticlesEvaporate(2016),
    ParticlesDestroyArmorStand(2017),
    ParticlesBreakingEgg(2018),
    ParticleDestroyEgg(2019),
    ParticlesPointCloud(2024),
    StartRaining(3001),
    StartThunderstorm(3002),
    StopRaining(3003),
    StopThunderstorm(3004),
    GlobalPause(3005),
    ActivateBlock(3500),
    CauldronExplode(3501),
    CauldronDyeArmor(3502),
    CauldronCleanArmor(3503),
    CauldronFillPotion(3504),
    CauldronTakePotion(3505),
    CauldronFillWater(3506),
    CauldronTakeWater(3507),
    CauldronAddDye(3508),
    CauldronCleanBanner(3509),
    CauldronFlush(3510),
    CauldronFillLava(3512),
    CauldronTakeLava(3513),
    StartBlockCracking(3600),
    StopBlockCracking(3601),
    UpdateBlockCracking(3602),
    AllPlayersSleeping(9800),
    JumpPrevented(9801),
    GlobalEvent(0x4000);

    private int type;

    private static Map<Integer, LevelEventType> types = new HashMap<>();

    static {
        for (LevelEventType levelEventType : LevelEventType.values()) {
            types.put(levelEventType.type, levelEventType);
        }
    }

    /**
     * 是否声音
     *
     * @param levelEventType
     * @return
     */
    public static boolean isSound(LevelEventType levelEventType) {
        if (levelEventType == null) {
            return false;
        }
        if (levelEventType.ordinal() >= SoundClick.ordinal()
                && levelEventType.ordinal() <= SoundArmorStandPlace.ordinal()) {
            return true;
        }
        return false;
    }

    public static LevelEventType fromIntType(int type) {
        return types.get(type);
    }

    LevelEventType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
