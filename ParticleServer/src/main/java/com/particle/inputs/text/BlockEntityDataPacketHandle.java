package com.particle.inputs.text;

import com.particle.api.utils.IOnlineMessageCheck;
import com.particle.core.ecs.module.ECSModuleHandler;
import com.particle.executor.service.AsyncScheduleService;
import com.particle.game.block.common.modules.BlockTextModule;
import com.particle.game.block.tile.TileEntityService;
import com.particle.game.utils.OnlineMessageCheck;
import com.particle.game.utils.config.ServerConfigService;
import com.particle.game.utils.logger.PlayerLogService;
import com.particle.inputs.PlayerPacketHandle;
import com.particle.model.entity.model.tile.TileEntity;
import com.particle.model.math.Vector3;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.BlockEntityDataPacket;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.AbstractMap;

@Singleton
public class BlockEntityDataPacketHandle extends PlayerPacketHandle<BlockEntityDataPacket> {

    private static final Logger logger = LoggerFactory.getLogger(BlockEntityDataPacketHandle.class);

    private static final ECSModuleHandler<BlockTextModule> BLOCK_TEXT_MODULE_HANDLER = ECSModuleHandler.buildHandler(BlockTextModule.class);

    @Inject
    private TileEntityService tileEntityService;

    @Inject
    private OnlineMessageCheck onlineMessageCheck;

    @Inject
    private PlayerLogService playerLogService;

    @Inject
    private ServerConfigService serverConfigService;

    @Override
    protected void handlePacket(Player player, BlockEntityDataPacket packet) {
        if (!player.isSpawned()) {
            logger.warn("当玩家尚未加入世界或者非生存状态时候，不允许操作");
            return;
        }
        logger.debug("--text:" + packet.toString());

        // 若關閉告示牌功能
        if (!serverConfigService.isEnableSign()) {
            return;
        }

        TileEntity tileEntity = tileEntityService.getEntityAt(player.getLevel(), new Vector3(packet.getX(), packet.getY(), packet.getZ()));
        if (tileEntity != null) {
            AsyncScheduleService.getInstance().getThread().scheduleSimpleTask("check_sign_text_task", () ->
            {
                String str = packet.getNbtTagCompound().getString("Text");

                OnlineMessageCheck.CheckResult checkResult = onlineMessageCheck.checkMessageSync(str);

                player.getLevel().getLevelSchedule().scheduleSimpleTask("sysn_check_sign_text_task", () ->
                {
                    BlockTextModule blockTextModule = BLOCK_TEXT_MODULE_HANDLER.bindModule(tileEntity);

                    AbstractMap.SimpleEntry<String, Object> channelEntry = new AbstractMap.SimpleEntry<>("channel", "告示牌");
                    AbstractMap.SimpleEntry<String, Object> shieldEntry = new AbstractMap.SimpleEntry<>("shield", false);

                    if (checkResult == IOnlineMessageCheck.CheckResult.PASS || checkResult == IOnlineMessageCheck.CheckResult.HIDE) {
                        blockTextModule.setText(str);
                    } else {
                        blockTextModule.setText("***");
                        shieldEntry = new AbstractMap.SimpleEntry<>("shield", true);
                    }

                    playerLogService.log(player,
                            "mcnetgame_chat_xjjy",
                            new AbstractMap.SimpleEntry<>("chat", str),
                            channelEntry,
                            shieldEntry,
                            new AbstractMap.SimpleEntry<>("d_role_uuid", ""),
                            new AbstractMap.SimpleEntry<>("horn", "无")
                    );
                });
            });
        }
    }

    @Override
    public int getTargetPacketID() {
        return ProtocolInfo.BLOCK_ENTITY_DATA_PACKET;
    }
}
