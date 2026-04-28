-- Fix content_view_history table: add create_time column and unique index
-- Run this if the table already exists but is missing columns/indexes

-- Drop and recreate if table structure is wrong
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
