package org.v2.core;

import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.v2.Context;
import org.v2.Pool;
import org.v2.util.Constant;
import org.v2.util.StrUtil;

/**
 * @author hjy
 * @date 2024/11/3 22:05
 */
public class CellRunVisitor extends RunVisitor{

    public static final String CELL_POOL = "CELL_POOL";
    @Override
    public void visit(XWPFRun target, Context context) {
        String key = target.getText(0);
        if (StrUtil.isConstantKey(key)) {
            target.setText(context.getString(key), 0);
        } else if (StrUtil.isGroupKey(key)){
            Pool pool = context.getPool(key);
            context.put(CELL_POOL, pool);//
            target.setText(pool.getString(Constant.DATA), 0);
            visitStyle(target, pool, context);
        }
    }
}
