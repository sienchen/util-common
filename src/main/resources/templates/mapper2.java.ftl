package ${package.Mapper};

import ${package.Entity}.${entity};
import ${package.Entity}.dto.${entity}Dto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
/**
 * @description: ${table.comment!} Mapper 接口
 * @author: ${author}
 * @since: ${date}
 **/
<#if kotlin>
interface ${table.mapperName} : ${superMapperClass}<${entity}>
<#else>
public interface ${table.mapperName} extends ${superMapperClass}<${entity}> {

    IPage<${entity}> getList(@Param("page") Page<${entity}> page, @Param("param") ${entity}Dto param);

    List<${entity}> getList(@Param("param") ${entity}Dto param);
}
</#if>
