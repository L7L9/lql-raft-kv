package com.lql.raft.utils;

import org.springframework.context.ConfigurableApplicationContext;

/**
 * Spring工具类
 * @author lql
 * @date 2024/03/19
 */
public class SpringUtils {
    private static ConfigurableApplicationContext context;

    public static void setContext(ConfigurableApplicationContext configurableApplicationContext){
        context = configurableApplicationContext;
    }

    public static <T> T getBean(Class<T> tClass){
        return context.getBean(tClass);
    }
}
