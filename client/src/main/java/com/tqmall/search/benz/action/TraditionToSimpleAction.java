package com.tqmall.search.benz.action;

import org.elasticsearch.action.Action;
import org.elasticsearch.client.ElasticsearchClient;

/**
 * Created by xing on 16/3/27.
 * 繁体转简体Action
 *
 * @author xing
 */
public class TraditionToSimpleAction extends Action<TraditionToSimpleRequest, TraditionToSimpleResponse, TraditionToSimpleRequestBuilder> {

    public static final TraditionToSimpleAction INSTANCE = new TraditionToSimpleAction();

    public static final String NAME = "indices:data/read/benz/t4s";

    private TraditionToSimpleAction() {
        super(NAME);
    }

    @Override
    public TraditionToSimpleRequestBuilder newRequestBuilder(ElasticsearchClient client) {
        return new TraditionToSimpleRequestBuilder(client);
    }

    @Override
    public TraditionToSimpleResponse newResponse() {
        return new TraditionToSimpleResponse();
    }
}
