package com.particle.event.dispatcher;

public enum EventRank {

    //预处理使用，优先级最高
    PRE_PROCESS(100),
    LOWEST(99),

    //高优先级的任务
    IMPORT(81),
    LOW(80),

    //插件的默认优先级
    DEFAULT(61),
    MIDDLE(60),

    //低优先级的任务
    UNIMPORTANT(41),
    HIGH(40),

    //插件业务逻辑结束，最后统一处理
    AFTER_PROCESS(21),
    HEIGHT(20),

    //监控使用，不对数据进行修改
    MONITOR(0),

    //服务端本地使用
    LOCAL(-1);

    private int level;

    EventRank(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
