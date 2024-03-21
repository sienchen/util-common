package com.tongtu.cyber.util.upload.minio.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 文件分块记录
 * </p>
 *
 * @author 陈世恩
 * @since 2024-03-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("file_chunk")
@ApiModel(value = "FileChunk对象", description = "文件分块记录")
public class FileChunk implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId("id")
    private String id;
    @ApiModelProperty(value = "上传文件名")
    @TableField("file_name")
    private String fileName;

    @ApiModelProperty(value = "当前分片下标")
    @TableField("current_chunk_index")
    private Integer currentChunkIndex;

    @ApiModelProperty(value = "当前分片大小")
    @TableField("current_chunk_size")
    private String currentChunkSize;

    @ApiModelProperty(value = "文件总大小")
    @TableField("total_size")
    private Double totalSize;

    @ApiModelProperty(value = "文件总分片数")
    @TableField("total_chunk")
    private Integer totalChunk;
    @ApiModelProperty(value = "文件类型")
    @TableField("file_type")
    private String fileType;

    @ApiModelProperty(value = "文件相对路径")
    @TableField("file_path")
    private String filePath;

    @ApiModelProperty(value = "文件唯一md5标识")
    @TableField("md5")
    private String md5;

    @TableField("created_by")
    private String createdBy;

    @TableField("update_by")
    private String updateBy;

    @TableField("update_time")
    private Date updateTime;

    @TableField("created_time")
    private Date createdTime;

    @ApiModelProperty(value = "文件每片大小")
    @TableField("chunk_size")
    private Double chunkSize;

    @ApiModelProperty(value = "是否上传完成0未完成 1已完成")
    @TableField("complete_flag")
    private Integer completeFlag;
    @TableField(exist = false)
    private MultipartFile file;

}
