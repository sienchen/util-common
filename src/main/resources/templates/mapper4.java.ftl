package ${package.Mapper};

import ${package.Entity}.${entity};
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
/**
 * @description: ${table.comment!} Mapper 接口
 * @author: ${author}
 * @since: ${date}
 **/
<#if kotlin>
interface ${table.mapperName} : ${superMapperClass}<${entity}>
<#else>
 @Component
 public interface ${table.mapperName} extends ${superMapperClass}<${entity}> {

}
</#if>
