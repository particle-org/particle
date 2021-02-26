package com.particle.game.player.inventory.modules;

import com.particle.core.ecs.module.BehaviorModule;
import com.particle.model.player.Player;

import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

public class ContainerViewerModule extends BehaviorModule {

    private Set<Player> playerSet = new ConcurrentSkipListSet<>((o1, o2) -> {
        if (o1 == null) {
            return -1;
        } else if (o2 == null) {
            return 1;
        } else if (o1.getRuntimeId() < o2.getRuntimeId()) {
            return -1;
        } else if (o1.getRuntimeId() > o2.getRuntimeId()) {
            return 1;
        } else {
            return 0;
        }
    });


    public void addPlayer(Player player) {
        this.playerSet.add(player);
    }

    public void removePlayer(Player player) {
        this.playerSet.remove(player);
    }

    public int getViewerSize() {
        return this.playerSet.size();
    }

}
