// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.persistence;

import io.fabric.sdk.android.Fabric;
import java.io.File;
import io.fabric.sdk.android.Kit;
import android.content.Context;

public class FileStoreImpl implements FileStore
{
    private final String contentPath;
    private final Context context;
    private final String legacySupport;
    
    public FileStoreImpl(final Kit kit) {
        if (kit.getContext() == null) {
            throw new IllegalStateException("Cannot get directory before context has been set. Call Fabric.with() first");
        }
        this.context = kit.getContext();
        this.contentPath = kit.getPath();
        this.legacySupport = "Android/" + this.context.getPackageName();
    }
    
    @Override
    public File getFilesDir() {
        return this.prepare(this.context.getFilesDir());
    }
    
    File prepare(final File file) {
        if (file != null) {
            if (file.exists() || file.mkdirs()) {
                return file;
            }
            Fabric.getLogger().w("Fabric", "Couldn't create file");
        }
        else {
            Fabric.getLogger().d("Fabric", "Null File");
        }
        return null;
    }
}
