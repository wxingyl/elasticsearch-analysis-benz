package com.tqmall.search.benz;

import com.tqmall.search.benz.action.LexicalizeRequestBuilder;
import com.tqmall.search.benz.action.PinyinRequestBuilder;
import com.tqmall.search.benz.action.TraditionToSimpleRequestBuilder;
import org.elasticsearch.client.ElasticsearchClient;

/**
 * Created by xing on 16/3/27.
 * benz client request 构造器类~~~
 *
 * @author xing
 */
public final class BenzClient {

    private final ElasticsearchClient client;

    public BenzClient(ElasticsearchClient client) {
        this.client = client;
    }

    public LexicalizeRequestBuilder lexicalize() {
        return new LexicalizeRequestBuilder(client);
    }

    public PinyinRequestBuilder pinyin() {
        return new PinyinRequestBuilder(client);
    }

    public TraditionToSimpleRequestBuilder traditionToSimple() {
        return new TraditionToSimpleRequestBuilder(client);
    }
}
