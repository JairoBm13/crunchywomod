// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.api;

import com.google.ads.interactivemedia.v3.b.j;
import com.google.ads.interactivemedia.v3.b.e;
import com.google.ads.interactivemedia.v3.b.p;
import android.content.Context;
import com.google.ads.interactivemedia.v3.b.a;

public class ImaSdkFactory
{
    private static ImaSdkFactory instance;
    
    public static ImaSdkFactory getInstance() {
        if (ImaSdkFactory.instance == null) {
            ImaSdkFactory.instance = new ImaSdkFactory();
        }
        return ImaSdkFactory.instance;
    }
    
    public AdDisplayContainer createAdDisplayContainer() {
        return new a();
    }
    
    public AdsLoader createAdsLoader(final Context context, final ImaSdkSettings imaSdkSettings) {
        return new e(context, p.a, imaSdkSettings);
    }
    
    public AdsRequest createAdsRequest() {
        return new j();
    }
    
    public ImaSdkSettings createImaSdkSettings() {
        return new ImaSdkSettings();
    }
}
