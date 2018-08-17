package com.github.dapeng.monitor.controller;

import com.github.dapeng.monitor.MonitorConfig;
import com.github.dapeng.monitor.dao.TestDao;
import com.github.dapeng.monitor.entity.ServiceMonitorVo;
import com.github.dapeng.util.HttpUtils;
import com.google.gson.Gson;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author huyj
 * @Created 2018-08-01 18:43
 */
@Controller
@RequestMapping("/exhibition")
@Transactional(rollbackFor = Throwable.class)
public class SystemMonitorController {
    private static final Logger logger = LoggerFactory.getLogger(SystemMonitorController.class);

    @Autowired
    private TestDao testDao;


    @Resource
    EntityManager em;

    @GetMapping()
    public String showPage() {
        //EntityManager em = testDao.  ..getEntityManager();
        String querySql = "SELECT tdu.service_id AS serviceId,ts.name AS servicename,GROUP_CONCAT(th.ip) AS ipList\n" +
                " FROM t_deploy_unit tdu LEFT JOIN t_host th ON tdu.host_id=th.id\n" +
                "                                LEFT JOIN t_service ts ON tdu.service_id=ts.id\n" +
                "                                GROUP BY tdu.service_id,ts.name";
        /* Query query = em.createNativeQuery(querySql);
        Object singleResult = query.getResultList();
        System.out.println(singleResult);*/

        Query nativeQuery = em.createNativeQuery(querySql);
        nativeQuery.unwrap(SQLQuery.class)
                // 这里是设置字段的数据类型，有几点注意，首先这里的字段名要和目标实体的字段名相同，然后 sql 语句中的名称（别名）得与实体的相同
                .addScalar("serviceId", StandardBasicTypes.LONG)
                .addScalar("serviceName", StandardBasicTypes.STRING)
                .addScalar("ipList", StandardBasicTypes.STRING)
                .setResultTransformer(Transformers.aliasToBean(ServiceMonitorVo.class));
        List list = nativeQuery.getResultList();
        System.out.println(list);

        //return new ModelAndView("exhibition/console");
        return "exhibition/console";
    }

    @GetMapping("/logInfo")
    @ResponseBody
    public String getLogInfo() {

        String logInfo = "Dear 雷华哲,梁斌斌:\n" +
                "  您负责的项目orderService出现异常日志，请安排相关人员查看错误原因并及时处理。确保系统正常运行！\n" +
                "  相关信息已经发送至您的邮箱，请查看邮件内容并及时处理，特殊情况请说明原因，谢谢合作！\n" +
                "\n" +
                "@雷华哲 @梁斌斌 \n" +
                "Dear 孙政,武平阳:\n" +
                "  您负责的项目memberService出现异常日志，请安排相关人员查看错误原因并及时处理。确保系统正常运行！\n" +
                "  相关信息已经发送至您的邮箱，请查看邮件内容并及时处理，特殊情况请说明原因，谢谢合作！\n" +
                "\n" +
                "@孙政 @武平阳 ";
        Map map = new HashMap<String, Object>();
        map.put("value", logInfo.replaceAll("\\n", "<br/>"));

        List list = new ArrayList();
        list.add(map);
        return (new Gson().toJson(list));
    }

    @GetMapping(value = "/serviceCount/{serviceName:.+}")
    @ResponseBody
    public String getServiceCount(@PathVariable String serviceName) {
        System.out.println("serviceCount :" + serviceName);
        String logInfo = HttpUtils.doGet(MonitorConfig.SERVICE_COUNT_URL.replace(MonitorConfig.SERVICENAME, serviceName));
        return logInfo;
    }
}
