// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.events;

import java.util.zip.GZIPOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.File;
import android.content.Context;

public class GZIPQueueFileEventStorage extends QueueFileEventStorage
{
    public GZIPQueueFileEventStorage(final Context context, final File file, final String s, final String s2) throws IOException {
        super(context, file, s, s2);
    }
    
    @Override
    public OutputStream getMoveOutputStream(final File file) throws IOException {
        return new GZIPOutputStream(new FileOutputStream(file));
    }
}
