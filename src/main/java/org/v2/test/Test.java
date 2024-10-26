package org.v2.test;

import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.v2.Context;
import org.v2.BaseContext;
import org.v2.core.ChartVisitor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hjy
 * @date 2024/10/19 17:52
 */
public class Test {

    static String path = "C:\\Users\\hjy\\Desktop\\temp\\5\\target.docx";
    static String read = "C:\\Users\\hjy\\Desktop\\temp\\5\\read.docx";
    static String template = "C:\\Users\\123\\Desktop\\test\\5\\template.docx";

    public static void main(String[] args) throws IOException {

        Map<String, Map<String, Object>> map = new HashMap<>();//测试参数
        map.put("constant", Map.of(
                "title", "再别康桥",
                "actor", "徐志摩",
                "r1v1", 10,
                "r1v2", 20,
                "r2v1", 30,
                "r2v2", 40,
                "content1", "[我是一个好人]",
                "content3", "[我是一个坏人]",
                "content2", "[我是一个坏人]",
                "content4", "[我是一个坏人]"
        ));
        map.put("group",
                Map.of("ageCharts", Map.of(
                        "title", "${title}",
                        "data", "#{list}"),
                        "list", List.of(
                                Map.of("name", "小明", "age", 18, "money", 56, "height", 170, "weight", 120),
                                Map.of("name", "张三", "age", 21, "money", 21, "height", 130, "weight", 105),
                                Map.of("name", "李四", "age", 50, "money", 63, "height", 190, "weight", 90),
                                Map.of("name", "王八", "age", 31, "money", 73, "height", 110, "weight", 180)),
                        "styleContent", Map.of("data", "我是最后一段文字", "fontSize", 25),
                        "title", Map.of("data", "再别康桥", "fontFamily", "NSimSun", "fontSize", 30)
                )
        );

        long t = System.currentTimeMillis();

        XWPFDocument document = get();
        WordUtil.resolveDocument(document, map);

        FileOutputStream stream = new FileOutputStream(path);
        document.write(stream);
        stream.flush();
        stream.close();

        System.out.println("cost:"+(System.currentTimeMillis()-t));


    }

    public static XWPFDocument get() throws IOException {
        FileInputStream inputStream = new FileInputStream(read);
        return new XWPFDocument(inputStream);
    }

}
