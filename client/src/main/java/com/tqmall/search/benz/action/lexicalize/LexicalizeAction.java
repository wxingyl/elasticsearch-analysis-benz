package com.tqmall.search.benz.action.lexicalize;

import org.elasticsearch.action.Action;
import org.elasticsearch.client.ElasticsearchClient;

/**
 * Created by xing on 16/3/24.
 * 添加词库操作
 * 该Action不建议使用, 实时的添加词对搜索没有什么好处, 原先创建的索引分词没办法改变, query分词结果生效, 两者分词不一致, 影响query匹配
 * 搜索一般部署为集群, 通过重启es, 重建全量索引去搞吧
 *
 * @author xing
 */
public class LexicalizeAction extends Action<LexicalizeRequest, LexicalizeResponse, LexicalizeRequestBuilder> {

    public static final LexicalizeAction INSTANCE = new LexicalizeAction();

    public static final String NAME = "cluster:admin/benz/lexicalize";

    private LexicalizeAction() {
        super(NAME);
    }

    @Override
    public LexicalizeRequestBuilder newRequestBuilder(ElasticsearchClient client) {
        return new LexicalizeRequestBuilder(client);
    }

    @Override
    public LexicalizeResponse newResponse() {
        return new LexicalizeResponse();
    }
}
