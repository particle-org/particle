package com.particle.model.exception;

public interface ErrorCode {

    public static final int SUCCESS = 200;

    public static final int COMMON_ERROR = -1;

    /**
     * 参数异常
     */
    public static final int PARAM_ERROR = 601;

    /**
     * 核心的异常
     */
    public static final int CORE_EROOR = 10000;


    /**
     * db的异常码
     */
    public static final int DB_ERROR = 20000;

    /**
     * redis异常
     */
    public static final int REDIS_ERROR = 20001;

    /**
     * lelvelDb异常
     */
    public static final int LEVELDB_ERROR = 20002;

    /**
     * mongodb异常
     */
    public static final int MONGODB_ERROR = 20003;

    /**
     * mysql异常
     */
    public static final int MYSQL_ERROR = 20004;

    /**
     * 配置文件异常
     */
    public static final int CONFIG_ERROR = 20005;

    /**
     * 插件异常码
     */
    public static final int PLUGIG_ERROR = 30000;


    /**
     * 网络层异常码
     */
    public static final int NETWORK_ERROR = 40000;
}
