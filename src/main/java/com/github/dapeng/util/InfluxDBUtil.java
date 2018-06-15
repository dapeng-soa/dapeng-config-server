package com.github.dapeng.util;

import com.github.dapeng.core.helper.SoaSystemEnvProperties;
import com.github.dapeng.entity.ZkNode;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.dto.QueryResult.Result;
import org.influxdb.dto.QueryResult.Series;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static com.github.dapeng.common.Commons.*;

/**
 * InfluxDB 操作工具类
 *
 * @author huyj
 * @Created 2018/6/13 10:40
 */
public class InfluxDBUtil {
    private static final Logger logger = LoggerFactory.getLogger(InfluxDBUtil.class);

    private String username = SoaSystemEnvProperties.get(INFLUXDB_USERNAME, PropertiesUtil.getProperty(INFLUXDB_USERNAME, "admin"));//用户名
    private String password = SoaSystemEnvProperties.get(INFLUXDB_PASSWORD, PropertiesUtil.getProperty(INFLUXDB_PASSWORD, "admin"));//密码
    private String openurl = SoaSystemEnvProperties.get(INFLUXDB_OPENURL, PropertiesUtil.getProperty(INFLUXDB_OPENURL, "http://127.0.0.1:8086"));//连接地址
    private String database = SoaSystemEnvProperties.get(INFLUXDB_DATABASE, PropertiesUtil.getProperty(INFLUXDB_DATABASE, null));//数据库

    private InfluxDB influxDB;

    public InfluxDBUtil() {
        influxDbBuild();
    }

    public InfluxDBUtil(String username, String password, String openurl, String database) {
        this.username = username;
        this.password = password;
        this.openurl = openurl;
        this.database = database;
        influxDbBuild();
    }


    public static String getOpenUrl(ZkNode zkNode) {
        return "http://" + zkNode.getInfluxdbHost() + ":8086";
    }

    /**
     * 连接时序数据库；获得InfluxDB
     **/
    private InfluxDB influxDbBuild() {
        if (influxDB == null) {
            influxDB = InfluxDBFactory.connect(openurl, username, password);
            try {
                influxDB.createDatabase(database);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("--- openurl:[{}],username:[{}], password:[{}] influxdb connect failed ... Cause：[{}]", openurl, username, password, e.getMessage());
            }
        }
        return influxDB;
    }

    /**
     * 设置数据保存策略
     * defalut 策略名 /database 数据库名/ 30d 数据保存时限30天/ 1  副本个数为1/ 结尾DEFAULT 表示 设为默认的策略
     */
    public void createRetentionPolicy() {
        String command = String.format("CREATE RETENTION POLICY \"%s\" ON \"%s\" DURATION %s REPLICATION %s DEFAULT",
                "defalut", database, "30d", 1);
        this.query(command);
    }

    /**
     * 查询
     *
     * @param command 查询语句
     * @return
     */
    public List<HashMap<String, Object>> query(String command) {
        try {
            List<HashMap<String, Object>> lists = new ArrayList<HashMap<String, Object>>();
            QueryResult queryResult = influxDB.query(new Query(command, database));
            if (queryResult.getResults() == null) {
                return lists;
            }
            for (Result result : queryResult.getResults()) {
                List<Series> series = result.getSeries();
                if (series != null && !series.isEmpty()) {
                    for (Series serie : series) {
                        lists.add(getQueryData(serie));
                    }
                }
            }
            return lists;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("--- influxdb query[command:[{}]] failed ... Cause：[{}]", command, e.getMessage());
        }
        return null;
    }


    /**
     * 插入
     *
     * @param measurement 表
     * @param tags        标签
     * @param fields      字段
     */
    public void insert(String measurement, Map<String, String> tags, Map<String, Object> fields) {
        Point.Builder builder = Point.measurement(measurement);
        builder.tag(tags);
        builder.fields(fields);

        influxDB.write(database, "", builder.build());
    }

    /**
     * 删除
     *
     * @param command 删除语句
     * @return 返回错误信息
     */
    public String deleteMeasurementData(String command) {
        QueryResult result = influxDB.query(new Query(command, database));
        return result.getError();
    }

    /**
     * 创建数据库
     *
     * @param dbName
     */
    public void createDB(String dbName) {
        influxDB.createDatabase(dbName);
    }

    /**
     * 删除数据库
     *
     * @param dbName
     */
    public void deleteDB(String dbName) {
        influxDB.deleteDatabase(dbName);
    }


    /*********************************************************/


    /*InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
    List<NewBean> cpuList = resultMapper.toPOJO(queryResult, NewBean.class);*/

    /***整理列名、行数据***/
    private HashMap<String, Object> getQueryData(Series serie) {
        List<List<Object>> values = serie.getValues();
        List<String> columns = serie.getColumns();
        //List<HashMap<String, Object>> listMaps = new ArrayList<HashMap<String, Object>>();

        HashMap<String, Object> resultMap = new HashMap<>();

        // add fileds data
        for (List<Object> list : values) {
            HashMap<String, Object> itemMap = new HashMap<String, Object>();
            for (int i = 0; i < list.size(); i++) {
                resultMap.put(columns.get(i), list.get(i));
            }
            //listMaps.add(itemMap);
        }

        // add tags data
        if (Objects.nonNull(serie.getTags())) {
            serie.getTags().forEach((key, value) -> {
                HashMap<String, Object> tagMap = new HashMap<>();
                resultMap.put(key, value);
                //resultMap.add(tagMap);
            });
        }
        return resultMap;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOpenurl() {
        return openurl;
    }

    public void setOpenurl(String openurl) {
        this.openurl = openurl;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public InfluxDB getInfluxDB() {
        return influxDB;
    }

    public void setInfluxDB(InfluxDB influxDB) {
        this.influxDB = influxDB;
    }
}
