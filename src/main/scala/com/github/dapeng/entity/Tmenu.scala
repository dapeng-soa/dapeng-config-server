package com.github.dapeng.entity

import java.sql.Timestamp

/**
  * @author with struy.
  *         Create by 2019-01-22 15:36
  *         email :yq1724555319@gmail.com
  */

case class Tmenu
(
  id: Long,
  status: Int,
  name: String,
  parentId: Option[Long] = None,
  url: String,
  remark: String,
  createdAt: Timestamp,
  updatedAt: Timestamp,
  createdBy: Long,
  updatedBy: Long
)
