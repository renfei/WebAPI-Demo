package net.renfei.webapi.service.impl;

import net.renfei.webapi.repository.OldPlatformMapper;
import net.renfei.webapi.service.ApiService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 为旧平台写的
 *
 * @author renfei
 */
@Service
public class OldPlatformApiServiceImpl implements ApiService {
    private final OldPlatformMapper oldPlatformMapper;

    public OldPlatformApiServiceImpl(OldPlatformMapper oldPlatformMapper) {
        this.oldPlatformMapper = oldPlatformMapper;
    }

    /**
     * 测试服务，用于接口调通测试
     *
     * @param nonce 随机字符串
     * @return
     */
    @Override
    public List<Map<String, Object>> test(String nonce) {
        List<Map<String, Object>> datas = new ArrayList<>();
        Map<String, Object> data = new HashMap<>();
        data.put("nonce", nonce);
        data.put("test", oldPlatformMapper.test());
        datas.add(data);
        return datas;
    }
}
