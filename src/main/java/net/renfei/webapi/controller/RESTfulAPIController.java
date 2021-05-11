package net.renfei.webapi.controller;

import net.renfei.sdk.entity.APIResult;
import net.renfei.webapi.service.ApiService;
import net.renfei.webapi.service.ApiServiceFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * API接口入口
 *
 * @author renfei
 */
@RestController
@RequestMapping("/api")
public class RESTfulAPIController {
    private final ApiServiceFactory apiServiceFactory;

    public RESTfulAPIController(ApiServiceFactory apiServiceFactory) {
        this.apiServiceFactory = apiServiceFactory;
    }

    /**
     * 测试服务，用于接口调通测试
     *
     * @param signature 签名（全局必填用于鉴权）
     * @param timestamp 时间戳（全局必填用于鉴权）
     * @param appid     AppID（全局必填用于鉴权）
     * @param nonce     随机字符串（全局必填用于鉴权）
     * @return APIResult<List < Map < String, Object>>>
     */
    @GetMapping("test")
    public APIResult<List<Map<String, Object>>>
    test(@RequestParam("signature") String signature,
         @RequestParam("timestamp") String timestamp,
         @RequestParam("appid") String appid,
         @RequestParam("nonce") String nonce) {
        ApiService apiService = apiServiceFactory.getApiService();
        return new APIResult<>(apiService.test(nonce));
    }
}
