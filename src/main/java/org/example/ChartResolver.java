package org.example;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.text.XDDFTextBody;
import org.apache.poi.xddf.usermodel.text.XDDFTextParagraph;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFChart;

import java.io.IOException;
import java.util.*;

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

            String group = StrUtil.ifKey(title);

            if (group != null) {
                DispatchPool dispatchPool = context.getDispatchPool();
                TablePool tablePool = context.getTablePool();
                String dataRef = dispatchPool.getString(group, "data");
                List<Map<String, Object>> chartData = tablePool.getList(dataRef);
                this.resolve(chart, chartData);
                System.out.println(chartData);
            }

        }

    }

    public void resolve(XWPFChart chart, List<Map<String, Object>> chartData) {
        try {
            XSSFWorkbook workbook = chart.getWorkbook();
            XSSFSheet sheet = workbook.getSheetAt(0);
            XSSFRow row = sheet.getRow(0);//config row
            short lastCellNum = row.getLastCellNum();

            List<String> config = new ArrayList<>();
            for (int i = 0; i < lastCellNum; i++) {
                XSSFCell cell = row.getCell(i);
                String key = cell.getStringCellValue();
                config.add(key);
                row.removeCell(cell);
            }

            int dataRowStartIndex = 2;

            for (Map<String, Object> rowData : chartData) {
                XSSFRow currentRow = sheet.createRow(dataRowStartIndex);
                for (int i = 0; i < config.size(); i++) {
                    XSSFCell cell = currentRow.createCell(i);
                    Object cellValue = rowData.get(config.get(i));
                    cell.setCellValue(cellValue == null ? null : cellValue.toString());
                }
                dataRowStartIndex++;
            }

            List<XDDFChartData> chartSeries = chart.getChartSeries();

            XDDFCategoryDataSource category = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(dataRowStartIndex, chartData.size()-1, 0, 0));

            for (XDDFChartData chartSerData : chartSeries) {

                for (int i = 0; i < chartSerData.getSeriesCount(); i++) {
                    XDDFChartData.Series series = chartSerData.getSeries(i);
//                    series.pl
//                    String seriesText = series.().getStrRef().getStrCache().getPtArray(0).getV();
                    XDDFDataSource<?> categoryData = series.getCategoryData();
                }

                chart.plot(chartSerData);
            }


            System.out.println(1);
        } catch (IOException | InvalidFormatException e) {
            throw new RuntimeException(e);
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
