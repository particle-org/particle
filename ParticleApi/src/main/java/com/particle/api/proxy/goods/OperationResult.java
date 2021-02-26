package com.particle.api.proxy.goods;

public enum OperationResult {
    /**
     * 操作成功
     */
    SUCCESS,

    /**
     * 操作数量不合法
     */
    ILLEGAL_AMOUNT,

    /**
     * 无效的商品ID或商品类型
     */
    UNKNOWN_GOODS,

    /**
     * 读取数据失败
     */
    LOAD_DATA_FAILED,

    /**
     * 未知错误
     */
    UNKNOWN_ERROR,

    /**
     * 已经获得
     */
    HAS_GAIN,

    /**
     * 不合法的操作
     */
    ILLEGAL_OPERATE
}
