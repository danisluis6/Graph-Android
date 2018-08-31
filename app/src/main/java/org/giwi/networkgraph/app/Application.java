package org.giwi.networkgraph.app;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.giwi.networkgraph.di.component.AppComponent;
import org.giwi.networkgraph.di.component.DaggerAppComponent;
import org.giwi.networkgraph.di.module.AppModule;
import org.giwi.networkgraph.di.module.PaintModule;
import org.giwi.networkgraph.di.module.NetworkModule;

/**
 * Created by vuongluis on 4/14/2018.
 * @author vuongluis
 * @version 0.0.1
 */

public class Application extends android.app.Application {

    private AppComponent mApplicationComponent;
    private Context mContext;
    private static Application sInstance;

    public static synchronized Application getInstance() {
        if (sInstance == null) {
            sInstance = new Application();
        }
        return sInstance;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        sInstance = this;
        initAppComponent();
    }

    private void initAppComponent() {
        mApplicationComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this,mContext))
                .paintModule(new PaintModule())
                .networkModule(new NetworkModule())
                .build();
    }

    public AppComponent getAppComponent() {
        return mApplicationComponent;
    }

}
