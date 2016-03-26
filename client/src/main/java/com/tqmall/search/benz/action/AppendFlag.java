package com.tqmall.search.benz.action;

/**
 * Created by xing on 16/3/26.
 * 拼音等转换时, 碰到对应非CJK字符时的处理策略
 *
 * @author xing
 */
public enum AppendFlag {
    /**
     * 拼音转化时遇到空白字符({@link Character#isWhitespace(char)}判断)添加到转换结果中
     */
    WHITESPACE,
    /**
     * 拼音转化时遇到ASCII英文字母('a' - 'z' / 'A' - 'Z')添加到转换结果中
     */
    LETTER,
    /**
     * 拼音转化时遇到阿拉伯数字('0' - '9')添加到转换结果中
     */
    DIGIT,
    /**
     * 拼音转化时遇到其他字符, 添加到转换结果中
     */
    OTHER
}
