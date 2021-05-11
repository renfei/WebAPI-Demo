package net.renfei.webapi.service;

import java.util.List;
import java.util.Map;

/**
 * 接口服务
 *
 * @author renfei
 */
public interface ApiService {
    /**
     * 测试服务，用于接口调通测试
     *
     * @param nonce 随机字符串
     * @return
     */
    List<Map<String, Object>> test(String nonce);
}
