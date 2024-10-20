package org.v2.core;

import org.v2.Context;
import org.v2.Pool;
import org.v2.util.Constant;

/**
 * @author hjy
 * @date 2024/10/19 20:22
 */
public abstract class AbstractDocumentVisitor<T> implements DocumentVisitor<T> {

    @Override
    public final void visitStyle(T target, Pool pool, Context context) {

        if (pool.has(Constant.STYLE)) {
            Pool stylePool = context.getPool(pool.getString(Constant.STYLE));
            this.visitStyle(target, stylePool, context);//递归去设置样式
        }

        visitStyle(target, pool);//越深层级的会被越浅层级的覆盖
    }


    //TODO 给Chart设置样式会空指针
    protected void visitStyle(T target, Pool pool) {
        //do nothing
    };
}
