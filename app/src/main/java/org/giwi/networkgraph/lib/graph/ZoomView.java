package org.giwi.networkgraph.lib.graph;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import org.giwi.networkgraph.R;
import org.giwi.networkgraph.lib.beans.ArcUtils;
import org.giwi.networkgraph.lib.beans.Dimension;
import org.giwi.networkgraph.lib.beans.Point2D;
import org.giwi.networkgraph.lib.beans.Vertex;
import org.giwi.networkgraph.lib.graph.edge.Edge;
import org.giwi.networkgraph.lib.graph.network.NetworkGraph;
import org.giwi.networkgraph.lib.graph.view.FRLayout;

public class ZoomView extends SurfaceView {

    private TypedArray attributes;

    private enum Mode {
        NONE,
        DRAG,
        ZOOM
    }

    private Mode mode = Mode.NONE;

    private static final float minScale = 1.0f;
    private static final float maxScale = 5.0f;

    private float scale = 1.0f;

    private float startX = 0.0f;
    private float startY = 0.0f;

    private float dx = 0.0f;
    private float dy = 0.0f;
    private float prevDx = 0.0f;
    private float prevDy = 0.0f;

    private ScaleGestureDetector SGD;
    private float lastScaleFactor = 1.0f;

    public ZoomView(Context context) {
        super(context);
        init(context);
    }

    public ZoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        attributes = getContext().obtainStyledAttributes(attrs, R.styleable.ZoomView);
        init(context);
    }

    public ZoomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        attributes = getContext().obtainStyledAttributes(attrs, R.styleable.ZoomView);
        init(context);
    }

    private void init(Context context) {
        setOnTouchListener(new MyTouchListeners());
        SGD = new ScaleGestureDetector(context, new ScaleListener());
    }

    private class MyTouchListeners implements View.OnTouchListener {

        MyTouchListeners() {
            super();
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    if (scale > minScale) {
                        mode = Mode.DRAG;
                        startX = motionEvent.getX() - prevDx;
                        startY = motionEvent.getY() - prevDy;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (mode == Mode.DRAG) {
                        dx = motionEvent.getX() - startX;
                        dy = motionEvent.getY() - startY;
                    }
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    mode = Mode.ZOOM;
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    mode = Mode.NONE;
                    break;
                case MotionEvent.ACTION_UP:
                    mode = Mode.NONE;
                    prevDx = dx;
                    prevDy = dy;
                    break;
            }
            SGD.onTouchEvent(motionEvent);

            if ((mode == Mode.DRAG && scale >= minScale) || mode == Mode.ZOOM) {
                getParent().requestDisallowInterceptTouchEvent(true);
                float maxDx = getWidth() * scale;
                float maxDy = getHeight() * scale;
                dx = Math.min(Math.max(dx, -maxDx), 0);
                dy = Math.min(Math.max(dy, -maxDy), 0);
                applyScaleAndTranslation();
            }

            return true;
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector scaleDetector) {
            float scaleFactor = scaleDetector.getScaleFactor();
            if (lastScaleFactor == 0 || (Math.signum(scaleFactor) == Math.signum(lastScaleFactor))) {
                float prevScale = scale;
                scale *= scaleFactor;
                scale = Math.max(minScale, Math.min(scale, maxScale));
                lastScaleFactor = scaleFactor;
                float adjustedScaleFactor = scale / prevScale;
                float focusX = scaleDetector.getFocusX();
                float focusY = scaleDetector.getFocusY();
                dx += (dx - focusX) * (adjustedScaleFactor - 1.0f);
                dy += (dy - focusY) * (adjustedScaleFactor - 1.0f);
            } else {
                lastScaleFactor = 0;
            }
            return true;
        }
    }

    private void applyScaleAndTranslation() {
        this.setScaleX(scale);
        this.setScaleY(scale);
        this.setPivotX(0f);
        this.setPivotY(0f);
        this.setTranslationX(dx);
        this.setTranslationY(dy);
    }

    public void drawGraph(final Canvas canvas, final NetworkGraph graph) {
        Paint paint = new Paint();
        Paint whitePaint = new Paint();
        paint.setAntiAlias(true);
        FRLayout layout = new FRLayout(graph, new Dimension(getWidth(), getHeight()));

        whitePaint.setColor(attributes.getColor(R.styleable.ZoomView_nodeBgColor, graph.getNodeBgColor()));
        whitePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        whitePaint.setStrokeWidth(2f);
        whitePaint.setShadowLayer(5, 0, 0, attributes.getColor(R.styleable.ZoomView_defaultColor, graph
                .getDefaultColor()));

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(20f);
        paint.setColor(attributes.getColor(R.styleable.ZoomView_defaultColor, graph.getDefaultColor()));

        for (Edge edge : graph.getEdges()) {
            Point2D p1 = layout.transform(edge.getFrom());
            Point2D p2 = layout.transform(edge.getTo());
            paint.setStrokeWidth(Float.valueOf(edge.getLabel()) + 1f);
            paint.setColor(attributes.getColor(R.styleable.ZoomView_edgeColor, graph.getEdgeColor()));
            Paint curve = new Paint();
            curve.setAntiAlias(true);
            curve.setStyle(Paint.Style.STROKE);
            curve.setStrokeWidth(2);
            curve.setColor(attributes.getColor(R.styleable.ZoomView_edgeColor, graph.getEdgeColor()));
            PointF e1 = new PointF((float) p1.getX(), (float) p1.getY());
            PointF e2 = new PointF((float) p2.getX(), (float) p2.getY());
            ArcUtils.drawArc(e1, e2, 36f, canvas, curve, paint, whitePaint, Integer.parseInt(edge.getLabel()));
        }

        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(30f);
        paint.setStrokeWidth(0f);
        paint.setColor(attributes.getColor(R.styleable.ZoomView_nodeColor, graph.getNodeColor()));

        Log.i("Vertex", "graph.getVertex(): "+graph.getVertex().size());
        Vertex node = graph.getVertex().get(0);

        Log.i("Vertex", "node.getNode()"+node.getNode());
        Point2D position = layout.transform(node.getNode());
        canvas.drawCircle((float) position.getX(), (float) position.getY(), 60, whitePaint);
        Log.i("Vertex", "Position x: "+position.getX());
        Log.i("Vertex", "Position y: "+position.getY());
        if (node.getIcon() != null) {
            Bitmap b = ((BitmapDrawable) node.getIcon()).getBitmap();
            Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
            Bitmap roundBitmap = getCroppedBitmap(bitmap, 75);
            canvas.drawBitmap(roundBitmap,
                    (float) position.getX() - 38f, (float) position.getY() - 38f, null);
        }
        canvas.drawRect(
                (float) position.getX() - 20,
                (float) position.getY() + 50,
                (float) position.getX() + 20, (float) position.getY() + 10, whitePaint);
        canvas.drawText(node.getNode().getNickName(), (float) position.getX(),
                (float) position.getY() + 40, paint);
    }

    private Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;
        if (bmp.getWidth() != radius || bmp.getHeight() != radius) {
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        } else {
            sbmp = bmp;
        }
        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(
                sbmp.getWidth() / 2 + 0.1f, sbmp.getHeight() / 2 + 0.1f, sbmp.getWidth() / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);
        return output;
    }

}
