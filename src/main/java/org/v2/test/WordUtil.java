package org.v2.test;

import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.v2.BaseContext;
import org.v2.core.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author hjy
 * @date 2024/10/20 15:34
 */
public class WordUtil {

    private static final class VisitorHolder {
        private static final DocumentVisitor<XWPFDocument> visitor;
        static {
            DocVisitor docVisitor = new DocVisitor();
            docVisitor.register(XWPFChart.class, new ChartVisitor());
            docVisitor.register(XWPFTable.class, new TableVisitor());
            docVisitor.register(XWPFParagraph.class, new ParagraphVisitor());
            visitor = docVisitor;
        }
    }

    public static void resolveDocument(XWPFDocument document, Map<String, ?> data)
    {
        VisitorHolder.visitor.visit(document, BaseContext.Factory.createContext(data));
    }

    public static void resolveDocument(InputStream inputStream, Map<String, ?> data)
    {
        XWPFDocument document = null;
        try {
            document = new XWPFDocument(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        resolveDocument(document, data);
    }

}
