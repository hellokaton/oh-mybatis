package io.github.biezhi.onmybatis.controller;

import com.blade.Blade;
import com.blade.kit.StringKit;
import com.blade.kit.UUID;
import com.blade.mvc.annotation.Controller;
import com.blade.mvc.annotation.JSON;
import com.blade.mvc.annotation.QueryParam;
import com.blade.mvc.annotation.Route;
import com.blade.mvc.http.HttpMethod;
import com.blade.mvc.http.Request;
import com.blade.mvc.view.RestResponse;
import io.github.biezhi.onmybatis.constant.DbName;
import io.github.biezhi.onmybatis.model.GeneratorParam;
import io.github.biezhi.onmybatis.utils.GeneratorUtils;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.*;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Controller
public class IndexController {

    public static final Logger LOGGER = LoggerFactory.getLogger(IndexController.class);

    @Route(value = {"/", "/index"})
    public String index() {
        return "index.html";
    }

    @Route(value = "gen", method = HttpMethod.POST)
    @JSON
    public RestResponse gen(Request request, @QueryParam String tableItems, @QueryParam String modelNames) {

        GeneratorParam param = request.model("p", GeneratorParam.class);

        if (StringKit.isNotBlank(tableItems) && StringKit.isNotBlank(modelNames)) {
            param.setTableNames(StringKit.split(tableItems, ","));
            param.setModelNames(StringKit.split(modelNames, ","));
        }

        List<String> warnings = new ArrayList<>();
        // 覆盖已有的重名文件
        boolean overwrite = true;
        // 准备 配置文件
        String srcDirName = UUID.UU32();
        String webRoot = Blade.$().webRoot();
        param.setBuildPath(webRoot + "/" + srcDirName);
        String config_path = "/mybatis-conf.xml";
        File configFile = new File(webRoot + config_path);
        try {
            // 1.创建 配置解析器
            ConfigurationParser parser = new ConfigurationParser(warnings);
            // 2.获取 配置信息
            Configuration config = parser.parseConfiguration(configFile);

            // 应用配置信息
            applyConfig(config, param);

            // 3.创建 默认命令解释调回器
            DefaultShellCallback callback = new DefaultShellCallback(overwrite);
            // 4.创建 mybatis的生成器
            MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
            // 5.执行，关闭生成器
            try {
                myBatisGenerator.generate(null);
            } catch (Exception e) {
                LOGGER.error("", e);
                return RestResponse.fail(e.getMessage());
            }

            GeneratorUtils.zipFodler(param.getBuildPath(), webRoot + "/static/temp/" + srcDirName + ".zip");
            String url = "/static/temp/" + srcDirName + ".zip";
            Executors.newFixedThreadPool(1).submit(() -> {
                try {
                    TimeUnit.SECONDS.sleep(3);
                    GeneratorUtils.deleteDir(new File(webRoot + "/" + srcDirName));
                } catch (Exception e) {
                    LOGGER.error("异步删除失败", e);
                }
            });
            return RestResponse.ok(url);
        } catch (Exception e) {
            LOGGER.error("", e);
            return RestResponse.fail(e.getMessage());
        }
    }

    void applyConfig(Configuration config, GeneratorParam param) {
        File dirFile = new File(param.getBuildPath());
        if (!dirFile.exists()) {
            dirFile.mkdirs();
            new File(param.getBuildPath() + "/src/main/java").mkdirs();
            new File(param.getBuildPath() + "/src/main/resources").mkdirs();
        }
        String connection = null;
        String driverClass = null;
        Context context = config.getContexts().get(0);
        if (param.getDbType().equals(DbName.MYSQL.getName())) {
            driverClass = "com.mysql.jdbc.Driver";
            connection = "jdbc:mysql://" + param.getConnection() + ":" + param.getPort() + "/" + param.getDataBase();
        }
        if (param.getDbType().equals(DbName.POSTGRESQL.getName())) {
            driverClass = "org.postgresql.Driver";
            connection = "jdbc:postgresql://" + param.getConnection() + ":" + param.getPort() + "/" + param.getDataBase();
        }
        if (param.getDbType().equals(DbName.ORACLE.getName())) {
            driverClass = "oracle.jdbc.OracleDriver";
            connection = "jdbc:oracle:thin:@" + param.getConnection() + ":" + param.getPort() + ":" + param.getDataBase();
        }

        // 注释
        CommentGeneratorConfiguration cgc = context.getCommentGeneratorConfiguration();
        cgc.setConfigurationType("io.github.biezhi.onmybatis.utils.QnloftCommentGenerator");

        // 配置数据库属性
        JDBCConnectionConfiguration jdbcConnectionConfiguration = context.getJdbcConnectionConfiguration();
        jdbcConnectionConfiguration.setDriverClass(driverClass);
        jdbcConnectionConfiguration.setConnectionURL(connection);
        jdbcConnectionConfiguration.setUserId(param.getUserId());
        jdbcConnectionConfiguration.setPassword(param.getUserPass());

        //配置模型的包名
        JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = context.getJavaModelGeneratorConfiguration();
        javaModelGeneratorConfiguration.setTargetPackage(param.getModelPath());
        javaModelGeneratorConfiguration.setTargetProject(param.getBuildPath() + "/src/main/java");

        //mapper的包名
        JavaClientGeneratorConfiguration javaClientGeneratorConfiguration = context.getJavaClientGeneratorConfiguration();
        javaClientGeneratorConfiguration.setTargetPackage(param.getMapperPath());
        javaClientGeneratorConfiguration.setTargetProject(param.getBuildPath() + "/src/main/java");

        //映射文件的包名
        SqlMapGeneratorConfiguration sqlMapGeneratorConfiguration = context.getSqlMapGeneratorConfiguration();
        sqlMapGeneratorConfiguration.setTargetPackage(param.getMappingPath());
        sqlMapGeneratorConfiguration.setTargetProject(param.getBuildPath() + "/src/main/resources");

        TableConfiguration tc = new TableConfiguration(context);
        tc.setTableName("%");
        tc.setAllColumnDelimitingEnabled(true);

        // 插件
        PluginConfiguration sp = new PluginConfiguration();
        sp.setConfigurationType("org.mybatis.generator.plugins.SerializablePlugin");
        context.addPluginConfiguration(sp);

        PluginConfiguration abp = new PluginConfiguration();
        abp.setConfigurationType("io.github.biezhi.onmybatis.plugins.AddAliasToBaseColumnListPlugin");
        context.addPluginConfiguration(abp);

        PluginConfiguration pcf = new PluginConfiguration();
        pcf.setConfigurationType("io.github.biezhi.onmybatis.plugins.MapperPlugin");

        if (StringKit.isNotBlank(param.getMapperPlugin())) {
            pcf.addProperty("mappers", "com.kongzhong.base.BaseMapper");
        } else {
            tc.setSelectByExampleStatementEnabled(true);
            tc.setDeleteByPrimaryKeyStatementEnabled(true);
            tc.setUpdateByExampleStatementEnabled(true);
            tc.setCountByExampleStatementEnabled(true);
        }

        context.addPluginConfiguration(pcf);
        context.getTableConfigurations().clear();
        context.getTableConfigurations().add(tc);

        if ((param.getTableNames() != null) && (0 < param.getTableNames().length)) {
            //表集合
            List<TableConfiguration> tableConfigurations = context.getTableConfigurations();
            tableConfigurations.clear();
            for (int i = 0; i < param.getTableNames().length; i++) {
                if (StringKit.isNotBlank(param.getTableNames()[i]) && StringKit.isNotBlank(param.getModelNames()[i])) {
                    TableConfiguration tableConfiguration = new TableConfiguration(context);
                    tableConfiguration.setTableName(param.getTableNames()[i]);
                    tableConfiguration.setDomainObjectName(param.getModelNames()[i]);
                    tableConfiguration.setCountByExampleStatementEnabled(true);
                    tableConfiguration.setDeleteByExampleStatementEnabled(true);
                    tableConfiguration.setSelectByExampleStatementEnabled(true);
                    tableConfiguration.setUpdateByExampleStatementEnabled(true);
                    //模型是否驼峰命名，为0则为驼峰
                    if (param.getIsHump().equals("0"))
                        tableConfiguration.getProperties().setProperty("useActualColumnNames", "true");
                    tableConfigurations.add(tableConfiguration);
                }
            }
        }
    }

}