// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.common;

import android.text.TextUtils;
import android.os.Bundle;
import io.fabric.sdk.android.Fabric;
import android.content.Context;

public class ApiKey
{
    protected String buildApiKeyInstructions() {
        return "Fabric could not be initialized, API key missing from AndroidManifest.xml. Add the following tag to your Application element \n\t<meta-data android:name=\"io.fabric.ApiKey\" android:value=\"YOUR_API_KEY\"/>";
    }
    
    protected String getApiKeyFromManifest(final Context context) {
        final String s = null;
        final String s2 = null;
        String s3 = s;
        try {
            final String packageName = context.getPackageName();
            s3 = s;
            final Bundle metaData = context.getPackageManager().getApplicationInfo(packageName, 128).metaData;
            String string = s2;
            if (metaData != null) {
                s3 = s;
                final String string2 = metaData.getString("io.fabric.ApiKey");
                if ((string = string2) == null) {
                    s3 = string2;
                    Fabric.getLogger().d("Fabric", "Falling back to Crashlytics key lookup from Manifest");
                    s3 = string2;
                    string = metaData.getString("com.crashlytics.ApiKey");
                }
            }
            return string;
        }
        catch (Exception ex) {
            Fabric.getLogger().d("Fabric", "Caught non-fatal exception while retrieving apiKey: " + ex);
            return s3;
        }
    }
    
    protected String getApiKeyFromStrings(final Context context) {
        String string = null;
        int n;
        if ((n = CommonUtils.getResourcesIdentifier(context, "io.fabric.ApiKey", "string")) == 0) {
            Fabric.getLogger().d("Fabric", "Falling back to Crashlytics key lookup from Strings");
            n = CommonUtils.getResourcesIdentifier(context, "com.crashlytics.ApiKey", "string");
        }
        if (n != 0) {
            string = context.getResources().getString(n);
        }
        return string;
    }
    
    public String getValue(final Context context) {
        String s;
        if (TextUtils.isEmpty((CharSequence)(s = this.getApiKeyFromManifest(context)))) {
            s = this.getApiKeyFromStrings(context);
        }
        if (TextUtils.isEmpty((CharSequence)s)) {
            this.logErrorOrThrowException(context);
        }
        return s;
    }
    
    protected void logErrorOrThrowException(final Context context) {
        if (Fabric.isDebuggable() || CommonUtils.isAppDebuggable(context)) {
            throw new IllegalArgumentException(this.buildApiKeyInstructions());
        }
        Fabric.getLogger().e("Fabric", this.buildApiKeyInstructions());
    }
}
