package com.tqmall.search.benz.analysis;

import com.tqmall.search.benz.Config;
import com.tqmall.search.commons.nlp.Segment;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;


import java.io.Reader;

/**
 * Created by xing on 16/3/19.
 *
 * @author xing
 */
public class BenzAnalyzer extends Analyzer {

    private final Config config;

    private final Segment segment;

    public BenzAnalyzer(Config config, Segment segment) {
        this.config = config;
        this.segment = segment;
    }

    @Override
    protected Reader initReader(String fieldName, Reader reader) {
        return new BenzCjkCharFilter(reader);
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        final Tokenizer tokenizer = new BenzTokenizer(segment);
        TokenStream result = tokenizer;
        Config.AnalysisConfig analysisConfig = config.getSegmentConfig(segment.getName());
        if (analysisConfig.getEnStem() != null) {
            result = analysisConfig.getEnStem().wrapper(result);
        }
        if (analysisConfig.isNeedStopWord()) {
            result = new StopFilter(result, config.getStopWords());
        }
        return new TokenStreamComponents(tokenizer, result);
    }
}
