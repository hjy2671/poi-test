package org.example;

import java.util.regex.Pattern;

/**
 * @author hjy
 * @date 2024/10/11 21:58
 */
public class StrUtil {

    private static final Pattern pattern = Pattern.compile("(#\\{|\\$\\{).+?}");

    public static String extractKey(String source) {
        return source.substring(2, source.length() - 1);
    }

    public static boolean isKey(String source) {
        return  pattern.matcher(source).matches();
    }

    public static String ifKey(String source) {
        if (isKey(source)) {
            return extractKey(source);
        }
        return null;
    }

}
