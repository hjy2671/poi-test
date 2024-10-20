package org.v2.core;

import org.v2.Context;
import org.v2.Pool;

/**
 * @author hjy
 * @date 2024/10/19 19:51
 */
public interface DocumentVisitor<T> {

    void visit(T target, Context context);

    void visitStyle(T target, Pool pool, Context context);

}
