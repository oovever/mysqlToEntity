package com.oovever.code.generator.pojo;

import lombok.Data;

/**
 * @author OovEver
 * 2018/1/6 22:21
 */
@Data
public class Field {
    /** 列类型 */
    private String columnType;
    /** 列名称 */
    private String columnName;
    /** java字段类型 */
    private String fieldType;
    /** java字段名称 */
    private String fieldName;
    /** 注释 */
    private String comment;
}
