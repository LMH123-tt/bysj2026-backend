-- ----------------------------
-- Content Module C-End User Tables
-- ----------------------------

-- ----------------------------
-- Table structure for content_user
-- ----------------------------
DROP TABLE IF EXISTS `content_user`;
CREATE TABLE `content_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'User ID',
  `user_name` varchar(30) NOT NULL COMMENT 'Username',
  `nick_name` varchar(30) NOT NULL COMMENT 'Nickname',
  `user_type` varchar(2) DEFAULT '01' COMMENT 'User Type (01-content user)',
  `email` varchar(50) DEFAULT '' COMMENT 'Email',
  `phonenumber` varchar(11) DEFAULT '' COMMENT 'Phone Number',
  `sex` char(1) DEFAULT '0' COMMENT 'Gender (0-male 1-female 2-unknown)',
  `avatar` varchar(100) DEFAULT '' COMMENT 'Avatar URL',
  `password` varchar(100) DEFAULT '' COMMENT 'Password',
  `status` char(1) DEFAULT '0' COMMENT 'Status (0-normal 1-disabled)',
  `del_flag` char(1) DEFAULT '0' COMMENT 'Delete Flag (0-exist 2-deleted)',
  `login_ip` varchar(128) DEFAULT '' COMMENT 'Last Login IP',
  `login_date` datetime DEFAULT NULL COMMENT 'Last Login Time',
  `pwd_update_date` datetime DEFAULT NULL COMMENT 'Password Update Time',
  `create_by` varchar(64) DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  `remark` varchar(500) DEFAULT NULL COMMENT 'Remark',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_username` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='Content C-End User Info';

-- ----------------------------
-- Insert initial data
-- ----------------------------
INSERT INTO `content_user` VALUES (1, 'content_user', 'Content User', '01', 'content@ry.com', '13888888888', '1', '', '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2', '0', '0', '127.0.0.1', NOW(), NOW(), 'admin', NOW(), '', NULL, 'Content C-End Test User');
