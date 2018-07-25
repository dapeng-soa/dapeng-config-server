package com.github.dapeng.socket.server;

import com.corundumstudio.socketio.SocketIOServer;

import java.util.concurrent.BlockingQueue;

/**
 * Created by duwupeng on 16/10/18.
 */
public class CmdExecutor implements Runnable{
    public BlockingQueue queue;
    SocketIOServer server;
    public CmdExecutor(BlockingQueue queue, SocketIOServer server) {
        this.queue = queue;
        this.server=server;
    }

    @Override
    public void run() {
        while(true) {
            try {
                String event  = (String)queue.take();
                System.out.println("Consumed: " + event);

                server.getRoomOperations("web").sendEvent(event + "Event","started");

                BuildServerShellInvoker.executeShell(server, event);

                server.getRoomOperations("web").sendEvent(event + "Event","end");
            } catch (Exception ex) {
               ex.printStackTrace();
            }
        }
    }
}
