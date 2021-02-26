package com.particle.game.utils.placeholder;

import com.particle.util.placeholder.CompileInterface;
import com.particle.util.placeholder.addon.CompileThreadBinder;
import com.particle.util.placeholder.addon.CompileThreadType;

@CompileThreadBinder(type = CompileThreadType.SYNC)
public class PlaceHolderSyncTest implements CompileInterface {
    @Override
    public String compile(String key, String playerName) {
        return "SYNC";
    }
}
