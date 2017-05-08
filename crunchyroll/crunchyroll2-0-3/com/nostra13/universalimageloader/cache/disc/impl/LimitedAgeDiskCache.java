// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.cache.disc.impl;

import com.nostra13.universalimageloader.utils.IoUtils;
import java.io.InputStream;
import java.io.IOException;
import android.graphics.Bitmap;
import java.util.Collections;
import java.util.HashMap;
import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import com.nostra13.universalimageloader.core.DefaultConfigurationFactory;
import java.io.File;
import java.util.Map;

public class LimitedAgeDiskCache extends BaseDiskCache
{
    private final Map<File, Long> loadingDates;
    private final long maxFileAge;
    
    public LimitedAgeDiskCache(final File file, final long n) {
        this(file, null, DefaultConfigurationFactory.createFileNameGenerator(), n);
    }
    
    public LimitedAgeDiskCache(final File file, final File file2, final long n) {
        this(file, file2, DefaultConfigurationFactory.createFileNameGenerator(), n);
    }
    
    public LimitedAgeDiskCache(final File file, final File file2, final FileNameGenerator fileNameGenerator, final long n) {
        super(file, file2, fileNameGenerator);
        this.loadingDates = Collections.synchronizedMap(new HashMap<File, Long>());
        this.maxFileAge = 1000L * n;
    }
    
    private void rememberUsage(final String s) {
        final File file = this.getFile(s);
        final long currentTimeMillis = System.currentTimeMillis();
        file.setLastModified(currentTimeMillis);
        this.loadingDates.put(file, currentTimeMillis);
    }
    
    @Override
    public void clear() {
        super.clear();
        this.loadingDates.clear();
    }
    
    @Override
    public File get(final String s) {
        final File value = super.get(s);
        if (value != null && value.exists()) {
            Long value2 = this.loadingDates.get(value);
            boolean b;
            if (value2 == null) {
                b = false;
                value2 = value.lastModified();
            }
            else {
                b = true;
            }
            if (System.currentTimeMillis() - value2 > this.maxFileAge) {
                value.delete();
                this.loadingDates.remove(value);
            }
            else if (!b) {
                this.loadingDates.put(value, value2);
                return value;
            }
        }
        return value;
    }
    
    @Override
    public boolean remove(final String s) {
        this.loadingDates.remove(this.getFile(s));
        return super.remove(s);
    }
    
    @Override
    public boolean save(final String s, final Bitmap bitmap) throws IOException {
        final boolean save = super.save(s, bitmap);
        this.rememberUsage(s);
        return save;
    }
    
    @Override
    public boolean save(final String s, final InputStream inputStream, final IoUtils.CopyListener copyListener) throws IOException {
        final boolean save = super.save(s, inputStream, copyListener);
        this.rememberUsage(s);
        return save;
    }
}
