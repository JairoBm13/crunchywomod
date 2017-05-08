// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.util;

import android.content.Intent;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import de.greenrobot.event.EventBus;
import com.crunchyroll.crunchyroid.events.ErrorEvent;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import android.content.Context;
import com.crunchyroll.android.api.ApiManager;
import com.crunchyroll.android.api.models.QueueEntry;
import com.crunchyroll.android.api.tasks.ApiTaskListener;
import com.crunchyroll.android.api.models.Series;
import com.crunchyroll.crunchyroid.app.ApplicationState;
import android.app.Activity;
import com.crunchyroll.android.util.LoggerFactory;
import com.crunchyroll.android.util.Logger;

public class ActionsUtil
{
    private static final String SHARE_URL_PARAMS = "utm_source=android&utm_campaign=socialshare";
    private static final Logger log;
    
    static {
        log = LoggerFactory.getLogger(ActionsUtil.class);
    }
    
    public static String addToQueue(final Activity activity, final ApplicationState applicationState, final Series series, final ApiTaskListener<QueueEntry> apiTaskListener) {
        if (applicationState.hasLoggedInUser()) {
            return ApiManager.getInstance(activity).addToQueue(series.getSeriesId(), apiTaskListener);
        }
        return null;
    }
    
    public static void share(final Context context, final Integer n, final String s, final String s2, final String s3) {
        if (s3 == null || s == null) {
            if (s3 == null) {
                ActionsUtil.log.error("The url supplied for sharing was null!", new Object[0]);
            }
            if (s == null) {
                ActionsUtil.log.error("The series name supplied for sharing was null!", new Object[0]);
            }
            ErrorEvent errorEvent;
            if (n != null) {
                errorEvent = new ErrorEvent(n, LocalizedStrings.SHARE_FAILED.get());
            }
            else {
                errorEvent = new ErrorEvent(LocalizedStrings.SHARE_FAILED.get());
            }
            EventBus.getDefault().post(errorEvent);
            return;
        }
        String s4;
        if (s3.contains("?")) {
            s4 = s3 + "&utm_source=android&utm_campaign=socialshare";
        }
        else {
            s4 = s3 + "?utm_source=android&utm_campaign=socialshare";
        }
        if (s != null && s2 != null) {
            Tracker.shareEpisode(context, s, s2);
        }
        else {
            Tracker.shareSeries(context, s);
        }
        final Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.TEXT", s4);
        intent.putExtra("android.intent.extra.SUBJECT", LocalizedStrings.SHARING_SUBJECT.get());
        context.startActivity(Intent.createChooser(intent, (CharSequence)LocalizedStrings.SHARE.get()));
    }
    
    public static void share(final Context context, final String s, final String s2, final String s3) {
        share(context, null, s, s2, s3);
    }
}
