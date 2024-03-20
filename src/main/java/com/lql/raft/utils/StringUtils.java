package com.lql.raft.utils;

/**
 * 字符串工具类
 * @author lql
 * @date 2024/03/20
 */
public class StringUtils {
    public static final String EMPTY_STR = "";

    public static boolean isEmpty(String str){
        return str == null || str.equals(EMPTY_STR);
    }
}
