package com.particle.network;

public enum PlayerConnectionState {

    /**
     * The entity is still waiting for the login packet.
     */
    HANDSHAKE,

    /**
     * We told the client it should get ready for encryption
     */
    ENCRPYTION_INIT,

    /**
     * Sending resource packs and waiting for the client to decide
     */
    RESOURCE_PACK,

    /**
     * The entity has logged in and is preparing for playing.
     */
    LOGIN,

    /**
     * The entity is entirely connected and is playing on the server.
     */
    PLAYING;

}
