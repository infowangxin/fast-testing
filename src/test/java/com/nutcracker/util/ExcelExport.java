package com.nutcracker.util;

import com.google.common.collect.Lists;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFDataValidationHelper;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationConstraint.OperatorType;
import org.apache.poi.ss.usermodel.DataValidationConstraint.ValidationType;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.util.IOUtils;

import java.io.FileOutputStream;
import java.util.ArrayList;
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
        // 1)单个下拉数据
        List<String> genderList = new ArrayList<String>();
        genderList.add("male");
        genderList.add("female");
        // 2)多级联动下拉数据
        List<String> provinceList = new ArrayList<String>();
        provinceList.add("广东省");
        provinceList.add("湖北省");
        Map<String, List<String>> siteMap = new HashMap<String, List<String>>();
        siteMap.put("广东省", Lists.newArrayList("广州市", "佛山市"));
        siteMap.put("湖北省", Lists.newArrayList("武汉市", "荆州市"));
        siteMap.put("广州市", Lists.newArrayList("白云区", "越秀区"));
        siteMap.put("佛山市", Lists.newArrayList("顺德区", "南海区"));
// 2.创建Excel
        // 1)创建workbook
        HSSFWorkbook hssfWorkBook = new HSSFWorkbook();
        // 2)创建sheet
        HSSFSheet mainSheet = hssfWorkBook.createSheet("mainSheet");// 主sheet
        // 用于展示
        //2.1 创建表头，供用户输入
        HSSFRow headRow = mainSheet.createRow(0);// 创建第一行
        headRow.createCell(0).setCellValue("gender");
        headRow.createCell(1).setCellValue("province");
        headRow.createCell(2).setCellValue("city");
        headRow.createCell(3).setCellValue("area");
        headRow.createCell(4).setCellValue("date");
        headRow.createCell(5).setCellValue("num1");
        headRow.createCell(6).setCellValue("num2");

        HSSFSheet genderSheet = hssfWorkBook.createSheet("genderSheet");// 隐藏sheet
        // 用于隐藏性别数据
        HSSFSheet siteSheet = hssfWorkBook.createSheet("siteSheet");// 隐藏sheet
        // 用于隐藏地点数据
        hssfWorkBook.setSheetHidden(hssfWorkBook.getSheetIndex(genderSheet), false);// 设置sheet是否隐藏
        // true:隐藏/false:显示
        hssfWorkBook.setSheetHidden(hssfWorkBook.getSheetIndex(siteSheet), false);// 设置sheet是否隐藏
        // true:隐藏/false:显示
// 3.写入数据
        writeData(hssfWorkBook, genderSheet, siteSheet, genderList, provinceList, siteMap);// 将数据写入隐藏的sheet中并做好关联关系
// 4.设置数据有效性
        setDataValid(hssfWorkBook, mainSheet, genderList, provinceList, siteMap);
        // 5.设置时间规则
        setDateFormat(hssfWorkBook, mainSheet);
        // 6.设置数据规则
        List<Integer> cellNumList = new ArrayList<Integer>();
        cellNumList.add(5);
        cellNumList.add(6);
        setNumberFormat(hssfWorkBook, mainSheet, cellNumList);
        FileOutputStream os = null;
        try {
            //os = new FileOutputStream("D:/excelExport.xls");
            os = new FileOutputStream("/Users/vincent/Downloads/excelExport.xls");
            hssfWorkBook.write(os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(os);
        }
    }

    //
    public static void setDataValid(HSSFWorkbook HSSFWorkBook, HSSFSheet mainSheet, List<String> genderList, List<String> provinceList, Map<String, List<String>> siteMap) {
        //设置省份下拉
        HSSFDataValidationHelper dvHelper = new HSSFDataValidationHelper((HSSFSheet) mainSheet);
        DataValidationConstraint provinceConstraint = dvHelper.createExplicitListConstraint(provinceList.toArray(new String[]{}));
        CellRangeAddressList provinceRangeAddressList = new CellRangeAddressList(1, 60, 1, 1);//意思是从B2：B61 为下拉
        DataValidation provinceDataValidation = dvHelper.createValidation(provinceConstraint, provinceRangeAddressList);
        provinceDataValidation.createErrorBox("error", "请选择正确的省份");
        provinceDataValidation.setShowErrorBox(true);
        // provinceDataValidation.setSuppressDropDownArrow(true);
        mainSheet.addValidationData(provinceDataValidation);

        //设置性别下拉
        DataValidationConstraint genderConstraint = dvHelper.createFormulaListConstraint("gender");
        CellRangeAddressList genderRangeAddressList = new CellRangeAddressList(1, 60, 0, 0);//意思是从A2：A61 为下拉
        HSSFDataValidation genderDataValidation = (HSSFDataValidation) dvHelper.createValidation(genderConstraint, genderRangeAddressList);
        genderDataValidation.createErrorBox("Error", "请选择或输入有效的选项，或下载最新模版重试！");
        // genderDataValidation.setSuppressDropDownArrow(true);
        mainSheet.addValidationData(genderDataValidation);

        // 设置市、区下拉
        for (int i = 0; i <= 60; i++) {
            setDataValidation('B', mainSheet, i + 1, 2);// "B"是指省所在的列，i+1初始值为1代表从第2行开始，2要与“B”对应，为B的列号加1，假如第一个参数为“C”，那么最后一个参数就3
        }
    }

    public static void setDataValidation(char offset, HSSFSheet sheet, int rowNum, int colNum) {
        HSSFDataValidationHelper dvHelper = new HSSFDataValidationHelper(sheet);
        DataValidation dataValidationList1;
        DataValidation dataValidationList2;
        dataValidationList1 = getDataValidationByFormula("INDIRECT($" + offset + (rowNum) + ")", rowNum, colNum, dvHelper);
        dataValidationList2 = getDataValidationByFormula("INDIRECT($" + (char) (offset + 1) + (rowNum) + ")", rowNum, colNum + 1, dvHelper);
        sheet.addValidationData(dataValidationList1);
        sheet.addValidationData(dataValidationList2);
    }

    private static DataValidation getDataValidationByFormula(String formulaString, int naturalRowIndex, int naturalColumnIndex, HSSFDataValidationHelper dvHelper) {
        DataValidationConstraint dvConstraint = dvHelper.createFormulaListConstraint(formulaString);
        CellRangeAddressList regions = new CellRangeAddressList(naturalRowIndex, 65535, naturalColumnIndex, naturalColumnIndex);
        HSSFDataValidation data_validation_list = (HSSFDataValidation) dvHelper.createValidation(dvConstraint, regions);
        data_validation_list.setEmptyCellAllowed(false);
        if (data_validation_list instanceof HSSFDataValidation) {
            // data_validation_list.setSuppressDropDownArrow(true);
            data_validation_list.setShowErrorBox(true);
        } else {
            // data_validation_list.setSuppressDropDownArrow(false);
        }
        // 设置输入信息提示信息
        data_validation_list.createPromptBox("下拉选择提示", "请使用下拉方式选择合适的值！");
        return data_validation_list;
    }

    public static void writeData(HSSFWorkbook hssfWorkBook, HSSFSheet genderSheet, HSSFSheet siteSheet, List<String> genderList, List<String> provinceList, Map<String, List<String>> siteMap) {
        //循环将性别的数据写入genderSheet的第A列中
        for (int i = 0; i < genderList.size(); i++) {
            HSSFRow genderRow = genderSheet.createRow(i);
            genderRow.createCell(0).setCellValue(genderList.get(i));
        }
        initGenderMapping(hssfWorkBook, genderSheet.getSheetName(), genderList.size());// 创建性别数据规则
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

    // 创建性别数据规则
    private static void initGenderMapping(HSSFWorkbook workbook, String genderSheetName, int genderQuantity) {
        Name genderName = workbook.createName();
        genderName.setNameName("gender");
        genderName.setRefersToFormula(genderSheetName + "!$A$1:$A$" + genderQuantity);
    }

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

    // TODO 测试时间格式
    public static void setDateFormat(HSSFWorkbook HSSFWorkbook, HSSFSheet mainsheet) {
        HSSFDataValidationHelper dvHelper = new HSSFDataValidationHelper(mainsheet);
        CellRangeAddressList regions = new CellRangeAddressList(1, 60, 4, 4);
        DataValidationConstraint dvConstraint = dvHelper.createDateConstraint(OperatorType.BETWEEN, "1900-01-01", "5000-12-31", "yyyy-MM-dd");
        HSSFDataValidation dataValidation = (HSSFDataValidation) dvHelper.createValidation(dvConstraint, regions);
        // dataValidation.setSuppressDropDownArrow(false);
        dataValidation.createPromptBox("输入提示", "请填写日期格式'yyyy-mm-dd'");
        dataValidation.setShowPromptBox(true);
        dataValidation.createErrorBox("日期格式错误提示", "你输入的日期格式不符合'yyyy-mm-dd'格式规范，请重新输入！");
        dataValidation.setShowErrorBox(true);
        mainsheet.addValidationData(dataValidation);
    }

    // TODO 测试数字规则
    public static void setNumberFormat(HSSFWorkbook HSSFWorkbook, HSSFSheet mainsheet, List<Integer> colNum) {

        if (colNum.size() > 0) {
            for (Integer index : colNum) {
                HSSFDataValidationHelper dvHelper = new HSSFDataValidationHelper(mainsheet);
                CellRangeAddressList regions = new CellRangeAddressList(1, 60, index, index);
                DataValidationConstraint dvConstraint = dvHelper.createNumericConstraint(ValidationType.DECIMAL, OperatorType.BETWEEN, "0.0001", "100000");
                HSSFDataValidation dataValidation = (HSSFDataValidation) dvHelper.createValidation(dvConstraint, regions);
                // dataValidation.setSuppressDropDownArrow(false);
                dataValidation.createPromptBox("", "请填写数字(至多4位小数)!");
                dataValidation.setShowPromptBox(true);
                dataValidation.createErrorBox("数字格式错误提示", "你输入的数字不正确,请重新输入!");
                dataValidation.setShowErrorBox(true);
                mainsheet.addValidationData(dataValidation);
            }
        }
    }
}


