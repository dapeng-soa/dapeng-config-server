package com.github.dapeng.datasource

import java.util

import com.github.dapeng.datasource.ConfigServerDataSource._
import com.github.dapeng.vo.{ServiceMonitorRes, ServiceMonitorVo}
import wangzx.scala_commons.sql._


/**
  * @author with struy.
  *         Create by 2019-01-15 11:04
  *         email :yq1724555319@gmail.com
  */


object ConfigServerQuery {
  def getBaseServiceList: java.util.List[ServiceMonitorVo] = {
    val lists = new util.ArrayList[ServiceMonitorVo](16)

    val serviceMonitorReses = mysqlData.withTransaction(conn => {
      conn.rows[ServiceMonitorRes](
        sql"""
           select tdu.service_id AS serviceId,ts.name AS serviceName,group_concat(th.ip) AS ipList, ts.env
            from t_deploy_unit tdu left join t_host th on tdu.host_id=th.id
            left join t_service ts on tdu.service_id=ts.id
             where th.name not in ('demo')
             group by tdu.service_id,ts.name
         """)
    })
    serviceMonitorReses.foreach(x => {
      val vo: ServiceMonitorVo = new ServiceMonitorVo()
      vo.setEnv(x.env)
      vo.setIpList(x.ipList)
      vo.setServiceId(x.serviceId)
      vo.setServiceName(x.serviceName)
      lists.add(vo)
    })
    lists
  }
}
