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
    private static String HOST_KEY = "mail.smtp.host";
    private static String CHARSET_KEY = "mail.charset";
    private static String AUTH_KEY = "mail.smtp.auth";
    private static String USER_NAME_KEY = "mail.sender.username";
    private static String PASSWORD_KEY = "mail.sender.password";
    private static String NAME_KEY = "mail.from.name";
    private static String TO_MAIL_KEY = "mail.to.email";


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
    public final static String HOST = get(HOST_KEY, "");

    /**
     * 邮件编码
     */
    public final static String CHARSET = get(CHARSET_KEY, "");

    /**
     * 发送者邮箱
     */
    public final static String DEFAULT_FROM_EMAIL = get(USER_NAME_KEY, "");

    /**
     * 发送者密码
     */
    public final static String DEFAULT_FROM_PASSWD = get(PASSWORD_KEY, "");

    /**
     * 发送者名称
     */
    public final static String DEFAULT_FROM_NAME = get(NAME_KEY, "");

    public final static String DEFAULT_TO_NAME = get(TO_MAIL_KEY, "");

    public final static Boolean AUTH = Boolean.valueOf(get(AUTH_KEY, "true"));

    public static String get(String key, String defaultValue) {
        String envValue = System.getenv(key.replaceAll("\\.", "_"));

        if (envValue == null)
            return properties.getProperty(key, defaultValue);

        return envValue;
    }

    public static void main(String[] args) {
        System.out.println(MailCfg.DEFAULT_FROM_EMAIL);
    }

}
