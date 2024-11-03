package org.v2;

import java.util.Optional;

/**
 * @author hjy
 * @date 2024/10/19 17:49
 */
public interface Context extends Pool{

    /**
     * 通过key定位一个pool
     * @param key String
     * @return Pool
     */
    Pool selectPool(String key);

    Object put(String key, Object value);

    Object get(String key);

    <T> T get(String key, Class<T> type);

    <T> Optional<T> pop(String key, Class<T> type);

}
