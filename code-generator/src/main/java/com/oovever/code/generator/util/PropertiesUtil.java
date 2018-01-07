package com.oovever.code.generator.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author OovEver
 * 2018/1/6 21:40
 */
public class PropertiesUtil {
    private Properties props;
    private  URI        uri;

    public PropertiesUtil(String fileName) {
        readProperties(fileName);
    }

    /**
     * 加载配置文件
     * @param fileName 配置文件名称
     */
    private void readProperties(String fileName) {
        InputStreamReader isr = null;
        try {
            props = new Properties();
//            使用Thread.currentThread().getContextClassLoader().getResource("")来得到当前的classpath的绝对路径的URI表示法。
            isr = new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName), "UTF-8");
            props.load(isr);
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    /**
     * 获取某个属性
     */
    public String getProperty(String key){
        return props.getProperty(key);
    }
    /**
     * 获取所有属性，返回一个map,不常用
     * 可以试试props.putAll(t)
     */
    public Map getAllProperty(){
        Map map = new HashMap();
//        propertyNames此方法返回属性列表中所有键，包括默认属性列表中的键的枚举。
        Enumeration enu = props.propertyNames();
        while (enu.hasMoreElements()) {
            String key = (String) enu.nextElement();
            String value = props.getProperty(key);
            map.put(key, value);
        }
        return map;
    }
    /**
     * 在控制台上打印出所有属性，调试时用。
     */
    public void printProperties(){
        props.list(System.out);
    }
}
