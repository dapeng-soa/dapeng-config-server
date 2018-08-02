package com.github.dapeng.socket;

import com.github.dapeng.socket.client.CmdExecutor;
import com.github.dapeng.socket.enums.EventType;
import io.socket.client.IO;
import io.socket.client.Socket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URISyntaxException;
import java.util.concurrent.LinkedBlockingQueue;

public class SocketUtil {

    static Logger logger = LoggerFactory.getLogger(SocketUtil.class);

    public static Socket registerWebSocketClient(String serverIp,int serverPort, String clientIp, String hostName) {
        IO.Options options = new IO.Options();
        options.forceNew = true;
        LinkedBlockingQueue queue = new LinkedBlockingQueue();
        try {
            Socket socketClient = IO.socket("http://" + serverIp + ":" + serverPort, options);

            socketClient.on(Socket.EVENT_CONNECT, objects -> {
                System.out.println(" connected to server: socketClient: " + socketClient.id());
                //Register nodeReg to server
                //fixme should be web ip
                socketClient.emit(EventType.WEB_REG().name(), hostName + ":" + clientIp);
            });

            CmdExecutor cmdExecutor = new CmdExecutor(queue, socketClient);
            //独立线程处理命令
            new Thread(cmdExecutor).start();

            socketClient.connect();
            return socketClient;
        } catch (URISyntaxException e) {
            logger.info(" failed to connected socketClient.......");
            return null;
        }
    }
}
