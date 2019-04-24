package com.github.dapeng.query

import java.util

import com.github.dapeng.datasource.ConfigServerDataSource.mysqlData
import com.github.dapeng.entity.{TRole, TRoleMenu, Tmenu}
import wangzx.scala_commons.sql._

import scala.collection.JavaConverters._

/**
  * @author with struy.
  *         Create by 2019-01-22 15:28
  *         email :yq1724555319@gmail.com
  */

object SystemQuery {
  def getMenuTree: List[Tmenu] = {
    mysqlData.rows[Tmenu](
      sql"""
           select * from t_menu
         """)
  }

  def getMenuList: util.List[Tmenu] = {
    mysqlData.rows[Tmenu]( sql""" select * from t_menu """).asJava
  }

  def getRoleListByMenuID(mid: Long ): util.List[TRole] = {
    mysqlData.rows[TRole]( sql""" select r.* from t_role_menu m left join t_role r on m.role_id = r.id where menu_id = $mid""").asJava
  }

  def getMenuRoules(mid: Long): List[TRoleMenu] = {
    val list = mysqlData.rows[TRoleMenu](
      sql"""select * from  t_role_menu where menu_id = $mid""")
    list
  }

  def getMenuRolesByRoleId(rids: util.List[java.lang.Long]): util.List[String] = {
    val list: List[Row] = rids.asScala.flatMap(r => {
      mysqlData.rows[Row](sql"SELECT name , url FROM t_menu as m LEFT JOIN t_role_menu as rm on rm.menu_id = m.id WHERE rm.role_id = ${r.toInt}")
    }).toList
    list.map(x => {
      x.cell("url").getString
    }).distinct.asJava
  }

  def loadResourceRoleDefine(): util.Map[String, util.List[String]] = {
    val list = mysqlData.rows[Row](
      sql"""
SELECT  r.role,m.url FROM  t_role_menu as rm  LEFT JOIN t_menu as m on rm.menu_id = m.id LEFT JOIN t_role as r ON r.id = rm.role_id
         """)
    val map = list.map(x => {
      (x.cell("role").getString, x.cell("url").getString)
    }).distinct.groupBy(y => y._2).map(z => z._1 -> z._2.map(m => m._1))
    // java
    val resMap: util.HashMap[String, util.List[String]] = new util.HashMap[String, util.List[String]](32)
    map.foreach(x => {
      resMap.put(x._1, x._2.asJava)
    })
    resMap
  }
}
