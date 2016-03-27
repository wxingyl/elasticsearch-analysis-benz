package com.tqmall.search.benz.action;

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

    public PinyinRequestBuilder traditionToSimple(boolean traditionToSimple) {
        request.traditionToSimple(traditionToSimple);
        return this;
    }

    public PinyinRequestBuilder appendFlags(EnumSet<AppendFlag> appendFlags) {
        request.appendFlags(appendFlags);
        return this;
    }

    public PinyinRequestBuilder needFirstLetter() {
        request.needFirstLetter(true);
        return this;
    }

    public PinyinRequestBuilder needFirstLetter(boolean needFirstLetter) {
        request.needFirstLetter(needFirstLetter);
        return this;
    }

    public PinyinRequestBuilder needSingleCharPy() {
        request.needSingleCharPy(true);
        return this;
    }

    public PinyinRequestBuilder needSingleCharPy(boolean needFirstLetter) {
        request.needSingleCharPy(needFirstLetter);
        return this;
    }
}
