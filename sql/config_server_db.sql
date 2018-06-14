
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;
CREATE SCHEMA config_server_db;
USE config_server_db;
-- ----------------------------
--  Table structure for `api_key_info`
-- ----------------------------
CREATE TABLE `api_key_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `api_key` varchar(100) NOT NULL COMMENT 'apiKey',
  `password` varchar(12) NOT NULL COMMENT 'apikey对应密码',
  `ips` varchar(255) NOT NULL DEFAULT '*' COMMENT 'ip规则,单个ip,多个ip用逗号隔开，掩码，*默认',
  `created_at` datetime NOT NULL COMMENT '添加时间',
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() COMMENT '更新时间',
  `created_by` int(11) NOT NULL COMMENT '添加人',
  `updated_by` int(11) NOT NULL COMMENT '最后更新人',
  `notes` varchar(255) DEFAULT NULL COMMENT '描述',
  `biz` varchar(100) NOT NULL DEFAULT '' COMMENT '业务',
  PRIMARY KEY (`id`),
  UNIQUE KEY `api_key_unique` (`api_key`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `authorities`
-- ----------------------------
CREATE TABLE `authorities` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `authority` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_username_auth` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `config_info`
-- ----------------------------
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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `config_publish_history`
-- ----------------------------
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
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `users`
-- ----------------------------
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL,
  `enabled` tinyint(4) NOT NULL DEFAULT 0 COMMENT '账号状态:[-1:禁用] [0:启用]',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

CREATE TABLE `white_list_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `serveice` varchar(255) NOT NULL DEFAULT '' COMMENT '服务名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


CREATE TABLE `white_list_history` (
  `id` int(11) NOT NULL,
  `service` varchar(255) NOT NULL DEFAULT '' COMMENT '服务',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '添加时间',
  `created_by` int(11) NOT NULL DEFAULT 0 COMMENT '添加人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

SET FOREIGN_KEY_CHECKS = 1;
