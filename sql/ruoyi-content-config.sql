# Content模块数据源配置修复说明
# 错误原因：dynamic-datasource can not find primary datasource
# 解决方法：在Nacos中添加content模块的配置

# 方法1：执行SQL脚本（推荐）
USE `ry-config`;

-- Insert content module configuration into config_info table
INSERT INTO config_info(data_id, group_id, content, md5, gmt_create, gmt_modified, src_user, src_ip, app_name, tenant_id, c_desc, c_use, effect, type, c_schema, encrypted_data_key) VALUES
('ruoyi-content-dev.yml','DEFAULT_GROUP','# spring configuration
spring:
  data:
    redis:
      host: localhost
      port: 6379
      password:
  datasource:
    druid:
      stat-view-servlet:
        enabled: true
        loginUsername: ruoyi
        loginPassword: 123456
    dynamic:
      druid:
        initial-size: 5
        min-idle: 5
        maxActive: 20
        maxWait: 60000
        connectTimeout: 30000
        socketTimeout: 60000
        timeBetweenEvictionRunsMillis: 60000
        minEvictableIdleTimeMillis: 300000
        validationQuery: SELECT 1 FROM DUAL
        testWhileIdle: true
        testOnBorrow: false
        testOnReturn: false
        poolPreparedStatements: true
        maxPoolPreparedStatementPerConnectionSize: 20
        filters: stat,slf4j
        connectionProperties: druid.stat.mergeSql\\=true;druid.stat.slowSqlMillis\\=5000
      datasource:
          # 主库数据源
          master:
            driver-class-name: com.mysql.cj.jdbc.Driver
            url: jdbc:mysql://localhost:3306/ry-cloud?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull&useSSL=true&serverTimezone=GMT%2B8
            username: root
            password: password

# mybatis configuration
mybatis:
    # search package alias
    typeAliasesPackage: com.ruoyi.content
    # configure mapper scanning, find all mapper.xml mapping files
    mapperLocations: classpath:mapper/**/*.xml
    configuration:
      map-underscore-to-camel-case: true
      cache-enabled: false

# springdoc configuration
springdoc:
  gatewayUrl: http://localhost:8080/${spring.application.name}
  api-docs:
    # enable api documentation
    enabled: true
  info:
    # title
    title: ''''Content Distribution Module API Documentation''''
    # description
    description: ''''Content Distribution Module API Description''''
    # author information
    contact:
      name: RuoYi
      url: https://ruoyi.vip

# logging configuration
logging:
  level:
    com.ruoyi.content: debug
    org.springframework: warn
','b8e5f6a7c9d2e3a4f1b8c7d6e5f4a3b2','2026-04-09 21:37:00','2026-04-09 21:37:00','nacos','127.0.0.1','','','Content Distribution Module','null','null','yaml','','');

# 方法2：在Nacos控制台手动添加配置
# 1. 登录Nacos控制台 http://127.0.0.1:8848/nacos
# 2. 进入配置管理 -> 配置列表
# 3. 点击"创建配置"
# 4. 填写以下信息：
#    - Data ID: ruoyi-content-dev.yml
#    - Group: DEFAULT_GROUP
#    - 配置格式: YAML
# 5. 配置内容复制上面的spring配置部分
# 6. 点击发布

-- Update sentinel configuration to include content module
UPDATE config_info SET content = '[
    {
        "resource": "ruoyi-auth",
        "count": 500,
        "grade": 1,
        "limitApp": "default",
        "strategy": 0,
        "controlBehavior": 0
    },
	{
        "resource": "ruoyi-system",
        "count": 1000,
        "grade": 1,
        "limitApp": "default",
        "strategy": 0,
        "controlBehavior": 0
    },
	{
        "resource": "ruoyi-gen",
        "count": 200,
        "grade": 1,
        "limitApp": "default",
        "strategy": 0,
        "controlBehavior": 0
    },
	{
        "resource": "ruoyi-job",
        "count": 300,
        "grade": 1,
        "limitApp": "default",
        "strategy": 0,
        "controlBehavior": 0
    },
	{
        "resource": "ruoyi-content",
        "count": 800,
        "grade": 1,
        "limitApp": "default",
        "strategy": 0,
        "controlBehavior": 0
    }
]', md5 = 'c7d8e9f0a1b2c3d4e5f6a7b8c9d0e1f2' WHERE data_id = 'sentinel-ruoyi-gateway' AND group_id = 'DEFAULT_GROUP';

-- ----------------------------
-- Content Module Database Tables
-- ----------------------------

USE `ry-cloud`;

-- Content module tables are already defined in content_tables.sql
-- This file only contains the Nacos configuration
