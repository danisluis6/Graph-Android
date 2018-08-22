package org.giwi.networkgraph.lib.graph.network;

import net.xqhs.util.config.Config;
import net.xqhs.util.logging.Debug;
import net.xqhs.util.logging.DisplayEntity;
import net.xqhs.util.logging.LoggerSimple;
import net.xqhs.util.logging.ReportingEntity;
import net.xqhs.util.logging.logging.LogWrapper;
import net.xqhs.util.logging.logging.Logging;

public class Unit extends Config {
    public static final String DEFAULT_UNIT_NAME = "theDefaulUnitName";
    public static final LoggerSimple.Level DEFAULT_LEVEL;
    String unitName = null;
    String logName = null;
    boolean ensureNew = false;
    LoggerSimple.Level level = null;
    DisplayEntity display = null;
    ReportingEntity reporter = null;
    LogWrapper.LoggerType loggerWrapperType = null;
    String loggerWrapperClass = null;
    UnitLinkData linkData = new UnitLinkData();
    LogWrapper log = null;

    protected String getDefaultUnitName() {
        return "theDefaulUnitName";
    }

    public Unit() {
    }

    public boolean lockedR() {
        try {
            super.locked();
            return false;
        } catch (ConfigLockedException var2) {
            this.le(var2.toString(), new Object[0]);
            return true;
        }
    }

    public Unit lock() {
        if(this.unitName == null || this.logName == null) {
            this.setUnitName(this.unitName);
        }

        super.lock();
        if(this.unitName != null && this.logName != null) {
            try {
                this.log = Logging.getLogger(this.logName, this.linkData.parentLogName, this.display, this.reporter, this.ensureNew, this.loggerWrapperClass, this.level);
            } catch (ClassNotFoundException var2) {
                throw new IllegalArgumentException("Failed to instantiate logging wrapper class.", var2);
            }
        }

        return this;
    }

    public Unit setUnitName(String var1) {
        return this.setUnitName(var1, false, false, false);
    }

    public Unit setUnitName(String var1, boolean var2, boolean var3) {
        return this.setUnitName(var1, true, var2, var3);
    }

    private Unit setUnitName(String var1, boolean var2, boolean var3, boolean var4) {
        if(this.lockedR()) {
            return this;
        } else {
            this.unitName = var1;
            if(this.unitName == null && !"theDefaulUnitName".equals(this.getDefaultUnitName())) {
                this.unitName = this.getDefaultUnitName();
            }

            if("theDefaulUnitName".equals(this.unitName)) {
                this.unitName = this.makeClassName(var4);
                this.logName = this.unitName;
            } else if(var2) {
                if(var3) {
                    this.logName = this.unitName + this.makeClassName(var4);
                } else {
                    this.logName = this.makeClassName(var4) + this.unitName;
                }
            } else {
                this.logName = this.unitName;
            }

            if(this.logName != null && this.level == null) {
                this.setLogLevel(DEFAULT_LEVEL);
            }

            return this;
        }
    }

    private String makeClassName(boolean var1) {
        return var1?this.getClass().getCanonicalName():this.getClass().getSimpleName();
    }

    public Unit setLogEnsureNew() {
        if(this.lockedR()) {
            return this;
        } else {
            this.ensureNew = true;
            return this;
        }
    }

    public Unit setLoggerType(LogWrapper.LoggerType var1) {
        if(var1 == null) {
            throw new IllegalArgumentException("Given logger type is null");
        } else {
            return this.setLoggerTypeClass(var1, (String)null);
        }
    }

    public Unit setLoggerClass(String var1) {
        if(var1 == null) {
            throw new IllegalArgumentException("Given class is null");
        } else {
            return this.setLoggerTypeClass((LogWrapper.LoggerType)null, var1);
        }
    }

    protected Unit setLoggerTypeClass(LogWrapper.LoggerType var1, String var2) {
        if(this.lockedR()) {
            return this;
        } else if(var1 != null && var2 != null) {
            throw new IllegalArgumentException("cannot set the logger type and the class name at the same time.");
        } else {
            if(var1 != null) {
                this.loggerWrapperType = var1;
                if(this.loggerWrapperType != LogWrapper.LoggerType.OTHER) {
                    this.loggerWrapperClass = var1.getClassName();
                }
            }

            if(var2 != null) {
                this.loggerWrapperClass = var2;
                LogWrapper.LoggerType[] var3 = LogWrapper.LoggerType.values();
                int var4 = var3.length;

                for(int var5 = 0; var5 < var4; ++var5) {
                    LogWrapper.LoggerType var6 = var3[var5];
                    if(var2.equals(var6.getClassName())) {
                        this.loggerWrapperType = var6;
                    }
                }

                this.loggerWrapperType = LogWrapper.LoggerType.OTHER;
            }

            return this;
        }
    }

    public Unit setLogLevel(LoggerSimple.Level var1) {
        this.level = var1;
        return this;
    }

    public Unit setLink(String var1) {
        return this.setLink((new UnitLinkData()).setparentLogName(var1));
    }

    public Unit setLink(UnitLinkData var1) {
        if(this.lockedR()) {
            return this;
        } else {
            this.linkData = var1;
            return this;
        }
    }

    public Unit setLogDisplay(DisplayEntity var1) {
        this.display = var1;
        return this;
    }

    public Unit setLogReporter(ReportingEntity var1) {
        this.reporter = var1;
        return this;
    }

    protected String getUnitName() {
        return this.unitName;
    }

    protected void doExit() {
        if(this.log != null && this.logName != null) {
            Logging.exitLogger(this.logName);
            this.log = null;
            this.logName = null;
        }

        this.unitName = null;
    }

    protected void le(String var1, Object... var2) {
        this.l(LoggerSimple.Level.ERROR, var1, var2);
    }

    protected void lw(String var1, Object... var2) {
        this.l(LoggerSimple.Level.WARN, var1, var2);
    }

    protected void li(String var1, Object... var2) {
        this.l(LoggerSimple.Level.INFO, var1, var2);
    }

    protected void lf(String var1, Object... var2) {
        this.l(LoggerSimple.Level.TRACE, var1, var2);
    }

    protected Object lr(Object var1) {
        return this.lr(var1, (String)null, new Object[0]);
    }

    protected Object lr(Object var1, String var2, Object... var3) {
        this.lf("[" + (var1 != null?var1.toString():"null") + "]" + (var2 != null?":[" + var2 + "]":""), new Object[0]);
        return var1;
    }

    protected void dbg(Debug.DebugItem var1, String var2, Object... var3) {
        if(var1.toBool()) {
            this.lf(var2, new Object[0]);
        }

    }

    protected void l(LoggerSimple.Level var1, String var2, Object... var3) {
        this.ensureLocked();
        if(this.log != null && var1.displayWith(this.level)) {
            this.log.l(var1, compose(var2, var3));
        }

    }

    protected static String compose(String var0, Object[] var1) {
        String[] var2 = var0.split("\\[\\]", var1.length + 1);
        String var3 = var2[0];

        int var4;
        for(var4 = 0; var4 < var2.length - 1; ++var4) {
            var3 = var3 + "[" + var1[var4] + "]";
            var3 = var3 + var2[var4 + 1];
        }

        for(var4 = var2.length - 1; var4 < var1.length; ++var4) {
            var3 = var3 + "[" + var1[var4] + "]";
        }

        return var3;
    }

    static {
        DEFAULT_LEVEL = LoggerSimple.Level.ALL;
    }
}