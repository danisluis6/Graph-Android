package org.giwi.networkgraph.lib.graph.network;

import net.xqhs.graphs.pattern.NodeP;
import net.xqhs.util.logging.UnitComponent;

import org.giwi.networkgraph.lib.graph.edge.Edge;
import org.giwi.networkgraph.lib.graph.edge.SimpleEdge;
import org.giwi.networkgraph.lib.graph.node.GraphComponent;
import org.giwi.networkgraph.lib.graph.node.Node;
import org.giwi.networkgraph.lib.graph.node.SimpleNode;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class SimpleGraph extends Unit implements Graph {
    /**
     * Protected structure holding two sets of edges -- incoming and outgoing.
     *
     * @author Andrei Olaru
     */
    protected static class NodeData {
        /**
         * Incoming edges.
         */
        Set<Edge> inEdges;
        /**
         * Outgoing edges.
         */
        Set<Edge> outEdges;

        /**
         * Default constructor.
         *
         * @param in  - incoming edges.
         * @param out - outgoing edges.
         */
        public NodeData(Set<Edge> in, Set<Edge> out) {
            inEdges = in;
            outEdges = out;
        }

        /**
         * Retrieves incoming edges.
         *
         * @return incoming edges.
         */
        public Set<Edge> getInEdges() {
            return inEdges;
        }

        /**
         * Retrieves outgoing edges.
         *
         * @return outgoing edges.
         */
        public Set<Edge> getOutEdges() {
            return outEdges;
        }

        @Override
        public String toString() {
            return "in:" + inEdges.toString() + ";out:" + outEdges.toString();
        }
    }

    /**
     * Separator between edges.
     */
    public static char EDGE_SEPARATOR = ';';
    /**
     * Character that marks the beginning and end of an edge. Edge labels may contain this character, but node labels
     * may not. At the destination end of the edge it may be replaced by {@link #EDGE_TARGET}. In case of bi-directional
     * unlabeled edges, the representation of an edge may contain only one character.
     */
    public static char EDGE_LINE = '-';
    /**
     * Character that marks the destination end of an oriented edge.
     */
    public static char EDGE_TARGET = '>';

    /**
     * The nodes
     */
    protected Map<Node, NodeData> nodes = null;
    /**
     * The edges
     */
    protected Set<Edge> edges = null;

    /**
     * Creates an empty graph.
     */
    public SimpleGraph() {
        super();
        nodes = new HashMap<Node, NodeData>();
        edges = new HashSet<Edge>();
    }

    @Override
    public String getUnitName() {
        return super.getUnitName();
    }

    @Override
    public SimpleGraph addNode(Node node) {
        return add(node);
    }

    /**
     * Warning: the function will not add the nodes to the graph, only the edge between them. Nodes must be added
     * separately.
     *
     * @param edge : the edge to add
     * @return the updated graph
     */
    @Override
    public SimpleGraph addEdge(Edge edge) {
        return add(edge);
    }

    /**
     * This is the only method that actually adds a component to the graph. Any other methods call (should call) this
     * method.
     */
    @Override
    public SimpleGraph add(GraphComponent component) {
        if (component == null)
            throw new IllegalArgumentException("null components not allowed");

        if (component instanceof Node) {
            Node node = (Node) component;
            if (!contains(node)) {
                Set<Edge> outEdges = new HashSet<Edge>();
                Set<Edge> inEdges = new HashSet<Edge>();
                // connect with potentially existing edges
                for (Edge e : edges) {
                    if (e.getFrom() == node)
                        outEdges.add(e);
                    if (e.getTo() == node)
                        inEdges.add(e);
                }
                nodes.put(node, new NodeData(inEdges, outEdges));
            } else
                lw("node [] already present. Not re-added.", node);
        } else if (component instanceof Edge) {
            Edge edge = (Edge) component;
            if (!contains(edge)) {
                edges.add(edge);
                if (contains(edge.getFrom()))
                    // connect 'from' node
                    nodes.get(edge.getFrom()).getOutEdges().add(edge);
                if (contains(edge.getTo()))
                    // connect 'to' node
                    nodes.get(edge.getTo()).getInEdges().add(edge);
            } else
                lw("edge [] already present. Not re-added.", edge);
        } else
            throw new IllegalArgumentException("Given component is not one of Node, Edge.");
        return this;
    }

    @Override
    public SimpleGraph addAll(Collection<? extends GraphComponent> components) {
        for (GraphComponent comp : components)
            add(comp);
        return this;
    }

    @Override
    public SimpleGraph removeNode(Node node) {
        return remove(node);
    }

    @Override
    public SimpleGraph removeEdge(Edge edge) {
        return remove(edge);
    }

    /**
     * This is the only method that actually removes a component from the graph. Any other methods call (should call)
     * this method.
     */
    @Override
    public SimpleGraph remove(GraphComponent component) {
        if (component == null)
            throw new IllegalArgumentException("given components is null.");
        if (!contains(component))
            lr(this, "component [] not contained", component);
        if (component instanceof Node)
            nodes.remove(component);
        else if (component instanceof Edge) {
            Edge edge = (Edge) component;
            if (contains(edge.getFrom()))
                nodes.get(edge.getFrom()).getOutEdges().remove(edge);
            if (contains(edge.getTo()))
                nodes.get(edge.getTo()).getInEdges().remove(edge);
            edges.remove(edge);
        } else
            throw new IllegalArgumentException("Given component is not one of Node, Edge.");
        return this;
    }

    @Override
    public Graph removeAll(Collection<? extends GraphComponent> components) {
        for (GraphComponent comp : components)
            remove(comp);
        return this;
    }

    @Override
    public int n() {
        return nodes.size();
    }

    @Override
    public int m() {
        return edges.size();
    }

    @Override
    public int size() {
        return n();
    }

    @Override
    public Collection<Node> getNodes() {
        return Collections.unmodifiableCollection(nodes.keySet());
    }

    @Override
    public Collection<Edge> getEdges() {
        return Collections.unmodifiableCollection(edges);
    }

    @Override
    public Collection<GraphComponent> getComponents() {
        Collection<GraphComponent> ret = new HashSet<GraphComponent>(nodes.keySet());
        ret.addAll(edges);
        return ret;
    }

    @Override
    public Collection<Edge> getOutEdges(Node node) {
        if (!contains(node))
            throw new IllegalArgumentException("node " + node + " is not in graph");
        return Collections.unmodifiableCollection(nodes.get(node).getOutEdges());
    }

    @Override
    public Collection<Edge> getInEdges(Node node) {
        if (!contains(node))
            throw new IllegalArgumentException("node " + node + " is not in graph");
        return Collections.unmodifiableCollection(nodes.get(node).getInEdges());
    }

    @Override
    public boolean contains(GraphComponent component) {
        if (component instanceof Node)
            return nodes.containsKey(component);
        if (component instanceof Edge)
            return edges.contains(component);
        throw new IllegalArgumentException("Given component is not one of Node, Edge.");
    }

    @Override
    public Collection<Node> getNodesNamed(String name) {
        Collection<Node> ret = new HashSet<Node>();
        for (Node node : nodes.keySet())
            if (node.getNickName().equals(name))
                ret.add(node);
        return ret;
    }

    /**
     * Simple Dijkstra algorithm to compute the distance between one node and all others.
     *
     * @param node : the source node.
     * @return the distances to the other nodes.
     */
    public Map<Node, Integer> computeDistancesFromUndirected(Node node) {
        if (!contains(node))
            throw new IllegalArgumentException("node " + node + " is not in graph");
        Map<Node, Integer> dists = new HashMap<Node, Integer>();
        Queue<Node> grayNodes = new LinkedList<Node>();
        Set<Node> blackNodes = new HashSet<Node>();
        grayNodes.add(node);
        dists.put(node, new Integer(0));

        while (!grayNodes.isEmpty()) {
            Node cNode = grayNodes.poll();
            if (!contains(cNode)) {
                lw("Node [] is not in graph.", cNode);
                continue;
            }
            int dist = dists.get(cNode).intValue();
            blackNodes.add(cNode);

            for (Edge e : getOutEdges(cNode))
                if (!blackNodes.contains(e.getTo())) {
                    if (!grayNodes.contains(e.getTo()))
                        grayNodes.add(e.getTo());
                    if (!dists.containsKey(e.getTo()) || (dists.get(e.getTo()).intValue() > (dist + 1)))
                        dists.put(e.getTo(), new Integer(dist + 1));
                }
            for (Edge e : getInEdges(cNode))
                if (!blackNodes.contains(e.getFrom())) {
                    if (!grayNodes.contains(e.getFrom()))
                        grayNodes.add(e.getFrom());
                    if (!dists.containsKey(e.getFrom()) || (dists.get(e.getFrom()).intValue() > (dist + 1)))
                        dists.put(e.getFrom(), new Integer(dist + 1));
                }
        }

        return dists;
    }

    /**
     * Returns a display of the graph that shows the number of nodes and edges, the list of nodes and the list of edges.
     */
    @Override
    public String toString() {
        String ret = "";
        ret += "G[" + n() + ", " + m() + "] ";
        List<Node> list = new ArrayList<Node>(nodes.keySet());
        Collections.sort(list, new NodeAlphaComparator());
        ret += list.toString();
        for (Edge e : edges)
            ret += "\n" + e.toString();
        return ret;
    }

    /**
     * Creates a representation of the {@link Graph} in DOT format.
     * <p>
     * See <a href = 'http://en.wikipedia.org/wiki/DOT_language'>http://en.wikipedia.org/wiki/DOT_language</a>
     *
     * @return the DOT representation
     */
    // FIXME: override this method in GraphPattern to handle NodeP instances, instead of doing that here
    public String toDot() {
        String ret = "digraph G {\n";
        for (Edge edge : edges) {
            String fromNode = edge.getFrom().toString();
            String toNode = edge.getTo().toString();
            // if(fromNode.contains(" "))
            // fromNode = fromNode.replace(' ', '_');
            // if(toNode.contains(" "))
            // toNode = toNode.replace(' ', '_');
            ret += "\t";
            ret += "\"" + fromNode + "\"";
            ret += " -> ";
            ret += "\"" + toNode + "\"";
            if (edge.getLabel() != null)
                ret += " [" + "nickName=\"" + edge.getLabel() + "\"]";
            ret += ";\n";
        }
        for (Node node : nodes.keySet()) {
            if (node instanceof NodeP && ((NodeP) node).isGeneric())
                ret += "\t\"" + node.toString() + "\" [nickName=\"" + node.getNickName() + "\"];\n";
            // if(node.getNickName().contains(" "))
            // ret += "\t" + node.getNickName().replace(' ', '_') + " [nickName=\"" + node.getNickName() + "\"];\n";
        }
        ret += "}";
        return ret;
    }

    /**
     * Reads the structure of the graph as list of edges, adding all nodes appearing in the definition of edges.
     * <ul>
     * <li>lines will always be read separately
     * <li>multiple edges are read from the same line, if they are separated by a semi-column (;)
     * <li>node names cannot have dashes or 'greater' (>)
     * <li>edge names cannot have 'greater' (>)
     * <li>a labeled edge begins with a dash (-)
     * <li>a unidirectional edge ends with dash-greater or just greater (-> or >)
     * <li>an unlabeled unidirectional edge is either dash-greater or just greater (-> or >)
     * <li>all unidirectional edges are to the right (source -> destination)
     * <li>bi-directional edges with no nickName should be one or two dashes (- or --)
     * <li>labeled bi-directional edges should begin and end with a dash ( - nickName here - )
     * <li>all spaces between elements are accepted and ignored
     * </ul>
     * The newly read edges and nodes are added on the existing structure, if any.
     *
     * @param input - a stream to read from
     * @return the enriched {@link SimpleGraph} instance
     */
    public SimpleGraph readFrom(InputStream input) {
        UnitComponent log = (UnitComponent) new UnitComponent().setLink(getUnitName());
        // .setUnitName("test").setLogLevel(Level.ALL);
        Scanner scan = new Scanner(input);
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            String edgeReads[] = line.split(Character.toString(EDGE_SEPARATOR));
            for (String edgeRead : edgeReads) // each element is an edge
            {
                log.lf("new edge: ", edgeRead);

                boolean bidirectional = true;

                String[] parts1 = edgeRead.split(Character.toString(EDGE_LINE), 2); // identify first dash (beginning of
                if (parts1.length < 2) // two parts: source node and edge name+destination node
                {
                    parts1 = edgeRead.split(Character.toString(EDGE_TARGET), 2);
                    if (parts1.length < 2) {
                        log.le("input corrupted");
                        continue;
                    }
                    bidirectional = false;
                }
                String node1name = parts1[0].trim(); // source node
                String node2name = null;
                String edgeName = null;
                String[] parts2 = parts1[1].split(Character.toString(EDGE_TARGET)); // split destination node from edge
                if ((parts2.length < 1) || (parts2.length > 2)) // no appearance or 1 appearance
                {
                    log.le("input corrupted");
                    continue;
                }

                Node node1 = null;
                Node node2 = null;

                if (parts2.length == 2) // unidirectional edge
                {
                    bidirectional = false;
                    node2name = parts2[1].trim();
                    if ((parts2[0].length() > 0) && (parts2[0].charAt(parts2[0].length() - 1) == '-'))
                        edgeName = parts2[0].substring(0, parts2[0].length() - 1).trim();
                    else
                        edgeName = parts2[0].trim();
                } else {
                    int idx = parts1[1].lastIndexOf(EDGE_LINE);
                    if (idx < 0) // edge is just '-' (the one detected earlier)
                        node2name = parts2[0].trim();
                    else { // there is another dash somewhere that marks the end of the edge name
                        node2name = parts1[1].substring(idx + 1).trim();
                        edgeName = parts1[1].substring(0, idx).trim();
                    }
                }
                if ((edgeName != null) && (edgeName.length() == 0))
                    edgeName = null;
                // log.trace("[" + parts1.toString() + "] [" + parts2.toString() + "]");
                log.lf("[] [] []", node1name, node2name, edgeName);

                if (getNodesNamed(node1name).isEmpty()) {
                    node1 = new SimpleNode(node1name);
                    addNode(node1);
                } else
                    node1 = getNodesNamed(node1name).iterator().next();

                if (getNodesNamed(node2name).isEmpty()) {
                    node2 = new SimpleNode(node2name);
                    addNode(node2);
                } else
                    node2 = getNodesNamed(node2name).iterator().next();

                addEdge(new SimpleEdge(node1, node2, edgeName));
                if (bidirectional)
                    addEdge(new SimpleEdge(node2, node1, edgeName));
            }
        }
        scan.close();
        return this;
    }
}
