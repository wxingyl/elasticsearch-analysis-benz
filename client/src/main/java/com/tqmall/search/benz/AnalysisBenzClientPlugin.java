package com.tqmall.search.benz;

import com.tqmall.search.benz.action.lexicalize.LexicalizeAction;
import com.tqmall.search.benz.action.py.PinyinAction;
import org.elasticsearch.action.ActionModule;
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

    /**
     * Module初始化时会加载进来,通过反射调用的
     */
    public void onModule(ActionModule module) {
        module.registerAction(LexicalizeAction.INSTANCE, null);
        module.registerAction(PinyinAction.INSTANCE, null);
    }
}
