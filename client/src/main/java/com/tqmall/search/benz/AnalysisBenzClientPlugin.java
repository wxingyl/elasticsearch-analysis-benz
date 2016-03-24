package com.tqmall.search.benz;

import org.elasticsearch.plugins.Plugin;

/**
 * Created by xing on 16/3/24.
 * 客户端调用ElasticSearch索引服务器,实现benz分词器词库增删改等操作
 *
 * @author xing
 */
public class AnalysisBenzClientPlugin extends Plugin {

    @Override
    public String name() {
        return "analysis-benz-client";
    }

    @Override
    public String description() {
        return "Benz Analysis Client for ElasticSearch";
    }
}
