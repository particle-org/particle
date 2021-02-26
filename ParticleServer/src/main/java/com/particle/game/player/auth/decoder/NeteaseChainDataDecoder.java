package com.particle.game.player.auth.decoder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.netease.mc.authlib.Profile;
import com.netease.mc.authlib.TokenChain;
import com.particle.game.player.auth.exception.AuthException;
import com.particle.game.player.auth.model.ClientChainData;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NeteaseChainDataDecoder {
    public static void decode(ClientChainData chainData, String chainDataString) {
        Map<String, List<String>> map = new Gson()
                .fromJson(
                        chainDataString,
                        new TypeToken<Map<String, List<String>>>() {
                        }.getType()
                );

        //读取玩家
        List<String> chains = map.get("chain");
        if (chains == null || chains.size() < 2) {
            throw new AuthException("chains error");
        } else {
            String[] chainArr = new String[chains.size() - 1];
            Iterator<String> iterator = chains.iterator();
            int index = 0;
            iterator.next();
            while (iterator.hasNext()) {
                chainArr[index++] = iterator.next();
            }

            try {
                Profile profile = TokenChain.check(chainArr);
                chainData.setUsername(profile.displayName);
                chainData.setClientUUID(profile.identity);
                chainData.setXUID(profile.XUID);
                // 认证服认证
                chainData.setEnv(profile.env);
                chainData.setUid(profile.uid);
                chainData.setVersion(profile.version);

                chainData.setIdentityPublicKey(profile.clientPubKey);
            } catch (Exception e) {
                // TODO: handle exception
                throw new AuthException("Check error");
            }
        }
    }
}
