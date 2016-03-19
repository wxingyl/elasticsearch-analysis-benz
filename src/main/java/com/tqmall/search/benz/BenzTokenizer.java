package com.tqmall.search.benz;

import com.tqmall.search.commons.analyzer.TokenType;
import com.tqmall.search.commons.match.Hit;
import com.tqmall.search.commons.nlp.Segment;
import com.tqmall.search.commons.nlp.SegmentConfig;
import com.tqmall.search.commons.utils.CommonsUtils;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.analysis.util.SegmentingTokenizerBase;
import org.apache.lucene.util.AttributeFactory;

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

    private Iterator<Hit<TokenType>> tokens;

    private final Segment segment;

    public BenzTokenizer() {
        this(DEFAULT_TOKEN_ATTRIBUTE_FACTORY);
    }

    public BenzTokenizer(AttributeFactory factory) {
        super(factory, sentenceProto);
        segment = new SegmentConfig("test").createSegment(null);
    }

    @Override
    protected final void setNextSentence(int sentenceStart, int sentenceEnd) {
        List<Hit<TokenType>> hits = segment.match(buffer, sentenceStart, sentenceEnd - sentenceStart);
        tokens = CommonsUtils.isEmpty(hits) ? null : hits.iterator();
    }

    @Override
    protected boolean incrementWord() {
        if (tokens == null) {
            return false;
        } else {
            Hit<TokenType> token = tokens.next();
            clearAttributes();
//            termAtt.copyBuffer(token.charArray, 0, token.charArray.length);
//            offsetAtt.setOffset(correctOffset(token.startOffset), correctOffset(token.endOffset));
            typeAtt.setType(token.getValue().name());
            return true;
        }
    }
}
