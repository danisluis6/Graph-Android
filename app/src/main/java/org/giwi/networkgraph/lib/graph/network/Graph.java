package org.giwi.networkgraph.lib.graph.network;


import org.giwi.networkgraph.lib.graph.edge.Edge;
import org.giwi.networkgraph.lib.graph.node.GraphComponent;
import org.giwi.networkgraph.lib.graph.node.Node;

import java.util.Collection;

public interface Graph {
    public Graph addNode(Node node);

    public Graph addEdge(Edge edge);

    public Graph add(GraphComponent component);

    public Graph addAll(Collection<? extends GraphComponent> components);

    public Graph removeNode(Node node);

    public Graph removeEdge(Edge edge);

    public Graph remove(GraphComponent component);

    public Graph removeAll(Collection<? extends GraphComponent> components);

    public int n();

    public int m();

    public int size();

    public Collection<Node> getNodes();

    public Collection<Edge> getEdges();

    public Collection<GraphComponent> getComponents();

    public Collection<Edge> getOutEdges(Node node);

    public Collection<Edge> getInEdges(Node node);

    public boolean contains(GraphComponent component);

    public Collection<Node> getNodesNamed(String name);
}
