

-- ----------------------------
--  Table structure for `config_info`
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '配置id',
  `service_name` varchar(100) NOT NULL COMMENT '服务全限定名',
  `version` varchar(50) NOT NULL COMMENT '唯一的版本,用于回滚和历史查看(uuid?)',
  `status` tinyint(2) NOT NULL DEFAULT 2 COMMENT '发布状态,0:无效,1:新建,2:审核通过,3:已发布',
  `timeout_config` text NOT NULL DEFAULT '' COMMENT '超时配置',
  `loadbalance_config` text NOT NULL DEFAULT '' COMMENT '负载均衡配置',
  `router_config` text NOT NULL DEFAULT '' COMMENT '路由配置',
  `freq_config` text NOT NULL DEFAULT '' COMMENT '限流配置',
  `published_by` int(11) NOT NULL DEFAULT 0 COMMENT '发布人(发布人默认为0,实为未发布状态，发布更新此id)',
  `published_at` timestamp NULL DEFAULT NULL COMMENT '发布时间',
  `created_by` int(11) NOT NULL DEFAULT 0 COMMENT '添加人',
  `updated_by` int(11) NOT NULL DEFAULT 0 COMMENT '最后更新人',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '添加时间',
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4
