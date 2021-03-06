package com.github.dapeng.web;

import com.github.dapeng.client.netty.RequestUtils;
import com.github.dapeng.core.helper.IPUtils;
import com.github.dapeng.core.helper.SoaSystemEnvProperties;
import com.github.dapeng.entity.ServiceMonitorRes;
import com.github.dapeng.query.OtherQuery;
import com.github.dapeng.echo.echo_result;
import com.github.dapeng.util.ZkUtil;
import com.github.dapeng.vo.*;
import com.google.common.base.Joiner;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
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
    private Pattern pattern = Pattern.compile("soa_container_port=(\\d.*)");
    private Gson gson = new Gson();

   /* ThreadPoolExecutor poolExecutor = (ThreadPoolExecutor) Executors.newFixedThreadPool(SoaSystemEnvProperties.SOA_CORE_POOL_SIZE,
            new ThreadFactoryBuilder()
                    .setDaemon(true)
                    .setNameFormat("dapeng-monitor-pool-%d")
                    .build());*/

    @Resource
    private EntityManager entityManager;

    @ResponseBody
    @RequestMapping("/list")
    public Object serviceMonitorList() {
        List<ServiceMonitorVo> baseServiceList = OtherQuery.getBaseServiceList();
        List<ServiceGroupVo> monitorList = getServiceMonitorList(baseServiceList);
        List<Map<String, Object>> resultList = resultList(monitorList);
        return resultList.stream().sorted((x, y) -> {
            HashMap xnode = (HashMap) x.get("node");
            HashMap ynode = (HashMap) y.get("node");
            if (Integer.parseInt(xnode.get("nodeCount").toString()) == Integer.parseInt(ynode.get("nodeCount").toString())
                    &&Integer.parseInt(xnode.get("nodeCount").toString())!=0&&Integer.parseInt(ynode.get("nodeCount").toString())!=0) {
                HashMap xtask = (HashMap) x.get("tasks");
                HashMap ytask = (HashMap) y.get("tasks");
                if (Integer.parseInt(xtask.get("waitingQueue").toString()) == Integer.parseInt(ytask.get("waitingQueue").toString())) {
                    return Integer.parseInt(ytask.get("total").toString()) - Integer.parseInt(xtask.get("total").toString());
                }
                return Integer.parseInt(xtask.get("waitingQueue").toString()) - Integer.parseInt(ytask.get("waitingQueue").toString());
            }
            return Integer.parseInt(xnode.get("nodeCount").toString()) - Integer.parseInt(ynode.get("nodeCount").toString());
        }).collect(Collectors.toList());
    }

    /**
     * 节点信息汇总
     *
     * @param monitorList
     * @return
     */
    public List<Map<String, Object>> resultList(List<ServiceGroupVo> monitorList) {
        List<Map<String, Object>> resList = new ArrayList<>(64);
        Map<String, Object> resultMap = new HashMap<>(32);
        Map<String, String> serviceIpMap = new HashMap<>(32);
        Map<String, String> ipNameMap = new HashMap<>(16);
        try {
            List<JsonObject> jsonObjectList = new ArrayList<>(16);
            List<Future<echo_result>> futureList = new ArrayList<>();
            //CompletionService<String> completionService = new ExecutorCompletionService<>(poolExecutor);
            int k = 0;
            for (int i = 0; i < monitorList.size(); i++) {
                ServiceGroupVo smlv = monitorList.get(i);
                List<MonitorHosts> hosts = smlv.getHosts();
                List<Subservices> subservices = smlv.getSubservices();
                if (subservices == null || subservices.isEmpty()) {
                    JsonObject nodeObject = new JsonObject();
                    nodeObject.addProperty("name", smlv.getService());
                    nodeObject.addProperty("child", false);
                    jsonObjectList.add(nodeObject);
                    continue;
                }

                for (int j = 0; j < hosts.size(); j++) {
                    MonitorHosts monitorHosts = hosts.get(j);
                    //k++;
                    ipNameMap.put(Joiner.on(":").join(monitorHosts.getIp(), monitorHosts.getPort()), smlv.getService());
                    subservices.stream().forEach(x -> {
                        serviceIpMap.put(x.getName(), Joiner.on(":").join(monitorHosts.getIp(), monitorHosts.getPort()));
                    });

                    try {
                        if(!CheckConnectInfo.set.contains(monitorHosts.getIp()+":"+monitorHosts.getPort())){
                            Future<echo_result> remoteServiceEchoAsync = RequestUtils.getRemoteServiceEchoAsync(monitorHosts.getIp(), Integer.parseInt(monitorHosts.getPort()), subservices.get(0).getName(), subservices.get(0).getVersion());
                            futureList.add(remoteServiceEchoAsync);
                        }
                    } catch (Exception e) {
                        LOGGER.error("获取服务echo信息出现异常", e);
                    }

                    /*GetServiceMonitorThread getServiceMonitorThread = new GetServiceMonitorThread(monitorHosts.getIp(), Integer.parseInt(monitorHosts.getPort()), subservices.get(0).getName(), subservices.get(0).getVersion());
                    completionService.submit(getServiceMonitorThread);*/
                }
            }

            for(Future<echo_result> echo:futureList){
                try {
                    String echoString = echo.get().getSuccess();
                    JsonObject asJsonObject = new JsonParser().parse(echoString).getAsJsonObject();
                    jsonObjectList.add(asJsonObject);
                } catch (JsonSyntaxException e) {
                    LOGGER.error("echo返回不是json串", e);
                }
            }


            /*for (int i = 0; i < k; i++) {
                try {
                    String takeStr = completionService.take().get();
                    if (StringUtils.isNotBlank(takeStr)) {
                        try {
                            JsonObject asJsonObject = new JsonParser().parse(takeStr).getAsJsonObject();
                            jsonObjectList.add(asJsonObject);
                        } catch (JsonSyntaxException e) {
                            LOGGER.error("echo返回不是json串", e);
                        }
                    }
                } catch (InterruptedException e) {
                    LOGGER.error("获取服务echo信息出现异常", e);
                    //e.printStackTrace();
                }
            }*/
            for (int i = 0; i < jsonObjectList.size(); i++) {
                JsonObject object = jsonObjectList.get(i);
                String serviceName = null;
                if (object.has("child")) {
                    serviceName = object.get("name").getAsString();
                    Map<String, Map> valueMap = new HashMap<>(16);
                    Map<String, Object> nodeMap = new HashMap(4);
                    valueMap.put("serviceInfo", gson.fromJson(gson.toJson(object), Map.class));
                    nodeMap.put("nodeCount", 0);
                    valueMap.put("node", nodeMap);
                    resultMap.put(serviceName, valueMap);
                } else {
                    serviceName = object.get("service").getAsString();
                    JsonObject serviceObject = object.get("serviceInfo").getAsJsonObject();
                    JsonObject taskInfoObject = object.get("tasks").getAsJsonObject();
                    JsonObject GcInfoObject = object.get("gcInfos").getAsJsonObject();
                    JsonObject flowsObject = object.get("flows").getAsJsonObject();
                    //相同节点数累加
                    if (resultMap.containsKey(serviceName)) {
                        Map instanceMap = (Map) resultMap.get(serviceName);
                        Map tasksMap = (Map) instanceMap.get("tasks");
                        Map flowsMap = (Map) instanceMap.get("flows");
                        Map GcMap = (Map) instanceMap.get("gcInfos");
                        Map nodeMap = (Map) instanceMap.get("node");
                        tasksMap.put("waitingQueue", getJsonObjectByKey(taskInfoObject, "waitingQueue") + Integer.parseInt(tasksMap.get("waitingQueue").toString()));
                        tasksMap.put("total", getJsonObjectByKey(taskInfoObject, "total") + Integer.parseInt(tasksMap.get("total").toString()));
                        tasksMap.put("succeed", getJsonObjectByKey(taskInfoObject, "succeed") + Integer.parseInt(tasksMap.get("succeed").toString()));
                        flowsMap.put("max", getJsonObjectByKey(flowsObject, "max") + (flowsMap.containsKey("max") ? Integer.parseInt(flowsMap.get("max").toString()) : 0));
                        flowsMap.put("min", getJsonObjectByKey(flowsObject, "min") + (flowsMap.containsKey("min") ? Integer.parseInt(flowsMap.get("min").toString()) : 0));
                        flowsMap.put("avg", getJsonObjectByKey(flowsObject, "avg") + (flowsMap.containsKey("avg") ? Integer.parseInt(flowsMap.get("avg").toString()) : 0));
                        nodeMap.put("nodeCount", Integer.parseInt(nodeMap.get("nodeCount").toString()) + 1);
                    } else {
                        Map<String, Map> valueMap = new HashMap<>(16);
                        Map<String, Object> tasksMap = new HashMap(16);
                        Map<String, Object> GcMap = new HashMap(16);
                        Map<String, Object> flowsMap = new HashMap(16);
                        Map<String, Object> nodeMap = new HashMap(16);
                        tasksMap.put("waitingQueue", getJsonObjectByKey(taskInfoObject, "waitingQueue"));
                        tasksMap.put("total", getJsonObjectByKey(taskInfoObject, "total"));
                        tasksMap.put("succeed", getJsonObjectByKey(taskInfoObject, "succeed"));
                        flowsMap.put("min", getJsonObjectByKey(flowsObject, "min"));
                        flowsMap.put("avg", getJsonObjectByKey(flowsObject, "avg"));
                        flowsMap.put("max", getJsonObjectByKey(flowsObject, "max"));
                        nodeMap.put("nodeCount", 1);
                        valueMap.put("tasks", tasksMap);
                        valueMap.put("gcInfos", GcMap);
                        valueMap.put("flows", flowsMap);
                        valueMap.put("node", nodeMap);
                        resultMap.put(serviceName, valueMap);
                        Map<String, Object> tmpMap = (Map) resultMap.get(serviceName);
                        String tmpJson = gson.toJson(serviceObject);
                        Map<String, Object> serviceMap = gson.fromJson(tmpJson, Map.class);
                        tmpMap.put("nodeInfo", serviceMap);
                        tmpMap.put("lastUpdate", System.currentTimeMillis());
                    }
                }
            }
            //服务名替换
            for (Map.Entry<String, Object> map : resultMap.entrySet()) {
                String serviceName = map.getKey().toString();
                Map<String, Object> resMap = (Map) map.getValue();
                if (ipNameMap.get(serviceIpMap.get(serviceName)) != null) {
                    resMap.put("name", ipNameMap.get(serviceIpMap.get(serviceName)));
                } else {
                    resMap.put("name", serviceName);
                }
                resList.add(resMap);
            }
            return resList;
        } catch (Exception e) {
            LOGGER.error("拼装健康度信息出现异常", e);
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
    private List<ServiceMonitorRes> getBaseServiceList() {
        Query nativeQuery = entityManager.createNativeQuery("\n" +
                "select tdu.service_id AS serviceId,ts.name AS serviceName,group_concat(th.ip) AS ipList, ts.env \n" +
                " from t_deploy_unit tdu left join t_host th on tdu.host_id=th.id\n" +
                "                                left join t_service ts on tdu.service_id=ts.id\n" +
                "                                where th.name not in ('demo')" +
                "                                group by tdu.service_id,ts.name\n");
        nativeQuery.unwrap(SQLQuery.class)
                .addScalar("serviceId", StandardBasicTypes.LONG)
                .addScalar("serviceName", StandardBasicTypes.STRING)
                .addScalar("ipList", StandardBasicTypes.STRING)
                .addScalar("env", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(ServiceMonitorRes.class));
        List<ServiceMonitorRes> res = nativeQuery.getResultList();
        entityManager.close();
        return res;
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
                Matcher m = pattern.matcher(smv.getEnv());
                String port = null;
                if (m.find()) {
                    port = m.group(1);
                }
                if (port == null) {
                    continue;
                }
                for (String ip : ipArrs) {
                    MonitorHosts hosts = new MonitorHosts();
                    String realIp = IPUtils.transferIp(Integer.parseInt(ip));
                    hosts.setIp(realIp);
                    hosts.setPort(port);
                    hostsList.add(hosts);
                    zkNodeList.forEach(x -> {
                        if (x.contains(Joiner.on(":").join(realIp, hosts.getPort()))) {
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
            LOGGER.error("拼装服务信息出现异常", e1);
        }
        return dataList;
    }


    /**
     * @return 获取zk节点信息
     */
    private List<String> cacheZkNodeList() {
        List<String> zkNodeList = new ArrayList<>(64);
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
            LOGGER.error("获取zk连接信息出现异常", e);
        } finally {
            ZkUtil.closeZk(zkByHost);
        }
        return zkNodeList;
    }

}
