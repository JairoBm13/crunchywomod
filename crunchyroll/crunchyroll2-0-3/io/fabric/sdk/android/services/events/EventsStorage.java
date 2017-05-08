// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.events;

import java.io.File;
import java.util.List;
import java.io.IOException;

public interface EventsStorage
{
    void add(final byte[] p0) throws IOException;
    
    boolean canWorkingFileStore(final int p0, final int p1);
    
    void deleteFilesInRollOverDirectory(final List<File> p0);
    
    void deleteWorkingFile();
    
    List<File> getAllFilesInRollOverDirectory();
    
    List<File> getBatchOfFilesToSend(final int p0);
    
    int getWorkingFileUsedSizeInBytes();
    
    boolean isWorkingFileEmpty();
    
    void rollOver(final String p0) throws IOException;
}
