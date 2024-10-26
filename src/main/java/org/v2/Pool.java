package org.v2;

public interface Pool {

    /**
     * 从池中取一个值，并转换成String
     * @param key String
     * @return String
     */
    String getString(String key);

    /**
     * 从池中取一个值，并转换成Number
     * @param key String
     * @return Number
     */
    Number getNumber(String key);

    boolean getBoolean(String key);

    /**
     * 从池中取一个值，并转换成Pool
     * @param key String
     * @return Pool
     */
    Pool getPool(String key);

    /**
     * 从池中取一个值，并转换成Table
     * @param key String
     * @return Table
     */
    Table getTable(String key);

    /**
     * 判断池中是否有这个key
     * 存在返回true，否则返回false
     * @param key String
     * @return boolean
     */
    boolean has(String key);

}
