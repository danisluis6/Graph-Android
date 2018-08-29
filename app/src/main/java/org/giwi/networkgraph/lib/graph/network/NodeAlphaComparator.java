package org.giwi.networkgraph.lib.graph.network;


import net.xqhs.graphs.pattern.NodeP;

import org.giwi.networkgraph.lib.graph.node.Node;

import java.util.Comparator;

public class NodeAlphaComparator implements Comparator<Node>
{
    @Override
    public int compare(Node n0, Node n1)
    {
        if(n0 == n1)
            return 0;
        if(n0 == null)
            return -1;
        if(n1 == null)
            return 1;
        if((n0 instanceof NodeP) && ((NodeP) n0).isGeneric() && (n0 instanceof NodeP) && ((NodeP) n0).isGeneric()
                && n0.getNickName().equals(n1.getNickName()))
            return ((NodeP) n0).genericIndex() - ((NodeP) n1).genericIndex();
        if((n0.getNickName() != null) && (n1.getNickName() != null))
            return n0.getNickName().compareTo(n1.getNickName());
        if((n0.toString() != null) && (n1.toString() != null))
            return n0.toString().compareTo(n1.toString());
        return n0.hashCode() - n1.hashCode();
    }
}