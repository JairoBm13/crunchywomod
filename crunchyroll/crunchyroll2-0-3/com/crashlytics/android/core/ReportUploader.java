// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.core;

import java.util.Iterator;
import io.fabric.sdk.android.services.common.BackgroundPriorityRunnable;
import io.fabric.sdk.android.Logger;
import io.fabric.sdk.android.services.common.ApiKey;
import java.util.LinkedList;
import io.fabric.sdk.android.Fabric;
import java.util.List;
import java.util.Collections;
import java.io.File;
import java.io.FilenameFilter;
import java.util.Map;

class ReportUploader
{
    static final Map<String, String> HEADER_INVALID_CLS_FILE;
    private static final short[] RETRY_INTERVALS;
    private static final FilenameFilter crashFileFilter;
    private final CreateReportSpiCall createReportCall;
    private final Object fileAccessLock;
    private Thread uploadThread;
    
    static {
        crashFileFilter = new FilenameFilter() {
            @Override
            public boolean accept(final File file, final String s) {
                return s.endsWith(".cls") && !s.contains("Session");
            }
        };
        HEADER_INVALID_CLS_FILE = Collections.singletonMap("X-CRASHLYTICS-INVALID-SESSION", "1");
        RETRY_INTERVALS = new short[] { 10, 20, 30, 60, 120, 300 };
    }
    
    public ReportUploader(final CreateReportSpiCall createReportCall) {
        this.fileAccessLock = new Object();
        if (createReportCall == null) {
            throw new IllegalArgumentException("createReportCall must not be null.");
        }
        this.createReportCall = createReportCall;
    }
    
    List<Report> findReports() {
        Fabric.getLogger().d("CrashlyticsCore", "Checking for crash reports...");
        Object fileAccessLock = this.fileAccessLock;
        synchronized (fileAccessLock) {
            final File[] listFiles = CrashlyticsCore.getInstance().getSdkDirectory().listFiles(ReportUploader.crashFileFilter);
            // monitorexit(fileAccessLock)
            fileAccessLock = new LinkedList<SessionReport>();
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                final File file = listFiles[i];
                Fabric.getLogger().d("CrashlyticsCore", "Found crash report " + file.getPath());
                ((List<SessionReport>)fileAccessLock).add(new SessionReport(file));
            }
        }
        if (((List)fileAccessLock).isEmpty()) {
            Fabric.getLogger().d("CrashlyticsCore", "No reports found.");
        }
        return (List<Report>)fileAccessLock;
    }
    
    boolean forceUpload(final Report report) {
        final boolean b = false;
        synchronized (this.fileAccessLock) {
            try {
                final boolean invoke = this.createReportCall.invoke(new CreateReportRequest(new ApiKey().getValue(CrashlyticsCore.getInstance().getContext()), report));
                final Logger logger = Fabric.getLogger();
                final StringBuilder append = new StringBuilder().append("Crashlytics report upload ");
                String s;
                if (invoke) {
                    s = "complete: ";
                }
                else {
                    s = "FAILED: ";
                }
                logger.i("CrashlyticsCore", append.append(s).append(report.getFileName()).toString());
                boolean b2 = b;
                if (invoke) {
                    report.remove();
                    b2 = true;
                }
                return b2;
            }
            catch (Exception ex) {
                Fabric.getLogger().e("CrashlyticsCore", "Error occurred sending report " + report, ex);
                return b;
            }
        }
    }
    
    public void uploadReports(final float n) {
        synchronized (this) {
            if (this.uploadThread == null) {
                (this.uploadThread = new Thread(new Worker(n), "Crashlytics Report Uploader")).start();
            }
        }
    }
    
    private class Worker extends BackgroundPriorityRunnable
    {
        private final float delay;
        
        Worker(final float delay) {
            this.delay = delay;
        }
        
        private void attemptUploadWithRetry() {
            Fabric.getLogger().d("CrashlyticsCore", "Starting report processing in " + this.delay + " second(s)...");
            Label_0057: {
                if (this.delay <= 0.0f) {
                    break Label_0057;
                }
                while (true) {
                    CrashlyticsCore instance;
                    List<Report> list;
                    try {
                        Thread.sleep((long)(this.delay * 1000.0f));
                        instance = CrashlyticsCore.getInstance();
                        final CrashlyticsUncaughtExceptionHandler handler = instance.getHandler();
                        list = ReportUploader.this.findReports();
                        if (handler.isHandlingException()) {
                            return;
                        }
                    }
                    catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                    if (!list.isEmpty() && !instance.canSendWithUserApproval()) {
                        Fabric.getLogger().d("CrashlyticsCore", "User declined to send. Removing " + list.size() + " Report(s).");
                        final Iterator<Report> iterator = list.iterator();
                        while (iterator.hasNext()) {
                            iterator.next().remove();
                        }
                        return;
                    }
                    int n = 0;
                    while (!list.isEmpty() && !CrashlyticsCore.getInstance().getHandler().isHandlingException()) {
                        Fabric.getLogger().d("CrashlyticsCore", "Attempting to send " + list.size() + " report(s)");
                        final Iterator<Report> iterator2 = list.iterator();
                        while (iterator2.hasNext()) {
                            ReportUploader.this.forceUpload(iterator2.next());
                        }
                        final List<Report> list2 = list = ReportUploader.this.findReports();
                        if (!list2.isEmpty()) {
                            final long n2 = ReportUploader.RETRY_INTERVALS[Math.min(n, ReportUploader.RETRY_INTERVALS.length - 1)];
                            Fabric.getLogger().d("CrashlyticsCore", "Report submisson: scheduling delayed retry in " + n2 + " seconds");
                            try {
                                Thread.sleep(1000L * n2);
                                ++n;
                                list = list2;
                            }
                            catch (InterruptedException ex2) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    }
                }
            }
        }
        
        public void onRun() {
            while (true) {
                try {
                    this.attemptUploadWithRetry();
                    ReportUploader.this.uploadThread = null;
                }
                catch (Exception ex) {
                    Fabric.getLogger().e("CrashlyticsCore", "An unexpected error occurred while attempting to upload crash reports.", ex);
                    continue;
                }
                break;
            }
        }
    }
}
