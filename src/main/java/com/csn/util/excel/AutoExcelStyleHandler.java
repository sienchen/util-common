package com.csn.util.excel;

/**
 * excel图表样式设置
 * @author : 陈世恩
 * @date : 2023/5/12 15:18
 */

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.export.styler.ExcelExportStylerDefaultImpl;

public class AutoExcelStyleHandler extends ExcelExportStylerDefaultImpl {

    public AutoExcelStyleHandler(Workbook workbook) {
        super(workbook);
    }

    /**
     * 这里设置表头的格式，最上面的一行
     */
    @Override
    public CellStyle getHeaderStyle(short color) {
        CellStyle cellStyle = super.getHeaderStyle(color);
        cellStyle.setFont(getFont(workbook, 11, true));
        cellStyle.setFillBackgroundColor(HSSFColor.HSSFColorPredefined.GREY_50_PERCENT.getIndex());
        return cellStyle;
    }

    /**
     * 列标题
     */
    @Override
    public CellStyle getTitleStyle(short color) {
        CellStyle cellStyle = super.getTitleStyle(color);
        // 仅将表头的字体设置大一号且加粗
        cellStyle.setFont(getFont(workbook, 11, true));
        cellStyle.setFillForegroundColor(HSSFColor.HSSFColorPredefined.LIGHT_TURQUOISE.getIndex());
        return cellStyle;
    }

    /*以下都是行样式，交替*/

    /**
     * 行样式
     */
    @Override
    public CellStyle stringSeptailStyle(Workbook workbook, boolean isWarp) {
        CellStyle cellStyle = super.stringSeptailStyle(workbook, isWarp);
        cellStyle.setFont(getFont(workbook, 10, false));
        return cellStyle;
    }

    /**
     * 这里设置循环行，没有样式的一行
     */
    @Override
    public CellStyle stringNoneStyle(Workbook workbook, boolean isWarp) {
        CellStyle cellStyle = super.stringNoneStyle(workbook, isWarp);
        cellStyle.setFont(getFont(workbook, 10, false));
        return cellStyle;
    }

    /**
     * 字体样式
     *
     * @param size   字体大小
     * @param isBold 是否加粗
     * @return
     */
    private Font getFont(Workbook workbook, int size, boolean isBold) {
        Font font = workbook.createFont();
        // 字体样式
        font.setFontName("宋体");
        // 是否加粗
        font.setBold(isBold);
        // 字体大小
        font.setFontHeightInPoints((short) size);
        return font;
    }

}