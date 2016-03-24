package com.tqmall.search.benz.lexicalize;

import org.elasticsearch.action.ActionWriteResponse;
import org.elasticsearch.action.ShardOperationFailedException;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.replication.TransportBroadcastReplicationAction;
import org.elasticsearch.action.support.replication.TransportReplicationAction;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.shard.ShardId;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

import java.util.List;

/**
 * Created by xing on 16/3/24.
 *
 * @author xing
 */
public class TransportLexicalizeAction extends TransportBroadcastReplicationAction<LexicalizeRequest, LexicalizeResponse, ShardLexicalizeRequest, ActionWriteResponse> {

    @Inject
    public TransportLexicalizeAction(Settings settings, ThreadPool threadPool, ClusterService clusterService, TransportService transportService, ActionFilters actionFilters, IndexNameExpressionResolver indexNameExpressionResolver, TransportReplicationAction replicatedBroadcastShardAction) {
        super(LexicalizeAction.NAME, LexicalizeRequest.class, settings, threadPool, clusterService, transportService, actionFilters, indexNameExpressionResolver, replicatedBroadcastShardAction);
    }

    @Override
    protected ActionWriteResponse newShardResponse() {
        return new ActionWriteResponse();
    }

    @Override
    protected ShardLexicalizeRequest newShardRequest(LexicalizeRequest request, ShardId shardId) {
        return new ShardLexicalizeRequest(request, shardId);
    }

    @Override
    protected LexicalizeResponse newResponse(int successfulShards, int failedShards, int totalNumCopies, List<ShardOperationFailedException> shardFailures) {
        return new LexicalizeResponse(totalNumCopies, successfulShards, failedShards, shardFailures);
    }
}
