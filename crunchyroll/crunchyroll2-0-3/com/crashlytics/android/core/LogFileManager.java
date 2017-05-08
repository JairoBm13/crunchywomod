// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.core;

import io.fabric.sdk.android.Fabric;
import java.util.Set;
import io.fabric.sdk.android.services.common.CommonUtils;
import java.io.File;
import android.content.Context;

class LogFileManager
{
    private static final NoopLogStore NOOP_LOG_STORE;
    private final Context context;
    private FileLogStore currentLog;
    private final File logFileDir;
    
    static {
        NOOP_LOG_STORE = new NoopLogStore();
    }
    
    public LogFileManager(final Context context, final File file) {
        this(context, file, null);
    }
    
    public LogFileManager(final Context context, final File file, final String currentSession) {
        this.context = context;
        this.logFileDir = new File(file, "log-files");
        this.currentLog = LogFileManager.NOOP_LOG_STORE;
        this.setCurrentSession(currentSession);
    }
    
    private void ensureTargetDirectoryExists() {
        if (!this.logFileDir.exists()) {
            this.logFileDir.mkdirs();
        }
    }
    
    private String getSessionIdForFile(final File file) {
        final String name = file.getName();
        final int lastIndex = name.lastIndexOf(".temp");
        if (lastIndex == -1) {
            return name;
        }
        return name.substring("crashlytics-userlog-".length(), lastIndex);
    }
    
    private File getWorkingFileForSession(String string) {
        this.ensureTargetDirectoryExists();
        string = "crashlytics-userlog-" + string + ".temp";
        return new File(this.logFileDir, string);
    }
    
    private boolean isLoggingEnabled() {
        return CommonUtils.getBooleanResourceValue(this.context, "com.crashlytics.CollectCustomLogs", true);
    }
    
    public void clearLog() {
        this.currentLog.deleteLogFile();
    }
    
    public void discardOldLogFiles(final Set<String> set) {
        final File[] listFiles = this.logFileDir.listFiles();
        if (listFiles != null) {
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                final File file = listFiles[i];
                if (!set.contains(this.getSessionIdForFile(file))) {
                    file.delete();
                }
            }
        }
    }
    
    public ByteString getByteStringForLog() {
        return this.currentLog.getLogAsByteString();
    }
    
    public final void setCurrentSession(final String s) {
        this.currentLog.closeLogFile();
        this.currentLog = LogFileManager.NOOP_LOG_STORE;
        if (s == null) {
            return;
        }
        if (!this.isLoggingEnabled()) {
            Fabric.getLogger().d("CrashlyticsCore", "Preferences requested no custom logs. Aborting log file creation.");
            return;
        }
        this.setLogFile(this.getWorkingFileForSession(s), 65536);
    }
    
    void setLogFile(final File file, final int n) {
        this.currentLog = new QueueFileLogStore(file, n);
    }
    
    private static final class NoopLogStore implements FileLogStore
    {
        @Override
        public void closeLogFile() {
        }
        
        @Override
        public void deleteLogFile() {
        }
        
        @Override
        public ByteString getLogAsByteString() {
            return null;
        }
    }
}
