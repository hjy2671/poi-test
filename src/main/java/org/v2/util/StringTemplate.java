package org.v2.util;

/**
 * @author hjy
 * @date 2024/10/19 21:31
 */
public class StringTemplate {

    private final String template;
    private final String key;

    public StringTemplate(String template, String key) {
        this.template = template;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getResult(String target) {
        return template.formatted(target);
    }
}
