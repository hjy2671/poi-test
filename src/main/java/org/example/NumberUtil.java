package org.example;

import java.util.regex.Pattern;

public class NumberUtil {

    private static final Pattern pattern = Pattern.compile("[0-9]*\\.?[0-9]+");

    public static boolean isNum(String val) {
        return pattern.matcher(val).matches();
    }


}
