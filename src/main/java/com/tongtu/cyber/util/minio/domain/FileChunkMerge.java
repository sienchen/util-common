package com.tongtu.cyber.util.minio.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 文件分片合并记录
 * </p>
 *
 * @author 陈世恩
 * @since 2024-03-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("file_chunk_merge")
@ApiModel(value="FileChunkMerge对象", description="文件分片合并记录")
public class FileChunkMerge implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId("id")
    private String id;

    @ApiModelProperty(value = "文件真实名称")
    @TableField("file_real_name")
    private String fileRealName;

    @ApiModelProperty(value = "文件名")
    @TableField("file_name")
    private String fileName;

    @ApiModelProperty(value = "文件后缀")
    @TableField("suffix")
    private String suffix;

    @ApiModelProperty(value = "文件路径")
    @TableField("file_path")
    private String filePath;

    @ApiModelProperty(value = "文件类型")
    @TableField("file_type")
    private String fileType;

    @ApiModelProperty(value = "文件总大小")
    @TableField("total_size")
    private String totalSize;

    @ApiModelProperty(value = "md5校验码	")
    @TableField("md5")
    private String md5;

    @TableField("created_by")
    private String createdBy;

    @TableField("update_by")
    private String updateBy;

    @TableField("created_time")
    private Date createdTime;

    @TableField("update_time")
    private Date updateTime;

    @ApiModelProperty(value = "文件时长(秒)(页数)")
    @TableField("material_duration")
    private Integer materialDuration;

    @ApiModelProperty(value = "文件缩略图(base64)")
    @TableField("upload_ids")
    private String uploadIds;


}
