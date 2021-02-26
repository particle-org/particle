package com.particle.api.ai.behavior;

import java.util.List;

public interface IBranchNode extends IBehaviour {

    /**
     * 添加子节点
     *
     * @param child
     */
    void addChild(IBehaviour child, int weight);

    /**
     * 移除子节点
     *
     * @param child
     */
    void removeChild(IBehaviour child);

    /**
     * 清空子节点
     */
    void clearChild();

    /**
     * 获取子节点列表
     *
     * @return
     */
    List<IBehaviour> getChildren();


}
