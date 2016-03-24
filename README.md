# Benz Analyzer for ElasticSearch

ElasticSearch中文分词插件，支持词典树正向，反向匹配，Ac算法识别，高度可配置, 目前还在开发中，基本的分词可用，但是添加词库等还不行~~~

文中`$ES_HOME`表示elastic search的安装目录，具体执行的时候自行转换

##Versions

|   Benz version   | Es version |
| :--------------: | :--------: |
|      master      |   2.2.0    |
| dev-1.0-SNAPSHOT |   2.2.0    |

##Install

###download code

git clone https://github.com/wxingyl/elasticsearch-analysis-benz.git

###complie

man package

###install

es的安装目录下面`cd $ES_HOME`，执行如下命令:

```shell
./bin/plugin install file:/{BENZ_CODE_PATH}/target/releases/analysis-benz-{version}.zip
```

`BENZ_CODE_PATH `为elasticsearch-abalysis-benz代码路径

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
    parse_en_mix: true
    append_en_mix: false
    cjk_analyzer: max
    merge_num_quantifier: true
    append_num_quantifier: false
```

`index` 尽可能多的分词，一般用于索引创建时的分词

`min` 最小分词，逆向最小匹配算法实现，query解析时比较好用

`max` 最大分词，逆向最大匹配算法实现，基本的语言识别时可以用用

各项配置解释：

|            配置key            |            解释            |   默认值   |
| :-------------------------: | :----------------------: | :-----: |
| `benz.analyzer.name.prefix` |         分词器名称前缀          |    空    |
|           `name`            |      分词器名称，唯一标识分词器       |    无    |
|         `ascii_max`         |     `ascii字符`是否最大分词      | `false` |
|       `parse_decimal`       |         是否识别小数数字         | `true`  |
|       `parse_en_mix`        |    是否识别英文合成词`en_mix`     | `false` |
|       `append_en_mix`       |    识别的`en_mix`是否最为新词     | `false` |
|       `cjk_analyzer`        | 中文分词方式`{full, min, max}` |  `min`  |
|   `merge_num_quantifier`    |         是否合并数量词          | `false` |
|   `append_num_quantifier`   |       合并的数量词是否作为新词       | `false` |

注：1. 分词器的真正名称为： `benz.analyzer.name.prefix`  + `name`

​	2. `ascii字符`指小写英文字母和阿拉伯数字

​	3. 英文合成词`en_mix`指通过`-`了连接的单词，`-`左边必须为英文单词，后边可以是数字，比如：`ac-47`, `ac-king`等，但是`47-ac`就不认为是连接词

​	4. `ascii_max`指无间隔的英文字符，数字(包括小数)认为一个词，比如`DG123`,  `HD5000`等都为一个词, `24.5benz-analyzerv1.0`分词结果为: `24.5benz`, `analyzerv1.0`

​	5. 具体配置详细信息直接看代码[Config.java](src/main/java/com/tqmall/search/benz/Config.java)，代码是最好的文档

