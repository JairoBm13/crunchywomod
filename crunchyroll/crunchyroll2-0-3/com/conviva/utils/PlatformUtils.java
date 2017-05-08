// 
// Decompiled by Procyon v0.5.30
// 

package com.conviva.utils;

import java.util.concurrent.Callable;
import org.json.simple.JSONValue;
import java.util.Map;
import java.util.Date;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.Random;
import java.util.List;

public abstract class PlatformUtils
{
    protected static PlatformUtils _instance;
    private static List<String> logBuffer;
    protected final String DEFAULT_CLIENT_ID;
    private final int MAX_SIZE_LOGBUFFER;
    public final String _PLATFORM;
    public String _PLATFORM_VER;
    protected final String _TAG;
    private boolean _isSendingPing;
    private String _pingUrl;
    private final Random _rand;
    protected int _referenceCount;
    protected Settings _settings;
    public String clientId;
    
    static {
        PlatformUtils._instance = null;
        PlatformUtils.logBuffer = new LinkedList<String>();
    }
    
    protected PlatformUtils(final Settings settings) throws Exception {
        this._referenceCount = 0;
        this._settings = null;
        this.DEFAULT_CLIENT_ID = "0";
        this.clientId = "0";
        this._pingUrl = null;
        this._isSendingPing = false;
        this._PLATFORM = "Android";
        this._PLATFORM_VER = null;
        this._TAG = "CONVIVA";
        this.MAX_SIZE_LOGBUFFER = 32;
        this._rand = new Random();
        this._settings = settings;
        this._referenceCount = 1;
        this._pingUrl = null;
        this._isSendingPing = false;
        PlatformUtils._instance = this;
    }
    
    public static PlatformUtils getInstance() throws Exception {
        if (PlatformUtils._instance == null) {
            throw new Exception("CreateUtils module has not been called");
        }
        return PlatformUtils._instance;
    }
    
    private void initPing() {
        if (this._pingUrl == null) {
            String string = "";
            if (this.clientId != null) {
                string = "&uuid=" + this.clientId;
            }
            this._pingUrl = this._settings.pingUrl + "?comp=" + this._settings.pingComponentName + "&clv=" + LivePassVersion.getVersion3Str() + "&cid=" + this._settings.customerKey + string;
        }
    }
    
    private void onUncaughtException(final String s, final Exception ex) {
        try {
            this.ping("Uncaught exception: " + ex.toString());
        }
        catch (Exception ex2) {
            this.err("Caught exception while sending ping: " + ex2.toString());
        }
    }
    
    private String urlEncodeString(final String s) throws UnsupportedEncodingException {
        return URLEncoder.encode(s, "UTF-8");
    }
    
    public void cleanup() {
        --this._referenceCount;
        if (this._referenceCount > 0) {
            return;
        }
        this._settings = null;
        PlatformUtils._instance = null;
    }
    
    public abstract void deleteLocalData();
    
    public double epochTimeMs() {
        return new Date().getTime();
    }
    
    public void err(final String s) {
        this.log("ERROR: " + s);
    }
    
    public List<String> getLogs(final int n) {
        final List<String> logBuffer = PlatformUtils.logBuffer;
        PlatformUtils.logBuffer = new LinkedList<String>();
        return logBuffer;
    }
    
    public abstract Map<String, String> getPlatformMetadata();
    
    public Settings getSettings() {
        return this._settings;
    }
    
    public abstract void httpRequest(final boolean p0, final String p1, final String p2, final String p3, final CallableWithParameters.With1<String> p4);
    
    public abstract boolean inSleepingMode();
    
    public abstract boolean isVisible();
    
    public Map<String, Object> jsonDecode(final String s) {
        try {
            return (Map<String, Object>)JSONValue.parse(s);
        }
        catch (Exception ex) {
            this.err("Failed to decode json string: " + ex.toString());
            return null;
        }
    }
    
    public String jsonEncode(final Map<String, Object> map) {
        try {
            return JSONValue.toJSONString(map);
        }
        catch (Exception ex) {
            this.err("Failed to encode json object: " + ex.toString());
            return null;
        }
    }
    
    public void log(String string) {
        this.logConsole(string);
        string = "[" + String.format("%.3f", this.epochTimeMs() / 1000.0) + "] " + string;
        if (PlatformUtils.logBuffer.size() >= 32) {
            PlatformUtils.logBuffer.remove(0);
        }
        PlatformUtils.logBuffer.add(string);
    }
    
    public abstract void logConsole(final String p0);
    
    public void logSession(final String s, final int n) {
        this.log("sid=" + n + " " + s);
    }
    
    public int parseInt(final String s, final int n) {
        try {
            return Integer.parseInt(s);
        }
        catch (Exception ex) {
            return n;
        }
    }
    
    public void ping(String string) {
        try {
            if (this._isSendingPing) {
                return;
            }
            this._isSendingPing = true;
            this.initPing();
            string = this._pingUrl + "&d=" + this.urlEncodeString(string);
            this.err("Ping: " + string);
            this.httpRequest(false, string, null, null, null);
            this._isSendingPing = false;
        }
        catch (Exception ex) {
            this._isSendingPing = false;
            this.err("Failed to send ping");
        }
    }
    
    public int randInt() {
        return this._rand.nextInt();
    }
    
    public int randUInt() {
        final int abs = Math.abs(this.randInt());
        if (abs >= 0) {
            return abs;
        }
        return 0;
    }
    
    public <V> void runProtected(final Callable<V> callable, final String s) {
        try {
            callable.call();
        }
        catch (Exception ex) {
            this.onUncaughtException(s, ex);
        }
    }
    
    public abstract void setClientIdFromServer(final String p0);
    
    public abstract void startFetchClientId();
}
