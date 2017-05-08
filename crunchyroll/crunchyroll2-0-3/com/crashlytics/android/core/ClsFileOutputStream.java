// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.core;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.FileOutputStream;

class ClsFileOutputStream extends FileOutputStream
{
    public static final FilenameFilter TEMP_FILENAME_FILTER;
    private boolean closed;
    private File complete;
    private File inProgress;
    private final String root;
    
    static {
        TEMP_FILENAME_FILTER = new FilenameFilter() {
            @Override
            public boolean accept(final File file, final String s) {
                return s.endsWith(".cls_temp");
            }
        };
    }
    
    public ClsFileOutputStream(final File file, final String s) throws FileNotFoundException {
        super(new File(file, s + ".cls_temp"));
        this.closed = false;
        this.root = file + File.separator + s;
        this.inProgress = new File(this.root + ".cls_temp");
    }
    
    @Override
    public void close() throws IOException {
        final File complete;
        Label_0086: {
            synchronized (this) {
                if (!this.closed) {
                    this.closed = true;
                    super.flush();
                    super.close();
                    complete = new File(this.root + ".cls");
                    if (!this.inProgress.renameTo(complete)) {
                        break Label_0086;
                    }
                    this.inProgress = null;
                    this.complete = complete;
                }
                return;
            }
        }
        String s = "";
        if (complete.exists()) {
            s = " (target already exists)";
        }
        else if (!this.inProgress.exists()) {
            s = " (source does not exist)";
        }
        throw new IOException("Could not rename temp file: " + this.inProgress + " -> " + complete + s);
    }
    
    public void closeInProgressStream() throws IOException {
        if (this.closed) {
            return;
        }
        this.closed = true;
        super.flush();
        super.close();
    }
}
