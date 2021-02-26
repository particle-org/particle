package com.particle.game.ui.task;

import com.particle.game.ui.TitleService;
import com.particle.model.network.packets.DataPacket;
import com.particle.model.player.Player;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActionBarTask extends UITask {

    private static final Logger looger = LoggerFactory.getLogger(ActionBarTask.class);

    // 每通知一次客户端，客户端会维持显示45 tick
    private static final int KEEP = 45;

    private DataPacket titlePacket;

    private TitleService titleService;

    private int firstTickEnd = 0;

    private int repeatCounts = 0;


    public ActionBarTask(TitleService titleService,
                         Player player, DataPacket titlePacket, int tickLive) {
        super(player, TaskType.ACTIONBAR, tickLive);
        this.titlePacket = titlePacket;
        this.titleService = titleService;
        this.firstTickEnd = tickLive % KEEP;
    }

    @Override
    public void onRun() {
        int residue = this.tick - this.firstTickEnd;
        if (this.tick == 0 || this.tick == this.firstTickEnd) {
            this.onTick();
        } else if (residue > 0 && residue % KEEP == 0) {
            this.onTick();
        }
        this.tick++;
    }

    @Override
    protected void onTick() {
        this.titleService.sendTitlePacket(player, titlePacket);
    }
}
