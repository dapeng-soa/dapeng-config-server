SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `api_key_info`
-- ----------------------------
DROP TABLE IF EXISTS `api_key_info`;
CREATE TABLE `api_key_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `api_key` varchar(100) NOT NULL COMMENT 'apiKey',
  `password` varchar(12) NOT NULL COMMENT 'apikey对应密码',
  `ips` varchar(255) NOT NULL DEFAULT '*' COMMENT 'ip规则,单个ip,多个ip用逗号隔开，掩码，*默认',
  `created_at` datetime NOT NULL COMMENT '添加时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `created_by` int(11) NOT NULL COMMENT '添加人',
  `updated_by` int(11) NOT NULL COMMENT '最后更新人',
  `notes` varchar(255) DEFAULT NULL COMMENT '描述',
  `biz` varchar(100) NOT NULL DEFAULT '' COMMENT '业务',
  PRIMARY KEY (`id`),
  UNIQUE KEY `api_key_unique` (`api_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `config_info`
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '配置id',
  `service_name` varchar(100) NOT NULL COMMENT '服务全限定名',
  `status` tinyint(2) NOT NULL DEFAULT '1' COMMENT '发布状态,0:无效,1:新建,2:审核通过,3:已发布（前期默认是审核通过）',
  `timeout_config` text NOT NULL COMMENT '超时配置',
  `loadbalance_config` text NOT NULL COMMENT '负载均衡配置',
  `router_config` text NOT NULL COMMENT '路由配置',
  `freq_config` text NOT NULL COMMENT '限流配置',
  `created_by` int(11) NOT NULL DEFAULT '0' COMMENT '添加人',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '添加时间',
  `updated_by` int(11) NOT NULL DEFAULT '0' COMMENT '最后更新人',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `remark` text NOT NULL COMMENT '备注',
  `tags` varchar(255) NOT NULL DEFAULT '' COMMENT '标签',
  `cookie_config` text COMMENT 'cooike配置',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4;


-- ----------------------------
--  Table structure for `config_publish_history`
-- ----------------------------
DROP TABLE IF EXISTS `config_publish_history`;
CREATE TABLE `config_publish_history` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '配置id',
  `version` varchar(50) NOT NULL COMMENT '版本号，每次发布的版本号(年月日时分秒)',
  `service_name` varchar(100) NOT NULL COMMENT '服务全限定名',
  `timeout_config` text NOT NULL COMMENT '超时配置',
  `loadbalance_config` text NOT NULL COMMENT '负载均衡配置',
  `router_config` text NOT NULL COMMENT '路由配置',
  `freq_config` text NOT NULL COMMENT '限流配置',
  `published_by` int(11) NOT NULL DEFAULT '0' COMMENT '发布人(发布人默认为0,实为未发布状态，发布更新此id)',
  `published_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
  `remark` text NOT NULL,
  `cookie_config` text COMMENT 'cookie配置',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8mb4;


-- ----------------------------
--  Table structure for `t_deploy_unit`
-- ----------------------------
DROP TABLE IF EXISTS `t_deploy_unit`;
CREATE TABLE `t_deploy_unit` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `set_id` int(11) NOT NULL DEFAULT '0' COMMENT '环境集id',
  `host_id` int(11) NOT NULL DEFAULT '0' COMMENT '节点id',
  `service_id` int(11) DEFAULT '0' COMMENT '服务id',
  `git_tag` varchar(45) NOT NULL DEFAULT '' COMMENT '部署的tag号',
  `image_tag` varchar(45) NOT NULL DEFAULT '' COMMENT '镜像tag',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `env` text COMMENT '环境变量',
  `ports` varchar(45) DEFAULT 'NULL' COMMENT '端口映射',
  `volumes` varchar(45) DEFAULT 'NULL' COMMENT '卷挂载',
  `docker_extras` varchar(1024) DEFAULT 'NULL' COMMENT '如：restart: on-failure:3',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态[0:有效,1:已删除]',
  `container_name` varchar(45) NOT NULL DEFAULT '' COMMENT '容器名',
  `branch` varchar(45) NOT NULL DEFAULT '' COMMENT '服务部署分支',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=63 DEFAULT CHARSET=utf8mb4 COMMENT='部署单元';


-- ----------------------------
--  Table structure for `t_files_unit`
-- ----------------------------
DROP TABLE IF EXISTS `t_files_unit`;
CREATE TABLE `t_files_unit` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `file_id` int(11) NOT NULL DEFAULT '0' COMMENT '文件id',
  `unit_id` int(11) NOT NULL DEFAULT '0' COMMENT '部署单元id',
  `create_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加关联时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COMMENT='t_service_files 文件表与部署单元关联表';

-- ----------------------------
--  Table structure for `t_host`
-- ----------------------------
DROP TABLE IF EXISTS `t_host`;
CREATE TABLE `t_host` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(45) NOT NULL DEFAULT '' COMMENT '节点(宿主机)名称, 例如app-5',
  `set_id` int(11) NOT NULL DEFAULT '0' COMMENT '绑定的环境集id',
  `ip` int(11) NOT NULL DEFAULT '0' COMMENT '该节点IP信息',
  `labels` varchar(45) DEFAULT NULL COMMENT '逗号分隔的标签,用于分类',
  `extra` smallint(2) NOT NULL DEFAULT '0' COMMENT '是否外部机器. 内部机器用于部署, 外部机器用于配置extra_host',
  `env` text COMMENT '环境变量',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(255) DEFAULT 'NULL' COMMENT '备注',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态[0:有效,1:已删除]',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_host_ip` (`ip`) USING BTREE,
  UNIQUE KEY `uniq_host_name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COMMENT='节点信息表';


-- ----------------------------
--  Table structure for `t_network`
-- ----------------------------
DROP TABLE IF EXISTS `t_network`;
CREATE TABLE `t_network` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `network_name` varchar(48) NOT NULL DEFAULT '' COMMENT '网络名称',
  `driver` varchar(48) NOT NULL DEFAULT 'bridge' COMMENT '网卡',
  `subnet` varchar(48) NOT NULL DEFAULT '' COMMENT '网段',
  `opt` varchar(1024) NOT NULL DEFAULT '' COMMENT 'driver-opt:(k:v=> 例如 : com.docker.network.driver.mtu=1450)，多个值可换行区分',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态[0:有效,1:已删除]',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='Docker网络表，定义网络配置';


-- ----------------------------
--  Table structure for `t_network_host`
-- ----------------------------
DROP TABLE IF EXISTS `t_network_host`;
CREATE TABLE `t_network_host` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `net_id` int(11) NOT NULL DEFAULT '0' COMMENT '网络id',
  `host_id` int(11) NOT NULL DEFAULT '0' COMMENT '节点id',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '关联时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='网络节点关联';


-- ----------------------------
--  Table structure for `t_op_log`
-- ----------------------------
DROP TABLE IF EXISTS `t_op_log`;
CREATE TABLE `t_op_log` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `oper_name` varchar(24) NOT NULL DEFAULT '' COMMENT '操作名(方法名)',
  `operator` varchar(24) NOT NULL DEFAULT '' COMMENT '操作人(当前登陆账号）',
  `oper_params` text COMMENT '操作参数',
  `oper_result` varchar(11) NOT NULL DEFAULT '' COMMENT '操作结果(状态码)',
  `result_msg` text COMMENT '结果消息',
  `oper_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=289 DEFAULT CHARSET=utf8mb4 COMMENT='操作记录表';


-- ----------------------------
--  Table structure for `t_operation_journal`
-- ----------------------------
DROP TABLE IF EXISTS `t_operation_journal`;
CREATE TABLE `t_operation_journal` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `set_id` int(11) NOT NULL DEFAULT '0' COMMENT '环境集id',
  `host_id` int(11) NOT NULL DEFAULT '0' COMMENT '节点id',
  `service_id` int(11) NOT NULL DEFAULT '0' COMMENT '服务id',
  `git_tag` varchar(45) NOT NULL DEFAULT '' COMMENT '部署的tag号',
  `image_tag` varchar(45) DEFAULT '' COMMENT '镜像tag',
  `yml` text COMMENT '本次部署的yaml文件',
  `diff` varchar(4096) DEFAULT '' COMMENT 'yml diff',
  `op_flag` smallint(2) NOT NULL DEFAULT '0' COMMENT '操作列表,1:升级(update);2:重启(restart);3:停止(stop);4:降级(rollback)',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `created_by` varchar(45) NOT NULL DEFAULT 'admin' COMMENT '操作人',
  `unit_id` int(11) NOT NULL DEFAULT '0' COMMENT '部署单元id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=395 DEFAULT CHARSET=utf8mb4 COMMENT='部署流水表';


-- ----------------------------
--  Table structure for `t_permissions`
-- ----------------------------
DROP TABLE IF EXISTS `t_permissions`;
CREATE TABLE `t_permissions` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(255) NOT NULL DEFAULT '' COMMENT '名称',
  `code` varchar(128) NOT NULL DEFAULT '' COMMENT '编码, 一级标签 1xx, 二级标签1xxxx, 三级标签1xxxxxx',
  `parent_permission_id` int(11) DEFAULT NULL COMMENT '父权限',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '权限状态; 0: 可用，1: 不可用',
  `remark` varchar(1024) NOT NULL DEFAULT '' COMMENT '权限备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created_by` int(11) NOT NULL DEFAULT '1' COMMENT '创建者staff_id',
  `updated_by` int(11) NOT NULL DEFAULT '1' COMMENT '修改者staff_id',
  `url` varchar(255) NOT NULL DEFAULT '' COMMENT 'url权限, like: /me',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_code` (`code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10001 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='权限表';

-- ----------------------------
--  Table structure for `t_users`
-- ----------------------------
DROP TABLE IF EXISTS `t_users`;
CREATE TABLE `t_users` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `username` varchar(50) NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(100) NOT NULL DEFAULT '' COMMENT '密码',
  `enabled` tinyint(4) NOT NULL DEFAULT '0' COMMENT '账号状态:[-1:禁用] [0:启用]',
  `email` varchar(50) NOT NULL DEFAULT '' COMMENT '邮箱',
  `tel` varchar(11) NOT NULL DEFAULT '' COMMENT '联系电话',
  `nickname` varchar(64) NOT NULL DEFAULT '' COMMENT '账户昵称',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_username` (`username`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';


-- ----------------------------
--  Table structure for `t_role`
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `role` varchar(24) NOT NULL DEFAULT '' COMMENT '角色[ADMIN-超级管理员,OPS-运维,DEV-开发,NORMAL-普通]',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_role` (`role`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='用户角色表';


-- ----------------------------
--  Table structure for `t_role_permission`
-- ----------------------------
DROP TABLE IF EXISTS `t_role_permission`;
CREATE TABLE `t_role_permission` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `role_id` int(11) NOT NULL DEFAULT '0' COMMENT '角色ID',
  `permission_id` int(11) NOT NULL DEFAULT '0' COMMENT '权限ID',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `created_by` int(11) NOT NULL DEFAULT '1' COMMENT '创建者staff_id',
  `updated_by` int(11) NOT NULL DEFAULT '1' COMMENT '修改者staff_id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uniq_role_permission` (`role_id`,`permission_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10001 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='角色权限中间表';


-- ----------------------------
--  Table structure for `t_role_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_role_user`;
CREATE TABLE `t_role_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `role_id` int(11) NOT NULL DEFAULT '0' COMMENT '角色id',
  `user_id` int(11) NOT NULL DEFAULT '0' COMMENT '用户id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- ----------------------------
--  Table structure for `t_service`
-- ----------------------------
DROP TABLE IF EXISTS `t_service`;
CREATE TABLE `t_service` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(45) NOT NULL DEFAULT '' COMMENT '服务名称',
  `image` varchar(128) NOT NULL DEFAULT '' COMMENT '镜像',
  `labels` varchar(255) DEFAULT NULL COMMENT '逗号分隔的标签,用于分类',
  `env` text COMMENT '环境变量',
  `volumes` varchar(1024) DEFAULT 'NULL' COMMENT '挂载卷',
  `ports` varchar(1024) DEFAULT 'NULL' COMMENT '端口映射',
  `compose_labels` varchar(4096) DEFAULT NULL COMMENT 'labels node of source-compose',
  `docker_extras` varchar(1024) DEFAULT NULL COMMENT '例如restart, cmd等',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(255) DEFAULT 'NULL' COMMENT '备注',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态[0:有效,1:已删除]',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_service_name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8mb4 COMMENT='服务信息表\nenv优先级: service<set<host<deploy_unit';

-- ----------------------------
--  Table structure for `t_service_build_records`
-- ----------------------------
DROP TABLE IF EXISTS `t_service_build_records`;
CREATE TABLE `t_service_build_records` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `agent_host` varchar(32) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT 'agent ip',
  `build_service` varchar(255) COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '构建服务名',
  `task_id` int(11) NOT NULL DEFAULT '0' COMMENT '构建任务id',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '服务构建状态',
  `build_log` mediumtext COLLATE utf8mb4_bin COMMENT '服务构建日志',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` int(11) NOT NULL DEFAULT '0' COMMENT '创建人',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=157 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='服务构建记录';


-- ----------------------------
--  Table structure for `t_service_files`
-- ----------------------------
DROP TABLE IF EXISTS `t_service_files`;
CREATE TABLE `t_service_files` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `file_name` varchar(255) NOT NULL DEFAULT '' COMMENT '容器内的映射文件/文件夹名(包含路径[如：/data/config/config.ini | /data/config/])',
  `file_ext_name` varchar(255) NOT NULL DEFAULT '' COMMENT '宿主机的映射文件/文件夹名(文件夹应当写全路径/文件只写文件名)',
  `file_context` text COMMENT '文件内容，最终将映射到容器内',
  `file_tag` varchar(32) NOT NULL DEFAULT '' COMMENT '文件内容MD5(16位),描述了宿主机上的一部分文件名，文件内容变更，此字段变更',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态[0:有效,1:已删除]',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COMMENT='服务配置文件表';


-- ----------------------------
--  Table structure for `t_set`
-- ----------------------------
DROP TABLE IF EXISTS `t_set`;
CREATE TABLE `t_set` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `name` varchar(45) NOT NULL DEFAULT '' COMMENT '环境集名称, 例如sandbox1, demo, onlineDemo, ',
  `env` text COMMENT '环境变量',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `remark` varchar(255) DEFAULT 'NULL' COMMENT '备注',
  `network_mtu` varchar(11) NOT NULL DEFAULT '1500' COMMENT '网络MTU值，默认为1500',
  `deleted` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除状态[0:有效,1:已删除]',
  `build_host` int(11) NOT NULL DEFAULT '0' COMMENT '构建主机',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_set_name` (`name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='环境集信息表';

-- ----------------------------
--  Table structure for `t_set_service_env`
-- ----------------------------
DROP TABLE IF EXISTS `t_set_service_env`;
CREATE TABLE `t_set_service_env` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `set_id` int(11) NOT NULL DEFAULT '0' COMMENT '环境集id',
  `service_id` int(11) NOT NULL DEFAULT '0' COMMENT '服务id',
  `env` text COMMENT '当前环境集中某服务的环境变量',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COMMENT='环境集服务环境变量';


-- ----------------------------
--  Table structure for `white_list_history`
-- ----------------------------
DROP TABLE IF EXISTS `white_list_history`;
CREATE TABLE `white_list_history` (
  `id` int(11) NOT NULL,
  `service` varchar(255) NOT NULL DEFAULT '' COMMENT '服务',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '添加时间',
  `created_by` int(11) NOT NULL DEFAULT '0' COMMENT '添加人',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
--  Table structure for `white_list_info`
-- ----------------------------
DROP TABLE IF EXISTS `white_list_info`;
CREATE TABLE `white_list_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
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
  `created_by` int(11) NOT NULL DEFAULT '0' COMMENT '添加人',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '添加时间',
  `updated_by` int(11) NOT NULL DEFAULT '0' COMMENT '最后更新人',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `remark` varchar(50) NOT NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COMMENT='zk节点配置';

SET FOREIGN_KEY_CHECKS = 1;
