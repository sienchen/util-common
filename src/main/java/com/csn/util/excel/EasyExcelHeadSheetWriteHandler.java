package com.csn.util.excel;

import com.alibaba.excel.write.handler.SheetWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteWorkbookHolder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 * excel图表样式设置
 * @author : 陈世恩
 * @date : 2023/5/12 15:18
 */
public class EasyExcelHeadSheetWriteHandler implements SheetWriteHandler {
    //设置标题
    String headName;
    //标题占列宽
    int cellNum = 3;

    //标题占几行高
    int rowNum = 0;

    public EasyExcelHeadSheetWriteHandler(String name, int rowNum, int cellNum) {
        this.headName = name;
        this.cellNum = cellNum-1;
        this.rowNum = rowNum-1;
    }


    @Override
    public void beforeSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
    }

    @Override
    public void afterSheetCreate(WriteWorkbookHolder writeWorkbookHolder, WriteSheetHolder writeSheetHolder) {
        Workbook workbook = writeWorkbookHolder.getWorkbook();
        Sheet sheet = workbook.getSheetAt(0);
        //设置第一行内容
        /* Row row1 = sheet.createRow(0);
        row1.setHeight((short) 500);
        Cell cell = row1.createCell(0);//设置单元格内容
        cell.setCellValue("附件2");//设置标题Row//设置填表日期,填报人,联系方式 */
        //设置第二行内容
        Row row2 = sheet.createRow(0);
        row2.setHeight((short) 800);
        Cell cell2 = row2.createCell(0);
        cell2.setCellValue(headName);
        //设置第三行内容
       /*  Row row3 = sheet.createRow(2);
        row3.setHeight((short) 500);
        row3.createCell(1).setCellValue("填表日期");
        row3.createCell(11).setCellValue("填表人");
        row3.createCell(15).setCellValue("联系方式"); */
        //设置内容格式
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight((short) 400);
        font.setFontName("宋体");
        cellStyle.setFont(font);
        cell2.setCellStyle(cellStyle);
        sheet.addMergedRegionUnsafe(new CellRangeAddress(0, rowNum, 0, cellNum));
    }
}