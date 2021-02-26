package com.particle.game.player.auth.decoder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.particle.game.player.auth.exception.AuthException;
import com.particle.game.player.auth.model.ClientChainData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ProxyChainDataDecoder {

    private static final Logger logger = LoggerFactory.getLogger(ProxyChainDataDecoder.class);

    public static void decode(ClientChainData chainData, String chainDataString) {
        //获取认证chain
        Map<String, List<String>> map = new Gson()
                .fromJson(
                        chainDataString,
                        new TypeToken<Map<String, List<String>>>() {
                        }.getType()
                );

        //解析auth的数据
        if (map.isEmpty() || !map.containsKey("proxyAuth") || map.get("proxyAuth").isEmpty()) return;
        List<String> proxyAuth = map.get("proxyAuth");

        try {
            String token = proxyAuth.get(0);
            String username = proxyAuth.get(1);
            UUID clientUUID = UUID.fromString(proxyAuth.get(2));
            String xuid = proxyAuth.get(3);
            long uid = Long.parseLong(proxyAuth.get(4));
            short version = Short.parseShort(proxyAuth.get(5));
            String env = proxyAuth.get(6);

            if (token.equals(md5(username))) {
                chainData.setUsername(username);
                chainData.setClientUUID(clientUUID);
                chainData.setXUID(xuid);
                chainData.setEnv(env);
                chainData.setUid(uid);
                chainData.setVersion(version);
            } else {
                logger.info(String.format("error chain data:%s, %s", username, token));
            }
        } catch (Exception e) {
            throw new AuthException("Auth error");
        }
    }

    private static String md5(String username) {
        try {
            byte[] md5Code = MessageDigest.getInstance("MD5").digest(("Prophet:" + username).getBytes(StandardCharsets.UTF_8));

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < md5Code.length; ++i) {
                sb.append(Integer.toHexString((md5Code[i] & 0xFF) | 0x100).substring(1, 3));
            }

            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();

            return null;
        }
    }
}
