/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50720
Source Host           : localhost:3306
Source Database       : article

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-03-09 10:54:16
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `kjz_article`
-- ----------------------------
DROP TABLE IF EXISTS `kjz_article`;
CREATE TABLE `kjz_article` (
  `article_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '文章ID',
  `title` char(50) DEFAULT NULL COMMENT '文章标题',
  `content` text COMMENT '文章内容',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `clicks` int(50) DEFAULT NULL COMMENT '浏览次数',
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `tb_status` char(50) DEFAULT '正常' COMMENT '状态：正常，正常；删除，删除；',
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `type_name` char(50) DEFAULT NULL COMMENT '文章类型',
  PRIMARY KEY (`article_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of kjz_article
-- ----------------------------

-- ----------------------------
-- Table structure for `kjz_article_comment`
-- ----------------------------
DROP TABLE IF EXISTS `kjz_article_comment`;
CREATE TABLE `kjz_article_comment` (
  `comment_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `user_id` int(11) DEFAULT NULL COMMENT '用户昵称',
  `comment_content` text COMMENT '评论内容',
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `tb_status` char(255) DEFAULT NULL COMMENT '状态：正常，正常；删除，删除；',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `article_id` int(11) DEFAULT NULL COMMENT '文章ID',
  PRIMARY KEY (`comment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of kjz_article_comment
-- ----------------------------

-- ----------------------------
-- Table structure for `kjz_article_tags`
-- ----------------------------
DROP TABLE IF EXISTS `kjz_article_tags`;
CREATE TABLE `kjz_article_tags` (
  `article_tags_id` int(11) NOT NULL COMMENT '文章标签ID',
  `tags_id` int(11) NOT NULL,
  `article_id` int(11) NOT NULL,
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `tb_status` char(50) DEFAULT '正常' COMMENT '状态：正常，正常；删除，删除；',
  PRIMARY KEY (`article_tags_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of kjz_article_tags
-- ----------------------------

-- ----------------------------
-- Table structure for `kjz_article_type`
-- ----------------------------
DROP TABLE IF EXISTS `kjz_article_type`;
CREATE TABLE `kjz_article_type` (
  `type_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '类型编号',
  `type_name` char(50) DEFAULT NULL COMMENT '类型名称',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `tb_status` char(50) DEFAULT '正常' COMMENT '状态：正常，正常；删除，删除；',
  PRIMARY KEY (`type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of kjz_article_type
-- ----------------------------

-- ----------------------------
-- Table structure for `kjz_photo`
-- ----------------------------
DROP TABLE IF EXISTS `kjz_photo`;
CREATE TABLE `kjz_photo` (
  `photo_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '照片ID',
  `photo_name` char(50) DEFAULT NULL COMMENT '照片名称',
  `photo_src` char(50) DEFAULT NULL COMMENT '照片路径',
  `photo_description` text COMMENT '照片描述',
  `user_id` int(11) DEFAULT NULL COMMENT '用户ID',
  `tb_status` char(50) DEFAULT '正常' COMMENT '状态：正常，正常；删除，删除；',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `photo_type_name` char(255) DEFAULT NULL COMMENT '相册类型名',
  PRIMARY KEY (`photo_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of kjz_photo
-- ----------------------------

-- ----------------------------
-- Table structure for `kjz_photo_type`
-- ----------------------------
DROP TABLE IF EXISTS `kjz_photo_type`;
CREATE TABLE `kjz_photo_type` (
  `photo_type_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '相册类型ID',
  `photo_type_name` char(50) DEFAULT NULL COMMENT '相册类型名称',
  `tb_status` char(50) DEFAULT '正常' COMMENT '状态：正常，正常；删除，删除；停用，停用；',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`photo_type_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of kjz_photo_type
-- ----------------------------

-- ----------------------------
-- Table structure for `kjz_tags`
-- ----------------------------
DROP TABLE IF EXISTS `kjz_tags`;
CREATE TABLE `kjz_tags` (
  `tags_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '标签编号',
  `tags_name` char(100) DEFAULT NULL COMMENT '标签名',
  `tb_status` char(50) DEFAULT '正常' COMMENT '状态：正常，正常；删除，删除；',
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`tags_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of kjz_tags
-- ----------------------------

-- ----------------------------
-- Table structure for `kjz_user`
-- ----------------------------
DROP TABLE IF EXISTS `kjz_user`;
CREATE TABLE `kjz_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户编号',
  `nickname` char(100) DEFAULT NULL COMMENT '用户昵称',
  `password` char(100) DEFAULT NULL COMMENT '用户密码',
  `email` char(100) DEFAULT NULL COMMENT '用户邮箱',
  `phone_number` char(50) DEFAULT NULL COMMENT '用户电话',
  `salt` char(50) DEFAULT NULL COMMENT '调味盐',
  `level` char(50) DEFAULT NULL COMMENT '用户等级',
  `head_img` char(50) DEFAULT NULL COMMENT '用户头像',
  `create_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `tb_status` char(50) DEFAULT '正常' COMMENT '状态：正常，正常；删除，删除；停用，停用；',
  `modify_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of kjz_user
-- ----------------------------
