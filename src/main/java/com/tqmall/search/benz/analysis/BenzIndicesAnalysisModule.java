package com.tqmall.search.benz.analysis;

import com.tqmall.search.benz.Config;
import org.elasticsearch.common.inject.AbstractModule;

/**
 * Created by xing on 16/3/19.
 *
 * @author xing
 */
public class BenzIndicesAnalysisModule extends AbstractModule {

    private final Config config;

    public BenzIndicesAnalysisModule(Config config) {
        this.config = config;
    }

    @Override
    protected void configure() {
        bind(Config.class).toInstance(config);
        bind(BenzIndicesAnalysis.class).asEagerSingleton();
    }
}
