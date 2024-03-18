package com.tongtu.cyber.util.pram;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author : 陈世恩
 * @date : 2023/5/15 11:36
 */
@Data
@ApiModel(value = "ids")
public class CommonIdsParam {
    @NotNull(message = "ids:不为空")
    @ApiModelProperty(value = "ids", required = true)
    private List<String> ids;
}
