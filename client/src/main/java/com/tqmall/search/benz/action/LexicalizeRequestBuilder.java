package com.tqmall.search.benz.action;

import org.elasticsearch.action.support.nodes.NodesOperationRequestBuilder;
import org.elasticsearch.client.ElasticsearchClient;

import java.util.Map;

/**
 * Created by xing on 16/3/24.
 *
 * @author xing
 */
public class LexicalizeRequestBuilder extends NodesOperationRequestBuilder<LexicalizeRequest, LexicalizeResponse, LexicalizeRequestBuilder> {

    public LexicalizeRequestBuilder(ElasticsearchClient client) {
        super(client, LexicalizeAction.INSTANCE, new LexicalizeRequest());
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

    public LexicalizeRequestBuilder pyLexicon(Map<String, String> pyLexicon) {
        request.pyLexicon(pyLexicon);
        return this;
    }

    public LexicalizeRequestBuilder removePyLexicon(String... removePyLexicon) {
        request.removePyLexicon(removePyLexicon);
        return this;
    }
}
