package com.tqmall.search.benz;

import org.apache.lucene.analysis.Analyzer;

/**
 * Created by xing on 16/3/19.
 *
 * @author xing
 */
public class BenzAnalyzer extends Analyzer {

    @Override
    protected TokenStreamComponents createComponents(String fieldName) {
        return null;
    }
}
