# 网关配置修改说明
# 在Nacos的ruoyi-gateway-dev.yml配置中需要修改以下内容：

# 1. 在routes部分添加content模块路由（在文件服务路由后面添加）
#            # 内容分发模块
#            - id: ruoyi-content
#              uri: lb://ruoyi-content
#              predicates:
#                - Path=/content/**
#              filters:
#                - StripPrefix=1

# 2. 在security.ignore.whites部分添加content登录和注册接口到白名单
#    whites:
#      - /auth/logout
#      - /auth/login
#      - /auth/register
#      - /content/login
#      - /content/user/register
#      - /*/v2/api-docs
#      - /*/v3/api-docs
#      - /csrf

# 3. 在Sentinel限流配置sentinel-ruoyi-gateway中添加content模块限流规则
# 	{
#         "resource": "ruoyi-content",
#         "count": 500,
#         "grade": 1,
#         "limitApp": "default",
#         "strategy": 0,
#         "controlBehavior": 0
#     }
