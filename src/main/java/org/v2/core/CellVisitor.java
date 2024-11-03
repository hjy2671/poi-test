package org.v2.core;

import org.apache.poi.xwpf.usermodel.IBody;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.v2.Context;
import org.v2.Pool;
import org.v2.util.Constant;

/**
 * @author hjy
 * @date 2024/11/3 17:50
 */
public class CellVisitor extends AbstractDocumentVisitor<XWPFTableCell>{

    private ParagraphVisitor paragraphVisitor;

    public CellVisitor() {
        init();
    }

    protected void init() {
        this.paragraphVisitor = new CellParagraphVisitor();
    }

    @Override
    public void visit(XWPFTableCell target, Context context) {
        paragraphVisitor.visit(target.getParagraphs().getFirst(), context);
        context.pop(CellRunVisitor.CELL_POOL, Pool.class)
                .ifPresent(pool -> visitStyle(target, pool, context));
    }

    @Override
    protected void visitStyle(XWPFTableCell target, Pool pool) {
        if (pool.has(Constant.CELL_COLOR))
            target.setColor(pool.getString(Constant.CELL_COLOR));
        if (pool.has(Constant.CELL_VERTICAL_ALIGN))
            target.setVerticalAlignment(XWPFTableCell.XWPFVertAlign.valueOf(pool.getString(Constant.CELL_VERTICAL_ALIGN)));
        if (pool.has(Constant.CELL_WIDTH))
            target.setWidth(pool.getString(Constant.CELL_WIDTH));
    }

    public static class CellParagraphVisitor extends ParagraphVisitor{
        @Override
        protected RunVisitor createRunVisitor() {
            return new CellRunVisitor();
        }
    }

}
