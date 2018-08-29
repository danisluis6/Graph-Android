package org.giwi.networkgraph.view.activity.Home;

import android.support.v4.content.ContextCompat;

import org.giwi.networkgraph.R;
import org.giwi.networkgraph.app.Application;
import org.giwi.networkgraph.di.module.HomeModule;
import org.giwi.networkgraph.lib.beans.Vertex;
import org.giwi.networkgraph.lib.graph.ZoomView;
import org.giwi.networkgraph.lib.graph.network.NetworkGraph;
import org.giwi.networkgraph.lib.graph.node.Node;
import org.giwi.networkgraph.lib.graph.node.SimpleNode;
import org.giwi.networkgraph.view.activity.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;

public class HomeActivity extends BaseActivity implements HomeView {

    @BindView(R.id.mysurface)
    ZoomView mZoomView;

    @Inject
    NetworkGraph mNetworkGraph;

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
    protected void initAttributes() {

    }

    @Override
    protected void initViews() {
        Node v1 = new SimpleNode("Bà ngoại");
        Vertex vertex1 = new Vertex(v1, ContextCompat.getDrawable(this, R.drawable.avatar));
        mNetworkGraph.getVertex().add(vertex1);
        mZoomView.initialize(mNetworkGraph);
    }
}
