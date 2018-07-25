package com.github.dapeng.socket.enums

import wangzx.scala_commons.sql.{DbEnum, DbEnumJdbcValueAccessor}

class EventType private(val id: Int, val name: String) extends DbEnum {
  override def toString(): String = "(" + id + "," + name + ")"

  override def equals(obj: Any): Boolean = {
    if (obj == null) false
    else if (obj.isInstanceOf[EventType]) obj.asInstanceOf[EventType].id == this.id
    else false
  }

  override def hashCode(): Int = this.id
}

object EventType {
  val SERVER_LIST = new EventType(1, "serverList")
  val NODE_REG = new EventType(2, "nodeReg")
  val NODE_EVENT = new EventType(3, "nodeEvent")
  val WEB_REG = new EventType(4, "webReg")
  val WEB_EVENT = new EventType(5, "webEvent")
  val WEB_CMD = new EventType(6, "webCmd")
  val SERVER_TIME = new EventType(7, "serverTime")
  val GET_SERVER_TIME = new EventType(8, "getServerTime")
  def unknown(id: Int) = new EventType(id, id + "")

  def valueOf(id: Int): EventType = id match {
    case 1 => SERVER_LIST
    case 2 => NODE_REG
    case 3 => NODE_EVENT
    case 4 => WEB_REG
    case 5 => WEB_EVENT
    case 6 => WEB_CMD
    case 7 => SERVER_TIME
    case 8 => GET_SERVER_TIME
    case _ => unknown(id)
  }

  def apply(v: Int) = valueOf(v)

  def unapply(v: EventType): Option[Int] = Some(v.id)

  implicit object Accessor extends DbEnumJdbcValueAccessor[EventType](valueOf)

}

