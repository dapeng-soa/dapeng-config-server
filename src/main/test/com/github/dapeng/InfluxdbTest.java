package com.github.dapeng;

import com.github.dapeng.util.DateUtil;
import com.github.dapeng.util.InfluxDBUtil;
import com.github.dapeng.util.PropertiesUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.List;

/**
 * influxdb 测试
 *
 * @author huyj
 * @Created 2018/6/13 10:54
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ConfigServerApplication.class)
@ConfigurationProperties
@WebAppConfiguration
@ImportResource(locations = {"classpath:spring.xml"})
public class InfluxdbTest {
    @Test
    public void influxdbQuery() throws Exception {
        PropertiesUtil.loadProperties();

        InfluxDBUtil influxDBUtil = new InfluxDBUtil();
        //String queryStr = "SELECT * FROM \"dapeng_service_process\" WHERE time >=  '" + DateUtil.getInfluxDbDate() + "'";
        String queryStr = "SELECT * FROM \"dapeng_service_process\" WHERE (\"service_name\" = 'com.today.api.goods.service.OpenGoodsService') AND time >= '" + DateUtil.getInfluxDbDate() + "' GROUP BY \"method_name\" fill(0) ";
        List<HashMap<String, Object>> queryResult = influxDBUtil.query(queryStr);
        System.out.println(queryResult);

        /*Map<String,String> tags = new HashMap<>();
        tags.put("test","1111");
        tags.put("test1","1111") ;
        Map<String,Object> f = new HashMap<>();
        f.put("test",120);
        f.put("test1",110) ;
        //influxDBUtils.insert("test1",tags,f);*/

        /*Point.Builder commit = Point.measurement("test2");
        commit.fields(f);
        commit.tag(tags);
        commit.time(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        influxDBUtils.getInfluxDB().write("dapengState","",commit.build());*/
    }
}
