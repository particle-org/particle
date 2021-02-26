package com.particle.game.player.auth.decoder;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.particle.game.player.auth.model.ClientChainData;
import com.particle.model.entity.model.skin.PlayerSkinAnimationData;
import com.particle.model.entity.model.skin.PlayerSkinData;
import com.particle.network.handler.AbstractPacketHandler;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class DefaultPlayerDataDecoder {

    private static final int SINGLE_SKIN_SIZE = 64 * 32 * 4;
    private static final int DOUBLE_SKIN_SIZE = 64 * 64 * 4;
    private static final int LARGE_SKIN_SIZE = 128 * 128 * 4;


    public static void decode(ClientChainData chainData, String playerDataString, int protocolVersion) {
        //读取额外信息
        JsonObject playerData = decodeToken(playerDataString);
        if (playerData != null) {
            if (playerData.has("ClientRandomId")) {
                chainData.setClientId(playerData.get("ClientRandomId").getAsLong());
            }

            if (playerData.has("GameVersion")) {
                chainData.setGameVersion(playerData.get("GameVersion").getAsString());
            }

            // 考虑到只用兼容3个版本，这里就不做复杂的判断，提高效率
            if (protocolVersion >= AbstractPacketHandler.VERSION_1_13) {
                PlayerSkinData playerSkinData = new PlayerSkinData();

                if (playerData.has("SkinId")) {
                    String[] skinIdAndName = playerData.get("SkinId").getAsString().split("_");
                    playerSkinData.setSkinId(skinIdAndName[0] + "_" + chainData.getUsername());
                    if (skinIdAndName.length > 1) {
                        playerSkinData.setSkinName(skinIdAndName[1]);
                    } else {
                        playerSkinData.setSkinName("");
                    }
                }

                if (playerData.has("SkinData")) {
                    playerSkinData.setSkinData(Base64.getDecoder().decode(playerData.get("SkinData").getAsString()));
                }

                if (playerData.has("SkinImageHeight")) {
                    playerSkinData.setSkinHeight(playerData.get("SkinImageHeight").getAsInt());
                }

                if (playerData.has("SkinImageWidth")) {
                    playerSkinData.setSkinWidth(playerData.get("SkinImageWidth").getAsInt());
                }

                if (playerData.has("CapeId")) {
                    playerSkinData.setCapeId(playerData.get("CapeId").getAsString());
                } else {
                    playerSkinData.setCapeId("");
                }

                if (playerData.has("CapeData")) {
                    playerSkinData.setCapeData(Base64.getDecoder().decode(playerData.get("CapeData").getAsString()));
                }

                if (playerData.has("CapeImageHeight")) {
                    playerSkinData.setCapeHeight(playerData.get("CapeImageHeight").getAsInt());
                }

                if (playerData.has("CapeImageWidth")) {
                    playerSkinData.setCapeWidth(playerData.get("CapeImageWidth").getAsInt());
                }

                if (playerData.has("SkinResourcePatch")) {
                    String skinResourcePatch = new String(Base64.getDecoder().decode(playerData.get("SkinResourcePatch").getAsString()), StandardCharsets.UTF_8);
                    playerSkinData.setSkinResourcePatch(skinResourcePatch);

                    JSONObject jsonObject = JSON.parseObject(skinResourcePatch);
                    playerSkinData.setSkinGeometryName(jsonObject.getJSONObject("geometry").getString("default"));
                }

                if (playerData.has("SkinGeometryData")) {
                    playerSkinData.setSkinGeometry(new String(Base64.getDecoder().decode(playerData.get("SkinGeometryData").getAsString()), StandardCharsets.UTF_8));
                }

                if (playerData.has("SkinGeometryData")) {
                    playerSkinData.setSkinGeometry(new String(Base64.getDecoder().decode(playerData.get("SkinGeometryData").getAsString()), StandardCharsets.UTF_8));
                }

                if (playerData.has("PremiumSkin")) {
                    playerSkinData.setPremiumSkin(playerData.get("PremiumSkin").getAsBoolean());
                }

                if (playerData.has("PersonaSkin")) {
                    playerSkinData.setPersonSkin(playerData.get("PersonaSkin").getAsBoolean());
                }

                if (playerData.has("CapeOnClassicSkin")) {
                    playerSkinData.setPersonCapeOnClassicSkin(playerData.get("CapeOnClassicSkin").getAsBoolean());
                }

                if (playerData.has("AnimatedImageData")) {
                    JsonArray animationsData = playerData.get("AnimatedImageData").getAsJsonArray();

                    PlayerSkinAnimationData[] playerSkinAnimationDataList = new PlayerSkinAnimationData[animationsData.size()];
                    for (int i = 0; i < animationsData.size(); i++) {
                        JsonObject animation = animationsData.get(i).getAsJsonObject();
                        PlayerSkinAnimationData playerSkinAnimationData = new PlayerSkinAnimationData();
                        playerSkinAnimationData.setAnimationType(animation.get("Type").getAsInt());
                        playerSkinAnimationData.setHeight(animation.get("ImageHeight").getAsInt());
                        playerSkinAnimationData.setWidth(animation.get("ImageWidth").getAsInt());
                        playerSkinAnimationData.setFrameCount(animation.get("Frames").getAsFloat());
                        playerSkinAnimationData.setImages(Base64.getDecoder().decode(animation.get("Image").getAsString()));
                        playerSkinAnimationDataList[i] = playerSkinAnimationData;
                    }
                    playerSkinData.setPlayerSkinAnimationData(playerSkinAnimationDataList);
                }

                if (playerData.has("SkinAnimationData")) {
                    playerSkinData.setSerializedAnimationData(playerData.get("SkinAnimationData").getAsString());
                }

                playerSkinData.setNewVersion(true);

                playerSkinData.setChecked(false);

                chainData.setPlayerSkinData(playerSkinData);
            } else {
                PlayerSkinData playerSkinData = new PlayerSkinData();

                if (playerData.has("SkinId")) {
                    String[] skinIdAndName = playerData.get("SkinId").getAsString().split("_");
                    playerSkinData.setSkinId(skinIdAndName[0] + "_" + chainData.getUsername());
                    if (skinIdAndName.length == 2) {
                        playerSkinData.setSkinName(skinIdAndName[1]);
                    }
                    playerSkinData.setSkinData(Base64.getDecoder().decode(playerData.get("SkinData").getAsString()));
                    playerSkinData.setCapeData(Base64.getDecoder().decode(playerData.get("CapeData").getAsString()));

                    if (playerSkinData.getSkinData().length == SINGLE_SKIN_SIZE) {
                        playerSkinData.setSkinWidth(64);
                        playerSkinData.setSkinHeight(32);
                    } else if (playerSkinData.getSkinData().length == DOUBLE_SKIN_SIZE) {
                        playerSkinData.setSkinWidth(64);
                        playerSkinData.setSkinHeight(64);
                    } else if (playerSkinData.getSkinData().length == LARGE_SKIN_SIZE) {
                        playerSkinData.setSkinWidth(128);
                        playerSkinData.setSkinHeight(128);
                    }
                }

                if (playerData.has("SkinGeometryName")) {
                    playerSkinData.setSkinResourcePatch(String.format("{\"geometry\":{\"default\":\"%s\"},\"convert\":true}", playerData.get("SkinGeometryName").getAsString()));
                    playerSkinData.setSkinGeometryName(playerData.get("SkinGeometryName").getAsString());
                }

                if (playerData.has("SkinGeometry")) {
                    playerSkinData.setSkinGeometry(new String(Base64.getDecoder().decode(playerData.get("SkinGeometry").getAsString()), StandardCharsets.UTF_8));
                }

                playerSkinData.setCapeId("");

                playerSkinData.setPremiumSkin(false);
                playerSkinData.setPersonSkin(false);
                playerSkinData.setPersonCapeOnClassicSkin(false);
                playerSkinData.setPlayerSkinAnimationData(new PlayerSkinAnimationData[0]);
                playerSkinData.setSerializedAnimationData("");

                playerSkinData.setNewVersion(false);

                playerSkinData.setChecked(false);

                chainData.setPlayerSkinData(playerSkinData);
            }

            if (playerData.has("ClientRandomId")) {
                chainData.setClientId(playerData.get("ClientRandomId").getAsLong());
            }

            if (playerData.has("ServerAddress")) {
                chainData.setServerAddress(playerData.get("ServerAddress").getAsString());
            }

            if (playerData.has("DeviceModel")) {
                chainData.setDeviceModel(playerData.get("DeviceModel").getAsString());
            }

            if (playerData.has("DeviceOS")) {
                chainData.setDeviceOS(playerData.get("DeviceOS").getAsInt());
            }

            if (playerData.has("GuiScale")) {
                chainData.setGuiScale(playerData.get("GuiScale").getAsInt());
            }

            if (playerData.has("LanguageCode")) {
                chainData.setLanguageCode(playerData.get("LanguageCode").getAsString());
            }

            if (playerData.has("CurrentInputMode")) {
                chainData.setCurrentInputMode(playerData.get("CurrentInputMode").getAsInt());
            }

            if (playerData.has("DefaultInputMode")) {
                chainData.setDefaultInputMode(playerData.get("DefaultInputMode").getAsInt());
            }

            if (playerData.has("UIProfile")) {
                chainData.setUIProfile(playerData.get("UIProfile").getAsInt());
            }
        }
    }

    private static JsonObject decodeToken(String token) {
        String[] base = token.split("\\.");
        if (base.length < 2) return null;
        return new Gson().fromJson(new String(Base64.getDecoder().decode(base[1]), StandardCharsets.UTF_8), JsonObject.class);
    }
}
