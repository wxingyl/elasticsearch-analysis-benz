package com.tqmall.search.benz.action;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;

import java.io.IOException;

/**
 * Created by xing on 16/3/27.
 *
 * @author xing
 */
public class TraditionToSimpleRequest extends ActionRequest<TraditionToSimpleRequest> {

    private String word;

    public void word(String word) {
        this.word = word;
    }

    public String word() {
        return word;
    }

    @Override
    public ActionRequestValidationException validate() {
        return Strings.isEmpty(word) ? new ActionRequestValidationException() : null;
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        word = in.readString();
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeString(word);
    }
}
