// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.settings;

import io.fabric.sdk.android.services.network.HttpMethod;
import io.fabric.sdk.android.services.network.HttpRequestFactory;
import io.fabric.sdk.android.Kit;

public class CreateAppSpiCall extends AbstractAppSpiCall
{
    public CreateAppSpiCall(final Kit kit, final String s, final String s2, final HttpRequestFactory httpRequestFactory) {
        super(kit, s, s2, httpRequestFactory, HttpMethod.POST);
    }
}
