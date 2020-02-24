package com.github.dapeng.client.netty;

import com.github.dapeng.core.SoaException;
import com.github.dapeng.web.CheckConnectInfo;
import com.github.dapeng.web.ServiceMonitorController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author huyj
 * @Created 2018-08-21 16:40
 */
public class SubPoolFactory {

    private static Logger LOGGER = LoggerFactory.getLogger(SubPoolFactory.class);

    private final static ConcurrentHashMap<IpPort, SubPool> subPoolsMap = new ConcurrentHashMap(16);
    private static final ReentrantLock subPoolLock = new ReentrantLock();

    public static SubPool getSubPool(String ip, int port) {
        IpPort ipPort = new IpPort(ip, port);
        SubPool subPool = subPoolsMap.get(ipPort);
        if (subPool == null) {
            try {
                subPoolLock.lock();
                subPool = subPoolsMap.get(ipPort);
                if (subPool == null) {
                    try {
                        subPool = new SubPool(ip, port);
                        subPoolsMap.put(ipPort, subPool);
                    } catch (Exception e) {
                        CheckConnectInfo.set.add(ip + ":" + port);
                        LOGGER.error("创建SubPool连接异常");
                    }
                }
            } finally {
                subPoolLock.unlock();
            }
        }
        return subPool;
    }
}
