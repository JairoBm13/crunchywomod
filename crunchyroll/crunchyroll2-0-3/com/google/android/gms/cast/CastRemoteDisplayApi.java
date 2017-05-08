// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.cast;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.GoogleApiClient;

public interface CastRemoteDisplayApi
{
    PendingResult<CastRemoteDisplay.CastRemoteDisplaySessionResult> stopRemoteDisplay(final GoogleApiClient p0);
}
