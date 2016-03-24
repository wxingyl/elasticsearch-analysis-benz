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

默认配置文件是读取`$ES_HOME/config/analysis-benz/config.yml`文件，该文件默认定义了3个分词器：

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

`index`为尽可能的分词