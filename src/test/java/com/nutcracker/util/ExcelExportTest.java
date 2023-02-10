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
        provNameList.add("云塘街道");
        provNameList.add("雨湖路街道");
        provNameList.add("昭潭街道");
        provNameList.add("广场街道");
        provNameList.add("城正街街道");
        provNameList.add("窑湾街道");

        // 整理数据，放入一个Map中，mapkey存放父地点，value 存放该地点下的子区域
        Map<String, List<String>> siteMap = new HashMap<>();
        siteMap.put("云塘街道", Arrays.asList("云塘街道本级", "杉树巷社区居委会", "公园社区居委会", "火车站社区居委会", "繁城社区居委会", "雪园社区居委会", "万新社区居委会", "繁城社区居委会", "雪园社区居委会", "万新社区居委会"));
        siteMap.put("雨湖路街道", Arrays.asList("雨湖街道本级", "关圣殿社区居委会", "古梁巷社区居委会", "风车坪社区居委会", "车站路社区居委会", "和平桥社区居委会", "雨湖路社区居委会", "繁城社区居委会", "雪园社区居委会", "万新社区居委会"));
        siteMap.put("昭潭街道", Arrays.asList("昭潭街道本级", "许家铺社区居委会", "白石社区居委会", "广园社区居委会", "烟竹社区居委会", "砂子岭社区居委会", "宝庆路社区居委会", "宝丰街社区居委会", "富民城社区居委会", "繁城社区居委会", "雪园社区居委会", "万新社区居委会"));
        siteMap.put("广场街道", Arrays.asList("广场街道本级", "建设社区居委会", "中心社区居委会", "福利社区居委会", "韶山路社区居委会", "南盘岭社区居委会", "和平社区居委会", "繁城社区居委会", "雪园社区居委会", "万新社区居委会"));
        siteMap.put("城正街街道", Arrays.asList("城正街街道本级", "泗洲庵社区居委会", "洗脚桥社区居委会", "三义井社区居委会", "瞻岳门社区居委会", "熙春路社区居委会", "繁城社区居委会", "雪园社区居委会", "万新社区居委会"));
        siteMap.put("窑湾街道", Arrays.asList("窑湾街道本级", "罗祖殿社区居委会", "龙子巷社区居委会", "鲁班殿社区居委会", "大码头社区居委会", "唐兴寺社区居委会", "繁城社区居委会", "雪园社区居委会", "万新社区居委会"));

        // 1)创建workbook
        HSSFWorkbook book = new HSSFWorkbook();

        // 2)创建sheet
        Sheet sheet1 = book.createSheet("mainSheet");// 主sheet
        sheet1.setDefaultColumnWidth(20);
        Row row0 = sheet1.createRow(0);
        row0.createCell(0).setCellValue("乡镇/街道");
        row0.createCell(1).setCellValue("村/社区");
        row0.createCell(2).setCellValue("姓名");
        row0.createCell(3).setCellValue("身份证号码");
        row0.createCell(4).setCellValue("居住地址");
        row0.createCell(5).setCellValue("所属部门");

        //创建一个专门用来存放地区信息的隐藏sheet页
        //因此也不能在现实页之前创建，否则无法隐藏。
        Sheet hideSheet = book.createSheet("site");
        //book.setSheetHidden(book.getSheetIndex(hideSheet), true);

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

        // 规则
        DVConstraint provConstraint = DVConstraint.createExplicitListConstraint(provNameList.toArray(new String[]{}));
        CellRangeAddressList provRangeAddressList = new CellRangeAddressList(1, 10000, 0, 0);
        DataValidation provinceDataValidation = new HSSFDataValidation(provRangeAddressList, provConstraint);
        provinceDataValidation.createErrorBox("error", "请选择正确的省份");
        sheet1.addValidationData(provinceDataValidation);

        // "INDIRECT($A$" + 2 + ")" 表示规则数据会从名称管理器中获取key与单元格 A2 值相同的数据，如果A2是浙江省，那么此处就是浙江省下的区域信息。
        DVConstraint formula = DVConstraint.createFormulaListConstraint("INDIRECT($A$" + 2 + ")");
        CellRangeAddressList rangeAddressList = new CellRangeAddressList(1, 10000, 1, 1);
        DataValidation cacse = new HSSFDataValidation(rangeAddressList, formula);
        cacse.createErrorBox("error", "请选择正确的市");
        sheet1.addValidationData(cacse);

        // 设置市、区下拉

        FileOutputStream os = null;
        try {
            os = new FileOutputStream("/Users/vincent/Downloads/ExcelExportTest.xls");
            //os = new FileOutputStream("d:/xxx.xls");
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
