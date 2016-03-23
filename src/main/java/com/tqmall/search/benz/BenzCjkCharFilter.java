package com.tqmall.search.benz;

import com.tqmall.search.commons.nlp.SegmentFilters;
import org.elasticsearch.index.analysis.CharFilterFactory;

import java.io.IOException;
import java.io.Reader;

/**
 * Created by xing on 16/3/22.
 * 只是实现字符转换: 繁体转简体, 大写转小写, 全角转半角
 * 不会出现字符位置移位, 所以就不继承{@link org.apache.lucene.analysis.CharFilter}
 *
 * @author xing
 * @see SegmentFilters#charConvert(char)
 */
public class BenzCjkCharFilter extends Reader {

    public static final String NAME = "benz_cjk";

    private Reader input;

    public BenzCjkCharFilter(Reader input) {
        super(input);
        this.input = input;
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        int ret = input.read(cbuf, off, len);
        for (int i = off + ret - 1; i >= off; i--) {
            char c = SegmentFilters.charConvert(cbuf[i]);
            if (c != Character.MIN_VALUE) {
                cbuf[i] = c;
            }
        }
        return ret;
    }

    @Override
    public void close() throws IOException {
        input.close();
    }

    public static class Factory implements CharFilterFactory {

        @Override
        public String name() {
            return NAME;
        }

        @Override
        public Reader create(Reader tokenStream) {
            return new BenzCjkCharFilter(tokenStream);
        }
    }
}
