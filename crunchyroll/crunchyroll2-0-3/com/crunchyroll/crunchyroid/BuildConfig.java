// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid;

import com.crunchyroll.crunchyroid.app.Constants;

public final class BuildConfig
{
    public static final String API_URL = "api.crunchyroll.com";
    public static final String APPLICATION_ID = "com.crunchyroll.crunchyroid";
    public static final String BUILD_TYPE = "release";
    public static final boolean DEBUG = false;
    public static final String FLAVOR = "production";
    public static final int VERSION_CODE = 302;
    public static final String VERSION_NAME = "2.0.3";
    public static final Constants.BuildType buildType;
    
    static {
        buildType = Constants.BuildType.RELEASE;
    }
}
