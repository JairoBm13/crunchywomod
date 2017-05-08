// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.settings;

import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory$Options;
import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.common.CommonUtils;
import android.content.Context;

public class IconRequest
{
    public final String hash;
    public final int height;
    public final int iconResourceId;
    public final int width;
    
    public IconRequest(final String hash, final int iconResourceId, final int width, final int height) {
        this.hash = hash;
        this.iconResourceId = iconResourceId;
        this.width = width;
        this.height = height;
    }
    
    public static IconRequest build(final Context context, final String s) {
        IconRequest iconRequest = null;
        if (s == null) {
            return iconRequest;
        }
        try {
            final int appIconResourceId = CommonUtils.getAppIconResourceId(context);
            Fabric.getLogger().d("Fabric", "App icon resource ID is " + appIconResourceId);
            final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
            bitmapFactory$Options.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(context.getResources(), appIconResourceId, bitmapFactory$Options);
            iconRequest = new IconRequest(s, appIconResourceId, bitmapFactory$Options.outWidth, bitmapFactory$Options.outHeight);
            return iconRequest;
        }
        catch (Exception ex) {
            Fabric.getLogger().e("Fabric", "Failed to load icon", ex);
            return null;
        }
    }
}
