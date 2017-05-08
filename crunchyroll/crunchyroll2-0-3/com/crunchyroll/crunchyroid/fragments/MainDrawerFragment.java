// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.fragments;

import android.widget.ArrayAdapter;
import com.google.common.base.Optional;
import com.crunchyroll.crunchyroid.activities.SettingsActivity;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import com.crunchyroll.android.api.models.User;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.os.Bundle;
import android.net.Uri;
import android.content.Intent;
import com.crunchyroll.crunchyroid.util.Util;
import android.widget.AdapterView;
import android.widget.AdapterView$OnItemClickListener;
import android.widget.ListAdapter;
import com.crunchyroll.android.api.models.RelatedApp;
import com.crunchyroll.android.api.tasks.BaseListener;
import com.crunchyroll.android.api.models.MembershipInfoItem;
import com.crunchyroll.android.api.models.FreeTrialInformationItem;
import java.util.List;
import com.crunchyroll.android.api.tasks.ApiTaskListener;
import com.crunchyroll.android.api.ApiManager;
import android.app.Activity;
import com.crunchyroll.crunchyroid.activities.MainActivity;
import com.crunchyroll.crunchyroid.app.PrepareToWatch;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import android.view.View$OnClickListener;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import android.content.Context;
import com.crunchyroll.crunchyroid.app.ApplicationState;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;

public class MainDrawerFragment extends BaseFragment
{
    private View mBottomBarAlreadyPremium;
    private View mBottomBarTryPremium;
    private boolean mHasFreeTrialInfo;
    private boolean mIsFreeTrial;
    private View mLogin;
    private TextView mLoginText;
    private ListView mOtherAppsLV;
    private View mSettings;
    private TextView mSettingsText;
    private TextView mThankYou;
    private int mTrialDuration;
    private String mTrialSpan;
    private TextView mTryPremium;
    private View mUpgrade;
    private TextView mUpgradeText;
    
    private void finalizeBottomMessage() {
        if (ApplicationState.get((Context)this.getActivity()).isAnyPremium()) {
            this.mUpgrade.setVisibility(8);
            this.mBottomBarTryPremium.setVisibility(8);
            this.mBottomBarAlreadyPremium.setVisibility(0);
            this.mThankYou.setText((CharSequence)LocalizedStrings.THANK_YOU_FOR_BEING_PREMIUM.get());
            return;
        }
        this.mBottomBarTryPremium.setVisibility(0);
        this.mBottomBarAlreadyPremium.setVisibility(8);
        if (this.mIsFreeTrial) {
            this.mTryPremium.setText((CharSequence)String.format(LocalizedStrings.HOME_MENU_UPSELL.get(), this.mTrialDuration, LocalizedStrings.getFromToken(this.mTrialSpan)));
        }
        else {
            this.mTryPremium.setText((CharSequence)LocalizedStrings.GO_PREMIUM.get());
        }
        this.mUpgradeText.setText((CharSequence)LocalizedStrings.HOME_MENU_UPGRADE.get());
        this.mUpgrade.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                Tracker.drawerUpgrade((Context)MainDrawerFragment.this.getActivity());
                MainActivity.signup(MainDrawerFragment.this.getActivity(), PrepareToWatch.Type.PREPARE_UPSELL_NONE, true);
            }
        });
        this.mBottomBarTryPremium.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                Tracker.drawerHimeUpsell((Context)MainDrawerFragment.this.getActivity());
                Tracker.swrveScreenView("side-menu-upsell");
                MainActivity.signup(MainDrawerFragment.this.getActivity(), PrepareToWatch.Type.PREPARE_UPSELL_NONE, true);
            }
        });
    }
    
    private void getFreeTrialInfo() {
        ApiManager.getInstance(this.getActivity()).getFreeTrialInfo(new ApiTaskListener<List<FreeTrialInformationItem>>() {
            @Override
            public void onCancel() {
            }
            
            @Override
            public void onException(final Exception ex) {
                MainDrawerFragment.this.getMembershipInfo();
            }
            
            @Override
            public void onFinally() {
            }
            
            @Override
            public void onInterrupted(final Exception ex) {
            }
            
            @Override
            public void onPreExecute() {
            }
            
            @Override
            public void onSuccess(final List<FreeTrialInformationItem> list) {
                if (list == null || list.size() == 0) {
                    this.onException(new ArrayIndexOutOfBoundsException("No free trial info available"));
                    return;
                }
                final FreeTrialInformationItem freeTrialInformationItem = list.get(0);
                MainDrawerFragment.this.mHasFreeTrialInfo = true;
                MainDrawerFragment.this.mIsFreeTrial = true;
                MainDrawerFragment.this.mTrialDuration = freeTrialInformationItem.getDuration();
                MainDrawerFragment.this.mTrialSpan = freeTrialInformationItem.getSpanType();
                MainDrawerFragment.this.finalizeBottomMessage();
            }
        });
    }
    
    private void getMembershipInfo() {
        ApiManager.getInstance(this.getActivity()).getMembershipInfo(new ApiTaskListener<List<MembershipInfoItem>>() {
            @Override
            public void onCancel() {
            }
            
            @Override
            public void onException(final Exception ex) {
            }
            
            @Override
            public void onFinally() {
            }
            
            @Override
            public void onInterrupted(final Exception ex) {
                this.onException(ex);
            }
            
            @Override
            public void onPreExecute() {
            }
            
            @Override
            public void onSuccess(final List<MembershipInfoItem> list) {
                if (list == null || list.size() == 0) {
                    this.onException(new ArrayIndexOutOfBoundsException("No membership info available"));
                    return;
                }
                final MembershipInfoItem membershipInfoItem = list.get(0);
                MainDrawerFragment.this.mHasFreeTrialInfo = true;
                MainDrawerFragment.this.mIsFreeTrial = false;
                MainDrawerFragment.this.mTrialDuration = membershipInfoItem.getDuration();
                MainDrawerFragment.this.mTrialSpan = membershipInfoItem.getSpanType();
                MainDrawerFragment.this.finalizeBottomMessage();
            }
        });
    }
    
    public static MainDrawerFragment newInstance() {
        return new MainDrawerFragment();
    }
    
    private void populateRelatedApps() {
        if (ApplicationState.get((Context)this.getActivity()).getCustomLocale().startsWith("en")) {
            ApiManager.getInstance(this.getActivity()).getRelatedApps(new BaseListener<List<RelatedApp>>() {
                @Override
                public void onException(final Exception ex) {
                }
                
                @Override
                public void onSuccess(final List<RelatedApp> list) {
                    MainDrawerFragment.this.mOtherAppsLV.setAdapter((ListAdapter)new MenuItemAdapter((Context)MainDrawerFragment.this.getActivity(), 2130903138, list));
                    MainDrawerFragment.this.mOtherAppsLV.setOnItemClickListener((AdapterView$OnItemClickListener)new AdapterView$OnItemClickListener() {
                        public void onItemClick(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
                            final RelatedApp relatedApp = list.get(n);
                            Intent launchIntentForPackage;
                            if (Util.packageExists((Context)MainDrawerFragment.this.getActivity(), relatedApp.getAppScheme())) {
                                launchIntentForPackage = Util.getLaunchIntentForPackage((Context)MainDrawerFragment.this.getActivity(), relatedApp.getAppScheme());
                            }
                            else {
                                launchIntentForPackage = new Intent("android.intent.action.VIEW");
                                launchIntentForPackage.setData(Uri.parse(relatedApp.getUrl()));
                            }
                            if (relatedApp.getAppScheme() != null) {
                                Tracker.relatedAppClicked((Context)MainDrawerFragment.this.getActivity(), relatedApp.getAppScheme());
                            }
                            else {
                                Tracker.relatedAppClicked((Context)MainDrawerFragment.this.getActivity(), relatedApp.getUrl());
                            }
                            MainDrawerFragment.this.startActivity(launchIntentForPackage);
                        }
                    });
                }
            });
        }
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            this.mHasFreeTrialInfo = false;
            if (!ApplicationState.get((Context)this.getActivity()).isAnyPremium()) {
                this.getFreeTrialInfo();
            }
            return;
        }
        this.mHasFreeTrialInfo = bundle.getBoolean("hasFreeTrialInfo");
        if (!ApplicationState.get((Context)this.getActivity()).isAnyPremium() && !this.mHasFreeTrialInfo) {
            this.getFreeTrialInfo();
            return;
        }
        this.mIsFreeTrial = bundle.getBoolean("isFreeTrial");
        this.mTrialDuration = bundle.getInt("trialDuration");
        this.mTrialSpan = bundle.getString("trialSpan");
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final View inflate = layoutInflater.inflate(2130903112, viewGroup, false);
        this.mOtherAppsLV = (ListView)inflate.findViewById(2131624143);
        this.mLoginText = (TextView)inflate.findViewById(2131624138);
        this.mLogin = inflate.findViewById(2131624137);
        this.mSettings = inflate.findViewById(2131624141);
        this.mSettingsText = (TextView)inflate.findViewById(2131624142);
        this.mUpgrade = inflate.findViewById(2131624139);
        this.mUpgradeText = (TextView)inflate.findViewById(2131624140);
        this.mBottomBarTryPremium = inflate.findViewById(2131624144);
        this.mBottomBarAlreadyPremium = inflate.findViewById(2131624147);
        this.mThankYou = (TextView)inflate.findViewById(2131624149);
        this.mTryPremium = (TextView)inflate.findViewById(2131624146);
        if (ApplicationState.get((Context)this.getActivity()).hasLoggedInUser()) {
            final Optional<User> loggedInUser = ApplicationState.get((Context)this.getActivity()).getLoggedInUser();
            String text;
            if (!TextUtils.isEmpty((CharSequence)loggedInUser.get().getUsername())) {
                text = loggedInUser.get().getUsername();
            }
            else {
                text = loggedInUser.get().getEmail();
            }
            this.mLoginText.setText((CharSequence)text);
            this.mLoginText.setCompoundDrawablesWithIntrinsicBounds((Drawable)null, (Drawable)null, this.getActivity().getResources().getDrawable(2130837803), (Drawable)null);
            this.mLogin.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    Util.showLogoutPrompt(MainDrawerFragment.this.getActivity());
                    Tracker.swrveScreenView("settings-menu-log-out");
                }
            });
        }
        else {
            this.mLoginText.setText((CharSequence)LocalizedStrings.HOME_MENU_LOGIN.get());
            this.mLoginText.setCompoundDrawables((Drawable)null, (Drawable)null, (Drawable)null, (Drawable)null);
            this.mLogin.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    MainActivity.signup(MainDrawerFragment.this.getActivity(), PrepareToWatch.Type.PREPARE_LOGIN, true);
                    Tracker.drawerLoginCreateAccount();
                    Tracker.swrveScreenView("settings-menu-log-in-create-account");
                }
            });
        }
        this.mSettingsText.setText((CharSequence)LocalizedStrings.HOME_MENU_SETTINGS.get());
        this.mSettings.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                SettingsActivity.start(MainDrawerFragment.this.getActivity());
                Tracker.drawerSettings();
            }
        });
        this.finalizeBottomMessage();
        return inflate;
    }
    
    @Override
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("hasFreeTrialInfo", this.mHasFreeTrialInfo);
        bundle.putBoolean("isFreeTrial", this.mIsFreeTrial);
        bundle.putInt("trialDuration", this.mTrialDuration);
        bundle.putString("trialSpan", this.mTrialSpan);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        this.populateRelatedApps();
    }
    
    public class MenuItemAdapter extends ArrayAdapter<RelatedApp>
    {
        private int mLayoutResourceId;
        private List<RelatedApp> mRelatedApps;
        
        public MenuItemAdapter(final Context context, final int mLayoutResourceId, final List<RelatedApp> mRelatedApps) {
            super(context, mLayoutResourceId, (List)mRelatedApps);
            this.mLayoutResourceId = mLayoutResourceId;
            this.mRelatedApps = mRelatedApps;
        }
        
        public View getView(final int n, final View view, final ViewGroup viewGroup) {
            View inflate = view;
            if (view == null) {
                inflate = MainDrawerFragment.this.getActivity().getLayoutInflater().inflate(this.mLayoutResourceId, viewGroup, false);
            }
            ((TextView)inflate.findViewById(2131624269)).setText((CharSequence)this.mRelatedApps.get(n).getTitle());
            return inflate;
        }
    }
}
