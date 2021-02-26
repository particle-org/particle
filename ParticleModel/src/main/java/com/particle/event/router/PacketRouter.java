package com.particle.event.router;

import com.particle.event.handle.PacketEventHandle;
import com.particle.model.network.packets.DataPacket;


/**
 * 数据包路由.
 */
public class PacketRouter {
    private static final PacketEventHandle[] PACKET_EVENT_HANDLES = new PacketEventHandle[512];

    public static void subscript(PacketEventHandle handle) {
        if (handle.getTargetPacketID() >= 512) {
            throw new RuntimeException("Illegal packet id");
        }

        PACKET_EVENT_HANDLES[handle.getTargetPacketID()] = handle;
    }

    public static PacketEventHandle route(DataPacket event) {
        if (PACKET_EVENT_HANDLES[event.pid()] != null) {
            return PACKET_EVENT_HANDLES[event.pid()];
        }

        return null;
    }
}
