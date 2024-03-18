<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${table.mapperName}">
    <#if enableCache>
        <!-- 开启二级缓存 -->
        <cache type="org.mybatis.caches.ehcache.LoggingEhcache"/>
    </#if>
       <!-- 通用查询列 -->
    <sql id="Base_Column_List">
            <#list table.commonFields as field>
                t1.${field.name},
            </#list>${table.fieldNames}
    </sql>
     <select id="getList" resultType="${package.Entity}.${entity}">
            select <include refid="Base_Column_List"/>  from ${table.name}
            <where>
                <if test="param.name!= null and param.name!= ''">
                    and name like concat('%',${r"#{"}param.name${r"}"},'%')
                </if>
                <if test="param.ids!=null and param.ids.size()>0">
                    and id in
                    <foreach collection="param.ids" index="index" item="item" open="(" separator="," close=")">
                        ${r"#{"}item${r"}"}
                    </foreach>
                </if>
            </where>
            order by update_time desc
     </select>


</mapper>
