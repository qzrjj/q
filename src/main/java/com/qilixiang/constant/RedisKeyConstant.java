package com.qilixiang.constant;

/**
 * @author com.qilixiang on 2022/12/23
 */
public class RedisKeyConstant {
    static String KEY = "URL";
    static String KEY_MD5 = "MD5";

    /**
     * key:id -> url
     *
     * @param id
     * @return
     */
    public static String getUrlKey(Long id) {
        return KEY + ":" + id;
    }

    /**
     * key:md5 -> id
     *
     * @param md5
     * @return
     */
    public static String getMd5Key(String md5) {
        return KEY_MD5 + ":" + md5;
    }
}
