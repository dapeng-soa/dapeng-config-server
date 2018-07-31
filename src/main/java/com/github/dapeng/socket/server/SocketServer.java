package com.github.dapeng.socket.server;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.github.dapeng.socket.AgentEvent;
import com.github.dapeng.socket.HostAgent;
import com.github.dapeng.socket.enums.EventType;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author with struy.
 * Create by 2018/7/24 16:25
 * email :yq1724555319@gmail.com
 */
public class SocketServer {

    Logger logger = LoggerFactory.getLogger(SocketServer.class);

    private static final int port = 9095;

    public void start() {
        new Thread(this::init).start();
    }

    public void init() {
        Configuration config = new Configuration();
        config.setPort(port);
        config.setHostname("127.0.0.1");

        config.setAllowCustomRequests(true);

        Map<String, HostAgent> nodesMap = new ConcurrentHashMap<String, HostAgent>();
        Map<String, Long> serverDeployTime = new ConcurrentHashMap<>();
        Map<String, HostAgent> webClientMap = new ConcurrentHashMap<String, HostAgent>();

        final SocketIOServer server = new SocketIOServer(config);
        final BlockingQueue queue = new LinkedBlockingQueue();


        server.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient socketIOClient) {
                System.out.println(String.format(socketIOClient.getRemoteAddress() + " --> join room %s", socketIOClient.getSessionId()));
            }
        });

        server.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient socketIOClient) {
                if (nodesMap.containsKey(socketIOClient.getSessionId().toString())) {
                    socketIOClient.leaveRoom("nodes");
                    nodesMap.remove(socketIOClient.getSessionId().toString());

                    System.out.println(String.format("leave room  nodes %s", socketIOClient.getSessionId()));
                    notifyWebClients(nodesMap, server);
                } else {
                    socketIOClient.leaveRoom("web");
                    System.out.println(String.format("leave room web  %s", socketIOClient.getSessionId()));
                }
            }
        });

        server.addEventListener("nodeReg", String.class, new DataListener<String>() {
                    @Override
                    public void onData(SocketIOClient client,
                                       String data, AckRequest ackRequest) {
                        client.joinRoom("nodes");
                        System.out.println("nodes Reg");
                        String name = data.split(":")[0];
                        String ip = data.split(":")[1];
                        nodesMap.put(client.getSessionId().toString(), new HostAgent(name, ip, client.getSessionId().toString()));
                        notifyWebClients(nodesMap, server);
                    }
                }

        );


        server.addEventListener("webReg", String.class, new DataListener<String>() {
                    @Override
                    public void onData(SocketIOClient client,
                                       String data, AckRequest ackRequest) {
                        client.joinRoom("web");
                        logger.info("web Reg..." + client.getSessionId());
                        String name = data.split(":")[0];
                        String ip = data.split(":")[1];
                        webClientMap.put(client.getSessionId().toString(), new HostAgent(name, ip, client.getSessionId().toString()));
                        notifyWebClients(nodesMap, server);
                    }
                }

        );

        server.addEventListener(EventType.WEB_EVENT().name(), String.class, new DataListener<String>() {

            @Override
            public void onData(SocketIOClient socketIOClient, String agentEvent, AckRequest ackRequest) throws Exception {
               // logger.info(" receive webEvent: " + agentEvent.getCmd());
                System.out.println("==================================================");
                System.out.println(" agentEvent: " + agentEvent);

                AgentEvent agentEventObj = new Gson().fromJson(agentEvent, AgentEvent.class);
                System.out.println(" agentEventObj: " + agentEventObj);

                agentEventObj.getClientSessionIds().forEach(sessionId -> {
                    SocketIOClient client = server.getClient(UUID.fromString(sessionId));
                    if (client != null) {
                        client.sendEvent(EventType.WEB_EVENT().name(), agentEvent);
                    } else {
                        logger.error(" Failed to get socketClient......");
                    }
                });
            }
        });

        //发送指令给agent获取当前节点的部署时间
        server.addEventListener(EventType.GET_SERVER_TIME().name(), String.class, new DataListener<String>() {
                    @Override
                    public void onData(SocketIOClient client,
                                       String data, AckRequest ackRequest) {
                        logger.info(" received serverTime cmd.....");
                        serverDeployTime.clear();
                        server.getRoomOperations("nodes").sendEvent(EventType.GET_SERVER_TIME().name(),data);
                    }
                }
        );

        //获取到agent返回的时间，并转发给web节点
        server.addEventListener(EventType.SERVER_TIME().name(), String.class, new DataListener<String>() {
                    @Override
                    public void onData(SocketIOClient client,
                                       String data, AckRequest ackRequest) {
                        logger.info(" received serverTime cmd.....");
                        String[] tempData = data.split(":");
                        String ip = tempData[0];
                        String time = tempData[1];
                        serverDeployTime.put(ip,Long.valueOf(time));
                        if (serverDeployTime.size() == nodesMap.size()) {
                            server.getRoomOperations("web").sendEvent(EventType.SERVER_TIME().name(), serverDeployTime);
                        }
                    }
                }
        );

        server.addEventListener(EventType.DEPLOY().name(), String.class, new DataListener<String>() {
            @Override
            public void onData(SocketIOClient client,
                               String data, AckRequest ackRequest) {
                server.getRoomOperations("nodes").sendEvent(EventType.DEPLOY().name(), data);
            }
        });


        server.start();
        System.out.println("websocket server started at " + port);

        ServiceCmdExecutor ex = new ServiceCmdExecutor(queue, server);
        System.out.println("CmdExecutor Thread started");


        new Thread(ex).start();
        try {

            Thread.sleep(Integer.MAX_VALUE);
        } catch (Exception e) {
            logger.error(" Failed to sleep.." + e.getMessage(), e);
        }

        server.stop();


    }

    public void stop() {

    }


    private void notifyWebClients(Map<String, HostAgent> map, SocketIOServer server) {
        Collection<HostAgent> agents = map.values();

        logger.info(" current agent clients size: " + agents.stream().map(i -> i.getIp()));

        server.getRoomOperations("nodes").sendEvent("serverList", agents);

    }

}
