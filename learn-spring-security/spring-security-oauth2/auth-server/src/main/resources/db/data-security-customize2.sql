-- ----------------------------
-- Records of s_user
-- ----------------------------
INSERT INTO `s_user` VALUES ('1', 'admin', 'admin');
INSERT INTO `s_user` VALUES ('2', 'veiking', 'veiking');
INSERT INTO `s_user` VALUES ('3', 'xiaoming', 'xiaoming');

-- ----------------------------
-- Records of s_user_role
-- ----------------------------
INSERT INTO `s_user_role` VALUES ('1', '1');
INSERT INTO `s_user_role` VALUES ('2', '2');

-- ----------------------------
-- Records of s_role
-- ----------------------------
INSERT INTO `s_role` VALUES ('1', 'R_ADMIN', '大总管，所有权限');
INSERT INTO `s_role` VALUES ('2', 'R_HELLO', '说hello相关的权限');

-- ----------------------------
-- Records of s_role_permission
-- ----------------------------
INSERT INTO `s_role_permission` VALUES ('1', '1');
INSERT INTO `s_role_permission` VALUES ('1', '2');
INSERT INTO `s_role_permission` VALUES ('1', '3');
INSERT INTO `s_role_permission` VALUES ('2', '1');
INSERT INTO `s_role_permission` VALUES ('2', '3');

-- ----------------------------
-- Records of s_permission
-- ----------------------------
INSERT INTO `s_permission` VALUES ('1', 'P_INDEX', '/index', 'index页面资源');
INSERT INTO `s_permission` VALUES ('2', 'P_ADMIN', '/admin', 'admin页面资源');
INSERT INTO `s_permission` VALUES ('3', 'P_HELLO', '/hello', 'hello页面资源');