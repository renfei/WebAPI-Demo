package net.renfei.webapi.service.impl;

import net.renfei.webapi.repository.NewPlatformMapper;
import net.renfei.webapi.service.ApiService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 为新平台写的
 *
 * @author renfei
 */
@Service
public class NewPlatformApiServiceImpl implements ApiService {
    private final NewPlatformMapper newPlatformMapper;

    public NewPlatformApiServiceImpl(NewPlatformMapper newPlatformMapper) {
        this.newPlatformMapper = newPlatformMapper;
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
        data.put("test", newPlatformMapper.test());
        datas.add(data);
        return datas;
    }
}
