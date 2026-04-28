-- ----------------------------
-- C-End Content Distribution Module Database Structure
-- ----------------------------

-- ----------------------------
-- Table structure for content_category
-- ----------------------------
DROP TABLE IF EXISTS `content_category`;
CREATE TABLE `content_category` (
  `category_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Category ID',
  `category_name` varchar(50) NOT NULL COMMENT 'Category Name',
  `parent_id` bigint(20) DEFAULT '0' COMMENT 'Parent Category ID',
  `order_num` int(4) DEFAULT '0' COMMENT 'Display Order',
  `status` char(1) DEFAULT '0' COMMENT 'Category Status (0-normal 1-disabled)',
  `icon` varchar(100) DEFAULT '' COMMENT 'Category Icon',
  `description` varchar(500) DEFAULT '' COMMENT 'Category Description',
  `content_count` int(8) DEFAULT '0' COMMENT 'Content Count',
  `create_by` varchar(64) DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  `remark` varchar(500) DEFAULT NULL COMMENT 'Remark',
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='C-End Content Category';

-- ----------------------------
-- Table structure for content_info
-- ----------------------------
DROP TABLE IF EXISTS `content_info`;
CREATE TABLE `content_info` (
  `content_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Content ID',
  `title` varchar(200) NOT NULL COMMENT 'Content Title',
  `summary` varchar(500) DEFAULT '' COMMENT 'Content Summary',
  `content` longtext COMMENT 'Content Content',
  `category_id` bigint(20) DEFAULT NULL COMMENT 'Category ID',
  `author` varchar(50) DEFAULT '' COMMENT 'Author',
  `cover_image` varchar(200) DEFAULT '' COMMENT 'Cover Image',
  `content_type` char(1) DEFAULT '1' COMMENT 'Content Type (1-article 2-video 3-image 4-audio)',
  `status` char(1) DEFAULT '0' COMMENT 'Content Status (0-draft 1-published 2-offline)',
  `view_count` bigint(20) DEFAULT '0' COMMENT 'View Count',
  `like_count` bigint(20) DEFAULT '0' COMMENT 'Like Count',
  `comment_count` bigint(20) DEFAULT '0' COMMENT 'Comment Count',
  `share_count` bigint(20) DEFAULT '0' COMMENT 'Share Count',
  `is_recommended` char(1) DEFAULT '0' COMMENT 'Is Recommended (0-no 1-yes)',
  `is_top` char(1) DEFAULT '0' COMMENT 'Is Top (0-no 1-yes)',
  `publish_time` datetime DEFAULT NULL COMMENT 'Publish Time',
  `tags` varchar(200) DEFAULT '' COMMENT 'Tags',
  `source_url` varchar(500) DEFAULT '' COMMENT 'Source URL',
  `create_by` varchar(64) DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  `remark` varchar(500) DEFAULT NULL COMMENT 'Remark',
  PRIMARY KEY (`content_id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_status` (`status`),
  KEY `idx_publish_time` (`publish_time`),
  KEY `idx_view_count` (`view_count`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='C-End Content Info';

-- ----------------------------
-- Table structure for user_favorite
-- ----------------------------
DROP TABLE IF EXISTS `user_favorite`;
CREATE TABLE `user_favorite` (
  `favorite_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Favorite ID',
  `user_id` bigint(20) NOT NULL COMMENT 'User ID',
  `content_id` bigint(20) NOT NULL COMMENT 'Content ID',
  `favorite_time` datetime DEFAULT NULL COMMENT 'Favorite Time',
  `create_by` varchar(64) DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  `remark` varchar(500) DEFAULT NULL COMMENT 'Remark',
  PRIMARY KEY (`favorite_id`),
  UNIQUE KEY `uk_user_content` (`user_id`, `content_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_content_id` (`content_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='C-End User Favorite';

-- ----------------------------
-- Table structure for content_view_history
-- ----------------------------
DROP TABLE IF EXISTS `content_view_history`;
CREATE TABLE `content_view_history` (
  `history_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'History ID',
  `user_id` bigint(20) NOT NULL COMMENT 'User ID',
  `content_id` bigint(20) NOT NULL COMMENT 'Content ID',
  `view_time` datetime DEFAULT NULL COMMENT 'View Time',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  PRIMARY KEY (`history_id`),
  UNIQUE KEY `uk_user_content` (`user_id`, `content_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_content_id` (`content_id`),
  KEY `idx_view_time` (`view_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='C-End Content View History';

-- ----------------------------
-- Table structure for content_comment
-- ----------------------------
DROP TABLE IF EXISTS `content_comment`;
CREATE TABLE `content_comment` (
  `comment_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'Comment ID',
  `content_id` bigint(20) NOT NULL COMMENT 'Content ID',
  `user_id` bigint(20) NOT NULL COMMENT 'User ID',
  `parent_id` bigint(20) DEFAULT '0' COMMENT 'Parent Comment ID',
  `content` text NOT NULL COMMENT 'Comment Content',
  `status` char(1) DEFAULT '0' COMMENT 'Status (0-normal 1-hidden)',
  `create_by` varchar(64) DEFAULT '' COMMENT 'Creator',
  `create_time` datetime DEFAULT NULL COMMENT 'Create Time',
  `update_by` varchar(64) DEFAULT '' COMMENT 'Updater',
  `update_time` datetime DEFAULT NULL COMMENT 'Update Time',
  `remark` varchar(500) DEFAULT NULL COMMENT 'Remark',
  PRIMARY KEY (`comment_id`),
  KEY `idx_content_id` (`content_id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='C-End Content Comment';

-- ----------------------------
-- Insert initial data
-- ----------------------------
INSERT INTO `content_category` VALUES (1, '科技', 0, 1, '0', 'tech', '科技相关内容', 0, 'admin', NOW(), '', NULL, NULL);
INSERT INTO `content_category` VALUES (2, '娱乐', 0, 2, '0', 'ent', '娱乐相关内容', 0, 'admin', NOW(), '', NULL, NULL);
INSERT INTO `content_category` VALUES (3, '体育', 0, 3, '0', 'sports', '体育相关内容', 0, 'admin', NOW(), '', NULL, NULL);
INSERT INTO `content_category` VALUES (4, '资讯', 0, 4, '0', 'news', '资讯相关内容', 0, 'admin', NOW(), '', NULL, NULL);
INSERT INTO `content_category` VALUES (5, '教育', 0, 5, '0', 'edu', '教育相关内容', 0, 'admin', NOW(), '', NULL, NULL);
INSERT INTO `content_category` VALUES (6, '生活', 0, 6, '0', 'life', '生活相关内容', 0, 'admin', NOW(), '', NULL, NULL);
INSERT INTO `content_category` VALUES (7, '音乐', 0, 7, '0', 'music', '音乐相关内容', 0, 'admin', NOW(), '', NULL, NULL);
INSERT INTO `content_category` VALUES (8, '美食', 0, 8, '0', 'food', '美食相关内容', 0, 'admin', NOW(), '', NULL, NULL);

INSERT INTO `content_info` VALUES (1, 'Welcome to C-End Content Platform', 'This is a sample content for demonstration', 'Welcome to our C-End content distribution platform. This platform provides various types of content including articles, videos, images, and audio content for our users.', 1, 'Admin', '', '1', '1', 0, 0, 0, 0, '1', '0', NOW(), 'welcome,demo,platform', '', 'admin', NOW(), '', NULL, NULL);
INSERT INTO `content_info` VALUES (2, 'How to Use Our Platform', 'A guide on how to use our content platform', 'This is a comprehensive guide on how to use our C-End content distribution platform. You can browse content by category, search for specific content, and manage your favorites.', 1, 'Admin', '', '1', '1', 0, 0, 0, 0, '0', '0', NOW(), 'guide,tutorial,help', '', 'admin', NOW(), '', NULL, NULL);
