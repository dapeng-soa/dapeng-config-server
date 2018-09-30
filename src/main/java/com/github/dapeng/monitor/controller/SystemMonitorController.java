package com.github.dapeng.monitor.controller;

import com.github.dapeng.monitor.MonitorConfig;
import com.github.dapeng.monitor.entity.ServiceMonitorVo;
import com.github.dapeng.util.HttpUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * @author huyj
 * @Created 2018-08-01 18:43
 */
@Controller
@RequestMapping("/exhibition")
@Transactional(rollbackFor = Throwable.class)
public class SystemMonitorController {
    private static final Logger logger = LoggerFactory.getLogger(SystemMonitorController.class);

    @Resource
    EntityManager em;

    @GetMapping()
    public String showPage() {
        String querySql = "SELECT tdu.service_id AS serviceId,ts.name AS servicename,GROUP_CONCAT(th.ip) AS ipList\n" +
                " FROM t_deploy_unit tdu LEFT JOIN t_host th ON tdu.host_id=th.id\n" +
                "                                LEFT JOIN t_service ts ON tdu.service_id=ts.id\n" +
                "                                GROUP BY tdu.service_id,ts.name";

        Query nativeQuery = em.createNativeQuery(querySql);
        nativeQuery.unwrap(SQLQuery.class)
                // 这里是设置字段的数据类型，有几点注意，首先这里的字段名要和目标实体的字段名相同，然后 sql 语句中的名称（别名）得与实体的相同
                .addScalar("serviceId", StandardBasicTypes.LONG)
                .addScalar("serviceName", StandardBasicTypes.STRING)
                .addScalar("ipList", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(ServiceMonitorVo.class));
        List list = nativeQuery.getResultList();
        System.out.println(list);

        return "exhibition/console";
    }


    @GetMapping(value = "/serviceCount/{serviceName:.+}")
    @ResponseBody
    public String getServiceCount(@PathVariable String serviceName) {
        System.out.println("serviceCount :" + serviceName);
        String logInfo = HttpUtils.doGet(MonitorConfig.SERVICE_COUNT_URL.replace(MonitorConfig.SERVICENAME, serviceName));
        return logInfo;
    }
}
