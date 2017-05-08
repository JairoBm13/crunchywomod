// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.answers;

import io.fabric.sdk.android.services.settings.AnalyticsSettingsData;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.common.ExecutorUtils;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import io.fabric.sdk.android.Kit;
import java.util.concurrent.ScheduledExecutorService;
import android.content.Context;
import io.fabric.sdk.android.services.events.EventsStorageListener;

class AnswersEventsHandler implements EventsStorageListener
{
    private final Context context;
    final ScheduledExecutorService executor;
    private final AnswersFilesManagerProvider filesManagerProvider;
    private final Kit kit;
    private final SessionMetadataCollector metadataCollector;
    private final HttpRequestFactory requestFactory;
    SessionAnalyticsManagerStrategy strategy;
    
    public AnswersEventsHandler(final Kit kit, final Context context, final AnswersFilesManagerProvider answersFilesManagerProvider, final SessionMetadataCollector sessionMetadataCollector, final HttpRequestFactory httpRequestFactory) {
        this(kit, context, answersFilesManagerProvider, sessionMetadataCollector, httpRequestFactory, ExecutorUtils.buildSingleThreadScheduledExecutorService("Answers Events Handler"));
    }
    
    AnswersEventsHandler(final Kit kit, final Context context, final AnswersFilesManagerProvider filesManagerProvider, final SessionMetadataCollector metadataCollector, final HttpRequestFactory requestFactory, final ScheduledExecutorService executor) {
        this.strategy = new DisabledSessionAnalyticsManagerStrategy();
        this.kit = kit;
        this.context = context;
        this.filesManagerProvider = filesManagerProvider;
        this.metadataCollector = metadataCollector;
        this.requestFactory = requestFactory;
        this.executor = executor;
    }
    
    private void executeAsync(final Runnable runnable) {
        try {
            this.executor.submit(runnable);
        }
        catch (Exception ex) {
            Fabric.getLogger().e("Answers", "Failed to submit events task", ex);
        }
    }
    
    private void executeSync(final Runnable runnable) {
        try {
            this.executor.submit(runnable).get();
        }
        catch (Exception ex) {
            Fabric.getLogger().e("Answers", "Failed to run events task", ex);
        }
    }
    
    public void disable() {
        this.executeAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    final SessionAnalyticsManagerStrategy strategy = AnswersEventsHandler.this.strategy;
                    AnswersEventsHandler.this.strategy = new DisabledSessionAnalyticsManagerStrategy();
                    strategy.deleteAllEvents();
                }
                catch (Exception ex) {
                    Fabric.getLogger().e("Answers", "Failed to disable events", ex);
                }
            }
        });
    }
    
    public void enable() {
        this.executeAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    final SessionEventMetadata metadata = AnswersEventsHandler.this.metadataCollector.getMetadata();
                    final SessionAnalyticsFilesManager analyticsFilesManager = AnswersEventsHandler.this.filesManagerProvider.getAnalyticsFilesManager();
                    analyticsFilesManager.registerRollOverListener(AnswersEventsHandler.this);
                    AnswersEventsHandler.this.strategy = new EnabledSessionAnalyticsManagerStrategy(AnswersEventsHandler.this.kit, AnswersEventsHandler.this.context, AnswersEventsHandler.this.executor, analyticsFilesManager, AnswersEventsHandler.this.requestFactory, metadata);
                }
                catch (Exception ex) {
                    Fabric.getLogger().e("Answers", "Failed to enable events", ex);
                }
            }
        });
    }
    
    @Override
    public void onRollOver(final String s) {
        this.executeAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    AnswersEventsHandler.this.strategy.sendEvents();
                }
                catch (Exception ex) {
                    Fabric.getLogger().e("Answers", "Failed to send events files", ex);
                }
            }
        });
    }
    
    void processEvent(final SessionEvent.Builder builder, final boolean b, final boolean b2) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    AnswersEventsHandler.this.strategy.processEvent(builder);
                    if (b2) {
                        AnswersEventsHandler.this.strategy.rollFileOver();
                    }
                }
                catch (Exception ex) {
                    Fabric.getLogger().e("Answers", "Failed to process event", ex);
                }
            }
        };
        if (b) {
            this.executeSync(runnable);
            return;
        }
        this.executeAsync(runnable);
    }
    
    public void processEventAsync(final SessionEvent.Builder builder) {
        this.processEvent(builder, false, false);
    }
    
    public void processEventAsyncAndFlush(final SessionEvent.Builder builder) {
        this.processEvent(builder, false, true);
    }
    
    public void processEventSync(final SessionEvent.Builder builder) {
        this.processEvent(builder, true, false);
    }
    
    public void setAnalyticsSettingsData(final AnalyticsSettingsData analyticsSettingsData, final String s) {
        this.executeAsync(new Runnable() {
            @Override
            public void run() {
                try {
                    AnswersEventsHandler.this.strategy.setAnalyticsSettingsData(analyticsSettingsData, s);
                }
                catch (Exception ex) {
                    Fabric.getLogger().e("Answers", "Failed to set analytics settings data", ex);
                }
            }
        });
    }
}
