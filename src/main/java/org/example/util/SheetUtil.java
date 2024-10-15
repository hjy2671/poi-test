package org.example.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hjy
 * @date 2024/10/15 22:00
 */
public class SheetUtil {

    private static final Pattern pattern = Pattern.compile("(!\\$)([A-Z]+)(\\$\\d+)");

    public static int parseCellColIndexFromRef(String reference) {

        String colName = findCol(reference);
        char[] arr = colName.toCharArray();
        int index = 0;

        for (int i = arr.length - 1; i >= 0; i--) {
            int back = (int) Math.pow(26, arr.length - i - 1);
            index += back * (arr[i] & 63);
        }

        return index - 1;

    }

    public static String findCol(String reference) {
        Matcher matcher = pattern.matcher(reference);

        if (matcher.find()) {
            return matcher.group(2);
        } else {
            throw new IllegalArgumentException("Invalid table reference: " + reference);
        }
    }

}
