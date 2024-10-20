package org.v2;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hjy
 * @date 2024/10/19 18:31
 */
public class PoolFactory {

    private static final Map<String, Object> EMPTY_MAP = new HashMap<>();
    public static Pool createPool(Map<String, Object> cache, String type, Context context) {
        return switch (type) {
            case "constant" -> new ConstantPool(cache, context);
            case "group" -> new GroupPool(cache, context);
            default -> throw new IllegalArgumentException("unknown pool type:" + type);
        };
    }

    public static Pool createPool(Object cache, String type, Context context) {
        return createPool(toMap(cache), type, context);
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> toMap(Object val) {
        if (val == null) {
            return EMPTY_MAP;
        }
        if (val instanceof Map) {
            return (Map<String, Object>) val;
        }
        throw new IllegalArgumentException("param should be Map");
    }

}
