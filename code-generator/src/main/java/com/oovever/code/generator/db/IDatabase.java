package com.oovever.code.generator.db;

import com.oovever.code.generator.pojo.TableClass;

import java.util.List;

/**
 * @author OovEver
 * 2018/1/6 22:20
 */
public interface IDatabase {
    /**
     * 获取所有表名
     * @return
     */
    List<TableClass> getTables();

    /**
     * 获取每个表的所有列并填充到表名
     * @param list
     */
    void addColumnsToTable(List<TableClass> list);
}
