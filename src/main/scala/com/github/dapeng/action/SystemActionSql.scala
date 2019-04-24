package com.github.dapeng.action

import com.github.dapeng.datasource.ConfigServerDataSource.mysqlData
import com.github.dapeng.dto.MenuRoleDto
import com.github.dapeng.entity.TRoleMenu
import com.github.dapeng.util.DateUtil
import com.github.dapeng.vo.JMenuVo
import wangzx.scala_commons.sql._

/**
  * @author with struy.
  *         Create by 2019-01-22 17:30
  *         email :yq1724555319@gmail.com
  */

object SystemActionSql {

  /**
    * 增加菜单
    *
    * @param vo
    */
  def insertMenu(vo: JMenuVo): Unit = {
    val optionSql1 = vo.getParentId match {
      case null => sql""
      case x => sql",parent_id = ${x.toInt}"
    }

    mysqlData.executeUpdate(
      sql"""
           insert into t_menu set
           name = ${vo.getName},
           remark = ${vo.getRemark},
           url = ${vo.getUrl},
           created_at = ${DateUtil.now},
           created_by = 0,
           updated_by = 0
         """ + optionSql1)
  }

  /**
    * 更新菜单
    *
    * @param id
    * @param vo
    */
  def updateMenu(id: Long, vo: JMenuVo): Unit = {
    mysqlData.executeUpdate(
      sql"""
           update t_menu set name = ${vo.getName},
            remark = ${vo.getRemark},
            url = ${vo.getUrl},
            updated_by = 0
            where id = $id
         """)
  }

  /**
    * 批量关联菜单角色
    *
    * @param dto
    */
  def insertMenuRoles(dto: MenuRoleDto): Unit = {
    dto.getRoleIds.forEach(r => {
      val mrlist = mysqlData.rows[TRoleMenu](sql"select * from t_role_menu where menu_id = ${dto.getMenuId.toInt} and role_id = ${r.toInt} ")
      if (mrlist.isEmpty) {
        mysqlData.executeUpdate(
          sql"""insert into t_role_menu set
          menu_id = ${dto.getMenuId.toInt},
         role_id = ${r.toInt},
         created_at = ${DateUtil.now},
         created_by = 0,
         updated_by = 0
        """)
      }
    })
  }

  /**
    * 批量删除关联菜单角色
    *
    * @param dto
    */
  def deleteMenuRoles(dto: MenuRoleDto): Unit = {
    dto.getRoleIds.forEach(r => {
      mysqlData.executeUpdate(
        sql"""delete from t_role_menu  where
          menu_id = ${dto.getMenuId.toInt} and
         role_id = ${r.toInt}
        """)
    })
  }
}
