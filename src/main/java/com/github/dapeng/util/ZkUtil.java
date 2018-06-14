package com.github.dapeng.util;

import com.github.dapeng.openapi.cache.ZookeeperClient;
import com.github.dapeng.openapi.utils.EnvUtil;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * @author with struy.
 * Create by 2018/6/3 17:53
 * email :yq1724555319@gmail.com
 */

public class ZkUtil {
    private static final Logger logger = LoggerFactory.getLogger(ZkUtil.class);

    public static ZookeeperClient getCurrInstance() {
        String zkHost = EnvUtil.prepareEnv();
        return ZookeeperClient.getCurrInstance(zkHost);
    }

    public static ZooKeeper createZooKeeperClient(String host) {
        CountDownLatch semaphore = new CountDownLatch(1);
        ZooKeeper zkClient = null;
        try {
            /**
             * ZooKeeper客户端和服务器会话的建立是一个异步的过程
             * 构造函数在处理完客户端的初始化工作后立即返回，在大多数情况下，并没有真正地建立好会话
             * 当会话真正创建完毕后，Zookeeper服务器会向客户端发送一个事件通知
             */
            zkClient = new ZooKeeper(host, 5000, (event) -> {
                //System.out.println("回调watcher实例： 路径" + event.getPath() + " 类型：" + event.getType());
                if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    semaphore.countDown();
                }
            });
            logger.info("build zk connect state1[{}]...", zkClient.getState());
            semaphore.await();
            logger.info("build zk connect state2[{}]...", zkClient.getState());
            logger.info("build zk connect on [{}]...", host);
        } catch (Exception e) {
            logger.info(e.getMessage(), e);
        }
        return zkClient;
    }

    public static List<String> getChildren(ZooKeeper zooKeeper, String path, boolean watcher) {
        try {
            return zooKeeper.getChildren(path, watcher);
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
