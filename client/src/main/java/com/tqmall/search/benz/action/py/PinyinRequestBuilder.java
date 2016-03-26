package com.tqmall.search.benz.action.py;

import com.tqmall.search.benz.action.AppendFlag;
import org.elasticsearch.action.ActionRequestBuilder;
import org.elasticsearch.client.ElasticsearchClient;

import java.util.EnumSet;

/**
 * Created by xing on 16/3/26.
 *
 * @author xing
 */
public class PinyinRequestBuilder extends ActionRequestBuilder<PinyinRequest, PinyinResponse, PinyinRequestBuilder> {

    public PinyinRequestBuilder(ElasticsearchClient client) {
        super(client, PinyinAction.INSTANCE, new PinyinRequest());
    }

    public PinyinRequestBuilder text(String text) {
        request.text(text);
        return this;
    }

    public PinyinRequestBuilder needFirstLetter(boolean needFirstLetter) {
        request.needFirstLetter(needFirstLetter);
        return this;
    }

    public PinyinRequestBuilder appendFlags(EnumSet<AppendFlag> appendFlags) {
        request.appendFlags(appendFlags);
        return this;
    }
}
