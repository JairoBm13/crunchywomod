// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.answers;

class KeepAllEventFilter implements EventFilter
{
    @Override
    public boolean skipEvent(final SessionEvent sessionEvent) {
        return false;
    }
}
