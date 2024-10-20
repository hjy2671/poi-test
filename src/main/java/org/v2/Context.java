package org.v2;

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

}
