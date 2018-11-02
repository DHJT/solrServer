# Changelog
<!-- @author DHJT 2018-10-17 -->

-------------------
## v7-1.0.3
#### 新特性
- `SolrJ`引入中文分词器：ik-analyzer
    + 添加`ik-analyzer-7.5.0.jar`
    + 添加动态加载字典表功能
- 修改文件名称：`dict1.txt`->`dynamicdic.txt`
- 更新`README.md`文档

-------------------
## v7-1.0.2
#### 新特性
- `SolrJ`部分代码更新
    - 添加`BaseServlet`类
- `demo`核心中加入索引的数据，可以在页面中'http://localhost:8080/solrServer/index.html#/demo/query'查看到

#### bug修复
- 缺少jar包：`mariadb-java-client-2.3.0.jar`

-------------------
## v7-1.0.1
#### bug修复
- `solrJ`代码升级后部分代码不可用
- 缺少jar包：`json-lib-2.2.1-jdk15.jar`,`ezmorph-1.0.6.jar`,`commons-beanutils-1.8.0.jar`
    + 主要是另需引入的jar包，用来通过http返回格式化json结果
- 补充v6版本的`web.servlet.xml`,`web.single.xml`

-------------------
## v7-1.0
#### 新特性
- `Solr`升级至<kbd>7.5.0</kbd>
- 暂时去除`ik`中文分词器
- 暂时没有迁移索引库：solrhome

-------------------
## v6-1.0.2
#### 新特性
- 优化`solrJ`代码，重构代码；

-------------------
## v6-1.0.1
#### 新特性
- sql脚本文件中加入DDL建表语句；

-------------------
## v6-1.0
#### 新特性
- 提供查询服务以及`SolrJ`样例

#### bug修复
- 项目不能在tomcat中启动，修改项目配置：由java工程转换为web项目
    + 参考连接：[Eclipse中将Java项目转换成Web项目的方法](https://blog.csdn.net/l4432321/article/details/52049125)