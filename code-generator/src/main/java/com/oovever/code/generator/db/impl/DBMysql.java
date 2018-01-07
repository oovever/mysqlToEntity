package com.oovever.code.generator.db.impl;

import com.oovever.code.generator.Main;
import com.oovever.code.generator.db.IDatabase;
import com.oovever.code.generator.pojo.Field;
import com.oovever.code.generator.pojo.TableClass;
import com.oovever.code.generator.util.DBUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author OovEver
 * 2018/1/6 22:25
 */
public class DBMysql implements IDatabase {
    /** 查询所有表信息 */
    private static final String SHOW_TABLES_SQL = "SHOW TABLE STATUS";
    /** 查询表的字段信息 */
    private static final String SHOW_FULL_COLUMNS = "SHOW FULL COLUMNS FROM ";
    private static Map<String, String> mysqlTypeToJavaType = new HashMap<String, String>();
    static {
        mysqlTypeToJavaType.put("int", "Integer");
        mysqlTypeToJavaType.put("tinyint", "Integer");
        mysqlTypeToJavaType.put("decimal", "Double");
        mysqlTypeToJavaType.put("varchar", "String");
        mysqlTypeToJavaType.put("text", "String");
        mysqlTypeToJavaType.put("bigint", "Long");
        mysqlTypeToJavaType.put("datetime", "Date");
        mysqlTypeToJavaType.put("date", "Date");
        mysqlTypeToJavaType.put("timestamp", "Date");
    }

    private DBUtil dbUtil = new DBUtil();
    /**
     * 获取所有表名
     * @return 返回所有表名
     */
    @Override
    public List<TableClass> getTables() {
        DBUtil dbUtil = new DBUtil();
        List<TableClass> tableClassesList = new ArrayList<TableClass>();
        TableClass tableClass = null;
        ResultSet rs = null;
        try {
            rs = dbUtil.query(SHOW_TABLES_SQL, null);
            while (rs.next()) {
                tableClass = new TableClass();
//                表名
                tableClass.setTableName(rs.getString("Name"));
//                注释
                tableClass.setComment(rs.getString("Comment"));
               //类名称
                tableClass.setClassName(parseSuffixClassname(rs.getString("Name")));
                tableClassesList.add(tableClass);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                dbUtil.close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return tableClassesList;
    }
    /**
     * 获取每个表的所有列并填充到表名
     * @param list 列表列
     */
    @Override
    public void addColumnsToTable(List<TableClass> list) {
        ResultSet rs = null;
        try {
            for (TableClass tableClass : list) {
                List<Field> fields = new ArrayList<Field>();
                rs = dbUtil.query(SHOW_FULL_COLUMNS + tableClass.getClassName(), null);
                Field field = null;
                while (rs.next()) {
                    field = new Field();
                    field.setColumnName(rs.getString("Field"));
                    field.setColumnType(rs.getString("Type"));
                    field.setComment(rs.getString("Comment"));
                    /** java字段名称 */
                    field.setFieldName(columnToJavaCamelStyle(rs.getString("Field")));
                    //java字段类型
                    field.setFieldType(getJavaTypeByMysqlType(rs.getString("Type"), null));
                    fields.add(field);
                }
                for (Field f : fields) {
                    if ("Date".equals(f.getFieldType())) {
                        /** 是否有日期字段，需要引入java.util */
                        tableClass.setHasDateType(true);
                        break;
                    }
                }
                /** 所有的列 */
                tableClass.setFieldList(fields);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                dbUtil.close();
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 表名或字段名转成驼峰型，并去掉下划线_
     * 注意：表名会首字母大写，字段名不会
     * 如  表名 USER_INFO  -->  UserInfo
     * 字段名 create_time --> createTime
     *
     * @param columnName 列名称
     * @return 返回转化后的表名或者字段名
     */
    public static String columnToJavaCamelStyle(String columnName) {
        StringBuilder camelString = new StringBuilder(30);
        String[] split = columnName.toLowerCase().split("_");
        String str = null;
        for (int i = 0; i < split.length; i++) {
            str = split[i];
            if (i > 0) {
                str = capitaliseFirst(str);
            }
            camelString.append(str);
        }

        return camelString.toString();

    }
    /***
     * 将数据库表名称以驼峰命名法转化为数据库类名
     * @param tableName 表名称
     * @return 返回转化后的类名称
     */
    private String parseSuffixClassname(String tableName) {
        StringBuilder camelString = new StringBuilder(30);
        String[] split = tableName.toLowerCase().split("_");
        String s = null;
        if (split.length > 1) {
            for (int i = 1; i < split.length; i++) {
                s = split[i];
                if (i > 0) {
                    s = capitaliseFirst(s);
                }
                camelString.append(s);
            }
        } else {
            camelString.append(capitaliseFirst(split[0]));
        }
        return camelString.toString();
    }
    /**
     * 将mysql类型转化为java类型
     * @param mysqlType mysql类型
     * @param dataScale dataScale中若是有数据就一定会是数字，若此时dataScale大于0就说明要返回Double类型，其它的就是简单的类型
     * @return 返回转化后的java类型
     */
    private String getJavaTypeByMysqlType(String mysqlType, String dataScale) {
        if (null == mysqlType || "".equals(mysqlType)) {
            return mysqlType;
        }
        if (dataScale == null || "".equals(dataScale)) {
            if (mysqlType.contains("(")) {
                mysqlType = mysqlType.substring(0, mysqlType.indexOf("("));
            }
            return mysqlTypeToJavaType.get(mysqlType);
        }
        if (mysqlType.equalsIgnoreCase("NUMBER") && Integer.parseInt(dataScale) > 0) {
            return "Double";
        }
        return mysqlTypeToJavaType.get(mysqlType);
    }
    /***
     *
     * 大写一个字符串的第一个字符
     *
     * @param str 要转化的字符
     * @return 返回转化的大写字符结果
     */
    public final static String capitaliseFirst(final String str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return str;
        }
        StringBuilder cap = new StringBuilder(str.substring(0, 1).toUpperCase());
        if (str.length() > 1) {
            cap.append(str.substring(1));
        }
        return cap.toString();
    }
}
