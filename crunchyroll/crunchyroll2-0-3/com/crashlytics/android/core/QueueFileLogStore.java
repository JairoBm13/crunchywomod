// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.core;

import java.io.InputStream;
import java.io.Closeable;
import io.fabric.sdk.android.services.common.CommonUtils;
import java.io.IOException;
import io.fabric.sdk.android.Fabric;
import java.io.File;
import io.fabric.sdk.android.services.common.QueueFile;

class QueueFileLogStore implements FileLogStore
{
    private QueueFile logFile;
    private final int maxLogSize;
    private final File workingFile;
    
    public QueueFileLogStore(final File workingFile, final int maxLogSize) {
        this.workingFile = workingFile;
        this.maxLogSize = maxLogSize;
    }
    
    private void openLogFile() {
        if (this.logFile != null) {
            return;
        }
        try {
            this.logFile = new QueueFile(this.workingFile);
        }
        catch (IOException ex) {
            Fabric.getLogger().e("CrashlyticsCore", "Could not open log file: " + this.workingFile, ex);
        }
    }
    
    @Override
    public void closeLogFile() {
        CommonUtils.closeOrLog(this.logFile, "There was a problem closing the Crashlytics log file.");
        this.logFile = null;
    }
    
    @Override
    public void deleteLogFile() {
        this.closeLogFile();
        this.workingFile.delete();
    }
    
    @Override
    public ByteString getLogAsByteString() {
        if (this.workingFile.exists()) {
            this.openLogFile();
            if (this.logFile != null) {
                final int[] array = { 0 };
                final byte[] array2 = new byte[this.logFile.usedBytes()];
                try {
                    this.logFile.forEach((QueueFile.ElementReader)new QueueFile.ElementReader() {
                        @Override
                        public void read(final InputStream inputStream, final int n) throws IOException {
                            try {
                                inputStream.read(array2, array[0], n);
                                final int[] val$offsetHolder = array;
                                val$offsetHolder[0] += n;
                            }
                            finally {
                                inputStream.close();
                            }
                        }
                    });
                    return ByteString.copyFrom(array2, 0, array[0]);
                }
                catch (IOException ex) {
                    Fabric.getLogger().e("CrashlyticsCore", "A problem occurred while reading the Crashlytics log file.", ex);
                    return ByteString.copyFrom(array2, 0, array[0]);
                }
            }
        }
        return null;
    }
}
