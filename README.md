# Benz Analysis for ElasticSearch

ElasticSearch中文分词插件，支持词典树正向，反向匹配，Ac算法识别，高度可配置, 目前还在开发中，基本的分词可用~~~

作为扩展，支持汉字文本获取拼音(去除多音字)，繁体转简体，封装了Es调用Action，Request，可以通过ElasticSearchClient调用

文中`$ES_HOME`表示elastic search的安装目录，具体执行的时候自行转换

##Versions

| Benz version | Es version |
| :----------: | :--------: |
|    1.1.0     |   2.2.0    |
|    1.1.1     |   2.3.0    |
|    1.1.2     |   2.4.0    |
|    1.2.0     |   5.0.0    |

注意:

1. 该项目依赖了[search-commons](http://dev.tqmall.com/tq-search/search-commons) 中的[commons-nlp](http://dev.tqmall.com/tq-search/search-commons/tree/master/commons-nlp) 模块，相关jar包没有在maven中央仓库，所以需要先行下载, 本地`mvn install`才能保证下面安装成功
2. 其他es 版本可以自己修改代码，打包编译，比如es 版本 2.2.1版本，自己可以打1.1.0.1版本



##Install

###download code

git clone http://dev.tqmall.com/tq-search/elasticsearch-analysis-benz.git

###complie

`man clean package`

###install

es的安装目录下面`cd $ES_HOME`，执行如下命令:

```shell
./bin/plugin install file:/{BENZ_CODE_PATH}/server/target/releases/analysis-benz-{version}.zip
```

`BENZ_CODE_PATH `为elasticsearch-analysis-benz代码路径

##Config

###config文件路径

1. benz配置可以通过单独文件配置，默认的配置文件`$ES_HOME/config/analysis-benz/config.yml`，具体可以在`$ES_HOME/config/elasticsearch.yml`中通过`benz.conf: {config_path}`指定配置文件路径
2. 也可以直接在es的主配置文件`$ES_HOME/config/elasticsearch.yml`中定义，不用单独的配置文件，只需`benz.conf: es`即可

###词库文件路径

1. 配置项key：`benz.lexicon`,  可以配置文件名或者文件夹，如果为文件夹，则加载该文件夹下面以`words`开头，`.dic`结尾的所有文件作为词库
2. 如果不配置，则加载默认词库文件`$ES_HOME/config/analysis-benz/words-default.dic`

###分词器

如下配置了3个分词器：

```yaml
benz.analyzer.name.prefix: benz_
benz.analyzer:
  - name: index
    parse_en_mix: true
    append_en_mix: true
    cjk_analyzer: full
    merge_num_quantifier: true
    append_num_quantifier: true
  - name: min
    parse_en_mix: false
    cjk_analyzer: min
    merge_num_quantifier: false
  - name: max
    en_stem: k
    parse_en_mix: true
    append_en_mix: false
    cjk_analyzer: max
    merge_num_quantifier: true
    append_num_quantifier: false
#  - name: big
#    en_stem: porter
#    ascii_max: true
#    cjk_analyzer: max
#    merge_num_quantifier: true
#    append_num_quantifier: false
```

`index` 尽可能多的分词，一般用于索引创建时的分词

`min` 最小分词，逆向最小匹配算法实现，query解析时比较好用

`max` 最大分词，逆向最大匹配算法实现，基本的语言识别时可以用用

各项配置解释：

|            配置key            |             解释             |   默认值   |
| :-------------------------: | :------------------------: | :-----: |
| `benz.analyzer.name.prefix` |          分词器名称前缀           |    空    |
|           `name`            |       分词器名称，唯一标识分词器        |    无    |
|         `ascii_max`         |      `ascii字符`是否最大分词       | `false` |
|       `parse_decimal`       |          是否识别小数数字          | `true`  |
|       `parse_en_mix`        |     是否识别英文合成词`en_mix`      | `false` |
|       `append_en_mix`       |     识别的`en_mix`是否最为新词      | `false` |
|       `cjk_analyzer`        |  中文分词方式`{full, min, max}`  |  `min`  |
|   `merge_num_quantifier`    |          是否合并数量词           | `false` |
|   `append_num_quantifier`   |        合并的数量词是否作为新词        | `false` |
|          `en_stem`          | 英文单词stem处理，可取值`k`和`porter` |    空    |

注：1. 分词器的真正名称为： `benz.analyzer.name.prefix`  + `name`

​	2. `ascii字符`指小写英文字母和阿拉伯数字

​	3. 英文合成词`en_mix`指通过`-`了连接的单词，`-`左边必须为英文单词，后边可以是数字，比如：`ac-47`, `ac-king`等，但是`47-ac`就不认为是连接词

​	4. `ascii_max`指无间隔的英文字符，数字(包括小数)认为一个词，比如`DG123`,  `HD5000`等都为一个词, `24.5benz-analyzerv1.0`分词结果为: `24.5benz`, `analyzerv1.0`

​	5. 具体配置详细信息直接看代码[Config.java](server/src/main/java/com/tqmall/search/benz/Config.java)，代码是最好的文档

##Benz Client

client模块封装了一些通过Es调用Action，通过[AnalysisBenzClientPlugin](client/src/main/java/com/tqmall/search/benz/AnalysisBenzClientPlugin.java) 实现，具体包括：

1. `LexicalizeAction` 实时添加分词词库和停止词，只支持添加，不能删除，但是实时修改词库的行为不建议使用，能不用就尽量不要使用~~~~~~
2. `PinyinAction` 汉字文本转换为拼音
3. `TraditionToSimpleAction` 中文文本繁体转简体

具体使用Demo可参阅[ClientDemoTest](client/src/test/java/com/tqmall/search/benz/ClientDemoTest.java)

###pom依赖

```xml
        <dependency>
            <groupId>com.tqmall.search</groupId>
            <artifactId>analysis-benz-client</artifactId>
            <version>dev-1.0-SNAPSHOT</version>
        </dependency>
```

