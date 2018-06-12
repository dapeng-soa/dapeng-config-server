
SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `authorities`
-- ----------------------------
DROP TABLE IF EXISTS `authorities`;
CREATE TABLE `authorities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `authority` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_username_auth` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Records of `authorities`
-- ----------------------------
BEGIN;
INSERT INTO `authorities` VALUES ('1', 'ROLE_ADMIN', 'admin');
COMMIT;

-- ----------------------------
--  Table structure for `config_info`
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '配置id',
  `service_name` varchar(100) NOT NULL COMMENT '服务全限定名',
  `status` tinyint(2) NOT NULL DEFAULT 1 COMMENT '发布状态,0:无效,1:新建,2:审核通过,3:已发布（前期默认是审核通过）',
  `timeout_config` text NOT NULL DEFAULT '' COMMENT '超时配置',
  `loadbalance_config` text NOT NULL DEFAULT '' COMMENT '负载均衡配置',
  `router_config` text NOT NULL DEFAULT '' COMMENT '路由配置',
  `freq_config` text NOT NULL DEFAULT '' COMMENT '限流配置',
  `created_by` int(11) NOT NULL DEFAULT 0 COMMENT '添加人',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '添加时间',
  `updated_by` int(11) NOT NULL DEFAULT 0 COMMENT '最后更新人',
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '最后更新时间',
  `remark` text NOT NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Records of `config_info`
-- ----------------------------
BEGIN;
INSERT INTO `config_info` VALUES ('26', 'com.today.api.order.service.OrderService2', '3', 'timeout/999ms;', '', '', '', '0', '2018-06-12 08:31:18', '0', '2018-06-12 08:31:18', '测试一哈');
COMMIT;

-- ----------------------------
--  Table structure for `config_publish_history`
-- ----------------------------
DROP TABLE IF EXISTS `config_publish_history`;
CREATE TABLE `config_publish_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '配置id',
  `version` varchar(50) NOT NULL COMMENT '版本号，每次发布的版本号(年月日时分秒)',
  `service_name` varchar(100) NOT NULL COMMENT '服务全限定名',
  `timeout_config` text NOT NULL DEFAULT '' COMMENT '超时配置',
  `loadbalance_config` text NOT NULL DEFAULT '' COMMENT '负载均衡配置',
  `router_config` text NOT NULL DEFAULT '' COMMENT '路由配置',
  `freq_config` text NOT NULL DEFAULT '' COMMENT '限流配置',
  `published_by` int(11) NOT NULL DEFAULT 0 COMMENT '发布人(发布人默认为0,实为未发布状态，发布更新此id)',
  `published_at` timestamp NOT NULL DEFAULT current_timestamp() COMMENT '发布时间',
  `remark` text NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Records of `config_publish_history`
-- ----------------------------
BEGIN;
INSERT INTO `config_publish_history` VALUES ('1', '20180612020254', 'com.today.api.order.service.OrderService2', 'timeout/800ms;', '', '', '', '0', '2018-06-12 02:02:54', '测试'), ('2', '20180612023249', 'com.today.api.order.service.OrderService2', 'timeout/999ms;', '', '', '', '0', '2018-06-12 02:32:49', '测试');
COMMIT;

-- ----------------------------
--  Table structure for `users`
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL,
  `enabled` tinyint(4) NOT NULL DEFAULT 0 COMMENT '账号状态:[-1:禁用] [0:启用]',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Records of `users`
-- ----------------------------
BEGIN;
INSERT INTO `users` VALUES ('1', 'admin', '21232f297a57a5a743894a0e4a801fc3', '0');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
