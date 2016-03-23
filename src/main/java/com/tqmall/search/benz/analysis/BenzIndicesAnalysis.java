package com.tqmall.search.benz.analysis;

import com.tqmall.search.benz.Config;
import com.tqmall.search.commons.nlp.Segment;
import com.tqmall.search.commons.nlp.SegmentConfig;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopFilter;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.analysis.*;
import org.elasticsearch.indices.analysis.IndicesAnalysisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by xing on 16/3/19.
 *
 * @author xing
 */
public class BenzIndicesAnalysis extends AbstractComponent {

    private static final Logger log = LoggerFactory.getLogger(BenzIndicesAnalysis.class);

    @Inject
    public BenzIndicesAnalysis(Settings settings, final Config config,
                               IndicesAnalysisService indicesAnalysisService) {
        super(settings);
        for (SegmentConfig sc : config.getSegmentConfigList()) {
            final Segment segment = sc.createSegment(config.getCjkLexicon());
            final String name = segment.getName();
            indicesAnalysisService.analyzerProviderFactories().put(name, new PreBuiltAnalyzerProviderFactory(name,
                    AnalyzerScope.GLOBAL, new BenzAnalyzer(config, segment)));
            indicesAnalysisService.tokenizerFactories().put(name,
                    new PreBuiltTokenizerFactoryFactory(new BenzTokenizer.Factory(segment)));
            log.info("add " + name + " analyzerProviderFactory and tokenizerFactory");
        }
        //添加cjk stopWord filter
        indicesAnalysisService.tokenFilterFactories().put(Config.STOP_FILTER_NAME, new PreBuiltTokenFilterFactoryFactory(new TokenFilterFactory() {
            @Override
            public String name() {
                return Config.STOP_FILTER_NAME;
            }

            @Override
            public TokenStream create(TokenStream tokenStream) {
                return new StopFilter(tokenStream, config.getStopWords());
            }
        }));
        log.info("add " + Config.STOP_FILTER_NAME + " tokenFilterFactory");
        //添加BenzCjkCharFilter
        indicesAnalysisService.charFilterFactories().put(BenzCjkCharFilter.NAME,
                new PreBuiltCharFilterFactoryFactory(new BenzCjkCharFilter.Factory()));
        log.info("add " + BenzCjkCharFilter.NAME + " tokenFilterFactory");
    }
}