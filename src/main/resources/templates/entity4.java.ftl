package ${package.Entity};
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * ${table.comment!} Entity 实体类
 * </p>
 * @author ${author}
 * @since ${date}
 */
<#if entityLombokModel>
@Data
<#if superEntityClass??>
@EqualsAndHashCode(callSuper = true)
<#else>
@EqualsAndHashCode(callSuper = false)
</#if>
</#if>
<#if table.convert>
@TableName(value = "${table.name}",autoResultMap = true)
</#if>
<#if swagger2>
@ApiModel(value="${entity}", description="${table.comment!}")
</#if>
public class ${entity} implements Serializable {

    private static final long serialVersionUID = 1L;
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
<#if field.keyFlag>
<#assign keyPropertyName="${field.propertyName}"/>
</#if>
<#if field.comment!?length gt 0>
<#if "id" != field.propertyName && "createBy" != field.propertyName && "createTime" != field.propertyName && "updateBy" != field.propertyName && "updateTime" != field.propertyName>
<#if "Date" == field.propertyType>
   @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
<#else>
</#if>
</#if>
</#if>
<#if field.comment!?length gt 0>
<#if swagger2>
    @ApiModelProperty(value = "${field.comment}")
<#else>
/**
 * ${field.comment}
 */
</#if>
</#if>
    private ${field.propertyType} ${field.propertyName};
</#list>
<#------------  END 字段循环遍历  ---------->

<#if !entityLombokModel>
<#list table.fields as field>
<#if field.propertyType == "boolean">
<#assign getprefix="is"/>
<#else>
<#assign getprefix="get"/>
</#if>
public ${field.propertyType} ${getprefix}${field.capitalName}() {
        return ${field.propertyName};
        }

<#if chainModel>
public ${entity} set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
<#else>
public void set${field.capitalName}(${field.propertyType} ${field.propertyName}) {
</#if>
        this.${field.propertyName} = ${field.propertyName};
<#if chainModel>
        return this;
</#if>
        }
</#list>
</#if>

<#if entityColumnConstant>
<#list table.fields as field>
public static final String ${field.name?upper_case} = "${field.name}";

</#list>
</#if>
<#if activeRecord>
@Override
protected Serializable pkVal() {
<#if keyPropertyName??>
        return this.${keyPropertyName};
<#else>
        return null;
</#if>
        }

</#if>
<#if !entityLombokModel>
@Override
public String toString() {
        return "${entity}{" +
<#list table.fields as field>
<#if field_index==0>
        "${field.propertyName}=" + ${field.propertyName} +
<#else>
        ", ${field.propertyName}=" + ${field.propertyName} +
</#if>
</#list>
        "}";
        }
</#if>
}
