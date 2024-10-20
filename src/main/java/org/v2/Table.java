package org.v2;

/**
 * @author hjy
 * @date 2024/10/19 19:16
 */
public interface Table {

    int rowSize();

    int colSize();

    default Object getValue(int row, int col) {
        throw new UnsupportedOperationException("not support");
    };

    default Object getValue(int row, String colKey) {
        throw new UnsupportedOperationException("not support");
    };

    default Object getValue(String rowKey, String colKey) {
        throw new UnsupportedOperationException("not support");
    };

    boolean isEmpty();

}
