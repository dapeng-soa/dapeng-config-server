package com.github.dapeng.socket.client;

import io.socket.client.Socket;

import java.util.concurrent.BlockingQueue;

/**
 * Created by duwupeng on 16/10/18.
 */
public class CmdExecutor implements Runnable{
    public BlockingQueue queue;
    Socket socket;
    public CmdExecutor(BlockingQueue queue,Socket socket) {
        this.queue = queue;
        this.socket=socket;
    }

    @Override
    public void run() {
            while(true) {
                try {
                    String  event = (String)queue.take();
                    System.out.println("Consumed Event " + event);

                    socket.emit("nodeEvent","started");

                    //DeployServerShellInvoker.executeShell(socket, event);

                    socket.emit("nodeEvent","end");
                } catch (Exception ex) {
                   ex.printStackTrace();
                }
            }
    }
}
