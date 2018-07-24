package com.github.dapeng.socket;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author with struy.
 * Create by 2018/7/24 16:25
 * email :yq1724555319@gmail.com
 */
public class SocketServer {

    Logger logger = LoggerFactory.getLogger(SocketServer.class);

    private static final int port = 9095;

    public void start() {
        Configuration config = new Configuration();
        config.setPort(port);

        config.setAllowCustomRequests(true);

        Map<String, HostAgent> nodesMap = new ConcurrentHashMap<String, HostAgent>();
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
//                        notifyWebClients(nodesMap, server);
                    }
                }

        );

        server.addEventListener("webEvent", AgentEvent.class, new DataListener<AgentEvent>() {

            @Override
            public void onData(SocketIOClient socketIOClient, AgentEvent agentEvent, AckRequest ackRequest) throws Exception {
                logger.info(" receive webEvent: " + agentEvent.getCmd());

                agentEvent.getClientSessionIds().stream().forEach(event -> {
                    SocketIOClient client = server.getClient(UUID.randomUUID());
                    if (client != null) {
                        client.sendEvent("webCmd", agentEvent);
                    }
                });
            }
        });


        server.start();
        System.out.println("websocket server started at " + port);

        CmdExecutor ex = new CmdExecutor(queue, server);
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

        if (agents.size() > 0 && server.getRoomOperations("web").getClients().size() > 0) {
            server.getRoomOperations("web").sendEvent("serverList", agents);
        }

    }

}
