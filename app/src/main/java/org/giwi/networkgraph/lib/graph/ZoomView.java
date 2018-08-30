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

    private static final float minScale = 1f;
    private static final float maxScale = 5f;

    private float scale = 1.0f;

    private float startX = 0.0f;
    private float startY = 0.0f;

    private float dx = 0.0f;
    private float dy = 0.0f;
    private float prevDx = 0.0f;
    private float prevDy = 0.0f;

    private ScaleGestureDetector mScaleDetector;
    private Context context;
    private boolean isSingleTouch;

    public ZoomView(Context context) {
        super(context);
        init();
    }

    public ZoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        attributes = getContext().obtainStyledAttributes(attrs, R.styleable.ZoomView);
        this.context  = context;
        init();
    }

    public ZoomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        attributes = getContext().obtainStyledAttributes(attrs, R.styleable.ZoomView);
        this.context = context;
        init();
    }

    private void init() {
        setOnTouchListener(new MyTouchListeners());
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    private class MyTouchListeners implements View.OnTouchListener {

        MyTouchListeners() {
            super();
        }

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getPointerCount() > 1) {
                isSingleTouch = false;
            } else {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    isSingleTouch = true;
                }
            }
            switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    if (scale > minScale) {
                        mode = Mode.DRAG;
                        startX = motionEvent.getX() - prevDx;
                        startY = motionEvent.getY() - prevDy;
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (isSingleTouch) {
                        if (mode == Mode.DRAG) {
                            dx = motionEvent.getX() - startX;
                            dy = motionEvent.getY() - startY;
                        }
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
                default:
                    return true;
            }

            if (mode == Mode.DRAG && scale >= minScale && isSingleTouch && !mScaleDetector.isInProgress()) {
                getParent().requestDisallowInterceptTouchEvent(true);
                float maxDx = getWidth() * scale;
                float maxDy = getHeight() * scale;
                dx = Math.min(Math.max(dx, - maxDx), 0);
                dy = Math.min(Math.max(dy, - maxDy), 0);
                applyScaleAndTranslation();
            }

            mScaleDetector.onTouchEvent(motionEvent);
            return true;
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(final ScaleGestureDetector detector) {
            final float prevScale = scale;
            scale *= detector.getScaleFactor();
            scale = Math.max(minScale, Math.min(scale, maxScale));
            float adjustedScaleFactor = scale / prevScale;
            float focusX = detector.getFocusX();
            float focusY = detector.getFocusY();
            dx += (dx - focusX) * (adjustedScaleFactor - 1.0f);
            dy += (dy - focusY) * (adjustedScaleFactor - 1.0f);
            applyScaleAndTranslation();
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

    public void init(final NetworkGraph graph, final double height, final double width) {
        setZOrderMediaOverlay(true);
        getHolder().setFormat(PixelFormat.RGBA_8888);
        getHolder().addCallback(new SurfaceHolder.Callback() {
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
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint whitePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);
        FRLayout layout = new FRLayout(graph, new Dimension(getWidth(), getHeight()));

        whitePaint.setColor(attributes.getColor(R.styleable.ZoomView_nodeBgColor, graph.getNodeBgColor()));
        whitePaint.setStyle(Paint.Style.STROKE);
        whitePaint.setStrokeWidth(4f);

        Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.BLACK);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(2);

        float x1 = (float) (width/2);
        float y1 = 140;
        float x2 = (float) (width/2);
        float y2 = 220;
        canvas.drawLine(x1, y1, x2, y2, linePaint);
        canvas.drawLine((float) (width*0.25), 220, (float) (width*0.75f), 220, paint);
        canvas.drawLine((float) (width*0.25), 220, (float) (width*0.25f), 300, paint);
        canvas.drawLine((float) (width*0.75), 220, (float) (width*0.75f), 300, paint);


        /*
         * Vertex 1
         */
        double posX = width/2;
        double posY = 70.0;
        Vertex vertex1 = graph.getVertex().get(0);
        Point2D position = layout.transform(vertex1.getNode());
//        double posX = position.getX();
//        double posY = position.getY();
        canvas.drawCircle((float) posX, (float) posY, 72, whitePaint);
        if (vertex1.getIcon() != null) {
            Bitmap b = ((BitmapDrawable) vertex1.getIcon()).getBitmap();
            Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
            Bitmap roundBitmap = getCroppedBitmap(bitmap, 140);
            canvas.drawBitmap(roundBitmap,
                    (float) posX - 70f, (float) posY - 70f, null);
        }

        /*
         * Vertex 2
         */
        posX = width*0.25 - 72;
        posY = 220.0 + 72;
        Vertex vertex2 = graph.getVertex().get(1);
        canvas.drawCircle((float) posX + 72, (float) posY + 72, 72, whitePaint);
        if (vertex2.getIcon() != null) {
            Bitmap b = ((BitmapDrawable) vertex2.getIcon()).getBitmap();
            Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
            Bitmap roundBitmap = getCroppedBitmap(bitmap, 140);
            canvas.drawBitmap(roundBitmap,
                    (float) posX, (float) posY, null);
        }
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
