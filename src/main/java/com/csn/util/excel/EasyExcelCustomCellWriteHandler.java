package com.csn.util.excel;

import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.style.column.AbstractColumnWidthStyleStrategy;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;

import java.util.List;

/**
 * excel图表样式设置
 * @author : 陈世恩
 * @date : 2023/5/12 15:18
 */
@Slf4j
public class EasyExcelCustomCellWriteHandler extends AbstractColumnWidthStyleStrategy {


    private static final int MAX_COLUMN_WIDTH = 255;
    private static final int PADDING_WIDTH = 6;

    @Override
    protected void setColumnWidth(WriteSheetHolder writeSheetHolder, List<WriteCellData<?>> cellDataList, Cell cell, Head head, Integer integer, Boolean isHead) {
        if (isHead) {
            int columnWidth = cell.getStringCellValue().length() * 2 + PADDING_WIDTH;
            columnWidth = Math.min(columnWidth, MAX_COLUMN_WIDTH);
            writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(), columnWidth * 256);
        }
    }
}
