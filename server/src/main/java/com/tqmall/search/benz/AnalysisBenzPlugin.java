package com.tqmall.search.benz;

import com.tqmall.search.benz.action.TransportLexicalizeAction;
import com.tqmall.search.benz.action.TransportPinyinAction;
import com.tqmall.search.benz.action.lexicalize.LexicalizeAction;
import com.tqmall.search.benz.action.py.PinyinAction;
import com.tqmall.search.benz.analysis.BenzIndicesAnalysis;
import org.elasticsearch.action.ActionModule;
import org.elasticsearch.common.inject.AbstractModule;
import org.elasticsearch.common.inject.Module;
import org.elasticsearch.plugins.Plugin;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by xing on 16/3/18.
 * analysis benz plugin main class
 *
 * @author xing
 */
public class AnalysisBenzPlugin extends Plugin {

    @Override
    public String name() {
        return Config.PLUGIN_NAME;
    }

    @Override
    public String description() {
        return "Benz Analysis for ElasticSearch";
    }

    @Override
    public Collection<Module> nodeModules() {
        return Collections.<Module>singletonList(new BenzModule());
    }

    /**
     * Module初始化时会加载进来, Es通过反射调用的
     */
    public void onModule(ActionModule module) {
        module.registerAction(LexicalizeAction.INSTANCE, TransportLexicalizeAction.class);
        module.registerAction(PinyinAction.INSTANCE, TransportPinyinAction.class);
    }

    public static class BenzModule extends AbstractModule {

        @Override
        protected void configure() {
            bind(Config.class).asEagerSingleton();
            bind(BenzIndicesAnalysis.class).asEagerSingleton();
        }
    }
}
