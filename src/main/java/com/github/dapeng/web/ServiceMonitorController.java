package com.github.dapeng.web;

import com.github.dapeng.client.netty.RequestUtils;
import com.github.dapeng.core.helper.IPUtils;
import com.github.dapeng.core.helper.SoaSystemEnvProperties;
import com.github.dapeng.openapi.utils.ZkUtil;
import com.github.dapeng.util.GetServiceMonitorThread;
import com.github.dapeng.vo.MonitorHosts;
import com.github.dapeng.vo.ServiceMonitorListVo;
import com.github.dapeng.vo.ServiceMonitorVo;
import com.github.dapeng.vo.Subservices;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.apache.zookeeper.ZooKeeper;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: zhup
 * @Date: 2018/8/1 11:37
 */

@RestController
@RequestMapping("/serviceMonitor")
public class ServiceMonitorController {

    private static Logger LOGGER = LoggerFactory.getLogger(ServiceMonitorController.class);

    private static final String SOA_ZOOKEEPER_HOST = "soa_zookeeper_host";

    @Resource
    private EntityManager entityManager;

    @ResponseBody
    @RequestMapping("/list")
    public Object serviceMonitorList(){
        String echoInfo = RequestUtils.getRomoteServiceEcho("192.168.4.102", 9095,"com.github.dapeng.hello.service.HelloService", "1.0.0");
        System.out.println(echoInfo);
        Map<String,Map<String,Map>> resultMap = new HashMap<>();
        /*List<ServiceMonitorVo> baseServiceList = getBaseServiceList();
        List<ServiceMonitorListVo> monitorList = getServiceMonitorList(baseServiceList);
        Map<String,Map<String,Map>> resultMap = resultMap(monitorList);*/
        return resultMap;
    }


    /**
     * 节点信息汇总
     * @param monitorList
     * @return
     */
    public Map<String,Map<String,Map>> resultMap(List<ServiceMonitorListVo> monitorList){
            Map<String,Map<String,Map>> resultMap = new HashMap<>();
        try {
            List<JsonObject> jsonObjectList = new ArrayList<>();
            ExecutorService threadPool = Executors.newCachedThreadPool();
            CompletionService<String> completionService = new ExecutorCompletionService<String>(threadPool);
            int k=0;
            for(int i=0;i<monitorList.size();i++){
                ServiceMonitorListVo smlv  = monitorList.get(i);
                List<MonitorHosts> hosts = smlv.getHosts();
                List<Subservices> subservices = smlv.getSubservices();
                for(int j=0;j<hosts.size();j++){
                    k++;
                    MonitorHosts monitorHosts = hosts.get(j);
                    GetServiceMonitorThread getServiceMonitorThread = new GetServiceMonitorThread(monitorHosts.getIp(),Integer.parseInt(monitorHosts.getPort()),subservices.get(i).getName(),subservices.get(i).getVersion());
                    completionService.submit(getServiceMonitorThread);
                }
            }
            threadPool.shutdown();
            for(int i=0;i<k;i++){
                try {
                    String takeStr = completionService.take().get();
                    JsonObject asJsonObject = new JsonParser().parse(takeStr).getAsJsonObject();
                    jsonObjectList.add(asJsonObject);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for(int i=0;i<jsonObjectList.size();i++){
                JsonObject object = jsonObjectList.get(i);
                String serviceName = object.get("service").getAsString();
                JsonObject serviceObject = object.get(serviceName).getAsJsonObject();
                JsonObject taskInfoObject = serviceObject.get("taskInfo").getAsJsonObject();
                JsonObject GcInfoObject = serviceObject.get("gcInfos").getAsJsonObject();
                JsonObject flowsObject = serviceObject.get("flows").getAsJsonObject();
                //相同节点数累加
                if(resultMap.containsKey(serviceName)){
                    Map tasksMap = resultMap.get("tasks");
                    Map GcMap = resultMap.get("gcInfos");
                    Map flowsMap = resultMap.get("flows");
                    tasksMap.put("waitingQueue",taskInfoObject.get("waitingQueue").getAsInt()+Integer.parseInt(tasksMap.get("waitingQueue").toString()));
                    tasksMap.put("total",taskInfoObject.get("total").getAsInt()+Integer.parseInt(tasksMap.get("total").toString()));
                    tasksMap.put("succeed",taskInfoObject.get("succeed").getAsInt()+Integer.parseInt(tasksMap.get("succeed").toString()));

                    GcMap.put("minorGc",GcInfoObject.get("minorGc").getAsInt()+Integer.parseInt(GcMap.get("minorGc").toString()));
                    GcMap.put("majorGc",GcInfoObject.get("majorGc").getAsInt()+Integer.parseInt(GcMap.get("majorGc").toString()));

                    flowsMap.put("max",flowsObject.get("max").getAsInt()+Integer.parseInt(flowsMap.get("max").toString()));
                    flowsMap.put("min",flowsObject.get("min").getAsInt()+Integer.parseInt(flowsMap.get("min").toString()));
                    flowsMap.put("avg",flowsObject.get("avg").getAsInt()+Integer.parseInt(flowsMap.get("avg").toString()));
                }else{
                    Map<String,Map> valueMap = new HashMap<>();
                    Map<String,Object> tasksMap = new HashMap();
                    Map<String,Object> GcMap = new HashMap();
                    Map<String,Object> flowsMap = new HashMap();
                    tasksMap.put("waitingQueue",taskInfoObject.get("waitingQueue").getAsInt());
                    tasksMap.put("total",taskInfoObject.get("total").getAsInt());
                    tasksMap.put("succeed",taskInfoObject.get("succeed").getAsInt());
                    GcMap.put("minorGc",GcInfoObject.get("minorGc").getAsInt());
                    GcMap.put("majorGc",GcInfoObject.get("majorGc"));
                    flowsMap.put("max",flowsObject.get("max").getAsInt());
                    flowsMap.put("min",flowsObject.get("min").getAsInt());
                    flowsMap.put("avg",flowsObject.get("avg").getAsInt());
                    valueMap.put("tasks",tasksMap);
                    valueMap.put("gcInfos",GcMap);
                    resultMap.put(serviceName,valueMap);
                }

                /**
                 * 统计服务节点数

                 for(int j=0;j<monitorList.size();j++){
                 ServiceMonitorListVo serviceMonitorListVo = monitorList.get(j);
                 if(serviceMonitorListVo.getService().equals(serviceName)){
                 if(resultMap.containsKey(serviceName)){
                 Map<String,Object> serviceMap= resultMap.get(serviceName);
                 serviceMap.put("nodeCount",Integer.parseInt(serviceMap.get("nodeCount").toString()));
                 }
                 }
                 }
                 */

            }
            //服务节点数统计
            for(Map.Entry<String,Map<String,Map>> map :resultMap.entrySet()){
                String serviceName = map.getKey().toString();
                Map<String,Map> resMap = map.getValue();
                for(int i=0;i<monitorList.size();i++){
                    ServiceMonitorListVo serviceMonitorListVo = monitorList.get(i);
                    if(serviceMonitorListVo.getService().equals(serviceName)){
                        Map nodesmap = resMap.get("node");
                        nodesmap.put("nodeCount",Integer.parseInt(nodesmap.get("nodeCount").toString()+1));
                    }else{
                        Map nodesmap = new HashMap();
                        nodesmap.put("nodeCount",1);
                        resMap.put("node",nodesmap);
                    }
                }
            }
            /**
             * {
             "gcInfos": {
             "gcInfos": "0/0"
             },
             "flows": {},
             "service": "com.github.dapeng.hello.service.HelloService",
             "tasks": {
             "waitingQueue": 0,
             "total": 48,
             "succeed": 48
             },
             "errors": {},
             "serviceInfo":{
             "className":"value"
             }
             }
             */

       /* String echoInfo =RequestUtils.getRomoteServiceEcho("192.168.4.102", 9095,"com.github.dapeng.hello.service.HelloService", "1.0.0");
        System.out.println(echoInfo);*/
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    /**
     * 获取节点信息
     * @return
     */
    private List<ServiceMonitorVo> getBaseServiceList() {
        Query nativeQuery = entityManager.createNativeQuery("\n" +
                "select tdu.service_id AS serviceId,ts.name AS serviceName,group_concat(th.ip) AS ipList, ts.env \n" +
                " from t_deploy_unit tdu left join t_host th on tdu.host_id=th.id\n" +
                "                                left join t_service ts on tdu.service_id=ts.id\n" +
                "                                group by tdu.service_id,ts.name\n");
        nativeQuery.unwrap(SQLQuery.class)
                .addScalar("serviceId", StandardBasicTypes.LONG)
                .addScalar("serviceName", StandardBasicTypes.STRING)
                .addScalar("ipList", StandardBasicTypes.STRING)
                .addScalar("env", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(ServiceMonitorVo.class));
        return nativeQuery.getResultList();
    }

    /**
     * 获取zk节点信息
     * @param baseServiceList
     * @return
     */
    private List<ServiceMonitorListVo> getServiceMonitorList(List<ServiceMonitorVo> baseServiceList) {
        String zkHost = SoaSystemEnvProperties.get(SOA_ZOOKEEPER_HOST, "10.10.10.45:2181");
        List<ServiceMonitorListVo> dataList = new ArrayList<>();
        ZooKeeper zkByHost;
        try {
            zkByHost = ZkUtil.createZkByHost(zkHost);
            List<String> nodeData = ZkUtil.getNodeChildren(zkByHost, "/soa/runtime/services", false);
            for (int i = 0; i < baseServiceList.size(); i++) {
                ServiceMonitorListVo smvo = new ServiceMonitorListVo();
                ServiceMonitorVo smv = (ServiceMonitorVo) baseServiceList.get(i);
                String[] ipArrs = smv.getIpList().split(",");
                List<MonitorHosts> hostsList = new ArrayList();
                List<Subservices> ssv = new ArrayList<>();
                Pattern pattern = Pattern.compile("soa_container_port=(\\d.*)");
                Matcher m = pattern.matcher(smv.getEnv());
                String port = null;
                while (m.find()) {
                    port = m.group(1);
                }
                for (String ip : ipArrs) {
                    MonitorHosts hosts = new MonitorHosts();
                    String realIp = IPUtils.transferIp(Integer.parseInt(ip));
                    hosts.setIp(realIp);
                    hosts.setPort(port);
                    hostsList.add(hosts);
                    for (int j = 0; j < nodeData.size(); j++) {
                        Subservices subservices = new Subservices();
                        //String currentNode = ZkUtil.getNodeData(zkByHost, "/soa/runtime/services"+nodeData.get(j));
                        String currentNode = nodeData.get(j);
                        List<String> serviceList = ZkUtil.getNodeChildren(zkByHost, "/soa/runtime/services/" + currentNode, false);
                        if (serviceList != null && serviceList.size() >= 0) {
                            for (String str : serviceList) {
                                String[] splitService = str.split(":");
                                if (splitService[0].equals(realIp) && splitService[1].equals(port)) {
                                    subservices.setName(currentNode);
                                    subservices.setVersion(splitService[2]);
                                    ssv.add(subservices);
                                    break;
                                }
                            }
                        }
                    }
                }
                smvo.setHosts(hostsList);
                smvo.setService(smv.getServiceName());
                smvo.setSubervices(ssv);
                dataList.add(smvo);
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return dataList;
    }
}
