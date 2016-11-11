package com.tqmall.search.benz;

import com.tqmall.search.benz.action.LexicalizeRequestBuilder;
import com.tqmall.search.benz.action.PinyinRequestBuilder;
import com.tqmall.search.benz.action.TraditionToSimpleRequestBuilder;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.plugins.Plugin;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;

/**
 * date 2016/11/11 ÏÂÎç9:53
 *
 * @author ÉÐ³½
 */
public class TestClient extends TransportClient {

    public TestClient(Settings settings, Collection<Class<? extends Plugin>> plugins) {
        super(settings, plugins);
        try {
            addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public LexicalizeRequestBuilder lexicalize() {
        return new LexicalizeRequestBuilder(this);
    }

    public PinyinRequestBuilder pinyin() {
        return new PinyinRequestBuilder(this);
    }

    public TraditionToSimpleRequestBuilder traditionToSimple() {
        return new TraditionToSimpleRequestBuilder(this);
    }

}
