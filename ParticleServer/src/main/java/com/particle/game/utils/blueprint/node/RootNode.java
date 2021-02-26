package com.particle.game.utils.blueprint.node;

import com.particle.executor.service.NodeScheduleService;
import com.particle.executor.thread.IScheduleThread;
import com.particle.game.utils.blueprint.context.BackgroundContext;
import com.particle.model.player.Player;

public class RootNode<T extends BackgroundContext> extends BaseTaskNode<T> {
    public RootNode(String taskName) {
        super(taskName);
    }

    @Override
    public void run(T context) {

        Player player = context.getPlayer();

        /*
         * 初始化Context中的一些信息
         */
        IScheduleThread nodeSchedule = NodeScheduleService.getInstance().getThread(player.getClientAddress().hashCode());
        IScheduleThread levelSchedule = player.getLevel().getLevelSchedule();

        context.setNodeScheduleThread(nodeSchedule);
        context.setLevelScheduleThread(levelSchedule);

        this.next(context);
    }
}
