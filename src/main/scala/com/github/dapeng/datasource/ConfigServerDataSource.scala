package com.github.dapeng.datasource
import javax.annotation.Resource
/**
  * @author with struy.
  *         Create by 2019-01-15 10:55
  *         email :yq1724555319@gmail.com
  */

object ConfigServerDataSource {
  var mysqlData: javax.sql.DataSource = _
}

class ConfigServerDataSource {

  @Resource(name = "dataSource")
  def setMysqlData(mysqlData: javax.sql.DataSource): Unit = {
    ConfigServerDataSource.mysqlData = mysqlData
  }
}