package com.tqmall.search.benz.lexicalize;

import org.elasticsearch.action.Action;
import org.elasticsearch.client.ElasticsearchClient;

/**
 * Created by xing on 16/3/24.
 * 词库相关操作
 *
 * @author xing
 */
public class LexicalizeAction extends Action<LexicalizeRequest, LexicalizeResponse, LexicalizeRequestBuilder> {

    public static final LexicalizeAction INSTANCE = new LexicalizeAction();

    public static final String NAME = "indices:admin/benz_lexicalize";

    private LexicalizeAction() {
        super(NAME);
    }

    @Override
    public LexicalizeRequestBuilder newRequestBuilder(ElasticsearchClient client) {
        return new LexicalizeRequestBuilder(client, INSTANCE);
    }

    @Override
    public LexicalizeResponse newResponse() {
        return new LexicalizeResponse();
    }
}
