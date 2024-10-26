package org.v2;

import org.v2.util.NumberUtil;

import java.util.Map;

/**
 * @author hjy
 * @date 2024/10/19 15:10
 * key为正常字符串
 */
public abstract class AbstractPool implements Pool{

    protected final Map<String, Object> meta;
    protected final Context context;

    public AbstractPool(Map<String, Object> meta, Context context) {
        this.meta = meta;
        this.context = context;
    }

    @Override
    public Number getNumber(String key) {
        return NumberUtil.toNum(meta.getOrDefault(key, 0));
    }

    @Override
    public String getString(String key) {
        return meta.getOrDefault(key, "").toString();
    }

    @Override
    public boolean getBoolean(String key) {
        Object def = meta.getOrDefault(key, null);
        if (def  == null) {
            return false;
        }
        return Boolean.parseBoolean(def.toString());
    }

    @Override
    public Pool getPool(String key) {
        throw new UnsupportedOperationException("getPool is unsupported");
    }

    @Override
    public Table getTable(String key) {
        throw new UnsupportedOperationException("getTable is unsupported");
    }

    @Override
    public boolean has(String key) {
        return meta.containsKey(key);
    }
}
