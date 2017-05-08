// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.b;

import android.view.View;
import java.util.HashMap;
import android.util.Log;
import android.graphics.BitmapFactory;
import java.net.URL;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.content.Context;
import com.google.ads.interactivemedia.v3.b.a.c;
import android.view.View$OnClickListener;
import android.widget.ImageView;

public class q extends ImageView implements View$OnClickListener
{
    private final c a;
    private final s b;
    private final String c;
    
    public q(final Context context, final s b, final c a, final String c) {
        super(context);
        this.b = b;
        this.a = a;
        this.c = c;
        this.setOnClickListener((View$OnClickListener)this);
        this.a();
    }
    
    private void a() {
        new AsyncTask<Void, Void, Bitmap>() {
            Exception a = null;
            
            protected Bitmap a(final Void... array) {
                try {
                    return BitmapFactory.decodeStream(new URL(q.this.a.src).openConnection().getInputStream());
                }
                catch (Exception a) {
                    this.a = a;
                    return null;
                }
            }
            
            protected void a(final Bitmap imageBitmap) {
                if (imageBitmap == null) {
                    Log.e("IMASDK", "Loading image companion " + q.this.a.src + " failed: " + this.a);
                    return;
                }
                q.this.b();
                q.this.setImageBitmap(imageBitmap);
            }
        }.execute((Object[])new Void[0]);
    }
    
    private void b() {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("companionId", this.a.companionId);
        this.b.b(new r(r.b.displayContainer, r.c.companionView, this.c, hashMap));
    }
    
    public void onClick(final View view) {
        this.b.b(this.a.clickThroughUrl);
    }
}
