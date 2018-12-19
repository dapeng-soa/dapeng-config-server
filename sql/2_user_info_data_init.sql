BEGIN;
--t_users | pwd:today-36524
INSERT INTO `t_users` VALUES ('1', 'admin', '9a8fe2f9f59093b02dade34aab295fd4', '0', '', '', '', '');
--t_role
INSERT INTO `t_role` VALUES ('1', 'ADMIN', '超级管理员'), ('2', 'OPS', '运维人员'), ('3', 'DEV', '开发人员'), ('4', 'NORMAL', '普通账户');
--t_role_user
INSERT INTO `t_role_user` VALUES ('1', '1', '1'), ('2', '2', '1'), ('3', '3', '1'), ('4', '4', '1');
--t_permissions
INSERT INTO `t_permissions` VALUES ('10000', '主页', '100', '0', '0', '用户主页', '2018-11-13 16:18:41', '2018-11-13 18:09:36', '1', '1', '/me');
--t_role_permission
INSERT INTO `t_role_permission` VALUES ('10000', '1', '10000', '管理员主页关联', '2018-11-13 16:20:55', '2018-11-13 16:20:55', '1', '1');
COMMIT;