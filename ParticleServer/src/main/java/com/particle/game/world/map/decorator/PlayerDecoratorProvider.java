package com.particle.game.world.map.decorator;

import com.particle.api.ui.map.IMapDecoratorProvider;
import com.particle.game.entity.movement.PositionService;
import com.particle.model.math.Vector3f;
import com.particle.model.player.Player;
import com.particle.model.ui.map.MapData;
import com.particle.model.ui.map.MapDecorator;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;

@Singleton
public class PlayerDecoratorProvider implements IMapDecoratorProvider {

    @Inject
    private PositionService positionService;

    @Override
    public MapDecorator getDecorator(Player player, MapData mapData) {
        // 位置校验
        Vector3f position = this.positionService.getPosition(player);

        // 计算相对于地图左上角的坐标
        int offsetX = (position.getFloorX() - mapData.getCenterX()) * 128 * mapData.getScale() / mapData.getRadius() - mapData.getOffsetX();
        int offsetZ = (position.getFloorZ() - mapData.getCenterZ()) * 128 * mapData.getScale() / mapData.getRadius() - mapData.getOffsetZ();

        if (Math.abs(offsetX) > mapData.getWidth() || Math.abs(offsetZ) > mapData.getHeight()) {
            return null;
        }

        MapDecorator mapDecorator = new MapDecorator();
        mapDecorator.setIcon(MapDecorator.Type.MARKER_WHITE);
        mapDecorator.setColor(Color.WHITE);
        mapDecorator.setLabel("");
        mapDecorator.setRotation((byte) (((byte) (this.positionService.getDirection(player).getYaw() / 11.25 + 33) >> 1) % 16));
        mapDecorator.setOffsetX((byte) offsetX);
        mapDecorator.setOffsetZ((byte) offsetZ);

        return mapDecorator;
    }
}
