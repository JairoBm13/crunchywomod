// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.cache.disc;

import com.nostra13.universalimageloader.utils.IoUtils;
import java.io.InputStream;
import java.io.IOException;
import android.graphics.Bitmap;
import java.io.File;

public interface DiskCache
{
    void clear();
    
    void close();
    
    File get(final String p0);
    
    File getDirectory();
    
    boolean remove(final String p0);
    
    boolean save(final String p0, final Bitmap p1) throws IOException;
    
    boolean save(final String p0, final InputStream p1, final IoUtils.CopyListener p2) throws IOException;
}
