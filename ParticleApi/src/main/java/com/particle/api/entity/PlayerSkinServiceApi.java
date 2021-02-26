package com.particle.api.entity;

import com.alibaba.fastjson.JSONObject;
import com.particle.model.entity.Entity;
import com.particle.model.network.packets.DataPacket;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URL;

public interface PlayerSkinServiceApi {

    /**
     * 初始化生物皮肤
     *
     * @param entity   操作生物
     * @param skinData 皮肤数据
     */
    void initSkin(Entity entity, byte[] skinData);

    /**
     * 初始化生物皮肤
     *
     * @param entity   操作生物
     * @param skinId   皮肤ID
     * @param skinData 皮肤数据
     */
    void initSkin(Entity entity, String skinId, byte[] skinData);

    /**
     * 初始化生物皮肤
     *
     * @param entity   操作生物
     * @param skinName 皮肤名称
     * @param skinId   皮肤ID
     * @param skinData 皮肤数据
     */
    void initSkin(Entity entity, String skinName, String skinId, byte[] skinData);

    /**
     * 初始化生物皮肤
     *
     * @param entity   操作生物
     * @param skinName 皮肤名称
     * @param skinId   皮肤ID
     * @param skinData 皮肤数据
     * @param cape
     */
    void initSkin(Entity entity, String skinName, String skinId, byte[] skinData, byte[] cape);

    /**
     * 初始化额外皮肤模块
     *
     * @param entity
     * @param skinName
     * @param skinId
     * @param skinData
     * @param cape
     */
    void updateAdditionSkin(Entity entity, String skinName, String skinId, byte[] skinData, byte[] cape);

    /**
     * 初始化生物骨架
     *
     * @param entity       操作生物
     * @param geometryName 骨架名称
     * @param geometryData 骨架数据
     */
    void initGeometry(Entity entity, String geometryName, String geometryData);

    /**
     * 初始化额外骨架模块
     *
     * @param entity
     * @param geometryName
     * @param geometryData
     */
    void updateAdditionGeometry(Entity entity, String geometryName, String geometryData);

    /**
     * 清理皮肤
     *
     * @param entity
     */
    void clearAdditionSkinData(Entity entity);

    /**
     * 读取皮肤数据
     *
     * @param file 文件
     * @return 皮肤数据
     */
    byte[] readSkin(File file);

    /**
     * 读取皮肤数据
     *
     * @param url UEL地址
     * @return 皮肤数据
     */
    byte[] readSkin(URL url);

    /**
     * 读取皮肤数据
     *
     * @param inputStream 输入Stream
     * @return 皮肤数据
     */
    byte[] readSkin(InputStream inputStream);

    /**
     * 读取皮肤文件
     *
     * @param image 图片Buffer
     * @return 皮肤数据
     */
    byte[] readSkin(BufferedImage image);

    /**
     * 读取骨架数据
     *
     * @param file 骨架文件
     * @return 骨架数据
     */
    JSONObject readGeometry(File file);

    /**
     * 读取骨架数据
     *
     * @param inputStream 数据文件流
     * @return 骨架数据
     */
    JSONObject readGeometry(InputStream inputStream);

    /**
     * 刷新皮肤
     *
     * @param entity
     */
    void refreshSkin(Entity entity, boolean refreshSelf);


    DataPacket[] getRefreshPacket(Entity entity);
}

