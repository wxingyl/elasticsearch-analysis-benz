package com.tqmall.search.benz.action;

import org.elasticsearch.action.ActionRequestBuilder;
import org.elasticsearch.client.ElasticsearchClient;

/**
 * Created by xing on 16/3/27.
 *
 * @author xing
 */
public class TraditionToSimpleRequestBuilder extends ActionRequestBuilder<TraditionToSimpleRequest, TraditionToSimpleResponse, TraditionToSimpleRequestBuilder> {

    public TraditionToSimpleRequestBuilder(ElasticsearchClient client) {
        super(client, TraditionToSimpleAction.INSTANCE, new TraditionToSimpleRequest());
    }

    public TraditionToSimpleRequestBuilder word(String word) {
        request.word(word);
        return this;
    }
}
