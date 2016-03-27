package com.tqmall.search.benz.action;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestValidationException;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

/**
 * Created by xing on 16/3/26.
 *
 * @author xing
 */
public class PinyinRequest extends ActionRequest<PinyinRequest> {

    private String text;
    /**
     * 繁体字是否转化为简体字之后再获取拼音~~~
     * 默认true, 该参数还是很有用的,如果转换的字符串{@link #text}中包含繁体字,不转化可能
     * 拿不到对应的拼音
     */
    private boolean traditionToSimple = true;

    private EnumSet<AppendFlag> appendFlags;
    /**
     * 返回结果是否需要拼音首字母~~~
     * @see PinyinResponse#firstLetter
     */
    private boolean needFirstLetter = false;

    /**
     * 返回结果是否需要每个cjk字符对应的拼音
     * @see PinyinResponse#singleCharPyMap
     */
    private boolean needSingleCharPy = false;

    @Override
    public ActionRequestValidationException validate() {
        return Strings.isEmpty(text) ? new ActionRequestValidationException() : null;
    }

    public void text(String text) {
        this.text = text;
    }

    public void appendFlags(EnumSet<AppendFlag> appendFlags) {
        this.appendFlags = appendFlags;
    }

    public void traditionToSimple(boolean traditionToSimple) {
        this.traditionToSimple = traditionToSimple;
    }

    public void needFirstLetter(boolean needFirstLetter) {
        this.needFirstLetter = needFirstLetter;
    }

    public void needSingleCharPy(boolean needSingleCharPy) {
        this.needSingleCharPy = needSingleCharPy;
    }

    public String text() {
        return text;
    }

    public EnumSet<AppendFlag> appendFlags() {
        return appendFlags;
    }

    public boolean traditionToSimple() {
        return traditionToSimple;
    }

    public boolean needFirstLetter() {
        return needFirstLetter;
    }

    public boolean needSingleCharPy() {
        return needSingleCharPy;
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        text = in.readString();
        traditionToSimple = in.readBoolean();
        int size = in.readVInt();
        if (size > 0) {
            List<AppendFlag> flags = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                flags.add(AppendFlag.valueOf(in.readString()));
            }
            appendFlags = EnumSet.copyOf(flags);
        }
        needFirstLetter = in.readBoolean();
        needSingleCharPy = in.readBoolean();
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeString(text);
        out.writeBoolean(traditionToSimple);
        if (appendFlags == null || appendFlags.isEmpty()) {
            out.writeVInt(0);
        } else {

            out.writeVInt(appendFlags.size());
            for (AppendFlag f : appendFlags) {
                out.writeString(f.name());
            }
        }
        out.writeBoolean(needFirstLetter);
        out.writeBoolean(needSingleCharPy);
    }
}
