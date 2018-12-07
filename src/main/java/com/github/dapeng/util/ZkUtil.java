package com.github.dapeng.util;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.apache.zookeeper.ZooKeeper.States.CONNECTED;

/**
 * @author with struy.
 * Create by 2018/6/21 17:21
 * email :yq1724555319@gmail.com
 */

public class ZkUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZkUtil.class);


    /**
     * 根据host连接zk
     *
     * @param host
     * @return
     * @throws Exception
     */
    public static ZooKeeper createZkByHost(String host) throws Exception {
        CountDownLatch semaphore = new CountDownLatch(1);
        ZooKeeper zkClient = null;
        try {
            /*
             * ZooKeeper客户端和服务器会话的建立是一个异步的过程
             * 构造函数在处理完客户端的初始化工作后立即返回，在大多数情况下，并没有真正地建立好会话
             * 当会话真正创建完毕后，Zookeeper服务器会向客户端发送一个事件通知
             */
            zkClient = new ZooKeeper(host, 500, (event) -> {
                LOGGER.info("waiting  连接 Zk ....");
                if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                    semaphore.countDown();
                }
            });
            LOGGER.info("build zk connect state1[{}]...", zkClient.getState());
            //semaphore.await();
            semaphore.await(1000, TimeUnit.MILLISECONDS);
            LOGGER.info("build zk connect state2[{}]...", zkClient.getState());
            LOGGER.info("build zk connect on [{}]...", host);
        } catch (Exception e) {
            LOGGER.info(e.getMessage(), e);
        }
        if (Objects.nonNull(zkClient) && zkClient.getState() == CONNECTED) {
            return zkClient;
        } else {
            if (zkClient != null) {
                zkClient.close();
            }
            LOGGER.info("ZK build connect on [{}] failed ...", host);
            throw new Exception("ZK build connect on [" + host + "] failed ...");
        }
    }

    //==============================================检查节点是否存在
    private static boolean checkExists(ZooKeeper zooKeeper, String path) {
        try {
            Stat exists = zooKeeper.exists(path, false);
            if (exists != null) {
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("check path status error");
        }
        return false;
    }


    private static Stat exists(ZooKeeper zooKeeper, String path) {
        Stat stat = null;
        try {
            stat = zooKeeper.exists(path, false);
        } catch (KeeperException | InterruptedException e) {
        }
        return stat;
    }


    //==============================================添加节点
    public static synchronized void createNode(ZooKeeper zooKeeper, String path, boolean ephemeral) throws Exception {

        int i = path.lastIndexOf("/");
        if (i > 0) {
            String parentPath = path.substring(0, i);
            //判断父节点是否存在...
            if (!checkExists(zooKeeper, parentPath)) {
                createNode(zooKeeper, parentPath, false);
            }
        }
        if (ephemeral) {
            // 创建临时节点

        } else {
            Stat stat = exists(zooKeeper, path);
            if (stat == null) {
                zooKeeper.create(path, "".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                LOGGER.info("create persistent node [{}] successful", path);
            }
        }
    }

    //==============================================添加节点data

    public static synchronized void createData(ZooKeeper zooKeeper, String path, String data) throws Exception {
        createNode(zooKeeper,path, false);
        if (checkExists(zooKeeper, path)) {
            LOGGER.info(" start to set data from: " + path);
            zooKeeper.setData(path, data.getBytes(), -1, null, data);
        }
    }

    //==============================================删除节点
    public static synchronized void delNode(ZooKeeper zooKeeper, String path) throws Exception {

        if (checkExists(zooKeeper, path)) {
            LOGGER.info("remove node is::" + path);
            zooKeeper.delete(path, -1);
        } else {
            LOGGER.info("zk node ::" + path + "not found");
        }
    }


    //==============================================无需watch获取节点data
    public static synchronized String getNodeData(ZooKeeper zooKeeper, String path) throws Exception {

        LOGGER.info(" get node data from: " + path);
        if (checkExists(zooKeeper, path)) {
            byte[] data = zooKeeper.getData(path, false, null);
            if (data.length > 0) {
                return new String(data, "utf-8");
            }
        } else {
            LOGGER.info("zk node ::" + path + "not found");
        }
        return "";
    }

    //==============================================无需watch获取zk节点数
    public static synchronized List<String> getNodeChildren(ZooKeeper zooKeeper, String path,Boolean watch) {
        try {
            if (checkExists(zooKeeper, path)) {
                LOGGER.info("get zk NodeChild on [{}]", path);
                return zooKeeper.getChildren(path, watch);
            }
        } catch (KeeperException | InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }

    //==============================================关闭连接
    public static synchronized void closeZk(ZooKeeper zooKeeper) {
        if (zooKeeper != null) {
            try {
                zooKeeper.close();
                LOGGER.info("zk close successful ..");
            } catch (InterruptedException e) {
                LOGGER.error("zk close error .. {}", e);
            }
        }
    }

}
