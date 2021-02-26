package com.particle.game.serializations;

import com.particle.core.ecs.GameObject;
import com.particle.core.ecs.serialization.IStringSerialization;
import com.particle.game.player.components.OnlineTimeRecorderModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OnlineTimeRecorderConvertSerialization implements IStringSerialization<OnlineTimeRecorderModule> {

    private static final Logger LOGGER = LoggerFactory.getLogger(OnlineTimeRecorderConvertSerialization.class);

    @Override
    public String serialization(GameObject gameObject, OnlineTimeRecorderModule onlineTimeRecorderModule) {
        return String.valueOf(onlineTimeRecorderModule.getOnlineTime());
    }

    @Override
    public void deserialization(GameObject gameObject, String data, OnlineTimeRecorderModule onlineTimeRecorderModule) {
        try {
            onlineTimeRecorderModule.setOnlineTime(Long.parseLong(data));
        } catch (NumberFormatException e) {
            onlineTimeRecorderModule.setOnlineTime(0);
            LOGGER.error("Fail to import online time", e);
        }
    }
}
