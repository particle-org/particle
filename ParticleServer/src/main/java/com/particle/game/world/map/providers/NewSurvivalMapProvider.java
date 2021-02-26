package com.particle.game.world.map.providers;

import com.particle.game.world.map.MapGenerateService;
import com.particle.model.player.Player;
import com.particle.model.ui.map.MapData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.inject.Singleton;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Singleton
public class NewSurvivalMapProvider implements IMapProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewSurvivalMapProvider.class);

    private MapData mapData = new MapData();

    public NewSurvivalMapProvider() {
        try {
            BufferedImage mapImage = ImageIO.read(this.getClass().getClassLoader().getResource("new_survival_map3.png"));
            Color[][] colors = new Color[mapImage.getWidth()][mapImage.getHeight()];

            for (int i = 0; i < mapImage.getWidth(); i++) {
                for (int j = 0; j < mapImage.getHeight(); j++) {
                    colors[i][j] = new Color(mapImage.getRGB(i, j));
                }
            }

            this.mapData.setMapId((MapGenerateService.TYPE_NEW_SURVIVAL << 48) | 1);
            this.mapData.setMapType(MapGenerateService.TYPE_NEW_SURVIVAL);
            this.mapData.setOffsetX(0);
            this.mapData.setOffsetZ(0);
            this.mapData.setWidth(mapImage.getWidth());
            this.mapData.setHeight(mapImage.getHeight());
            this.mapData.setScale((byte) 1);
            this.mapData.setImage(colors);

            this.mapData.setCenterX(1280);
            this.mapData.setCenterZ(1280);
            this.mapData.setRadius(1280);
        } catch (IOException e) {
            LOGGER.error("Fail to load map image!");
        }
    }

    @Override
    public MapData getMapData(Player player, long mapId) {
        /*
         * Map id 列表:
         *
         * 1 : 新生存地表家园地图
         */

        return this.mapData;
    }
}
