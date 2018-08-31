package org.giwi.networkgraph.lib.graph;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceView;
import android.view.View;

import org.giwi.networkgraph.R;

public class ZoomView extends SurfaceView implements View.OnTouchListener {

    private static TypedArray attributes;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        return false;
    }

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
        this.context = context;
        setAttributes(getContext().obtainStyledAttributes(attrs, R.styleable.ZoomView));
        init();
    }

    public ZoomView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        setAttributes(getContext().obtainStyledAttributes(attrs, R.styleable.ZoomView));
        init();
    }

    public static void setAttributes(TypedArray attributes) {
        ZoomView.attributes = attributes;
    }

    public static TypedArray getAttributes() {
        return attributes;
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
                dx = Math.min(Math.max(dx, -maxDx), 0);
                dy = Math.min(Math.max(dy, -maxDy), 0);
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
}
