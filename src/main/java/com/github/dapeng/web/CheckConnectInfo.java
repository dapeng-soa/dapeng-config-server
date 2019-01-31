package com.github.dapeng.web;

import com.github.dapeng.client.netty.NettyClient;
import com.github.dapeng.client.netty.NettyClientFactory;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author: zhup
 * @Date: 2019/1/31 15:38
 */

public class CheckConnectInfo {

    private static Logger LOGGER = LoggerFactory.getLogger(CheckConnectInfo.class);

    public static Set<String> set = new HashSet<>();

    public void init() {
        final Thread checkThread = new Thread("check-connectInfo-thread") {
            @Override
            public void run() {
                try {
                    if(!set.isEmpty()){
                        checkConnectInfo();
                    }
                } catch (Exception e) {
                    LOGGER.error("Check-connectInfo error", e);
                }
            }
        };
        checkThread.start();
    }

    private void checkConnectInfo() {
        try {
            NettyClient nettyClient = NettyClientFactory.getNettyClient();
            Iterator<String> iterator = set.iterator();
            while(iterator.hasNext()){
                String ipPort = iterator.next();
                String[] split = ipPort.split(":");
                Channel connect = nettyClient.connect(split[0], Integer.parseInt(split[1]));
                if(connect!=null){
                    iterator.remove();
                    connect.close();
                }
            }
            Thread.sleep(1000*60*10);
        } catch (InterruptedException e) {
            LOGGER.error("check connect info error");
            //e.printStackTrace();
        }
    }

}
