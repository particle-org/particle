package com.particle.game.world.map.providers;

import com.particle.game.utils.config.ServerConfigService;
import com.particle.game.world.map.MapGenerateService;
import com.particle.model.player.Player;
import com.particle.model.ui.map.MapData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Singleton
public class SingleContentMapProvider implements IMapProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(SingleContentMapProvider.class);

    @Inject
    private ServerConfigService serverConfigService;

    private MapData mapData = new MapData();

    @Inject
    public void init() {
        // 创建目录
        File maps = new File("maps");
        if (!maps.exists()) {
            LOGGER.warn("Map {} not exist!", serverConfigService.getVersion());
            maps.mkdirs();
            return;
        }

        // 查询地图
        File file = new File("maps/" + serverConfigService.getVersion() + ".png");
        if (file.exists()) {
            try {
                BufferedImage mapImage = ImageIO.read(file);
                this.mapData = this.generateMapDataFromImage(mapImage);
            } catch (IOException e) {
                LOGGER.error("Fail to load map image!");
            }
        } else {
            LOGGER.warn("Map {} not exist!", serverConfigService.getVersion());

            try {
                BufferedImage mapImage = ImageIO.read(this.getClass().getClassLoader().getResource("new_survival_map3.png"));
                this.mapData = this.generateMapDataFromImage(mapImage);
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

    @Override
    public MapData getMapData(Player player, long mapId) {
        return this.mapData;
    }
}
