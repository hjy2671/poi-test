package org.v2.core;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xddf.usermodel.chart.XDDFCategoryDataSource;
import org.apache.poi.xddf.usermodel.chart.XDDFChartData;
import org.apache.poi.xddf.usermodel.chart.XDDFDataSourcesFactory;
import org.apache.poi.xddf.usermodel.chart.XDDFNumericalDataSource;
import org.apache.poi.xddf.usermodel.text.XDDFTextBody;
import org.apache.poi.xddf.usermodel.text.XDDFTextParagraph;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.usermodel.XWPFChart;
import org.v2.Table;
import org.v2.exceptions.ChartException;
import org.v2.util.*;
import org.v2.Context;
import org.v2.Pool;
import org.v2.util.StringTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hjy
 * @date 2024/10/19 19:51
 */
public class ChartVisitor extends AbstractDocumentVisitor<XWPFChart> {

    @Override
    public void visit(XWPFChart chart, Context context) {

        String title = getTitle(chart);
        Pool pool = null;

        if (StrUtil.isNotEmpty(title)) {
            StringTemplate template = StrUtil.findKey(title);
            String key = template.getKey();

            if (StrUtil.isGroupKey(key)) {
                pool = context.getPool(key);
                title = pool.getString(Constant.TITLE);
            } else {
                title = context.getString(key);
            }
            chart.setTitleText(template.getResult(title));

        }

        if (pool == null) {
            replace(chart, context);
        } else {
            fill(chart, pool.getTable(Constant.DATA));
        }

    }

    public String getTitle(XWPFChart chart) {
        //TODO 如果图标使用的是默认标题，那么只要这里读取了此标题，那么文档就会报错打不开
        XDDFTextBody titleBody = chart.getFormattedTitle();
        if (titleBody == null) {
            return null;
        }
        List<XDDFTextParagraph> paragraphs = titleBody.getParagraphs();

        return paragraphs.isEmpty() ? null : paragraphs.getFirst().getText();
    }

    public void replace(XWPFChart chart, Context context) {
        try {
            XSSFWorkbook workbook = chart.getWorkbook();
            XSSFSheet sheet = getDefaultSheet(workbook);

            int lastRowNum = sheet.getLastRowNum();
            int dataRowStartIndex = 1;
            int rowEndIndex = 1;
            boolean emptyRow;

            for (int i = 0; i < lastRowNum; i++) {
                XSSFRow row = sheet.getRow(i);
                emptyRow = true;
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    XSSFCell cell = row.getCell(j);
                    if (cell != null) {
                        String cellValue = cell.getStringCellValue();
                        if (StrUtil.isEmpty(cellValue)) {
                            continue;
                        }
                        emptyRow = false;
                        if (StrUtil.isKey(cellValue)) {
                            if ((i | j) == 0) {
                                cell.setCellValue(context.getString(cellValue));
                            } else {
                                cell.setCellValue(context.getNumber(cellValue).doubleValue());
                            }
                        }
                    }
                }
                if (emptyRow) {
                    break;
                }
                rowEndIndex = i;
            }

            plot(chart, sheet, dataRowStartIndex, rowEndIndex);

        } catch (IOException | InvalidFormatException e) {
            throw new RuntimeException(e);
        }

    }

    protected void plot(XWPFChart chart, XSSFSheet sheet, int startRow, int endRow) {
        List<XDDFChartData> chartSeries = chart.getChartSeries();
        XDDFCategoryDataSource category = XDDFDataSourcesFactory.fromStringCellRange(sheet, new CellRangeAddress(startRow, endRow, 0, 0));

        for (XDDFChartData chartSerData : chartSeries) {
            for (int i = 0; i < chartSerData.getSeriesCount(); i++) {
                XDDFChartData.Series series = chartSerData.getSeries(i);
                String formula = series.getValuesData().getFormula();
                int index = SheetUtil.parseCellColIndexFromRef(formula);

                XDDFNumericalDataSource<Double> valuesData = XDDFDataSourcesFactory.fromNumericCellRange(sheet, new CellRangeAddress(startRow, endRow, index, index));
                series.replaceData(category, valuesData);
            }
            chart.plot(chartSerData);
        }
    }

    protected XSSFSheet getDefaultSheet(XSSFWorkbook workbook) {
        int nums = workbook.getNumberOfSheets();
        if (nums == 0) {
            throw new ChartException("No sheet found in the workbook");
        }
        return workbook.getSheetAt(0);//default
    }


    public void fill(XWPFChart chart, Table table) {
        if (table == null) {
            return;
        }

        try {
            int dataRowStartIndex = 1;
            int dataRowEndIndex = dataRowStartIndex + table.rowSize() - 1;
            int currentRowIndex = dataRowStartIndex;

            XSSFWorkbook workbook = chart.getWorkbook();
            XSSFSheet sheet = getDefaultSheet(workbook);

            List<String> config = readConfig(sheet, dataRowStartIndex);

            if (config.isEmpty()) {
                return;
            }

            for (int r = 0; r < table.rowSize(); r++) {
                XSSFRow currentRow = sheet.getRow(currentRowIndex);
                if (currentRow == null) {
                    currentRow = sheet.createRow(currentRowIndex);//找不到行，创建行
                }
                for (int i = 0; i < config.size(); i++) {
                    XSSFCell cell = currentRow.getCell(i);
                    if (cell == null) {
                        cell = currentRow.createCell(i);//没有单元格就创建单元格
                    }
                    Object cellValue = table.getValue(r, config.get(i));//按顺序填充list的数据
                    if ((r | i) == 0) {//category or series
                        cell.setCellValue(cellValue.toString());
                    } else {
                        cell.setCellValue(NumberUtil.toNum(cellValue).doubleValue());
                    }
                }
                currentRowIndex++;
            }

            plot(chart, sheet, dataRowStartIndex, dataRowEndIndex);
        } catch (IOException | InvalidFormatException e) {
            throw new RuntimeException(e);
        }
    }

    protected List<String> readConfig(XSSFSheet sheet, int index) {
        XSSFRow row = sheet.getRow(index);
        short lastCellNum = row.getLastCellNum();

        List<String> config = new ArrayList<>();
        for (int i = 0; i < lastCellNum; i++) {
            XSSFCell cell = row.getCell(i);
            String key = cell.getStringCellValue();
            config.add(key);
        }

        return  config;
    }

}
