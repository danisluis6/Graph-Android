package org.giwi.networkgraph.lib.graph.edge;

import org.giwi.networkgraph.lib.graph.node.GraphComponent;
import org.giwi.networkgraph.lib.graph.node.Node;

public interface Edge extends GraphComponent {
    /**
     * If in need of a readable rendition of the edge's features, use the <code>toString</code> functions.
     *
     * @return the label of the edge
     */
    public String getLabel();

    /**
     * @return the source {@link Node}
     */
    public Node getFrom();

    /**
     * @return the destination {@link Node}
     */
    public Node getTo();

    /**
     * Constructs a full representation of the edge, including its two adjacent nodes.
     *
     * @return a {@link String} representation of the edge
     */
    @Override
    String toString();

    /**
     * Constructs a short representation of the edge, including only information about label.
     *
     * @return a short {@link String} representation of the edge
     */
    String toStringShort();

    /**
     * Constructs a short representation of the edge, including only information about label and direction. The
     * direction depends on the general direction of the representation.
     *
     * @param isBackward
     *            - mentions that the edge is in opposite direction with respect to the representation and should be
     *            represented accordingly
     *
     * @return a short {@link String} representation of the edge
     */
    String toStringShort(boolean isBackward);
}
