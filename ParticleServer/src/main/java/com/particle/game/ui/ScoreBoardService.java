package com.particle.game.ui;

import com.particle.api.ui.ScoreBoardServiceAPI;
import com.particle.game.world.level.LevelService;
import com.particle.model.level.Level;
import com.particle.model.network.packets.data.RemoveObjectivePacket;
import com.particle.model.network.packets.data.SetDisplayObjecivePacket;
import com.particle.model.network.packets.data.SetScorePacket;
import com.particle.model.player.Player;
import com.particle.model.ui.score.ScoreObjective;
import com.particle.model.ui.score.ScorePacketInfo;
import com.particle.model.ui.score.ScorePacketType;
import com.particle.model.ui.score.ScoreboardPosition;
import com.particle.network.NetworkManager;
import com.particle.util.font.FontUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Singleton
public class ScoreBoardService implements ScoreBoardServiceAPI {

    private static final Logger logger = LoggerFactory.getLogger(ScoreBoardService.class);

    private static final int MIN_DISPLAY_LEN = 200;

    @Inject
    private NetworkManager networkManager;

    @Inject
    private LevelService levelService;

    /**
     * 单个玩家显示他所在世界的计分板
     * 显示计分板
     * 玩家登录后会被调用
     *
     * @param player
     */
    @Override
    public void show(Player player) {
        Level level = player.getLevel();
        if (!level.isScoreboardDisplay()) {
            return;
        }
        SetDisplayObjecivePacket setDisplayObjecivePacket = new SetDisplayObjecivePacket();
        setDisplayObjecivePacket.setScoreObjective(level.getScoreObjective());
        this.networkManager.sendMessage(player.getClientAddress(), setDisplayObjecivePacket);

        this.onAddScore(level, level.getAllScorePacketInfos());
    }

    /**
     * 该世界中的所有玩家
     * 显示计分板
     *
     * @param level
     */
    @Override
    public void show(Level level) {
        level.setScoreboardDisplay(true);
        Set<Player> players = this.levelService.getPlayers(level);
        for (Player player : players) {
            this.show(player);
        }
    }

    /**
     * 关闭单个玩家的计分板
     * 玩家切换世界的时候会被调用
     */
    @Override
    public void close(Player player) {
        Level level = player.getLevel();
        if (!level.isScoreboardDisplay()) {
            return;
        }
        ScoreObjective scoreObjective = level.getScoreObjective();
        this.onRemoveObjective(player, scoreObjective.getObjectiveName());
    }

    /**
     * 关闭世界的计分板
     *
     * @param level
     */
    @Override
    public void close(Level level) {
        if (!level.isScoreboardDisplay()) {
            return;
        }
        Set<Player> players = this.levelService.getPlayers(level);
        for (Player player : players) {
            this.close(player);
        }
        level.setScoreboardDisplay(false);
    }

    /**
     * 外部接口
     * 用来设置世界的计分板
     *
     * @param level
     * @param displaySlot
     * @param objective
     * @param objectiveDisplay
     */
    @Override
    public void setDisplayObjective(Level level, ScoreboardPosition displaySlot, String objective, String objectiveDisplay) {
        ScoreObjective scoreObjective = level.getScoreObjective();
        scoreObjective.setDisplaySlotName(displaySlot.getPosition());
        scoreObjective.setObjectiveName(objective);
        scoreObjective.setObjectiveDisplayName(objectiveDisplay);
    }

    /**
     * @param level
     * @param infos
     */
    @Override
    public void changeScore(Level level, List<ScorePacketInfo> infos) {
        this.adjustLength(level.getScoreObjective(), infos);
        if (level.isScoreboardDisplay()) {
            // 必须先删除已发下的计分
            this.onRemoveScore(level, level.getAllScorePacketInfos());
            // 重新下发积分项
            this.onAddScore(level, infos);
        }
        level.setAllScorePacketInfos(infos);
    }

    /**
     * 内部接口，发包，去除计分板
     *
     * @param player
     * @param objectiveName
     */
    private void onRemoveObjective(Player player, String objectiveName) {
        RemoveObjectivePacket removeObjectivePacket = new RemoveObjectivePacket();
        removeObjectivePacket.setObjectiveName(objectiveName);
        this.networkManager.sendMessage(player.getClientAddress(), removeObjectivePacket);
    }

    /**
     * 通知客户端增加记分项
     *
     * @param level
     */
    private void onAddScore(Level level, List<ScorePacketInfo> allInfos) {
        if (allInfos.isEmpty()) {
            return;
        }
        SetScorePacket setScorePacket = new SetScorePacket();
        setScorePacket.setScorePacketType(ScorePacketType.Change);
        setScorePacket.setScorePacketInfos(allInfos);
        Set<Player> players = this.levelService.getPlayers(level);
        ArrayList<InetSocketAddress> sendPlayers = new ArrayList<>();
        for (Player player : players) {
            sendPlayers.add(player.getClientAddress());
        }
        this.networkManager.broadcastMessage(sendPlayers, setScorePacket);
    }

    /**
     * 通知客户端删除计分项
     *
     * @param level
     * @param allInfos
     */
    private void onRemoveScore(Level level, List<ScorePacketInfo> allInfos) {
        if (allInfos.isEmpty()) {
            return;
        }
        SetScorePacket setScorePacket = new SetScorePacket();
        setScorePacket.setScorePacketType(ScorePacketType.Remove);
        setScorePacket.setScorePacketInfos(allInfos);
        Set<Player> players = this.levelService.getPlayers(level);
        ArrayList<InetSocketAddress> sendPlayers = new ArrayList<>();
        for (Player player : players) {
            sendPlayers.add(player.getClientAddress());
        }
        this.networkManager.broadcastMessage(sendPlayers, setScorePacket);
    }

    /**
     * 调整长度
     *
     * @param scoreObjective
     * @param infos
     */
    private void adjustLength(ScoreObjective scoreObjective, List<ScorePacketInfo> infos) {
        if (infos.isEmpty()) {
            return;
        }
        ScorePacketInfo scorePacketInfo = null;
        int maxSize = 0;
        for (ScorePacketInfo score : infos) {
            score.setFakePlayerName(score.getFakePlayerName().trim());
            int size = FontUtils.getScoreboardDisplayWidth(score.getFakePlayerName());
            if (size > maxSize) {
                maxSize = size;
                scorePacketInfo = score;
            }
        }

        if (maxSize < MIN_DISPLAY_LEN) {
            int INDEX = FontUtils.getScoreboardDisplayWidth(" ");
            int spaceSize = (MIN_DISPLAY_LEN - maxSize) / INDEX + 1;
            StringBuilder sb = new StringBuilder();
            sb.append(scorePacketInfo.getFakePlayerName());
            for (int space = 0; space < spaceSize; space++) {
                sb.append(" ");
            }
            scorePacketInfo.setFakePlayerName(sb.toString());
        } else {
            scorePacketInfo.setFakePlayerName(String.format("%s%s", scorePacketInfo.getFakePlayerName(), "   "));
        }
    }

}
