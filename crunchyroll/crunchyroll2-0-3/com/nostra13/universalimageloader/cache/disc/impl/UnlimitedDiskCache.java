// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.cache.disc.impl;

import com.nostra13.universalimageloader.cache.disc.naming.FileNameGenerator;
import java.io.File;

public class UnlimitedDiskCache extends BaseDiskCache
{
    public UnlimitedDiskCache(final File file) {
        super(file);
    }
    
    public UnlimitedDiskCache(final File file, final File file2) {
        super(file, file2);
    }
    
    public UnlimitedDiskCache(final File file, final File file2, final FileNameGenerator fileNameGenerator) {
        super(file, file2, fileNameGenerator);
    }
}
