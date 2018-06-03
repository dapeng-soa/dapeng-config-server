package com.github.dapeng.util;

import com.github.dapeng.openapi.cache.ZookeeperClient;
import com.github.dapeng.openapi.utils.EnvUtil;

/**
 * @author with struy.
 * Create by 2018/6/3 17:53
 * email :yq1724555319@gmail.com
 */

public class ZkUtil {

    public static ZookeeperClient getCurrInstance(){
        String zkHost = EnvUtil.prepareEnv();
        return ZookeeperClient.getCurrInstance(zkHost);
    }
}
