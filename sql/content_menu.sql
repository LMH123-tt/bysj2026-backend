INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark) VALUES
('内容管理', 0, 4, 'content', NULL, 'M', '0', '0', '', 'documentation', 'admin', sysdate(), '', NULL, '内容管理目录'),
('内容审核', (SELECT menu_id FROM sys_menu WHERE menu_name = '内容管理' AND parent_id = 0 LIMIT 1), 1, 'audit', 'content/audit/index', 'C', '0', '0', 'content:info:edit', 'checkbox', 'admin', sysdate(), '', NULL, '内容审核菜单'),
('内容列表', (SELECT menu_id FROM sys_menu WHERE menu_name = '内容管理' AND parent_id = 0 LIMIT 1), 2, 'info', 'content/info/index', 'C', '0', '0', 'content:info:list', 'list', 'admin', sysdate(), '', NULL, '内容列表菜单'),
('分类管理', (SELECT menu_id FROM sys_menu WHERE menu_name = '内容管理' AND parent_id = 0 LIMIT 1), 3, 'category', 'content/category/index', 'C', '0', '0', 'content:category:list', 'tree-table', 'admin', sysdate(), '', NULL, '分类管理菜单'),
('评论管理', (SELECT menu_id FROM sys_menu WHERE menu_name = '内容管理' AND parent_id = 0 LIMIT 1), 4, 'comment', 'content/comment/index', 'C', '0', '0', 'content:comment:list', 'message', 'admin', sysdate(), '', NULL, '评论管理菜单');

INSERT INTO sys_dict_type (dict_id, dict_name, dict_type, status, create_by, create_time, remark) VALUES (100, '内容状态', 'content_status', '0', 'admin', sysdate(), '内容状态列表');
INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark) VALUES
(1, '已发布', '0', 'content_status', '', 'success', 'N', '0', 'admin', sysdate(), ''),
(2, '待审核', '1', 'content_status', '', 'warning', 'N', '0', 'admin', sysdate(), ''),
(3, '已驳回', '2', 'content_status', '', 'danger', 'N', '0', 'admin', sysdate(), '');
