package com.ruoyi.content;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import com.ruoyi.common.security.annotation.EnableCustomConfig;
import com.ruoyi.common.security.annotation.EnableRyFeignClients;

/**
 * C-End Content Distribution Module
 * 
 * @author ruoyi
 */
@EnableCustomConfig
@EnableRyFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class RuoYiContentApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(RuoYiContentApplication.class, args);
        System.out.println("(___) C-End Content Distribution Module Started Successfully启动成功！ (___)");
    }
}
