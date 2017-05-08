// 
// Decompiled by Procyon v0.5.30
// 

package com.conviva.session;

import com.conviva.StreamerError;
import com.conviva.utils.CallableWithParameters;
import java.util.List;
import java.util.TimerTask;
import java.util.Iterator;
import java.util.HashMap;
import com.conviva.utils.LivePassVersion;
import com.conviva.utils.PlatformUtils;
import com.conviva.utils.Settings;
import java.util.Map;
import com.conviva.monitor.Monitor;
import com.conviva.ConvivaContentInfo;
import com.conviva.platforms.PlatformApi;

public class Session
{
    private PlatformApi _api;
    private String _clv;
    private ConvivaContentInfo _contentInfo;
    private EventQueue _eventQueue;
    private int _heartbeatSequenceNumber;
    private Monitor _monitor;
    private Map<String, String> _nativeReprTags;
    private int _sessionId;
    private Settings _settings;
    private double _startTimeMs;
    private PlatformUtils _utils;
    
    public Session(final Object o, final ConvivaContentInfo contentInfo, final Map<String, Object> map, final boolean b) throws Exception {
        this._contentInfo = null;
        this._nativeReprTags = null;
        this._utils = null;
        this._api = null;
        this._settings = null;
        this._monitor = null;
        this._eventQueue = null;
        this._sessionId = 0;
        this._startTimeMs = 0.0;
        this._heartbeatSequenceNumber = 0;
        this._clv = LivePassVersion.getVersionStr();
        this._utils = PlatformUtils.getInstance();
        this._contentInfo = contentInfo;
        this._sessionId = this._utils.randInt();
        if (this._contentInfo.tags == null) {
            this._utils.err("tags should not be null");
            this._contentInfo.tags = new HashMap<String, String>();
        }
        for (final String s : this._contentInfo.tags.keySet()) {
            if (this._contentInfo.tags.get(s) == null) {
                this._contentInfo.tags.put(s, "null");
            }
        }
        final String platformApiName = this._utils.getSettings().platformApiName;
        while (true) {
            if (platformApiName == null || platformApiName.isEmpty()) {
                break Label_0254;
            }
            try {
                final Class<?> forName = Class.forName(platformApiName);
                if (forName != null) {
                    this._api = (PlatformApi)forName.getConstructor(Object.class).newInstance(o);
                }
                if (this._api == null) {
                    this._api = this._utils.getSettings().platformApi;
                }
                this._nativeReprTags = this._contentInfo.tags;
                this._settings = this._utils.getSettings();
                this._eventQueue = new EventQueue();
                if (!b) {
                    this._monitor = new Monitor(o, this._eventQueue, this._contentInfo, map, this._sessionId, this._api);
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
                continue;
            }
            break;
        }
    }
    
    private void createHBTimer(final boolean b) {
        int heartbeatIntervalMs = 0;
        if (!b) {
            heartbeatIntervalMs = this._settings.heartbeatIntervalMs;
        }
        this._api.createTimer(new TimerTask() {
            @Override
            public void run() {
                Session.this.sendHeartbeat();
            }
        }, heartbeatIntervalMs, this._settings.heartbeatIntervalMs, "sendHeartbeat");
    }
    
    private void encodeAndPostHeartbeat(final Map<String, Object> map) {
        final String jsonEncode = this._utils.jsonEncode(map);
        if (jsonEncode == null) {
            return;
        }
        try {
            this.postHeartbeat(jsonEncode);
        }
        catch (Exception ex) {
            this._utils.log("JSON post error: " + ex.toString());
        }
    }
    
    private Map<String, Object> makeHeartbeat() {
        final List<Map<String, Object>> flushEvents = this._eventQueue.flushEvents();
        final HashMap<String, Double> hashMap = new HashMap<String, Double>();
        hashMap.put("t", "CwsSessionHb");
        hashMap.put("evs", (Double)flushEvents);
        hashMap.put("cid", (Double)this._settings.customerKey);
        hashMap.put("clid", (Double)this._utils.clientId);
        hashMap.put("sid", (Double)this._sessionId);
        hashMap.put("seq", (Double)this._heartbeatSequenceNumber);
        hashMap.put("pver", (Double)this._settings.protocolVersion);
        hashMap.put("clv", (Double)this._clv);
        hashMap.put("iid", (Double)this._settings.clientInstanceId);
        final Map<String, String> platformMetadata = this._utils.getPlatformMetadata();
        if (platformMetadata != null) {
            hashMap.put("pm", (Double)platformMetadata);
        }
        hashMap.put("tags", (Double)this._nativeReprTags);
        this._utils.getClass();
        if ("Android" != null) {
            this._utils.getClass();
            hashMap.put("pt", (Double)"Android");
        }
        if (this._utils._PLATFORM_VER != null) {
            hashMap.put("ptv", (Double)this._utils._PLATFORM_VER);
        }
        if (this._contentInfo.viewerId != null) {
            hashMap.put("vid", (Double)this._contentInfo.viewerId);
        }
        if (this._monitor != null) {
            hashMap.put("an", (Double)this._contentInfo.assetName);
            hashMap.put("lv", (Double)(Object)this._contentInfo.isLive);
            this._monitor.updateHeartbeat((Map<String, Object>)hashMap);
        }
        else {
            hashMap.put("sf", (Double)0);
        }
        if (this._contentInfo.playerName != null) {
            hashMap.put("pn", (Double)this._contentInfo.playerName);
        }
        if (this._settings.sendLogs) {
            hashMap.put("lg", (Double)this._utils.getLogs(this._sessionId));
        }
        final double epochTimeMs = this._utils.epochTimeMs();
        hashMap.put("st", (Double)(int)(epochTimeMs - this._startTimeMs));
        hashMap.put("cts", epochTimeMs / 1000.0);
        ++this._heartbeatSequenceNumber;
        return (Map<String, Object>)hashMap;
    }
    
    private void onHeartbeatResponse(String gatewayUrl) {
        if (gatewayUrl == null) {
            this._utils.log("JSON: Received null response to POST request");
        }
        else {
            this._utils.logConsole("onHeartbeatResponse: data = " + gatewayUrl);
            final Map<String, Object> jsonDecode = this._utils.jsonDecode(gatewayUrl);
            if (jsonDecode == null) {
                this._utils.log("JSON: Received null decoded response");
                return;
            }
            if (jsonDecode.containsKey("clid")) {
                this._utils.setClientIdFromServer(jsonDecode.get("clid").toString());
            }
            final boolean sendLogs = jsonDecode.containsKey("slg") && jsonDecode.get("slg");
            if (sendLogs != this._settings.sendLogs) {
                final PlatformUtils utils = this._utils;
                final StringBuilder append = new StringBuilder().append("Turning ");
                if (sendLogs) {
                    gatewayUrl = "on";
                }
                else {
                    gatewayUrl = "off";
                }
                utils.log(append.append(gatewayUrl).append(" sending of logs").toString());
                this._settings.sendLogs = sendLogs;
            }
            if (jsonDecode.containsKey("hbi")) {
                final long n = jsonDecode.get("hbi") * 1000L;
                if (this._settings.heartbeatIntervalMs != n) {
                    this._utils.log("Received hbIntervalMs from server " + n);
                    this._settings.heartbeatIntervalMs = (int)n;
                    this.createHBTimer(false);
                }
            }
            if (jsonDecode.containsKey("gw")) {
                gatewayUrl = (String)jsonDecode.get("gw");
                if (!this._settings.gatewayUrl.equals(gatewayUrl)) {
                    this._utils.log("Received gatewayUrl from server " + gatewayUrl);
                    this._settings.gatewayUrl = gatewayUrl;
                }
            }
        }
    }
    
    private void postHeartbeat(final String s) {
        final String string = this._settings.gatewayUrl + this._settings.gatewayPath;
        this._utils.logSession("Send HB[" + (this._heartbeatSequenceNumber - 1) + "]", this._sessionId);
        this._utils.logConsole("heartbeat to be sent: " + s);
        this._utils.httpRequest(true, string, s, "application/json", new CallableWithParameters.With1<String>() {
            public void exec(final String s) {
                Session.this.onHeartbeatResponse(s);
            }
        });
    }
    
    private void sendHeartbeat() {
        boolean b = false;
        if (this._eventQueue.size() > 0) {
            b = true;
        }
        else if (this._monitor == null) {
            return;
        }
        if (!b && (this._utils.inSleepingMode() || !this._utils.isVisible())) {
            this._utils.log("Do not send out heartbeat: player is sleeping or not visible");
        }
        else {
            final Map<String, Object> heartbeat = this.makeHeartbeat();
            if (heartbeat != null) {
                this.encodeAndPostHeartbeat(heartbeat);
            }
        }
    }
    
    public void adEnd() {
        if (this._monitor != null) {
            this._monitor.adEnd();
        }
    }
    
    public void adStart() {
        if (this._monitor != null) {
            this._monitor.adStart();
        }
    }
    
    public void attachStreamer(final Object o) throws Exception {
        this._monitor.attachStreamer(o);
    }
    
    public void cleanup() {
        this._utils.log("Schedule the last hb before session cleanup");
        if (this._monitor != null) {
            this.sendSessionEndEvent();
        }
        this.sendHeartbeat();
        this._api.cleanup();
        this._api = null;
        if (this._monitor != null) {
            this._monitor.cleanup();
            this._monitor = null;
        }
    }
    
    public void pauseMonitor() throws Exception {
        this._monitor.pauseMonitor();
    }
    
    public void playerPaused(final boolean b) {
        this._monitor.playerPaused(b);
    }
    
    public void reportError(final StreamerError streamerError) {
        if (this._monitor != null) {
            this._monitor.OnError(streamerError);
        }
    }
    
    public void sendCustomEvent(final String s, final Map<String, Object> map) {
        this._utils.log("cws.sendEvent " + s);
        final HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put("name", s);
        hashMap.put("attr", map);
        this._eventQueue.enqueueEvent("CwsCustomEvent", hashMap, (int)(this._utils.epochTimeMs() - this._startTimeMs));
    }
    
    public void sendSessionEndEvent() {
        this._utils.log("cws.sendSessionEndEvent()");
        this._eventQueue.enqueueEvent("CwsSessionEndEvent", new HashMap<String, Object>(), (int)(this._utils.epochTimeMs() - this._startTimeMs));
    }
    
    public void setBitrate(final int bitrate) {
        this._monitor.setBitrate(bitrate);
    }
    
    public void setCdnNameOrResource(final String s) {
        if (this._monitor != null) {
            this._monitor.SetStream(-1, s, s);
        }
    }
    
    public void setMetadata(final Map<String, String> map) {
        this._utils.log("cws.setMetadata");
        if (this._monitor != null) {
            this._monitor.OnMetadata(map);
        }
    }
    
    public void start() {
        this._utils.log("Session.start assetName=" + this._contentInfo.assetName);
        this._startTimeMs = this._utils.epochTimeMs();
        try {
            if (this._monitor != null) {
                this._monitor.start(this._startTimeMs);
            }
            this._heartbeatSequenceNumber = 0;
            this.createHBTimer(true);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
