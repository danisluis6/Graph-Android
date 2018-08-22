package org.giwi.networkgraph.layout;

import org.apache.commons.collections4.Transformer;
import org.giwi.networkgraph.beans.Dimension;
import org.giwi.networkgraph.beans.Point2D;
import org.giwi.networkgraph.graph.network.NetworkGraph;
import org.giwi.networkgraph.graph.node.Node;


/**
 * The interface Layout.
 */
interface Layout extends Transformer<Node, Point2D> {

    /**
     * Initialize void.
     */
    void initialize();

    /**
     * provides initial locations for all vertices.
     *
     * @param initializer the initializer
     */
    void setInitializer(Transformer<Node, Point2D> initializer);

    /**
     * setter for graph
     *
     * @param graph the graph
     */
    void setGraph(NetworkGraph graph);

    /**
     * Returns the full graph (the one that was passed in at
     * construction time) that this Layout refers to.
     *
     * @return the graph
     */
    NetworkGraph getGraph();

    /**
     * Reset void.
     */
    void reset();

    /**
     * Sets size.
     *
     * @param d the d
     */
    void setSize(Dimension d);

    /**
     * Returns the current size of the visualization's space.
     *
     * @return the size
     */
    Dimension getSize();


    /**
     * Sets a flag which fixes this vertex in place.
     *
     * @param v     vertex
     * @param state the state
     */
    void lock(Node v, boolean state);

    /**
     * Returns <code>true</code> if the position of vertex <code>v</code>
     * is locked.
     *
     * @param v the v
     * @return the boolean
     */
    boolean isLocked(Node v);

    /**
     * set the location of a vertex
     *
     * @param v        the v
     * @param location the location
     */
    void setLocation(Node v, Point2D location);
}
