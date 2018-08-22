package org.giwi.networkgraph.lib.graph.network;

import net.xqhs.util.config.Config;

public class UnitLinkData extends Config {
    String parentLogName = null;
    boolean exitTogether = true;
    boolean includeInParent = false;
    String prefix = "";

    public UnitLinkData() {
    }

    public UnitLinkData setparentLogName(String var1) {
        this.parentLogName = var1;
        return this;
    }
}