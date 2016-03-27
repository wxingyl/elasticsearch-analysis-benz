package com.tqmall.search.benz.action;

import com.tqmall.search.benz.AnalysisBenzClientPlugin;
import com.tqmall.search.benz.BenzClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.junit.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * Created by xing on 16/3/25.
 *
 * @author xing
 */
public class ClientTest {

    private static TransportClient client;

    @BeforeClass
    public static void init() {
        client = AnalysisBenzClientPlugin.addToClient(TransportClient.builder())
                .settings(Settings.builder().put("client.transport.sniff", true))
                .build();
        try {
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @AfterClass
    public static void close() {
        client.close();
    }

    @Ignore
    public void pluginTest() {
        List<DiscoveryNode> nodes = client.listedNodes();
        System.out.println(nodes);
    }

    @Ignore
    public void actionTest() {
        BenzClient benz = new BenzClient(client);
        String text = "长沙我叫天朝小浪子, 是我的新浪微博";
        PinyinResponse response = benz.pinyin().text(text).get();
        System.out.println("text: " + text + ", pinyin: " + response.pinyin());
        Assert.assertNull(response.firstLetter());
        response = benz.pinyin().text(text).needFirstLetter().get();
        System.out.println("text: " + text + ", pinyin: " + response.pinyin() + ", first letter: "
                + response.firstLetter());
        Assert.assertNotNull(response.firstLetter());
        response = benz.pinyin().text(text).needFirstLetter().needSingleCharPy().get();
        System.out.println("needSingleCharPy: ");
        System.out.println("text: " + text + ", pinyin: " + response.pinyin() + ", first letter: "
                + response.firstLetter());
        for (PinyinResponse.CjkCharacter cc : response.charactersPinyin()) {
            System.out.println("" + cc.character() + ',' + cc.position() + ',' + cc.pinyin());
        }

    }
}
