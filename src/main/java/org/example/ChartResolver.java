package org.example;

import org.apache.poi.xddf.usermodel.text.XDDFTextBody;
import org.apache.poi.xddf.usermodel.text.XDDFTextParagraph;
import org.apache.poi.xwpf.usermodel.XWPFChart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author hjy
 * @date 2024/10/11 21:15
 */
public class ChartResolver {

    Context context;
    public ChartResolver(Context context) {
        this.context = context;
    }
    public void resolve(XWPFChart chart) {
        String title = this.getTitle(chart);

        if (title == null) {
            //没有
        } else {

            if (StrUtil.isKey(title)) {
//                Object config = context.get(StrUtil.extractKey(title));
            }

        }



    }

    public String getTitle(XWPFChart chart) {
        XDDFTextBody titleBody = chart.getFormattedTitle();
        if (titleBody == null) {
            return null;
        }
        List<XDDFTextParagraph> paragraphs = titleBody.getParagraphs();
        return paragraphs.isEmpty() ? null : paragraphs.getFirst().getText();
    }
}
