package com.particle.model.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class RconMessage {

    private static final Logger logger = LoggerFactory.getLogger(RconMessage.class);

    public static final String SYNC_TYPE = "sync";

    public static final String MSG_TYPE = "msg";

    public static final String RCON_SPLIT = "&&";

    private String type;

    private boolean isServerName;

    private String serverName;

    private InetSocketAddress address;

    private String message;

    public boolean isSyncType() {
        if (StringUtils.isEmpty(this.type)) {
            return false;
        }
        if (type.equalsIgnoreCase(SYNC_TYPE)) {
            return true;
        }
        return false;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public void setAddress(InetSocketAddress address) {
        this.address = address;
        this.isServerName = false;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
        this.isServerName = true;
    }

    public boolean isServerName() {
        return isServerName;
    }

    /**
     * encode方法
     *
     * @return
     */
    public String encode() {
        if (this.isServerName) {
            return String.format("%s%s%s%s%s", type, RCON_SPLIT, this.serverName, RCON_SPLIT, this.message);
        } else {
            return String.format("%s%s%s:%s%s%s", type, RCON_SPLIT,
                    this.address.getAddress().getHostAddress(), this.address.getPort(),
                    RCON_SPLIT, this.message);
        }
    }

    /**
     * 静态sync方法
     *
     * @param address
     * @param serverName
     * @return
     */
    public static String encodeSyncType(InetSocketAddress address, String serverName) {
        return String.format("%s%s%s:%s%s%s", SYNC_TYPE, RCON_SPLIT,
                address.getAddress().getHostAddress(), address.getPort(),
                RCON_SPLIT, serverName);
    }

    /**
     * 静态encode方法
     *
     * @param address
     * @param cmd
     * @return
     */
    public static String encode(InetSocketAddress address, String cmd) {
        return String.format("%s%s%s:%s%s%s", MSG_TYPE, RCON_SPLIT,
                address.getAddress().getHostAddress(), address.getPort(),
                RCON_SPLIT, cmd);
    }

    /**
     * 静态encode方法
     *
     * @param serverName
     * @param cmd
     * @return
     */
    public static String encode(String serverName, String cmd) {
        return String.format("%s%s%s%s%s", MSG_TYPE, RCON_SPLIT, serverName, RCON_SPLIT, cmd);
    }

    /**
     * 静态decode方法
     *
     * @param receive
     * @return
     */
    public static RconMessage docode(String receive) {
        if (StringUtils.isEmpty(receive)) {
            return null;
        }
        RconMessage rconMessage = new RconMessage();

        if (!receive.contains(RCON_SPLIT)) {
            // 默认作为sync的回复消息
            rconMessage.setType(SYNC_TYPE);
            return rconMessage;
        }
        String[] splits = receive.split(RCON_SPLIT);
        int length = splits.length;
        if (length != 3) {
            logger.error("收到的rcon参数[{}]不合法", receive);
            return null;
        }
        String type = splits[0];
        rconMessage.setType(type);
        if (SYNC_TYPE.equalsIgnoreCase(type)) {
            InetSocketAddress address = parseInetSocketAddress(splits[1]);
            if (address == null) {
                return null;
            }
            rconMessage.setAddress(address);
            rconMessage.setServerName(splits[2]);
        } else {
            InetSocketAddress address = parseInetSocketAddress(splits[1]);
            if (address == null) {
                rconMessage.setServerName(splits[1]);
            } else {
                rconMessage.setAddress(address);
            }
            rconMessage.setMessage(splits[2]);
        }
        return rconMessage;
    }

    /**
     * 解析inet
     *
     * @param value
     * @return
     */
    private static InetSocketAddress parseInetSocketAddress(String value) {
        if (value.contains(":")) {
            String[] ipSplits = value.split(":");
            if (ipSplits.length != 2) {
                logger.error("parseInetSocketAddress failed! 参数[{}]不合法", value);
                return null;
            }
            try {
                return new InetSocketAddress(InetAddress.getByName(ipSplits[0]), Integer.parseInt(ipSplits[1]));
            } catch (UnknownHostException uhe) {
                logger.error("parseInetSocketAddress failed!", uhe);
                return null;
            }
        } else {
            return null;
        }
    }
}
