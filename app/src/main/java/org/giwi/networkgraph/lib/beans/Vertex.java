package org.giwi.networkgraph.lib.beans;

import android.graphics.drawable.Drawable;

import org.giwi.networkgraph.lib.graph.node.Node;

public class Vertex {

    private Node node;

    private Drawable icon;

    public Vertex(final Node node, final Drawable icon) {
        this.node = node;
        this.icon = icon;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(final Node node) {
        this.node = node;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(final Drawable icon) {
        this.icon = icon;
    }
}
