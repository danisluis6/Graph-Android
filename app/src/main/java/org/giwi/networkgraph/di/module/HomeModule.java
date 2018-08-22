package org.giwi.networkgraph.di.module;

import org.giwi.networkgraph.view.activity.Home.HomeActivity;
import org.giwi.networkgraph.view.activity.Home.HomeView;

import dagger.Module;

/**
 * Created by vuongluis on 4/14/2018.
 *
 * @author vuongluis
 * @version 0.0.1
 */

@Module
public class HomeModule {

    private HomeActivity mActivity;
    private HomeView mView;

    public HomeModule(HomeActivity activity, HomeView view) {
        mActivity = activity;
        mView = view;
    }
}
