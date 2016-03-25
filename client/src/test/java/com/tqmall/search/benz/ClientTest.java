package com.tqmall.search.benz;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;

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
        client = TransportClient.builder()
                .addPlugin(AnalysisBenzClientPlugin.class)
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
    public void test() {
        List<DiscoveryNode> nodes = client.listedNodes();
        System.out.println(nodes);
    }
}
