DROP TABLE IF EXISTS `authorities`;
CREATE TABLE `authorities`
(
  `id`        int(11) NOT NULL AUTO_INCREMENT,
  `username`  varchar(20) DEFAULT NULL,
  `authority` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for groups
-- ----------------------------
DROP TABLE IF EXISTS `groups`;
CREATE TABLE `groups`
(
  `id`         int(11) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for group_authorities
-- ----------------------------
DROP TABLE IF EXISTS `group_authorities`;
CREATE TABLE `group_authorities`
(
  `group_id`  int(11) NOT NULL AUTO_INCREMENT,
  `authority` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`group_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for group_members
-- ----------------------------
DROP TABLE IF EXISTS `group_members`;
CREATE TABLE `group_members`
(
  `id`       int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) DEFAULT NULL,
  `group_id` int(11)     DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`
(
  `id`       int(8) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL,
  `enabled`  tinyint(4)  DEFAULT NULL,
  `email`    varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8;