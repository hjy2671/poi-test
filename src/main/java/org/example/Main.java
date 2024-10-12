package org.example;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.SchemaType;
import org.example.ChartResolver;
import org.openxmlformats.schemas.drawingml.x2006.chart.CTChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.impl.CTNumDataImpl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hjy
 * @date 2024/9/22 20:55
 */
public class Main {
    static String path = "C:\\Users\\123\\Desktop\\test\\5\\target.docx";
    static String read = "C:\\Users\\123\\Desktop\\test\\5\\read.docx";
    static String template = "C:\\Users\\123\\Desktop\\test\\5\\template.docx";
    public static void main(String[] args) throws IOException {
//        createDocument();

//        readDocument4();

        Map<String, Map<String, Object>> map = new HashMap<>();//测试参数
        map.put("planePool", Map.of("title", "再别康桥", "actor", "徐志摩"));
        map.put("tablePool", Map.of("students", List.of(
                Map.of("name", "小明", "age", 18, "money", 120),
                Map.of("name", "张三", "age", 15, "money", 60),
                Map.of("name", "发多少", "age", 30, "money", 75),
                Map.of("name", "电风扇", "age", 20, "money", 90)
        )));
        map.put("dispatchPool", Map.of("ageCharts", Map.of("data", "students")));

        Context context = Context.Factory.createContext(map);

        ChartResolver resolver = new ChartResolver(context);

        XWPFDocument document = get();
        resolver.resolve(document.getCharts().getFirst());

        FileOutputStream stream = new FileOutputStream(path);
        document.write(stream);
        stream.flush();
        stream.close();

    }

    public static XWPFDocument get() throws IOException {
        FileInputStream inputStream = new FileInputStream(read);
        return new XWPFDocument(inputStream);
    }

    public static void readDocument4() throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream(read);
        try (XWPFDocument document = new XWPFDocument(inputStream)) {

            List<XWPFChart> charts = document.getCharts();

            XWPFChart xwpfChart = charts.get(1);
            XSSFWorkbook workbook = xwpfChart.getWorkbook();
            XSSFSheet sheet1 = workbook.getSheet("图表1");
            XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFRow row = sheet.getRow(0);
            XSSFCell cell = row.createCell(4, CellType.STRING);
            cell.setCellValue("大学");

            row = sheet.getRow(1);
            row.getCell(0).setCellValue("男生");
            row.getCell(1).setCellValue(3);
            row.getCell(2).setCellValue(4);
            row.getCell(3).setCellValue(5);
            row.createCell(4).setCellValue(6);

            row = sheet.getRow(2);
            row.getCell(0).setCellValue("女生");
            row.getCell(1).setCellValue(3);
            row.getCell(2).setCellValue(4);
            row.getCell(3).setCellValue(5);
            row.createCell(4).setCellValue(6);

            row = sheet.getRow(3);
            row.getCell(0).setCellValue("老师");
            row.getCell(1).setCellValue(3);
            row.getCell(2).setCellValue(4);
            row.getCell(3).setCellValue(5);
            row.createCell(4).setCellValue(6);

            row = sheet.getRow(4);
            row.getCell(0).setCellValue("其他");
            row.getCell(1).setCellValue(3);
            row.getCell(2).setCellValue(4);
            row.getCell(3).setCellValue(5);
            row.createCell(4).setCellValue(6);


            XDDFChartData data = xwpfChart.createData(ChartTypes.BAR, xwpfChart.createCategoryAxis(AxisPosition.TOP), xwpfChart.createValueAxis(AxisPosition.BOTTOM));

            List<XDDFChartData> chartSeries = xwpfChart.getChartSeries();

            XDDFChartData chartData = xwpfChart.getChartSeries().get(0);


            XDDFCategoryDataSource category = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, 4, 0, 0));

            data.addSeries(category, XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, 4, 4, 4)));

            XDDFChartData.Series series = chartSeries.get(0).getSeries(0);
            series.setTitle("小学", new CellReference(0, 1));
            XDDFNumericalDataSource<Double> target = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, 4, 1, 1));
            series.replaceData(category, target);

            series = chartSeries.get(1).getSeries(0);
//            series.setTitle("初中", new CellReference(0, 2));
            target = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, 4, 2, 2));
            series.replaceData(category, target);

            series = chartSeries.get(2).getSeries(0);
//            series.setTitle("高中", new CellReference(0, 4));
            target = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, 4, 3, 3));
            series.replaceData(category, target);




            xwpfChart.plot(chartData);



            FileOutputStream stream = new FileOutputStream(path);
            document.write(stream);
            stream.flush();
            stream.close();
        } catch (Exception e) {
            throw  new RuntimeException(e);
        }
    }

    public static void readDocument3() throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream(read);
        try (XWPFDocument document = new XWPFDocument(inputStream)) {

            List<XWPFChart> charts = document.getCharts();

            XWPFChart xwpfChart = charts.get(0);
            XSSFWorkbook workbook = xwpfChart.getWorkbook();

            XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFRow row = sheet.getRow(0);


//            row.getCell(1).setCellValue("小学");
//            row.getCell(2).setCellValue("初中");
            row.getCell(3).setCellValue("高中");

            row = sheet.getRow(1);
            row.getCell(0).setCellValue("男生");
            row.getCell(1).setCellValue(3);
            row.getCell(2).setCellValue(4);
            row.getCell(3).setCellValue(5);

            row = sheet.getRow(2);
            row.getCell(0).setCellValue("女生");
            row.getCell(1).setCellValue(3);
            row.getCell(2).setCellValue(4);
            row.getCell(3).setCellValue(5);

            row = sheet.getRow(3);
            row.getCell(0).setCellValue("老师");
            row.getCell(1).setCellValue(3);
            row.getCell(2).setCellValue(4);
            row.getCell(3).setCellValue(5);

            row = sheet.getRow(4);
            row.getCell(0).setCellValue("其他");
            row.getCell(1).setCellValue(3);
            row.getCell(2).setCellValue(4);
            row.getCell(3).setCellValue(5);


            XDDFChartData chartData = xwpfChart.getChartSeries().get(0);

            XDDFCategoryDataSource category = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(1, 4, 0, 0));

            XDDFChartData.Series series = chartData.getSeries(0);
            series.setTitle("小学", new CellReference(0, 1));
            XDDFNumericalDataSource<Double> target = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, 4, 1, 1));
            series.replaceData(category, target);

            series = chartData.getSeries(1);
//            series.setTitle("初中", new CellReference(0, 2));
            target = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, 4, 2, 2));
            series.replaceData(category, target);

            series = chartData.getSeries(2);
//            series.setTitle("高中", new CellReference(0, 4));
            target = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(1, 4, 3, 3));
            series.replaceData(category, target);




            xwpfChart.plot(chartData);



            FileOutputStream stream = new FileOutputStream(path);
            document.write(stream);
            stream.flush();
            stream.close();
        } catch (Exception e) {
            throw  new RuntimeException(e);
        }
    }

    public static void readDocument2() throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream(read);
        try (XWPFDocument document = new XWPFDocument(inputStream)) {

            List<XWPFParagraph> paragraphs = document.getParagraphs();
            paragraphs.forEach(paragraph -> {

                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {

                    if (run.text().equals("${title}")) {

                        run.setText("再别康桥", 0);
                    }
                    if (run.text().equals("${content1}")) {
                        run.setText("轻轻的我走了", 0);
                    }
                    if (run.text().equals("${content2}")) {
                        run.setText("正如我轻轻的来", 0);
                    }

                    System.out.println(run.text());
                }
            });


            System.out.println(1);
            FileOutputStream stream = new FileOutputStream(path);
            document.write(stream);
            stream.flush();
            stream.close();
        } catch (Exception e) {
            throw  new RuntimeException(e);
        }
    }

    public static void readDocument() throws FileNotFoundException {
        FileInputStream inputStream = new FileInputStream(template);
        try (XWPFDocument document = new XWPFDocument(inputStream)) {
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            paragraphs.forEach(paragraph -> {

                if (paragraph.getText().startsWith("{{")) {
                    System.out.println(paragraph.getText());
                }
            });

            List<XWPFTable> tables = document.getTables();
            tables.forEach(table -> {
                List<XWPFTableRow> rows = table.getRows();

                rows.forEach(row -> {
                    List<XWPFTableCell> tableCells = row.getTableCells();
                    tableCells.forEach(tableCell -> {
//                        System.out.println("==Paragraph start==");
                        List<XWPFParagraph> cellParagraphs = tableCell.getParagraphs();

                        cellParagraphs.forEach(cellParagraph -> {
                            System.out.println(cellParagraph.getText());
//                            List<XWPFRun> cellRuns = cellParagraph.getRuns();
//                            System.out.println("==run start==");
//                            cellRuns.forEach(cellRun -> {
//                                System.out.println(cellRun.text());
////                                if (cellRun.text().startsWith("{{")) {
////                                    System.out.println(cellRun.text());
////                                }
//                            });
//                            System.out.println("==run end==");

                        });
                        System.out.println();
//                        System.out.println("==Paragraph end==");
//                        System.out.println();
                   });
                });
            });
            System.out.println(1);
        } catch (Exception e) {
            throw  new RuntimeException(e);
        }
    }

    public static void createDocument() {

        try (XWPFDocument document = new XWPFDocument()) {
            XWPFParagraph paragraph = document.createParagraph();

            paragraph.setBorderBottom(Borders.BASIC_BLACK_DASHES);
            paragraph.setBorderTop(Borders.BASIC_BLACK_DASHES);
            paragraph.setBorderLeft(Borders.BASIC_BLACK_DASHES);
            paragraph.setBorderRight(Borders.BASIC_BLACK_DASHES);

            XWPFRun run = paragraph.createRun();
            run.setText("轻轻的我走了，\n" +
                    "正如我轻轻的来；\n" +
                    "我轻轻的招手，\n" +
                    "作别西天的云彩。\n" +
                    "那河畔的金柳，\n" +
                    "是夕阳中的新娘；\n" +
                    "波光里的艳影，\n" +
                    "在我的心头荡漾。\n" +
                    "软泥上的青荇⑴，\n" +
                    "油油的在水底招摇⑵；\n" +
                    "在康河的柔波里，\n" +
                    "我甘心做一条水草！\n" +
                    "那榆荫下的一潭，\n" +
                    "不是清泉，是天上虹；\n" +
                    "揉碎在浮藻间，\n" +
                    "沉淀着彩虹似的梦。\n" +
                    "寻梦？撑一支长篙⑶，\n" +
                    "向青草更青处漫溯⑷；\n" +
                    "满载一船星辉，\n" +
                    "在星辉斑斓里放歌。\n" +
                    "但我不能放歌，\n" +
                    "悄悄是别离的笙箫；\n" +
                    "夏虫也为我沉默，\n" +
                    "沉默是今晚的康桥！\n" +
                    "悄悄的我走了，\n" +
                    "正如我悄悄的来；\n" +
                    "我挥一挥衣袖，\n" +
                    "不带走一片云彩。");

            run.addBreak();
            run.addBreak();
            run.addBreak();

            run = paragraph.createRun();
            run.setText("轻轻的我走了，\n" +
                    "正如我轻轻的来；\n" +
                    "我轻轻的招手，\n" +
                    "作别西天的云彩。\n" +
                    "那河畔的金柳，\n" +
                    "是夕阳中的新娘；\n" +
                    "波光里的艳影，\n" +
                    "在我的心头荡漾。\n" +
                    "软泥上的青荇⑴，\n" +
                    "油油的在水底招摇⑵；\n" +
                    "在康河的柔波里，\n" +
                    "我甘心做一条水草！\n" +
                    "那榆荫下的一潭，\n" +
                    "不是清泉，是天上虹；\n" +
                    "揉碎在浮藻间，\n" +
                    "沉淀着彩虹似的梦。\n" +
                    "寻梦？撑一支长篙⑶，\n" +
                    "向青草更青处漫溯⑷；\n" +
                    "满载一船星辉，\n" +
                    "在星辉斑斓里放歌。\n" +
                    "但我不能放歌，\n" +
                    "悄悄是别离的笙箫；\n" +
                    "夏虫也为我沉默，\n" +
                    "沉默是今晚的康桥！\n" +
                    "悄悄的我走了，\n" +
                    "正如我悄悄的来；\n" +
                    "我挥一挥衣袖，\n" +
                    "不带走一片云彩。");




            FileOutputStream stream = new FileOutputStream(path);
            document.write(stream);
            stream.flush();
            stream.close();
        } catch (Exception e) {
            throw  new RuntimeException(e);
        }


    }


}
