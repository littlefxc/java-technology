/*
Navicat MySQL Data Transfer

Source Server         : 192.168.212.59
Source Server Version : 50505
Source Host           : 192.168.212.59:3306
Source Database       : awifi_capacity_rbac

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2019-03-29 14:26:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(12) NOT NULL,
  `name` varchar(64) NOT NULL,
  `url` varchar(1024) DEFAULT NULL,
  `path` varchar(1024) DEFAULT NULL,
  `icon` varchar(32) DEFAULT NULL COMMENT '图标',
  `sort` int(11) NOT NULL,
  `create_by` varchar(20) DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_by` varchar(20) DEFAULT NULL COMMENT '修改人',
  `modify_date` datetime DEFAULT NULL COMMENT '修改时间',
  `hidden` int(11) DEFAULT NULL COMMENT '是否隐藏,0 false 1 true',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL COMMENT '资源编码',
  `type` varchar(255) DEFAULT NULL COMMENT '资源类型 Buttion,interface',
  `name` varchar(255) DEFAULT NULL COMMENT '资源名称',
  `uri` varchar(255) DEFAULT NULL COMMENT '资源路径',
  `menu_id` varchar(255) DEFAULT NULL COMMENT '资源关联菜单',
  `parent_id` varchar(255) DEFAULT NULL,
  `path` varchar(2000) DEFAULT NULL COMMENT '资源树状检索路径',
  `method` varchar(10) DEFAULT NULL COMMENT '资源请求类型',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_by` varchar(20) DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_by` varchar(20) DEFAULT NULL COMMENT '修改人',
  `modify_date` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=51 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT,
  `code` varchar(32) NOT NULL COMMENT '角色code',
  `name` varchar(50) NOT NULL COMMENT '角色名',
  `create_by` varchar(20) DEFAULT NULL COMMENT '创建人',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `modify_by` varchar(20) DEFAULT NULL COMMENT '修改人',
  `modify_date` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` bigint(12) NOT NULL COMMENT '角色ID',
  `menu_id` bigint(12) NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`role_id`,`menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='角色菜单表';

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `role_id` bigint(12) NOT NULL,
  `permission_id` bigint(12) NOT NULL,
  PRIMARY KEY (`role_id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_role_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_user`;
CREATE TABLE `sys_role_user` (
  `user_id` bigint(12) NOT NULL,
  `role_id` bigint(12) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_user_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_permission`;
CREATE TABLE `sys_user_permission` (
  `user_id` bigint(12) NOT NULL,
  `permission_id` bigint(12) NOT NULL,
  PRIMARY KEY (`user_id`,`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
