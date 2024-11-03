package org.v2.core;

import org.apache.poi.xwpf.usermodel.*;
import org.v2.Context;
import org.v2.Pool;
import org.v2.Table;
import org.v2.util.Constant;
import org.v2.util.StrUtil;
import org.v2.util.StringTemplate;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author hjy
 * @date 2024/10/20 15:17
 */
public class TableVisitor extends AbstractDocumentVisitor<XWPFTable>{

    private CellVisitor cellVisitor;

    public TableVisitor() {
        init();
    }

    protected void init() {
        cellVisitor = new CellVisitor();
    }

    /**
     * 左上角第一个单元格含配置信息
     * 如果是
     */
    @Override
    public void visit(XWPFTable target, Context context) {
        String config = target.getRow(0).getCell(0).getText();
        StringTemplate template = StrUtil.findKey(config);
        String key = template.getKey();
        String type = "replace";
        Pool pool = context.getPool(key);;
        if (!StrUtil.isEmpty(key)) {
            if (pool.has(Constant.TYPE)) {
                type = pool.getString(Constant.TYPE);
            }
            if ("ignore".equals(type)) {
                return;
            }
        }

        if ("replace".equals(type)) {
            wrapTable(target)
                    .limit(
                            pool.getNumber("startX").intValue(), pool.getNumber("startY").intValue(),
                            pool.getNumber("endX").intValue(), pool.getNumber("endY").intValue())
                    .each(cell -> cellVisitor.visit(cell, context));
        } else if ("list".equals(type)) {

        }

        visitStyle(target, pool, context);

    }

    @Override
    protected void visitStyle(XWPFTable target, Pool pool) {

    }

    protected TableWrapper wrapTable(XWPFTable table) {
        return new TableWrapper(table);
    }

    protected static class TableWrapper {
        private final XWPFTable table;
        private int startX = 0;
        private int startY = 0;
        private int endX;
        private int endY;

        public TableWrapper(XWPFTable table) {
            this.table = table;
            List<XWPFTableRow> rows = table.getRows();
            this.endX = rows.size() - 1;
            this.endY = rows.getFirst().getTableCells().size() - 1;
        }

        public TableWrapper limit(int x1, int y1, int x2, int y2) {
            this.startX = Math.max(x1, this.startX);
            this.startY = Math.max(y1, this.startY);
            this.endX = Math.max(x2, this.endX);
            this.endY = Math.max(y2, this.endY);
            return this;
        }

        public void each(Consumer<XWPFTableCell> consumer) {
            for (int i = startX; i <= endX; i++) {
                XWPFTableRow row = table.getRow(i);
                for (int j = startY; j <= endY; j++) {
                    consumer.accept(row.getCell(j));
                }
            }
        }
    }
}
