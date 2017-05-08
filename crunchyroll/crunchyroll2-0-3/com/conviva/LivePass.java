// 
// Decompiled by Procyon v0.5.30
// 

package com.conviva;

import java.util.HashMap;
import com.conviva.monitor.PlayerStates;
import com.conviva.utils.Utils;
import android.content.Context;
import java.util.concurrent.Callable;
import com.conviva.session.Session;
import java.util.Map;
import com.conviva.utils.PlatformUtils;
import com.conviva.utils.Settings;
import com.conviva.session.SessionFactory;

public class LivePass
{
    public static final String OPTION_EXPLICIT_PLAYER_PAUSED = "explicitPlayerPaused";
    public static final String OPTION_EXTERNAL_BITRATE_REPORTING = "externalBitrateReporting";
    private static int _globalSessionId;
    private static SessionFactory _sessionFactory;
    private static Settings _settings;
    private static boolean _toggleTracesSet;
    private static boolean _toggleTracesSetValue;
    private static PlatformUtils _utils;
    private static boolean readyState;
    public final int STREAM_SELECTION_FAILURE;
    public final int STREAM_SELECTION_SUCC;
    public final int STREAM_SELECTION_TIMEOUT;
    
    static {
        LivePass.readyState = false;
        LivePass._settings = null;
        LivePass._utils = null;
        LivePass._sessionFactory = null;
        LivePass._toggleTracesSet = false;
        LivePass._toggleTracesSetValue = false;
        LivePass._globalSessionId = -1;
    }
    
    public LivePass() {
        this.STREAM_SELECTION_SUCC = 0;
        this.STREAM_SELECTION_FAILURE = 1;
        this.STREAM_SELECTION_TIMEOUT = 2;
    }
    
    public static void adEnd(final int n) {
        if (!LivePass.readyState && LivePass._utils != null) {
            LivePass._utils.ping("adEnd before LivePass.init");
        }
        final Session session = LivePass._sessionFactory.getSession(n);
        if (session == null) {
            LivePass._utils.log("LivePass.adEnd(): session not found");
            return;
        }
        session.adEnd();
    }
    
    public static void adStart(final int n) {
        if (!LivePass.readyState && LivePass._utils != null) {
            LivePass._utils.ping("adStart before LivePass.init");
        }
        final Session session = LivePass._sessionFactory.getSession(n);
        if (session == null) {
            LivePass._utils.log("LivePass.adStart(): session not found");
            return;
        }
        session.adStart();
    }
    
    public static void attachStreamer(final int n, final Object o) throws Exception {
        final Session session = LivePass._sessionFactory.getSession(n);
        if (session == null) {
            LivePass._utils.log("LivePass.attachStreamer(): session not found");
            return;
        }
        session.attachStreamer(o);
    }
    
    public static void cleanup() {
        if (LivePass._utils != null) {
            LivePass._utils.runProtected((Callable<Object>)new Callable<Void>() {
                @Override
                public Void call() throws Exception {
                    if (LivePass._sessionFactory != null) {
                        LivePass._sessionFactory.cleanup();
                    }
                    LivePass._sessionFactory = null;
                    LivePass._globalSessionId = -1;
                    if (LivePass._utils != null) {
                        LivePass._utils.cleanup();
                    }
                    LivePass._utils = null;
                    LivePass.readyState = false;
                    return null;
                }
            }, "LivePass.cleanup");
        }
    }
    
    public static void cleanupSession(final int n) {
        if (!LivePass.readyState) {
            return;
        }
        LivePass._utils.runProtected((Callable<Object>)new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                LivePass._sessionFactory.cleanupSession(n);
                return null;
            }
        }, "Livepass.cleanupSession");
    }
    
    public static int createSession(final Object o, final ConvivaContentInfo convivaContentInfo) throws Exception {
        return createSessionWorker(o, convivaContentInfo, null, false);
    }
    
    public static int createSession(final Object o, final ConvivaContentInfo convivaContentInfo, final Map<String, Object> map) throws Exception {
        return createSessionWorker(o, convivaContentInfo, map, false);
    }
    
    private static int createSessionWorker(final Object o, final ConvivaContentInfo convivaContentInfo, final Map<String, Object> map, final boolean b) throws Exception {
        if (!LivePass.readyState) {
            if (LivePass._utils != null) {
                LivePass._utils.ping("createSession before LivePass.init");
            }
            throw new Exception("createSession before LivePass.init");
        }
        if (LivePass._settings.customerKey == null) {
            throw new Exception("Cannot create session: customerKey is null");
        }
        final int sessionId = LivePass._sessionFactory.newSessionId();
        LivePass._utils.runProtected((Callable<Object>)new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                LivePass._sessionFactory.makeSession(o, convivaContentInfo, map, sessionId, b);
                LivePass._utils.log("LivePass Session Created");
                return null;
            }
        }, "LivePass.createSession");
        return sessionId;
    }
    
    public static void init(final String s, final Context context) throws Exception {
        initWithSettings(s, null, context);
    }
    
    public static void initWithSettings(final String customerKey, final Map<String, Object> map, final Context context) throws Exception {
        if (customerKey == null || customerKey.length() == 0) {
            throw new Exception("invalid customerKey: " + customerKey);
        }
        if (LivePass.readyState) {
            return;
        }
        (LivePass._utils = Utils.CreateUtils(map, context)).startFetchClientId();
        LivePass._settings = LivePass._utils.getSettings();
        LivePass._settings.clientInstanceId = LivePass._utils.randUInt();
        LivePass._settings.customerKey = customerKey;
        LivePass._settings.sendLogs = false;
        LivePass._utils.runProtected((Callable<Object>)new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                if (LivePass._toggleTracesSet) {
                    LivePass._settings.enableLogging = LivePass._toggleTracesSetValue;
                }
                LivePass._utils.log("LivePass.init url=" + LivePass._settings.gatewayUrl + ", customerKey=" + LivePass._settings.customerKey);
                PlayerStates.init();
                LivePass._sessionFactory = new SessionFactory();
                LivePass.readyState = true;
                return null;
            }
        }, "LivePass.init");
    }
    
    public static void pauseMonitor(final int n) throws Exception {
        final Session session = LivePass._sessionFactory.getSession(n);
        if (session == null) {
            LivePass._utils.log("LivePass.pauseSession(): session not found");
            return;
        }
        session.pauseMonitor();
    }
    
    public static void playerPaused(final int n, final boolean b) {
        final Session session = LivePass._sessionFactory.getSession(n);
        if (session == null) {
            LivePass._utils.log("LivePass.playerPaused(): session not found");
            return;
        }
        session.playerPaused(b);
    }
    
    public static void reportError(final int n, final String s, final int n2) {
        final Session session = LivePass._sessionFactory.getSession(n);
        if (session == null) {
            LivePass._utils.log("LivePass.reportError(): session not found");
            return;
        }
        session.reportError(StreamerError.makeUnscopedError(s, n2));
    }
    
    public static void resumeMonitor(final int n, final Object o) throws Exception {
        final Session session = LivePass._sessionFactory.getSession(n);
        if (session == null) {
            LivePass._utils.log("LivePass.resumeSession(): session not found");
            return;
        }
        session.attachStreamer(o);
    }
    
    public static void sendEvent(final String s, final Map<String, Object> map) {
        if (!LivePass.readyState) {
            if (LivePass._utils != null) {
                LivePass._utils.ping("sendGlobalEvent before LivePass.init");
            }
            return;
        }
        LivePass._utils.runProtected((Callable<Object>)new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                if (LivePass._globalSessionId < 0) {
                    LivePass._globalSessionId = createSessionWorker(null, new ConvivaContentInfo("", new HashMap<String, String>()), null, true);
                }
                LivePass.sendSessionEvent(LivePass._globalSessionId, s, map);
                return null;
            }
        }, "LivePass.sendGlobalEvent");
    }
    
    public static void sendSessionEvent(final int n, final String s, final Map<String, Object> map) {
        if (!LivePass.readyState) {
            if (LivePass._utils != null) {
                LivePass._utils.ping("sendEvent before LivePass.init");
            }
            return;
        }
        LivePass._utils.runProtected((Callable<Object>)new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                LivePass._sessionFactory.getSession(n).sendCustomEvent(s, map);
                return null;
            }
        }, "LivePass.sendSessionEvent");
    }
    
    public static void setBitrate(final int n, final int n2) {
        if (!LivePass.readyState) {
            if (LivePass._utils != null) {
                LivePass._utils.ping("setBitrate before LivePass.init");
            }
            return;
        }
        LivePass._utils.runProtected((Callable<Object>)new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                LivePass._sessionFactory.getSession(n).setBitrate(n2);
                return null;
            }
        }, "setBitrate");
    }
    
    public static void setCdnNameOrResource(final int n, final String cdnNameOrResource) {
        final Session session = LivePass._sessionFactory.getSession(n);
        if (session == null) {
            LivePass._utils.log("LivePass.setResource(): session not found");
            return;
        }
        session.setCdnNameOrResource(cdnNameOrResource);
    }
    
    public static void setCurrentStreamMetadata(final int n, final Map<String, String> map) {
        if (!LivePass.readyState) {
            if (LivePass._utils != null) {
                LivePass._utils.ping("setCurrentMetadata before LivePass.init");
            }
            return;
        }
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.putAll((Map<?, ?>)map);
        if (hashMap.containsKey("duration")) {
            hashMap.put("duration", Integer.toString(LivePass._utils.parseInt((String)hashMap.get("duration"), -1) * 1000));
        }
        LivePass._utils.runProtected((Callable<Object>)new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                LivePass._sessionFactory.getSession(n).setMetadata(hashMap);
                return null;
            }
        }, "setMetadata");
    }
    
    public static void toggleTraces(final boolean b) {
        LivePass._toggleTracesSet = true;
        if (LivePass._settings != null) {
            LivePass._settings.enableLogging = b;
            return;
        }
        LivePass._toggleTracesSetValue = b;
    }
}
