# bysj2026
# 基于微服务与多级缓存的内容分发平台
## 项目介绍
本项目是基于若依微服务框架（RuoYi-Cloud）开发的内容分发平台，采用前后端分离架构，包含管理端与用户端（C端），支持内容发布、审核、推荐、多级缓存优化等核心功能，用于毕业设计。

## 技术栈
### 后端
- 核心框架：Spring Cloud Alibaba、Spring Boot
- 服务注册与发现：Nacos
- 网关：Spring Cloud Gateway
- 数据库：MySQL、Redis（多级缓存）
- 权限认证：Spring Security、JWT

### 前端
- 框架：Vue 3、TypeScript、Vite
- UI组件库：Element Plus
- 状态管理：Pinia
- 路由：Vue Router

## 项目结构
bysj2026/
├── ruoyi-cloud/ # 后端微服务代码
├── ruoyi-cloud-vue3/ # 前端管理端 + C 端代码
├── sql/ # 数据库初始化脚本
├── docs/ # 项目文档（开题报告、需求文档等）
└── README.md # 项目说明

## 快速启动
### 环境要求
- JDK 17+、Maven 3.8+
- Node.js 20.x、npm 9.x+
- MySQL 8.0+、Redis 6.0+、Nacos 2.2+

### 启动步骤
1.  启动MySQL、Redis、Nacos
2.  执行`sql/`目录下的初始化脚本
3.  按顺序启动后端微服务：`ruoyi-gateway` → `ruoyi-auth` → `ruoyi-system` → `ruoyi-content`
4.  启动前端：`cd ruoyi-cloud-vue3 && npm install && npm run dev`
5.  访问管理端：`http://localhost:8088`，用户端：`http://localhost:8088/`

## 功能模块
### 管理端
- 系统管理：用户、角色、菜单、部门、字典
- 内容管理：内容审核、分类管理、标签管理
- 数据统计：内容分发量、用户活跃度、热门榜单
- 系统监控：服务监控、日志监控

### 用户端（C端）
- 首页推荐、内容浏览、搜索、详情
- 内容发布、个人中心、收藏、评论
- 多级缓存优化、内容推荐算法

## 项目特色
- 微服务架构，高可用、可扩展
- 多级缓存（Redis + 本地缓存）优化内容分发性能
- 管理端+C端双端分离，角色权限清晰
- 基于若依框架快速开发，开箱即用

## 作者
LMH123-tt（毕设项目）
