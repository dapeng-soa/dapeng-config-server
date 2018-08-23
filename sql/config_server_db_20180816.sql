SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;
CREATE SCHEMA config_server_db;
USE config_server_db;

-- ----------------------------
--  Table structure for `api_key_info`
-- ----------------------------
DROP TABLE IF EXISTS `api_key_info`;
CREATE TABLE `api_key_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `authorities`
-- ----------------------------
DROP TABLE IF EXISTS `authorities`;
CREATE TABLE `authorities` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `authority` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `index_username_auth` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `config_info`
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '配置id',
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `config_publish_history`
-- ----------------------------
DROP TABLE IF EXISTS `config_publish_history`;
CREATE TABLE `config_publish_history` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '配置id',
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
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `t_deploy_unit`
-- ----------------------------
DROP TABLE IF EXISTS `t_deploy_unit`;
CREATE TABLE `t_deploy_unit` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `set_id` int(11) NOT NULL DEFAULT 0 COMMENT '环境集id',
  `host_id` int(11) NOT NULL DEFAULT 0 COMMENT '节点id',
  `service_id` int(11) DEFAULT NULL DEFAULT 0 COMMENT '服务id',
  `git_tag` varchar(45) NOT NULL DEFAULT '' COMMENT '部署的tag号',
  `image_tag` varchar(45) NOT NULL DEFAULT '' COMMENT '镜像tag',
  `created_at` datetime NOT NULL DEFAULT current_timestamp() COMMENT '添加时间',
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '更新时间',
  `env` varchar(1024) DEFAULT 'NULL' COMMENT '环境变量',
  `ports` varchar(45) DEFAULT 'NULL' COMMENT '端口映射',
  `volumes` varchar(45) DEFAULT 'NULL' COMMENT '卷挂载',
  `docker_extras` varchar(1024) DEFAULT 'NULL' COMMENT '如：restart: on-failure:3',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='部署单元';

-- ----------------------------
--  Table structure for `t_deploy_unit_status`
-- ----------------------------
DROP TABLE IF EXISTS `t_deploy_unit_status`;
CREATE TABLE `t_deploy_unit_status` (
  `id` int(11) unsigned NOT NULL COMMENT '自增id',
  `current_timestamp` varchar(45) DEFAULT 'NULL' COMMENT '当前部署的时间戳',
  `current_yml` varchar(45) DEFAULT 'NULL' COMMENT '当前的yaml',
  `expired` varchar(45) DEFAULT 'NULL' COMMENT '过期？？？',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `t_host`
-- ----------------------------
DROP TABLE IF EXISTS `t_host`;
CREATE TABLE `t_host` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(45) NOT NULL DEFAULT '' COMMENT '节点(宿主机)名称, 例如app-5',
  `set_id` int(11) NOT NULL DEFAULT 0 COMMENT '绑定的环境集id',
  `ip` int(11) NOT NULL DEFAULT 0 COMMENT '该节点IP信息',
  `labels` varchar(45) DEFAULT NULL COMMENT '逗号分隔的标签,用于分类',
  `extra` smallint(2) NOT NULL DEFAULT 0 COMMENT '是否外部机器. 内部机器用于部署, 外部机器用于配置extra_host',
  `env` varchar(4096) DEFAULT NULL COMMENT '尽量不要再host上配置, 以免扩容时麻烦',
  `created_at` datetime NOT NULL DEFAULT current_timestamp() COMMENT '添加时间',
  `updated_at` timestamp NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '更新时间',
  `remark` varchar(255) DEFAULT 'NULL' COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_host_name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COMMENT='节点信息表';

-- ----------------------------
--  Table structure for `t_operation_journal`
-- ----------------------------
DROP TABLE IF EXISTS `t_operation_journal`;
CREATE TABLE `t_operation_journal` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `set_id` int(11) NOT NULL DEFAULT 0 COMMENT '环境集id',
  `host_id` int(11) NOT NULL DEFAULT 0 COMMENT '节点id',
  `service_id` int(11) NOT NULL DEFAULT 0 COMMENT '服务id',
  `git_tag` varchar(45) NOT NULL DEFAULT '' COMMENT '部署的tag号',
  `image_tag` varchar(45) DEFAULT '' COMMENT '镜像tag',
  `yml` text  COMMENT '本次部署的yaml文件',
  `diff` varchar(4096) DEFAULT '' COMMENT 'yml diff',
  `op_flag` smallint(2) NOT NULL DEFAULT 0 COMMENT '操作列表,1:升级(update);2:重启(restart);3:停止(stop);4:降级(rollback)',
  `created_at` datetime NOT NULL DEFAULT current_timestamp() COMMENT '添加时间',
  `created_by` varchar(45) NOT NULL DEFAULT 'admin' COMMENT '操作人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='部署流水表';

-- ----------------------------
--  Table structure for `t_service`
-- ----------------------------
DROP TABLE IF EXISTS `t_service`;
CREATE TABLE `t_service` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(45) NOT NULL DEFAULT '' COMMENT '服务名称',
  `image` varchar(128) NOT NULL DEFAULT '' COMMENT '镜像',
  `labels` varchar(255) DEFAULT NULL COMMENT '逗号分隔的标签,用于分类',
  `env` varchar(4096) DEFAULT 'NULL' COMMENT '环境变量',
  `volumes` varchar(1024) DEFAULT 'NULL' COMMENT '挂载卷',
  `ports` varchar(1024) DEFAULT 'NULL' COMMENT '端口映射',
  `compose_labels` varchar(4096) DEFAULT NULL COMMENT 'labels node of source-compose',
  `docker_extras` varchar(1024) DEFAULT NULL COMMENT '例如restart, cmd等',
  `created_at` datetime NOT NULL DEFAULT current_timestamp() COMMENT  '添加时间',
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '更新时间',
  `remark` varchar(255) DEFAULT 'NULL' COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_service_name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='服务信息表\nenv优先级: service<set<host<deploy_unit';

-- ----------------------------
--  Table structure for `t_set`
-- ----------------------------
DROP TABLE IF EXISTS `t_set`;
CREATE TABLE `t_set` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(45) NOT NULL DEFAULT '' COMMENT '环境集名称, 例如sandbox1, demo, onlineDemo, ',
  `env` varchar(8192) DEFAULT NULL COMMENT '环境变量',
  `created_at` datetime NOT NULL DEFAULT current_timestamp() COMMENT '添加时间',
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '最后更新时间',
  `remark` varchar(255) DEFAULT 'NULL' COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_set_name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='环境集信息表';

-- ----------------------------
--  Table structure for `users`
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL,
  `enabled` tinyint(4) NOT NULL DEFAULT 0 COMMENT '账号状态:[-1:禁用] [0:启用]',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `white_list_history`
-- ----------------------------
DROP TABLE IF EXISTS `white_list_history`;
CREATE TABLE `white_list_history` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `service` varchar(255) NOT NULL DEFAULT '' COMMENT '服务',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '添加时间',
  `created_by` int(11) NOT NULL DEFAULT 0 COMMENT '添加人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `white_list_info`
-- ----------------------------
DROP TABLE IF EXISTS `white_list_info`;
CREATE TABLE `white_list_info` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `serveice` varchar(255) NOT NULL DEFAULT '' COMMENT '服务名',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `zk_node`
-- ----------------------------
DROP TABLE IF EXISTS `zk_node`;
CREATE TABLE `zk_node` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `zk_host` varchar(20) NOT NULL DEFAULT '' COMMENT 'zk IP',
  `influxdb_host` varchar(15) NOT NULL DEFAULT '' COMMENT 'influxdb ip',
  `influxdb_user` varchar(50) NOT NULL DEFAULT '' COMMENT 'influxdb 用户名',
  `influxdb_pass` varchar(20) NOT NULL DEFAULT '' COMMENT 'influxdb 密码',
  `created_by` int(11) NOT NULL DEFAULT 0 COMMENT '添加人',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '添加时间',
  `updated_by` int(11) NOT NULL DEFAULT 0 COMMENT '最后更新人',
  `updated_at` timestamp NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp() COMMENT '最后更新时间',
  `remark` varchar(50) NOT NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COMMENT='zk节点配置';

SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO `users` VALUES ('1','admin', '9a8fe2f9f59093b02dade34aab295fd4', '0');
INSERT INTO `authorities` VALUES ('1', 'ROLE_ADMIN', 'admin');
