package org.elasticsearch.indices.analysis;

import com.tqmall.search.benz.BenzConfig;
import org.elasticsearch.common.inject.AbstractModule;

/**
 * Created by xing on 16/3/19.
 *
 * @author xing
 */
public class BenzIndicesAnalysisModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(BenzConfig.class).asEagerSingleton();
        bind(BenzIndicesAnalysis.class).asEagerSingleton();
    }
}
