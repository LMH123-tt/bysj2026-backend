-- =============================================
-- 内容分发平台 - 管理端菜单重构SQL
-- 在 Navicat 或 IDEA 中执行此 SQL
-- =============================================

-- 1. 删除不需要的顶级菜单及其子菜单
DELETE FROM sys_menu WHERE menu_id = 3;
DELETE FROM sys_menu WHERE parent_id = 3;
DELETE FROM sys_menu WHERE menu_id IN (116, 105);

DELETE FROM sys_menu WHERE menu_id = 104;
DELETE FROM sys_menu WHERE parent_id = 104;

DELETE FROM sys_menu WHERE menu_id = 103;
DELETE FROM sys_menu WHERE parent_id = 103;

DELETE FROM sys_menu WHERE menu_id = 106;
DELETE FROM sys_menu WHERE parent_id = 106;

DELETE FROM sys_menu WHERE menu_id = 102;
DELETE FROM sys_menu WHERE parent_id = 102;

DELETE FROM sys_menu WHERE menu_id = 110;
DELETE FROM sys_menu WHERE parent_id IN (110, 111, 112);

DELETE FROM sys_menu WHERE menu_id = 107;
DELETE FROM sys_menu WHERE parent_id = 107;

DELETE FROM sys_menu WHERE menu_id = 113;
DELETE FROM sys_menu WHERE parent_id = 113;

DELETE FROM sys_menu WHERE menu_id = 114;
DELETE FROM sys_menu WHERE parent_id = 114;

DELETE FROM sys_menu WHERE menu_id = 115;
DELETE FROM sys_menu WHERE parent_id = 115;

-- 2. 修改保留菜单的名称和排序
UPDATE sys_menu SET menu_name = '平台管理', order_num = 2, icon = 'peoples' WHERE menu_id = 1;
UPDATE sys_menu SET order_num = 3 WHERE menu_id = 2;

UPDATE sys_menu SET order_num = 1 WHERE menu_id = 100;
UPDATE sys_menu SET order_num = 2 WHERE menu_id = 101;

UPDATE sys_menu SET order_num = 5 WHERE menu_id = 108;
UPDATE sys_menu SET order_num = 1, parent_id = 2, path = 'operlog', component = 'monitor/operlog/index', perms = 'monitor:operlog:list' WHERE menu_id = 500;
UPDATE sys_menu SET order_num = 2, parent_id = 2, path = 'logininfor', component = 'monitor/logininfor/index', perms = 'monitor:logininfor:list' WHERE menu_id = 501;

UPDATE sys_menu SET order_num = 1 WHERE menu_id = 109;

-- 3. 添加内容管理菜单
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
SELECT '内容管理', 0, 1, 'content', NULL, 'M', '0', '0', '', 'documentation', 'admin', sysdate(), '', NULL, '内容管理目录'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_name = '内容管理' AND parent_id = 0);

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '内容审核', (SELECT menu_id FROM sys_menu WHERE menu_name = '内容管理' AND parent_id = 0 LIMIT 1), 1, 'audit', 'content/audit/index', 'C', '0', '0', 'content:info:edit', 'checkbox', 'admin', sysdate(), '内容审核菜单'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_name = '内容审核');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '内容列表', (SELECT menu_id FROM sys_menu WHERE menu_name = '内容管理' AND parent_id = 0 LIMIT 1), 2, 'info', 'content/info/index', 'C', '0', '0', 'content:info:list', 'list', 'admin', sysdate(), '内容列表菜单'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_name = '内容列表');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '分类管理', (SELECT menu_id FROM sys_menu WHERE menu_name = '内容管理' AND parent_id = 0 LIMIT 1), 3, 'category', 'content/category/index', 'C', '0', '0', 'content:category:list', 'tree-table', 'admin', sysdate(), '分类管理菜单'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_name = '分类管理');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '评论管理', (SELECT menu_id FROM sys_menu WHERE menu_name = '内容管理' AND parent_id = 0 LIMIT 1), 4, 'comment', 'content/comment/index', 'C', '0', '0', 'content:comment:list', 'message', 'admin', sysdate(), '评论管理菜单'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_name = '评论管理');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT 'C端用户', (SELECT menu_id FROM sys_menu WHERE menu_name = '内容管理' AND parent_id = 0 LIMIT 1), 5, 'user', 'content/user/index', 'C', '0', '0', 'content:user:list', 'peoples', 'admin', sysdate(), 'C端用户管理菜单'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_name = 'C端用户');

-- 4. 添加内容管理按钮权限
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '内容查询', (SELECT menu_id FROM sys_menu WHERE menu_name = '内容列表' LIMIT 1), 1, '#', '', 'F', '0', '0', 'content:info:query', '#', 'admin', sysdate(), ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'content:info:query');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '内容新增', (SELECT menu_id FROM sys_menu WHERE menu_name = '内容列表' LIMIT 1), 2, '#', '', 'F', '0', '0', 'content:info:add', '#', 'admin', sysdate(), ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'content:info:add');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '内容修改', (SELECT menu_id FROM sys_menu WHERE menu_name = '内容列表' LIMIT 1), 3, '#', '', 'F', '0', '0', 'content:info:edit', '#', 'admin', sysdate(), ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'content:info:edit');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '内容删除', (SELECT menu_id FROM sys_menu WHERE menu_name = '内容列表' LIMIT 1), 4, '#', '', 'F', '0', '0', 'content:info:remove', '#', 'admin', sysdate(), ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'content:info:remove');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '内容导出', (SELECT menu_id FROM sys_menu WHERE menu_name = '内容列表' LIMIT 1), 5, '#', '', 'F', '0', '0', 'content:info:export', '#', 'admin', sysdate(), ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'content:info:export');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '分类查询', (SELECT menu_id FROM sys_menu WHERE menu_name = '分类管理' LIMIT 1), 1, '#', '', 'F', '0', '0', 'content:category:query', '#', 'admin', sysdate(), ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'content:category:query');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '分类新增', (SELECT menu_id FROM sys_menu WHERE menu_name = '分类管理' LIMIT 1), 2, '#', '', 'F', '0', '0', 'content:category:add', '#', 'admin', sysdate(), ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'content:category:add');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '分类修改', (SELECT menu_id FROM sys_menu WHERE menu_name = '分类管理' LIMIT 1), 3, '#', '', 'F', '0', '0', 'content:category:edit', '#', 'admin', sysdate(), ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'content:category:edit');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '分类删除', (SELECT menu_id FROM sys_menu WHERE menu_name = '分类管理' LIMIT 1), 4, '#', '', 'F', '0', '0', 'content:category:remove', '#', 'admin', sysdate(), ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'content:category:remove');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '评论查询', (SELECT menu_id FROM sys_menu WHERE menu_name = '评论管理' LIMIT 1), 1, '#', '', 'F', '0', '0', 'content:comment:query', '#', 'admin', sysdate(), ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'content:comment:query');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '评论删除', (SELECT menu_id FROM sys_menu WHERE menu_name = '评论管理' LIMIT 1), 2, '#', '', 'F', '0', '0', 'content:comment:remove', '#', 'admin', sysdate(), ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'content:comment:remove');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT 'C端用户查询', (SELECT menu_id FROM sys_menu WHERE menu_name = 'C端用户' LIMIT 1), 1, '#', '', 'F', '0', '0', 'content:user:query', '#', 'admin', sysdate(), ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'content:user:query');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT 'C端用户修改', (SELECT menu_id FROM sys_menu WHERE menu_name = 'C端用户' LIMIT 1), 2, '#', '', 'F', '0', '0', 'content:user:edit', '#', 'admin', sysdate(), ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'content:user:edit');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT 'C端用户删除', (SELECT menu_id FROM sys_menu WHERE menu_name = 'C端用户' LIMIT 1), 3, '#', '', 'F', '0', '0', 'content:user:remove', '#', 'admin', sysdate(), ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'content:user:remove');

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT 'C端用户导出', (SELECT menu_id FROM sys_menu WHERE menu_name = 'C端用户' LIMIT 1), 4, '#', '', 'F', '0', '0', 'content:user:export', '#', 'admin', sysdate(), ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE perms = 'content:user:export');

-- 5. 添加内容状态字典
INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark)
SELECT '内容状态', 'content_status', '0', 'admin', sysdate(), '内容状态列表'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_type WHERE dict_type = 'content_status');

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
SELECT 1, '已发布', '0', 'content_status', '', 'success', 'N', '0', 'admin', sysdate(), ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'content_status' AND dict_value = '0');

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
SELECT 2, '待审核', '1', 'content_status', '', 'warning', 'N', '0', 'admin', sysdate(), ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'content_status' AND dict_value = '1');

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
SELECT 3, '已驳回', '2', 'content_status', '', 'danger', 'N', '0', 'admin', sysdate(), ''
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_dict_data WHERE dict_type = 'content_status' AND dict_value = '2');

-- 6. 给admin角色赋予所有内容管理权限
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu WHERE menu_name IN ('内容管理', '内容审核', '内容列表', '分类管理', '评论管理', 'C端用户')
AND NOT EXISTS (SELECT 1 FROM sys_role_menu rm WHERE rm.role_id = 1 AND rm.menu_id = sys_menu.menu_id);

-- 7. 更新操作日志和登录日志的权限标识（从system改为monitor）
UPDATE sys_menu SET perms = 'monitor:operlog:query' WHERE menu_id = 500 AND perms = 'system:operlog:query';
UPDATE sys_menu SET perms = 'monitor:operlog:remove' WHERE perms = 'system:operlog:remove';
UPDATE sys_menu SET perms = 'monitor:operlog:export' WHERE perms = 'system:operlog:export';
UPDATE sys_menu SET perms = 'monitor:logininfor:query' WHERE menu_id = 501 AND perms = 'system:logininfor:query';
UPDATE sys_menu SET perms = 'monitor:logininfor:remove' WHERE perms = 'system:logininfor:remove';
UPDATE sys_menu SET perms = 'monitor:logininfor:unlock' WHERE perms = 'system:logininfor:unlock';
UPDATE sys_menu SET perms = 'monitor:logininfor:export' WHERE perms = 'system:logininfor:export';

-- 8. 更新系统监控菜单名称
UPDATE sys_menu SET menu_name = '系统监控', icon = 'monitor' WHERE menu_id = 2;

-- =============================================
-- 9. 清理无关数据库表（整表删除）
-- 代码生成模块（ruoyi-gen）已删除
-- 通知公告功能（前端页面+后端接口）已删除
-- =============================================
DROP TABLE IF EXISTS gen_table_column;
DROP TABLE IF EXISTS gen_table;
DROP TABLE IF EXISTS notice_read;
DROP TABLE IF EXISTS sys_notice;

-- =============================================
-- 10. 清理无关数据库表数据
-- 岗位和部门的表结构保留（后端Service仍依赖），仅清理冗余数据
-- =============================================
DELETE FROM sys_user_post;
DELETE FROM sys_post;
DELETE FROM sys_role_dept;
DELETE FROM sys_dept WHERE dept_id > 100;

-- =============================================
-- 11. 清理无关角色菜单关联
-- 删除已移除菜单的角色关联数据
-- =============================================
DELETE FROM sys_role_menu WHERE menu_id NOT IN (SELECT menu_id FROM sys_menu);

-- =============================================
-- 12. 清理无关字典类型和字典数据
-- =============================================
DELETE FROM sys_dict_data WHERE dict_type = 'sys_notice_type';
DELETE FROM sys_dict_data WHERE dict_type = 'sys_notice_status';
DELETE FROM sys_dict_type WHERE dict_type = 'sys_notice_type';
DELETE FROM sys_dict_type WHERE dict_type = 'sys_notice_status';

-- =============================================
-- 13. 添加内容搜索全文索引（提升搜索性能）
-- =============================================
ALTER TABLE content_info ADD FULLTEXT INDEX ft_content_search (title, summary, tags) WITH PARSER ngram;

-- =============================================
-- 14. 更新已发布内容的发布时间（历史数据修复）
-- =============================================
UPDATE content_info SET publish_time = create_time WHERE status = '0' AND publish_time IS NULL;

-- =============================================
-- 15. 添加缓存监控菜单
-- =============================================
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, remark)
SELECT '缓存监控', 2, 4, 'cache', 'monitor/cache/index', 'C', '0', '0', 'content:cache:list', 'redis', 'admin', sysdate(), '缓存监控菜单'
FROM DUAL WHERE NOT EXISTS (SELECT 1 FROM sys_menu WHERE menu_name = '缓存监控');

-- =============================================
-- 以下操作需要在 Nacos 控制台中手动执行
-- =============================================
-- 1. 登录 Nacos 控制台 (http://127.0.0.1:8848/nacos)
-- 2. 找到配置 ruoyi-gateway-dev.yml
-- 3. 删除以下路由配置:
--    - ruoyi-gen (代码生成模块，已删除)
-- 4. 保存并发布配置
