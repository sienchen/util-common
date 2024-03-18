<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.Mapper}.${table.mapperName}">
    <#if enableCache>
        <!-- 开启二级缓存 -->
        <cache type="org.mybatis.caches.ehcache.LoggingEhcache"/>

    </#if>
    <#if baseResultMap>
        <!-- 通用查询映射结果 -->
    <resultMap id="BaseResultMap" type="${package.Entity}.${entity}">
        <#list table.fields as field>
            <#if field.keyFlag><#--生成主键排在第一位-->
                <id column="${field.name}" property="${field.propertyName}" />
            </#if>
        </#list>
        <#list table.commonFields as field><#--生成公共字段 -->
            <result column="${field.name}" property="${field.propertyName}" />
        </#list>
        <#list table.fields as field>
            <#if !field.keyFlag><#--生成普通字段 -->
                <result column="${field.name}" property="${field.propertyName}" />
            </#if>
        </#list>
    </resultMap>
    </#if>
    <!-- 通用查询列 -->
    <sql id="Base_Column_List">
        <#list table.commonFields as field>
            ${field.name},
        </#list>
        ${table.fieldNames}
    </sql>
     <select id="getList" resultType="${package.Entity}.${entity}">
            select t1.* from ${table.name} t1
            <where>
                <if test="startTime != null and startTime !='' and endTime != null and endTime !='' ">
                    <bind name="startTime" value="startTime + ' 00:00:00'"/>
                    <bind name="endTime" value="endTime + ' 23:59:59'"/>
                    and t1.up_time >= to_timestamp ( ${r"#{"}startTime${r"}"}, 'yyyy-MM-dd hh24:mi:ss' )
                    and t1.up_time &lt;= to_timestamp (${r"#{"}endTime${r"}"},'yyyy-MM-dd hh24:mi:ss' )
                </if>
                <if test="name!=null and name!=''">
                    and t1.name like concat('%',{name},'%')
                </if>
                <if test="ids!=null and ids.size()>0">
                                         and t1.id in
                                         <foreach collection="ids" index="index" item="item" open="(" separator="," close=")">
                                             ${r"#{"}item${r"}"}
                                         </foreach>
                                     </if>
            </where>
            order by t1.update_time desc
        </select>

         <insert id="add" parameterType="${package.Entity}.${entity}">
                   INSERT INTO ${table.name}
                 <trim prefix="(" suffix=")" suffixOverrides=",">
                     <#list table.fields as field>
                            <#if !field.keyFlag><#--生成普通字段 -->
                                <if test="${field.propertyName}!= null and ${field.propertyName}!= 'null' and ${field.propertyName}!= ''">${field.name},</if>
                            </#if>
                     </#list>
                </trim>
                VALUES
                <trim prefix="(" suffix=")" suffixOverrides=",">
                     <#list table.fields as field>
                           <#if !field.keyFlag><#--生成普通字段 -->
                               <if test="${field.propertyName}!= null and ${field.propertyName}!= 'null' and ${field.propertyName}!= ''">${r"#{"}${field.propertyName}${r"}"},</if>
                            </#if>
                     </#list>
                </trim>
        </insert>
</mapper>
