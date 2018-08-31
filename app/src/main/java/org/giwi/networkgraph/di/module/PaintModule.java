package org.giwi.networkgraph.di.module;

import org.giwi.networkgraph.lib.paint.CircleBitmap;
import org.giwi.networkgraph.lib.paint.LinePaint;
import org.giwi.networkgraph.lib.paint.StrokePaint;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by vuongluis on 4/14/2018.
 *
 * @author vuongluis
 * @version 0.0.1
 */

@Module
public class PaintModule {

    public PaintModule() {
    }

    @Singleton
    @Provides
    public LinePaint provideLinePaint() {
        return new LinePaint();
    }

    @Singleton
    @Provides
    public StrokePaint provideStrokePaint() {
        return new StrokePaint();
    }

    @Singleton
    @Provides
    public CircleBitmap provideCircelBitmap() {
        return new CircleBitmap();
    }
}
