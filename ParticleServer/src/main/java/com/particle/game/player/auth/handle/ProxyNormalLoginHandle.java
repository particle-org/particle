package com.particle.game.player.auth.handle;


import com.particle.game.player.auth.decoder.DefaultPlayerDataDecoder;
import com.particle.game.player.auth.decoder.ProxyChainDataDecoder;
import com.particle.game.player.auth.model.ClientChainData;

public class ProxyNormalLoginHandle {
    public static ClientChainData decode(String chainDataString, String playerDataString, int protocolVersion) {
        ClientChainData chainData = new ClientChainData();

        ProxyChainDataDecoder.decode(chainData, chainDataString);
        DefaultPlayerDataDecoder.decode(chainData, playerDataString, protocolVersion);

        return chainData;
    }
}
