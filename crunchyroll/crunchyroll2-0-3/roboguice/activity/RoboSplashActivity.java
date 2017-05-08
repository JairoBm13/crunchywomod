// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.activity;

import roboguice.RoboGuice;
import android.os.Bundle;
import android.app.Application;
import android.app.Activity;

public abstract class RoboSplashActivity extends Activity
{
    protected int minDisplayMs;
    
    public RoboSplashActivity() {
        this.minDisplayMs = 2500;
    }
    
    protected void andFinishThisOne() {
        this.finish();
    }
    
    protected void doStuffInBackground(final Application application) {
    }
    
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        new Thread(new Runnable() {
            final /* synthetic */ long val$start = System.currentTimeMillis();
            
            @Override
            public void run() {
                final Application application = RoboSplashActivity.this.getApplication();
                RoboGuice.getBaseApplicationInjector(RoboSplashActivity.this.getApplication());
                RoboSplashActivity.this.doStuffInBackground(application);
                final long n = System.currentTimeMillis() - this.val$start;
                while (true) {
                    if (n >= RoboSplashActivity.this.minDisplayMs) {
                        break Label_0062;
                    }
                    try {
                        Thread.sleep(RoboSplashActivity.this.minDisplayMs - n);
                        RoboSplashActivity.this.startNextActivity();
                        RoboSplashActivity.this.andFinishThisOne();
                    }
                    catch (InterruptedException ex) {
                        Thread.interrupted();
                        continue;
                    }
                    break;
                }
            }
        }).start();
    }
    
    protected abstract void startNextActivity();
}
