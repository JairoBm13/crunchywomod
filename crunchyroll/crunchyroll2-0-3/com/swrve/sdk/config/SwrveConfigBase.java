// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.config;

import java.net.MalformedURLException;
import com.swrve.sdk.messaging.SwrveOrientation;
import java.net.URL;
import java.io.File;

public abstract class SwrveConfigBase
{
    private String appStore;
    private String appVersion;
    private boolean autoDownloadCampaignsAndResources;
    private long autoShowMessagesMaxDelay;
    private File cacheDir;
    private URL contentUrl;
    private String dbName;
    private int defaultBackgroundColor;
    private URL defaultContentUrl;
    private URL defaultEventsUrl;
    private URL eventsUrl;
    private String language;
    private boolean loadCachedCampaignsAndResourcesOnUIThread;
    private int maxConcurrentDownloads;
    private int maxEventsPerFlush;
    private long maxSqliteDbSize;
    private int minSampleSize;
    private long newSessionInterval;
    private SwrveOrientation orientation;
    private boolean sendQueuedEventsOnResume;
    private boolean talkEnabled;
    private boolean useHttpsForContentUrl;
    private boolean useHttpsForEventsUrl;
    private String userId;
    
    public SwrveConfigBase() {
        this.talkEnabled = true;
        this.maxSqliteDbSize = 1048576L;
        this.maxEventsPerFlush = 50;
        this.maxConcurrentDownloads = 10;
        this.dbName = "swrve.db";
        this.eventsUrl = null;
        this.defaultEventsUrl = null;
        this.useHttpsForEventsUrl = true;
        this.contentUrl = null;
        this.defaultContentUrl = null;
        this.useHttpsForContentUrl = false;
        this.newSessionInterval = 30000L;
        this.appStore = "google";
        this.orientation = SwrveOrientation.Both;
        this.autoDownloadCampaignsAndResources = true;
        this.minSampleSize = 1;
        this.sendQueuedEventsOnResume = true;
        this.autoShowMessagesMaxDelay = 5000L;
        this.loadCachedCampaignsAndResourcesOnUIThread = true;
        this.defaultBackgroundColor = 0;
    }
    
    private static String getSchema(final boolean b) {
        if (b) {
            return "https";
        }
        return "http";
    }
    
    public void generateUrls(final int n) throws MalformedURLException {
        this.defaultEventsUrl = new URL(getSchema(this.useHttpsForEventsUrl) + "://" + n + ".api.swrve.com");
        this.defaultContentUrl = new URL(getSchema(this.useHttpsForContentUrl) + "://" + n + ".content.swrve.com");
    }
    
    public String getAppStore() {
        return this.appStore;
    }
    
    public String getAppVersion() {
        return this.appVersion;
    }
    
    public long getAutoShowMessagesMaxDelay() {
        return this.autoShowMessagesMaxDelay;
    }
    
    public File getCacheDir() {
        return this.cacheDir;
    }
    
    public URL getContentUrl() {
        if (this.contentUrl == null) {
            return this.defaultContentUrl;
        }
        return this.contentUrl;
    }
    
    public String getDbName() {
        return this.dbName;
    }
    
    public int getDefaultBackgroundColor() {
        return this.defaultBackgroundColor;
    }
    
    public URL getEventsUrl() {
        if (this.eventsUrl == null) {
            return this.defaultEventsUrl;
        }
        return this.eventsUrl;
    }
    
    public String getLanguage() {
        return this.language;
    }
    
    public int getMaxEventsPerFlush() {
        return this.maxEventsPerFlush;
    }
    
    public long getMaxSqliteDbSize() {
        return this.maxSqliteDbSize;
    }
    
    public int getMinSampleSize() {
        return this.minSampleSize;
    }
    
    public long getNewSessionInterval() {
        return this.newSessionInterval;
    }
    
    public SwrveOrientation getOrientation() {
        return this.orientation;
    }
    
    public String getUserId() {
        return this.userId;
    }
    
    public boolean isAutoDownloadCampaingsAndResources() {
        return this.autoDownloadCampaignsAndResources;
    }
    
    public boolean isLoadCachedCampaignsAndResourcesOnUIThread() {
        return this.loadCachedCampaignsAndResourcesOnUIThread;
    }
    
    public boolean isSendQueuedEventsOnResume() {
        return this.sendQueuedEventsOnResume;
    }
    
    public boolean isTalkEnabled() {
        return this.talkEnabled;
    }
    
    public SwrveConfigBase setTalkEnabled(final boolean talkEnabled) {
        this.talkEnabled = talkEnabled;
        return this;
    }
}
