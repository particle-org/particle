package com.particle.game.ui;

import com.particle.api.ui.TextServiceAPI;
import com.particle.game.entity.attribute.identified.EntityNameService;
import com.particle.game.server.Server;
import com.particle.game.ui.task.MessageTask;
import com.particle.game.ui.task.UITaskHandler;
import com.particle.game.world.level.LevelService;
import com.particle.model.level.Level;
import com.particle.model.network.packets.data.TextPacket;
import com.particle.model.player.Player;
import com.particle.network.NetworkManager;
import com.particle.util.placeholder.CompiledModel;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Collection;
import java.util.Set;

@Singleton
public class TextService implements TextServiceAPI {

    @Inject
    private NetworkManager networkManager;

    @Inject
    private LevelService levelService;

    @Inject
    private EntityNameService entityNameService;

    @Inject
    private Server server;

    @Inject
    private UITaskHandler uiTaskHandler;

    /**
     * @param player
     * @param textPacket
     */
    public void sendTextPacket(Player player, TextPacket textPacket) {
        this.networkManager.sendMessage(player.getClientAddress(), textPacket);
    }

    /**
     * 发送原始消息
     *
     * @param player
     * @param message
     */
    @Override
    public void sendRawMessage(Player player, String message) {
        TextPacket textPacket = new TextPacket();
        textPacket.setMessageType(TextPacket.RawType);
        message = CompiledModel.quickCompile(message, this.entityNameService.getEntityName(player));
        textPacket.setMessage(message);
        this.networkManager.sendMessage(player.getClientAddress(), textPacket);
    }

    /**
     * 给特定世界的玩家广播原始消息
     *
     * @param level
     * @param message
     */
    @Override
    public void broadcastRawMessage(Level level, String message) {
        Set<Player> players = this.levelService.getPlayers(level);
        for (Player player : players) {
            TextPacket textPacket = new TextPacket();
            textPacket.setMessageType(TextPacket.RawType);
            message = CompiledModel.quickCompile(message, this.entityNameService.getEntityName(player));
            textPacket.setMessage(message);
            this.networkManager.sendMessage(player.getClientAddress(), textPacket);
        }
    }

    /**
     * 给端内的所有玩家广播原始消息
     *
     * @param message
     */
    @Override
    public void broadcastRawMessage(String message) {
        Collection<Player> players = this.server.getAllPlayers();
        for (Player player : players) {
            TextPacket textPacket = new TextPacket();
            textPacket.setMessageType(TextPacket.RawType);
            message = CompiledModel.quickCompile(message, this.entityNameService.getEntityName(player));
            textPacket.setMessage(message);
            this.networkManager.sendMessage(player.getClientAddress(), textPacket);
        }
    }

    /**
     * 发送聊天消息
     *
     * @param player
     * @param message
     */
    @Override
    public void sendChatMessage(Player player, String message) {
        TextPacket textPacket = new TextPacket();
        textPacket.setMessageType(TextPacket.ChatType);
        textPacket.setPrimaryName("");
        textPacket.setMessage(message);
        this.networkManager.sendMessage(player.getClientAddress(), textPacket);
    }

    /**
     * 给特定世界的玩家广播聊天消息
     *
     * @param level
     * @param message
     */
    @Override
    public void broadcastChatMessage(Level level, String message) {
        Set<Player> players = this.levelService.getPlayers(level);
        for (Player player : players) {
            TextPacket textPacket = new TextPacket();
            textPacket.setMessageType(TextPacket.ChatType);
            textPacket.setPrimaryName("");
            message = CompiledModel.quickCompile(message, this.entityNameService.getEntityName(player));
            textPacket.setMessage(message);
            this.networkManager.sendMessage(player.getClientAddress(), textPacket);
        }
    }

    /**
     * 给端内的所有玩家广播聊天消息
     *
     * @param message
     */
    @Override
    public void broadcastChatMessage(String message) {
        Collection<Player> players = this.server.getAllPlayers();
        for (Player player : players) {
            TextPacket textPacket = new TextPacket();
            textPacket.setMessageType(TextPacket.ChatType);
            textPacket.setPrimaryName("");
            message = CompiledModel.quickCompile(message, this.entityNameService.getEntityName(player));
            textPacket.setMessage(message);
            this.networkManager.sendMessage(player.getClientAddress(), textPacket);
        }
    }

    /**
     * 发送翻译消息
     *
     * @param player
     * @param message
     */
    @Override
    public void sendTranslateMessage(Player player, String message) {
        TextPacket textPacket = new TextPacket();
        textPacket.setMessageType(TextPacket.TranslateType);
        message = CompiledModel.quickCompile(message, this.entityNameService.getEntityName(player));
        textPacket.setMessage(message);
        textPacket.setLocalize(true);
        this.networkManager.sendMessage(player.getClientAddress(), textPacket);
    }

    /**
     * 给特定世界的玩家广播翻译消息
     *
     * @param level
     * @param message
     */
    @Override
    public void broadcastTranslateMessage(Level level, String message) {
        Set<Player> players = this.levelService.getPlayers(level);
        for (Player player : players) {
            TextPacket textPacket = new TextPacket();
            textPacket.setMessageType(TextPacket.TranslateType);
            textPacket.setLocalize(true);
            message = CompiledModel.quickCompile(message, this.entityNameService.getEntityName(player));
            textPacket.setMessage(message);
            this.networkManager.sendMessage(player.getClientAddress(), textPacket);
        }
    }

    /**
     * 给端内的所有玩家广播翻译消息
     *
     * @param message
     */
    @Override
    public void broadcastTranslateMessage(String message) {

        Collection<Player> players = this.server.getAllPlayers();
        for (Player player : players) {
            TextPacket textPacket = new TextPacket();
            textPacket.setMessageType(TextPacket.TranslateType);
            message = CompiledModel.quickCompile(message, this.entityNameService.getEntityName(player));
            textPacket.setMessage(message);
            textPacket.setLocalize(true);
            this.networkManager.sendMessage(player.getClientAddress(), textPacket);
        }
    }

    /**
     * 发送popup消息
     *
     * @param player
     * @param message
     */
    @Override
    public void sendPopupMessage(Player player, String message) {
        this.sendPopupMessage(player, message, 0);
    }

    /**
     * 给特定世界的玩家广播popup消息
     *
     * @param level
     * @param message
     */
    @Override
    public void broadcastPopupMessage(Level level, String message) {
        this.broadcastPopupMessage(level, message, 0);
    }

    /**
     * 给端内的所有玩家广播popup消息
     *
     * @param message
     */
    @Override
    public void broadcastPopupMessage(String message) {
        this.broadcastPopupMessage(message, 0);
    }

    @Override
    public void sendPopupMessage(Player player, String message, int stayTick) {
        TextPacket textPacket = new TextPacket();
        textPacket.setMessageType(TextPacket.PopupType);
        message = CompiledModel.quickCompile(message, this.entityNameService.getEntityName(player));
        textPacket.setMessage(message);
        textPacket.setLocalize(true);
        if (stayTick > 20) {
            MessageTask messageTask = new MessageTask(this, player, textPacket, stayTick);
            uiTaskHandler.addTaskQueue(messageTask);
        } else {
            this.networkManager.sendMessage(player.getClientAddress(), textPacket);
        }
    }

    @Override
    public void broadcastPopupMessage(Level level, String message, int stayTick) {
        Set<Player> players = this.levelService.getPlayers(level);
        for (Player player : players) {
            TextPacket textPacket = new TextPacket();
            textPacket.setMessageType(TextPacket.PopupType);
            message = CompiledModel.quickCompile(message, this.entityNameService.getEntityName(player));
            textPacket.setMessage(message);
            textPacket.setLocalize(true);
            if (stayTick > 20) {
                MessageTask messageTask = new MessageTask(this, player, textPacket, stayTick);
                uiTaskHandler.addTaskQueue(messageTask);
            } else {
                this.networkManager.sendMessage(player.getClientAddress(), textPacket);
            }
        }
    }

    @Override
    public void broadcastPopupMessage(String message, int stayTick) {
        Collection<Player> players = this.server.getAllPlayers();
        for (Player player : players) {
            TextPacket textPacket = new TextPacket();
            textPacket.setMessageType(TextPacket.PopupType);
            message = CompiledModel.quickCompile(message, this.entityNameService.getEntityName(player));
            textPacket.setMessage(message);
            textPacket.setLocalize(true);
            if (stayTick > 20) {
                MessageTask messageTask = new MessageTask(this, player, textPacket, stayTick);
                uiTaskHandler.addTaskQueue(messageTask);
            } else {
                this.networkManager.sendMessage(player.getClientAddress(), textPacket);
            }
        }
    }

    /**
     * 发送JukeboxPopup消息
     *
     * @param player
     * @param message
     */
    @Override
    public void sendJukePopupMessage(Player player, String message) {
        TextPacket textPacket = new TextPacket();
        textPacket.setMessageType(TextPacket.JukeboxPopupType);
        message = CompiledModel.quickCompile(message, this.entityNameService.getEntityName(player));
        textPacket.setMessage(message);
        textPacket.setLocalize(true);
        this.networkManager.sendMessage(player.getClientAddress(), textPacket);
    }

    /**
     * 给特定世界的玩家广播JukeboxPopup消息
     *
     * @param level
     * @param message
     */
    @Override
    public void broadcastJukePopupMessage(Level level, String message) {
        Set<Player> players = this.levelService.getPlayers(level);
        for (Player player : players) {
            TextPacket textPacket = new TextPacket();
            textPacket.setMessageType(TextPacket.JukeboxPopupType);
            message = CompiledModel.quickCompile(message, this.entityNameService.getEntityName(player));
            textPacket.setMessage(message);
            textPacket.setLocalize(true);
            this.networkManager.sendMessage(player.getClientAddress(), textPacket);
        }
    }

    /**
     * 给端内的所有玩家广播JukeboxPopup消息
     *
     * @param message
     */
    @Override
    public void broadcastJukePopupMessage(String message) {
        Collection<Player> players = this.server.getAllPlayers();
        for (Player player : players) {
            TextPacket textPacket = new TextPacket();
            textPacket.setMessageType(TextPacket.JukeboxPopupType);
            message = CompiledModel.quickCompile(message, this.entityNameService.getEntityName(player));
            textPacket.setMessage(message);
            textPacket.setLocalize(true);
            this.networkManager.sendMessage(player.getClientAddress(), textPacket);
        }
    }

    /**
     * 发送Tip消息
     *
     * @param player
     * @param message
     */
    @Override
    public void sendTipMessage(Player player, String message) {
        this.sendTipMessage(player, message, 0);
    }

    /**
     * 给特定世界的玩家广播Tip消息
     *
     * @param level
     * @param message
     */
    @Override
    public void broadcastTipMessage(Level level, String message) {
        this.broadcastTipMessage(level, message, 0);
    }

    /**
     * 给端内的所有玩家广播Tip消息
     *
     * @param message
     */
    @Override
    public void broadcastTipMessage(String message) {
        this.broadcastTipMessage(message, 0);
    }

    @Override
    public void sendTipMessage(Player player, String message, int stayTick) {
        TextPacket textPacket = new TextPacket();
        textPacket.setMessageType(TextPacket.TipType);
        message = CompiledModel.quickCompile(message, this.entityNameService.getEntityName(player));
        textPacket.setMessage(message);
        if (stayTick > 20) {
            MessageTask messageTask = new MessageTask(this, player, textPacket, stayTick);
            uiTaskHandler.addTaskQueue(messageTask);
        } else {
            this.networkManager.sendMessage(player.getClientAddress(), textPacket);
        }
    }

    @Override
    public void broadcastTipMessage(Level level, String message, int stayTick) {

        Set<Player> players = this.levelService.getPlayers(level);
        for (Player player : players) {
            TextPacket textPacket = new TextPacket();
            textPacket.setMessageType(TextPacket.TipType);
            message = CompiledModel.quickCompile(message, this.entityNameService.getEntityName(player));
            textPacket.setMessage(message);
            if (stayTick > 20) {
                MessageTask messageTask = new MessageTask(this, player, textPacket, stayTick);
                uiTaskHandler.addTaskQueue(messageTask);
            } else {
                this.networkManager.sendMessage(player.getClientAddress(), textPacket);
            }
        }
    }

    @Override
    public void broadcastTipMessage(String message, int stayTick) {

        Collection<Player> players = this.server.getAllPlayers();
        for (Player player : players) {
            TextPacket textPacket = new TextPacket();
            textPacket.setMessageType(TextPacket.TipType);
            message = CompiledModel.quickCompile(message, this.entityNameService.getEntityName(player));
            textPacket.setMessage(message);
            if (stayTick > 20) {
                MessageTask messageTask = new MessageTask(this, player, textPacket, stayTick);
                uiTaskHandler.addTaskQueue(messageTask);
            } else {
                this.networkManager.sendMessage(player.getClientAddress(), textPacket);
            }
        }
    }


    /**
     * 发送system消息
     *
     * @param player
     * @param message
     */
    @Override
    public void sendSystemMessage(Player player, String message) {
        this.sendSystemMessage(player, message, 0);
    }

    /**
     * 给特定世界的玩家广播system消息
     *
     * @param level
     * @param message
     */
    @Override
    public void broadcastSystemMessage(Level level, String message) {
        this.broadcastSystemMessage(level, message, 0);
    }

    /**
     * 给端内的所有玩家广播system消息
     *
     * @param message
     */
    @Override
    public void broadcastSystemMessage(String message) {
        this.broadcastSystemMessage(message, 0);
    }


    @Override
    public void sendSystemMessage(Player player, String message, int stayTick) {
        TextPacket textPacket = new TextPacket();
        textPacket.setMessageType(TextPacket.SystemMessageType);
        message = CompiledModel.quickCompile(message, this.entityNameService.getEntityName(player));
        textPacket.setMessage(message);
        if (stayTick > 20) {
            MessageTask messageTask = new MessageTask(this, player, textPacket, stayTick);
            uiTaskHandler.addTaskQueue(messageTask);
        } else {
            this.networkManager.sendMessage(player.getClientAddress(), textPacket);
        }
    }

    @Override
    public void broadcastSystemMessage(Level level, String message, int stayTick) {

        Set<Player> players = this.levelService.getPlayers(level);
        for (Player player : players) {
            TextPacket textPacket = new TextPacket();
            textPacket.setMessageType(TextPacket.SystemMessageType);
            message = CompiledModel.quickCompile(message, this.entityNameService.getEntityName(player));
            textPacket.setMessage(message);
            if (stayTick > 20) {
                MessageTask messageTask = new MessageTask(this, player, textPacket, stayTick);
                uiTaskHandler.addTaskQueue(messageTask);
            } else {
                this.networkManager.sendMessage(player.getClientAddress(), textPacket);
            }
        }
    }

    @Override
    public void broadcastSystemMessage(String message, int stayTick) {

        Collection<Player> players = this.server.getAllPlayers();
        for (Player player : players) {
            TextPacket textPacket = new TextPacket();
            textPacket.setMessageType(TextPacket.SystemMessageType);
            message = CompiledModel.quickCompile(message, this.entityNameService.getEntityName(player));
            textPacket.setMessage(message);
            if (stayTick > 20) {
                MessageTask messageTask = new MessageTask(this, player, textPacket, stayTick);
                uiTaskHandler.addTaskQueue(messageTask);
            } else {
                this.networkManager.sendMessage(player.getClientAddress(), textPacket);
            }
        }
    }

    /**
     * 发送Whisper消息
     *
     * @param player
     * @param message
     */
    @Override
    public void sendWhisperMessage(Player player, String message) {
        TextPacket textPacket = new TextPacket();
        textPacket.setMessageType(TextPacket.WhisperType);
        textPacket.setPrimaryName("");
        message = CompiledModel.quickCompile(message, this.entityNameService.getEntityName(player));
        textPacket.setMessage(message);
        this.networkManager.sendMessage(player.getClientAddress(), textPacket);
    }

    /**
     * 给特定世界的玩家广播Whisper消息
     *
     * @param level
     * @param message
     */
    @Override
    public void broadcastWhisperMessage(Level level, String message) {
        Set<Player> players = this.levelService.getPlayers(level);
        for (Player player : players) {
            TextPacket textPacket = new TextPacket();
            textPacket.setMessageType(TextPacket.WhisperType);
            textPacket.setPrimaryName("");
            message = CompiledModel.quickCompile(message, this.entityNameService.getEntityName(player));
            textPacket.setMessage(message);
            this.networkManager.sendMessage(player.getClientAddress(), textPacket);
        }
    }

    /**
     * 给端内的所有玩家广播Whisper消息
     *
     * @param message
     */
    @Override
    public void broadcastWhisperMessage(String message) {
        Collection<Player> players = this.server.getAllPlayers();
        for (Player player : players) {
            TextPacket textPacket = new TextPacket();
            textPacket.setMessageType(TextPacket.WhisperType);
            textPacket.setPrimaryName("");
            message = CompiledModel.quickCompile(message, this.entityNameService.getEntityName(player));
            textPacket.setMessage(message);
            this.networkManager.sendMessage(player.getClientAddress(), textPacket);
        }
    }


    /**
     * 发送Announcement消息
     *
     * @param player
     * @param message
     */
    @Override
    public void sendAnnouncement(Player player, String message) {
        TextPacket textPacket = new TextPacket();
        textPacket.setMessageType(TextPacket.AnnouncementType);
        textPacket.setPrimaryName("");
        message = CompiledModel.quickCompile(message, this.entityNameService.getEntityName(player));
        textPacket.setMessage(message);
        this.networkManager.sendMessage(player.getClientAddress(), textPacket);
    }

    /**
     * 给特定世界的玩家广播Announcement消息
     *
     * @param level
     * @param message
     */
    @Override
    public void broadcastAnnouncement(Level level, String message) {
        Set<Player> players = this.levelService.getPlayers(level);
        for (Player player : players) {
            TextPacket textPacket = new TextPacket();
            textPacket.setMessageType(TextPacket.AnnouncementType);
            textPacket.setPrimaryName("");
            message = CompiledModel.quickCompile(message, this.entityNameService.getEntityName(player));
            textPacket.setMessage(message);
            this.networkManager.sendMessage(player.getClientAddress(), textPacket);
        }
    }

    /**
     * 给端内的所有玩家广播Announcement消息
     *
     * @param message
     */
    @Override
    public void broadcastAnnouncement(String message) {
        Collection<Player> players = this.server.getAllPlayers();
        for (Player player : players) {
            TextPacket textPacket = new TextPacket();
            textPacket.setMessageType(TextPacket.AnnouncementType);
            textPacket.setPrimaryName("");
            message = CompiledModel.quickCompile(message, this.entityNameService.getEntityName(player));
            textPacket.setMessage(message);
            this.networkManager.sendMessage(player.getClientAddress(), textPacket);
        }
    }
}
