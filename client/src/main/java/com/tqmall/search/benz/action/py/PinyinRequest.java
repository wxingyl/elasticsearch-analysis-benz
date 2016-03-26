package com.tqmall.search.benz.action.py;

import com.tqmall.search.benz.action.AppendFlag;
import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.ActionRequestValidationException;
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

    private boolean needFirstLetter = false;

    private EnumSet<AppendFlag> appendFlags;

    @Override
    public ActionRequestValidationException validate() {
        return text == null ? new ActionRequestValidationException() : null;
    }

    public void text(String text) {
        this.text = text;
    }

    public void needFirstLetter(boolean needFirstLetter) {
        this.needFirstLetter = needFirstLetter;
    }

    public void appendFlags(EnumSet<AppendFlag> appendFlags) {
        this.appendFlags = appendFlags;
    }

    public String text() {
        return text;
    }

    public boolean needFirstLetter() {
        return needFirstLetter;
    }

    public EnumSet<AppendFlag> appendFlags() {
        return appendFlags;
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        text = in.readString();
        needFirstLetter = in.readBoolean();
        int size = in.readVInt();
        if (size > 0) {
            List<AppendFlag> flags = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                flags.add(AppendFlag.valueOf(in.readString()));
            }
            appendFlags = EnumSet.copyOf(flags);
        }
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeString(text);
        out.writeBoolean(needFirstLetter);
        if (appendFlags == null || appendFlags.isEmpty()) {
            out.writeVInt(0);
        } else {
            out.writeVInt(appendFlags.size());
            for (AppendFlag f : appendFlags) {
                out.writeString(f.name());
            }
        }
    }
}
