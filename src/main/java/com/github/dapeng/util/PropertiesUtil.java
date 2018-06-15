package com.github.dapeng.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

/**
 * properties 工具类
 *
 * @author huyj
 * @Created 2018/6/13 11:59
 */
public class PropertiesUtil {
    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
    public static Properties properties;

    public static void loadProperties() {
        System.out.println("");
        logger.info("---------start load properties...-----------");
        properties = new Properties();
        try {
            // properties.load(in);
            properties.load(ClassLoader.getSystemResourceAsStream("config_server.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String getProperty(String key, String defaultValue) {
        return Objects.isNull(properties) || Objects.isNull(properties.get(key)) ? defaultValue : (String) properties.get(key);
    }

}
