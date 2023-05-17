package com.qilixiang.service;

import com.qilixiang.dto.Url;

import javax.servlet.http.HttpServletRequest;

public interface UrlService {
    /**
     * 根据ID获取短链接
     *
     * @param id
     * @return
     */
    Url getUrlById(Long id);

    /**
     * 根据url生成短链接
     *
     * @param url
     * @return
     */
    String getShortLink(HttpServletRequest request, Url url);
}
