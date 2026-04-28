# 内容分发平台 - 后端API接口文档

> 本文档为web-user前端项目对应的后端接口规范文档
> 创建时间: 2024年
> 适用框架: 若依(RuoYi) SpringBoot + Vue3

---

## 目录

1. [数据库设计](#数据库设计)
2. [接口清单](#接口清单)
3. [接口详情](#接口详情)
4. [数据模型](#数据模型)
5. [若依集成指南](#若依集成指南)

---

## 数据库设计

### 1. 内容分类表 (content_category)

```sql
CREATE TABLE `content_category` (
  `category_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `category_name` varchar(50) NOT NULL COMMENT '分类名称',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父分类ID',
  `order_num` int(4) DEFAULT '0' COMMENT '显示顺序',
  `status` char(1) DEFAULT '0' COMMENT '状态（0正常 1停用）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`category_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='内容分类表';
```

### 2. 内容信息表 (content_info)

```sql
CREATE TABLE `content_info` (
  `content_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '内容ID',
  `title` varchar(200) NOT NULL COMMENT '标题',
  `summary` varchar(500) DEFAULT NULL COMMENT '摘要',
  `content` longtext COMMENT '内容(富文本HTML)',
  `cover` varchar(255) DEFAULT NULL COMMENT '封面图URL',
  `category_id` bigint(20) DEFAULT NULL COMMENT '分类ID',
  `view_count` int(11) DEFAULT '0' COMMENT '浏览次数',
  `favorite_count` int(11) DEFAULT '0' COMMENT '收藏次数',
  `status` char(1) DEFAULT '0' COMMENT '状态（0已发布 1草稿 2下架）',
  `is_top` char(1) DEFAULT '0' COMMENT '是否置顶（0否 1是）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`content_id`),
  KEY `idx_category` (`category_id`),
  KEY `idx_status` (`status`),
  KEY `idx_top` (`is_top`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='内容信息表';
```

### 3. 用户收藏表 (user_favorite)

```sql
CREATE TABLE `user_favorite` (
  `favorite_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `content_id` bigint(20) NOT NULL COMMENT '内容ID',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`favorite_id`),
  UNIQUE KEY `idx_user_content` (`user_id`,`content_id`),
  KEY `idx_user` (`user_id`),
  KEY `idx_content` (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户收藏表';
```

### 4. 公告已读记录表 (notice_read) - 可选

```sql
CREATE TABLE `notice_read` (
  `read_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `notice_id` bigint(20) NOT NULL COMMENT '公告ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `read_time` datetime DEFAULT NULL COMMENT '阅读时间',
  PRIMARY KEY (`read_id`),
  UNIQUE KEY `idx_notice_user` (`notice_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公告阅读记录表';
```

---

## 接口清单

### 内容模块

| 序号 | 接口名称 | 请求方法 | 接口路径 | 是否需要登录 | 说明 |
|:---:|:--------|:-------:|:--------|:----------:|:----|
| 1 | 内容列表 | GET | /content/list | 否 | 分页查询，支持分类筛选 |
| 2 | 推荐内容 | GET | /content/recommend | 否 | 获取热门/置顶推荐内容 |
| 3 | 内容详情 | GET | /content/{id} | 否 | 获取内容详情，自动增加浏览量 |
| 4 | 搜索内容 | GET | /content/search | 否 | 关键词搜索标题和内容 |
| 5 | 收藏内容 | POST | /content/favorite/{id} | **是** | 收藏指定内容 |
| 6 | 取消收藏 | DELETE | /content/favorite/{id} | **是** | 取消收藏 |
| 7 | 收藏列表 | GET | /content/favorites | **是** | 获取当前用户收藏列表 |

### 分类模块

| 序号 | 接口名称 | 请求方法 | 接口路径 | 是否需要登录 | 说明 |
|:---:|:--------|:-------:|:--------|:----------:|:----|
| 8 | 分类列表 | GET | /content/category/list | 否 | 获取所有启用状态的分类 |

### 公告模块 (复用system/notice接口，需扩展)

| 序号 | 接口名称 | 请求方法 | 接口路径 | 是否需要登录 | 说明 |
|:---:|:--------|:-------:|:--------|:----------:|:----|
| 9 | 公告列表 | GET | /system/notice/list | 否 | 获取公告列表，需扩展返回isRead字段 |
| 10 | 公告详情 | GET | /system/notice/{id} | 否 | 获取公告详情 |
| 11 | 置顶公告 | GET | /system/notice/listTop | 否 | 获取首页展示的置顶公告 |
| 12 | 标记已读 | POST | /system/notice/markRead | **是** | 标记单条公告已读 |
| 13 | 批量已读 | POST | /system/notice/markReadAll | **是** | 批量标记公告已读 |

---

## 接口详情

### 1. 获取内容列表

**请求方式**: `GET`

**接口地址**: `/content/list`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|:------|:---:|:---:|:----|
| pageNum | int | 否 | 页码，默认1 |
| pageSize | int | 否 | 每页条数，默认10 |
| categoryId | long | 否 | 分类ID，不传查询全部 |

**响应示例**:

```json
{
  "code": 200,
  "msg": "操作成功",
  "total": 100,
  "rows": [
    {
      "contentId": 1,
      "title": "文章标题示例",
      "summary": "文章摘要...",
      "cover": "https://example.com/cover.jpg",
      "categoryId": 1,
      "categoryName": "科技",
      "viewCount": 1234,
      "favoriteCount": 56,
      "createTime": "2024-01-15 10:30:00",
      "isFavorite": false
    }
  ]
}
```

---

### 2. 获取推荐内容

**请求方式**: `GET`

**接口地址**: `/content/recommend`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|:------|:---:|:---:|:----|
| pageSize | int | 否 | 获取数量，默认6 |

**业务逻辑**:
- 优先返回 is_top=1 的置顶内容
- 其次按 view_count 浏览量降序
- 最后按 create_time 创建时间降序

**响应示例**: 同内容列表

---

### 3. 获取内容详情

**请求方式**: `GET`

**接口地址**: `/content/{contentId}`

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|:------|:---:|:---:|:----|
| contentId | long | 是 | 内容ID |

**响应示例**:

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "contentId": 1,
    "title": "文章标题示例",
    "summary": "文章摘要...",
    "content": "<p>富文本HTML内容</p><img src='...'/>",
    "cover": "https://example.com/cover.jpg",
    "categoryId": 1,
    "categoryName": "科技",
    "viewCount": 1235,
    "favoriteCount": 56,
    "createTime": "2024-01-15 10:30:00",
    "isFavorite": true
  }
}
```

**特殊说明**:
- 接口会自动增加该内容的 view_count 浏览次数
- isFavorite 字段表示当前登录用户是否已收藏（未登录返回false）

---

### 4. 搜索内容

**请求方式**: `GET`

**接口地址**: `/content/search`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|:------|:---:|:---:|:----|
| keyword | string | **是** | 搜索关键词 |
| pageNum | int | 否 | 页码，默认1 |
| pageSize | int | 否 | 每页条数，默认10 |

**业务逻辑**:
- 在 title 和 summary 字段中进行模糊匹配
- 只返回 status='0' 已发布状态的内容

**响应示例**: 同内容列表

---

### 5. 收藏内容

**请求方式**: `POST`

**接口地址**: `/content/favorite/{contentId}`

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|:------|:---:|:---:|:----|
| contentId | long | 是 | 内容ID |

**响应示例**:

```json
{
  "code": 200,
  "msg": "收藏成功"
}
```

**特殊说明**:
- 需要登录（JWT Token）
- 同一用户不能重复收藏同一内容（唯一索引约束）
- 收藏成功后需增加 content_info 表的 favorite_count 字段

---

### 6. 取消收藏

**请求方式**: `DELETE`

**接口地址**: `/content/favorite/{contentId}`

**路径参数**:

| 参数名 | 类型 | 必填 | 说明 |
|:------|:---:|:---:|:----|
| contentId | long | 是 | 内容ID |

**响应示例**:

```json
{
  "code": 200,
  "msg": "已取消收藏"
}
```

**特殊说明**:
- 需要登录（JWT Token）
- 取消成功后需减少 content_info 表的 favorite_count 字段

---

### 7. 获取收藏列表

**请求方式**: `GET`

**接口地址**: `/content/favorites`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|:------|:---:|:---:|:----|
| pageNum | int | 否 | 页码，默认1 |
| pageSize | int | 否 | 每页条数，默认10 |

**响应示例**:

```json
{
  "code": 200,
  "msg": "操作成功",
  "total": 10,
  "rows": [
    {
      "favoriteId": 1,
      "contentId": 1,
      "title": "文章标题",
      "cover": "https://example.com/cover.jpg",
      "categoryName": "科技",
      "createTime": "2024-01-15 10:30:00"
    }
  ]
}
```

**特殊说明**:
- 需要登录（JWT Token）
- 只返回当前登录用户的收藏记录
- 按收藏时间倒序排列

---

### 8. 获取分类列表

**请求方式**: `GET`

**接口地址**: `/content/category/list`

**响应示例**:

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "categoryId": 1,
      "categoryName": "科技",
      "parentId": 0,
      "orderNum": 1
    },
    {
      "categoryId": 2,
      "categoryName": "生活",
      "parentId": 0,
      "orderNum": 2
    }
  ]
}
```

**特殊说明**:
- 只返回 status='0' 启用状态的分类
- 按 order_num 升序排列

---

### 9. 公告列表（扩展）

复用若依原有接口 `/system/notice/list`，但需要扩展以下字段：

**扩展字段**:

| 字段名 | 类型 | 说明 |
|:------|:---:|:----|
| isRead | boolean | 当前用户是否已读（未登录返回false） |

**实现方式**: 在 NoticeController 中修改 list 方法，根据当前用户ID查询 notice_read 表判断已读状态。

---

### 10. 公告详情（复用）

复用若依原有接口 `/system/notice/{noticeId}`。

**前端行为**: 打开详情页后调用 `/system/notice/markRead` 标记已读。

---

### 11. 获取置顶公告

**请求方式**: `GET`

**接口地址**: `/system/notice/listTop`

**业务逻辑**:
- 返回 notice_type='2'（公告类型为通知）且 status='0' 的公告
- 最多返回5条
- 按 create_time 降序

**响应示例**:

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "noticeId": 1,
      "noticeTitle": "系统维护通知",
      "noticeType": "2",
      "status": "0",
      "createTime": "2024-01-15 10:00:00",
      "isRead": false
    }
  ]
}
```

---

### 12. 标记公告已读

**请求方式**: `POST`

**接口地址**: `/system/notice/markRead`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|:------|:---:|:---:|:----|
| noticeId | long | **是** | 公告ID |

**响应示例**:

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

**实现逻辑**:
- 向 notice_read 表插入记录（已存在则忽略）

---

### 13. 批量标记公告已读

**请求方式**: `POST`

**接口地址**: `/system/notice/markReadAll`

**请求参数**:

| 参数名 | 类型 | 必填 | 说明 |
|:------|:---:|:---:|:----|
| ids | array | **是** | 公告ID数组 |

**响应示例**:

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

---

## 数据模型

### ContentInfo (内容信息)

| 字段名 | 类型 | 必填 | 说明 |
|:------|:---:|:---:|:----|
| contentId | Long | 是 | 主键ID |
| title | String | 是 | 标题，最大200字符 |
| summary | String | 否 | 摘要，最大500字符 |
| content | String | 否 | 富文本内容（HTML） |
| cover | String | 否 | 封面图URL |
| categoryId | Long | 否 | 分类ID |
| categoryName | String | 否 | 分类名称（关联查询） |
| viewCount | Integer | 否 | 浏览次数，默认0 |
| favoriteCount | Integer | 否 | 收藏次数，默认0 |
| status | String | 是 | 0=已发布, 1=草稿, 2=下架 |
| isTop | String | 是 | 0=否, 1=是 |
| createBy | String | 是 | 创建者 |
| createTime | Date | 是 | 创建时间 |
| updateBy | String | 否 | 更新者 |
| updateTime | Date | 否 | 更新时间 |
| isFavorite | boolean | 否 | 非数据库字段，是否已收藏 |

### ContentCategory (内容分类)

| 字段名 | 类型 | 必填 | 说明 |
|:------|:---:|:---:|:----|
| categoryId | Long | 是 | 主键ID |
| categoryName | String | 是 | 分类名称 |
| parentId | Long | 是 | 父分类ID，0=顶级分类 |
| orderNum | Integer | 是 | 排序号 |
| status | String | 是 | 0=正常, 1=停用 |

### UserFavorite (用户收藏)

| 字段名 | 类型 | 必填 | 说明 |
|:------|:---:|:---:|:----|
| favoriteId | Long | 是 | 主键ID |
| userId | Long | 是 | 用户ID |
| contentId | Long | 是 | 内容ID |
| createTime | Date | 是 | 收藏时间 |
| title | String | 否 | 内容标题（关联查询） |
| cover | String | 否 | 封面图（关联查询） |
| categoryName | String | 否 | 分类名称（关联查询） |

---

## 若依集成指南

### 1. 模块结构

建议在若依框架中创建新模块 `ruoyi-content`：

```
ruoyi-content/
├── src/main/java/com/ruoyi/content/
│   ├── controller/
│   │   ├── ContentController.java
│   │   └── ContentCategoryController.java
│   ├── domain/
│   │   ├── ContentInfo.java
│   │   ├── ContentCategory.java
│   │   └── UserFavorite.java
│   ├── mapper/
│   │   ├── ContentMapper.java
│   │   ├── ContentCategoryMapper.java
│   │   └── UserFavoriteMapper.java
│   ├── service/
│   │   ├── IContentService.java
│   │   ├── IContentCategoryService.java
│   │   ├── IUserFavoriteService.java
│   │   └── impl/
│   │       ├── ContentServiceImpl.java
│   │       ├── ContentCategoryServiceImpl.java
│   │       └── UserFavoriteServiceImpl.java
│   └── RuoYiContentApplication.java (可选)
└── src/main/resources/mapper/content/
    ├── ContentMapper.xml
    ├── ContentCategoryMapper.xml
    └── UserFavoriteMapper.xml
```

### 2. 安全配置

在 `SecurityConfig.java` 中添加匿名访问路径：

```java
// 内容模块允许匿名访问
.antMatchers("/content/list").permitAll()
.antMatchers("/content/recommend").permitAll()
.antMatchers("/content/search").permitAll()
.antMatchers("/content/category/list").permitAll()
.antMatchers("/content/**").permitAll()
// 公告模块匿名访问
.antMatchers("/system/notice/list").permitAll()
.antMatchers("/system/notice/listTop").permitAll()
.antMatchers("/system/notice/*").permitAll()
```

### 3. MyBatis配置

在 `application.yml` 中添加mapper扫描路径：

```yaml
mybatis:
  mapperLocations: classpath*:mapper/**/*Mapper.xml
  typeAliasesPackage: com.ruoyi.**.domain
```

### 4. 权限配置（可选）

如需在admin端管理内容，添加菜单权限：

| 菜单 | 权限标识 |
|:----|:--------|
| 内容管理 | content:info:list |
| 内容新增 | content:info:add |
| 内容修改 | content:info:edit |
| 内容删除 | content:info:remove |
| 分类管理 | content:category:list |
| 分类新增 | content:category:add |
| 分类修改 | content:category:edit |
| 分类删除 | content:category:remove |

---

## 附录：SQL初始化数据

```sql
-- 初始化分类数据
INSERT INTO `content_category` (`category_name`, `parent_id`, `order_num`, `status`, `create_by`, `create_time`)
VALUES
  ('科技', 0, 1, '0', 'admin', NOW()),
  ('生活', 0, 2, '0', 'admin', NOW()),
  ('娱乐', 0, 3, '0', 'admin', NOW()),
  ('教育', 0, 4, '0', 'admin', NOW()),
  ('财经', 0, 5, '0', 'admin', NOW());

-- 初始化测试内容
INSERT INTO `content_info` (`title`, `summary`, `content`, `category_id`, `status`, `is_top`, `view_count`, `favorite_count`, `create_by`, `create_time`)
VALUES
  ('欢迎使用内容分发平台', '这是一个测试内容', '<p>欢迎使用内容分发平台，这里可以发布和浏览各种精彩内容。</p>', 1, '0', '1', 100, 10, 'admin', NOW()),
  ('平台使用指南', '了解如何使用本平台', '<p>详细的使用指南内容...</p>', 1, '0', '0', 50, 5, 'admin', NOW());
```

---

## 版本记录

| 版本 | 日期 | 说明 |
|:---:|:----|:----|
| v1.0 | 2024-01-01 | 初始版本，包含内容、分类、收藏基础接口 |
| v1.1 | 2024-01-15 | 增加公告已读标记接口 |

---

*文档结束*
