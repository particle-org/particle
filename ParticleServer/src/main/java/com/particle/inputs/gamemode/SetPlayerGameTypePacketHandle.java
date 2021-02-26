package com.particle.inputs.gamemode;

import com.particle.game.player.PlayerService;
import com.particle.inputs.PlayerPacketHandle;
import com.particle.model.network.packets.ProtocolInfo;
import com.particle.model.network.packets.data.SetPlayerGameTypePacket;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SetPlayerGameTypePacketHandle extends PlayerPacketHandle<SetPlayerGameTypePacket> {

    private static final Logger logger = LoggerFactory.getLogger(SetPlayerGameTypePacketHandle.class);

    @Inject
    private PlayerService playerService;

    @Override
    protected void handlePacket(Player player, SetPlayerGameTypePacket packet) {

        /*

        因为没有权限管理，暂时屏蔽


        logger.info("SetPlayerGameTypePacket:{}", packet);
        int gameMode = packet.getGameMode();
        if (gameMode != this.playerService.getGameMode(player).getMode()) {
            // TODO 判断是否有权限更改,没有的话，切换回生存模式
            playerService.changePlayerGameMode(player, GameMode.valueOf(gameMode), true);
        }

         */
    }

    @Override
    public int getTargetPacketID() {
        return ProtocolInfo.SET_PLAYER_GAME_TYPE_PACKET;
    }
}
