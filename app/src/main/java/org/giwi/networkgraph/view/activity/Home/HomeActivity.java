package org.giwi.networkgraph.view.activity.Home;

import android.content.Context;
import android.support.v4.content.ContextCompat;

import org.giwi.networkgraph.R;
import org.giwi.networkgraph.app.Application;
import org.giwi.networkgraph.di.module.HomeModule;
import org.giwi.networkgraph.lib.beans.Vertex;
import org.giwi.networkgraph.lib.graph.ZoomView;
import org.giwi.networkgraph.lib.graph.edge.SimpleEdge;
import org.giwi.networkgraph.lib.graph.network.NetworkGraph;
import org.giwi.networkgraph.lib.graph.node.Node;
import org.giwi.networkgraph.lib.graph.node.SimpleNode;
import org.giwi.networkgraph.utilities.Utils;
import org.giwi.networkgraph.view.activity.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;

public class HomeActivity extends BaseActivity implements HomeView {

    @BindView(R.id.mysurface)
    ZoomView mZoomView;

    @Inject
    NetworkGraph mNetworkGraph;

    @Inject
    Context mContext;

    @Override
    public void distributedDaggerComponents() {
        Application.getInstance()
                .getAppComponent()
                .plus(new HomeModule(this, this))
                .inject(this);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_home;
    }

    @Override
    protected void initViews() {

        Node v1 = new SimpleNode("1");
        Node v2 = new SimpleNode("24");
        Vertex vertex1 = new Vertex(v1, ContextCompat.getDrawable(mContext, R.drawable.avatar));
        Vertex vertex2 = new Vertex(v2, ContextCompat.getDrawable(mContext, R.drawable.avatar));
        mNetworkGraph.getVertex().add(vertex1);
        mNetworkGraph.getVertex().add(vertex2);

        mNetworkGraph.addEdge(new SimpleEdge(v1, v2, "12"));

        mZoomView.init(mNetworkGraph, Utils.getScreenHeight(this), Utils.getScreenWidth(this));
    }
}
