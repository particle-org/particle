package com.particle.core.aoi.container;

import com.particle.core.aoi.model.Grid;
import com.particle.core.aoi.model.Subscriber;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class SceneDataProvider<T> {

    private static AtomicInteger dataIndexGenerate = new AtomicInteger(0);

    private int dataIndex = dataIndexGenerate.getAndIncrement();

    /**
     * 当节点激活
     *
     * @param grid
     */
    public abstract void onGridActive(Grid grid);

    /**
     * 当结点取消激活
     *
     * @param grid
     */
    public abstract void onGridInactive(Grid grid);

    /**
     * 当节点被订阅
     *
     * @param grid
     */
    public abstract void onSubscribed(Grid grid, Subscriber subscriber);

    /**
     * 当节点被取消订阅
     *
     * @param grid
     */
    public abstract void onUnsubscribed(Grid grid, Subscriber subscriber);

    /**
     * 获取节点数据
     *
     * @param grid
     * @return
     */
    public T getData(Grid grid) {
        return (T) grid.getGridDataContainer().getGridData(this.dataIndex);
    }

    /**
     * 设置节点数据
     *
     * @param grid
     * @param data
     */
    public void setData(Grid grid, T data) {
        grid.getGridDataContainer().setGridData(this.dataIndex, data);
    }

}
