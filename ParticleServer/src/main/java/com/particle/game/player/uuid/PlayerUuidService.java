package com.particle.game.player.uuid;

import com.google.common.collect.Maps;
import com.particle.api.utils.UuidHelperApi;
import com.particle.game.player.uuid.model.PlayerBaseInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.*;
import java.util.Map;

@Singleton
public class PlayerUuidService implements UuidHelperApi {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlayerUuidService.class);

    private File file;

    private Map<String, PlayerBaseInfo> playerUuid2PlayerBaseInfo;
    private Map<String, PlayerBaseInfo> playerName2PlayerBaseInfo;
    private Map<Long, PlayerBaseInfo> roleId2PlayerBaseInfo;

    public void init() {
        try {
            file = new File("players/playerBaseInfoList.txt");
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            LOGGER.error("create new file error ! file name is players/playerBaseInfoList.txt");
            System.exit(1);
        }

        Map<String, PlayerBaseInfo> _playerUuid2PlayerBaseInfo = Maps.newConcurrentMap();
        Map<String, PlayerBaseInfo> _playerName2PlayerBaseInfo = Maps.newConcurrentMap();
        Map<Long, PlayerBaseInfo> _roleId2PlayerBaseInfo = Maps.newConcurrentMap();

        try {
            // 读取存档数据
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String temp = "";
            while ((temp = bufferedReader.readLine()) != null) {
                PlayerBaseInfo playerBaseInfo = new PlayerBaseInfo(temp);

                _playerUuid2PlayerBaseInfo.put(playerBaseInfo.getPlayerUuid(), playerBaseInfo);
                _playerName2PlayerBaseInfo.put(playerBaseInfo.getPlayerName(), playerBaseInfo);
                _roleId2PlayerBaseInfo.put(playerBaseInfo.getRoleId(), playerBaseInfo);

            }
            bufferedReader.close();

            playerUuid2PlayerBaseInfo = _playerUuid2PlayerBaseInfo;
            playerName2PlayerBaseInfo = _playerName2PlayerBaseInfo;
            roleId2PlayerBaseInfo = _roleId2PlayerBaseInfo;
        } catch (Exception e) {
            LOGGER.error("load players/playerBaseInfos.json error !");
            System.exit(1);
        }
    }

    @Override
    public String getPlayerName(String playerUuid) {
        PlayerBaseInfo playerBaseInfo = playerUuid2PlayerBaseInfo.get(playerUuid);
        return playerBaseInfo != null ? playerBaseInfo.getPlayerName() : "";
    }

    @Override
    public String getPlayerUuid(String playerName) {
        PlayerBaseInfo playerBaseInfo = playerName2PlayerBaseInfo.get(playerName);
        return playerBaseInfo != null ? playerBaseInfo.getPlayerUuid() : "";
    }

    @Override
    public long getPlayerRoleId(String playerUuid) {
        PlayerBaseInfo playerBaseInfo = playerUuid2PlayerBaseInfo.get(playerUuid);
        return playerBaseInfo != null ? playerBaseInfo.getRoleId() : -1;
    }

    @Override
    public String getPlayerUuid(long roleId) {
        PlayerBaseInfo playerBaseInfo = playerUuid2PlayerBaseInfo.get(roleId);
        return playerBaseInfo != null ? playerBaseInfo.getPlayerUuid() : "";
    }

    public void playerNameCheck(String playerName, String playerUuid, long roleId) {
        PlayerBaseInfo playerBaseInfo = playerUuid2PlayerBaseInfo.get(playerUuid);
        if (playerBaseInfo == null) {
            playerBaseInfo = new PlayerBaseInfo(playerUuid, playerName, roleId, System.currentTimeMillis());

            playerUuid2PlayerBaseInfo.put(playerUuid, playerBaseInfo);
            playerName2PlayerBaseInfo.put(playerName, playerBaseInfo);
            roleId2PlayerBaseInfo.put(roleId, playerBaseInfo);

            save();
        }
    }

    public void save() {
        BufferedWriter bufferedWriter = null;
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            bufferedWriter = new BufferedWriter(fileWriter);
            for (PlayerBaseInfo playerBaseInfo : playerUuid2PlayerBaseInfo.values()) {
                bufferedWriter.write(playerBaseInfo.toString());
                bufferedWriter.newLine();//换行
            }

            bufferedWriter.flush();


        } catch (Exception e) {
            LOGGER.error("Fail to save playerBaseInfos!");
        } finally {
            try {
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
