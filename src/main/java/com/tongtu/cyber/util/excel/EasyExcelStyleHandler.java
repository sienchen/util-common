package com.tongtu.cyber.util.excel;

import com.alibaba.excel.metadata.data.ImageData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import java.util.ArrayList;
import java.util.List;

/**
 * excel图表样式设置
 * @author : 陈世恩
 * @date : 2023/5/12 15:18
 */
public class EasyExcelStyleHandler {
    public static HorizontalCellStyleStrategy initStyle() {
        //内容样式策略，垂直居中,水平居中
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setWrapped(true);
        // 字体策略
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontHeightInPoints((short) 12);
        contentWriteCellStyle.setWriteFont(contentWriteFont);
        //表头策略使用默认
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        headWriteCellStyle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        headWriteCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        return new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
    }

    public static WriteCellData imageCellsStyle(byte[] bytes) {

        WriteCellData<Void> writeCellData = new WriteCellData<>();
        // 这里可以设置为 EMPTY 则代表不需要其他数据了
        //writeCellData.setType(CellDataTypeEnum.EMPTY);

        // 可以放入多个图片
        List<ImageData> imageDataList = new ArrayList<>();
        writeCellData.setImageDataList(imageDataList);

        ImageData imageData = new ImageData();
        imageDataList.add(imageData);
        // 设置图片
        imageData.setImage(bytes);
        // 图片类型
        //imageData.setImageType(ImageData.ImageType.PICTURE_TYPE_PNG);
        // 上 右 下 左 需要留空，这个类似于 css 的 margin；这里实测 不能设置太大 超过单元格原始大小后 打开会提示修复。暂时未找到很好的解法。
        imageData.setTop(10);
        imageData.setRight(10);
        imageData.setBottom(10);
        imageData.setLeft(10);

        // * 设置图片的位置。Relative表示相对于当前的单元格index。first是左上点，last是对角线的右下点，这样确定一个图片的位置和大小。
        // 目前填充模板的图片变量是images，index：row=7,column=0。所有图片都基于此位置来设置相对位置
        // 第1张图片相对位置
        imageData.setRelativeFirstRowIndex(0);
        imageData.setRelativeFirstColumnIndex(0);
        imageData.setRelativeLastRowIndex(20);
        imageData.setRelativeLastColumnIndex(5);

        return writeCellData;
    }
}
