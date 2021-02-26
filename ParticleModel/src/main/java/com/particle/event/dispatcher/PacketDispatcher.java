package com.particle.event.dispatcher;


import com.particle.event.handle.PacketEventHandle;
import com.particle.event.router.PacketRouter;
import com.particle.executor.service.NodeScheduleService;
import com.particle.executor.thread.IScheduleThread;
import com.particle.model.network.packets.DataPacket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class PacketDispatcher {

    private static final Logger LOGGER = LoggerFactory.getLogger(PacketDispatcher.class);

    /**
     * 单例对象
     */
    private static final PacketDispatcher INSTANCE = new PacketDispatcher();

    /**
     * 获取单例
     */
    public static PacketDispatcher getInstance() {
        return PacketDispatcher.INSTANCE;
    }

    // 记录执行时间过长的事件的阈值 20ms
    private static final long EVENT_LOG_RUN_TIME_THREASHOLD = 20000000;

    private NodeScheduleService nodeScheduleService = NodeScheduleService.getInstance();

    private Map<String, AtomicLong> runningTime = new HashMap<>();

    private PacketDispatcher() {
        for (String threadName : nodeScheduleService.getThreadMap().keySet()) {
            this.runningTime.put(threadName, new AtomicLong(0));
        }
    }

    public void subscriptPacket(PacketEventHandle handle) {
        PacketRouter.subscript(handle);
    }

    public void dispatchEvent(InetSocketAddress address, DataPacket packet) {
        PacketEventHandle handler = PacketRouter.route(packet);

        if (handler == null) {
            return;
        }

        //执行任务
        IScheduleThread scheduleThread = nodeScheduleService.getThread(address.hashCode());
        scheduleThread.scheduleSimpleTask("packet_handle_" + packet.pid(), () -> {
            long nanoTimestamp = System.nanoTime();

            handler.handle(address, packet);

            // 统计
            long timeRunning = System.nanoTime() - nanoTimestamp;
            this.runningTime.get(scheduleThread.getName()).addAndGet(timeRunning);
            if (timeRunning > EVENT_LOG_RUN_TIME_THREASHOLD) {
                LOGGER.warn("Packet Event {} running tooooo long! cost {}ms", "packet_handle_" + packet.pid(), timeRunning / 1000000);
            }
        });
    }

    public Map<String, AtomicLong> getRunningTime() {
        return runningTime;
    }
}
