# solrServer
<!-- @author DHJT 2018-10-17 -->
A Demo for Apache Solr, you can get something by searching from this Server.

## 环境
- 开发工具：Eclipse；
- 数据库：MariaDb；
- Java代码`SolrJ`操作（6.6.0版本） 
- `Solr`升级（7.5.0版本） 2018-10-31
    + 版本分支<kbd>v7-1.0</kbd>
    + 参考文章[CentOS7搭建solr7.2][1]

## 现场部署
1. 获取所需的solr服务地址：`http://localhost:8080/solr/`
2. 修改solr服务器中`web.xml`中`<env-entry-value>`的值，指定Solr核心存放的位置：`C:/Workspaces/QRwork/solr/WebRoot/solrhome`
3. 只提供查询服务，修改solr服务器中`web.xml`使用`web.single.xml`，src下的源码可以删除（其他文件要保留）;如果要集成SolrJ查询并提供接口，或者将solr集成进现有服务，需要使用到'web.servlet.xml',具体操作还是要根据实际情况进行修改。
4. `li_user_collect_201810182013.sql`,该文件是本demo的测试数据。

## 创建核心
- 启动：'solr.cmd start'
- 使用命令：`C:\DEVELOPERS\Apache Solr\solr-6.4.1\bin\solr.cmd create -c item -d basic_configs`
- 停止：`solr.cmd stop -all`

## 修改核心名称
该核心下有有一个配置文件`core.properties`,修改`name`属性的值即可修改该SolrCore的名称。

## 移植/复用核心
- 可直接复制某个核心到其他solr服务上，服务重启后，可直接使用
    + 保证没有同名的solrCore，若有则需修改核心名。
    + 也可以复制到本Solr服务上，但须修改核心名。

## 配置
### 建立实体Bean索引的配置
- jar包考虑导入
- 实体Bean中加上Solr的`@Field`注解
- `managed-schema`配置文件
    + 加入中文分词器`IKTokenizer`
        * 导入相应的jar包
        * 编写配置文件'IKAnalyzer.cfg.xml'
        * 添加自己的扩展词和停止词
    + 配置与实体Bean对应的字段属性
        * 例：`<field name="archiveTypeId" type="text_general" indexed="true" stored="true"/>`
```xml
<!-- 添加中文分词器IKTokenizer -->
<fieldType name="text_zh" class="solr.TextField" >
  <analyzer type="index" >
      <tokenizer class="org.wltea.analyzer.lucene.IKTokenizerFactory" useSmart="false" conf="ik.conf"/>
      <filter class="solr.StopFilterFactory" ignoreCase="true" words="stopwords.txt" />
  </analyzer>
  <analyzer type="query">
      <tokenizer class="org.wltea.analyzer.lucene.IKTokenizerFactory" useSmart="false" conf="ik.conf"/>
      <filter class="solr.StopFilterFactory" ignoreCase="true" words="stopwords.txt" />
  </analyzer>
</fieldType>
```

### word、excel、PDF等文档索引配置
- 创建新核心或者直接复制已存在的SolrCore
- jar导入位置（core1是Solr核心）
    + 将\solr-5.5.4\solr-5.5.4\contrib\extraction下的jar包复制到\solr_home\core1\lib（目录不存在就新建一个）下
    + 将\solr-5.5.4\solr-5.5.4\dist下的solr-cell-5.5.4.jar复制到\solr_home\core1\lib下
    + 注：也可以把相关jar包放到项目的`WEB-INF\lib`文件夹下
- `managed-schema`配置文件
    + 添加字段属性：
        * `<field name="path"      type="string"   indexed="true"  stored="true"  multiValued="false" />`
        * `<field name="pathftype"      type="string"   indexed="true"  stored="true"  multiValued="false" />`
        * `<field name="pathuploaddate"      type="string"   indexed="true"  stored="true"  multiValued="false" />`
        * `<field name="pathsummary"      type="string"   indexed="true"  stored="true"  multiValued="false" />`
        * `<field name="attr_content"      type="text_general"   indexed="true"  stored="true"  multiValued="false" />`
    + 中文分词器
- `solrconfig.xml`配置文件
    + 配置jar包位置:`<lib dir="./lib" regex=".*\.jar" />`
    + 配置`ExtractingRequestHandler`
```xml
    <!-- solr索引文档配置 -->
  <requestHandler name="/update/extract"
    startup="lazy"
    class="solr.extraction.ExtractingRequestHandler">
    <lst name="defaults">
      <str name="fmap.content">text</str>
      <str name="fmap.meta">ignored_</str>
      <str name="lowernames">true</str>
      <str name="uprefix">attr_</str>
      <str name="captureAttr">true</str>
    </lst>
  </requestHandler>
```

## 参考链接
- [Java实现Slor实体bean数据的索引创建](http://blog.csdn.net/boonya/article/details/57420823)
- [solr6.3.0升级与IK动态词库自动加载](http://www.cnblogs.com/liang1101/articles/6395016.html)
- [solr6.6 solrJ索引富文本(word/pdf)文件](https://www.cnblogs.com/shaosks/p/8033362.html) 
[1]: http://blog.51cto.com/12889016/2103167 'CentOS7搭建solr7.2'
