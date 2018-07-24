package com.github.dapeng.socket.client;//package com.deploy.client;
//
//import io.socket.client.Socket;
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//
//import static com.isuwang.operations.contex.SystemParas.*;
//
///**
// * @author duwupeng on 2016-08-10
// */
//public class DeployServerShellInvoker {
//
//    public static void executeShell(Socket socket,String event) throws Exception{
//        BufferedReader br = null;
//        BufferedWriter wr = null;
//        BufferedWriter bw = null;
//
//
//            boolean isPs=false;
//            if(event.equalsIgnoreCase(AllowedCopmmand.SERVICES.name())){
//                isPs=true;
//            }
//
//        try {
//            System.out.println("execute command:" + SHELLNAME+" "+event);
//            Runtime runtime = Runtime.getRuntime();
//            Process process;
//
//            String[] cmd = null;
//            String realCmd =null;
//            if(event.indexOf(COMMAS)!=-1){
//                realCmd = SHELLNAME + event.replaceAll(COMMAS," ").replace("*","");
//                event=event.split(COMMAS)[0];
//            }else{
//                realCmd = SHELLNAME +" "+ event;
//            }
//            System.out.println("event:" + event);
//            System.out.println("cmd: " + realCmd);
//            cmd = new String[]{ "/bin/sh", "-c",realCmd };
//
//
//
//            // 执行Shell命令
//            process = runtime.exec(cmd);
//
//            br = new BufferedReader(new InputStreamReader(process.getInputStream()));
//            wr = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
//
//
//            if(isPs){
//                String inline;
//                while ((inline = br.readLine()) != null) {
//                    if(inline.indexOf("Name")!=-1 &&inline.indexOf("Command")!=-1 && inline.indexOf("State")!=-1){
//                        continue;
//                    }
//                    if(inline.indexOf("-------------------------------------------------------")!=-1){
//                        continue;
//                    }
//                    if(inline.indexOf("osType:LINUX")!=-1){
//                        continue;
//                    }
//
//                    socket.emit("nodeEvent",event+":"+inline);
//                    System.out.println(inline);
//                }
//                br.close();
//            }else{
//                String inline;
//                while ((inline = br.readLine()) != null) {
//                    socket.emit("nodeEvent",event+":"+inline);
//                    System.out.println(inline);
//                }
//                br.close();
//
//
//
//                br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//                while ((inline = br.readLine()) != null) {
//                    System.out.println(inline);
//                }
//            }
//
//
//
//            if (process != null) {
//                process.waitFor();
//            }
//
//        } catch (Exception ioe) {
//            ioe.printStackTrace();
//        }finally {
//            if (br != null)
//                br.close();
//            if (wr != null)
//                wr.close();
//            if (bw != null) {
//                bw.flush();
//                bw.close();
//            }
//        }
//    }
//        public static void main(String[] args) throws Exception{
//
//            System.out.println("agent.sh  deploy;* PRODUCT".replaceAll(COMMAS, " ").replace("*", ""));
//        }
//
//}
//
