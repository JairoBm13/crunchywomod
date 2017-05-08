// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.a;

import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import java.io.IOException;
import android.content.Context;

public class f extends e
{
    private f(final Context context, final i i, final j j) {
        super(context, i, j);
    }
    
    public static f a(final String s, final Context context) {
        final com.google.android.a.a a = new com.google.android.a.a();
        com.google.android.a.e.a(s, context, a);
        return new f(context, a, new l(239));
    }
    
    @Override
    protected void b(final Context context) {
        final long n = 1L;
        super.b(context);
        a a = null;
        try {
            final a f;
            a = (f = this.f(context));
            final boolean b = f.b();
            if (!b) {
                goto Label_0055;
            }
            final f f2 = this;
            final int n2 = 28;
            final long n3 = n;
            f2.a(n2, n3);
            final a a2 = a;
            final String s = a2.a();
            final String s3;
            final String s2 = s3 = s;
            if (s3 != null) {
                final f f3 = this;
                final int n4 = 30;
                final String s4 = s2;
                f3.a(n4, s4);
            }
            return;
        }
        catch (IOException ex) {
            this.a(28, 1L);
            return;
        }
        catch (GooglePlayServicesNotAvailableException ex2) {}
        try {
            final a f = a;
            final boolean b = f.b();
            if (!b) {
                goto Label_0055;
            }
            final f f2 = this;
            final int n2 = 28;
            final long n3 = n;
            f2.a(n2, n3);
            final a a2 = a;
            final String s = a2.a();
            final String s3;
            final String s2 = s3 = s;
            if (s3 != null) {
                final f f3 = this;
                final int n4 = 30;
                final String s4 = s2;
                f3.a(n4, s4);
            }
        }
        catch (IOException ex3) {}
    }
    
    a f(final Context context) throws IOException, GooglePlayServicesNotAvailableException {
        int i = 0;
        AdvertisingIdClient.Info advertisingIdInfo;
        byte[] array;
        try {
            advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
            final String s = advertisingIdInfo.getId();
            if (s == null || !s.matches("^[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}$")) {
                return new a(s, advertisingIdInfo.isLimitAdTrackingEnabled());
            }
            array = new byte[16];
            int n = 0;
            while (i < s.length()) {
                int n2;
                if (i == 8 || i == 13 || i == 18 || (n2 = i) == 23) {
                    n2 = i + 1;
                }
                array[n] = (byte)((Character.digit(s.charAt(n2), 16) << 4) + Character.digit(s.charAt(n2 + 1), 16));
                ++n;
                i = n2 + 2;
            }
        }
        catch (GooglePlayServicesRepairableException ex) {
            throw new IOException(ex);
        }
        final String s = this.c.a(array, true);
        return new a(s, advertisingIdInfo.isLimitAdTrackingEnabled());
    }
    
    class a
    {
        private String b;
        private boolean c;
        
        public a(final String b, final boolean c) {
            this.b = b;
            this.c = c;
        }
        
        public String a() {
            return this.b;
        }
        
        public boolean b() {
            return this.c;
        }
    }
}
