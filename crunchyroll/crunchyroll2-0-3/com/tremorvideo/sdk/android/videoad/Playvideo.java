// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.videoad;

import android.view.KeyEvent;
import com.tremorvideo.sdk.android.a.b;
import android.os.Bundle;
import android.content.res.Configuration;
import android.content.Intent;
import android.view.MotionEvent;
import android.app.Activity;

public class Playvideo extends Activity
{
    l a;
    String b;
    int c;
    
    public boolean dispatchTouchEvent(final MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0 && this.a != null) {
            this.a.a(motionEvent.getRawX(), motionEvent.getRawY(), 0);
        }
        if (motionEvent.getAction() == 1 && this.a != null) {
            this.a.a(motionEvent.getRawX(), motionEvent.getRawY(), 1);
        }
        return super.dispatchTouchEvent(motionEvent);
    }
    
    protected void onActivityResult(final int n, final int n2, final Intent intent) {
        super.onActivityResult(n, n2, intent);
        ac.e("activity returned");
        this.a.a(n, n2, intent);
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.a.a(configuration);
    }
    
    public void onCreate(final Bundle bundle) {
        ac.e("TremorDebug: Playvideo activity on create");
        this.b = this.getIntent().getStringExtra("tremorVideoType");
        if (this.b.compareTo("buyitnow") == 0) {
            this.setTheme(16973841);
        }
        while (true) {
            super.onCreate(bundle);
            ac.e("Playvideo - onCreate");
            while (true) {
                Label_0213: {
                    while (true) {
                        try {
                            if (this.b.compareTo("ad") == 0) {
                                this.a = new i();
                            }
                            else {
                                if (this.b.compareTo("adProgress") != 0) {
                                    break Label_0213;
                                }
                                this.a = new j();
                            }
                            this.c = this.getIntent().getIntExtra("curEventID", -1);
                            if (this.c > -1) {
                                final Intent intent = new Intent();
                                intent.putExtra("endEventId", this.c);
                                this.setResult(-1, intent);
                            }
                            this.a.a(bundle, this);
                            ac.e("Playvideo - Activity Created");
                            return;
                        }
                        catch (Exception ex) {
                            ac.a(ex);
                            if (this.b.compareTo("ad") == 0) {
                                ac.C().j();
                            }
                            this.finish();
                            bx.a(bx.a.b, false, -1);
                            continue;
                        }
                        break;
                    }
                }
                if (this.b.compareTo("coupon") == 0) {
                    this.a = new k();
                    continue;
                }
                if (this.b.compareTo("webview") == 0) {
                    this.a = new m();
                    continue;
                }
                if (this.b.compareTo("buyitnow") == 0) {
                    this.a = new b();
                    continue;
                }
                if (this.b.compareTo("movieboard") == 0) {
                    this.a = new com.tremorvideo.sdk.android.b.b();
                    continue;
                }
                if (this.b.compareTo("genericwebview") == 0) {
                    this.a = new com.tremorvideo.sdk.android.richmedia.a.b();
                    continue;
                }
                if (this.b.compareTo("adchoices") == 0) {
                    this.a = new com.tremorvideo.sdk.android.d.b();
                    continue;
                }
                continue;
            }
        }
    }
    
    protected void onDestroy() {
        ac.e("Playvideo - onDestroy");
        super.onDestroy();
        while (true) {
            try {
                ac.b();
                this.a.f();
                System.gc();
                ac.b();
            }
            catch (Exception ex) {
                ac.a(ex);
                continue;
            }
            break;
        }
    }
    
    public boolean onKeyDown(final int n, final KeyEvent keyEvent) {
        return this.a.a(n, keyEvent) || super.onKeyDown(n, keyEvent);
    }
    
    protected void onPause() {
        super.onPause();
        ac.e("Playvideo - onPause");
        this.a.b();
    }
    
    protected void onResume() {
        super.onResume();
        ac.e("Playvideo - onResume");
        this.a.c();
    }
    
    public boolean onSearchRequested() {
        return this.a.d();
    }
    
    public void onStart() {
        super.onStart();
        ac.e("Playvideo - onStart");
        try {
            this.a.a();
        }
        catch (Exception ex) {
            ac.a(ex);
            if (this.b.compareTo("ad") == 0) {
                ac.C().j();
            }
            this.finish();
            bx.a(bx.a.b, false, -1);
        }
    }
    
    protected void onStop() {
        super.onStop();
        ac.e("Playvideo - onStop");
        try {
            this.a.e();
        }
        catch (Exception ex) {
            ac.a(ex);
        }
    }
    
    public void setTheme(final int theme) {
        super.setTheme(theme);
    }
}
