package org.giwi.networkgraph.di.component;

import org.giwi.networkgraph.di.module.HomeModule;
import org.giwi.networkgraph.view.activity.Home.HomeActivity;
import org.giwi.networkgraph.di.scope.ActivityScope;

import dagger.Subcomponent;

/**
 * Created by vuongluis on 4/14/2018.
 *
 * @author vuongluis
 * @version 0.0.1
 */

@ActivityScope
@Subcomponent(

        modules = {
                HomeModule.class
        }
)
public interface HomeComponent {
    HomeActivity inject(HomeActivity activity);
}


