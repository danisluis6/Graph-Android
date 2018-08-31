package org.giwi.networkgraph.lib.paint;

import android.graphics.Paint;

import javax.inject.Inject;

public class StrokePaint extends Paint {

    @Inject
    public StrokePaint() {
        init();
    }

    private void init() {
        Paint strokePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setStrokeWidth(4f);
    }
}
