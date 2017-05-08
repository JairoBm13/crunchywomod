// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.events;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import io.fabric.sdk.android.services.common.CommonUtils;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.ScheduledExecutorService;
import android.content.Context;

public abstract class EnabledEventsStrategy<T> implements EventsStrategy<T>
{
    protected final Context context;
    final ScheduledExecutorService executorService;
    protected final EventsFilesManager<T> filesManager;
    volatile int rolloverIntervalSeconds;
    final AtomicReference<ScheduledFuture<?>> scheduledRolloverFutureRef;
    
    public EnabledEventsStrategy(final Context context, final ScheduledExecutorService executorService, final EventsFilesManager<T> filesManager) {
        this.rolloverIntervalSeconds = -1;
        this.context = context;
        this.executorService = executorService;
        this.filesManager = filesManager;
        this.scheduledRolloverFutureRef = new AtomicReference<ScheduledFuture<?>>();
    }
    
    @Override
    public void cancelTimeBasedFileRollOver() {
        if (this.scheduledRolloverFutureRef.get() != null) {
            CommonUtils.logControlled(this.context, "Cancelling time-based rollover because no events are currently being generated.");
            this.scheduledRolloverFutureRef.get().cancel(false);
            this.scheduledRolloverFutureRef.set(null);
        }
    }
    
    protected void configureRollover(final int rolloverIntervalSeconds) {
        this.rolloverIntervalSeconds = rolloverIntervalSeconds;
        this.scheduleTimeBasedFileRollOver(0L, this.rolloverIntervalSeconds);
    }
    
    @Override
    public void deleteAllEvents() {
        this.filesManager.deleteAllEventsFiles();
    }
    
    public void recordEvent(final T t) {
        CommonUtils.logControlled(this.context, t.toString());
        while (true) {
            try {
                this.filesManager.writeEvent(t);
                this.scheduleTimeBasedRollOverIfNeeded();
            }
            catch (IOException ex) {
                CommonUtils.logControlledError(this.context, "Failed to write event.", ex);
                continue;
            }
            break;
        }
    }
    
    @Override
    public boolean rollFileOver() {
        try {
            return this.filesManager.rollFileOver();
        }
        catch (IOException ex) {
            CommonUtils.logControlledError(this.context, "Failed to roll file over.", ex);
            return false;
        }
    }
    
    void scheduleTimeBasedFileRollOver(final long n, final long n2) {
        Label_0087: {
            if (this.scheduledRolloverFutureRef.get() != null) {
                break Label_0087;
            }
            int n3 = 1;
            while (true) {
                if (n3 == 0) {
                    return;
                }
                final TimeBasedFileRollOverRunnable timeBasedFileRollOverRunnable = new TimeBasedFileRollOverRunnable(this.context, this);
                CommonUtils.logControlled(this.context, "Scheduling time based file roll over every " + n2 + " seconds");
                try {
                    this.scheduledRolloverFutureRef.set(this.executorService.scheduleAtFixedRate(timeBasedFileRollOverRunnable, n, n2, TimeUnit.SECONDS));
                    return;
                    n3 = 0;
                    continue;
                }
                catch (RejectedExecutionException ex) {
                    CommonUtils.logControlledError(this.context, "Failed to schedule time based file roll over", ex);
                }
                break;
            }
        }
    }
    
    public void scheduleTimeBasedRollOverIfNeeded() {
        int n;
        if (this.rolloverIntervalSeconds != -1) {
            n = 1;
        }
        else {
            n = 0;
        }
        if (n != 0) {
            this.scheduleTimeBasedFileRollOver(this.rolloverIntervalSeconds, this.rolloverIntervalSeconds);
        }
    }
    
    void sendAndCleanUpIfSuccess() {
        final FilesSender filesSender = this.getFilesSender();
        if (filesSender == null) {
            CommonUtils.logControlled(this.context, "skipping files send because we don't yet know the target endpoint");
        }
        else {
            CommonUtils.logControlled(this.context, "Sending all files");
            int n = 0;
            List<File> list = this.filesManager.getBatchOfFilesToSend();
        Label_0137_Outer:
            while (true) {
                int n2 = n;
                int n3 = n;
                while (true) {
                    try {
                        if (list.size() > 0) {
                            n3 = n;
                            CommonUtils.logControlled(this.context, String.format(Locale.US, "attempt to send batch of %d files", list.size()));
                            n3 = n;
                            final boolean send = filesSender.send(list);
                            n2 = n;
                            if (send) {
                                n3 = n;
                                n2 = (n3 = n + list.size());
                                this.filesManager.deleteSentFiles(list);
                            }
                            if (send) {
                                n3 = n2;
                                list = this.filesManager.getBatchOfFilesToSend();
                                n = n2;
                                continue Label_0137_Outer;
                            }
                        }
                        if (n2 == 0) {
                            this.filesManager.deleteOldestInRollOverIfOverMax();
                            return;
                        }
                        break;
                    }
                    catch (Exception ex) {
                        CommonUtils.logControlledError(this.context, "Failed to send batch of analytics files to server: " + ex.getMessage(), ex);
                        n2 = n3;
                        continue;
                    }
                    break;
                }
            }
        }
    }
    
    @Override
    public void sendEvents() {
        this.sendAndCleanUpIfSuccess();
    }
}
