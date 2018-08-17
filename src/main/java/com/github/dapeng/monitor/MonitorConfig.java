package com.github.dapeng.monitor;

/**
 * @author huyj
 * @Created 2018-08-09 17:19
 */
public class MonitorConfig {

    public static String INFLUXDB_HOST = "influxdb.today36524.td";
    public static String INFLUXDB_NAME = "dapengState";
    public static String TIME_PERIOD = "10";
    public static String SERVICENAME = "❅※❅";
    public static String SERVICE_COUNT_URL = "http://"+INFLUXDB_HOST+"/query?pretty=true&db="+INFLUXDB_NAME+"&q=SELECT%20sum(%22total_calls%22)%20FROM%20%22dapeng_service_process%22%20WHERE%20(%22service_name%22%20=%20%27"+SERVICENAME+"%27)%20AND%20time%20%3E%20now()%20-%20"+TIME_PERIOD+"m%20GROUP%20BY%20time(1m),%20%22method_name%22%20fill(0)";
}
