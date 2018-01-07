package com.oovever.code.generator.pojo;

import com.sun.org.apache.xpath.internal.operations.Or;
import lombok.Data;

import java.util.Date;

/**
 * @author OovEver
 * 2018/1/6 21:33
 */
@Data
public class Global {
//    代码生成的目录
    private String targetPath;
//    代码的基本包名（package com.chentongwei.entity）
    private String basePackage;
//    代码的最终包名（com.chentongwei.entity.po）
    private String po;
//    时间
    private Date now = new Date();
//    作者
    private String author;
}
