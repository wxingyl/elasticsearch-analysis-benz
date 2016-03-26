package com.tqmall.search.benz.action.py;

import org.elasticsearch.action.Action;
import org.elasticsearch.client.ElasticsearchClient;

/**
 * Created by xing on 16/3/25.
 *
 * @author xing
 */
public class PinyinAction extends Action<PinyinRequest, PinyinResponse, PinyinRequestBuilder> {

    public static final PinyinAction INSTANCE = new PinyinAction();

    public static final String NAME = "indices:data/read/benz/py";

    private PinyinAction() {
        super(NAME);
    }

    @Override
    public PinyinRequestBuilder newRequestBuilder(ElasticsearchClient client) {
        return new PinyinRequestBuilder(client);
    }

    @Override
    public PinyinResponse newResponse() {
        return new PinyinResponse();
    }
}
