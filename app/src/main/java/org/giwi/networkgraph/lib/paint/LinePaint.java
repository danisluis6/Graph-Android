package org.giwi.networkgraph.lib.paint;

import android.graphics.Color;
import android.graphics.Paint;

import javax.inject.Inject;

public class LinePaint extends Paint {

    @Inject
    public LinePaint() {
        init();
    }

    private void init() {
        Paint linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.BLACK);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(2);
    }
}
