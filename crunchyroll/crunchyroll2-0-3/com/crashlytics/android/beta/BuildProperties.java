// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.beta;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

class BuildProperties
{
    public final String buildId;
    public final String packageName;
    public final String versionCode;
    public final String versionName;
    
    BuildProperties(final String versionCode, final String versionName, final String buildId, final String packageName) {
        this.versionCode = versionCode;
        this.versionName = versionName;
        this.buildId = buildId;
        this.packageName = packageName;
    }
    
    public static BuildProperties fromProperties(final Properties properties) {
        return new BuildProperties(properties.getProperty("version_code"), properties.getProperty("version_name"), properties.getProperty("build_id"), properties.getProperty("package_name"));
    }
    
    public static BuildProperties fromPropertiesStream(final InputStream inputStream) throws IOException {
        final Properties properties = new Properties();
        properties.load(inputStream);
        return fromProperties(properties);
    }
}
