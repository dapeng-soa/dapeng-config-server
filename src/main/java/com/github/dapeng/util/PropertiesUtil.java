package com.github.dapeng.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
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
        logger.info("---------start load properties...-----------");
        properties = new Properties();
        try {
            File file = ResourceUtils.getFile("classpath:config_server.properties");
            FileInputStream inputStream = new FileInputStream(file);
            properties.load(inputStream);
            logger.info("---------load propertie success...-----------");
        } catch (IOException e) {
            logger.info("load config_server.properties error::",e);
        }
    }


    public static String getProperty(String key, String defaultValue) {
        return Objects.isNull(properties) || Objects.isNull(properties.get(key)) ? defaultValue : (String) properties.get(key);
    }

}
