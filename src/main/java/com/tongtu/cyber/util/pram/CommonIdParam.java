package com.tongtu.cyber.util.pram;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author : 陈世恩
 * @date : 2023/5/15 11:36
 */
@Data
@ApiModel(value = "id")
public class CommonIdParam {
    @NotNull(message = "id:不为空")
    @ApiModelProperty(value = "id", required = true)
    private String id;
}
