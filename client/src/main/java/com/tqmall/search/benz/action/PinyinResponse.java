package com.tqmall.search.benz.action;

import org.elasticsearch.action.ActionResponse;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xing on 16/3/26.
 *
 * @author xing
 */
public class PinyinResponse extends ActionResponse {

    private String pinyin;

    private String firstLetter;

    /**
     * {@link PinyinRequest#text} 中每个cjk字符对应的pinyin, 返回结果按照字符位置排好序的
     */
    private List<CjkCharacter> charactersPinyin;

    PinyinResponse() {
    }

    public PinyinResponse(String pinyin, String firstLetter, List<CjkCharacter> charactersPinyin) {
        this.pinyin = pinyin;
        this.firstLetter = firstLetter;
        this.charactersPinyin = charactersPinyin;
    }

    public String firstLetter() {
        return firstLetter;
    }

    public String pinyin() {
        return pinyin;
    }

    public List<CjkCharacter> charactersPinyin() {
        return charactersPinyin;
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        pinyin = in.readOptionalString();
        firstLetter = in.readOptionalString();
        int size = in.readVInt();
        if (size > 0) {
            charactersPinyin = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                CjkCharacter cc = new CjkCharacter();
                cc.readFrom(in);
                charactersPinyin.add(cc);
            }
        }
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeOptionalString(pinyin);
        out.writeOptionalString(firstLetter);
        if (charactersPinyin == null || charactersPinyin.isEmpty()) {
            out.writeVInt(0);
        } else {
            out.writeVInt(charactersPinyin.size());
            for (CjkCharacter cc : charactersPinyin) {
                cc.writeTo(out);
            }
        }
    }

    public static class CjkCharacter {

        private char character;

        private int position;

        private String pinyin;

        CjkCharacter() {
        }

        public CjkCharacter(char character, int position, String pinyin) {
            this.character = character;
            this.pinyin = pinyin;
            this.position = position;
        }

        public char getCharacter() {
            return character;
        }

        public String getPinyin() {
            return pinyin;
        }

        public int getPosition() {
            return position;
        }

        public void readFrom(StreamInput in) throws IOException {
            character = (char) in.readVInt();
            pinyin = in.readString();
            position = in.readVInt();
        }

        public void writeTo(StreamOutput out) throws IOException {
            out.writeVInt(character);
            out.writeString(pinyin);
            out.writeVInt(position);
        }
    }
}
