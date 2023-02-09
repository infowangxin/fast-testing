package com.nutcracker.util;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.util.IOUtils;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 胡桃夹子
 * @date 2023-02-09 20:17
 */
public class ExcelExportTest {
    public static void main(String[] args) {
        // 查询所有的省名称
        List<String> provNameList = new ArrayList<>();
        provNameList.add("安徽省");
        provNameList.add("浙江省");

        // 整理数据，放入一个Map中，mapkey存放父地点，value 存放该地点下的子区域
        Map<String, List<String>> siteMap = new HashMap<>();
        siteMap.put("浙江省", Arrays.asList("杭州市", "宁波市"));
        siteMap.put("安徽省", Arrays.asList("芜湖市", "滁州市"));
        siteMap.put("芜湖市", Arrays.asList("戈江区", "三山区"));
        siteMap.put("滁州市", Arrays.asList("来安县", "凤阳县"));

        // 1)创建workbook
        HSSFWorkbook book = new HSSFWorkbook();

        // 2)创建sheet
        Sheet sheet1 = book.createSheet("mainSheet");// 主sheet

        // 创建需要用户填写的数据页
        // 设计表头
        //Sheet sheet1 = book.getSheet("sheet1");
        Row row0 = sheet1.createRow(4);
        row0.createCell(0).setCellValue("省");
        row0.createCell(1).setCellValue("市");
        row0.createCell(2).setCellValue("区");

        //创建一个专门用来存放地区信息的隐藏sheet页
        //因此也不能在现实页之前创建，否则无法隐藏。
        Sheet hideSheet = book.createSheet("site");
        book.setSheetHidden(book.getSheetIndex(hideSheet), true);

        int rowId = 0;
        // 设置第一行，存省的信息
        Row proviRow = hideSheet.createRow(rowId++);
        proviRow.createCell(0).setCellValue("省列表");
        for (int i = 0; i < provNameList.size(); i++) {
            Cell proviCell = proviRow.createCell(i + 1);
            proviCell.setCellValue(provNameList.get(i));
        }
        // 将具体的数据写入到每一行中，行开头为父级区域，后面是子区域。
        for (String key : siteMap.keySet()) {
            List<String> son = siteMap.get(key);

            Row row = hideSheet.createRow(rowId++);
            row.createCell(0).setCellValue(key);
            for (int i = 0; i < son.size(); i++) {
                Cell cell = row.createCell(i + 1);
                cell.setCellValue(son.get(i));
            }

            // 添加名称管理器
            String range = getRange(1, rowId, son.size());
            Name name = book.createName();
            name.setNameName(key);
            String formula = "site!" + range;
            name.setRefersToFormula(formula);
        }

        // 省规则
        DVConstraint provConstraint = DVConstraint.createExplicitListConstraint(provNameList.toArray(new String[]{}));
        CellRangeAddressList provRangeAddressList = new CellRangeAddressList(5, 5, 0, 0);
        DataValidation provinceDataValidation = new HSSFDataValidation(provRangeAddressList, provConstraint);
        provinceDataValidation.createErrorBox("error", "请选择正确的省份");
        sheet1.addValidationData(provinceDataValidation);

        // 市以规则，此处仅作一个示例
        // "INDIRECT($A$" + 2 + ")" 表示规则数据会从名称管理器中获取key与单元格 A2 值相同的数据，如果A2是浙江省，那么此处就是浙江省下的区域信息。
        DVConstraint formula = DVConstraint.createFormulaListConstraint("INDIRECT($A$" + 6 + ")");
        CellRangeAddressList rangeAddressList = new CellRangeAddressList(5, 10, 1, 1);
        DataValidation cacse = new HSSFDataValidation(rangeAddressList, formula);
        cacse.createErrorBox("error", "请选择正确的市");
        sheet1.addValidationData(cacse);

        // 区规则
        formula = DVConstraint.createFormulaListConstraint("INDIRECT($B$" + 6 + ")");
        rangeAddressList = new CellRangeAddressList(5, 10, 2, 2);
        cacse = new HSSFDataValidation(rangeAddressList, formula);
        cacse.createErrorBox("error", "请选择正确的区");
        sheet1.addValidationData(cacse);

        FileOutputStream os = null;
        try {
            //os = new FileOutputStream("D:/excelExport.xls");
            os = new FileOutputStream("/Users/vincent/Downloads/excelExport.xls");
            book.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    /**
     * @param offset   偏移量，如果给0，表示从A列开始，1，就是从B列
     * @param rowId    第几行
     * @param colCount 一共多少列
     * @return 如果给入参 1,1,10. 表示从B1-K1。最终返回 $B$1:$K$1
     * @author denggonghai 2016年8月31日 下午5:17:49
     */
    public static String getRange(int offset, int rowId, int colCount) {
        char start = (char) ('A' + offset);
        if (colCount <= 25) {
            char end = (char) (start + colCount - 1);
            return "$" + start + "$" + rowId + ":$" + end + "$" + rowId;
        } else {
            char endPrefix = 'A';
            char endSuffix = 'A';
            if ((colCount - 25) / 26 == 0 || colCount == 51) {// 26-51之间，包括边界（仅两次字母表计算）
                if ((colCount - 25) % 26 == 0) {// 边界值
                    endSuffix = (char) ('A' + 25);
                } else {
                    endSuffix = (char) ('A' + (colCount - 25) % 26 - 1);
                }
            } else {// 51以上
                if ((colCount - 25) % 26 == 0) {
                    endSuffix = (char) ('A' + 25);
                    endPrefix = (char) (endPrefix + (colCount - 25) / 26 - 1);
                } else {
                    endSuffix = (char) ('A' + (colCount - 25) % 26 - 1);
                    endPrefix = (char) (endPrefix + (colCount - 25) / 26);
                }
            }
            return "$" + start + "$" + rowId + ":$" + endPrefix + endSuffix + "$" + rowId;
        }
    }

}
