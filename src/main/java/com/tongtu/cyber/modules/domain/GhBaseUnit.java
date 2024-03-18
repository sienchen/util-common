package com.tongtu.cyber.modules.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 港航段-基础信息-站信息管理 Entity 实体类
 * </p>
 *
 * @author 陈世恩
 * @since 2024-03-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "gh_base_unit", autoResultMap = true)
@ApiModel(value = "GhBaseUnit", description = "港航段-基础信息-站信息管理")
public class GhBaseUnit implements Serializable {

    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "创建人")
    @TableField("create_by")
    private String createBy;
    @ApiModelProperty(value = "创建时间")
    @TableField("create_time")
    private Date createTime;
    @ApiModelProperty(value = "更新人")
    @TableField("update_by")
    private String updateBy;
    @ApiModelProperty(value = "更新时间")
    @TableField("update_time")
    private Date updateTime;
    @ApiModelProperty(value = "主键id")
    @TableField("id")
    private String id;
    @Excel(name = "站名称", width = 15)
    @ApiModelProperty(value = "站名称")
    @TableField("name")
    private String name;
    @Excel(name = "站编号", width = 15)
    @ApiModelProperty(value = "站编号")
    @TableField("code")
    private String code;
    @Excel(name = "航道等级(字典unit_level)", width = 15)
    @ApiModelProperty(value = "航道等级(字典unit_level)")
    @TableField("level")
    private String level;
    @Excel(name = "站起点(保存节点id)", width = 15)
    @ApiModelProperty(value = "站起点(保存节点id)")
    @TableField("start_node_id")
    private String startNodeId;
    @Excel(name = "站讫点(保存节点id)", width = 15)
    @ApiModelProperty(value = "站讫点(保存节点id)")
    @TableField("end_node_id")
    private String endNodeId;
    @Excel(name = "站长(km)", width = 15)
    @ApiModelProperty(value = "站长(km)")
    @TableField("length")
    private BigDecimal length;
    @Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    @TableField("sort")
    private Integer sort;
    @Excel(name = "站起点简称", width = 15)
    @ApiModelProperty(value = "站起点简称")
    @TableField("start_name")
    private String startName;
    @Excel(name = "站讫点简称", width = 15)
    @ApiModelProperty(value = "站讫点简称")
    @TableField("end_name")
    private String endName;
    @Excel(name = "备注信息", width = 15)
    @ApiModelProperty(value = "备注信息")
    @TableField("remark")
    private String remark;
    @Excel(name = "关联水道(id逗号分隔)", width = 15)
    @ApiModelProperty(value = "关联水道(id逗号分隔)")
    @TableField("water_ids")
    private String waterIds;
    @Excel(name = "关联水情流量名称", width = 15)
    @ApiModelProperty(value = "关联水情流量名称")
    @TableField("water_name")
    private String waterName;
    @Excel(name = "站别名", width = 15)
    @ApiModelProperty(value = "站别名")
    @TableField("bak_name")
    private String bakName;


}
