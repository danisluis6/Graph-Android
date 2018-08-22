package org.giwi.networkgraph.lib.graph.node;

public class SimpleNode extends AbstractVisualizableGraphComponent implements Node {
    protected String label = null;

    public SimpleNode(String nodeLabel) {
        this.label = nodeLabel;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return this.label;
    }

}