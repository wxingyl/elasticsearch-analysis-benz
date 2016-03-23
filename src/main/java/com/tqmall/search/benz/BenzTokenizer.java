package com.tqmall.search.benz;

import com.tqmall.search.commons.analyzer.TokenType;
import com.tqmall.search.commons.match.Hit;
import com.tqmall.search.commons.nlp.Segment;
import com.tqmall.search.commons.utils.CommonsUtils;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.KeywordAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.analysis.util.SegmentingTokenizerBase;
import org.elasticsearch.index.analysis.TokenizerFactory;

import java.text.BreakIterator;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by xing on 16/3/19.
 *
 * @author xing
 */
public class BenzTokenizer extends SegmentingTokenizerBase {

    private static final BreakIterator sentenceProto = BreakIterator.getSentenceInstance(Locale.ROOT);

    private final CharTermAttribute termAtt = addAttribute(CharTermAttribute.class);
    private final OffsetAttribute offsetAtt = addAttribute(OffsetAttribute.class);
    private final TypeAttribute typeAtt = addAttribute(TypeAttribute.class);
    private final KeywordAttribute keywordAtt = addAttribute(KeywordAttribute.class);

    private Iterator<Hit<TokenType>> tokens;

    private final Segment segment;

    public BenzTokenizer(Segment segment) {
        super(DEFAULT_TOKEN_ATTRIBUTE_FACTORY, sentenceProto);
        this.segment = segment;
    }

    @Override
    protected final void setNextSentence(int sentenceStart, int sentenceEnd) {
        List<Hit<TokenType>> hits = segment.match(buffer, sentenceStart, sentenceEnd - sentenceStart);
        tokens = CommonsUtils.isEmpty(hits) ? null : hits.iterator();
    }

    @Override
    protected boolean incrementWord() {
        if (tokens == null || !tokens.hasNext()) {
            return false;
        } else {
            Hit<TokenType> token = tokens.next();
            clearAttributes();
            TokenType type = token.getValue();
            termAtt.copyBuffer(buffer, token.getStart(), token.length());
            offsetAtt.setOffset(correctOffset(token.getStart()), correctOffset(token.getEnd()));
            typeAtt.setType(type.name());
            keywordAtt.setKeyword(type != TokenType.EN && type != TokenType.EN_MIX);
            return true;
        }
    }

    public static class Factory implements TokenizerFactory {

        private final Segment segment;

        public Factory(Segment segment) {
            this.segment = segment;
        }

        @Override
        public String name() {
            return segment.getName();
        }

        @Override
        public Tokenizer create() {
            return new BenzTokenizer(segment);
        }
    }
}
