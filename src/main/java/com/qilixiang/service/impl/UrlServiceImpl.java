package com.qilixiang.service.impl;

import cn.hutool.crypto.SecureUtil;
import com.qilixiang.constant.BaseConstant;
import com.qilixiang.constant.RedisKeyConstant;
import com.qilixiang.service.UrlService;
import com.qilixiang.dto.Url;
import com.qilixiang.utils.IPUtils;
import com.qilixiang.utils.SnowFlakeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author qilixiang
 * @date 2023/1/16 11:26
 */
@Service
@Slf4j
public class UrlServiceImpl implements UrlService {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public Url getUrlById(Long id) {
        String url;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(RedisKeyConstant.getUrlKey(id)))) {
            url = (String) redisTemplate.opsForValue().get(RedisKeyConstant.getUrlKey(id));
        } else {
            return null;
        }
        return new Url().setId(id).setUrl(url);
    }

    @Override
    public String getShortLink(HttpServletRequest request, Url url) {
        String paramUrl = url.getUrl();
        Long resultId = 0L;
        if (StringUtils.isEmpty(paramUrl)) {
            throw new RuntimeException("请输入链接地址");
        }

        log.info("IP来自：{}用户访问，请求链接：{}", IPUtils.getIP(request), paramUrl);

        if (!paramUrl.startsWith(BaseConstant.HTTP) && !paramUrl.startsWith(BaseConstant.HTTPS)) {
            url.setUrl(BaseConstant.HTTP + paramUrl);
        }
        // 计算url的md5
        String md5 = SecureUtil.md5(url.getUrl());
        // 判断该md5是否已经存在
        if (Boolean.TRUE.equals(redisTemplate.hasKey(RedisKeyConstant.getMd5Key(md5)))) {
            // 获取url md5对应的id
            Long id = (Long) redisTemplate.opsForValue().get(RedisKeyConstant.getMd5Key(md5));
            // 获取id对应的url
            Url resultUrl = getUrlById(id);
            if (!Objects.isNull(resultUrl) && resultUrl.getUrl().equals(url.getUrl())) {
                resultId = id;
            }
        } else {
            // ID生成策略
            Long id = SnowFlakeUtil.getSnowFlakeId();
            // Url key -> url
            redisTemplate.opsForValue().set(RedisKeyConstant.getUrlKey(id), url.getUrl());
            // MD5 key -> id
            redisTemplate.opsForValue().set(RedisKeyConstant.getMd5Key(md5), id);
            resultId = id;
        }
        // 完整链接
        String wholeUrlFromId = getWholeUrlFromId(request, resultId);
        if (wholeUrlFromId.length() > paramUrl.length()) {
            redisTemplate.delete(RedisKeyConstant.getUrlKey(resultId));
            redisTemplate.delete(RedisKeyConstant.getMd5Key(md5));
            return paramUrl;
        } else {
            return wholeUrlFromId;
        }
    }

    private String getWholeUrlFromId(HttpServletRequest request, Long resultId) {
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + "/" + resultId;
    }
}
