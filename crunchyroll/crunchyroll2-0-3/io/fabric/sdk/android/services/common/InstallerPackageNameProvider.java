// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.common;

import io.fabric.sdk.android.Fabric;
import android.content.Context;
import io.fabric.sdk.android.services.cache.ValueLoader;
import io.fabric.sdk.android.services.cache.MemoryValueCache;

public class InstallerPackageNameProvider
{
    private final MemoryValueCache<String> installerPackageNameCache;
    private final ValueLoader<String> installerPackageNameLoader;
    
    public InstallerPackageNameProvider() {
        this.installerPackageNameLoader = new ValueLoader<String>() {
            @Override
            public String load(final Context context) throws Exception {
                String installerPackageName;
                if ((installerPackageName = context.getPackageManager().getInstallerPackageName(context.getPackageName())) == null) {
                    installerPackageName = "";
                }
                return installerPackageName;
            }
        };
        this.installerPackageNameCache = new MemoryValueCache<String>();
    }
    
    public String getInstallerPackageName(final Context context) {
        try {
            String s = this.installerPackageNameCache.get(context, this.installerPackageNameLoader);
            if ("".equals(s)) {
                s = null;
            }
            return s;
        }
        catch (Exception ex) {
            Fabric.getLogger().e("Fabric", "Failed to determine installer package name", ex);
            return null;
        }
    }
}
