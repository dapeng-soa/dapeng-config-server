package com.github.dapeng.util;

import com.github.dapeng.k8s.yaml.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
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
            InputStream inputStream = FileUtils.getResourceFileInputStream("config_server.properties");
            properties.load(inputStream);
            logger.info("---------load properties success...-----------");
        } catch (IOException e) {
            logger.info("load config_server.properties error::", e);
        }
    }


    public static String getProperty(String key, String defaultValue) {
        return Objects.isNull(properties) || Objects.isNull(properties.get(key)) ? defaultValue : (String) properties.get(key);
    }

}
