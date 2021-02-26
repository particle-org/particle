package com.particle.api.animation;

import com.particle.model.level.Level;
import com.particle.model.math.Vector3;

public interface InventoryAnimationServiceApi {
    void sendOpenInventoryPacket(Level level, Vector3 position);

    void sendCloseInventoryPacket(Level level, Vector3 position);
}
