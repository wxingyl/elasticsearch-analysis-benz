package com.tqmall.search.benz.lexicalize;

import org.elasticsearch.action.ActionWriteResponse;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.replication.TransportReplicationAction;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.action.index.MappingUpdatedAction;
import org.elasticsearch.cluster.action.shard.ShardStateAction;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.cluster.metadata.MetaData;
import org.elasticsearch.common.collect.Tuple;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.indices.IndicesService;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

/**
 * Created by xing on 16/3/24.
 *
 * @author xing
 */
public class TransportShardLexicalizeAction extends TransportReplicationAction<ShardLexicalizeRequest, ShardLexicalizeRequest, ActionWriteResponse> {

    public static final String NAME = LexicalizeAction.NAME + "[s]";

    @Inject
    public TransportShardLexicalizeAction(Settings settings, TransportService transportService, ClusterService clusterService, IndicesService indicesService,
                                          ThreadPool threadPool, ShardStateAction shardStateAction, MappingUpdatedAction mappingUpdatedAction,
                                          ActionFilters actionFilters, IndexNameExpressionResolver indexNameExpressionResolver) {
        super(settings, NAME, transportService, clusterService, indicesService, threadPool, shardStateAction, mappingUpdatedAction, actionFilters, indexNameExpressionResolver,
                ShardLexicalizeRequest.class, ShardLexicalizeRequest.class, ThreadPool.Names.GENERIC);
    }

    @Override
    protected ActionWriteResponse newResponseInstance() {
        return new ActionWriteResponse();
    }

    @Override
    protected Tuple<ActionWriteResponse, ShardLexicalizeRequest> shardOperationOnPrimary(MetaData metaData, ShardLexicalizeRequest shardRequest) throws Throwable {
        return null;
    }

    @Override
    protected void shardOperationOnReplica(ShardLexicalizeRequest shardRequest) {

    }
}
