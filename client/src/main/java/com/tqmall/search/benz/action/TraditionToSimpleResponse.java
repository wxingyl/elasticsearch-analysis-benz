package com.tqmall.search.benz.action;

import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;

import java.io.IOException;

/**
 * Created by xing on 16/3/27.
 *
 * @author xing
 */
public class TraditionToSimpleResponse extends ActionResponse {
    /**
     * 不包含繁体字的文本
     */
    private String simpleText;

    TraditionToSimpleResponse() {
    }

    public TraditionToSimpleResponse(String simpleText) {
        this.simpleText = simpleText;
    }

    public String getSimpleText() {
        return simpleText;
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        simpleText = in.readString();
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeString(simpleText);
    }
}
