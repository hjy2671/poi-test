package org.example;

import java.util.HashMap;
import java.util.Map;

/**
 * @author hjy
 * @date 2024/10/11 22:30
 */
public class Context {

    private final PlanePool planePool;
    private final TablePool tablePool;
    private final DispatchPool dispatchPool;

    private Context(PlanePool planePool, TablePool tablePool, DispatchPool dispatchPool) {
        this.planePool = planePool;
        this.tablePool = tablePool;
        this.dispatchPool = dispatchPool;
    }

    public PlanePool getPlanePool() {
        return planePool;
    }

    public TablePool getTablePool() {
        return tablePool;
    }

    public DispatchPool getDispatchPool() {
        return dispatchPool;
    }

    public static class Factory {

        private static final Map<String, Object> EMPTY_MAP = new HashMap<>();
        public static Context createContext(Map<String, ?> cache) {
            return new Context(
                    new PlanePool(toMap(cache.get("planePool"))),
                    new TablePool(toMap(cache.get("tablePool"))),
                    new DispatchPool(toMap(cache.get("dispatchPool")))
            );
        }

        @SuppressWarnings("unchecked")
        private static Map<String, Object> toMap(Object val) {
            if (val == null) {
                return EMPTY_MAP;
            }
            if (val instanceof Map) {
                return (Map<String, Object>) val;
            }
            throw new IllegalArgumentException("param is illegal, should be {key:value}");
        }


    }
}
