package com.tqmall.search.benz.lexicalize;

import org.elasticsearch.action.ShardOperationFailedException;
import org.elasticsearch.action.support.broadcast.BroadcastResponse;

import java.util.List;

/**
 * Created by xing on 16/3/24.
 *
 * @author xing
 */
public class LexicalizeResponse extends BroadcastResponse {

    public LexicalizeResponse() {
    }

    public LexicalizeResponse(int totalShards, int successfulShards, int failedShards, List<? extends ShardOperationFailedException> shardFailures) {
        super(totalShards, successfulShards, failedShards, shardFailures);
    }
}
