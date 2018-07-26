package com.github.dapeng.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.dapeng.util.NullUtil.isEmpty;

/**
 * @author with struy.
 * Create by 2018/7/23 15:09
 * email :yq1724555319@gmail.com
 */

public class UnitUtil {
    private static final String REGEX = "\n|\r|\r\n";

    /**
     * 将换行文本转换为Map
     *
     * @return
     */
    public static Map<String, String> ofEnv(String s) {
        Map<String, String> envMap = new HashMap<>(16);
        Arrays.asList(s.split(REGEX)).forEach(s1 -> {
            if (!isEmpty(s1) && s1.contains(":")) {
                String[] s2 = s1.split(":", 2);
                envMap.put(s2[0].trim(), s2[1].trim());
            }
        });
        return envMap;
    }


    /**
     * 将换行文本转换为集合
     *
     * @return
     */
    public static List<String> ofList(String s) {
        return Arrays.asList(s.split(REGEX));
    }



    public static void main(String[] args) {
        String sss = "LANG: zh_CN.UTF-8\n" +
                "      TZ: CST-8\n" +
                "      fluent_bit_enable: \"true\"\n" +
                "      redis_host_ip: redis_host\n" +
                "      redis_host_port: '6379'\n" +
                "      serviceName: idGenService\n" +
                "      slow_service_check_enable: \"true\"\n" +
                "      soa_container_port: '9081'\n" +
                "      soa_core_pool_size: '100'\n" +
                "      soa_jmxrmi_enable: \"false\"\n" +
                "      soa_monitor_enable: \"true\"";
        ofEnv(sss).forEach((k, v) -> {
            System.out.println(k + " ->" + v);
        });
    }
}
