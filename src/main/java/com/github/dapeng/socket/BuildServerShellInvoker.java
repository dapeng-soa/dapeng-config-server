package com.github.dapeng.socket;

import com.corundumstudio.socketio.SocketIOServer;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static com.github.dapeng.socket.SystemParas.SHELLNAME;


/**
 * @author duwupeng on 2016-08-10
 */
public class BuildServerShellInvoker {
    final BlockingQueue queue = new LinkedBlockingQueue();

    public BlockingQueue getQueue() {
        return queue;
    }

    public static void executeShell(SocketIOServer server, String event) throws Exception {
        BufferedReader br = null;
        BufferedWriter wr = null;
        BufferedWriter bw = null;

        try {
            System.out.println("execute command:" + SHELLNAME + " " + event);
            Runtime runtime = Runtime.getRuntime();
            Process process;

            String[] cmd = null;
            String realCmd = null;

            realCmd = SHELLNAME + event.replace("*", "");

            System.out.println("event:" + event);
            System.out.println("cmd: " + realCmd);
            cmd = new String[]{"/bin/sh", "-c", realCmd};

            // 执行Shell命令
            process = runtime.exec(cmd);

            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
            wr = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));

            boolean toBakInLocal = false;
            if (event.equals("build")) {
                toBakInLocal = true;
            }

            if (event.split("\\s+").length > 1) {
                event = event.split("\\s+")[0];
            }

            if (toBakInLocal) {
                //---------记录保存文件夹----------
                final File recordDir = new File(new File(BuildServerShellInvoker.class.getClassLoader().getResource("./").getPath()), "command-record");
                if (!recordDir.exists()) {
                    recordDir.mkdir();
                }
                //---------记录保存文件------------
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss");
                Date date = new Date();
                String dateStr = sdf.format(date);
                File recordFile = new File(recordDir.getAbsolutePath(), event + "." + dateStr + ".txt");
                bw = new BufferedWriter(new FileWriter(recordFile.getAbsoluteFile()));
            }

            String inline;
            while ((inline = br.readLine()) != null) {
                if (toBakInLocal) {
                    bw.write(inline + "\n");
                }
                server.getRoomOperations("web").sendEvent(event + "Event", inline);
                System.out.println(inline);
            }

            br.close();

            br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            while ((inline = br.readLine()) != null) {
                if (toBakInLocal) {
                    bw.write(inline + "\n");
                }
                server.getRoomOperations("web").sendEvent(event + "Event", inline);
                System.out.println(inline);
            }

            if (process != null) {
                process.waitFor();
            }


        } catch (Exception ioe) {
            ioe.printStackTrace();
        } finally {
            if (br != null)
                br.close();
            if (wr != null)
                wr.close();
            if (bw != null) {
                bw.flush();
                bw.close();
            }
        }
    }

    public static void main(String[] args) throws Exception {
//            executeShell(String.format(args[0]));
    }

}

