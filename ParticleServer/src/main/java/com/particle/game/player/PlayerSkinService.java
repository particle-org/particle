package com.particle.game.player;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.particle.api.entity.PlayerSkinServiceApi;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.game.entity.attribute.identified.EntityNameService;
import com.particle.game.entity.attribute.identified.UUIDModule;
import com.particle.game.utils.ecs.ECSComponentService;
import com.particle.game.world.aoi.BroadcastServiceProxy;
import com.particle.model.entity.Entity;
import com.particle.model.entity.component.player.PlayerSkinModule;
import com.particle.model.entity.model.skin.PlayerSkinAnimationData;
import com.particle.model.entity.model.skin.PlayerSkinData;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.data.ConfirmSkinPacket;
import com.particle.model.network.packets.data.PlayerListPacket;
import com.particle.model.player.BasicModelSkin;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Singleton
public class PlayerSkinService implements PlayerSkinServiceApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerService.class);

    private static final ECSModuleHandler<UUIDModule> UUID_MODULE_HANDLER = ECSModuleHandler.buildHandler(UUIDModule.class);
    private static final ECSModuleHandler<PlayerSkinModule> PLAYER_SKIN_MODULE_HANDLER = ECSModuleHandler.buildHandler(PlayerSkinModule.class);


    @Inject
    private EntityNameService entityNameService;

    @Inject
    private BroadcastServiceProxy broadcastServiceProxy;

    @Inject
    private ECSComponentService ecsComponentService;

    @Inject
    private NetworkManager networkManager;

    private static final int SINGLE_SKIN_SIZE = 64 * 32 * 4;
    private static final int DOUBLE_SKIN_SIZE = 64 * 64 * 4;
    private static final int LARGE_SKIN_SIZE = 128 * 128 * 4;
    private static final int SUPER_SKIN_SIZE = 256 * 256 * 4;

    private static final String SKIN_STEVE = "Standard_Steve";
    private static final String SKIN_ALEX = "Standard_Alex";

    /**
     * 基础史蒂夫皮肤数据
     */
    private BasicModelSkin basicSteveSkin;

    /**
     * 默认Alex皮肤数据
     */
    private BasicModelSkin baseAlexSkin;

    //--------------------------------------------- 默认皮肤相关 ----------------------------------------------

    @Inject
    public void init() {
        try {
            this.basicSteveSkin = JSON.parseObject(this.getClass().getClassLoader().getResourceAsStream("basic_model_steve.json"), BasicModelSkin.class);
            if (StringUtils.isNotBlank(this.basicSteveSkin.getSkinDataEncoded())) {
                this.basicSteveSkin.setSkinData(Base64.getDecoder().decode(this.basicSteveSkin.getSkinDataEncoded()));
                if (this.basicSteveSkin.getSkinData().length == SINGLE_SKIN_SIZE) {
                    this.basicSteveSkin.setSkinWidth(64);
                    this.basicSteveSkin.setSkinHeight(32);
                } else if (this.basicSteveSkin.getSkinData().length == DOUBLE_SKIN_SIZE) {
                    this.basicSteveSkin.setSkinWidth(64);
                    this.basicSteveSkin.setSkinHeight(64);
                } else if (this.basicSteveSkin.getSkinData().length == LARGE_SKIN_SIZE) {
                    this.basicSteveSkin.setSkinWidth(128);
                    this.basicSteveSkin.setSkinHeight(128);
                } else if (this.basicSteveSkin.getSkinData().length == SUPER_SKIN_SIZE) {
                    this.basicSteveSkin.setSkinWidth(256);
                    this.basicSteveSkin.setSkinHeight(256);
                    this.basicSteveSkin.setPersonSkin(true);
                }
            }
            if (StringUtils.isNotBlank(this.basicSteveSkin.getCapeDataEncoded())) {
                this.basicSteveSkin.setCapeData(Base64.getDecoder().decode(this.basicSteveSkin.getCapeDataEncoded()));
            } else {
                this.basicSteveSkin.setCapeData(new byte[0]);
            }
            this.basicSteveSkin.setSkinResourcePatch(String.format("{\"geometry\":{\"default\":\"%s\"},\"convert\":true}", this.basicSteveSkin.getSkinGeometryName()));
            // 配置文件中的数据是经过base64编码过的
            this.basicSteveSkin.setSkinGeometry(new String(Base64.getDecoder().decode(this.basicSteveSkin.getSkinGeometry()), StandardCharsets.UTF_8));
            this.basicSteveSkin.setSerializedAnimationData("");


            this.baseAlexSkin = JSON.parseObject(this.getClass().getClassLoader().getResourceAsStream("basic_model_alex.json"), BasicModelSkin.class);
            if (StringUtils.isNotBlank(this.baseAlexSkin.getSkinDataEncoded())) {
                this.baseAlexSkin.setSkinData(Base64.getDecoder().decode(this.baseAlexSkin.getSkinDataEncoded()));
                if (this.baseAlexSkin.getSkinData().length == SINGLE_SKIN_SIZE) {
                    this.baseAlexSkin.setSkinWidth(64);
                    this.baseAlexSkin.setSkinHeight(32);
                } else if (this.baseAlexSkin.getSkinData().length == DOUBLE_SKIN_SIZE) {
                    this.baseAlexSkin.setSkinWidth(64);
                    this.baseAlexSkin.setSkinHeight(64);
                } else if (this.baseAlexSkin.getSkinData().length == LARGE_SKIN_SIZE) {
                    this.baseAlexSkin.setSkinWidth(128);
                    this.baseAlexSkin.setSkinHeight(128);
                } else if (this.baseAlexSkin.getSkinData().length == SUPER_SKIN_SIZE) {
                    this.baseAlexSkin.setSkinWidth(256);
                    this.baseAlexSkin.setSkinHeight(256);
                    this.basicSteveSkin.setPersonSkin(true);
                }
            }
            if (StringUtils.isNotBlank(this.baseAlexSkin.getCapeDataEncoded())) {
                this.baseAlexSkin.setCapeData(Base64.getDecoder().decode(this.baseAlexSkin.getCapeDataEncoded()));
            } else {
                this.baseAlexSkin.setCapeData(new byte[0]);
            }
            this.baseAlexSkin.setSkinResourcePatch(String.format("{\"geometry\":{\"default\":\"%s\"},\"convert\":true}", this.baseAlexSkin.getSkinGeometryName()));
            // 配置文件中的数据是经过base64编码过的
            this.baseAlexSkin.setSkinGeometry(new String(Base64.getDecoder().decode(this.baseAlexSkin.getSkinGeometry()), StandardCharsets.UTF_8));
            this.baseAlexSkin.setSerializedAnimationData("");

            LOGGER.info("初始化默认皮肤成功！");
        } catch (Exception e) {
            LOGGER.error("Fail to load default skin", e);

            throw new RuntimeException(e);
        }
    }

    /**
     * 拷贝基础的steve皮肤
     *
     * @param justGeometry
     * @return
     */
    public BasicModelSkin getBasicSteveSkin(boolean justGeometry) {
        if (this.basicSteveSkin == null) {
            return null;
        } else if (justGeometry) {
            this.basicSteveSkin.cloneGeometry();
        }
        return this.basicSteveSkin.clone();
    }

    /**
     * 拷贝基础的alex的皮肤
     *
     * @param justGeometry
     * @return
     */
    public BasicModelSkin getBaseAlexSkin(boolean justGeometry) {
        if (this.baseAlexSkin == null) {
            return null;
        } else if (justGeometry) {
            this.baseAlexSkin.cloneGeometry();
        }
        return this.baseAlexSkin.clone();
    }

    //--------------------------------------------- Skin 操作相关 ----------------------------------------------

    /**
     * 初始化皮肤
     *
     * @param entity
     * @param skinData
     */
    @Override
    public void initSkin(Entity entity, byte[] skinData) {
        this.initSkin(entity, "", SKIN_STEVE, skinData);
    }

    /**
     * 初始化皮肤
     *
     * @param entity
     * @param skinId
     * @param skinData
     */
    @Override
    public void initSkin(Entity entity, String skinId, byte[] skinData) {
        this.initSkin(entity, "", skinId, skinData);
    }

    /**
     * 初始化皮肤
     *
     * @param entity
     * @param skinName
     * @param skinId
     * @param skinData
     */
    @Override
    public void initSkin(Entity entity, String skinName, String skinId, byte[] skinData) {
        this.initSkin(entity, skinName, skinId, skinData, new byte[0]);
    }

    /**
     * 初始化皮肤
     *
     * @param entity
     * @param skinName
     * @param skinId
     * @param skinData
     * @param cape
     */
    @Override
    public void initSkin(Entity entity, String skinName, String skinId, byte[] skinData, byte[] cape) {
        PlayerSkinData playerSkinData = new PlayerSkinData();

        playerSkinData.setSkinId(skinId);
        playerSkinData.setSkinName(skinName);
        playerSkinData.setSkinData(skinData);
        playerSkinData.setCapeData(cape);

        if (playerSkinData.getSkinGeometryName() == null) {
            playerSkinData.setSkinGeometryName("");
            playerSkinData.setSkinResourcePatch("");
        }

        if (playerSkinData.getSkinGeometry() == null) {
            playerSkinData.setSkinGeometry("");
        }

        playerSkinData.setCapeId("");
        playerSkinData.setPremiumSkin(false);
        playerSkinData.setPersonSkin(skinData.length == SUPER_SKIN_SIZE);
        playerSkinData.setPersonCapeOnClassicSkin(false);
        playerSkinData.setPlayerSkinAnimationData(new PlayerSkinAnimationData[0]);
        playerSkinData.setSerializedAnimationData("");

        playerSkinData.setNewVersion(false);

        playerSkinData.setChecked(true);

        this.initSkin(entity, playerSkinData);
    }

    public void initSkin(Entity entity, PlayerSkinData playerSkinData) {
        PlayerSkinModule playerSkinModule = PLAYER_SKIN_MODULE_HANDLER.bindModule(entity);

        playerSkinModule.setBaseSkinData(playerSkinData);
    }

    @Override
    public void updateAdditionSkin(Entity entity, String skinName, String skinId, byte[] skinData, byte[] cape) {
        PlayerSkinData playerSkinData = new PlayerSkinData();

        playerSkinData.setSkinId(skinId);
        playerSkinData.setSkinName(skinName);
        playerSkinData.setSkinData(skinData);
        playerSkinData.setCapeData(cape);

        if (playerSkinData.getSkinGeometryName() == null) {
            playerSkinData.setSkinGeometryName("");
            playerSkinData.setSkinResourcePatch("");
        }

        if (playerSkinData.getSkinGeometry() == null) {
            playerSkinData.setSkinGeometry("");
        }

        playerSkinData.setCapeId("");
        playerSkinData.setPremiumSkin(false);
        playerSkinData.setPersonSkin(skinData.length == SUPER_SKIN_SIZE);
        playerSkinData.setPersonCapeOnClassicSkin(false);
        playerSkinData.setPlayerSkinAnimationData(new PlayerSkinAnimationData[0]);
        playerSkinData.setSerializedAnimationData("");

        playerSkinData.setNewVersion(false);

        playerSkinData.setChecked(true);

        this.updateAdditionSkin(entity, playerSkinData);
    }

    private void updateAdditionSkin(Entity entity, PlayerSkinData playerSkinData) {
        PlayerSkinModule playerSkinModule = PLAYER_SKIN_MODULE_HANDLER.bindModule(entity);
        if (playerSkinModule == null) {
            return;
        }

        playerSkinModule.setAdditionSkinData(playerSkinData);
    }

    @Override
    public void initGeometry(Entity entity, String geometryName, String geometryData) {
        PlayerSkinModule playerSkinModule = PLAYER_SKIN_MODULE_HANDLER.bindModule(entity);

        PlayerSkinData playerSkinData = playerSkinModule.getBaseSkinData();
        if (playerSkinData == null) {
            initSkin(entity, new byte[0]);

            playerSkinData = playerSkinModule.getBaseSkinData();
        }

        playerSkinData.setSkinGeometryName(geometryName);
        playerSkinData.setSkinResourcePatch(String.format("{\"geometry\":{\"default\":\"%s\"},\"convert\":true}", geometryName));
        playerSkinData.setSkinGeometry(geometryData);
    }

    @Override
    public void updateAdditionGeometry(Entity entity, String geometryName, String geometryData) {
        PlayerSkinModule playerSkinModule = PLAYER_SKIN_MODULE_HANDLER.getModule(entity);
        if (playerSkinModule == null) {
            return;
        }

        PlayerSkinData playerSkinData = playerSkinModule.getAdditionSkinData();
        if (playerSkinData == null) {
            this.updateAdditionSkin(entity, "", SKIN_STEVE, new byte[0], new byte[0]);

            playerSkinData = playerSkinModule.getAdditionSkinData();
        }

        playerSkinData.setSkinGeometryName(geometryName);
        playerSkinData.setSkinResourcePatch(String.format("{\"geometry\":{\"default\":\"%s\"},\"convert\":true}", geometryName));
        playerSkinData.setSkinGeometry(geometryData);
    }

    @Override
    public void clearAdditionSkinData(Entity entity) {
        PlayerSkinModule playerSkinModule = PLAYER_SKIN_MODULE_HANDLER.getModule(entity);
        if (playerSkinModule == null) {
            return;
        }

        playerSkinModule.setAdditionSkinData(null);
    }

    /**
     * 读取皮肤
     *
     * @param file
     * @return
     */
    @Override
    public byte[] readSkin(File file) {
        BufferedImage image;
        try {
            image = ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this.readSkin(image);
    }

    /**
     * 读取皮肤
     *
     * @param url
     * @return
     */
    @Override
    public byte[] readSkin(URL url) {
        BufferedImage image;
        try {
            image = ImageIO.read(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this.readSkin(image);
    }

    /**
     * 读取皮肤
     *
     * @param inputStream
     * @return
     */
    @Override
    public byte[] readSkin(InputStream inputStream) {
        BufferedImage image;
        try {
            image = ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this.readSkin(image);
    }

    /**
     * 读取皮肤
     *
     * @param image
     * @return
     */
    @Override
    public byte[] readSkin(BufferedImage image) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = new Color(image.getRGB(x, y), true);
                outputStream.write(color.getRed());
                outputStream.write(color.getGreen());
                outputStream.write(color.getBlue());
                outputStream.write(color.getAlpha());
            }
        }
        image.flush();

        return outputStream.toByteArray();
    }

    /**
     * 读取骨架文件
     *
     * @param file
     * @return
     */
    @Override
    public JSONObject readGeometry(File file) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

            StringBuffer stringBuffer = new StringBuffer();
            String temp = "";
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuffer.append(temp);
            }

            return JSONObject.parseObject(stringBuffer.toString());
        } catch (IOException e) {
            LOGGER.error("Fail to read geometry", e);
        }

        return null;
    }

    /**
     * 读取骨架文件
     *
     * @param inputStream
     * @return
     */
    @Override
    public JSONObject readGeometry(InputStream inputStream) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer stringBuffer = new StringBuffer();
            String temp = "";
            while ((temp = bufferedReader.readLine()) != null) {
                stringBuffer.append(temp);
            }

            return JSONObject.parseObject(stringBuffer.toString());
        } catch (IOException e) {
            LOGGER.error("Fail to read geometry", e);
        }

        return null;
    }

    //----------------------------- skin 广播相关 -------------------------------------

    /**
     * 同步个人的皮肤信息
     * 当玩家spawn的时候调用
     *
     * @param player
     */
    public void refreshSelfSkin(Player player) {
        DataPacket[] refreshPacket = this.getRefreshPacket(player);
        this.networkManager.sendMessage(player.getClientAddress(), refreshPacket);
    }


    @Override
    public void refreshSkin(Entity entity, boolean refreshSelf) {
        DataPacket[] refreshPacket = this.getRefreshPacket(entity);

        for (DataPacket dataPacket : refreshPacket) {
            this.broadcastServiceProxy.broadcast(entity, dataPacket, refreshSelf);
        }
    }

    public DataPacket[] getRefreshPacket(Entity entity) {
        PlayerSkinModule playerSkinModule = PLAYER_SKIN_MODULE_HANDLER.getModule(entity);

        PlayerSkinData skinData = playerSkinModule.getAdditionSkinData();
        if (skinData == null) skinData = playerSkinModule.getBaseSkinData();
        if (skinData == null) return null;

        PlayerListPacket playerListPacket = new PlayerListPacket();
        playerListPacket.setAction(PlayerListPacket.PlayerListAction.ADD);
        PlayerListPacket.PlayerListEntry playerListEntry = new PlayerListPacket.PlayerListEntry();
        playerListEntry.setEntityUUID(this.UUID_MODULE_HANDLER.getModule(entity).getUuid());
        playerListEntry.setEntityId(entity.getRuntimeId());
        playerListEntry.setEntityName(this.entityNameService.getEntityName(entity));
        playerListEntry.setSkinId(skinData.getSkinId());
        playerListEntry.setSkinData(skinData.getSkinData());
        playerListEntry.setSkinWidth(skinData.getSkinWidth());
        playerListEntry.setSkinHeight(skinData.getSkinHeight());
        playerListEntry.setCapeId(skinData.getCapeId());
        playerListEntry.setCapeData(skinData.getCapeData());
        playerListEntry.setCapeWidth(skinData.getCapeWidth());
        playerListEntry.setCapeHeight(skinData.getCapeHeight());
        playerListEntry.setSkinGeometryName(skinData.getSkinGeometryName());
        playerListEntry.setSkinGeometry(skinData.getSkinGeometry());
        playerListEntry.setSkinResourcePatch(skinData.getSkinResourcePatch());
        playerListEntry.setReserved("");
        playerListEntry.setChatId("");
        playerListEntry.setPremiumSkin(skinData.isPremiumSkin());
        playerListEntry.setPersonSkin(skinData.isPersonSkin());
        playerListEntry.setPersonCapeOnClassicSkin(skinData.isPersonCapeOnClassicSkin());
        playerListEntry.setPlayerSkinAnimationData(skinData.getPlayerSkinAnimationData());
        playerListEntry.setSerializedAnimationData(skinData.getSerializedAnimationData());
        playerListEntry.setNewVersion(skinData.isNewVersion());
        if (skinData.getSkinData().length == SUPER_SKIN_SIZE) {
            playerListEntry.setSkinWidth(256);
            playerListEntry.setSkinHeight(256);
        }

        playerListPacket.addPlayerListEntry(playerListEntry);

        ConfirmSkinPacket confirmSkinPacket = null;
        if (skinData.isChecked()) {
            confirmSkinPacket = new ConfirmSkinPacket();
            confirmSkinPacket.addPlayerListEntry(playerListEntry.getEntityUUID());
        }

        if (confirmSkinPacket == null || skinData.isPersonSkin()) {
            return new DataPacket[]{playerListPacket};
        } else {
            return new DataPacket[]{playerListPacket, confirmSkinPacket};
        }
    }

    public DataPacket getRemovePacket(Entity entity) {
        PlayerListPacket playerListPacket = new PlayerListPacket();
        playerListPacket.setAction(PlayerListPacket.PlayerListAction.REMOVE);
        PlayerListPacket.PlayerListEntry playerListEntry = new PlayerListPacket.PlayerListEntry();
        playerListEntry.setEntityUUID(this.UUID_MODULE_HANDLER.getModule(entity).getUuid());
        playerListEntry.setEntityId(entity.getRuntimeId());
        playerListEntry.setEntityName("");
        playerListEntry.setSkinId("");
        playerListEntry.setSkinData(new byte[0]);
        playerListEntry.setCapeId("");
        playerListEntry.setCapeData(new byte[0]);
        playerListEntry.setSkinGeometry("");
        playerListEntry.setSkinGeometryName("");
        playerListEntry.setSkinResourcePatch("");
        playerListEntry.setReserved("");
        playerListEntry.setChatId("");
        playerListEntry.setPremiumSkin(false);
        playerListEntry.setPersonSkin(false);
        playerListEntry.setPersonCapeOnClassicSkin(false);
        playerListEntry.setPlayerSkinAnimationData(null);
        playerListEntry.setSerializedAnimationData("");
        playerListEntry.setNewVersion(true);
        playerListPacket.addPlayerListEntry(playerListEntry);

        return playerListPacket;
    }
}
