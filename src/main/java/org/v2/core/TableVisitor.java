package org.v2.core;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.v2.Context;

import java.util.List;

/**
 * @author hjy
 * @date 2024/10/20 15:17
 */
public class TableVisitor extends AbstractDocumentVisitor<XWPFTable>{
    @Override
    public void visit(XWPFTable target, Context context) {
        List<XWPFTableRow> rows = target.getRows();
        XWPFTableRow xwpfTableRow = rows.get(0);
        ParagraphVisitor visitor = new ParagraphVisitor();
        List<XWPFTableCell> tableCells = xwpfTableRow.getTableCells();
        for (XWPFTableCell tableCell : tableCells) {
            List<XWPFParagraph> paragraphs = tableCell.getParagraphs();
            for (XWPFParagraph paragraph : paragraphs) {
                visitor.visit(paragraph, context);
            }
        }


    }
}
