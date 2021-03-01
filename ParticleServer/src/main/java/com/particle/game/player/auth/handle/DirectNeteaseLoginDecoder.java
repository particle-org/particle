package com.particle.game.player.auth.handle;


import com.particle.game.player.auth.decoder.NeteaseChainDataDecoder;
import com.particle.game.player.auth.decoder.NeteasePlayerDataDecoder;
import com.particle.game.player.auth.model.ClientChainData;

public class DirectNeteaseLoginDecoder {

    public static ClientChainData decode(String chainDataString, String playerDataString, int protocolVersion) {
        ClientChainData chainData = new ClientChainData();

        NeteaseChainDataDecoder.decode(chainData, chainDataString);
        NeteasePlayerDataDecoder.decode(chainData, playerDataString, protocolVersion);

        return chainData;
    }
}
