package com.github.dapeng.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author with struy.
 * Create by 2018/7/29 17:27
 * email :yq1724555319@gmail.com
 */

public class DeployCheck {

    private static final String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
    private static final Pattern pattern = Pattern.compile(ip);

    public static void hasChinese(String s,String fieldName) throws Exception{
        if (s.getBytes().length != s.length()) {
            throw new Exception(fieldName+"不能含有中文");
        }
    }

    /** * 判断是否为合法IP * @return the ip */
    public static void isboolIp(String ipAddress) throws Exception {
        Matcher matcher = pattern.matcher(ipAddress);
        if (!matcher.matches()){
            throw new Exception("IP格式错误");
        }
    }

}
