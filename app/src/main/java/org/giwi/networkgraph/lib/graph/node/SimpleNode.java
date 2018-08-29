package org.giwi.networkgraph.lib.graph.node;

public class SimpleNode extends AbstractVisualizableGraphComponent implements Node {
    protected String name = null;

    public SimpleNode(String name) {
        this.name = name;
    }

    @Override
    public String getNickName() {
        return name;
    }

    @Override
    public String toString() {
        return this.name;
    }

}