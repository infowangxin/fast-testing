package com.nutcracker.util;

import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFDataValidationHelper;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.util.IOUtils;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ExcelExport {


    public static void main(String[] args) {
        export();
    }

    public static void export() {
        // 1.准备数据
        // 2)多级联动下拉数据
        List<String> provinceList = new ArrayList<>();
        provinceList.add("云塘街道");
        provinceList.add("雨湖路街道");
        provinceList.add("昭潭街道");
        provinceList.add("广场街道");
        provinceList.add("城正街街道");
        provinceList.add("窑湾街道");

        // 整理数据，放入一个Map中，mapkey存放父地点，value 存放该地点下的子区域
        Map<String, List<String>> siteMap = new HashMap<>();
        siteMap.put("云塘街道", Arrays.asList("云塘街道本级", "杉树巷社区居委会", "公园社区居委会", "火车站社区居委会", "繁城社区居委会"));
        siteMap.put("雨湖路街道", Arrays.asList("雨湖街道本级", "关圣殿社区居委会", "古梁巷社区居委会", "风车坪社区居委会", "车站路社区居委会", "和平桥社区居委会"));
        siteMap.put("昭潭街道", Arrays.asList("昭潭街道本级", "许家铺社区居委会", "白石社区居委会", "广园社区居委会", "烟竹社区居委会", "砂子岭社区居委会", "宝庆路社区居委会", "宝丰街社区居委会", "富民城社区居委会"));
        siteMap.put("广场街道", Arrays.asList("广场街道本级", "建设社区居委会", "中心社区居委会", "福利社区居委会", "韶山路社区居委会", "南盘岭社区居委会", "和平社区居委会"));

        // 2.创建Excel
        // 1)创建workbook
        HSSFWorkbook hssfWorkBook = new HSSFWorkbook();
        // 2)创建sheet
        HSSFSheet mainSheet = hssfWorkBook.createSheet("mainSheet");// 主sheet
        mainSheet.setDefaultColumnWidth(20);
        // 用于展示
        //2.1 创建表头，供用户输入
        HSSFRow headRow = mainSheet.createRow(0);// 创建第一行
        headRow.createCell(0).setCellValue("乡镇/街道");
        headRow.createCell(1).setCellValue("村/社区");
        headRow.createCell(2).setCellValue("姓名");
        headRow.createCell(3).setCellValue("身份证号码");
        headRow.createCell(4).setCellValue("居住地址");
        headRow.createCell(5).setCellValue("所属部门");

        HSSFSheet siteSheet = hssfWorkBook.createSheet("siteSheet");// 隐藏sheet
        hssfWorkBook.setSheetHidden(hssfWorkBook.getSheetIndex(siteSheet), true);// // true:隐藏/false:显示

        // 3.写入数据
        writeData(hssfWorkBook, siteSheet, provinceList, siteMap);// 将数据写入隐藏的sheet中并做好关联关系
        // 4.设置数据有效性
        setDataValid(hssfWorkBook, mainSheet, provinceList, siteMap);

        FileOutputStream os = null;
        try {
            os = new FileOutputStream("/Users/vincent/Downloads/excelExport.xls");
            //os = new FileOutputStream("D:/excelExport.xls");
            hssfWorkBook.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    //
    public static void setDataValid(HSSFWorkbook HSSFWorkBook, HSSFSheet mainSheet, List<String> provinceList, Map<String, List<String>> siteMap) {
        //设置省份下拉
        HSSFDataValidationHelper dvHelper = new HSSFDataValidationHelper((HSSFSheet) mainSheet);
        DataValidationConstraint provinceConstraint = dvHelper.createExplicitListConstraint(provinceList.toArray(new String[]{}));
        CellRangeAddressList provinceRangeAddressList = new CellRangeAddressList(1, 60, 0, 0);//意思是从B1：B61 为下拉
        DataValidation provinceDataValidation = dvHelper.createValidation(provinceConstraint, provinceRangeAddressList);
        provinceDataValidation.createErrorBox("error", "请选择正确的乡镇/街道");
        provinceDataValidation.setShowErrorBox(true);
        mainSheet.addValidationData(provinceDataValidation);

        // 设置市、区下拉
        for (int i = 0; i <= 60; i++) {
            setDataValidation('A', mainSheet, i + 1, 1); // "B"是指省所在的列，i+1初始值为1代表从第2行开始，2要与“B”对应，为B的列号加1，假如第一个参数为“C”，那么最后一个参数就3
        }
    }

    public static void setDataValidation(char offset, HSSFSheet sheet, int rowNum, int colNum) {
        HSSFDataValidationHelper dvHelper = new HSSFDataValidationHelper(sheet);
        DataValidation dataValidationList1;
        dataValidationList1 = getDataValidationByFormula("INDIRECT($" + offset + (rowNum) + ")", rowNum, colNum, dvHelper);
        sheet.addValidationData(dataValidationList1);
    }

    private static DataValidation getDataValidationByFormula(String formulaString, int naturalRowIndex, int naturalColumnIndex, HSSFDataValidationHelper dvHelper) {
        DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint(formulaString);
        CellRangeAddressList regions = new CellRangeAddressList(naturalRowIndex, 10000, naturalColumnIndex, naturalColumnIndex);
        HSSFDataValidation data_validation_list = (HSSFDataValidation) dvHelper.createValidation(dvConstraint, regions);
        data_validation_list.setEmptyCellAllowed(false);
        if (data_validation_list instanceof HSSFDataValidation) {
            data_validation_list.setShowErrorBox(true);
        } else {
        }
        // 设置输入信息提示信息
        data_validation_list.createPromptBox("下拉选择提示", "请使用下拉方式选择合适的值！");
        return data_validation_list;
    }

    public static void writeData(HSSFWorkbook hssfWorkBook, HSSFSheet siteSheet, List<String> provinceList, Map<String, List<String>> siteMap) {
        //循环将省数据写入siteSheet的第1行中
        int siteRowId = 0;
        HSSFRow provinceRow = siteSheet.createRow(siteRowId++);
        provinceRow.createCell(0).setCellValue("省列表");
        for (int i = 0; i < provinceList.size(); i++) {
            provinceRow.createCell(i + 1).setCellValue(provinceList.get(i));
        }
        // 将具体的数据写入到每一行中，行开头为父级区域，后面是子区域。
        Iterator<String> keyIterator = siteMap.keySet().iterator();
        while (keyIterator.hasNext()) {
            String key = keyIterator.next();
            List<String> son = siteMap.get(key);
            HSSFRow siteRow = siteSheet.createRow(siteRowId++);
            siteRow.createCell(0).setCellValue(key);
            for (int i = 0; i < son.size(); i++) {
                siteRow.createCell(i + 1).setCellValue(son.get(i));
            }
            // 添加名称管理器
            String range = getRange(1, siteRowId, son.size());
            Name name = hssfWorkBook.createName();
            name.setNameName(key);
            String formula = siteSheet.getSheetName() + "!" + range;
            name.setRefersToFormula(formula);
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


