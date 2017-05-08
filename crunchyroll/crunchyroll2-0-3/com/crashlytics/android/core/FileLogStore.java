// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.core;

interface FileLogStore
{
    void closeLogFile();
    
    void deleteLogFile();
    
    ByteString getLogAsByteString();
}
