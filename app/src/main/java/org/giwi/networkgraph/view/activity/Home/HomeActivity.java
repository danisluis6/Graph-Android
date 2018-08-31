package org.giwi.networkgraph.view.activity.Home;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
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
import org.giwi.networkgraph.lib.paint.CircleBitmap;
import org.giwi.networkgraph.lib.paint.LinePaint;
import org.giwi.networkgraph.lib.paint.StrokePaint;
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

    @Inject
    LinePaint mLinePaint;

    @Inject
    StrokePaint mStrokePaint;

    @Inject
    CircleBitmap mCircleBitmap;

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
        Vertex vertex1 = new Vertex(v1, ContextCompat.getDrawable(mContext, R.drawable.avatar));
        mNetworkGraph.getVertex().add(vertex1);

        init(mNetworkGraph, Utils.getScreenHeight(this), Utils.getScreenWidth(this));
    }

    public void init(final NetworkGraph graph, final double height, final double width) {
        mZoomView.setZOrderMediaOverlay(true);
        mZoomView.getHolder().setFormat(PixelFormat.RGBA_8888);
        mZoomView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Canvas canvas = holder.lockCanvas(null);
                canvas.drawARGB(255, 255, 254, 244); // Get it here: https://color.adobe.com/create/color-wheel
                drawGraph(canvas, graph, height, width);
                holder.unlockCanvasAndPost(canvas);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
            }
        });
    }

    private void drawGraph(final Canvas canvas, final NetworkGraph graph, double height, double width) {
        mStrokePaint.setColor(ZoomView.getAttributes().getColor(R.styleable.ZoomView_nodeBgColor, graph.getNodeBgColor()));

        float x1 = (float) (width / 2);
        float y1 = (float) (height/2);

        float x2 = (float) (width / 2);
        float y2 = (float) (height/2 + 40);
        canvas.drawLine(x1, y1, x2, y2, mLinePaint);
        canvas.drawLine((float) (width * 0.25), (float) (height/2 + 40), (float) (width * 0.75f), (float) (height/2 + 40), mLinePaint);
        canvas.drawLine((float) (width * 0.25), (float) (height/2 + 40), (float) (width * 0.25f), (float) (height/2) + 80, mLinePaint);
        canvas.drawLine((float) (width * 0.75), (float) (height/2 + 40), (float) (width * 0.75f), (float) (height/2) + 80, mLinePaint);

        /*
         * Vertex 1
         */
        double posX = width / 2;
        double posY = height / 2 - 70;
        Vertex vertex1 = graph.getVertex().get(0);
        canvas.drawCircle((float) posX, (float) posY, 70, mStrokePaint);
        if (vertex1.getIcon() != null) {
            Bitmap b = ((BitmapDrawable) vertex1.getIcon()).getBitmap();
            Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
            Bitmap roundBitmap = mCircleBitmap.getCircleBitmap(bitmap, 140);
            canvas.drawBitmap(roundBitmap,
                    (float) posX - 70f, (float) posY - 70f, null);
        }
    }
}
