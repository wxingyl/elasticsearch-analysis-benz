package com.tqmall.search.benz.lexicalize;

import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.action.support.nodes.BaseNodesRequest;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.util.CollectionUtils;

import java.io.IOException;
import java.util.Map;

/**
 * Created by xing on 16/3/24.
 * 添加中文词, 或者添加停止词
 * Note: 词库都不支持删除, 包括停止词
 *
 * @author xing
 */
public class LexicalizeRequest extends BaseNodesRequest<LexicalizeRequest> {

    private Map<String, String> addWords;

    private String[] addStopWords;

    /**
     * 中文分词是通过ac算法实现的, 添加完之后需要执行buildFailed操作才能生效
     * 默认不立即build
     */
    private boolean buildAcFailed = false;

    public LexicalizeRequest() {
    }

    public void addWords(Map<String, String> addWords) {
        this.addWords = addWords;
    }

    public void addStopWords(String... words) {
        this.addStopWords = words;
    }

    public void buildAcFailed(boolean buildAcFailed) {
        this.buildAcFailed = buildAcFailed;
    }

    public Map<String, String> addWords() {
        return addWords;
    }

    public String[] addStopWords() {
        return addStopWords;
    }

    public boolean buildAcFailed() {
        return buildAcFailed;
    }

    @Override
    public ActionRequestValidationException validate() {
        if ((addWords != null && !addWords.isEmpty()) || buildAcFailed
                || !CollectionUtils.isEmpty(addStopWords)) return null;
        else {
            return new ActionRequestValidationException();
        }
    }

    @SuppressWarnings({"rawstype", "unchecked"})
    @Override
    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        buildAcFailed = in.readBoolean();
        addWords = (Map<String, String>) in.readGenericValue();
        addStopWords = in.readStringArray();
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeBoolean(buildAcFailed);
        out.writeGenericValue(addWords);
        out.writeStringArrayNullable(addStopWords);
    }
}
