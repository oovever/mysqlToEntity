# mysqlToEntity
mysqlToEntity

### 软件说明：
- *适用于Mysql*
- *目前仅能生成数据库表对应的实体，还不支持生成mapper，service等*

### 使用教程：
**首先修改db配置为自己的**

- *1、修改resources目录下的databsase.properties*
```
### 数据库地址：端口：名称
jdbc.url=jdbc:mysql://localhost:3306/blog
### 用户名
jdbc.username=root
### 密码
jdbc.password=123456
```
- *2、修改resources目录下的me.properties*
```
### 代码生成的目录，我这里是D盘的tmpTest文件夹下
targetPath=e:/test
### 代码的基本包名（package com.chentongwei.entity）
basic=com.oovever.entity
### 代码的最终包名（com.chentongwei.entity.po）
po=po
### 生成代码后的类注释作者，比如@author TongWei.Chen
author=OovEver.Mu
```
**根据自己的需要自行更改。**

- *3、如何运行？*

部署到自己的ide上，打开com.oovever.code.generator下面的Main.java。