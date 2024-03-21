package com.tongtu.cyber.util.generatorcode;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.LikeTable;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * MyBatisPlus自动代码生成器
 */
public class MyBatisPlusGeneratorUtil {

    public static void main(String[] args) {
        //输入生成包名和配置名称
        String projectPath = System.getProperty("user.dir") + File.separator;
        String moduleName = scanner("请输入生成后包名（默认当前路径下）");
        String[] tableNames = scanner("表名，多个英文逗号分割").split(",");
        AutoGenerator autoGenerator = new AutoGenerator();
        //全局配置
        autoGenerator.setGlobalConfig(initGlobalConfig(projectPath));
        //数据源配置
        autoGenerator.setDataSource(initDataSourceConfig());
        //包名配置
        autoGenerator.setPackageInfo(initPackageConfig(moduleName));
        //自定义模板配置
        autoGenerator.setCfg(initInjectionConfig(projectPath, moduleName));
        //模板配置
        autoGenerator.setTemplate(initTemplateConfig());
        //初始化策略配置
        autoGenerator.setStrategy(initStrategyConfig(tableNames));
        //设置模板引擎
        // autoGenerator.setTemplateEngine(new VelocityTemplateEngine());
        autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());
        autoGenerator.execute();
    }

    /**
     * 数据源配置
     */
    private static DataSourceConfig initDataSourceConfig() {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        //Postgresql 链接数据库
        dataSourceConfig.setUrl("jdbc:postgresql://10.253.129.6:5432/xytocc_gh?reWriteBatchedInserts=true&ApplicationName=vehicle&allowMultiQueries=true");
        //dataSourceConfig.setUrl("jdbc:postgresql://10.253.129.6:5432/xytocc_business?currentSchema=transport&reWriteBatchedInserts=true&ApplicationName=vehicle&allowMultiQueries=true");
        dataSourceConfig.setDriverName("org.postgresql.Driver");
        dataSourceConfig.setUsername("postgres");
        dataSourceConfig.setPassword("ttyjpostgres");
        //Mysql 链接数据库
        /* dataSourceConfig.setUrl("jdbc:mysql://10.253.129.6:3306/activity?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=Asia/Shanghai");
        dataSourceConfig.setDriverName("com.mysql.cj.jdbc.Driver");
        dataSourceConfig.setUsername("tocc");
        dataSourceConfig.setPassword("admin123ttyj7890uiop"); */
        return dataSourceConfig;
    }

    /**
     * 模板配置
     */
    private static TemplateConfig initTemplateConfig() {
        TemplateConfig templateConfig = new TemplateConfig();
        //使用jecgBootEasyPOI导出
       /*  templateConfig.setEntity("templates/entity2.java");
        templateConfig.setMapper("templates/mapper2.java");
        templateConfig.setController("templates/controller2.java");
        templateConfig.setService("templates/service2.java");
        templateConfig.setServiceImpl("templates/serviceImpl2.java"); */
        //使用easypoi导出
       /*  templateConfig.setEntity("templates/entity3.java");
        templateConfig.setMapper("templates/mapper3.java");
        templateConfig.setController("templates/controller3.java");
        templateConfig.setService("templates/service3.java");
        templateConfig.setServiceImpl("templates/serviceImpl3.java"); */
        //精简版
       /*  templateConfig.setEntity("templates/entity4.java");
        templateConfig.setMapper("templates/mapper4.java");
        templateConfig.setController("templates/controller4.java");
        templateConfig.setService("templates/service4.java");
        templateConfig.setServiceImpl("templates/serviceImpl4.java"); */
        //mapper.xml模板需单独配置
        templateConfig.setXml(null);
        return templateConfig;
    }

    /**
     * 默认生成包名配置
     */
    private static PackageConfig initPackageConfig(String moduleName) {
        PackageConfig packageConfig = new PackageConfig();
        packageConfig.setModuleName(moduleName);
        packageConfig.setParent("com.tongtu.cyber");
        packageConfig.setEntity("domain");
        return packageConfig;
    }


    /**
     * 初始化全局配置
     */
    private static GlobalConfig initGlobalConfig(String projectPath) {
        GlobalConfig globalConfig = new GlobalConfig();
        globalConfig.setOutputDir(projectPath + "/src/main/java");
        globalConfig.setAuthor("陈世恩");
        globalConfig.setOpen(false);
        globalConfig.setSwagger2(true);
        globalConfig.setBaseResultMap(true);
        globalConfig.setFileOverride(true);
        globalConfig.setDateType(DateType.ONLY_DATE);
        globalConfig.setEntityName("%s");
        globalConfig.setMapperName("%sMapper");
        globalConfig.setXmlName("%sMapper");
        globalConfig.setServiceName("%sService");
        globalConfig.setServiceImplName("%sServiceImpl");
        globalConfig.setControllerName("%sController");
        return globalConfig;
    }


    /**
     * 生成策略配置
     */
    private static StrategyConfig initStrategyConfig(String[] tableNames) {
        StrategyConfig strategyConfig = new StrategyConfig();
        //数据库表映射到实体的命名策略
        strategyConfig.setNaming(NamingStrategy.underline_to_camel);
        //数据库表字段映射到实体的命名策略, 未指定按照 naming 执行
        strategyConfig.setColumnNaming(NamingStrategy.underline_to_camel);
        //为实体类添加公共基类
        // strategyConfig.setSuperEntityClass("com.wt.demo01.BaseEntity");
        //实体类采用lombok的形式
        strategyConfig.setEntityLombokModel(true);
        //设置controller为restcontroller
        strategyConfig.setRestControllerStyle(true);
        //【实体】是否生成字段常量（默认 false）
        strategyConfig.setEntityColumnConstant(false);
        //是否生成实体时，生成字段配置 即在字段属性上加上@TableField("")注解
        strategyConfig.setEntityTableFieldAnnotationEnable(true);
        // controller的公共父类
//        strategyConfig.setSuperControllerClass("com.wt.demo01.BaseController");
        // 写于父类中的公共字段
//        strategyConfig.setSuperEntityColumns("id");
        //驼峰转连字符
        strategyConfig.setControllerMappingHyphenStyle(true);
        //当表名中带*号时可以启用通配符模式
        if (tableNames.length == 1 && tableNames[0].contains("*")) {
            String[] likeStr = tableNames[0].split("_");
            String likePrefix = likeStr[0] + "_";
            strategyConfig.setLikeTable(new LikeTable(likePrefix));
        } else {
            strategyConfig.setInclude(tableNames);
        }
        return strategyConfig;
    }

    /**
     * 自定义模板配置
     */
    private static InjectionConfig initInjectionConfig(String projectPath, String moduleName) {
        // 自定义配置
        InjectionConfig injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {
                // 可用于自定义属性
            }
        };
        // 如果模板引擎是 freemarker
        String templatePath = "/templates/mapper2.xml.ftl";
        //自定义生成某个固定类
        String templatePath2 = "/templates/entityDto2.java.ftl";
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                //第一种生成xml路径方式
               /* String path = projectPath + "/src/main/resources/mapper/" + moduleName
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML; */
                //第二种生成xml路径方式
                String path2 = projectPath + "/src/main/java/com/tongtu/cyber/" + moduleName
                        + "/mapper/xml/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
                return path2;
            }
        });
        //添加定制模板生成
        focList.add(new FileOutConfig(templatePath2) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                String path2 = projectPath + "/src/main/java/com/tongtu/cyber/" + moduleName
                        + "/domain/dto/" + tableInfo.getEntityName() + "Dto" + StringPool.DOT_JAVA;
                return path2;
            }
        });
        injectionConfig.setFileOutConfigList(focList);
        return injectionConfig;
    }
    /**
     * 读取控制台内容信息
     */
    private static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(("请输入" + tip + "："));
        if (scanner.hasNext()) {
            String next = scanner.next();
            if (StrUtil.isNotEmpty(next)) {
                return next;
            }
        }
        throw new MybatisPlusException("请输入正确的" + tip + "！");
    }
}