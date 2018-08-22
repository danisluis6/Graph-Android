package org.giwi.networkgraph.graph.node;

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