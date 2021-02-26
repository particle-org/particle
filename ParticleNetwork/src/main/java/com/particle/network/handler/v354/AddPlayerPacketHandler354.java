package com.particle.network.handler.v354;

import com.particle.model.entity.link.EntityLink;
import com.particle.model.network.packets.data.AddPlayerPacket;
import com.particle.model.player.settings.AdventureSettingsPermissionFlags;
import com.particle.model.player.settings.CommandPermissionLevel;
import com.particle.model.player.settings.PlayerPermissionLevel;
import com.particle.network.encoder.*;
import com.particle.network.handler.AbstractPacketHandler;

import java.util.List;

public class AddPlayerPacketHandler354 extends AbstractPacketHandler<AddPlayerPacket> {

    private UUIDEncoder uuidEncoder = UUIDEncoder.getInstance();

    private ItemStackEncoder itemStackEncoder = ItemStackEncoder.getInstance();

    private EntityMetaDataEncoderV354 entityMetaDataEncoder = EntityMetaDataEncoderV354.getInstance();

    private PositionFEncoder positionFEncoder = PositionFEncoder.getInstance();

    private DirectionFEncoder directionFEncoder = DirectionFEncoder.getInstance();

    private EntityLinkEncoder entityLinkEncoder = EntityLinkEncoder.getInstance();

    @Override
    protected void doDecode(AddPlayerPacket dataPacket, int version) {
        dataPacket.setUuid(uuidEncoder.decode(dataPacket, version));
        dataPacket.setUserName(dataPacket.readString());

        dataPacket.setEntityUniqueId(dataPacket.readSignedVarLong().longValue());
        dataPacket.setEntityRuntimeId(dataPacket.readUnsignedVarLong());
        dataPacket.setPlatformChatId(dataPacket.readString());
        dataPacket.setPosition(positionFEncoder.decode(dataPacket, version));
        dataPacket.setSpeedX(dataPacket.readLFloat());
        dataPacket.setSpeedY(dataPacket.readLFloat());
        dataPacket.setSpeedZ(dataPacket.readLFloat());
        dataPacket.setDirection(directionFEncoder.decode(dataPacket, version));
        dataPacket.setItem(itemStackEncoder.decode(dataPacket, version));
        dataPacket.setMetadata(entityMetaDataEncoder.decode(dataPacket, version));

        dataPacket.setFlags(dataPacket.readUnsignedVarInt());
        dataPacket.setUserCommandPermissions(CommandPermissionLevel.valueOf(dataPacket.readUnsignedVarInt()));
        dataPacket.setPermissionsFlags(AdventureSettingsPermissionFlags.valueOf(dataPacket.readUnsignedVarInt()));
        dataPacket.setPlayerPermissions(PlayerPermissionLevel.valueOf(dataPacket.readUnsignedVarInt()));
        dataPacket.setStoredCustomAbilities(PlayerPermissionLevel.valueOf(dataPacket.readUnsignedVarInt()));

        dataPacket.setPlayerUniqueId(dataPacket.readLLong());

        int size = dataPacket.readUnsignedVarInt();
        List<EntityLink> links = dataPacket.getEntityLinks();
        for (int i = 0; i < size; i++) {
            links.add(entityLinkEncoder.decode(dataPacket, version));
        }

        // deviceId
        dataPacket.setDeviceId(dataPacket.readString());
    }

    @Override
    protected void doEncode(AddPlayerPacket dataPacket, int version) {
        uuidEncoder.encode(dataPacket, dataPacket.getUuid(), version);
        dataPacket.writeString(dataPacket.getUserName());

        dataPacket.writeSignedVarLong(dataPacket.getEntityUniqueId());
        dataPacket.writeUnsignedVarLong(dataPacket.getEntityRuntimeId());
        dataPacket.writeString(dataPacket.getPlatformChatId());
        positionFEncoder.encode(dataPacket, dataPacket.getPosition(), version);
        dataPacket.writeLFloat(dataPacket.getSpeedX());
        dataPacket.writeLFloat(dataPacket.getSpeedY());
        dataPacket.writeLFloat(dataPacket.getSpeedZ());
        directionFEncoder.encode(dataPacket, dataPacket.getDirection(), version);
        itemStackEncoder.encode(dataPacket, dataPacket.getItem(), version);
        entityMetaDataEncoder.encode(dataPacket, dataPacket.getMetadata(), version);

        dataPacket.writeUnsignedVarInt(dataPacket.getFlags());
        dataPacket.writeUnsignedVarInt(dataPacket.getUserCommandPermissions().getLevel());
        dataPacket.writeUnsignedVarInt(dataPacket.getPermissionsFlags().getMode());
        dataPacket.writeUnsignedVarInt(dataPacket.getPlayerPermissions().getLevel());
        dataPacket.writeUnsignedVarInt(dataPacket.getStoredCustomAbilities().getLevel());
        dataPacket.writeLLong(dataPacket.getPlayerUniqueId());

        // entitylink
        dataPacket.writeUnsignedVarInt(dataPacket.getEntityLinks().size());
        for (EntityLink link : dataPacket.getEntityLinks()) {
            this.entityLinkEncoder.encode(dataPacket, link, version);
        }

        // deviceId
        dataPacket.writeString("");
    }
}
