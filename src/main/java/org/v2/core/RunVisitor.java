package org.v2.core;

import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.v2.Context;
import org.v2.Pool;
import org.v2.util.Constant;
import org.v2.util.StrUtil;

/**
 * @author hjy
 * @date 2024/10/20 15:59
 */
public class RunVisitor extends AbstractDocumentVisitor<XWPFRun>{
    @Override
    public void visit(XWPFRun target, Context context) {
        String key = target.getText(0);
        if (StrUtil.isConstantKey(key)) {
            target.setText(context.getString(key), 0);
        } else if (StrUtil.isGroupKey(key)){
            Pool pool = context.getPool(key);
            target.setText(pool.getString(Constant.DATA), 0);
            visitStyle(target, pool, context);
        } else {

        }
    }

    @Override
    protected void visitStyle(XWPFRun target, Pool pool) {
        if (pool.has(Constant.FONT_SIZE))
            target.setFontSize(pool.getNumber(Constant.FONT_SIZE).intValue());
    }
}
