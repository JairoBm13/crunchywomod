// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.core;

import io.fabric.sdk.android.Fabric;
import java.util.HashMap;
import java.util.Collections;
import java.io.File;
import java.util.Map;

class SessionReport implements Report
{
    private final Map<String, String> customHeaders;
    private final File file;
    
    public SessionReport(final File file) {
        this(file, Collections.emptyMap());
    }
    
    public SessionReport(final File file, final Map<String, String> map) {
        this.file = file;
        this.customHeaders = new HashMap<String, String>(map);
        if (this.file.length() == 0L) {
            this.customHeaders.putAll(ReportUploader.HEADER_INVALID_CLS_FILE);
        }
    }
    
    @Override
    public Map<String, String> getCustomHeaders() {
        return Collections.unmodifiableMap((Map<? extends String, ? extends String>)this.customHeaders);
    }
    
    @Override
    public File getFile() {
        return this.file;
    }
    
    @Override
    public String getFileName() {
        return this.getFile().getName();
    }
    
    @Override
    public String getIdentifier() {
        final String fileName = this.getFileName();
        return fileName.substring(0, fileName.lastIndexOf(46));
    }
    
    @Override
    public boolean remove() {
        Fabric.getLogger().d("CrashlyticsCore", "Removing report at " + this.file.getPath());
        return this.file.delete();
    }
}
