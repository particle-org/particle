package com.particle.core.aoi.container;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GridDataContainer {

    private static final Logger LOGGER = LoggerFactory.getLogger(GridDataContainer.class);

    private Object[] gridData = new Object[1];

    // ----- 节点数据操作 -----
    Object getGridData(int index) {
        // 合法性检查
        if (index < 0) {
            LOGGER.warn("Illegal component index of {}!", index);
            return null;
        }

        if (index < this.gridData.length) {
            return gridData[index];
        }

        return null;
    }

    void setGridData(int index, Object gridData) {
        // 合法性检查
        if (index < 0) {
            LOGGER.warn("Illegal component index of {}!", index);
            return;
        }

        // 扩展数组
        while (!(index < this.gridData.length)) {
            Object[] storage = new Object[this.gridData.length << 1];
            System.arraycopy(this.gridData, 0, storage, 0, this.gridData.length);

            this.gridData = storage;
        }

        // 设置组件
        this.gridData[index] = gridData;
    }

}
