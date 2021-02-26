package com.particle.game.world.map.providers;

import com.particle.game.world.map.MapGenerateService;
import com.particle.model.ui.map.MapData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.inject.Singleton;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class MultiContentMapProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(MultiContentMapProvider.class);

    private Map<String, MapData> mapData = new HashMap<>();

    public MultiContentMapProvider() {
        File maps = new File("maps");
        if (!maps.exists()) {
            maps.mkdirs();
            return;
        }

        if (!maps.isDirectory()) {
            LOGGER.error("Map dictionary not exist!");
            return;
        }

        for (File file : maps.listFiles()) {
            try {
                String fileName = file.getName();
                if (fileName.endsWith(".png")) {
                    BufferedImage mapImage = ImageIO.read(file);

                    MapData mapData = this.generateMapDataFromImage(mapImage);

                    this.mapData.put(fileName.substring(0, fileName.length() - 4), mapData);
                }
            } catch (IOException e) {
                LOGGER.error("Fail to load map image!");
            }
        }
    }

    private MapData generateMapDataFromImage(BufferedImage mapImage) {
        Color[][] colors = new Color[mapImage.getWidth()][mapImage.getHeight()];

        for (int i = 0; i < mapImage.getWidth(); i++) {
            for (int j = 0; j < mapImage.getHeight(); j++) {
                colors[i][j] = new Color(mapImage.getRGB(i, j));
            }
        }

        MapData mapData = new MapData();
        mapData.setMapId((MapGenerateService.TYPE_NEW_SURVIVAL << 48) | 1);
        mapData.setMapType(MapGenerateService.TYPE_NEW_SURVIVAL);
        mapData.setOffsetX(0);
        mapData.setOffsetZ(0);
        mapData.setWidth(mapImage.getWidth());
        mapData.setHeight(mapImage.getHeight());
        mapData.setScale((byte) 1);
        mapData.setImage(colors);

        mapData.setCenterX(1280);
        mapData.setCenterZ(1280);
        mapData.setRadius(1280);

        return mapData;
    }
}
