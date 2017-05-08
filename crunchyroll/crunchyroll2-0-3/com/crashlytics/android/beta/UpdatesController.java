// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.beta;

import io.fabric.sdk.android.services.network.HttpRequestFactory;
import io.fabric.sdk.android.services.common.CurrentTimeProvider;
import io.fabric.sdk.android.services.persistence.PreferenceStore;
import io.fabric.sdk.android.services.settings.BetaSettingsData;
import io.fabric.sdk.android.services.common.IdManager;
import android.content.Context;

interface UpdatesController
{
    void initialize(final Context p0, final Beta p1, final IdManager p2, final BetaSettingsData p3, final BuildProperties p4, final PreferenceStore p5, final CurrentTimeProvider p6, final HttpRequestFactory p7);
}
