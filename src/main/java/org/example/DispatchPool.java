package org.example;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hjy
 * @date 2024/10/11 19:56
 */
public class DispatchPool {

    private final Map<String, Map<String, String>> cache = new HashMap<>();
    private final Map<String, Object> sourceMap;

    public DispatchPool(Map<String, Object> source) {
        this.sourceMap = source;
    }

    public String getString(String group, String field) {
        if (cache.containsKey(group)) {
            return cache.get(group).get(field);
        }

        if (sourceMap.containsKey(group)) {
            Object config = sourceMap.get(group);
            Map<String, String> resultMap = new HashMap<>();
            if (config instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> cMap = ((Map<String, Object>) config);
                cMap.forEach((key, value) -> {
                    if (value instanceof String || value instanceof Number) {
                        resultMap.put(key, value.toString());
                    } else {
                        //异常
                        throw new IllegalArgumentException(key);
                    }
                });
            } else {
                //异常
                throw new IllegalArgumentException(group);
            }
            cache.put(group, resultMap);
            return resultMap.get(field);
        }
        return null;
    }


}
