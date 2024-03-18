package com.tongtu.cyber.util.chart;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * 图表日期返回model
 * @author : 陈世恩
 * @date : 2023/6/6 17:46
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChartModel {
    @ApiModelProperty(value = "图例名称")
    private String name;
    @ApiModelProperty(value = "值集合")
    private List<Map<String, Object>> values;
    @ApiModelProperty(value = "图例日期")
    private String date;

}
