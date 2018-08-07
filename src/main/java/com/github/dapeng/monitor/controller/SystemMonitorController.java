package com.github.dapeng.monitor.controller;

import com.github.dapeng.monitor.dao.TestDao;
import com.github.dapeng.monitor.entity.ServiceMonitorVo;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StandardBasicTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
}
