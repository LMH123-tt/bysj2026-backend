在 Nacos 控制台 (http://127.0.0.1:8848/nacos) 中编辑 ruoyi-gateway-dev.yml 配置：

1. content 路由不要使用 StripPrefix=1（因为后端Controller路径已包含/content前缀）：

            - id: ruoyi-content
              uri: lb://ruoyi-content
              predicates:
                - Path=/content/**

2. 白名单添加C端公开接口（security.ignore.whites部分）：

    whites:
      - /auth/logout
      - /auth/login
      - /auth/register
      - /content/login
      - /content/user/register
      - /content/user/search
      - /content/category/enabled
      - /content/info/published
      - /content/info/recommended
      - /content/info/top
      - /content/info/hot
      - /content/info/latest
      - /content/info/search
      - /content/info/view/*
      - /content/comment/list
      - /content/info/personalized
      - /content/info/ranking
      - /content/info/related/*
      - /*/v2/api-docs
      - /*/v3/api-docs
      - /csrf

3. 保存并发布配置
