-- ----------------------------
-- Table structure for `s_user`
-- ----------------------------
DROP TABLE IF EXISTS `s_user`;
CREATE TABLE `s_user`
(
  `id`       int(11) NOT NULL,
  `name`     varchar(32) DEFAULT NULL,
  `password` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for `s_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `s_user_role`;
CREATE TABLE `s_user_role`
(
  `fk_user_id` int(11) DEFAULT NULL,
  `fk_role_id` int(11) DEFAULT NULL,
  KEY `union_key` (`fk_user_id`, `fk_role_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for `s_role`
-- ----------------------------
DROP TABLE IF EXISTS `s_role`;
CREATE TABLE `s_role`
(
  `id`       int(11) NOT NULL,
  `role`     varchar(32) DEFAULT NULL,
  `describe` varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for `s_role_permission`
-- ----------------------------
DROP TABLE IF EXISTS `s_role_permission`;
CREATE TABLE `s_role_permission`
(
  `fk_role_id`       int(11) DEFAULT NULL,
  `fk_permission_id` int(11) DEFAULT NULL,
  KEY `union_key` (`fk_role_id`, `fk_permission_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for `s_permission`
-- ----------------------------
DROP TABLE IF EXISTS `s_permission`;
CREATE TABLE `s_permission`
(
  `id`         int(11) NOT NULL,
  `permission` varchar(32) DEFAULT NULL,
  `url`        varchar(32) DEFAULT NULL,
  `describe`   varchar(32) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;