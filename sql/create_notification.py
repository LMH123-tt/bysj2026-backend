import pymysql

conn = pymysql.connect(
    host='localhost', port=3306, user='root', password='118829',
    database='ry-cloud', charset='utf8mb4', autocommit=True
)
cursor = conn.cursor()

cursor.execute("""
CREATE TABLE IF NOT EXISTS `content_notification` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '接收通知的用户ID',
  `from_user_id` bigint(20) DEFAULT NULL COMMENT '触发通知的用户ID',
  `type` varchar(20) NOT NULL COMMENT '通知类型: friend_request/friend_accept/comment/like/system',
  `title` varchar(100) NOT NULL COMMENT '通知标题',
  `content` varchar(500) DEFAULT '' COMMENT '通知内容',
  `related_id` bigint(20) DEFAULT NULL COMMENT '关联业务ID',
  `is_read` char(1) DEFAULT '0' COMMENT '是否已读: 0未读 1已读',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_is_read` (`is_read`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='C-End User Notification'
""")
print("Table content_notification created")

cursor.close()
conn.close()
