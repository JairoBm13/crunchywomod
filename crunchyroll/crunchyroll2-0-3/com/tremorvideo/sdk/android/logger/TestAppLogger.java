// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.logger;

import android.content.Intent;
import android.database.Cursor;
import com.tremorvideo.sdk.android.videoad.ac;

public class TestAppLogger
{
    public static final String STATE_FAIL = "fail";
    public static final String STATE_INFO = "info";
    public static final String STATE_PASS = "pass";
    private static TestAppLogger b;
    ResultsDbAdapter a;
    
    static {
        TestAppLogger.b = new TestAppLogger();
    }
    
    private TestAppLogger() {
        (this.a = new ResultsDbAdapter(ac.x())).open();
    }
    
    public static TestAppLogger getInstance() {
        Label_0022: {
            if (TestAppLogger.b != null) {
                break Label_0022;
            }
            synchronized (TestAppLogger.class) {
                TestAppLogger.b = new TestAppLogger();
                return TestAppLogger.b;
            }
        }
    }
    
    public Cursor fetchLogs() {
        if (this.a != null) {
            return this.a.fetchAllResults();
        }
        return null;
    }
    
    public void log(final String s, String s2, String s3, final String s4) {
        if (this.a != null) {
            String s5;
            if ((s5 = s2) == null) {
                s5 = "";
            }
            if ((s2 = s3) == null) {
                s2 = "";
            }
            if ((s3 = s4) == null) {
                s3 = "";
            }
            this.a.createResultRow(s, s5, s2, s3);
        }
    }
    
    public void logAdReady(final String s, final String s2, final String s3) {
        final Intent intent = new Intent();
        intent.setAction("com.tremorvideo.logger.BroadcastAdReady");
        ac.x().sendBroadcast(intent);
    }
    
    public void logClickout(final String s, final String s2, final String s3) {
        this.log("page_loaded", s, s2, s3);
    }
    
    public void logClickoutURL(final String s, final String s2, final String s3) {
        this.log("page_load_url", s, s2, s3);
    }
    
    public void logCreative(final String s, final String s2, final String s3) {
    }
    
    public void logInit(final String s, final String s2, final String s3) {
        this.log("initialize", s, s2, s3);
    }
    
    public void logMediaDownloaded(final String s, final String s2, final String s3) {
        if (s3.equals("fail")) {
            final Intent intent = new Intent();
            intent.setAction("com.tremorvideo.logger.BroadcastFail");
            intent.putExtra("msg", "media_loaded");
            intent.putExtra("shortMsg", s);
            intent.putExtra("longMsg", s2);
            intent.putExtra("logType", s3);
            ac.x().sendBroadcast(intent);
            return;
        }
        final Intent intent2 = new Intent();
        intent2.setAction("com.tremorvideo.logger.BroadcastMediaLoaded");
        intent2.putExtra("msg", "media_loaded");
        intent2.putExtra("shortMsg", s);
        intent2.putExtra("longMsg", s2);
        intent2.putExtra("logType", s3);
        ac.x().sendBroadcast(intent2);
    }
    
    public void logRequestAd(final String s, final String s2, final String s3) {
        if (s3.equals("fail")) {
            final Intent intent = new Intent();
            intent.setAction("com.tremorvideo.logger.BroadcastFail");
            intent.putExtra("msg", "requesting_ad");
            ac.x().sendBroadcast(intent);
        }
    }
    
    public void logShowAd(final String s, final String s2, final String s3) {
        this.log("showAd", s, s2, s3);
    }
    
    public void logStart(final String s, final String s2, final String s3) {
    }
    
    public void logStopAd(final String s, final String s2, final String s3) {
        this.log("stopAd", s, s2, s3);
    }
    
    public void logVastTag(final String s, final String s2, final String s3) {
        if (s3.equals("fail")) {
            final Intent intent = new Intent();
            intent.setAction("com.tremorvideo.logger.BroadcastFail");
            intent.putExtra("msg", "vast_tag");
            ac.x().sendBroadcast(intent);
        }
    }
    
    public void logVideoPlaybackStart(final String s, final String s2, final String s3) {
    }
    
    public void logVideoPlaybackStatus(final String s, final String s2, final String s3) {
    }
    
    public void logtracking(final String s, final String s2, final String s3) {
        this.log("tracking_fired", s, s2, s3);
    }
    
    public void resetDB() {
        if (this.a != null) {
            this.a.dropTable();
        }
    }
}
