package com.tqmall.search.benz.lexicalize;

import org.elasticsearch.action.support.nodes.BaseNodeResponse;
import org.elasticsearch.action.support.nodes.BaseNodesResponse;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;

import java.io.IOException;

/**
 * Created by xing on 16/3/24.
 *
 * @author xing
 */
public class LexicalizeResponse extends BaseNodesResponse<LexicalizeResponse.Node> {

    public LexicalizeResponse() {
    }

    public LexicalizeResponse(ClusterName clusterName, Node[] nodes) {
        super(clusterName, nodes);
    }

    public static class Node extends BaseNodeResponse {

        private int addedNum;

        private int addedStopWordNum;

        private boolean buildFailedSucceed;

        public Node(){
        }

        public Node(DiscoveryNode node, int addedNum, int addedStopWordNum, boolean buildFailedSucceed) {
            super(node);
            this.addedNum = addedNum;
            this.addedStopWordNum = addedStopWordNum;
            this.buildFailedSucceed = buildFailedSucceed;
        }

        public int addedNum() {
            return addedNum;
        }

        public int addedStopWordNum() {
            return addedStopWordNum;
        }

        public boolean buildFailedSucceed() {
            return buildFailedSucceed;
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            super.readFrom(in);
            addedNum = in.readInt();
            addedStopWordNum = in.readInt();
            buildFailedSucceed = in.readBoolean();
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            super.writeTo(out);
            out.write(addedNum);
            out.write(addedStopWordNum);
            out.writeBoolean(buildFailedSucceed);
        }
    }
}
