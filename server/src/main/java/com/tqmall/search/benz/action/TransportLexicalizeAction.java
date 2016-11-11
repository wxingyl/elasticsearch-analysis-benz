package com.tqmall.search.benz.action;

import com.tqmall.search.benz.Config;
import com.tqmall.search.commons.analyzer.CjkLexicon;
import com.tqmall.search.commons.analyzer.TokenType;
import com.tqmall.search.commons.nlp.PinyinConvert;
import com.tqmall.search.commons.utils.CommonsUtils;
import org.elasticsearch.action.FailedNodeException;
import org.elasticsearch.action.admin.cluster.stats.ClusterStatsAction;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.nodes.BaseNodeRequest;
import org.elasticsearch.action.support.nodes.TransportNodesAction;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.cluster.service.ClusterService;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.util.CollectionUtils;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by xing on 16/3/24.
 *
 * @author xing
 */
public class TransportLexicalizeAction extends TransportNodesAction<LexicalizeRequest, LexicalizeResponse, TransportLexicalizeAction.NodeInfoRequest, LexicalizeResponse.Node> {

    private final Config config;

    @Inject
    public TransportLexicalizeAction(Settings settings, ThreadPool threadPool, ClusterService clusterService, TransportService transportService,
                                     ActionFilters actionFilters, IndexNameExpressionResolver indexNameExpressionResolver, Config config) {
        super(settings, ClusterStatsAction.NAME, threadPool, clusterService, transportService, actionFilters, indexNameExpressionResolver,
                LexicalizeRequest::new, TransportLexicalizeAction.NodeInfoRequest::new, ThreadPool.Names.MANAGEMENT, LexicalizeResponse.Node.class);
        this.config = config;
    }

    @Override
    protected LexicalizeResponse newResponse(LexicalizeRequest request, List<LexicalizeResponse.Node> nodes, List<FailedNodeException> failures) {
        return new LexicalizeResponse(clusterService.getClusterName(), nodes, failures);
    }

    @Override
    protected NodeInfoRequest newNodeRequest(String nodeId, LexicalizeRequest request) {
        return new NodeInfoRequest(nodeId, request);
    }

    @Override
    protected LexicalizeResponse.Node newNodeResponse() {
        return new LexicalizeResponse.Node();
    }

    @SuppressWarnings("deprecation")
    @Override
    protected LexicalizeResponse.Node nodeOperation(NodeInfoRequest request) {
        LexicalizeRequest lexicalizeRequest = request.request;
        int addedNum = 0;
        if (!CommonsUtils.isEmpty(lexicalizeRequest.addWords())) {
            CjkLexicon cjkLexicon = config.getCjkLexicon().get();
            for (Map.Entry<String, String> e : lexicalizeRequest.addWords().entrySet()) {
                if (cjkLexicon.addWord(e.getKey(), TokenType.fromString(e.getValue()))) addedNum++;
            }
        }
        int addedStopWordNum = 0;
        if (!CollectionUtils.isEmpty(lexicalizeRequest.addStopWords())) {
            for (String s : lexicalizeRequest.addStopWords()) {
                if (config.getStopWords().add(s)) addedStopWordNum++;
            }
        }
        boolean buildFailedSucceed = false;
        if (lexicalizeRequest.buildAcFailed()) {
            CjkLexicon cjkLexicon = config.getCjkLexicon().get();
            buildFailedSucceed = cjkLexicon.buildAcTrieFailed();
        }
        int pyAddedNum = 0;
        if (!CommonsUtils.isEmpty(lexicalizeRequest.pyLexicon())) {
            PinyinConvert pyConvert = PinyinConvert.instance();
            for (Map.Entry<String, String> e : lexicalizeRequest.pyLexicon().entrySet()) {
                if (pyConvert.addPinyinLexicon(e.getKey(), e.getValue())) pyAddedNum++;
            }
        }
        int pyRemovedNum = 0;
        if (!CollectionUtils.isEmpty(lexicalizeRequest.removePyLexicon())) {
            PinyinConvert pyConvert = PinyinConvert.instance();
            for (String word : lexicalizeRequest.removePyLexicon()) {
                if (pyConvert.removePinyinLexicon(word)) pyRemovedNum++;
            }
        }
        return new LexicalizeResponse.Node(clusterService.localNode(), addedNum, addedStopWordNum,
                buildFailedSucceed, pyAddedNum, pyRemovedNum);
    }

    @Override
    protected boolean accumulateExceptions() {
        return false;
    }

    static class NodeInfoRequest extends BaseNodeRequest {

        private LexicalizeRequest request;

        NodeInfoRequest() {
        }

        NodeInfoRequest(String nodeId, LexicalizeRequest request) {
            super(nodeId);
            this.request = request;
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            super.writeTo(out);
            request.writeTo(out);
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            super.readFrom(in);
            request = new LexicalizeRequest();
            request.readFrom(in);
        }
    }
}
