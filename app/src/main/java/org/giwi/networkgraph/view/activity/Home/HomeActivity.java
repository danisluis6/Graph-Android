package org.giwi.networkgraph.view.activity.Home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.SurfaceHolder;

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

public class HomeActivity extends BaseActivity implements HomeView, SurfaceHolder.Callback {

    @BindView(R.id.surfaceView)
    ZoomView mZoomView;

    @Inject
    NetworkGraph mNetworkGraph;

    @Inject
    Context mContext;

    private MediaPlayer mediaPlayer;
    String path = "http://download.blender.org/peach/bigbuckbunny_movies/BigBuckBunny_320x180.mp4";

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
        mZoomView.getHolder().addCallback(this);

        Node v1 = new SimpleNode("Bà ngoại");
        Vertex vertex1 = new Vertex(v1, ContextCompat.getDrawable(mContext, R.drawable.avatar));
        mNetworkGraph.getVertex().add(vertex1);


        mZoomView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Canvas canvas = holder.lockCanvas(null);
        canvas.drawARGB(0, 225, 225, 255);
        mZoomView.drawGraph(canvas, mNetworkGraph);
        holder.unlockCanvasAndPost(canvas);

//        mediaPlayer = MediaPlayer.create(getApplicationContext(), Uri.parse(path), holder);
//        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener(){
//            public void onPrepared(MediaPlayer arg0) {
//                mediaPlayer.start();
//            }} );
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
