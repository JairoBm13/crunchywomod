// 
// Decompiled by Procyon v0.5.30
// 

package com.conviva.utils;

import java.util.Iterator;
import java.util.Map;
import com.conviva.platforms.PlatformApi;

public class Settings
{
    public static final int POLL_STREAMER_INTERVAL_MS = 200;
    public static final int POLL_STREAMER_WINDOW_SIZE_MS = 1000;
    public int clientInstanceId;
    public String customerKey;
    public boolean enableLogging;
    public String gatewayPath;
    public String gatewayUrl;
    public int heartbeatIntervalMs;
    public int loadDataTimeoutMs;
    public int maxEventsPerHeartbeat;
    public String pingComponentName;
    public String pingUrl;
    public PlatformApi platformApi;
    public String platformApiName;
    public String protocolVersion;
    public boolean sendLogs;
    
    public Settings() {
        this.heartbeatIntervalMs = 20000;
        this.maxEventsPerHeartbeat = 10;
        this.customerKey = null;
        this.gatewayUrl = "https://cws.conviva.com";
        this.gatewayPath = "/0/wsg";
        this.protocolVersion = "1.7";
        this.clientInstanceId = 0;
        this.platformApiName = "com.conviva.platforms.AndroidApi";
        this.platformApi = null;
        this.loadDataTimeoutMs = 10000;
        this.enableLogging = false;
        this.sendLogs = false;
        this.pingComponentName = "javacws";
        this.pingUrl = "https://pings.conviva.com/ping.ping";
    }
    
    public void changeSettings(final Map<String, Object> map) throws Exception {
        if (map != null) {
            for (final Map.Entry<String, Object> entry : map.entrySet()) {
                final String s = entry.getKey();
                final Boolean value = entry.getValue();
                if (s.equals("platformApiName")) {
                    this.platformApiName = (String)value;
                }
                else if (s.equals("platformApi")) {
                    this.platformApi = (PlatformApi)value;
                }
                else if (s.equals("gatewayUrl")) {
                    this.gatewayUrl = (String)value;
                }
                else if (s.equals("pingUrl")) {
                    this.pingUrl = (String)value;
                }
                else if (s.equals("heartbeatIntervalMs")) {
                    this.heartbeatIntervalMs = (Integer)(Object)value;
                }
                else {
                    if (!s.equals("enableLogging")) {
                        throw new Exception("Unsupported settings: " + s);
                    }
                    this.enableLogging = value;
                }
            }
        }
    }
}
