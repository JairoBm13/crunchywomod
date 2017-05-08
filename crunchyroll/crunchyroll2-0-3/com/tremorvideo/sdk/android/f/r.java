// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.f;

import com.tremorvideo.sdk.android.videoad.ac;
import android.telephony.TelephonyManager;

class r extends o
{
    private boolean a;
    private boolean b;
    private final boolean c;
    private final boolean d;
    private final boolean e;
    
    r() {
        while (true) {
            try {
                if (((TelephonyManager)ac.x().getSystemService("phone")).getLine1Number() != null) {
                    this.a = true;
                    this.b = true;
                }
                this.c = false;
                this.d = true;
                this.e = true;
            }
            catch (Exception ex) {
                continue;
            }
            break;
        }
    }
    
    public static r b() {
        return new r();
    }
    
    @Override
    public String a() {
        return "supports: { sms: " + this.a + ", tel: " + this.b + ", calendar: " + this.c + ", storePicture: " + this.d + ", inlineVideo: " + this.e + " }";
    }
}
