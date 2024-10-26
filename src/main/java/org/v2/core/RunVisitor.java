package org.v2.core;

import org.apache.poi.xwpf.usermodel.UnderlinePatterns;
import org.apache.poi.xwpf.usermodel.VerticalAlign;
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
        }
    }

    @Override
    protected void visitStyle(XWPFRun target, Pool pool) {
        if (pool.has(Constant.FONT_SIZE))
            target.setFontSize(pool.getNumber(Constant.FONT_SIZE).intValue());
        if (pool.has(Constant.COLOR))
            target.setColor(pool.getString(Constant.COLOR));
        if (pool.has(Constant.IS_BOLD))
            target.setBold(pool.getBoolean(Constant.IS_BOLD));
        if (pool.has(Constant.IS_ITALIC))
            target.setItalic(pool.getBoolean(Constant.IS_ITALIC));
        if (pool.has(Constant.UNDERLINE))
            target.setUnderline(UnderlinePatterns.valueOf(pool.getString(Constant.UNDERLINE)));
        if (pool.has(Constant.FONT_FAMILY))
            target.setFontFamily(pool.getString(Constant.FONT_FAMILY));
        if (pool.has(Constant.CAPITALIZED))
            target.setCapitalized(pool.getBoolean(Constant.CAPITALIZED));
        if (pool.has(Constant.CHARACTER_SPACING))
            target.setCharacterSpacing(pool.getNumber(Constant.CHARACTER_SPACING).intValue());
        if (pool.has(Constant.DOUBLE_STRIKETHROUGH))
            target.setDoubleStrikethrough(pool.getBoolean(Constant.DOUBLE_STRIKETHROUGH));
        if (pool.has(Constant.EMBOSSED))
            target.setEmbossed(pool.getBoolean(Constant.EMBOSSED));
        if (pool.has(Constant.EMPHASIS_MARK))
            target.setEmphasisMark(pool.getString(Constant.EMPHASIS_MARK));
        if (pool.has(Constant.IMPRINTED))
            target.setImprinted(pool.getBoolean(Constant.IMPRINTED));
        if (pool.has(Constant.KERNING))
            target.setKerning(pool.getNumber(Constant.KERNING).intValue());
        if (pool.has(Constant.LANG))
            target.setLang(pool.getString(Constant.LANG));
        if (pool.has(Constant.SHADOW))
            target.setShadow(pool.getBoolean(Constant.SHADOW));
        if (pool.has(Constant.SMALL_CAPS))
            target.setSmallCaps(pool.getBoolean(Constant.SMALL_CAPS));
        if (pool.has(Constant.SUBSCRIPT))
            target.setSubscript(VerticalAlign.valueOf(pool.getString(Constant.SUBSCRIPT)));
        if (pool.has(Constant.TEXT_HIGHLIGHT_COLOR))
            target.setTextHighlightColor(pool.getString(Constant.TEXT_HIGHLIGHT_COLOR));
        if (pool.has(Constant.TEXT_POSITION))
            target.setTextPosition(pool.getNumber(Constant.TEXT_POSITION).intValue());
        if (pool.has(Constant.TEXT_SCALE))
            target.setTextScale(pool.getNumber(Constant.TEXT_SCALE).intValue());
        if (pool.has(Constant.UNDERLINE_COLOR))
            target.setUnderlineColor(pool.getString(Constant.UNDERLINE_COLOR));
        if (pool.has(Constant.VANISH))
            target.setVanish(pool.getBoolean(Constant.VANISH));
        if (pool.has(Constant.VERTICAL_ALIGNMENT))
            target.setVerticalAlignment(pool.getString(Constant.VERTICAL_ALIGNMENT));
    }

}
