package org.elasticsearch.indices.analysis;

import com.tqmall.search.benz.BenzAnalyzer;
import com.tqmall.search.benz.BenzCjkCharFilter;
import com.tqmall.search.benz.BenzTokenizer;
import com.tqmall.search.benz.Config;
import com.tqmall.search.commons.nlp.Segment;
import com.tqmall.search.commons.nlp.SegmentConfig;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.StopFilter;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.analysis.*;

/**
 * Created by xing on 16/3/19.
 *
 * @author xing
 */
public class BenzIndicesAnalysis extends AbstractComponent {

    @Inject
    public BenzIndicesAnalysis(Settings settings, final Config config,
                               IndicesAnalysisService indicesAnalysisService) {
        super(settings);
        for (SegmentConfig sc : config.getSegmentConfigList()) {
            Segment segment = sc.createSegment(config.getCjkLexicon());
            indicesAnalysisService.analyzerProviderFactories().put(segment.getName(), new PreBuiltAnalyzerProviderFactory(segment.getName(),
                    AnalyzerScope.INDICES, new BenzAnalyzer(config, segment)));
            indicesAnalysisService.tokenizerFactories().put(segment.getName(),
                    new PreBuiltTokenizerFactoryFactory(new BenzTokenizer.Factory(segment)));
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
        //添加BenzCjkCharFilter
        indicesAnalysisService.charFilterFactories().put(BenzCjkCharFilter.NAME,
                new PreBuiltCharFilterFactoryFactory(new BenzCjkCharFilter.Factory()));
    }
}