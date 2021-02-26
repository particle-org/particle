package com.particle.game.server.behaviors;

import com.alibaba.fastjson.JSON;
import com.particle.model.resources.ResourcePackInfo;
import com.particle.model.resources.ResourcePackManifest;
import com.particle.model.resources.ResourcePackModule;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 材质包文件的管理
 * 默认会缓存所有的材质包文件
 */
@Singleton
public class BehaviorsPackManager {

    private static final Logger logger = LoggerFactory.getLogger(BehaviorsPackManager.class);

    private static final String behaviorPackPath = "behavior_packs";

    private ResourcePackInfo[] behaviorPackInfos = null;

    private Map<String, ResourcePackInfo> behaviorPackInfoMap = new HashMap<>();

    // 是否需要将数据缓存到内存中
    private boolean needCacheData = true;

    // 1024*1024
    public static final int CHUNK_RESOURCE_SCALE_SIZE = 1048576;

    public BehaviorsPackManager() {
        this.loadAll();
    }

    /**
     * 获取所有的材质包
     *
     * @return
     */
    public ResourcePackInfo[] getBehaviorPackInfo() {
        return behaviorPackInfos == null ? new ResourcePackInfo[0] : behaviorPackInfos;
    }

    /**
     * 根据id获取材质包
     *
     * @param id
     * @return
     */
    public ResourcePackInfo getBehaviorPackById(String id) {
        if (StringUtils.isEmpty(id)) {
            return null;
        }
        return this.behaviorPackInfoMap.get(id);
    }

    /**
     * 加载材质包，将文件中信息加载到内存中
     *
     * @return
     */
    private boolean loadAll() {
        File behaviorFile = new File(behaviorPackPath);
        if (!behaviorFile.exists()) {
            behaviorFile.mkdirs();
        }
        List<ResourcePackInfo> loadedBehaviorPacks = new ArrayList<>();
        File[] files = behaviorFile.listFiles();
        if (files == null) {
            return true;
        }
        for (File pack : files) {
            try {
                if (pack.isDirectory()) {
                    logger.warn("warming: load resource pack, don't support directory!");
                    continue;
                }
                String extension = FilenameUtils.getExtension(pack.getName());
                if (StringUtils.isEmpty(extension)) {
                    logger.warn("warming: load resource pack, don't support no extension files!");
                    continue;
                }
                if (!extension.equalsIgnoreCase("zip") && !extension.equalsIgnoreCase("bhpack")) {
                    logger.warn("warming: load resource pack, only support zip or mcpack extension files!");
                    continue;
                }
                ResourcePackInfo resourcePackInfo = this.parseBehaviorPack(pack);
                if (resourcePackInfo != null) {
                    loadedBehaviorPacks.add(resourcePackInfo);
                    behaviorPackInfoMap.put(resourcePackInfo.getId(), resourcePackInfo);
                }
            } catch (Exception e) {
                logger.error("load resource failed!", e);
            }
        }
        this.behaviorPackInfos = loadedBehaviorPacks.toArray(new ResourcePackInfo[loadedBehaviorPacks.size()]);
        return true;
    }

    /**
     * 解析文件为resourcePack格式
     *
     * @param pack
     * @return
     * @throws IOException
     */
    private ResourcePackInfo parseBehaviorPack(File pack) {
        if (!pack.exists()) {
            logger.error("the pack is not existed! it may be deleted!");
            return null;
        }
        ZipFile zipFile = null;
        InputStream manifestInputStream = null;
        try {
            zipFile = new ZipFile(pack);
            ZipEntry entry = zipFile.getEntry("pack_manifest.json");
            if (entry == null) {
                logger.error("parse behavior pack, the pack_manifest.json file is not existed!");
                return null;
            }
            manifestInputStream = zipFile.getInputStream(entry);
            String manifestStr = IOUtils.toString(manifestInputStream, StandardCharsets.UTF_8);
            ResourcePackManifest resourcePackManifest = JSON.parseObject(manifestStr, ResourcePackManifest.class);
            if (resourcePackManifest == null) {
                logger.error("fastJson parse manifest failed!");
                return null;
            }
            if (!checkManifest(resourcePackManifest)) {
                return null;
            }
            ResourcePackInfo resourcePackInfo = new ResourcePackInfo();
            resourcePackInfo.setId(resourcePackManifest.getHeader().getUuid());
            resourcePackInfo.setSubPackName(resourcePackManifest.getHeader().getName());
            resourcePackInfo.setVersion(StringUtils.join(resourcePackManifest.getHeader().getVersion(), "."));
            resourcePackInfo.setContentKey("");
            resourcePackInfo.setSize(pack.length());
            resourcePackInfo.setFilePath(pack.getPath());
            resourcePackInfo.setMaxChunkIndex((int) (pack.length() / CHUNK_RESOURCE_SCALE_SIZE));
            resourcePackInfo.setFileHash(checkBehaviorHash(pack));
            Map<Integer, byte[]> chunkData = new HashMap<>();
            if (needCacheData) {
                for (int i = 0; i <= resourcePackInfo.getMaxChunkIndex(); i++) {
                    byte[] packData = this.getPackData(pack, i, (int) resourcePackInfo.getSize());
                    chunkData.put(i, packData);
                }
            }
            resourcePackInfo.setChunkData(chunkData);
            return resourcePackInfo;
        } catch (IOException ioe) {
            logger.error("parseResourcePack failed!", ioe);
        } finally {
            if (manifestInputStream != null) {
                try {
                    manifestInputStream.close();
                } catch (IOException ioe) {

                }
            }
            if (zipFile != null) {
                try {
                    zipFile.close();
                } catch (IOException ioe) {

                }
            }
        }
        return null;
    }

    /**
     * 获取文件的sha256值
     *
     * @param pack
     * @return
     * @throws IOException
     */
    private byte[] checkBehaviorHash(File pack) throws IOException {
        byte[] packBytes = FileUtils.readFileToByteArray(pack);
        if (packBytes == null) {
            return new byte[0];
        }
        byte[] sha256 = null;
        try {
            sha256 = MessageDigest.getInstance("SHA-256").digest(packBytes);
        } catch (Exception e) {
            logger.error("checkResourceHash failed!", e);
            return new byte[0];
        }
        return sha256;
    }

    /**
     * 检测manifest格式
     *
     * @param resourcePackManifest
     * @return
     */
    private boolean checkManifest(ResourcePackManifest resourcePackManifest) {
        if (resourcePackManifest == null) {
            return false;
        }
        if (resourcePackManifest.getFormat_version() <= 0) {
            logger.error("checkManifest failed!, the formatVersion is illegal!");
            return false;
        }
        if (resourcePackManifest.getHeader() == null) {
            logger.error("checkManifest failed!, the header is illegal!");
            return false;
        }
        if (resourcePackManifest.getModules() == null) {
            logger.error("checkManifest failed!, the modules is illegal!");
            return false;
        }
        ResourcePackModule header = resourcePackManifest.getHeader();
        if (header.getDescription() == null) {
            logger.error("checkManifest failed!, the header[description] is illegal!");
            return false;
        }
        if (header.getName() == null) {
            logger.error("checkManifest failed!, the header[name] is illegal!");
            return false;
        }
        if (header.getUuid() == null) {
            logger.error("checkManifest failed!, the header[uuid] is illegal!");
            return false;
        }
        if (header.getVersion() == null || header.getVersion().size() != 3) {
            logger.error("checkManifest failed!, the header[version] is illegal!");
            return false;
        }
        return true;
    }

    /**
     * 根据scale，读取不同段的材质包文件
     *
     * @param pack
     * @param startIndex
     * @param length
     * @return
     */
    private byte[] getPackData(File pack, int startIndex, int length) {
        if (!pack.exists()) {
            return new byte[0];
        }
        byte[] packData = null;
        int off = startIndex * CHUNK_RESOURCE_SCALE_SIZE;
        if (length > off + CHUNK_RESOURCE_SCALE_SIZE) {
            packData = new byte[CHUNK_RESOURCE_SCALE_SIZE];
        } else {
            packData = new byte[length - off];
        }
        try (FileInputStream fileInputStream = new FileInputStream(pack)) {
            fileInputStream.skip(off);
            fileInputStream.read(packData);
        } catch (IOException ioe) {
            logger.error("getPackData failed!", ioe);
            return new byte[0];
        }
        return packData;
    }

    /**
     * 按刻度分割材质包文件，用于下发给客户端
     * 如果缓存标志为true，可直接从缓存中获取
     *
     * @param id
     * @param startIndex
     * @return
     */
    public byte[] getPackData(String id, int startIndex) {
        ResourcePackInfo resourcePackInfo = this.getBehaviorPackById(id);
        if (resourcePackInfo == null) {
            return new byte[0];
        }
        if (needCacheData) {
            Map<Integer, byte[]> chunkData = resourcePackInfo.getChunkData();
            if (chunkData != null && chunkData.containsKey(startIndex)) {
                return chunkData.get(startIndex);
            } else {
                return new byte[0];
            }
        }
        return this.getPackData(new File(resourcePackInfo.getFilePath()), startIndex, (int) resourcePackInfo.getSize());
    }
}
