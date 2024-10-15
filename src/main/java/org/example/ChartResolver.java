package org.example;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.*;
import org.apache.poi.xddf.usermodel.text.XDDFTextBody;
import org.apache.poi.xddf.usermodel.text.XDDFTextParagraph;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.example.util.SheetUtil;
import org.example.util.StrUtil;

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
            int dataRowStartIndex = 1;
            int dataRowEndIndex = dataRowStartIndex + chartData.size() - 1;
            int currentRowIndex = dataRowStartIndex;
            int sheetIndex = 0;

            XSSFWorkbook workbook = chart.getWorkbook();
            XSSFSheet sheet = workbook.getSheetAt(sheetIndex);
            XSSFRow row = sheet.getRow(dataRowStartIndex);//config row
            short lastCellNum = row.getLastCellNum();

            List<String> config = new ArrayList<>();
            for (int i = 0; i < lastCellNum; i++) {
                XSSFCell cell = row.getCell(i);
                String key = cell.getStringCellValue();
                config.add(key);
            }



            for (Map<String, Object> rowData : chartData) {

                XSSFRow currentRow = sheet.getRow(currentRowIndex);
                if (currentRow == null) {
                    currentRow = sheet.createRow(currentRowIndex);//找不到行，创建行
                }

                for (int i = 0; i < config.size(); i++) {

                    XSSFCell cell = currentRow.getCell(i);
                    if (cell == null) {
                        cell = currentRow.createCell(i);//没有单元格就创建单元格
                    }

                    Object cellValue = rowData.get(config.get(i));//按顺序填充list的数据
                    if (cellValue instanceof String) {
                        cell.setCellValue(cellValue.toString());
                    }
                    if (cellValue instanceof Number) {
                        cell.setCellValue(((Number) cellValue).doubleValue());
                    }

                }
                currentRowIndex++;
            }

            List<XDDFChartData> chartSeries = chart.getChartSeries();
            XDDFCategoryDataSource category = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(dataRowStartIndex, dataRowEndIndex, 0, 0));

            for (XDDFChartData chartSerData : chartSeries) {
                for (int i = 0; i < chartSerData.getSeriesCount(); i++) {
                    XDDFChartData.Series series = chartSerData.getSeries(i);
                    String formula = series.getValuesData().getFormula();
                    int index = SheetUtil.parseCellColIndexFromRef(formula);

                    XDDFNumericalDataSource<Double> valuesData = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(dataRowStartIndex, dataRowEndIndex, index, index));
                    series.replaceData(category, valuesData);
                }
                chart.plot(chartSerData);
            }

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
