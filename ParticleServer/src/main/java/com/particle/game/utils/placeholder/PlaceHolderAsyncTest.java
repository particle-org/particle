package com.particle.game.utils.placeholder;

import com.particle.util.placeholder.CompileInterface;
import com.particle.util.placeholder.addon.CompileThreadBinder;
import com.particle.util.placeholder.addon.CompileThreadType;

@CompileThreadBinder(type = CompileThreadType.ASYNC)
public class PlaceHolderAsyncTest implements CompileInterface {
    @Override
    public String compile(String key, String playerName) {
        return "ASYNC";
    }
}
