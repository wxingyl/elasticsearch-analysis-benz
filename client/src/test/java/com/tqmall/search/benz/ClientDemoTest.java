package com.tqmall.search.benz;

import com.tqmall.search.benz.action.AppendFlag;
import com.tqmall.search.benz.action.PinyinResponse;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xing on 16/3/25.
 *
 * @author xing
 */
public class ClientDemoTest {

    private static TransportClient esClient;

    private static Benz benz;


    @BeforeClass
    public static void init() {
        esClient = AnalysisBenzClientPlugin.addToClient(TransportClient.builder())
                .settings(Settings.builder().put("client.transport.sniff", true))
                .build();
        try {
            esClient.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        benz = new Benz(esClient);
    }

    @AfterClass
    public static void close() {
        esClient.close();
    }

    @Test
    public void pluginTest() {
        List<DiscoveryNode> nodes = esClient.listedNodes();
        System.out.println(nodes);
    }

    @Test
    public void lexicalizeTest() {
        String text = "淘汽云修";
        AnalyzeResponse response = esClient.admin().indices().prepareAnalyze(text).setAnalyzer("benz_index").get();
        List<AnalyzeToken> exceptedTokens = new ArrayList<>();
        exceptedTokens.add(new AnalyzeToken("淘", 0, 1, 0));
        exceptedTokens.add(new AnalyzeToken("汽", 1, 2, 1));
        exceptedTokens.add(new AnalyzeToken("云", 2, 3, 2));
        exceptedTokens.add(new AnalyzeToken("修", 3, 4, 3));
        Assert.assertEquals(exceptedTokens, AnalyzeToken.valueOf(response));

        Map<String, String> addWords = new HashMap<>();
        addWords.put("淘汽", "c");
        addWords.put("云修", "c");
        addWords.put("淘汽云修", "c");
        benz.lexicalize().addWords(addWords).buildAcFailed().get();

        response = esClient.admin().indices().prepareAnalyze(text).setAnalyzer("benz_index").get();
        exceptedTokens.clear();
        exceptedTokens.add(new AnalyzeToken("淘汽", 0, 2, 0));
        exceptedTokens.add(new AnalyzeToken("淘汽云修", 1, 4, 0));
        exceptedTokens.add(new AnalyzeToken("云修", 2, 4, 2));
        Assert.assertEquals(exceptedTokens, AnalyzeToken.valueOf(response));
    }

    static class AnalyzeToken {

        private final String term;

        private final int start, end;

        private final int position;

        AnalyzeToken(String term, int start, int end, int position) {
            this.term = term;
            this.start = start;
            this.end = end;
            this.position = position;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof AnalyzeToken)) return false;

            AnalyzeToken that = (AnalyzeToken) o;

            if (start != that.start) return false;
            if (end != that.end) return false;
            if (position != that.position) return false;
            return term.equals(that.term);
        }

        @Override
        public int hashCode() {
            int result = term.hashCode();
            result = 31 * result + start;
            result = 31 * result + end;
            result = 31 * result + position;
            return result;
        }

        static List<AnalyzeToken> valueOf(AnalyzeResponse response) {
            List<AnalyzeToken> retList = new ArrayList<>();
            for (AnalyzeResponse.AnalyzeToken t : response) {
                retList.add(new AnalyzeToken(t.getTerm(), t.getStartOffset(), t.getEndOffset(), t.getPosition()));
            }
            return retList;
        }
    }

    @Test
    public void pinyinTest() {
        String text = "长沙 我叫小浪子, 是66我的新浪微博";
        PinyinResponse response = benz.pinyin().text(text).get();
        String exceptedPinyin = "changshawojiaoxiaolangzishiwodexinlangweibo";
        Assert.assertEquals(exceptedPinyin, response.pinyin());
        Assert.assertNull(response.firstLetter());

        response = benz.pinyin().text(text).needFirstLetter().appendFlags(AppendFlag.WHITESPACE).get();
        exceptedPinyin = "changsha wojiaoxiaolangzi shiwodexinlangweibo";
        String exceptedFirstLetter = "cswjxlzswdxlwb";
        Assert.assertEquals(exceptedPinyin, response.pinyin());
        Assert.assertEquals(exceptedFirstLetter, response.firstLetter());

        response = benz.pinyin().text(text).needFirstLetter().appendFlags(AppendFlag.WHITESPACE, AppendFlag.DIGIT,
                AppendFlag.OTHER).get();
        exceptedPinyin = "changsha wojiaoxiaolangzi, shi66wodexinlangweibo";
        exceptedFirstLetter = "cswjxlzswdxlwb";
        Assert.assertEquals(exceptedPinyin, response.pinyin());
        Assert.assertEquals(exceptedFirstLetter, response.firstLetter());

        System.out.println("needSingleCharPy: ");
        response = benz.pinyin().text(text).needFirstLetter().needSingleCharPy().get();
        exceptedPinyin = "changshawojiaoxiaolangzishiwodexinlangweibo";
        exceptedFirstLetter = "cswjxlzswdxlwb";
        Assert.assertEquals(exceptedPinyin, response.pinyin());
        Assert.assertEquals(exceptedFirstLetter, response.firstLetter());
        List<PinyinResponse.CjkCharacter> exceptedCjkCharacters = new ArrayList<>();
        exceptedCjkCharacters.add(new PinyinResponse.CjkCharacter('长', 0, "chang"));
        exceptedCjkCharacters.add(new PinyinResponse.CjkCharacter('沙', 1, "sha"));
        exceptedCjkCharacters.add(new PinyinResponse.CjkCharacter('我', 3, "wo"));
        exceptedCjkCharacters.add(new PinyinResponse.CjkCharacter('叫', 4, "jiao"));
        exceptedCjkCharacters.add(new PinyinResponse.CjkCharacter('小', 5, "xiao"));
        exceptedCjkCharacters.add(new PinyinResponse.CjkCharacter('浪', 6, "lang"));
        exceptedCjkCharacters.add(new PinyinResponse.CjkCharacter('子', 7, "zi"));
        exceptedCjkCharacters.add(new PinyinResponse.CjkCharacter('是', 10, "shi"));
        exceptedCjkCharacters.add(new PinyinResponse.CjkCharacter('我', 13, "wo"));
        exceptedCjkCharacters.add(new PinyinResponse.CjkCharacter('的', 14, "de"));
        exceptedCjkCharacters.add(new PinyinResponse.CjkCharacter('新', 15, "xin"));
        exceptedCjkCharacters.add(new PinyinResponse.CjkCharacter('浪', 16, "lang"));
        exceptedCjkCharacters.add(new PinyinResponse.CjkCharacter('微', 17, "wei"));
        exceptedCjkCharacters.add(new PinyinResponse.CjkCharacter('博', 18, "bo"));
        Assert.assertEquals(exceptedCjkCharacters, response.charactersPinyin());
    }

    @Test
    public void traditionToSimpleTest() {
        String text = "發現淘汽雲修汽車";
        String exceptedSimpleText = "发现淘汽云修汽车";
        String simpleText = benz.traditionToSimple().word(text).get().getSimpleText();
        Assert.assertEquals(exceptedSimpleText, simpleText);

        text = "發現淘汽雲修, 汽車";
        exceptedSimpleText = "发现淘汽云修, 汽车";
        simpleText = benz.traditionToSimple().word(text).get().getSimpleText();
        Assert.assertEquals(exceptedSimpleText, simpleText);
    }

}
