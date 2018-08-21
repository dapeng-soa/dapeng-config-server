package com.github.dapeng.web;

import com.github.dapeng.core.helper.IPUtils;
import com.github.dapeng.core.helper.SoaSystemEnvProperties;
import com.github.dapeng.openapi.utils.ZkUtil;
import com.github.dapeng.util.GetServiceMonitorThread;
import com.github.dapeng.util.InfluxDBUtil;
import com.github.dapeng.vo.MonitorHosts;
import com.github.dapeng.vo.ServiceGroupVo;
import com.github.dapeng.vo.ServiceMonitorVo;
import com.github.dapeng.vo.Subservices;
import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.zookeeper.ZooKeeper;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.*;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author: zhup
 * @Date: 2018/8/1 11:37
 */

@RestController
@RequestMapping("/serviceMonitor")
public class ServiceMonitorController {

    private static Logger LOGGER = LoggerFactory.getLogger(ServiceMonitorController.class);

    private static final String SOA_ZOOKEEPER_HOST = "soa_zookeeper_host";

    private static final String PATH = "/soa/runtime/services";

    @Resource
    private EntityManager entityManager;

    @ResponseBody
    @RequestMapping("/list")
    public Object serviceMonitorList() {
        List<ServiceMonitorVo> baseServiceList = getBaseServiceList();
        List<ServiceGroupVo> monitorList = getServiceMonitorList(baseServiceList);
        List<Map<String, Object>> resultList = resultList(monitorList);
        return resultList;
    }


    @ResponseBody
    @RequestMapping("/influx")
    public Object countInfoFromInflux() {
        try {
            InfluxDBUtil influxDBUtil = new InfluxDBUtil("admin", "admin", "http://10.10.10.37:8086", "dapengState");
            /***
             * 请求数前10名
             */
            String queryStr = "select sum(total_calls) from dapeng_service_process where time>now()-1m group by service_name limit 100";
            List<HashMap<String, Object>> methodMaps = influxDBUtil.query(queryStr);
            List<HashMap<String, Object>> collect = methodMaps.stream().sorted((x, y) -> {
                String xValue = x.get("sum").toString();
                String yValue = y.get("sum").toString();
                return Integer.parseInt(xValue.substring(0, xValue.indexOf("."))) - Integer.parseInt(yValue.substring(0, yValue.indexOf(".")));
            }).collect(Collectors.toList());
            Collections.reverse(collect);


            System.out.println(collect.subList(0, 10).size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 节点信息汇总
     *
     * @param monitorList
     * @return
     */
    public List<Map<String, Object>> resultList(List<ServiceGroupVo> monitorList) {
        List<Map<String, Object>> resList = new ArrayList<>();
        Map<String, Object> resultMap = new HashMap<>();
        Map<String, String> serviceIpMap = new HashMap<>();
        Map<String, String> ipNameMap = new HashMap<>();
        try {
            List<JsonObject> jsonObjectList = new ArrayList<>();
            ExecutorService threadPool = Executors.newCachedThreadPool();
            CompletionService<String> completionService = new ExecutorCompletionService<>(threadPool);
            int k = 0;
            for (int i = 0; i < monitorList.size(); i++) {
                ServiceGroupVo smlv = monitorList.get(i);
                List<MonitorHosts> hosts = smlv.getHosts();
                List<Subservices> subservices = smlv.getSubservices();
                if (subservices == null || subservices.isEmpty()) {
                    continue;
                }

                for (int j = 0; j < hosts.size(); j++) {
                    k++;
                    MonitorHosts monitorHosts = hosts.get(j);
                    ipNameMap.put(Joiner.on(":").join(monitorHosts.getIp(), monitorHosts.getPort()), smlv.getService());

                    subservices.stream().forEach(x -> {
                        serviceIpMap.put(x.getName(), Joiner.on(":").join(monitorHosts.getIp(), monitorHosts.getPort()));
                    });
                    GetServiceMonitorThread getServiceMonitorThread = new GetServiceMonitorThread(monitorHosts.getIp(), Integer.parseInt(monitorHosts.getPort()), subservices.get(0).getName(), subservices.get(0).getVersion());
                    completionService.submit(getServiceMonitorThread);
                }
            }
            threadPool.shutdown();
            for (int i = 0; i < k; i++) {
                try {
                    String takeStr = completionService.take().get();
                    JsonObject asJsonObject = new JsonParser().parse(takeStr).getAsJsonObject();
                    jsonObjectList.add(asJsonObject);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for (int i = 0; i < jsonObjectList.size(); i++) {
                JsonObject object = jsonObjectList.get(i);
                //{"gcInfos":{"gcInfos":"0/0"},"flows":{},
                // "service":"com.github.dapeng.hello.service.HelloService",
                // "container_info":" shutdown/terminating/terminated[false/false/false] -activeCount/poolSize[0/8] -waitingTasks/completeTasks/totalTasks[0/10/10]",
                // "serviceInfo":{"com.github.dapeng.hello.scala.service.HelloService":"Green|test"},
                // "tasks":{"waitingQueue":0,"total":10,"succeed":10},
                // "errors":{}}
                String serviceName = object.get("service").getAsString();
                JsonObject serviceObject = object.get("serviceInfo").getAsJsonObject();
                JsonObject taskInfoObject = object.get("tasks").getAsJsonObject();
                JsonObject GcInfoObject = object.get("gcInfos").getAsJsonObject();
                JsonObject flowsObject = object.get("flows").getAsJsonObject();
                //相同节点数累加
                if (resultMap.containsKey(serviceName)) {
                    Map tasksMap = (Map) resultMap.get("tasks");
                    Map GcMap = (Map) resultMap.get("gcInfos");
                    Map flowsMap = (Map) resultMap.get("flows");
                    tasksMap.put("waitingQueue", getJsonObjectByKey(taskInfoObject, "waitingQueue") + Integer.parseInt(tasksMap.get("waitingQueue").toString()));
                    tasksMap.put("total", getJsonObjectByKey(taskInfoObject, "total") + Integer.parseInt(tasksMap.get("total").toString()));
                    tasksMap.put("succeed", getJsonObjectByKey(taskInfoObject, "succeed") + Integer.parseInt(tasksMap.get("succeed").toString()));

                    /*GcMap.put("minorGc", getJsonObjectByKey(GcInfoObject, "minorGc") + Integer.parseInt(GcMap.get("minorGc").toString()));
                    GcMap.put("majorGc", getJsonObjectByKey(GcInfoObject, "majorGc") + Integer.parseInt(GcMap.get("majorGc").toString()));*/

                    flowsMap.put("max", getJsonObjectByKey(flowsObject, "max") + Integer.parseInt(flowsMap.get("max").toString()));
                    flowsMap.put("min", getJsonObjectByKey(flowsObject, "min") + Integer.parseInt(flowsMap.get("min").toString()));
                    flowsMap.put("avg", getJsonObjectByKey(flowsObject, "avg") + Integer.parseInt(flowsMap.get("avg").toString()));
                } else {
                    Map<String, Map> valueMap = new HashMap<>();
                    Map<String, Object> tasksMap = new HashMap();
                    Map<String, Object> GcMap = new HashMap();
                    Map<String, Object> flowsMap = new HashMap();
                    tasksMap.put("waitingQueue", getJsonObjectByKey(taskInfoObject, "waitingQueue"));
                    tasksMap.put("total", getJsonObjectByKey(taskInfoObject, "total"));
                    tasksMap.put("succeed", getJsonObjectByKey(taskInfoObject, "succeed"));

                   /* GcMap.put("minorGc", getJsonObjectByKey(GcInfoObject, "minorGc"));
                    GcMap.put("majorGc", getJsonObjectByKey(GcInfoObject, "majorGc"));*/
                    flowsMap.put("max", getJsonObjectByKey(flowsObject, "max"));
                    flowsMap.put("min", getJsonObjectByKey(flowsObject, "min"));
                    flowsMap.put("avg", getJsonObjectByKey(flowsObject, "avg"));
                    valueMap.put("tasks", tasksMap);
                    valueMap.put("gcInfos", GcMap);
                    resultMap.put(serviceName, valueMap);
                }
                Map<String, Object> tmpMap = (Map) resultMap.get(serviceName);
                Gson gson = new Gson();
                String tmpJson = gson.toJson(serviceObject);
                Map<String, Object> serviceMap = gson.fromJson(tmpJson, Map.class);
                tmpMap.put("nodeInfo", serviceMap);
                tmpMap.put("lastUpdate", System.currentTimeMillis());
            }
            //服务节点数统计
            for (Map.Entry<String, Object> map : resultMap.entrySet()) {
                String serviceName = map.getKey().toString();
                Map<String, Object> resMap = (Map) map.getValue();
                resMap.put("name", ipNameMap.get(serviceIpMap.get(serviceName)));
                for (int i = 0; i < monitorList.size(); i++) {
                    ServiceGroupVo serviceMonitorListVo = monitorList.get(i);
                    if (serviceMonitorListVo.getService().equals(serviceName)) {
                        Map nodesmap = (Map) resMap.get("node");
                        nodesmap.put("nodeCount", Integer.parseInt(nodesmap.get("nodeCount").toString() + 1));
                    } else {
                        Map nodesmap = new HashMap();
                        nodesmap.put("nodeCount", 1);
                        resMap.put("node", nodesmap);
                    }
                }
                resList.add(resMap);
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

            return resList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resList;
    }

    private int getJsonObjectByKey(JsonObject object, String key) {
        return object.has(key) ? object.get(key).getAsInt() : 0;
    }

    /**
     * 获取节点信息
     *
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
     *
     * @param baseServiceList
     * @return
     */
    private List<ServiceGroupVo> getServiceMonitorList(List<ServiceMonitorVo> baseServiceList) {
        List<ServiceGroupVo> dataList = new ArrayList<>();
        try {
            List<String> zkNodeList = cacheZkNodeList();
            for (int i = 0; i < baseServiceList.size(); i++) {
                ServiceGroupVo smvo = new ServiceGroupVo();
                ServiceMonitorVo smv = baseServiceList.get(i);
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
                    zkNodeList.stream().forEach(x -> {
                        if (x.indexOf(Joiner.on(":").join(realIp, hosts.getPort())) != -1) {
                            Subservices subservices = new Subservices();
                            subservices.setName(x.split(":")[0]);
                            subservices.setVersion(x.split(":")[3]);
                            ssv.add(subservices);
                        }
                    });
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


    /**
     * @return 获取zk节点信息
     */
    private List<String> cacheZkNodeList() {
        List<String> zkNodeList = new ArrayList<>();
        String zkHost = SoaSystemEnvProperties.get(SOA_ZOOKEEPER_HOST, "192.168.4.102:2181");
        ZooKeeper zkByHost = null;
        try {
            zkByHost = ZkUtil.createZkByHost(zkHost);
            List<String> nodeData = ZkUtil.getNodeChildren(zkByHost, PATH, false);
            for (int i = 0; i < nodeData.size(); i++) {
                String currentNode = nodeData.get(i);
                List<String> serviceList = ZkUtil.getNodeChildren(zkByHost, PATH + "/" + currentNode, false);
                for (int j = 0; j < serviceList.size(); j++) {
                    zkNodeList.add(Joiner.on(":").join(currentNode, serviceList.get(j)));
                }
            }
            ZkUtil.closeZk(zkByHost);
            return zkNodeList;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ZkUtil.closeZk(zkByHost);
        }
        return zkNodeList;
    }

}
