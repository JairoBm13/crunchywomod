// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.b;

import java.util.Iterator;
import com.google.ads.interactivemedia.v3.api.player.VideoProgressUpdate;
import android.os.Message;
import java.util.ArrayList;
import java.util.List;
import android.os.Handler;
import com.google.ads.interactivemedia.v3.api.player.VideoAdPlayer;
import android.os.Handler$Callback;

public class v implements Handler$Callback
{
    protected final VideoAdPlayer a;
    protected final long b;
    protected boolean c;
    private Handler d;
    private List<a> e;
    private List<a> f;
    private List<a> g;
    
    v(final Handler d, final VideoAdPlayer a, final long b) {
        this.c = false;
        this.e = new ArrayList<a>(1);
        this.f = new ArrayList<a>(1);
        this.a = a;
        this.b = b;
        this.d = d;
        if (d == null) {
            this.d = new Handler((Handler$Callback)this);
        }
        this.g = this.e;
    }
    
    public v(final VideoAdPlayer videoAdPlayer, final long n) {
        this(null, videoAdPlayer, n);
    }
    
    private void d() {
        if (this.c) {
            return;
        }
        this.c = true;
        this.d.sendEmptyMessage(1);
    }
    
    public void a() {
        this.g = this.f;
        this.d();
    }
    
    public void a(final a a) {
        this.e.add(a);
    }
    
    public void b() {
        this.g = this.e;
        this.d();
    }
    
    public void b(final a a) {
        this.e.remove(a);
    }
    
    public void c() {
        this.c = false;
        this.d.sendMessageAtFrontOfQueue(Message.obtain(this.d, 2));
    }
    
    public void c(final a a) {
        this.f.add(a);
    }
    
    public boolean handleMessage(final Message message) {
        switch (message.what) {
            default: {
                return true;
            }
            case 2: {
                this.d.removeMessages(1);
                return true;
            }
            case 1: {
                final VideoProgressUpdate progress = this.a.getProgress();
                final Iterator<a> iterator = this.g.iterator();
                while (iterator.hasNext()) {
                    iterator.next().a(progress);
                }
                this.d.sendMessageDelayed(Message.obtain(this.d, 1), this.b);
                return true;
            }
        }
    }
    
    public interface a
    {
        void a(final VideoProgressUpdate p0);
    }
}
