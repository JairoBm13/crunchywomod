// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.appindexing;

import android.net.Uri;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.GoogleApiClient;

public interface AppIndexApi
{
    PendingResult<Status> end(final GoogleApiClient p0, final Action p1);
    
    PendingResult<Status> start(final GoogleApiClient p0, final Action p1);
    
    @Deprecated
    public static final class AppIndexingLink
    {
        public final Uri appIndexingUrl;
        public final int viewId;
        public final Uri webUrl;
    }
}
