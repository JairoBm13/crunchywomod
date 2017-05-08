// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.events;

public class DisabledEventsStrategy<T> implements EventsStrategy<T>
{
    @Override
    public void cancelTimeBasedFileRollOver() {
    }
    
    @Override
    public void deleteAllEvents() {
    }
    
    @Override
    public FilesSender getFilesSender() {
        return null;
    }
    
    @Override
    public boolean rollFileOver() {
        return false;
    }
    
    @Override
    public void sendEvents() {
    }
}
