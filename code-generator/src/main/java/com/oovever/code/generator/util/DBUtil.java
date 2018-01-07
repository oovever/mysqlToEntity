package com.oovever.code.generator.util;

import com.oovever.code.generator.pojo.JDBC;
import com.sun.rowset.CachedRowSetImpl;

import javax.sql.rowset.CachedRowSet;
import java.sql.*;

/**
 * 数据库CRUD工具类
 *
 * @author OovEver
 * 2018/1/6 22:30
 */
public class DBUtil {
    /**
     * 数据库配置信息
     */
    private static JDBC jdbc = new JDBC();
    /**
     * 数据库连接
     */
    private Connection conn;

    public DBUtil() {
        conn = getConnection();
    }
//    注册驱动
    static{
        try {
            Class.forName(jdbc.getDriver());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     * 获取数据库连接
     *
     * @return 返回数据库连接
     */
    public Connection getConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(jdbc.getUrl(), jdbc.getUser(),
                    jdbc.getPass());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    /**
     * 插入数据
     *
     * @param sql 要执行的sql语句
     * @param params sql参数
     * @return 返回执行结果
     */
    public int insert(String sql, Object... params) {
        return executeUpdate(sql, params);
    }
    /**
     * 更新数据
     *
     * @param sql 要执行的sql语句
     * @param params sql参数
     * @return  返回执行结果
     */
    public int update(String sql, Object... params) {
        return executeUpdate(sql, params);
    }
    /**
     * 删除数据
     *
     * @param sql
     * @param params
     * @return
     */
    public int delete(String sql, Object... params) {
        return executeUpdate(sql, params);
    }
    /**
     * 执行DDL DML
     * @param sql 要执行的sql语句
     * @return 返回执行结果
     */
    public int executeUpdate(String sql, Object... params) {
        int rlt = 0;
        try {
            PreparedStatement pst = null;
            pst = conn.prepareStatement(sql);
            putParams(pst, params);
            rlt = pst.executeUpdate();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rlt;
    }

    /**
     * 执行查询语句
     *
     * @param sql 要执行的sql语句
     * @return 返回执行结果
     */
    public ResultSet query(String sql, Object... params) {
//        这是一个结果的缓存类，保存在其中的数据不会随着数据库和ResultSet的连接的关闭而丢失，可以传递。
        CachedRowSetImpl rowset = null;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            pst = conn.prepareStatement(sql);
            putParams(pst, params);
            rs = pst.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
    /**
     * 往预编译语句中放置参数
     * @param pst 预编译语句
     * @param params [] 要放置的参数
     * @throws SQLException
     */
    private void putParams(PreparedStatement pst, Object... params) throws SQLException {
        if (params != null) {
            for(int i=0;i<params.length;i++) {
                pst.setObject(i + 1, params[i]);
            }
        }
    }

    /**
     * 关闭数据库连接
     */
    public void close() {
        if (this.conn != null) {
            try {
                this.conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
