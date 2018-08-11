package com.github.dapeng.common.mail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * <p>Title: 邮件配置信息</p>
 * <p>Description: 描述（简要描述类的职责、实现方式、使用注意事项等）</p>
 */
public class MailCfg {

    private static Properties properties;

    static {
        properties = new Properties();
        InputStream path = MailCfg.class.getClassLoader().getResourceAsStream("config_server.properties");
        try {
            properties.load(new BufferedReader(new InputStreamReader(path, "GBK")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 邮件服务器
     */
    public final static String HOST = properties.getProperty("mail.smtp.host");

    /**
     * 邮件编码
     */
    public final static String CHARSET = properties.getProperty("mail.charset");

    /**
     * 发送者邮箱
     */
    public final static String DEFAULT_FROM_EMAIL = properties.getProperty("mail.sender.username");

    /**
     * 发送者密码
     */
    public final static String DEFAULT_FROM_PASSWD = properties.getProperty("mail.sender.password");

    /**
     * 发送者名称
     */
    public final static String DEFAULT_FROM_NAME = properties.getProperty("mail.from.name");

    public final static String DEFAULT_TO_NAME = properties.getProperty("mail.to.email");

}
