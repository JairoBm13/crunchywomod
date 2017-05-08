// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.appindexing;

import com.google.android.gms.common.internal.zzu;
import android.net.Uri;
import android.os.Bundle;

public final class Action extends Thing
{
    private Action(final Bundle bundle) {
        super(bundle);
    }
    
    public static Action newAction(String string, final String name, final Uri uri, final Uri url) {
        final Builder builder = new Builder(string);
        final Thing.Builder setName = new Thing.Builder().setName(name);
        if (uri == null) {
            string = null;
        }
        else {
            string = uri.toString();
        }
        return builder.setObject(setName.setId(string).setUrl(url).build()).build();
    }
    
    public static final class Builder extends Thing.Builder
    {
        public Builder(final String s) {
            zzu.zzu(s);
            super.put("type", s);
        }
        
        public Action build() {
            zzu.zzb(this.zzNW.get("object"), "setObject is required before calling build().");
            zzu.zzb(this.zzNW.get("type"), "setType is required before calling build().");
            final Bundle bundle = (Bundle)this.zzNW.getParcelable("object");
            zzu.zzb(bundle.get("name"), "Must call setObject() with a valid name. Example: setObject(new Thing.Builder().setName(name).setUrl(url))");
            zzu.zzb(bundle.get("url"), "Must call setObject() with a valid app URI. Example: setObject(new Thing.Builder().setName(name).setUrl(url))");
            return new Action(this.zzNW, null);
        }
        
        public Builder put(final String s, final Thing thing) {
            return (Builder)super.put(s, thing);
        }
        
        public Builder put(final String s, final String s2) {
            return (Builder)super.put(s, s2);
        }
        
        public Builder setName(final String s) {
            return (Builder)super.put("name", s);
        }
        
        public Builder setObject(final Thing thing) {
            zzu.zzu(thing);
            return (Builder)super.put("object", thing);
        }
        
        public Builder setUrl(final Uri uri) {
            if (uri != null) {
                super.put("url", uri.toString());
            }
            return this;
        }
    }
}
