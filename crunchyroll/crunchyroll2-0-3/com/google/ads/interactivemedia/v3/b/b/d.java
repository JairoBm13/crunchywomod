// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.b.b;

import com.google.ads.interactivemedia.v3.api.Ad;

public class d
{
    public boolean a;
    public int[] b;
    public int c;
    public int d;
    public int e;
    public int f;
    public String g;
    public int h;
    public String i;
    public int j;
    public int k;
    public boolean l;
    public String m;
    public String n;
    public int o;
    public float p;
    
    public d() {
        this.a = false;
        this.b = new int[] { -2013265920, -2013265920 };
        this.c = -1728053248;
        this.d = 1;
        this.e = 1728053247;
        this.f = 1;
        this.g = "Advertisement";
        this.h = -3355444;
        this.i = "Arial";
        this.j = 12;
        this.k = 4;
        this.l = true;
        this.m = "···";
        this.n = "Learn More " + this.m;
        this.o = -3355444;
        this.p = 16.0f;
    }
    
    public static d a(final Ad ad) {
        final d d = new d();
        d.a = ad.isSkippable();
        return d;
    }
}
