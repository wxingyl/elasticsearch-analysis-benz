package com.tqmall.search.benz.lexicalize;

import org.elasticsearch.action.Action;
import org.elasticsearch.action.support.nodes.NodesOperationRequestBuilder;
import org.elasticsearch.client.ElasticsearchClient;

import java.util.Map;

/**
 * Created by xing on 16/3/24.
 *
 * @author xing
 */
public class LexicalizeRequestBuilder extends NodesOperationRequestBuilder<LexicalizeRequest, LexicalizeResponse, LexicalizeRequestBuilder> {

    public LexicalizeRequestBuilder(ElasticsearchClient client, Action<LexicalizeRequest, LexicalizeResponse, LexicalizeRequestBuilder> action) {
        super(client, action, new LexicalizeRequest());
    }

    public LexicalizeRequestBuilder addWords(Map<String, String> addWords) {
        request.addWords(addWords);
        return this;
    }

    public LexicalizeRequestBuilder addStopWords(String... words) {
        request.addStopWords(words);
        return this;
    }

    public LexicalizeRequestBuilder buildAcFailed(boolean buildAcFailed) {
        request.buildAcFailed(buildAcFailed);
        return this;
    }
}
