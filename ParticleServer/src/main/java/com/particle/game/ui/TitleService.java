package com.particle.game.ui;

import com.particle.api.ui.TitleServiceAPI;
import com.particle.game.entity.attribute.identified.EntityNameService;
import com.particle.game.ui.task.ActionBarTask;
import com.particle.game.ui.task.UITaskHandler;
import com.particle.game.world.level.LevelService;
import com.particle.model.level.Level;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.network.packets.data.SetTitlePacket;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;
import com.particle.util.placeholder.CompiledModel;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Set;

@Singleton
public class TitleService implements TitleServiceAPI {

    @Inject
    private NetworkManager networkManager;

    @Inject
    private LevelService levelService;

    @Inject
    private EntityNameService entityNameService;

    @Inject
    private UITaskHandler uiTaskHandler;

    /**
     * 清理title消息
     *
     * @param player
     */
    @Override
    public void clearTitle(Player player) {
        SetTitlePacket setTitlePacket = new SetTitlePacket();
        setTitlePacket.setTitleType(SetTitlePacket.CLEAR);
        this.networkManager.sendMessage(player.getClientAddress(), setTitlePacket);
    }

    /**
     * 清理title消息
     *
     * @param level
     */
    @Override
    public void clearTitle(Level level) {
        Set<Player> players = this.levelService.getPlayers(level);
        SetTitlePacket setTitlePacket = new SetTitlePacket();
        setTitlePacket.setTitleType(SetTitlePacket.CLEAR);
        ArrayList<InetSocketAddress> sendPlayers = new ArrayList<>();
        for (Player player : players) {
            sendPlayers.add(player.getClientAddress());
        }
        this.networkManager.broadcastMessage(sendPlayers, setTitlePacket);
    }

    /**
     * reset title
     *
     * @param player
     */
    @Override
    public void resetTitle(Player player) {
        SetTitlePacket setTitlePacket = new SetTitlePacket();
        setTitlePacket.setTitleType(SetTitlePacket.RESET);
        this.networkManager.sendMessage(player.getClientAddress(), setTitlePacket);
    }

    /**
     * @param level
     */
    @Override
    public void resetTitle(Level level) {
        Set<Player> players = this.levelService.getPlayers(level);
        SetTitlePacket setTitlePacket = new SetTitlePacket();
        setTitlePacket.setTitleType(SetTitlePacket.RESET);
        ArrayList<InetSocketAddress> sendPlayers = new ArrayList<>();
        for (Player player : players) {
            sendPlayers.add(player.getClientAddress());
        }
        this.networkManager.broadcastMessage(sendPlayers, setTitlePacket);
    }

    /**
     * @param player
     * @param title
     */
    @Override
    public void setTitle(Player player, String title) {
        this.setTitle(player, title, 10, 75, 20);
    }

    @Override
    public void setTitle(Player player, String title, int fadeIn, int stay, int fadeOut) {
        SetTitlePacket setTitlePacket = new SetTitlePacket();
        setTitlePacket.setFadeInTime(fadeIn);
        setTitlePacket.setStayTime(stay);
        setTitlePacket.setFadeOutTime(fadeOut);

        setTitlePacket.setTitleType(SetTitlePacket.TIMES);
        this.networkManager.sendMessage(player.getClientAddress(), setTitlePacket);

        setTitlePacket = new SetTitlePacket();
        setTitlePacket.setTitleType(SetTitlePacket.TITLE);
        title = CompiledModel.quickCompile(title, this.entityNameService.getEntityName(player));
        setTitlePacket.setTitleText(title);
        setTitlePacket.setFadeInTime(fadeIn);
        setTitlePacket.setStayTime(stay);
        setTitlePacket.setFadeOutTime(fadeOut);
        this.networkManager.sendMessage(player.getClientAddress(), setTitlePacket);
    }

    /**
     * @param level
     * @param title
     */
    @Override
    public void setTitle(Level level, String title) {
        this.setTitle(level, title, 10, 75, 20);
    }

    @Override
    public void setTitle(Level level, String title, int fadeIn, int stay, int fadeOut) {
        Set<Player> players = this.levelService.getPlayers(level);
        for (Player player : players) {
            setTitle(player, title, fadeIn, stay, fadeOut);
        }
    }

    /**
     * @param player
     * @param subTitle
     */
    @Override
    public void setSubTitle(Player player, String subTitle) {
        this.setSubTitle(player, subTitle, 10, 75, 20);
    }

    /**
     * @param level
     * @param subTitle
     */
    @Override
    public void setSubTitle(Level level, String subTitle) {
        this.setSubTitle(level, subTitle, 10, 75, 20);
    }

    /**
     * @param player
     * @param subTitle
     */
    @Override
    public void setSubTitle(Player player, String subTitle, int fadeIn, int stay, int fadeOut) {
        SetTitlePacket setTitlePacket = new SetTitlePacket();
        setTitlePacket.setFadeInTime(fadeIn);
        setTitlePacket.setStayTime(stay);
        setTitlePacket.setFadeOutTime(fadeOut);

        setTitlePacket.setTitleType(SetTitlePacket.TIMES);
        this.networkManager.sendMessage(player.getClientAddress(), setTitlePacket);

        setTitlePacket = new SetTitlePacket();
        setTitlePacket.setFadeInTime(fadeIn);
        setTitlePacket.setStayTime(stay);
        setTitlePacket.setFadeOutTime(fadeOut);
        // 先發 subTitle
        setTitlePacket.setTitleType(SetTitlePacket.SUBTITLE);
        subTitle = CompiledModel.quickCompile(subTitle, this.entityNameService.getEntityName(player));
        setTitlePacket.setTitleText(subTitle);
        this.networkManager.sendMessage(player.getClientAddress(), setTitlePacket);

        setTitlePacket = new SetTitlePacket();
        setTitlePacket.setFadeInTime(fadeIn);
        setTitlePacket.setStayTime(stay);
        setTitlePacket.setFadeOutTime(fadeOut);
        // 後發隱藏字符
        setTitlePacket.setTitleType(SetTitlePacket.TITLE);
        setTitlePacket.setTitleText("§r");
        this.networkManager.sendMessage(player.getClientAddress(), setTitlePacket);
    }

    /**
     * @param level
     * @param subTitle
     */
    @Override
    public void setSubTitle(Level level, String subTitle, int fadeIn, int stay, int fadeOut) {
        Set<Player> players = this.levelService.getPlayers(level);
        for (Player player : players) {
            setSubTitle(player, subTitle, fadeIn, stay, fadeOut);
        }
    }

    @Override
    public void setTitleAndSubTitle(Level level, String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        Set<Player> players = this.levelService.getPlayers(level);
        for (Player player : players) {
            setTitleAndSubTitle(player, title, subTitle, fadeIn, stay, fadeOut);
        }
    }

    @Override
    public void setTitleAndSubTitle(Player player, String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        SetTitlePacket setTitlePacket = new SetTitlePacket();
        setTitlePacket.setFadeInTime(fadeIn);
        setTitlePacket.setStayTime(stay);
        setTitlePacket.setFadeOutTime(fadeOut);

        setTitlePacket.setTitleType(SetTitlePacket.TIMES);
        this.networkManager.sendMessage(player.getClientAddress(), setTitlePacket);

        setTitlePacket = new SetTitlePacket();
        setTitlePacket.setFadeInTime(fadeIn);
        setTitlePacket.setStayTime(stay);
        setTitlePacket.setFadeOutTime(fadeOut);
        // 先發 subTitle
        setTitlePacket.setTitleType(SetTitlePacket.SUBTITLE);
        subTitle = CompiledModel.quickCompile(subTitle, this.entityNameService.getEntityName(player));
        setTitlePacket.setTitleText(subTitle);
        this.networkManager.sendMessage(player.getClientAddress(), setTitlePacket);

        setTitlePacket = new SetTitlePacket();
        setTitlePacket.setFadeInTime(fadeIn);
        setTitlePacket.setStayTime(stay);
        setTitlePacket.setFadeOutTime(fadeOut);
        // 後發隱藏字符
        setTitlePacket.setTitleType(SetTitlePacket.TITLE);
        setTitlePacket.setTitleText(title);
        this.networkManager.sendMessage(player.getClientAddress(), setTitlePacket);
    }

    /**
     * @param player
     * @param title
     */
    @Override
    public void setActionBar(Player player, String title) {
        SetTitlePacket setTitlePacket = new SetTitlePacket();
        setTitlePacket.setTitleType(SetTitlePacket.ACTIONBAR);
        title = CompiledModel.quickCompile(title, this.entityNameService.getEntityName(player));
        setTitlePacket.setTitleText(title);
        this.networkManager.sendMessage(player.getClientAddress(), setTitlePacket);
    }

    @Override
    public void setActionBar(Player player, String actionBar, int keep) {
        SetTitlePacket setTitlePacket = new SetTitlePacket();
        setTitlePacket.setTitleType(SetTitlePacket.ACTIONBAR);
        actionBar = CompiledModel.quickCompile(actionBar, this.entityNameService.getEntityName(player));
        setTitlePacket.setTitleText(actionBar);
        ActionBarTask actionBarTask = new ActionBarTask(this, player, setTitlePacket, keep);
        this.uiTaskHandler.addTaskQueue(actionBarTask);
    }

    /**
     * @param level
     * @param title
     */
    @Override
    public void setActionBar(Level level, String title) {
        Set<Player> players = this.levelService.getPlayers(level);
        for (Player player : players) {
            SetTitlePacket setTitlePacket = new SetTitlePacket();
            setTitlePacket.setTitleType(SetTitlePacket.ACTIONBAR);
            title = CompiledModel.quickCompile(title, this.entityNameService.getEntityName(player));
            setTitlePacket.setTitleText(title);
            this.networkManager.sendMessage(player.getClientAddress(), setTitlePacket);
        }
    }

    /**
     * @param title
     */
    @Override
    public SetTitlePacket getActionBarPacket(String title) {
        SetTitlePacket setTitlePacket = new SetTitlePacket();
        setTitlePacket.setTitleType(SetTitlePacket.ACTIONBAR);
        setTitlePacket.setTitleText(title);
        return setTitlePacket;
    }

    /**
     * @param player
     * @param fadeIn
     * @param stayTime
     * @param fadeOut
     */
    @Override
    public void setTimes(Player player, int fadeIn, int stayTime, int fadeOut) {
        SetTitlePacket setTitlePacket = new SetTitlePacket();
        setTitlePacket.setTitleType(SetTitlePacket.TIMES);
        setTitlePacket.setFadeInTime(fadeIn);
        setTitlePacket.setStayTime(stayTime);
        setTitlePacket.setFadeOutTime(fadeOut);
        this.networkManager.sendMessage(player.getClientAddress(), setTitlePacket);
    }

    /**
     * @param level
     * @param fadeIn
     * @param stayTime
     * @param fadeOut
     */
    @Override
    public void setTimes(Level level, int fadeIn, int stayTime, int fadeOut) {
        SetTitlePacket setTitlePacket = new SetTitlePacket();
        setTitlePacket.setTitleType(SetTitlePacket.TIMES);
        setTitlePacket.setFadeInTime(fadeIn);
        setTitlePacket.setStayTime(stayTime);
        setTitlePacket.setFadeOutTime(fadeOut);
        Set<Player> players = this.levelService.getPlayers(level);
        ArrayList<InetSocketAddress> sendPlayers = new ArrayList<>();
        for (Player player : players) {
            sendPlayers.add(player.getClientAddress());
        }
        this.networkManager.broadcastMessage(sendPlayers, setTitlePacket);
    }

    /**
     * @param player
     * @param setTitlePacket
     */
    public void sendTitlePacket(Player player, DataPacket setTitlePacket) {
        this.networkManager.sendMessage(player.getClientAddress(), setTitlePacket);
    }
}
