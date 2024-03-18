package com.tongtu.cyber.modules.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "GhBaseUnitDto")
public class GhBaseUnitDto {
    private Integer pageNo = 1;
    private Integer pageSize = 10;
    @ApiModelProperty(value = "ids集合")
    private List<String> ids;
    @ApiModelProperty(value = "名称")
    private String name;
    @ApiModelProperty(value = "开始时间：yyyy-MM-dd")
    private String startTime;
    @ApiModelProperty(value = "结束时间：yyyy-MM-dd")
    private String endTime;
}
