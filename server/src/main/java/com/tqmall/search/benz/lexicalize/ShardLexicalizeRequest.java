package com.tqmall.search.benz.lexicalize;

import org.elasticsearch.action.ActionRequest;
import org.elasticsearch.action.support.replication.ReplicationRequest;
import org.elasticsearch.index.shard.ShardId;

/**
 * Created by xing on 16/3/24.
 *
 * @author xing
 */
public class ShardLexicalizeRequest extends ReplicationRequest<ShardLexicalizeRequest> {

    public ShardLexicalizeRequest(ActionRequest request, ShardId shardId) {
        super(request, shardId);
    }
}
