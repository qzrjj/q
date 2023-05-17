package com.qilixiang.controller;

import com.qilixiang.constant.BaseConstant;
import com.qilixiang.service.UrlService;
import com.qilixiang.dto.Url;
import com.qilixiang.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author qilixiang
 * @date 2023/1/16 11:19
 */
@Controller
@RequestMapping("/")
public class UrlController {

    @Autowired
    UrlService urlService;

    @RequestMapping("/")
    public String index() {
        return BaseConstant.INDEX_PAGE;
    }

    @GetMapping("/{id}")
    public String redirect(@PathVariable("id") Long id) {
        Url url = urlService.getUrlById(id);
        if (Objects.isNull(url)) {
            return BaseConstant.NOT_FOUND_PAGE;
        }
        return BaseConstant.REDIRECT + url.getUrl();
    }

    @PostMapping("/getShortUrl")
    @ResponseBody
    public Result getShortUrl(HttpServletRequest request, Url url) {
        return Result.success(urlService.getShortLink(request, url));
    }
}
