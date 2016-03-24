package com.tqmall.search.benz.lexicalize;

import org.elasticsearch.action.support.broadcast.BroadcastRequest;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;

import java.io.IOException;
import java.util.Map;

/**
 * Created by xing on 16/3/24.
 * 添加中文词, 或者停止词
 *
 * @author xing
 */
public class LexicalizeRequest extends BroadcastRequest<LexicalizeRequest> {

    private Map<String, String> addWords;

    private String[] removeWords;

    private String[] addStopWords;

    private String[] removeStopWords;

    /**
     * 中文分词是通过ac算法实现的, 添加完之后需要执行buildFailed操作才能生效
     * 默认不立即build
     */
    private boolean buildAcFailed = false;

    public void addWords(Map<String, String> addWords) {
        this.addWords = addWords;
    }

    public void removeWords(String... words) {
        this.removeWords = words;
    }

    public void addStopWords(String... words) {
        this.addStopWords = words;
    }

    public void removeStopWords(String... words) {
        this.removeStopWords = words;
    }

    public void buildAcFailed(boolean buildAcFailed) {
        this.buildAcFailed = buildAcFailed;
    }

    public Map<String, String> addWords() {
        return addWords;
    }

    public String[] removeWords() {
        return removeWords;
    }

    public String[] addStopWords() {
        return addStopWords;
    }

    public String[] removeStopWords() {
        return removeStopWords;
    }

    public boolean buildAcFailed() {
        return buildAcFailed;
    }

    @SuppressWarnings({"rawstype", "unchecked"})
    @Override
    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        buildAcFailed = in.readBoolean();
        addWords = (Map<String, String>) in.readGenericValue();
        removeWords = in.readStringArray();
        addStopWords = in.readStringArray();
        removeStopWords = in.readStringArray();
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeBoolean(buildAcFailed);
        out.writeGenericValue(addWords);
        out.writeStringArrayNullable(removeWords);
        out.writeStringArrayNullable(addStopWords);
        out.writeStringArrayNullable(removeStopWords);
    }
}
