package com.particle.game.player.auth.handle;


import com.particle.game.player.auth.decoder.NeteasePlayerDataDecoder;
import com.particle.game.player.auth.decoder.ProxyChainDataDecoder;
import com.particle.game.player.auth.model.ClientChainData;

public class ProxyNeteaseLoginDecoder {

    public static ClientChainData decode(String chainByteData, String playerByteData, int protocolVersion) {
        ClientChainData chainData = new ClientChainData();

        ProxyChainDataDecoder.decode(chainData, chainByteData);
        NeteasePlayerDataDecoder.decode(chainData, playerByteData, protocolVersion);

        return chainData;
    }
}
