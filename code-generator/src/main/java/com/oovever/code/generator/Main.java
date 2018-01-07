package com.oovever.code.generator;

import com.oovever.code.generator.db.IDatabase;
import com.oovever.code.generator.db.impl.DBMysql;
import com.oovever.code.generator.pojo.Global;
import com.oovever.code.generator.pojo.TableClass;
import com.oovever.code.generator.util.PropertiesUtil;
import org.apache.commons.io.FileUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author OovEver
 * 2018/1/6 21:32
 */
public class Main {
    public static void main(String[] args) throws Exception {
//        初始化全局参数
        Global global = Main.prepareGlobalVariables();
        Main.createDir(global);
        Main.process(global);
        System.out.println("执行完成");
    }

    /**
     * 准备全局参数
     * @return
     */
    private static Global prepareGlobalVariables() {
        Global global = new Global();
        PropertiesUtil propertiesUtil = new PropertiesUtil("me.properties");
        global.setTargetPath(propertiesUtil.getProperty("targetPath"));
        global.setBasePackage(propertiesUtil.getProperty("basic"));
        global.setPo(propertiesUtil.getProperty("po"));
        global.setAuthor(propertiesUtil.getProperty("author"));
        return global;
    }
    /**
     * 创建文件夹
     *
     * @param global 全局参数
     */
    private static void createDir(Global global) {
        String srcPath = global.getTargetPath().replace("\\", "/");
        File file = new File(srcPath);
        try {
            FileUtils.deleteDirectory(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (null != file && !file.exists()) {
            file.mkdirs();
        }
        List<String> dirList = new ArrayList<String>();
        String srcBasePackage = srcPath + "/" + global.getBasePackage();
        dirList.add(srcBasePackage);
        dirList.add(srcBasePackage + "/" + global.getPo());
        for (String dir : dirList) {
            File codePackage = new File(dir.replace(".", "/"));
            if(null != codePackage && ! codePackage.exists()) {
                codePackage.mkdirs();
            }
        }


    }
    /**
     * 程序骨干，生成文件
     * 1. 获取数据库表信息，字段信息
     * 2. 将表名及字段名转成java的驼峰样式 将数据库中表字段的类型转换为相应的java类型
     * 3. 根据每个表生成实体文件
     *
     * @param global 全局参数
     * @throws Exception
     */
    private static void process(Global global) throws Exception {
        IDatabase database = new DBMysql();
//       获取所有的数据表
        List<TableClass> tables = database.getTables();
        // 如下方法做了如下工作：
        //  0. 获取每个表的每个字段的名字，类型和注释
        //  1. 将数据库中表字段的类型转换为相应的java类型
        //  2. 将表名和字段名改成相应的驼峰样式
        database.addColumnsToTable(tables);
        Configuration cfg = getConfiguration();
        for (TableClass tableClass : tables) {
            process(global, cfg, tableClass, "Po");
        }
    }
    /**
     * 获取Freemarker配置
     *
     * @return 返回Freemarker配置
     */
    private static Configuration getConfiguration() {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_21);
        try {
//           通过文件路径加载freemarker模板
//            完成普通字符串 和 application/x-www-form-urlencoded MIM
//            .class.getClassLoader()是为了获得一个类加载器，用来加载classpath下的.class文件而已。getResource返回的是一个URL对象，不要简单的认为是这个资源的绝对地址，这是一个Java中封装的对象。
            cfg.setDirectoryForTemplateLoading(new File(URLDecoder.decode(Main.class.getClassLoader().getResource("").getPath() + "freemarker/", System.getProperty("file.encoding"))));
        } catch (IOException e) {
            System.out.println("文件不存在");
            e.printStackTrace();
        }
        cfg.setDefaultEncoding("UTF-8");
//        如果方法抛出异常，那么模板的执行就会中止，而且Template.process() 方法也会抛出同样的异常。如果 handleTemplateException 对象不抛出异常，那么模板将会继续执行，就好像什么也没发生过一样，但是引发异常的语句将会被跳过。
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        return cfg;
    }
    /**
     * 根据type生成Controller、Service、DAO、mapper
     *
     * @param global 全局数据
     * @param cfg    Freemarker配置
     * @param tableClass     表/类信息
     * @param freemarkerName   freemarkerName模板名称
     * @throws Exception 抛出的异常
     */
    private static void process(
            Global global, Configuration cfg, TableClass tableClass, String freemarkerName)
            throws Exception {
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("pojo", tableClass);
        root.put("global", global);
        String fileExt=".java";
        String filename = tableClass.getClassName();

        Template template = cfg.getTemplate(freemarkerName.toLowerCase() + ".ftl");
        String methodName = "get" + freemarkerName;
//        获得对象所声明的公开方法
        Method getXPkg = global.getClass().getMethod(methodName);
        File file = new File(global.getTargetPath() + "/"
                + global.getBasePackage().replace(".", "/") + "/"
                + getXPkg.invoke(global).toString().replace(".", "/") + "/"
                + filename + fileExt);
        FileWriter    fileWriter      = new FileWriter(file);
        BufferedWriter bufferedWritter = new BufferedWriter(fileWriter);
        template.process(root, bufferedWritter);
        bufferedWritter.flush();
        bufferedWritter.close();
        fileWriter.close();

    }
}
