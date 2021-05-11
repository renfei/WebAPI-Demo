package net.renfei.webapi.repository;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author renfei
 */
@Repository
public interface NewPlatformMapper {
    List<Map<String,Object>> test();
}
