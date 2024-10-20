package org.v2.util;

import java.util.regex.Pattern;

public class NumberUtil {

    private static final Pattern pattern = Pattern.compile("[0-9]*\\.?[0-9]+");

    public static boolean isNum(String val) {
        return pattern.matcher(val).matches();
    }

    public static Number toNum(Object val) {
        if (val instanceof Number) {
            return ((Number) val);
        }
        if (val instanceof String) {
            if (isNum(val.toString())) {
                return Double.valueOf(val.toString());
            }
        }
        return 0;
    }


}
