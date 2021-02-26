package com.particle.game.world.nav;

import com.particle.game.entity.movement.PositionService;
import com.particle.game.world.level.LevelService;
import com.particle.game.world.particle.AdvanceParticleService;
import com.particle.model.level.Level;
import com.particle.model.level.chunk.NavSquare;
import com.particle.model.math.Vector3;
import com.particle.model.math.Vector3f;
import com.particle.model.particle.ParticleType;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashSet;
import java.util.Set;

@Singleton
public class NavMeshMarker {

    @Inject
    private PositionService positionService;

    @Inject
    private NavMeshService navMeshService;

    @Inject
    private LevelService levelService;

    @Inject
    private AdvanceParticleService advanceParticleService;

    public void mark(Level level, Vector3 position) {
        int topBlockHeightBelow = this.levelService.getTopBlockHeightBelow(level, position);

        NavSquare navSquare = this.navMeshService.findNavSquare(level, new Vector3(position.getX(), topBlockHeightBelow, position.getZ()));

        this.doMark(level, navSquare, new HashSet<>());
    }

    private void doMark(Level level, NavSquare source, Set<NavSquare> history) {
        for (NavSquare connectTo : source.getConnectTo()) {
            for (int i = 0; i < 10; i++) {
                level.getLevelSchedule().scheduleDelayTask("Test", () -> {
                    this.advanceParticleService.markLine(level, this.getNavCenter(source), this.getNavCenter(connectTo).subtract(this.getNavCenter(source)), ParticleType.TYPE_HUGE_EXPLODE);
                }, 500 * i);
            }
        }
    }

    private Vector3f getNavCenter(NavSquare navSquare) {
        return new Vector3f((navSquare.getxMin() + navSquare.getxMax()) / 2.0f, navSquare.getY() + 1, (navSquare.getzMin() + navSquare.getzMax()) / 2.0f);
    }
}
