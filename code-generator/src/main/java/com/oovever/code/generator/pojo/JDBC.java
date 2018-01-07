package com.oovever.code.generator.pojo;

import java.io.InputStream;
import java.util.Properties;

/**
 * JDBC实体
 * @author OovEver
 * 2018/1/6 22:31
 */
public class JDBC {
    /**
     * jdbc Properties
     */
    private static Properties pro;
    /**
     * db user
     */
    private        String     user;
    /**
     * db pass
     */
    private        String     pass;
    /**
     * db driver
     */
    private        String     driver;
    /**
     * db url
     */
    private        String     url;

    public JDBC() {
        loadFile();
        this.url = pro.getProperty("jdbc.url");
        this.pass = pro.getProperty("jdbc.password");
        this.driver = pro.getProperty("jdbc.driver");
        this.user = pro.getProperty("jdbc.username");
    }
    /**
     * 加载数据库配置文件 如果没有配置文件将使用默认值
     */
    private void loadFile() {
        if (pro == null) {
            try {
                pro = new Properties();
//                T是确定的，可用于限制，？是非限定的，调用时候确定
                Class<?> cls = JDBC.class;
//                获取类加载器
                ClassLoader cl = cls.getClassLoader();
                InputStream                in = cl.getResourceAsStream("database.properties");
                pro.load(in);
            } catch (Exception e) {
                e.printStackTrace();
                pro.put("jdbc.driver", "org.gjt.mm.mysql.Driver");
                pro.put("jdbc.url", "jdbc:mysql://localhost:3306/blog");
                pro.put("jdbc.username", "root");
                pro.put("jdbc.password", "root");
            }
        }
    }
    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public String getDriver() {
        return driver;
    }

    public String getUrl() {
        return url;
    }

    public String getProV(String key) {
        return pro.getProperty(key);
    }
}
