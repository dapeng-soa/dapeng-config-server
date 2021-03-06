package com.github.dapeng.common;

/**
 * @author with struy.
 * Create by 2018/6/1 14:41
 * email :yq1724555319@gmail.com
 */

public class Commons {

    public static final String PAGENO_ERROR_MSG = "页数不能小于0";
    public static final String DATA_NOTFOUND_MSG = "数据不存在";
    public static final String SYS_CONFIG_ERROR = "同步服务配置出错";
    public static final String CONFIG_PUBLISHED_MSG = "配置已经发布过了";
    public static final String SERVICE_ISEMPTY_MSG = "服务名不能为空";
    public static final String SERVICE_ISEXISTS_MSG = "此服务已经存在配置";
    public static final String SERVICE_FORMAT_EROR_MSG = "服务名格式有误";
    public static final String SAVE_SUCCESS_MSG = "保存成功";
    public static final String SAVE_ERROR_MSG = "保存失败";
    public static final String LOADED_DATA = "数据载入成功";
    public static final String LOAD_DATA_ERROR = "数据载入失败";
    public static final String DEL_SUCCESS_MSG = "删除成功";
    public static final String DEL_ERROR_MSG = "删除失败";
    public static final String PUBLISH_SUCCESS_MSG = "发布配置成功";
    public static final String ROLLBACK_SUCCESS_MSG = "回滚配置成功";
    public static final String ADD_WHITELIST_SUCCESS_MSG = "白名单添加成功";
    public static final String COMMON_ERRO_MSG = "操作失败";
    public static final String COMMON_SUCCESS_MSG = "操作成功";
    public static final String APIKEY_PWD_NOTNULL = "ApiKey和密码不能为空";
    public static final String PWD_LENGTH_ERROR = "密码必须等于12位";
    public static final String ZKHOST_NOTNULL = "zookeeper集群地址不能为空";
    public static final int SUCCESS_CODE = 200;
    public static final int ERROR_CODE = 4004;
    public static final int DEF_PWD_LENGTH = 12;

    // 数据状态
    public static final int NORMAL_STATUS = 0;
    public static final int DELETED_STATUS = 1;

    /*
    构建状态【0:初始，1:构建中，2:构建成功，3:构建失败】
     */
    public static final long BUILD_INIT = 0;
    public static final long BUILD_ING = 1;
    public static final long BUILD_SUCCESS = 2;
    public static final long BUILD_FAIL = 3;


    public static final String INFLUXDB_USERNAME = "influxdb_username";
    public static final String INFLUXDB_PASSWORD = "influxdb_password";
    public static final String INFLUXDB_OPENURL = "influxdb_openurl";
    public static final String INFLUXDB_DATABASE = "influxdb_database";

    public static final String MAIN_DATASOURCE = "mainSource";
    public static final String EXTRA_DATASOURCE = "extraSource";

    public static final String NETWORK_MTU_KEY = "com.docker.network.driver.mtu";
    public static final String NETWORK_MTU_VAL = "1500";
    public static final String DEFAULT_NETWORK = "net";
    public static final String DEFAULT_VERSION = "2";

    public static final String ROLE_PREFIX = "ROLE_";
    public static final String ADMIN = "ADMIN";
    public static final String SUPER_ROLE = ROLE_PREFIX + ADMIN;

    public static final String CONFIG_COOKIE_PATH = "/soa/config/cookies";
}
