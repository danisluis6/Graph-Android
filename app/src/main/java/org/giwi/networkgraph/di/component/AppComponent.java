package org.giwi.networkgraph.di.component;

import org.giwi.networkgraph.di.module.AppModule;
import org.giwi.networkgraph.di.module.PaintModule;
import org.giwi.networkgraph.di.module.HomeModule;
import org.giwi.networkgraph.di.module.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by vuongluis on 4/14/2018.
 *
 * @author vuongluis
 * @version 0.0.1
 */

@Singleton
@Component(
        modules = {
                AppModule.class, PaintModule.class, NetworkModule.class
        }
)
public interface AppComponent {
        HomeComponent plus(HomeModule module);
}
