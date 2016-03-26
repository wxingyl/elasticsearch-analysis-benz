package com.tqmall.search.benz.action.py;

import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;

import java.io.IOException;

/**
 * Created by xing on 16/3/26.
 *
 * @author xing
 */
public class PinyinResponse extends ActionResponse {

    private String pinyin;

    private String firstLetter;

    public PinyinResponse(){
    }

    public PinyinResponse(String pinyin, String firstLetter) {
        this.pinyin = pinyin;
        this.firstLetter = firstLetter;
    }

    public String getFirstLetter() {
        return firstLetter;
    }

    public String getPinyin() {
        return pinyin;
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        pinyin = in.readOptionalString();
        firstLetter = in.readOptionalString();
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeOptionalString(pinyin);
        out.writeOptionalString(firstLetter);
    }
}
