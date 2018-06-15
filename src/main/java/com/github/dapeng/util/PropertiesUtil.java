package com.github.dapeng.util;

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
    public static Properties properties;

    public static void loadProperties() {
        System.out.println("start load properties...");
        properties = new Properties();
        try {
            // properties.load(in);
            properties.load(ClassLoader.getSystemResourceAsStream("config_server.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String getProperty(String key, String defaultValue) {
        return Objects.isNull(properties) ||  Objects.isNull(properties.get(key)) ? defaultValue : (String) properties.get(key);
    }

}
