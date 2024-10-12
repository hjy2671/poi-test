package org.example;

import java.util.List;
import java.util.Map;

/**
 * @author hjy
 * @date 2024/10/11 19:56
 */
public class TablePool {

    private final Map<String, Object> map;

    public TablePool(Map<String, Object> source) {
        map = source;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getList(String key) {
        Object o = map.get(key);
        return (List<Map<String, Object>>) o;
    }

}
