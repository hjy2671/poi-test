package org.v2.util;

/**
 * @author hjy
 * @date 2024/10/20 14:15
 */
public interface Constant {

    char CONSTANT_PREFIX = '$';
    char GROUP_PREFIX = '#';
    char HOLDER_START = '{';
    char HOLDER_END = '}';

    String TYPE = "type";
    String TITLE = "title";//标题
    String DATA = "data";//表示数据
    String STYLE = "style";//指定一组样式的引用#{}
    String FONT_SIZE = "fontSize"; //字体大小，数值
    String COLOR = "color"; // 十六进制代码，无需#
    String IS_BOLD = "bold"; //true / other:false
    String IS_ITALIC = "italic"; //true / other:false

    //下划线样式 SINGLE,WORDS,DOUBLE,THICK,DOTTED,DOTTED_HEAVY,DASH,DASHED_HEAVY,DASH_LONG,DASH_LONG_HEAVY,DOT_DASH,DASH_DOT_HEAVY,DOT_DOT_DASH,DASH_DOT_DOT_HEAVY,WAVE,WAVY_HEAVY,WAVY_DOUBLE,NONE
    String UNDERLINE = "underline";

    //字体样式 例如 SimSun SimHei KaiTi NSimSun 等
    String FONT_FAMILY = "fontFamily";
    String CAPITALIZED = "capitalized";
    String CHARACTER_SPACING = "characterSpacing";
    String DOUBLE_STRIKETHROUGH = "doubleStrikethrough";
    String EMBOSSED = "embossed";
    String EMPHASIS_MARK = "emphasisMark";
    String IMPRINTED = "imprinted";
    String KERNING = "kerning";

    String LANG = "lang";
    String SHADOW = "shadow";
    String SMALL_CAPS = "smallCaps";
    String SUBSCRIPT = "subscript";
    String TEXT_HIGHLIGHT_COLOR = "textHighlightColor";
    String TEXT_POSITION = "textPosition";
    String TEXT_SCALE = "textScale";
    String UNDERLINE_COLOR = "underlineColor";
    String VANISH = "vanish";
    String VERTICAL_ALIGNMENT = "verticalAlignment";
    String CELL_COLOR = "cellColor";
    String CELL_VERTICAL_ALIGN = "cellVerticalAlign";
    String CELL_WIDTH = "cellWidth";
}
