package com.particle.model.network.packets.data;

import com.particle.model.entity.link.EntityLink;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.metadata.type.EntityData;
import com.particle.model.item.ItemStack;
import com.particle.model.math.Direction;
import com.particle.model.math.Vector3f;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.player.settings.AdventureSettingsFlags;
import com.particle.model.player.settings.AdventureSettingsPermissionFlags;
import com.particle.model.player.settings.CommandPermissionLevel;
import com.particle.model.player.settings.PlayerPermissionLevel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class AddPlayerPacket extends DataPacket {
    private UUID uuid;
    private String userName;
    private String thirdPartyName;
    private int platformId;
    private long entityUniqueId;
    private long entityRuntimeId;

    private String platformChatId;

    private Vector3f position;

    private float speedX;
    private float speedY;
    private float speedZ;

    private Direction direction;
    private ItemStack item;
    private Map<EntityMetadataType, EntityData> metadata;

    // 默认flag
    private int flags =
            AdventureSettingsFlags.AutoJump.getMode() | AdventureSettingsFlags.PlayerWorldBuilder.getMode();

    private CommandPermissionLevel userCommandPermissions = CommandPermissionLevel.Any;

    private AdventureSettingsPermissionFlags permissionsFlags = AdventureSettingsPermissionFlags.DefaultLevelPermissions;

    private PlayerPermissionLevel playerPermissions = PlayerPermissionLevel.Member;

    private PlayerPermissionLevel storedCustomAbilities = PlayerPermissionLevel.Member;

    private long playerUniqueId;

    private List<EntityLink> entityLinks = new ArrayList<>();

    private String deviceId = "";

    private int buildPlatformId = 1;

    @Override
    public int pid() {
        return ProtocolInfo.ADD_PLAYER_PACKET;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getThirdPartyName() {
        return thirdPartyName;
    }

    public void setThirdPartyName(String thirdPartyName) {
        this.thirdPartyName = thirdPartyName;
    }

    public int getPlatformId() {
        return platformId;
    }

    public void setPlatformId(int platformId) {
        this.platformId = platformId;
    }

    public String getPlatformChatId() {
        return platformChatId;
    }

    public void setPlatformChatId(String platformChatId) {
        this.platformChatId = platformChatId;
    }

    public long getEntityUniqueId() {
        return entityUniqueId;
    }

    public void setEntityUniqueId(long entityUniqueId) {
        this.entityUniqueId = entityUniqueId;
    }

    public long getEntityRuntimeId() {
        return entityRuntimeId;
    }

    public void setEntityRuntimeId(long entityRuntimeId) {
        this.entityRuntimeId = entityRuntimeId;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public float getSpeedX() {
        return speedX;
    }

    public void setSpeedX(float speedX) {
        this.speedX = speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void setSpeedY(float speedY) {
        this.speedY = speedY;
    }

    public float getSpeedZ() {
        return speedZ;
    }

    public void setSpeedZ(float speedZ) {
        this.speedZ = speedZ;
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public Map<EntityMetadataType, EntityData> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<EntityMetadataType, EntityData> metadata) {
        this.metadata = metadata;
    }

    public int getFlags() {
        return flags;
    }

    public void setFlags(int flags) {
        this.flags = flags;
    }

    public CommandPermissionLevel getUserCommandPermissions() {
        return userCommandPermissions;
    }

    public void setUserCommandPermissions(CommandPermissionLevel userCommandPermissions) {
        this.userCommandPermissions = userCommandPermissions;
    }

    public AdventureSettingsPermissionFlags getPermissionsFlags() {
        return permissionsFlags;
    }

    public void setPermissionsFlags(AdventureSettingsPermissionFlags permissionsFlags) {
        this.permissionsFlags = permissionsFlags;
    }

    public PlayerPermissionLevel getPlayerPermissions() {
        return playerPermissions;
    }

    public void setPlayerPermissions(PlayerPermissionLevel playerPermissions) {
        this.playerPermissions = playerPermissions;
    }

    public PlayerPermissionLevel getStoredCustomAbilities() {
        return storedCustomAbilities;
    }

    public void setStoredCustomAbilities(PlayerPermissionLevel storedCustomAbilities) {
        this.storedCustomAbilities = storedCustomAbilities;
    }

    public long getPlayerUniqueId() {
        return playerUniqueId;
    }

    public void setPlayerUniqueId(long playerUniqueId) {
        this.playerUniqueId = playerUniqueId;
    }

    public List<EntityLink> getEntityLinks() {
        return entityLinks;
    }

    public void setEntityLinks(List<EntityLink> entityLinks) {
        this.entityLinks = entityLinks;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getBuildPlatformId() {
        return buildPlatformId;
    }

    public void setBuildPlatformId(int buildPlatformId) {
        this.buildPlatformId = buildPlatformId;
    }
}
