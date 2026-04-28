UPDATE `content_category` SET `category_name` = '科技', `description` = '科技相关内容' WHERE `category_id` = 1;
UPDATE `content_category` SET `category_name` = '娱乐', `description` = '娱乐相关内容' WHERE `category_id` = 2;
UPDATE `content_category` SET `category_name` = '体育', `description` = '体育相关内容' WHERE `category_id` = 3;
UPDATE `content_category` SET `category_name` = '资讯', `description` = '资讯相关内容' WHERE `category_id` = 4;

INSERT IGNORE INTO `content_category` (`category_id`, `category_name`, `parent_id`, `order_num`, `status`, `icon`, `description`, `content_count`, `create_by`, `create_time`) VALUES
(5, '教育', 0, 5, '0', 'edu', '教育相关内容', 0, 'admin', NOW()),
(6, '生活', 0, 6, '0', 'life', '生活相关内容', 0, 'admin', NOW()),
(7, '音乐', 0, 7, '0', 'music', '音乐相关内容', 0, 'admin', NOW()),
(8, '美食', 0, 8, '0', 'food', '美食相关内容', 0, 'admin', NOW());
