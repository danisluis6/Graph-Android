package org.giwi.networkgraph.view.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by vuongluis on 4/14/2018.
 * @author vuongluis
 * @version 0.0.1
 */

public abstract class BaseActivity extends AppCompatActivity implements BaseView {

    private Unbinder mUnbinder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        distributedDaggerComponents();
        setContentView(getLayoutRes());
        mUnbinder = ButterKnife.bind(this);
        ButterKnife.bind(this);
        initAttributes();
        initViews();
    }

    public abstract void distributedDaggerComponents();

    protected abstract int getLayoutRes();

    protected abstract void initAttributes();

    protected abstract void initViews();

    @Override
    protected void onDestroy() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroy();
    }
}