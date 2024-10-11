package org.example;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class CpPool {

    private final Map<String, Object> map;

    public CpPool(Map<String, Object> source) {
        map = source;
    }

    public String getString(String name) {
        Object temp = map.get(name);
        return temp == null ? null : temp.toString();
    }

    public Double getNumber(String name) {
        Object val = map.getOrDefault(name, 0);
        if (val instanceof Number) {
            return ((Double) val);
        }
        String strVal = val.toString();
        if (NumberUtil.isNum(strVal)) {
            return Double.valueOf(strVal);
        }
        throw new NumberFormatException("key:%s,value:%s is not a number".formatted(name, val));
    }

}
