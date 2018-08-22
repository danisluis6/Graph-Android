package org.giwi.networkgraph.graph.edge;

import org.giwi.networkgraph.graph.node.AbstractVisualizableGraphComponent;
import org.giwi.networkgraph.graph.node.Node;

public class SimpleEdge extends AbstractVisualizableGraphComponent implements Edge
{
    protected String	label	= null;

    protected Node from	= null;

    protected Node		to		= null;

    public SimpleEdge(Node fromNode, Node toNode, String edgeLabel)
    {
        this.from = fromNode;
        this.to = toNode;
        this.label = edgeLabel;
    }

    @Override
    public String getLabel()
    {
        return label;
    }

    @Override
    public Node getFrom()
    {
        return from;
    }

    @Override
    public Node getTo()
    {
        return to;
    }

    @Override
    public String toString()
    {
        return from + toStringShort() + to;
    }

    @Override
    public String toStringShort()
    {
        return toStringShort(false);
    }

    @Override
    public String toStringShort(boolean isBackward)
    {
        return (isBackward ? "<" : "") + (this.label != null ? ("-" + this.label + "-") : "-")
                + (isBackward ? "" : ">");
    }
}