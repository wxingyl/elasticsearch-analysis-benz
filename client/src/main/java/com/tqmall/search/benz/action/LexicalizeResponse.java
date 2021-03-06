package com.tqmall.search.benz.action;

import org.elasticsearch.action.FailedNodeException;
import org.elasticsearch.action.support.nodes.BaseNodeResponse;
import org.elasticsearch.action.support.nodes.BaseNodesResponse;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.io.stream.Writeable;

import java.io.IOException;
import java.util.List;

/**
 * Created by xing on 16/3/24.
 *
 * @author xing
 */
public class LexicalizeResponse extends BaseNodesResponse<LexicalizeResponse.Node> {

    LexicalizeResponse() {
    }

    LexicalizeResponse(ClusterName clusterName, List<Node> nodes, List<FailedNodeException> failures) {
        super(clusterName, nodes, failures);
    }

    @Override
    protected List<Node> readNodesFrom(StreamInput in) throws IOException {
        return in.readList(in1 -> {
            Node node = new Node();
            node.readFrom(in1);
            return node;
        });
    }

    @Override
    protected void writeNodesTo(StreamOutput out, List<Node> nodes) throws IOException {
        out.writeStreamableList(nodes);
    }

    public static class Node extends BaseNodeResponse {

        private int addedNum;

        private int addedStopWordNum;

        private boolean buildFailedSucceed;

        private int pyAddedNum;

        private int pyRemovedNum;

        public Node() {
        }

        public Node(DiscoveryNode node, int addedNum, int addedStopWordNum, boolean buildFailedSucceed,
                    int pyAddedNum, int pyRemovedNum) {
            super(node);
            this.addedNum = addedNum;
            this.addedStopWordNum = addedStopWordNum;
            this.buildFailedSucceed = buildFailedSucceed;
            this.pyAddedNum = pyAddedNum;
            this.pyRemovedNum = pyRemovedNum;
        }

        public int getAddedNum() {
            return addedNum;
        }

        public int getAddedStopWordNum() {
            return addedStopWordNum;
        }

        public boolean isBuildFailedSucceed() {
            return buildFailedSucceed;
        }

        public int getPyAddedNum() {
            return pyAddedNum;
        }

        public int getPyRemovedNum() {
            return pyRemovedNum;
        }

        @Override
        public void readFrom(StreamInput in) throws IOException {
            super.readFrom(in);
            addedNum = in.readInt();
            addedStopWordNum = in.readInt();
            buildFailedSucceed = in.readBoolean();
            pyAddedNum = in.readInt();
            pyRemovedNum = in.read();
        }

        @Override
        public void writeTo(StreamOutput out) throws IOException {
            super.writeTo(out);
            out.write(addedNum);
            out.write(addedStopWordNum);
            out.writeBoolean(buildFailedSucceed);
            out.write(pyAddedNum);
            out.write(pyRemovedNum);
        }
    }
}
