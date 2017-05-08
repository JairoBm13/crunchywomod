// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.events;

import android.net.Uri;

public class AppIndexEvent
{
    public final Uri appUri;
    public final String title;
    public final Uri webUri;
    
    public AppIndexEvent(final String title, final Uri webUri, final Uri appUri) {
        this.title = title;
        this.webUri = webUri;
        this.appUri = appUri;
    }
}
