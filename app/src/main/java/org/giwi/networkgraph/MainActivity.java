package org.giwi.networkgraph;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import org.giwi.networkgraph.beans.Vertex;
import org.giwi.networkgraph.graph.GraphSurfaceView;
import org.giwi.networkgraph.graph.edge.SimpleEdge;
import org.giwi.networkgraph.graph.network.NetworkGraph;
import org.giwi.networkgraph.graph.node.Node;
import org.giwi.networkgraph.graph.node.SimpleNode;


/**
 * The type Main activity.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * On create.
     *
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetworkGraph graph = new NetworkGraph();

        Node v1 = new SimpleNode("18");
        graph.getVertex().add(new Vertex(v1, ContextCompat.getDrawable(this, R.drawable.avatar)));


        Node v2 = new SimpleNode("24");
        graph.getVertex().add(new Vertex(v2, ContextCompat.getDrawable(this, R.drawable.avatar)));
        graph.addEdge(new SimpleEdge(v1, v2, "12"));

        GraphSurfaceView surface = (GraphSurfaceView) findViewById(R.id.mysurface);
        surface.init(graph);

    }
}
