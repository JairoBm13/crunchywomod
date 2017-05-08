// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.tracking;

import com.crunchyroll.crunchyroid.util.UrlUtils;
import com.crashlytics.android.Crashlytics;
import junit.framework.Assert;
import java.util.Iterator;
import java.util.Map;
import com.google.android.gms.analytics.HitBuilders;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import android.content.Context;
import com.crunchyroll.android.util.LoggerFactory;
import com.crunchyroll.android.util.Logger;

public class GATracker
{
    public static final int CUSTOM_DIMENSION_AUTO_PLAY_ENABLED = 3;
    public static final int CUSTOM_DIMENSION_USER_TYPE = 1;
    public static final int CUSTOM_DIMENSION_VIDEO_PLAYER_TYPE = 2;
    private static final Logger LOG;
    
    static {
        LOG = LoggerFactory.getLogger(Tracker.class);
    }
    
    public static int getStringIdentifier(final Context context, final String s) {
        return context.getResources().getIdentifier(s, "string", context.getPackageName());
    }
    
    public static void trackEvent(final Context context, final String category, final String action, final String label, final Long n) {
        GATracker.LOG.verbose("Track event: category:%s, action:%s label:%s", category, action, label);
        final com.google.android.gms.analytics.Tracker gaTracker = CrunchyrollApplication.getApp(context).getGATracker();
        final HitBuilders.EventBuilder setAction = new HitBuilders.EventBuilder().setCategory(category).setAction(action);
        if (label != null) {
            setAction.setLabel(label);
        }
        if (n != null) {
            setAction.setValue(n);
        }
        final Map<Integer, String> customDimensions = CrunchyrollApplication.getApp(context).getCustomDimensions();
        if (customDimensions != null) {
            GATracker.LOG.verbose("Track event: dimensions", new Object[0]);
            for (final Map.Entry<Integer, String> entry : customDimensions.entrySet()) {
                GATracker.LOG.verbose("Track event: dimension:%s, value:%s", entry.getKey(), entry.getValue());
                setAction.setCustomDimension(entry.getKey(), entry.getValue());
            }
        }
        gaTracker.send(setAction.build());
    }
    
    public static void trackView(final Context context, final String screenName) {
        if (context == null) {
            Assert.fail("Trying to track with a null context");
        }
        else {
            GATracker.LOG.verbose("Track view: %s", screenName);
            final com.google.android.gms.analytics.Tracker gaTracker = CrunchyrollApplication.getApp(context).getGATracker();
            gaTracker.setScreenName(screenName);
            final HitBuilders.AppViewBuilder appViewBuilder = new HitBuilders.AppViewBuilder();
            final Map<Integer, String> customDimensions = CrunchyrollApplication.getApp(context).getCustomDimensions();
            if (customDimensions != null) {
                for (final Map.Entry<Integer, String> entry : customDimensions.entrySet()) {
                    appViewBuilder.setCustomDimension(entry.getKey(), entry.getValue());
                }
            }
            gaTracker.send(appViewBuilder.build());
            if (!CrunchyrollApplication.isDebuggable()) {
                Crashlytics.setString("last_viewed_screen", screenName);
            }
        }
    }
    
    public static void trackView(final Context context, final String[] array) {
        trackView(context, UrlUtils.getScreenUriFromComponents(array));
    }
}
