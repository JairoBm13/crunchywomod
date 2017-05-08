// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.activities;

import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.app.AlertDialog$Builder;
import android.view.View;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import com.crunchyroll.crunchyroid.events.CardTypeEvent;
import de.greenrobot.event.EventBus;
import android.widget.RadioGroup$OnCheckedChangeListener;
import android.widget.RadioGroup;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.BitmapDrawable;
import android.widget.PopupWindow;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import android.widget.TextView;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.view.MenuItem;
import com.swrve.sdk.SwrveSDK;
import android.support.v7.app.MediaRouteDialogFactory;
import com.crunchyroll.video.util.CustomRouteDialogFactory;
import android.annotation.TargetApi;
import android.view.Window;
import com.swrve.sdk.SwrveSDKBase;
import android.os.Build$VERSION;
import android.app.Activity;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import com.crunchyroll.crunchyroid.app.ApplicationState;
import com.crunchyroll.cast.CastState;
import com.crunchyroll.cast.CastHandler;
import android.view.KeyEvent;
import com.crunchyroll.android.util.LoggerFactory;
import android.content.BroadcastReceiver;
import android.view.Menu;
import com.crunchyroll.android.util.Logger;
import android.support.v7.app.AppCompatActivity;

public class TrackedActivity extends AppCompatActivity
{
    public static final int MENU_POSITION_CHROMECAST = 0;
    public static final int MENU_POSITION_OVERFLOW = 4;
    public static final int MENU_POSITION_SEARCH = 2;
    public static final int MENU_POSITION_SEND = 3;
    public static final int MENU_POSITION_SHARE = 1;
    protected final Logger log;
    protected Menu mMenu;
    protected BroadcastReceiver mSessionExpiredReceiver;
    
    public TrackedActivity() {
        this.log = LoggerFactory.getLogger(this.getClass());
    }
    
    public boolean dispatchKeyEvent(final KeyEvent keyEvent) {
        final CastHandler value = CastHandler.get();
        if (value != null && value.getState() == CastState.CONNECTED) {
            if (keyEvent.getKeyCode() == 24) {
                if (keyEvent.getAction() == 0) {
                    value.increaseVolume();
                }
            }
            else {
                if (keyEvent.getKeyCode() != 25) {
                    return super.dispatchKeyEvent(keyEvent);
                }
                if (keyEvent.getAction() == 0) {
                    value.decreaseVolume();
                    return true;
                }
            }
            return true;
        }
        return super.dispatchKeyEvent(keyEvent);
    }
    
    protected ApplicationState getApplicationState() {
        return CrunchyrollApplication.getApp((Context)this).getApplicationState();
    }
    
    @TargetApi(21)
    @Override
    protected void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.mSessionExpiredReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                TrackedActivity.this.onSessionExpired();
            }
        };
        LocalBroadcastManager.getInstance((Context)this).registerReceiver(this.mSessionExpiredReceiver, new IntentFilter("SessionExpired"));
        CrunchyrollApplication.tremorBind(this);
        if (Build$VERSION.SDK_INT >= 21) {
            final Window window = this.getWindow();
            window.addFlags(Integer.MIN_VALUE);
            window.clearFlags(67108864);
            window.setStatusBarColor(this.getResources().getColor(2131558479));
        }
        SwrveSDKBase.onCreate(this);
    }
    
    public boolean onCreateOptionsMenu(final Menu mMenu) {
        this.getMenuInflater().inflate(2131689474, mMenu);
        this.mMenu = mMenu;
        if (CastHandler.isCastSupported((Context)this)) {
            CastHandler.get().setRouteSelector(mMenu.findItem(2131624355), new CustomRouteDialogFactory((Context)this));
        }
        return super.onCreateOptionsMenu(mMenu);
    }
    
    @Override
    protected void onDestroy() {
        if (this.mSessionExpiredReceiver != null) {
            LocalBroadcastManager.getInstance((Context)this).unregisterReceiver(this.mSessionExpiredReceiver);
        }
        CrunchyrollApplication.tremorUnbind();
        super.onDestroy();
        SwrveSDKBase.onDestroy(this);
    }
    
    public void onEvent(final Object o) {
    }
    
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        SwrveSDK.onLowMemory();
    }
    
    public boolean onOptionsItemSelected(final MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            this.finish();
            return true;
        }
        if (menuItem.getItemId() == 2131624358) {
            final View inflate = LayoutInflater.from((Context)this).inflate(2130903169, (ViewGroup)null);
            final TextView textView = (TextView)inflate.findViewById(2131624020);
            final TextView textView2 = (TextView)inflate.findViewById(2131624336);
            final TextView textView3 = (TextView)inflate.findViewById(2131624337);
            final TextView textView4 = (TextView)inflate.findViewById(2131624338);
            textView.setText((CharSequence)LocalizedStrings.SWITCH_VIEW.get());
            textView2.setText((CharSequence)LocalizedStrings.SIZE_LIST.get());
            textView3.setText((CharSequence)LocalizedStrings.SIZE_SMALL.get());
            textView4.setText((CharSequence)LocalizedStrings.SIZE_LARGE.get());
            final PopupWindow popupWindow = new PopupWindow((Context)this);
            popupWindow.setWidth(-2);
            popupWindow.setHeight(-2);
            popupWindow.setBackgroundDrawable((Drawable)new BitmapDrawable());
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setContentView(inflate);
            final View viewById = this.findViewById(2131624358);
            inflate.measure(0, 0);
            popupWindow.showAsDropDown(viewById, viewById.getWidth() - inflate.getMeasuredWidth() - 20, -viewById.getHeight() + 20);
            final RadioGroup radioGroup = (RadioGroup)inflate.findViewById(2131624335);
            final int cardType = CrunchyrollApplication.getApp((Context)this).getApplicationState().getCardType();
            if (cardType == 1) {
                radioGroup.check(2131624336);
            }
            else if (cardType == 2) {
                radioGroup.check(2131624337);
            }
            else if (cardType == 3) {
                radioGroup.check(2131624338);
            }
            radioGroup.setOnCheckedChangeListener((RadioGroup$OnCheckedChangeListener)new RadioGroup$OnCheckedChangeListener() {
                public void onCheckedChanged(final RadioGroup radioGroup, final int n) {
                    final ApplicationState applicationState = CrunchyrollApplication.getApp((Context)TrackedActivity.this).getApplicationState();
                    if (2131624336 == n) {
                        applicationState.setCardType(1);
                        EventBus.getDefault().post(new CardTypeEvent(1));
                        Tracker.settingsSwitchView((Context)TrackedActivity.this, 1);
                    }
                    else {
                        if (2131624337 == n) {
                            applicationState.setCardType(2);
                            EventBus.getDefault().post(new CardTypeEvent(2));
                            Tracker.settingsSwitchView((Context)TrackedActivity.this, 2);
                            return;
                        }
                        if (2131624338 == n) {
                            applicationState.setCardType(3);
                            EventBus.getDefault().post(new CardTypeEvent(3));
                            Tracker.settingsSwitchView((Context)TrackedActivity.this, 3);
                        }
                    }
                }
            });
        }
        else if (menuItem.getItemId() == 2131624356) {
            this.onSearch();
        }
        return super.onOptionsItemSelected(menuItem);
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        SwrveSDK.onPause();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        SwrveSDKBase.onResume(this);
    }
    
    protected void onSearch() {
    }
    
    protected void onSessionExpired() {
        final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)this);
        alertDialog$Builder.setCancelable(false);
        alertDialog$Builder.setMessage((CharSequence)this.getString(2131165534));
        alertDialog$Builder.setPositiveButton((CharSequence)LocalizedStrings.OK.get(), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
            public void onClick(final DialogInterface dialogInterface, final int n) {
                dialogInterface.dismiss();
            }
        });
        alertDialog$Builder.create().show();
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        CrunchyrollApplication.getApp((Context)this).onActivityDidStart();
        this.trackView();
        EventBus.getDefault().register(this);
    }
    
    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        CrunchyrollApplication.getApp((Context)this).onActivityDidStop();
        super.onStop();
    }
    
    protected void trackView() {
        Tracker.trackView(this);
    }
}
