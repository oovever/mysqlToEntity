package com.oovever.code.generator.pojo;

import lombok.Data;

import java.util.List;

/**
 * @author OovEver
 * 2018/1/6 22:21
 */
@Data
public class TableClass {
    /** 表名称 */
    private String      tableName;
    /** 类名称 */
    private String      className;
    /** 注释 */
    private String      comment;
    /** 是否有日期字段，需要引入java.util */
    private boolean     hasDateType;
    /** 所有的列 */
    private List<Field> fieldList;

}
