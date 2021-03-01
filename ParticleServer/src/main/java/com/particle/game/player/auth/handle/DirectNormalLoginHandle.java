package com.particle.game.player.auth.handle;


import com.particle.game.player.auth.decoder.DefaultChainDataDecoder;
import com.particle.game.player.auth.decoder.DefaultPlayerDataDecoder;
import com.particle.game.player.auth.model.ClientChainData;

public class DirectNormalLoginHandle {

    public static ClientChainData decode(String chainDataString, String playerDataString, int protocolVersion) {
        ClientChainData chainData = new ClientChainData();

        DefaultChainDataDecoder.decode(chainData, chainDataString);
        DefaultPlayerDataDecoder.decode(chainData, playerDataString, protocolVersion);

        return chainData;
    }
}
