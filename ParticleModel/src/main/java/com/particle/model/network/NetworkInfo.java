package com.particle.model.network;

import com.particle.util.configer.anno.ConfigBean;

@ConfigBean(name = "network")
public class NetworkInfo {
    /**
     * 服务器的ip
     */
    private String ip = "127.0.0.1";
    /**
     * 服务器的端口
     */
    private int port = 19132;
    /**
     * 是否需要tcp
     */
    private boolean needTcp = false;
    /**
     * 是否需要udp
     */
    private boolean needUdp = true;
    /**
     * 最大连接数
     */
    private int maxConnection = 230;

    /**
     * 分组
     */
    private int group = 2;
    /**
     * 服务器名称
     */
    private String serverName = "particleServer";
    /**
     * 最大支持的mtu
     */
    private int maxMtu = 1200;

    /**
     * 网络详细日志
     */
    private boolean enableDetailLog = false;

    /**
     * 是否只允许正式服登陆
     */
    private boolean isOnlyObt = true;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isNeedTcp() {
        return needTcp;
    }

    public void setNeedTcp(boolean needTcp) {
        this.needTcp = needTcp;
    }

    public boolean isNeedUdp() {
        return needUdp;
    }

    public void setNeedUdp(boolean needUdp) {
        this.needUdp = needUdp;
    }

    public int getMaxConnection() {
        return maxConnection;
    }

    public void setMaxConnection(int maxConnection) {
        this.maxConnection = maxConnection;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getMaxMtu() {
        return maxMtu;
    }

    public void setMaxMtu(int maxMtu) {
        this.maxMtu = maxMtu;
    }

    public boolean isEnableDetailLog() {
        return enableDetailLog;
    }

    public void setEnableDetailLog(boolean enableDetailLog) {
        this.enableDetailLog = enableDetailLog;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public boolean isOnlyObt() {
        return isOnlyObt;
    }

    public void setOnlyObt(boolean onlyObt) {
        isOnlyObt = onlyObt;
    }
}
