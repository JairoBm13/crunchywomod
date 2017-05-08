// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.tracking;

import com.crunchyroll.crunchyroid.app.ApplicationState;
import com.facebook.AppEventsLogger;
import android.content.Context;

public class FacebookTracker
{
    public static void addToQueue(final Context context) {
        AppEventsLogger.newLogger(context).logEvent("fb_mobile_add_to_wishlist");
    }
    
    public static void freetrial(final Context context) {
        AppEventsLogger.newLogger(context).logEvent("fb_mobile_purchase");
    }
    
    public static void install(final Context context) {
        AppEventsLogger.activateApp(context, context.getResources().getString(2131165404));
    }
    
    public static void login(final Context context) {
        AppEventsLogger.newLogger(context).logEvent("fb_mobile_tutorial_completion");
    }
    
    public static void signup(final Context context) {
        AppEventsLogger.newLogger(context).logEvent("fb_mobile_complete_registration");
    }
    
    public static void videoStart(final Context context) {
        final ApplicationState value = ApplicationState.get(context);
        if (value.getNumVideoViews() < 0) {
            AppEventsLogger.newLogger(context).logEvent("fb_mobile_content_view");
            value.setNumVideoViews(0);
        }
    }
    
    public static void videoView(final Context context) {
        final ApplicationState value = ApplicationState.get(context);
        final int numVideoViews = value.getNumVideoViews() + 1;
        value.setNumVideoViews(numVideoViews);
        final AppEventsLogger logger = AppEventsLogger.newLogger(context);
        switch (numVideoViews) {
            default: {}
            case 1: {
                logger.logEvent("fb_mobile_add_to_cart");
            }
            case 2: {
                logger.logEvent("fb_mobile_initiated_checkout");
            }
            case 5: {
                logger.logEvent("fb_mobile_achievement_unlocked");
            }
        }
    }
}
