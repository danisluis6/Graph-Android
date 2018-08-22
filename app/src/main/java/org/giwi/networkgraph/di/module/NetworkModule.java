package org.giwi.networkgraph.di.module;

import org.giwi.networkgraph.lib.graph.network.NetworkGraph;

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
public class NetworkModule {

    public NetworkModule() {
    }

    @Provides
    @Singleton
    NetworkGraph provideNetworkGraph() {
        return new NetworkGraph();
    }
}
