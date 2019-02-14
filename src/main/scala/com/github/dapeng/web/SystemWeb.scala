package com.github.dapeng.web

import java.util

import com.github.dapeng.action.SystemActionSql
import com.github.dapeng.common.{Commons, Resp}
import com.github.dapeng.dto.MenuRoleDto
import com.github.dapeng.entity.{TRoleMenu, Tmenu}
import com.github.dapeng.query.SystemQuery
import com.github.dapeng.vo.{JMenuVo, JRoleMenuVo}
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation._
import wangzx.scala_commons.sql.BeanBuilder

import scala.collection.JavaConverters._

/**
  * @author with struy.
  *         Create by 2019-01-22 16:38
  *         email :yq1724555319@gmail.com
  */

@RestController
@RequestMapping(Array("/api"))
@Transactional(rollbackFor = Array(classOf[Exception]))
class SystemWeb {

  /**
    * 添加一个菜单
    *
    * @param vo
    * @return
    */
  @PostMapping(Array("/system-menu/add"))
  def addMenu(@RequestBody vo: JMenuVo): ResponseEntity[Resp] = {
    SystemActionSql.insertMenu(vo)
    ResponseEntity.ok(Resp.of(Commons.SUCCESS_CODE, Commons.COMMON_SUCCESS_MSG))
  }

  /**
    * 修改一个菜单
    *
    * @param vo
    * @return
    */
  @PostMapping(Array("/system-menu/edit/{id}"))
  def editMenu(@RequestBody vo: JMenuVo, @PathVariable id: Long): ResponseEntity[Resp] = {
    SystemActionSql.updateMenu(id, vo)
    ResponseEntity.ok(Resp.of(Commons.SUCCESS_CODE, Commons.COMMON_SUCCESS_MSG))
  }

  /**
    * 获取菜单树
    *
    * @return
    */
  @GetMapping(Array("/system-menu/getMenuTree"))
  def getMenuTree: ResponseEntity[Resp] = {
    val menus: List[Tmenu] = SystemQuery.getMenuTree
    // 父级菜单
    val menuTree: util.List[JMenuVo] = menus.filter(x => x.parentId.isEmpty).map(m => {
      toVo(m, menus)
    }).asJava
    ResponseEntity.ok(Resp.of(Commons.SUCCESS_CODE, Commons.LOADED_DATA, menuTree))
  }

  /**
    * 获取单个菜单的绑定角色
    *
    * @param mid
    */
  @GetMapping(Array("/system-menu-roles/{mid}"))
  def getMenuRoules(@PathVariable mid: Long): ResponseEntity[Resp] = {
    val list: List[TRoleMenu] = SystemQuery.getMenuRoules(mid)
    val menuVoes: List[JRoleMenuVo] = list.map(x => {
      val vo: JRoleMenuVo = new JRoleMenuVo
      vo.setId(x.id)
      vo.setMenuId(x.menuId)
      vo.setRemark(x.remark)
      vo.setRoleId(x.roleId)
      vo
    })
    ResponseEntity.ok(Resp.of(Commons.SUCCESS_CODE, Commons.LOADED_DATA, menuVoes.asJava))
  }

  @PostMapping(Array("/system-menu-role/link-role"))
  def linkRole(@RequestBody dto: MenuRoleDto): Unit = {
    SystemActionSql.insertMenuRoles(dto)
  }

  @PostMapping(Array("/system-menu-role/unlink-role"))
  def unlinkRole(@RequestBody dto: MenuRoleDto): Unit = {
    SystemActionSql.deleteMenuRoles(dto)
  }

  private def getChildMenus(parentId: Long, menus: List[Tmenu]): java.util.List[JMenuVo] = {
    val menuVoes: List[JMenuVo] = menus.filter(x => x.parentId.getOrElse(-1) == parentId).map(m => {
      toVo(m, menus)
    })
    menuVoes.asJava
  }

  private def toVo(m: Tmenu, menus: List[Tmenu]): JMenuVo = {
    val vo: JMenuVo = new JMenuVo
    vo.setId(m.id)
    vo.setName(m.name)
    vo.setRemark(m.remark)
    vo.setUrl(m.url)
    vo.setStatus(if (m.status == 1) {
      "valid"
    } else {
      "invalid"
    })
    vo.setText(m.name)
    val voes = getChildMenus(m.id, menus.filterNot(x => x.parentId.isEmpty))
    if (voes.size() > 0) {
      vo.setNodes(voes)
    } else {
      vo.setNodes(null)
    }
    vo
  }
}