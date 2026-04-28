package com.ruoyi.content.cache;

public class CacheConstants
{
    public static final String CONTENT_DETAIL = "detail:";
    public static final String CONTENT_HOT = "list:hot";
    public static final String CONTENT_RECOMMENDED = "list:recommended";
    public static final String CONTENT_TOP = "list:top";
    public static final String CONTENT_LATEST = "list:latest";
    public static final String CONTENT_CATEGORY = "category:all";
    public static final String CONTENT_SEARCH = "search:";

    public static final long TTL_DETAIL = 600L;
    public static final long TTL_LIST = 300L;
    public static final long TTL_CATEGORY = 1800L;
    public static final long TTL_SEARCH = 120L;
    public static final long TTL_LOCAL_DETAIL = 30L;
    public static final long TTL_LOCAL_LIST = 20L;
    public static final long TTL_LOCAL_CATEGORY = 60L;
}
