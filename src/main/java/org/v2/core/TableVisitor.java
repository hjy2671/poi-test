package org.v2.core;

import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.v2.Context;

/**
 * @author hjy
 * @date 2024/10/20 15:17
 */
public class TableVisitor extends AbstractDocumentVisitor<XWPFTable>{
    @Override
    public void visit(XWPFTable target, Context context) {

    }
}
