package com.particle.game.world.nav;

import com.particle.event.dispatcher.EventRank;
import com.particle.event.handle.AbstractLevelEventHandle;
import com.particle.game.world.level.LevelService;
import com.particle.model.block.Block;
import com.particle.model.block.types.BlockPrototype;
import com.particle.model.events.level.player.PlayerInteractiveBlockEvent;
import com.particle.model.level.Level;
import com.particle.model.level.chunk.NavSquare;
import com.particle.model.math.Vector3;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Singleton
public class NavCheckHandle extends AbstractLevelEventHandle<PlayerInteractiveBlockEvent> {

    @Inject
    private LevelService levelService;

    @Inject
    private NavMeshService navMeshService;

    @Inject
    private NavSearchService navSearchService;

    @Inject
    private NavMeshMarker navMeshMarker;

    private NavSquare lastClicked = null;
    private Vector3 lastClickedVector = null;

    private long timestamp = 0;

    @Override
    public Class<PlayerInteractiveBlockEvent> getListenerEvent() {
        return PlayerInteractiveBlockEvent.class;
    }

    @Override
    public void onHandle(Level level, PlayerInteractiveBlockEvent playerInteractiveBlockEvent) {
        if (System.currentTimeMillis() - timestamp < 2000) {
            return;
        } else {
            timestamp = System.currentTimeMillis();
        }

        Vector3 startVector = playerInteractiveBlockEvent.getBlockPosition();
        NavSquare current = navMeshService.findNavSquare(level, playerInteractiveBlockEvent.getBlockPosition());

        this.navMeshMarker.mark(level, startVector);

        if (current != null) {
            if (lastClicked != null) {
                List<NavSquare> navSquares = navSearchService.search(lastClicked, current, lastClickedVector, startVector);

                if (navSquares != null) {
                    List<Vector3> roadPoints = this.navSearchService.getRoadPoints(navSquares, lastClickedVector, startVector);

                    for (int i = 1; i < navSquares.size(); i++) {
                        this.renderSquare(level, navSquares.get(i), BlockPrototype.STAINED_GLASS, i & 0xf);
                    }

                    this.renderSquare(level, lastClicked, BlockPrototype.GOLD_BLOCK, 0);
                    this.renderSquare(level, current, BlockPrototype.IRON_BLOCK, 0);

                    for (Vector3 roadPoint : roadPoints) {
                        this.levelService.setBlockAt(level, Block.getBlock(BlockPrototype.DIAMOND_BLOCK), roadPoint);
                    }
                }
            }

            lastClicked = current;
            lastClickedVector = startVector;
        }
    }

    private void renderSquare(Level level, NavSquare navSquare, BlockPrototype type, int meta) {
        for (int i = navSquare.getxMin(); i <= navSquare.getxMax(); i++) {
            for (int j = navSquare.getzMin(); j <= navSquare.getzMax(); j++) {
                Block block = Block.getBlock(type);
                block.setMeta(meta);

                this.levelService.setBlockAt(level, block, new Vector3(i, navSquare.getY(), j));
            }
        }
    }

    @Override
    public EventRank getEventRank() {
        return EventRank.LOCAL;
    }
}
