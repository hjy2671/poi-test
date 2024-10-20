package org.v2;


import org.v2.util.StrUtil;

import java.util.Map;

/**
 * @author hjy
 * @date 2024/10/19 15:11
 *
 * context传过来的key 必须是#{} / ${}格式的
 */
public class BaseContext implements Context {

    private Pool constantPool;
    private Pool groupPool;

    public BaseContext(Pool cPool, Pool groupMeta) {
        constantPool = cPool;
        groupPool = groupMeta;
    }

    public BaseContext() {
    }

    @Override
    public Pool selectPool(String key) {
        if (key.charAt(0) == '$') {
            return constantPool;
        }
        if (key.charAt(0) == '#') {
            return groupPool;
        }
        throw new IllegalArgumentException("key should start with ${ or #{, error:" + key);
    }

    @Override
    public String getString(String key) {
        return selectPool(key).getString(createKey(key));
    }

    @Override
    public Number getNumber(String key) {
        return selectPool(key).getNumber(createKey(key));
    }

    @Override
    public Pool getPool(String key) {
        return selectPool(key).getPool(createKey(key));
    }

    @Override
    public Table getTable(String key) {
        return selectPool(key).getTable(createKey(key));
    }

    @Override
    public boolean has(String key) {
        return selectPool(key).has(createKey(key));
    }

    private String createKey(String key) {
        return StrUtil.ifKey(key);
    }



    public static class Factory {
        public static Context createContext(Map<String, ?> cache) {
            BaseContext context = new BaseContext();
            context.constantPool = PoolFactory.createPool(cache.get("constant"), "constant", context);
            context.groupPool = PoolFactory.createPool(cache.get("group"), "group", context);
            return context;
        }

    }

}
