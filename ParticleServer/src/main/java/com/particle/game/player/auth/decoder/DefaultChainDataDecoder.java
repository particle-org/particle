package com.particle.game.player.auth.decoder;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.particle.game.player.auth.exception.AuthException;
import com.particle.game.player.auth.model.ClientChainData;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DefaultChainDataDecoder {
    public static void decode(ClientChainData chainData, String chainDataString) {
        Map<String, List<String>> map = new Gson()
                .fromJson(
                        chainDataString,
                        new TypeToken<Map<String, List<String>>>() {
                        }.getType()
                );

        //读取玩家
        List<String> chains = map.get("chain");
        if (chains == null || chains.isEmpty()) {
            throw new AuthException("Chain data error");
        } else {
            for (String chain : chains) {
                JsonObject chainMap = decodeToken(chain);
                if (chainMap == null) continue;
                if (chainMap.has("extraData")) {
                    JsonObject extra = chainMap.get("extraData").getAsJsonObject();

                    if (extra.has("displayName")) {
                        chainData.setUsername(extra.get("displayName").getAsString());
                    }

                    if (extra.has("identity")) {
                        chainData.setClientUUID(UUID.fromString(extra.get("identity").getAsString()));
                    }

                    if (extra.has("XUID")) {
                        chainData.setXUID(extra.get("XUID").getAsString());
                    }
                }

                if (chainMap.has("identityPublicKey")) {
                    chainData.setIdentityPublicKey(chainMap.get("identityPublicKey").getAsString());
                }
            }
        }
    }

    private static JsonObject decodeToken(String token) {
        String[] base = token.split("\\.");
        if (base.length < 2) return null;
        return new Gson().fromJson(new String(Base64.getDecoder().decode(base[1]), StandardCharsets.UTF_8), JsonObject.class);
    }
}
