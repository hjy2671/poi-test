package org.v2;

import org.v2.util.StrUtil;

import java.util.Map;

/**
 * @author hjy
 * @date 2024/10/19 17:03
 */
public class GroupPool extends AbstractPool{


    public GroupPool(Map<String, Object> meta, Context context) {
        super(meta, context);
    }

    @Override
    public String getString(String key) {
        String tmp = meta.getOrDefault(key, "").toString();
        if (StrUtil.isKey(tmp)) {
            return context.getString(tmp);
        }
        return tmp;
    }

    @Override
    public Pool getPool(String key) {
        Object v = meta.getOrDefault(key, null);
        if (v instanceof String && StrUtil.isKey(v.toString())) {
            return context.getPool(v.toString());
        }
        return PoolFactory.createPool(v, "group", context);
    }

    @Override
    public Table getTable(String key) {
        Object v = meta.getOrDefault(key, "");
        if (v instanceof String && StrUtil.isKey(v.toString())) {
            return context.getTable(v.toString());
        }
        return new SimpleTable(v);
    }

}
