package ${package.Entity}.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel(value = "${entity}Dto")
public class ${entity}Dto {
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
