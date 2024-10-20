package org.v2.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hjy
 * @date 2024/10/11 21:58
 */
public class StrUtil {

    private static final Pattern STRICT_PATTERN = Pattern.compile("[#$]\\{.+?}");
    private static final Pattern LENIENT_PATTERN  = Pattern.compile("(.*)([#$]\\{.+?})(.*)");

    public static String extractKey(String source) {
        return source.substring(2, source.length() - 1);
    }

    public static boolean isKey(String source) {
        return !isEmpty(source) && STRICT_PATTERN.matcher(source).matches();
    }

    public static String ifKey(String source) {
        if (isKey(source)) {
            return extractKey(source);
        }
        return null;
    }

    public static boolean isEmpty(String source) {
        return source == null || source.isEmpty();
    }
    public static boolean isNotEmpty(String source) {
        return !isEmpty(source);
    }

    public static StringTemplate findKey(String key) {
        Matcher matcher = LENIENT_PATTERN.matcher(key);

        if (matcher.find()) {
            return new StringTemplate(matcher.group(1) + "%s" + matcher.group(3), matcher.group(2));
        }

        return new StringTemplate(key, "");
    }

    public static boolean isConstantKey(String key) {
        return isNotEmpty(key) && key.charAt(0) == Constant.CONSTANT_PREFIX;
    }

    public static boolean isGroupKey(String key) {
        return isNotEmpty(key) && key.charAt(0) == Constant.GROUP_PREFIX;
    }

    public static boolean isPrefix(char c) {
        return  c == Constant.CONSTANT_PREFIX || c == Constant.GROUP_PREFIX;
    }

    public static void main(String[] args) {
        System.out.println(findKey("#{abc}").getResult(null));
    }

}
