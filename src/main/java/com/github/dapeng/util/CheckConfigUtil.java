package com.github.dapeng.util;

import com.github.dapeng.core.helper.IPUtils;
import com.github.dapeng.router.Route;
import com.github.dapeng.router.RoutesExecutor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author with struy.
 * Create by 2018/6/6 12:05
 * email :yq1724555319@gmail.com
 */

public class CheckConfigUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(CheckConfigUtil.class);

    // 限流格式检查
    public static Boolean doCheckFreq(String ruleData) {
        LOGGER.debug("doParseRuleData,限流规则解析前：{}", ruleData);
        List<FreqControlRule> datasOfRule = new ArrayList<>();
        String[] str = ruleData.split("\n|\r|\r\n");
        String pattern1 = "^\\[.*\\]$";
        String pattern2 = "^[a-zA-Z]+\\[.*\\]$";

        for (int i = 0; i < str.length; ) {
            if (Pattern.matches(pattern1, str[i])) {
                FreqControlRule rule = new FreqControlRule();
                rule.targets = new HashSet<>();

                while (!Pattern.matches(pattern1, str[++i])) {
                    if ("".equals(str[i].trim())) continue;
                    String[] s = str[i].split("=");
                    switch (s[0].trim()) {
                        case "match_app":
                            rule.app = s[1].trim();
                            break;
                        case "rule_type":
                            if (Pattern.matches(pattern2, s[1].trim())) {
                                rule.ruleType = s[1].trim().split("\\[")[0];
                                String[] str1 = s[1].trim().split("\\[")[1].trim().split("\\]")[0].trim().split(",");
                                for (int k = 0; k < str1.length; k++) {
                                    if (!str1[k].contains(".")) {
                                        rule.targets.add(Integer.parseInt(str1[k].trim()));
                                    } else {
                                        rule.targets.add(IPUtils.transferIp(str1[k].trim()));
                                    }
                                }
                            } else {
                                rule.targets = null;
                                rule.ruleType = s[1].trim();
                            }
                            break;
                        case "min_interval":
                            rule.minInterval = Integer.parseInt(s[1].trim().split(",")[0]);
                            rule.maxReqForMinInterval = Integer.parseInt(s[1].trim().split(",")[1]);
                            break;
                        case "mid_interval":
                            rule.midInterval = Integer.parseInt(s[1].trim().split(",")[0]);
                            rule.maxReqForMidInterval = Integer.parseInt(s[1].trim().split(",")[1]);
                            break;
                        case "max_interval":
                            rule.maxInterval = Integer.parseInt(s[1].trim().split(",")[0]);
                            rule.maxReqForMaxInterval = Integer.parseInt(s[1].trim().split(",")[1]);
                            break;
                        default:
                            break;
                    }
                    if (i == str.length - 1) {
                        i++;
                        break;
                    }
                }
                if (rule.app == null || rule.ruleType == null ||
                        rule.minInterval == 0 ||
                        rule.midInterval == 0 ||
                        rule.maxInterval == 0) {
                    LOGGER.error("doParseRuleData, 限流规则解析失败。rule:{}", rule);
                    return false;
                }
                datasOfRule.add(rule);
            } else {
                i++;
            }
        }
        LOGGER.debug("doParseRuleData,限流规则解析后：{}", datasOfRule);
        return true;
    }

    //路由格式检查
    public static Boolean doCheckRouter(String routeData) {
        List<Route> zkRoutes = null;
        if (StringUtils.isNotBlank(routeData)) {
            try {
                zkRoutes = RoutesExecutor.parseAll(routeData);
            } catch (Exception e) {
                zkRoutes = new ArrayList<>(16);
                LOGGER.error("parser routes 信息 失败，请检查路由规则写法是否正确!");
            }
        }
        return zkRoutes != null && !zkRoutes.isEmpty();
    }

    //配置格式检查
    public static Boolean doCheckConfig(String configData) {
        //全局配置，格式  timeout/800ms;loadBalance/random
        //服务级别和方法级别   timeout/800ms,register:4001ms,modifySupplier:200ms;loadBalance/leastActive,createSupplier:random,modifySupplier:roundRobin;
        if (StringUtils.isNotBlank(configData)) {
            String[] configArr = configData.split(";");
            for (String item : configArr) {
                if (item.contains(",")) {
                    String[] config_item = item.split(",");
                    for (String _item : config_item) {
                        if (!(_item.contains("/") || _item.contains(":"))) {
                            return false;
                        }
                    }

                } else {
                    if (!item.contains("/")) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
