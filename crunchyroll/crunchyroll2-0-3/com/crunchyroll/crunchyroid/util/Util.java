// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.util;

import com.google.common.base.Function;
import com.crunchyroll.video.activities.VideoPlayerActivity;
import android.view.ViewGroup$LayoutParams;
import android.view.LayoutInflater;
import com.google.common.base.Optional;
import android.content.res.Resources;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.DialogInterface$OnShowListener;
import com.crunchyroll.android.api.tasks.ApiTaskListener;
import com.crunchyroll.crunchyroid.activities.MainActivity;
import com.crunchyroll.android.api.exceptions.ApiErrorException;
import com.crunchyroll.android.api.tasks.BaseListener;
import com.crunchyroll.android.api.ApiManager;
import android.text.TextUtils;
import com.crunchyroll.android.api.models.User;
import com.crunchyroll.crunchyroid.app.ApplicationState;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.app.AlertDialog$Builder;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager$NameNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.MessageDigest;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import com.crunchyroll.cast.model.CastInfo;
import com.crunchyroll.cast.CastHandler;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import com.crunchyroll.android.api.models.EpisodeInfo;
import android.view.View$OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.text.ParseException;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import com.crunchyroll.android.api.models.ImageSet;

public final class Util
{
    public static String chooseLargestImage(final ImageSet set) {
        if (set.getFullUrl().isPresent()) {
            return set.getFullUrl().get();
        }
        if (set.getLargeUrl().isPresent()) {
            return set.getLargeUrl().get();
        }
        if (set.getMediumUrl().isPresent()) {
            return set.getMediumUrl().get();
        }
        if (set.getSmallUrl().isPresent()) {
            return set.getSmallUrl().get();
        }
        if (set.getThumbUrl().isPresent()) {
            return set.getThumbUrl().get();
        }
        return "";
    }
    
    public static String createAvailableTimeInfo(String string) throws ParseException {
        final String s = "";
        final long n = toCalendar(string).getTimeInMillis() - System.currentTimeMillis();
        string = s;
        if (n > 0L) {
            final long n2 = n / 60000L;
            final long n3 = n / 3600000L;
            final long n4 = n / 86400000L;
            final long n5 = n3 % 24L;
            final long n6 = n2 % 60L;
            if (n4 > 0L) {
                if (n4 <= 1L) {
                    return "" + String.format(LocalizedStrings.DAY_FROM_NOW.get(), n4);
                }
                string = "" + String.format(LocalizedStrings.DAYS_FROM_NOW.get(), n4);
            }
            else if (n5 > 0L) {
                if (n5 > 1L) {
                    return "" + String.format(LocalizedStrings.HOURS_FROM_NOW.get(), n5);
                }
                return "" + String.format(LocalizedStrings.HOUR_FROM_NOW.get(), n5);
            }
            else {
                string = s;
                if (n6 > 0L) {
                    if (n6 > 1L) {
                        return "" + String.format(LocalizedStrings.MINS_FROM_NOW.get(), n6);
                    }
                    return "" + String.format(LocalizedStrings.MIN_FROM_NOW.get(), n6);
                }
            }
        }
        return string;
    }
    
    public static String createUpdatedTimeInfo(String s) throws ParseException {
        final String s2 = "";
        final Calendar instance = Calendar.getInstance();
        instance.setTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(s));
        final Calendar instance2 = Calendar.getInstance();
        instance2.set(11, 0);
        instance2.set(12, 0);
        instance2.set(13, 0);
        instance2.set(14, 0);
        final long n = instance2.getTimeInMillis() - instance.getTimeInMillis();
        if (n <= 0L) {
            return LocalizedStrings.JUST_ARRIVED.get().toUpperCase();
        }
        if (n / 86400000L == 0L) {
            return LocalizedStrings.YESTERDAY.get().toUpperCase();
        }
        switch (instance.get(7)) {
            default: {
                s = s2;
                break;
            }
            case 1: {
                s = LocalizedStrings.SUNDAY.get().toUpperCase();
                break;
            }
            case 2: {
                s = LocalizedStrings.MONDAY.get().toUpperCase();
                break;
            }
            case 3: {
                s = LocalizedStrings.TUESDAY.get().toUpperCase();
                break;
            }
            case 4: {
                s = LocalizedStrings.WEDNESDAY.get().toUpperCase();
                break;
            }
            case 5: {
                s = LocalizedStrings.THURSDAY.get().toUpperCase();
                break;
            }
            case 6: {
                s = LocalizedStrings.FRIDAY.get().toUpperCase();
                break;
            }
            case 7: {
                s = LocalizedStrings.SATURDAY.get().toUpperCase();
                break;
            }
        }
        return s + " " + (instance.get(2) + 1) + "/" + instance.get(5);
    }
    
    public static int dayOfWeek(final String s) throws ParseException {
        final Calendar instance = Calendar.getInstance();
        instance.setTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(s));
        return instance.get(7);
    }
    
    public static int dpToPx(final Context context, final int n) {
        return Math.round(n * context.getResources().getDisplayMetrics().density);
    }
    
    public static String getDateInfo(final String s) throws ParseException {
        final Calendar instance = Calendar.getInstance();
        instance.setTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(s));
        return instance.get(2) + 1 + "/" + instance.get(5) + "/" + instance.get(1);
    }
    
    public static Intent getLaunchIntentForPackage(final Context context, final String s) {
        return context.getPackageManager().getLaunchIntentForPackage(s);
    }
    
    public static void hideProgressBar(final Activity activity) {
        if (activity != null) {
            hideProgressBar((Context)activity, (ViewGroup)((ViewGroup)activity.findViewById(16908290)).getChildAt(0));
        }
    }
    
    public static void hideProgressBar(final Context context, final ViewGroup viewGroup) {
        if (viewGroup != null) {
            final View viewWithTag = viewGroup.findViewWithTag((Object)context.getResources().getString(2131165466));
            if (viewWithTag != null) {
                viewGroup.removeView(viewWithTag);
            }
        }
    }
    
    public static void hideProgressBarHideChildren(final Context context, final ViewGroup viewGroup) {
        hideProgressBar(context, viewGroup);
        for (int i = 0; i < viewGroup.getChildCount(); ++i) {
            viewGroup.getChildAt(i).setVisibility(0);
        }
    }
    
    public static void initCastBar(final ViewGroup viewGroup) {
        if (viewGroup != null) {
            viewGroup.findViewById(2131624096).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    final EpisodeInfo episodeInfo = CrunchyrollApplication.getApp(viewGroup.getContext()).getCurrentlyCastingEpisode().orNull();
                    Tracker.rewindChromecast(viewGroup.getContext(), episodeInfo.getMedia().getSeriesName().or(""), "episode-" + episodeInfo.getMedia().getEpisodeNumber());
                    CastHandler.get().seekTo(Math.max(0L, CastHandler.get().getPlayhead() - 10000L));
                }
            });
            viewGroup.findViewById(2131624097).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    if (CastHandler.get() != null) {
                        if (!CastHandler.get().isPlaying()) {
                            CastHandler.get().resume();
                            view.setSelected(false);
                            return;
                        }
                        CastHandler.get().pause();
                        view.setSelected(true);
                    }
                }
            });
            viewGroup.findViewById(2131624098).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    final EpisodeInfo episodeInfo = CrunchyrollApplication.getApp(viewGroup.getContext()).getCurrentlyCastingEpisode().orNull();
                    Tracker.fastForwardChromecast(viewGroup.getContext(), episodeInfo.getMedia().getSeriesName().or(""), "episode-" + episodeInfo.getMedia().getEpisodeNumber());
                    CastHandler.get().seekTo(Math.min(CastHandler.get().getCurrentCastInfo().get().getDuration() * 1000 - 2000, CastHandler.get().getPlayhead() + 10000L));
                }
            });
        }
    }
    
    public static boolean isOnWifiOrEthernet(final Context context) {
        boolean b = false;
        final ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
        final NetworkInfo networkInfo = connectivityManager.getNetworkInfo(1);
        final NetworkInfo networkInfo2 = connectivityManager.getNetworkInfo(9);
        final boolean connected = networkInfo.isConnected();
        final boolean b2 = networkInfo2 != null && networkInfo2.isConnected();
        if (connected || b2) {
            b = true;
        }
        return b;
    }
    
    public static final String md5(String s) {
        try {
            final MessageDigest instance = MessageDigest.getInstance("MD5");
            instance.update(s.getBytes());
            final byte[] digest = instance.digest();
            final StringBuffer sb = new StringBuffer();
            for (int i = 0; i < digest.length; ++i) {
                for (s = Integer.toHexString(digest[i] & 0xFF); s.length() < 2; s = "0" + s) {}
                sb.append(s);
            }
            s = sb.toString();
            return s;
        }
        catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
            return "";
        }
    }
    
    public static boolean packageExists(final Context context, final String s) {
        final PackageManager packageManager = context.getPackageManager();
        try {
            packageManager.getPackageInfo(s, 128);
            return true;
        }
        catch (PackageManager$NameNotFoundException ex) {
            return false;
        }
    }
    
    public static String secondsToString(int n) {
        final int n2 = n / 3600;
        final int n3 = n / 60 % 60;
        n %= 60;
        if (n2 > 0) {
            return String.format("%d:%02d:%02d", n2, n3, n);
        }
        return String.format("%d:%02d", n3, n);
    }
    
    @SuppressLint({ "InlinedApi" })
    public static void setOrientation(final Activity activity, final Orientation orientation) {
        if (activity.getPackageManager().hasSystemFeature("com.google.android.tv")) {
            activity.setRequestedOrientation(0);
            return;
        }
        switch (orientation) {
            default: {}
            case PORTRAIT: {
                activity.setRequestedOrientation(7);
            }
            case LANDSCAPE: {
                activity.setRequestedOrientation(6);
            }
            case ANY: {
                activity.setRequestedOrientation(2);
            }
        }
    }
    
    public static void showErrorPopup(final Context context, final String message) {
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder(context);
        alertDialog$Builder.setCancelable(false);
        alertDialog$Builder.setPositiveButton((CharSequence)LocalizedStrings.BACK.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                dialogInterface.dismiss();
            }
        });
        alertDialog$Builder.setMessage((CharSequence)message);
        alertDialog$Builder.show();
    }
    
    public static void showLogoutPrompt(final Activity activity) {
        final ApplicationState value = ApplicationState.get((Context)activity);
        final Optional<User> loggedInUser = value.getLoggedInUser();
        String s;
        if (!TextUtils.isEmpty((CharSequence)loggedInUser.get().getUsername())) {
            s = loggedInUser.get().getUsername();
        }
        else {
            s = loggedInUser.get().getEmail();
        }
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)activity);
        alertDialog$Builder.setCancelable(false);
        alertDialog$Builder.setTitle((CharSequence)LocalizedStrings.LOG_OUT_QUESTION.get());
        alertDialog$Builder.setMessage((CharSequence)String.format(LocalizedStrings.YOU_ARE_SIGNED_IN_AS.get(), s));
        alertDialog$Builder.setNegativeButton((CharSequence)LocalizedStrings.CANCEL.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                dialogInterface.dismiss();
            }
        });
        alertDialog$Builder.setPositiveButton((CharSequence)LocalizedStrings.YES.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                dialogInterface.dismiss();
                ApiManager.getInstance(activity).logout(value.getAuth(), new BaseListener<Void>() {
                    @Override
                    public void onException(final Exception ex) {
                        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)activity);
                        alertDialog$Builder.setCancelable(false);
                        if (ex instanceof ApiErrorException) {
                            alertDialog$Builder.setMessage((CharSequence)ex.getLocalizedMessage());
                        }
                        else {
                            alertDialog$Builder.setMessage((CharSequence)LocalizedStrings.ERROR_UNKNOWN.get());
                        }
                        alertDialog$Builder.setPositiveButton((CharSequence)LocalizedStrings.BACK.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                            public void onClick(final DialogInterface dialogInterface, final int n) {
                                dialogInterface.dismiss();
                            }
                        });
                        alertDialog$Builder.show();
                    }
                    
                    @Override
                    public void onFinally() {
                        value.clearAuth();
                        value.clearLoggedInUser();
                    }
                    
                    @Override
                    public void onSuccess(final Void void1) {
                        if (activity instanceof MainActivity) {
                            Tracker.logOut((Context)activity, "side-menu");
                        }
                        else {
                            Tracker.logOut((Context)activity, "settings");
                        }
                        value.clearAuth();
                        value.clearLoggedInUser();
                        if (activity instanceof MainActivity) {
                            ((MainActivity)activity).closeDrawer();
                        }
                    }
                });
            }
        });
        final AlertDialog create = alertDialog$Builder.create();
        create.requestWindowFeature(1);
        create.setOnShowListener((DialogInterface$OnShowListener)new DialogInterface$OnShowListener() {
            public void onShow(final DialogInterface dialogInterface) {
                final Resources resources = activity.getResources();
                final View viewById = create.findViewById(resources.getIdentifier("alertTitle", "id", "android"));
                if (viewById != null) {
                    ((TextView)viewById).setTextColor(resources.getColor(2131558417));
                }
                ((TextView)create.findViewById(16908299)).setTextColor(resources.getColor(2131558476));
                final View viewById2 = create.findViewById(activity.getResources().getIdentifier("titleDivider", "id", "android"));
                if (viewById2 != null) {
                    viewById2.setVisibility(8);
                }
                create.getButton(-2).setTextColor(resources.getColor(2131558477));
                create.getButton(-1).setTextColor(resources.getColor(2131558477));
            }
        });
        create.show();
    }
    
    public static void showProgressBar(final Activity activity, final int n) {
        showProgressBar((Context)activity, (ViewGroup)((ViewGroup)activity.findViewById(16908290)).getChildAt(0), n);
    }
    
    public static void showProgressBar(final Context context, final ViewGroup viewGroup, final int backgroundColor) {
        final View inflate = LayoutInflater.from(context).inflate(2130903153, viewGroup, false);
        inflate.setLayoutParams(new ViewGroup$LayoutParams(-1, -1));
        inflate.setBackgroundColor(backgroundColor);
        viewGroup.addView(inflate, viewGroup.getChildCount());
    }
    
    public static void showProgressBarHideChildren(final Context context, final ViewGroup viewGroup, final int n) {
        for (int i = 0; i < viewGroup.getChildCount(); ++i) {
            viewGroup.getChildAt(i).setVisibility(4);
        }
        showProgressBar(context, viewGroup, n);
    }
    
    @SuppressLint({ "DefaultLocale" })
    public static String stringFromDuration(int n) {
        final int n2 = n / 1000;
        n = n2 / 3600;
        final int n3 = n2 - n * 60 * 60;
        final int n4 = n3 / 60;
        final int n5 = n3 - n4 * 60;
        if (n > 0) {
            return String.format("%d:%2d:%2d", n, n4, n5);
        }
        return String.format("%d:%02d", n4, n5);
    }
    
    public static Calendar toCalendar(String s) throws ParseException {
        final Calendar instance = Calendar.getInstance();
        s = s.replace("Z", "+00:00");
        try {
            s = s.substring(0, 22) + s.substring(23);
            instance.setTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").parse(s));
            return instance;
        }
        catch (IndexOutOfBoundsException ex) {
            throw new ParseException("Invalid length", 0);
        }
    }
    
    public static void updateCastBar(final Activity activity, final ViewGroup viewGroup) {
        if (viewGroup != null) {
            final EpisodeInfo episodeInfo = CrunchyrollApplication.getApp((Context)activity).getCurrentlyCastingEpisode().orNull();
            if (!CastHandler.get().isShowingMedia() || episodeInfo == null) {
                viewGroup.findViewById(2131624092).setVisibility(8);
                return;
            }
            final View viewById = viewGroup.findViewById(2131624097);
            if (CastHandler.get().isPlaying()) {
                viewById.setSelected(false);
            }
            else {
                viewById.setSelected(true);
            }
            viewGroup.findViewById(2131624093).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    VideoPlayerActivity.start(activity, episodeInfo, true, 0);
                }
            });
            final TextView textView = (TextView)viewGroup.findViewById(2131624094);
            final TextView textView2 = (TextView)viewGroup.findViewById(2131624095);
            textView.setText((CharSequence)(LocalizedStrings.EP.get(episodeInfo.getMedia().getEpisodeNumber()) + " - " + episodeInfo.getMedia().getSeriesName().get()));
            final String value = LocalizedStrings.CASTING_TO_W_TEXT.get();
            String deviceName;
            if (CastHandler.get() == null) {
                deviceName = "";
            }
            else {
                deviceName = CastHandler.get().getDeviceName();
            }
            textView2.setText((CharSequence)String.format(value, deviceName));
            if (viewGroup.findViewById(2131624092).getVisibility() == 8) {
                Tracker.swrveScreenView("chromecast-control");
            }
            viewGroup.findViewById(2131624092).setVisibility(0);
        }
    }
    
    public static <T> Optional<String> wrapOptionalInString(final String s, final Optional<T> optional) {
        return optional.transform((Function<? super T, String>)new Function<T, String>() {
            @Override
            public String apply(final T t) {
                return String.format(s, t);
            }
        });
    }
    
    public enum Orientation
    {
        ANY, 
        LANDSCAPE, 
        PORTRAIT;
    }
}
