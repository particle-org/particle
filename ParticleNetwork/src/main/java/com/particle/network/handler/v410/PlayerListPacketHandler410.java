package com.particle.network.handler.v410;

import com.particle.model.entity.model.skin.PlayerSkinAnimationData;
import com.particle.model.network.packets.data.PersonaPiece;
import com.particle.model.network.packets.data.PersonaPieceTint;
import com.particle.model.network.packets.data.PlayerListPacket;
import com.particle.network.encoder.UUIDEncoder;
import com.particle.network.handler.AbstractPacketHandler;

import java.util.ArrayList;
import java.util.List;

public class PlayerListPacketHandler410 extends AbstractPacketHandler<PlayerListPacket> {

    private UUIDEncoder uuidEncoder = UUIDEncoder.getInstance();

    @Override
    protected void doDecode(PlayerListPacket dataPacket, int version) {
        dataPacket.setAction(dataPacket.readByte() == 0 ? PlayerListPacket.PlayerListAction.ADD : PlayerListPacket.PlayerListAction.REMOVE);

        int length = dataPacket.readUnsignedVarInt();
        for (int i = 0; i < length; i++) {
            PlayerListPacket.PlayerListEntry playerListEntry = new PlayerListPacket.PlayerListEntry();
            playerListEntry.setEntityUUID(this.uuidEncoder.decode(dataPacket, version));

            if (dataPacket.getAction() == PlayerListPacket.PlayerListAction.ADD) {
                playerListEntry.setEntityId(dataPacket.readSignedVarInt());
                playerListEntry.setEntityName(dataPacket.readString());
                playerListEntry.setReserved(dataPacket.readString());
                playerListEntry.setChatId(dataPacket.readString());
                playerListEntry.setBuildPlatform(dataPacket.readLInt());

                playerListEntry.setSkinId(dataPacket.readString());
                playerListEntry.setSkinResourcePatch(dataPacket.readString());
                playerListEntry.setSkinWidth(dataPacket.readLInt());
                playerListEntry.setSkinHeight(dataPacket.readLInt());
                playerListEntry.setSkinData(dataPacket.readBytesWithLength());
                int skinAnimationsSize = dataPacket.readLInt();
                PlayerSkinAnimationData[] playerSkinAnimationData = new PlayerSkinAnimationData[skinAnimationsSize];
                for (int j = 0; j < skinAnimationsSize; j++) {
                    PlayerSkinAnimationData playerSkinAnimationDatum = new PlayerSkinAnimationData();
                    playerSkinAnimationDatum.setWidth(dataPacket.readLInt());
                    playerSkinAnimationDatum.setHeight(dataPacket.readLInt());
                    playerSkinAnimationDatum.setImages(dataPacket.readBytesWithLength());
                    playerSkinAnimationDatum.setAnimationType(dataPacket.readLInt());
                    playerSkinAnimationDatum.setFrameCount(dataPacket.readLFloat());
                    playerSkinAnimationData[i] = playerSkinAnimationDatum;
                }
                playerListEntry.setPlayerSkinAnimationData(playerSkinAnimationData);
                playerListEntry.setCapeWidth(dataPacket.readLInt());
                playerListEntry.setCapeHeight(dataPacket.readLInt());
                playerListEntry.setCapeData(dataPacket.readBytesWithLength());
                playerListEntry.setSkinGeometry(dataPacket.readString());

                playerListEntry.setTeacher(dataPacket.readBoolean());
                playerListEntry.setHost(dataPacket.readBoolean());
                playerListEntry.setNewVersion(true);

                playerListEntry.setSerializedAnimationData(dataPacket.readString());
                playerListEntry.setPremiumSkin(dataPacket.readBoolean());
                playerListEntry.setPersonSkin(dataPacket.readBoolean());
                playerListEntry.setPersonCapeOnClassicSkin(dataPacket.readBoolean());
                playerListEntry.setCapeId(dataPacket.readString());
                dataPacket.readString();

                // 1.16
                playerListEntry.setArmSize(dataPacket.readString());
                playerListEntry.setSkinColor(dataPacket.readString());
                int piecesLength = dataPacket.readLInt();
                for (int j = 0; j < piecesLength; j++) {
                    String pieceId = dataPacket.readString();
                    String pieceType = dataPacket.readString();
                    String packId = dataPacket.readString();
                    boolean isDefault = dataPacket.readBoolean();
                    String productId = dataPacket.readString();
                    playerListEntry.getPersonaPieces().add(new PersonaPiece(pieceId, pieceType, packId, isDefault, productId));
                }

                int tintsLength = dataPacket.readLInt();
                for (int j = 0; j < tintsLength; j++) {
                    String pieceType = dataPacket.readString();
                    List<String> colorList = new ArrayList<>();
                    int colorsLength = dataPacket.readLInt();
                    for (int k = 0; k < colorsLength; k++) {
                        colorList.add(dataPacket.readString());
                    }

                    playerListEntry.getTintColors().add(new PersonaPieceTint(pieceType, colorList));
                }
            }

            dataPacket.addPlayerListEntry(playerListEntry);
        }
    }

    @Override
    protected void doEncode(PlayerListPacket dataPacket, int version) {
        dataPacket.writeByte(dataPacket.getAction().getValue());

        List<PlayerListPacket.PlayerListEntry> entries = dataPacket.getEntries();
        dataPacket.writeUnsignedVarInt(entries.size());
        for (PlayerListPacket.PlayerListEntry entry : entries) {

            this.uuidEncoder.encode(dataPacket, entry.getEntityUUID(), version);

            if (dataPacket.getAction() == PlayerListPacket.PlayerListAction.ADD) {
                dataPacket.writeSignedVarLong(entry.getEntityId());
                dataPacket.writeString(entry.getEntityName());
                dataPacket.writeString(entry.getReserved());
                dataPacket.writeString(entry.getChatId());
                dataPacket.writeLInt(entry.getBuildPlatform());

                dataPacket.writeString(entry.getSkinId());
                dataPacket.writeString(entry.getSkinResourcePatch());
                dataPacket.writeLInt(entry.getSkinWidth());
                dataPacket.writeLInt(entry.getSkinHeight());
                dataPacket.writeBytesWithLength(entry.getSkinData());
                PlayerSkinAnimationData[] playerSkinAnimationData = entry.getPlayerSkinAnimationData();
                if (playerSkinAnimationData == null) {
                    dataPacket.writeLInt(0);
                } else {
                    dataPacket.writeLInt(playerSkinAnimationData.length);
                    for (PlayerSkinAnimationData playerSkinAnimationDatum : playerSkinAnimationData) {
                        dataPacket.writeLInt(playerSkinAnimationDatum.getWidth());
                        dataPacket.writeLInt(playerSkinAnimationDatum.getHeight());
                        dataPacket.writeBytesWithLength(playerSkinAnimationDatum.getImages());
                        dataPacket.writeLInt(playerSkinAnimationDatum.getAnimationType());
                        dataPacket.writeLFloat(playerSkinAnimationDatum.getFrameCount());
                    }
                }
                dataPacket.writeLInt(entry.getCapeWidth());
                dataPacket.writeLInt(entry.getCapeHeight());
                dataPacket.writeBytesWithLength(entry.getCapeData());
                dataPacket.writeString(entry.getSkinGeometry());

                dataPacket.writeString(entry.getSerializedAnimationData());
                dataPacket.writeBoolean(entry.isPremiumSkin());
                dataPacket.writeBoolean(entry.isPersonSkin());
                dataPacket.writeBoolean(entry.isPersonCapeOnClassicSkin());
                dataPacket.writeString(entry.getCapeId());
                dataPacket.writeString(entry.getSkinId());

                // 1.16
                dataPacket.writeString(entry.getArmSize());
                dataPacket.writeString(entry.getSkinColor());
                List<PersonaPiece> pieceList = entry.getPersonaPieces();
                dataPacket.writeLInt(pieceList.size());
                for (PersonaPiece personaPiece : pieceList) {
                    dataPacket.writeString(personaPiece.getId());
                    dataPacket.writeString(personaPiece.getType());
                    dataPacket.writeString(personaPiece.getPackId());
                    dataPacket.writeBoolean(personaPiece.isDefault());
                    dataPacket.writeString(personaPiece.getProductId());
                }
                List<PersonaPieceTint> personaPieceTintList = entry.getTintColors();
                dataPacket.writeLInt(personaPieceTintList.size());
                for (PersonaPieceTint personaPieceTint : personaPieceTintList) {
                    dataPacket.writeString(personaPieceTint.getPieceType());
                    List<String> colorList = personaPieceTint.getColors();
                    dataPacket.writeLInt(colorList.size());
                    for (String color : colorList) {
                        dataPacket.writeString(color);
                    }
                }

                dataPacket.writeBoolean(entry.isTeacher());
                dataPacket.writeBoolean(entry.isHost());
            }

        }

        // 1.16
        for (PlayerListPacket.PlayerListEntry entry : entries) {
            if (dataPacket.getAction() == PlayerListPacket.PlayerListAction.ADD) {
                dataPacket.writeBoolean(true);
            }
        }
    }

}
