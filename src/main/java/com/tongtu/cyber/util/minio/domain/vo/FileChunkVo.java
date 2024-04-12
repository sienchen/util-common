package com.tongtu.cyber.util.minio.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "FileChunkVo")
public class FileChunkVo {
    @ApiModelProperty(value = "文件记录id")
    private String id;
    @ApiModelProperty(value = "文件路径")
    @TableField("file_path")
    private String filePath;
    @ApiModelProperty(value = "已上传分片下标")
    private Integer[] indexArr;
    @ApiModelProperty(value = "是否上传完成")
    private Boolean flag;
}
