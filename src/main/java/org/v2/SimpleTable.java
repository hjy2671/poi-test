package org.v2;

import java.util.List;
import java.util.Map;

/**
 * @author hjy
 * @date 2024/10/19 22:30
 */
public class SimpleTable implements Table{

    private final List<Map<String, Object>> data;
    private final int rowSize;
    private final int colSize;

    @SuppressWarnings("unchecked")
    public SimpleTable(Object data) {
        if (data instanceof List<?> temp) {
            if (!temp.isEmpty()) {
                Object first = temp.getFirst();
                if (first instanceof Map) {
                    this.data = (List<Map<String, Object>>) temp;
                    colSize = this.data.getFirst().size();
                    this.rowSize = this.data.size();
                    return;
                }
            }
            this.rowSize = 0;
            this.colSize = 0;
        }
        throw new IllegalArgumentException("data is not a list of map");
    }

    @Override
    public int rowSize() {
        return rowSize;
    }

    @Override
    public int colSize() {
        return colSize;
    }

    @Override
    public Object getValue(int rowIndex, String colKey) {
        if (rowIndex >= rowSize || isEmpty()) {
            throw new  IndexOutOfBoundsException("row index out of bounds");
        }
        return data.get(rowIndex).get(colKey);
    }

    @Override
    public boolean isEmpty() {
        return rowSize == 0;
    }
}
