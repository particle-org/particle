package com.particle.network.monitor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.particle.model.network.packets.DataPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class PacketLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketLogger.class);

    /**
     * 单例对象
     */
    private static final PacketLogger INSTANCE = new PacketLogger();

    /**
     * 获取单例
     */
    public static PacketLogger getInstance() {
        return PacketLogger.INSTANCE;
    }

    private AtomicLong sendingPacketAmount = new AtomicLong(0);
    private AtomicLong receivingPacketAmount = new AtomicLong(0);

    private boolean enableDetailLog;

    private PacketLogger() {
    }

    public long getSendingPacketAmount() {
        return sendingPacketAmount.get();
    }

    public long getReceivingPacketAmount() {
        return receivingPacketAmount.get();
    }

    public void setEnableDetailLog(boolean enableDetailLog) {
        this.enableDetailLog = enableDetailLog;
    }

    public void loggerInputPacket(InetSocketAddress inetSocketAddress, List<DataPacket> dataPacketLists) {
        for (DataPacket dataPacket : dataPacketLists) {
            this.loggerInputPacket(inetSocketAddress, dataPacket);
        }
    }

    public void loggerInputPacket(InetSocketAddress inetSocketAddress, DataPacket dataPacket) {
        this.receivingPacketAmount.getAndIncrement();

        if (enableDetailLog) {
            LOGGER.info("{} -> [Input]{}", inetSocketAddress, dataPacket.getClass().getName());
            LOGGER.info(this.analysePacket(dataPacket));
        }
    }

    public void loggerOutputPacket(InetSocketAddress inetSocketAddress, List<DataPacket> dataPacketLists) {
        for (DataPacket dataPacket : dataPacketLists) {
            this.loggerOutputPacket(inetSocketAddress, dataPacket);
        }
    }

    public void loggerOutputPacket(InetSocketAddress inetSocketAddress, DataPacket dataPacket) {
        this.sendingPacketAmount.getAndIncrement();

        if (enableDetailLog) {
            LOGGER.info("{} <- [Output]{}", inetSocketAddress, dataPacket.getClass().getName());
            LOGGER.info(this.analysePacket(dataPacket));
        }
    }

    private String analysePacket(DataPacket dataPacket) {
        JSONObject jsonObject = new JSONObject();

        Method[] declaredMethods = dataPacket.getClass().getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            String name = declaredMethod.getName();

            if (name.startsWith("get")) {
                try {
                    jsonObject.put(name.substring(3), declaredMethod.invoke(dataPacket));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        return JSON.toJSONString(jsonObject, SerializerFeature.PrettyFormat);
    }
}
