package com.particle.game.ui;

import com.particle.api.ui.IPromptServiceApi;
import com.particle.executor.service.NodeScheduleService;
import com.particle.model.entity.EntityType;
import com.particle.model.entity.metadata.EntityMetadataType;
import com.particle.model.entity.metadata.type.*;
import com.particle.model.entity.model.mob.MobEntity;
import com.particle.model.math.Direction;
import com.particle.model.math.Vector3f;
import com.particle.model.network.NetworkService;
import com.particle.model.network.packets.data.AddEntityPacket;
import com.particle.model.network.packets.data.RemoveEntityPacket;
import com.particle.model.player.Player;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

/**
 * PromptService 是一个基于Holograph原理实现的提示服务，用于给世界中的地点进行标注
 * 与直接使用Holograph不同，PromptService支持以虚拟Entity的方式给客户端发送提示，以降低服务端的Tick压力。
 */
@Singleton
public class PromptService implements IPromptServiceApi {

    private static final Direction DIRECTION_ZERO = new Direction(0, 0, 0);
    private static final LongEntityData FLAG = new LongEntityData(0);
    private static final FloatEntityData SCALE = new FloatEntityData(0.001f);
    private static final FloatEntityData BOUNDING_BOX_WIDTH = new FloatEntityData(0.001f);
    private static final FloatEntityData BOUNDING_BOX_HEIGHT = new FloatEntityData(0.001f);
    private static final ByteEntityData NAMETAG_ALWAYS_SHOW = new ByteEntityData((byte) 1);

    @Inject
    private NetworkService networkService;

    /**
     * 给指定玩家发送提示
     * 改模式下服务端不会生成真的Entity，提示的显示控制也不会接入AOI
     *
     * @param player
     * @param position
     * @param ttl      提示存在事件
     * @param content
     */
    @Override
    public void prompt(Player player, Vector3f position, long ttl, String... content) {
        AddEntityPacket addEntityPacket = new AddEntityPacket();

        addEntityPacket.setActorType(EntityType.ARMOR_STAND.actorType());

        long runtimeId = MobEntity.requestId();
        addEntityPacket.setEntityUniqueId(runtimeId);
        addEntityPacket.setEntityRuntimeId(runtimeId);

        addEntityPacket.setPosition(position);
        addEntityPacket.setDirection(DIRECTION_ZERO);

        addEntityPacket.setSpeedX(0);
        addEntityPacket.setSpeedY(0);
        addEntityPacket.setSpeedZ(0);

        Map<EntityMetadataType, EntityData> metadata = new HashMap<>();
        metadata.put(EntityMetadataType.FLAGS, FLAG);
        metadata.put(EntityMetadataType.SCALE, SCALE);
        metadata.put(EntityMetadataType.NAMETAG, new StringEntityData(this.connect(content)));
        metadata.put(EntityMetadataType.BOUNDING_BOX_WIDTH, BOUNDING_BOX_WIDTH);
        metadata.put(EntityMetadataType.BOUNDING_BOX_HEIGHT, BOUNDING_BOX_HEIGHT);
        metadata.put(EntityMetadataType.NAMETAG_ALWAYS_SHOW, NAMETAG_ALWAYS_SHOW);
        addEntityPacket.setMetadata(metadata);

        this.networkService.sendMessage(player.getClientAddress(), addEntityPacket);

        NodeScheduleService.getInstance().getThread(player.getIdentifiedId()).scheduleDelayTask("PlayerPromotRemove", () -> {
            RemoveEntityPacket removeEntityPacket = new RemoveEntityPacket();
            removeEntityPacket.setEntityUniqueId(runtimeId);
            networkService.sendMessage(player.getClientAddress(), removeEntityPacket);
        }, ttl);
    }

    private String connect(String[] content) {
        if (content.length == 0) {
            return "";
        } else if (content.length == 1) {
            return content[0];
        } else {
            StringBuilder stringBuilder = new StringBuilder(content[0]);
            for (int i = 1; i < content.length; i++) {
                stringBuilder.append("\n");
                stringBuilder.append(content[i]);
            }

            return stringBuilder.toString();
        }
    }
}
