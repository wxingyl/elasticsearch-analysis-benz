package com.tqmall.search.benz;

import com.tqmall.search.commons.nlp.Segment;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.PorterStemFilter;

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
        // LowerCaseFilter is not needed, BenzCjkCharFilter has convert to low
        result = new PorterStemFilter(result);
        result = new StopFilter(result, config.getStopWords());
        return new TokenStreamComponents(tokenizer, result);
    }
}
