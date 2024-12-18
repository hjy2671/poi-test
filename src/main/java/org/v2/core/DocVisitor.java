package org.v2.core;

import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.v2.Context;
import org.v2.test.WordUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hjy
 * @date 2024/10/20 15:12
 */
public class DocVisitor extends AbstractDocumentVisitor<XWPFDocument>{

    private final Map<Class<?>, DocumentVisitor<?>> visitors = new HashMap<>();

    public <T> void  register(Class<T> key, DocumentVisitor<T> visitor)
    {
        visitors.put(key, visitor);
    }

    @SuppressWarnings("unchecked")
    public <T> DocumentVisitor<T> find(Class<T> key)
    {
        return ((DocumentVisitor<T>) visitors.get(key));
    }

    @Override
    public void visit(XWPFDocument target, Context context) {
        List<XWPFChart> charts = target.getCharts();
        if (!charts.isEmpty()) {
            long t = System.currentTimeMillis();
            DocumentVisitor<XWPFChart> visitor = find(XWPFChart.class);
            charts.forEach(chart -> visitor.visit(chart, context));
            System.out.println("cost-XWPFChart:"+(System.currentTimeMillis()-t));
        }
        List<XWPFParagraph> paragraphs = target.getParagraphs();
        if (!paragraphs.isEmpty()) {
            long t = System.currentTimeMillis();
            DocumentVisitor<XWPFParagraph> visitor = find(XWPFParagraph.class);
            paragraphs.forEach(chart -> visitor.visit(chart, context));
            System.out.println("cost-XWPFParagraph:"+(System.currentTimeMillis()-t));
        }
        List<XWPFTable> tables = target.getTables();
        if (!tables.isEmpty()) {
            long t = System.currentTimeMillis();
            DocumentVisitor<XWPFTable> visitor = find(XWPFTable.class);
            tables.forEach(chart -> visitor.visit(chart, context));
            System.out.println("cost-XWPFTable:"+(System.currentTimeMillis()-t));
        }
    }
}
