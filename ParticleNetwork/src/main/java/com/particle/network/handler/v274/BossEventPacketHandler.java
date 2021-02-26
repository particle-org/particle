package com.particle.network.handler.v274;

import com.particle.model.network.packets.data.BossEventPacket;
import com.particle.network.handler.AbstractPacketHandler;

public class BossEventPacketHandler extends AbstractPacketHandler<BossEventPacket> {
    @Override
    protected void doDecode(BossEventPacket dataPacket, int version) {
        dataPacket.setEntityId(dataPacket.readSignedVarLong().longValue());

        int type = dataPacket.readUnsignedVarInt();
        switch (type) {
            case 0:
                dataPacket.setEventType(BossEventPacket.EventType.ADD);
                break;
            case 1:
                dataPacket.setEventType(BossEventPacket.EventType.PLAYER_ADDED);
                break;
            case 2:
                dataPacket.setEventType(BossEventPacket.EventType.REMOVE);
                break;
            case 3:
                dataPacket.setEventType(BossEventPacket.EventType.PLAYER_REMOVED);
                break;
            case 4:
                dataPacket.setEventType(BossEventPacket.EventType.UPDATE_PERCENT);
                break;
            case 5:
                dataPacket.setEventType(BossEventPacket.EventType.UPDATE_NAME);
                break;
            case 6:
                dataPacket.setEventType(BossEventPacket.EventType.UPDATE_PROPERTIES);
                break;
            case 7:
                dataPacket.setEventType(BossEventPacket.EventType.UPDATE_STYLE);
                break;
            default:
                return;
        }

        switch (dataPacket.getEventType()) {
            case ADD:
                dataPacket.setBossName(dataPacket.readString());
                dataPacket.setHealthPercent(dataPacket.readLFloat());
            case UPDATE_PROPERTIES:
                // TODO: 2019/1/3 改成Unsigned Short
                dataPacket.setDarkenScreen(dataPacket.readShort());
            case UPDATE_STYLE:
                dataPacket.setColor(dataPacket.readUnsignedVarInt());
                dataPacket.setOverlay(dataPacket.readUnsignedVarInt());
                break;
            case PLAYER_ADDED:
            case PLAYER_REMOVED:
                dataPacket.setPlayerEntityId(dataPacket.readSignedVarLong().longValue());
                break;
            case REMOVE:
                break;
            case UPDATE_PERCENT:
                dataPacket.setHealthPercent(dataPacket.readLFloat());
                break;
            case UPDATE_NAME:
                dataPacket.setBossName(dataPacket.readString());
                break;
        }
    }

    @Override
    protected void doEncode(BossEventPacket dataPacket, int version) {
        dataPacket.writeSignedVarLong(dataPacket.getEntityId());

        dataPacket.writeUnsignedVarInt(dataPacket.getEventType().getValue());
        switch (dataPacket.getEventType()) {
            case ADD:
                dataPacket.writeString(dataPacket.getBossName());
                dataPacket.writeLFloat(dataPacket.getHealthPercent());
            case UPDATE_PROPERTIES:
                // TODO: 2019/1/3 改成Unsigned Short
                dataPacket.writeShort(dataPacket.getDarkenScreen());
            case UPDATE_STYLE:
                dataPacket.writeUnsignedVarInt(dataPacket.getColor());
                dataPacket.writeUnsignedVarInt(dataPacket.getOverlay());
                break;
            case PLAYER_ADDED:
            case PLAYER_REMOVED:
                dataPacket.writeSignedVarLong(dataPacket.getPlayerEntityId());
                break;
            case REMOVE:
                break;
            case UPDATE_PERCENT:
                dataPacket.writeLFloat(dataPacket.getHealthPercent());
                break;
            case UPDATE_NAME:
                dataPacket.writeString(dataPacket.getBossName());
                break;
        }
    }
}
