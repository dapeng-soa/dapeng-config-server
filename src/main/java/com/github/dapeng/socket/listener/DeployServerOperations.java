package com.github.dapeng.socket.listener;

import com.github.dapeng.socket.SystemParas;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONObject;

import java.util.concurrent.BlockingQueue;

import static com.github.dapeng.socket.SystemParas.COMMAS;

/**
 * @author duwupeng
 * @date
 */
public class DeployServerOperations implements Emitter.Listener{
    Socket socket;
    BlockingQueue<String> queue;
    public DeployServerOperations(BlockingQueue<String> queue , Socket socket){
        this.socket = socket;
        this.queue = queue;
    }
    @Override
    public void call(Object... objects) {
//        JSONObject msg = (JSONObject)objects[0];
//        System.out.println("msg: " + msg);
//        try{
//            String cmd=((String)msg.get("cmd"));
//
//            if (cmd.equalsIgnoreCase(SystemParas.AllowedCopmmand.DEPLOY.name())||cmd.equalsIgnoreCase(SystemParas.AllowedCopmmand.ROLLBACK.name())){
//                cmd = cmd+ COMMAS + msg.getString("branchName");
//            }else if(cmd.equalsIgnoreCase(SystemParas.AllowedCopmmand.SERVICERESTART.name())|| cmd.equalsIgnoreCase(SystemParas.AllowedCopmmand.SERVICESTOP.name())){
//                cmd = cmd+ COMMAS + msg.getString("serviceName");
//            }
//
//            queue.put(cmd);
////            if(cmd.startsWith("serviceStop")||cmd.startsWith("serviceRestart")){
////                queue.put("services");
////            }
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        String agentEvent = (String)objects[0];
        String[] values = agentEvent.split(" ");
        String sessionId = values[0];
        String cmd = values[1];
        String serviceName = values[2];
        String content = values[3];

        //queue.add(agentEvent);
        try {
            queue.put(agentEvent);
        } catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println(" just for test sessionId " + sessionId + " cmd: " + cmd + " serviceName: " + serviceName + " content: " + content);
    }
}
