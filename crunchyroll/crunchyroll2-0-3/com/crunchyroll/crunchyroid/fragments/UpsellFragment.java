// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.fragments;

import tv.ouya.console.api.OuyaFacade;
import android.view.KeyEvent;
import android.view.View$OnKeyListener;
import com.crunchyroll.android.api.models.Media;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import com.crunchyroll.crunchyroid.events.Upsell;
import de.greenrobot.event.EventBus;
import android.view.View$OnClickListener;
import android.widget.TextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import com.crunchyroll.android.api.models.FreeTrialInformationItem;
import com.crunchyroll.android.api.models.MembershipInfoItem;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import android.content.Context;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import com.crunchyroll.crunchyroid.app.Extras;
import android.os.Bundle;

public class UpsellFragment extends BaseFragment
{
    private boolean mDidGoForward;
    private boolean mIsFreeTrial;
    private Type mUpsellType;
    private String recurringPrice;
    private int trialDuration;
    private String trialSpan;
    
    public static UpsellFragment newInstance(final Type type) {
        final UpsellFragment upsellFragment = new UpsellFragment();
        final Bundle arguments = new Bundle();
        Extras.putSerializable(arguments, "upsellType", type);
        upsellFragment.setArguments(arguments);
        return upsellFragment;
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.mUpsellType = (Type)this.getArguments().getSerializable("upsellType");
        this.mDidGoForward = false;
        this.mIsFreeTrial = true;
        if (CrunchyrollApplication.getApp((Context)this.getActivity()).getPrepareToWatch() == null) {
            this.getActivity().finish();
        }
        else if (CrunchyrollApplication.getApp((Context)this.getActivity()).getPrepareToWatch().getMembershipInfoItem() != null) {
            final MembershipInfoItem membershipInfoItem = CrunchyrollApplication.getApp((Context)this.getActivity()).getPrepareToWatch().getMembershipInfoItem();
            this.mIsFreeTrial = false;
            this.trialDuration = membershipInfoItem.getDuration();
            this.trialSpan = membershipInfoItem.getSpanType();
            this.recurringPrice = membershipInfoItem.getPriceAndSymbol();
        }
        else if (CrunchyrollApplication.getApp((Context)this.getActivity()).getPrepareToWatch().getFreeTrialInfoItem() != null) {
            final FreeTrialInformationItem freeTrialInfoItem = CrunchyrollApplication.getApp((Context)this.getActivity()).getPrepareToWatch().getFreeTrialInfoItem();
            this.trialDuration = freeTrialInfoItem.getDuration();
            this.trialSpan = freeTrialInfoItem.getSpanType();
            this.recurringPrice = freeTrialInfoItem.getPriceAndSymbol();
        }
        if (this.mUpsellType == Type.TYPE_ANONYMOUS) {
            Tracker.swrveScreenView("free-video-upsell");
        }
        if (this.mUpsellType == Type.TYPE_PREMIUM_REQUIRED) {
            Tracker.swrveScreenView("premium-video-upsell");
        }
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        View view;
        if (this.mUpsellType == Type.TYPE_ANONYMOUS) {
            view = layoutInflater.inflate(2130903126, viewGroup, false);
        }
        else {
            view = layoutInflater.inflate(2130903125, viewGroup, false);
        }
        final View viewById = view.findViewById(2131624081);
        final TextView textView = (TextView)view.findViewById(2131624239);
        final TextView textView2 = (TextView)view.findViewById(2131624228);
        final TextView textView3 = (TextView)view.findViewById(2131624231);
        final TextView textView4 = (TextView)view.findViewById(2131624234);
        final TextView textView5 = (TextView)view.findViewById(2131624224);
        final TextView textView6 = (TextView)view.findViewById(2131624225);
        final TextView textView7 = (TextView)view.findViewById(2131624222);
        final TextView textView8 = (TextView)view.findViewById(2131624235);
        final TextView textView9 = (TextView)view.findViewById(2131624236);
        viewById.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                EventBus.getDefault().post(new Upsell.CloseEvent());
            }
        });
        if (textView != null) {
            textView.setText((CharSequence)LocalizedStrings.NO_THANKS.get());
        }
        textView2.setText((CharSequence)LocalizedStrings.UPSELL_POINT_AD_FREE.get());
        textView3.setText((CharSequence)LocalizedStrings.UPSELL_POINT_SIMULCASTS.get());
        textView4.setText((CharSequence)LocalizedStrings.UPSELL_POINT_HD_VIDEO.get());
        if (this.mIsFreeTrial) {
            switch (this.mUpsellType) {
                case TYPE_NONE: {
                    textView5.setVisibility(8);
                    Tracker.trackView((Context)this.getActivity(), Tracker.Screen.UPSELL_FROM_DRAWER);
                    if (this.mIsFreeTrial) {
                        textView6.setText((CharSequence)String.format(LocalizedStrings.UPSELL_TRY_PREMIUM.get(), this.trialDuration, LocalizedStrings.getFromToken(this.trialSpan)));
                        textView7.setText((CharSequence)LocalizedStrings.START_TRIAL.get());
                        break;
                    }
                    textView6.setText((CharSequence)LocalizedStrings.UPGRADE_TITLE.get());
                    textView7.setText((CharSequence)LocalizedStrings.UPGRADE_NOW.get());
                    break;
                }
                case TYPE_ANONYMOUS: {
                    Tracker.trackView((Context)this.getActivity(), Tracker.Screen.UPSELL_FREE_VIDEO);
                    textView5.setText((CharSequence)LocalizedStrings.UPSELL_WITHOUT_ADS.get());
                    textView6.setText((CharSequence)String.format(LocalizedStrings.UPSELL_TRY_PREMIUM.get(), this.trialDuration, LocalizedStrings.getFromToken(this.trialSpan)));
                    textView7.setText((CharSequence)LocalizedStrings.SIGN_UP.get());
                    break;
                }
                case TYPE_ACCOUNT_REQUIRED: {
                    textView5.setText((CharSequence)LocalizedStrings.UPSELL_ACCOUNT.get());
                    textView6.setText((CharSequence)String.format(LocalizedStrings.UPSELL_TRY_PREMIUM.get(), this.trialDuration, LocalizedStrings.getFromToken(this.trialSpan)));
                    textView7.setText((CharSequence)LocalizedStrings.START_TRIAL.get());
                    break;
                }
                case TYPE_ALL_ACCESS_REQUIRED:
                case TYPE_PREMIUM_REQUIRED: {
                    Tracker.trackView((Context)this.getActivity(), Tracker.Screen.UPSELL_PREMIUM_VIDEO);
                    textView5.setText((CharSequence)LocalizedStrings.UPSELL_PREMIUM.get());
                    textView6.setText((CharSequence)String.format(LocalizedStrings.UPSELL_TRY_PREMIUM.get(), this.trialDuration, LocalizedStrings.getFromToken(this.trialSpan)));
                    textView7.setText((CharSequence)LocalizedStrings.START_TRIAL.get());
                    break;
                }
            }
        }
        else {
            if (this.mUpsellType == Type.TYPE_NONE) {
                textView5.setVisibility(8);
                Tracker.trackView((Context)this.getActivity(), Tracker.Screen.UPSELL_FROM_DRAWER);
            }
            else {
                textView5.setText((CharSequence)LocalizedStrings.UPSELL_UPGRADE.get());
                Tracker.trackView((Context)this.getActivity(), Tracker.Screen.UPSELL_PREMIUM_VIDEO);
            }
            textView6.setText((CharSequence)LocalizedStrings.UPGRADE_TITLE.get());
            textView7.setText((CharSequence)LocalizedStrings.UPGRADE_NOW.get());
        }
        if (view.findViewById(2131624236) == null) {
            if (this.mIsFreeTrial) {
                textView8.setText((CharSequence)(String.format(LocalizedStrings.UPSELL_SUBSCRIPTION_VALUE_LINE_1.get(), this.recurringPrice) + " " + LocalizedStrings.UPSELL_SUBSCRIPTION_VALUE_LINE_2.get()));
            }
            else {
                textView8.setText((CharSequence)(String.format(LocalizedStrings.UPSELL_SUBSCRIPTION_VALUE_LINE_1_NO_FREE_TRIAL.get(), this.recurringPrice) + " " + LocalizedStrings.UPSELL_SUBSCRIPTION_VALUE_LINE_2.get()));
            }
        }
        else {
            textView9.setText((CharSequence)LocalizedStrings.UPSELL_SUBSCRIPTION_VALUE_LINE_2.get());
            if (this.mIsFreeTrial) {
                textView8.setText((CharSequence)String.format(LocalizedStrings.UPSELL_SUBSCRIPTION_VALUE_LINE_1.get(), this.recurringPrice));
            }
            else {
                textView8.setText((CharSequence)String.format(LocalizedStrings.UPSELL_SUBSCRIPTION_VALUE_LINE_1_NO_FREE_TRIAL.get(), this.recurringPrice));
            }
        }
        if (CrunchyrollApplication.getApp((Context)this.getActivity()).getPrepareToWatch() != null) {
            final Media startMedia = CrunchyrollApplication.getApp((Context)this.getActivity()).getPrepareToWatch().getStartMedia();
            switch (this.mUpsellType) {
                case TYPE_NONE: {
                    textView7.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                        public void onClick(final View view) {
                            UpsellFragment.this.mDidGoForward = true;
                            EventBus.getDefault().post(new Upsell.OkEvent());
                        }
                    });
                    if (textView != null) {
                        textView.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                            public void onClick(final View view) {
                                EventBus.getDefault().post(new Upsell.CancelEvent());
                            }
                        });
                        return view;
                    }
                    break;
                }
                case TYPE_ANONYMOUS: {
                    textView7.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                        public void onClick(final View view) {
                            Tracker.trackView((Context)UpsellFragment.this.getActivity().getApplication(), Tracker.Screen.UPSELL_FREE_ACCOUNT_CLICKTHROUGH);
                            Tracker.freeVideoUpsellSignup((Context)UpsellFragment.this.getActivity(), startMedia);
                            UpsellFragment.this.mDidGoForward = true;
                            EventBus.getDefault().post(new Upsell.OkEvent());
                        }
                    });
                    if (textView != null) {
                        textView.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                            public void onClick(final View view) {
                                Tracker.freeVideoUpsellNothanks((Context)UpsellFragment.this.getActivity(), startMedia);
                                EventBus.getDefault().post(new Upsell.UpsellDismissedEvent());
                            }
                        });
                        return view;
                    }
                    break;
                }
                case TYPE_ALL_ACCESS_REQUIRED: {
                    textView7.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                        public void onClick(final View view) {
                            if (UpsellFragment.this.mIsFreeTrial) {
                                Tracker.premiumVideoFreeUpsellStartfreetrial((Context)UpsellFragment.this.getActivity(), startMedia);
                                UpsellFragment.this.mDidGoForward = true;
                            }
                            else {
                                Tracker.premiumVideoUpsellUpgradeUpgradeNow((Context)UpsellFragment.this.getActivity(), startMedia);
                                UpsellFragment.this.mDidGoForward = true;
                            }
                            EventBus.getDefault().post(new Upsell.OkEvent());
                        }
                    });
                    return view;
                }
                case TYPE_ACCOUNT_REQUIRED: {
                    textView7.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                        public void onClick(final View view) {
                            Tracker.trackView((Context)UpsellFragment.this.getActivity(), Tracker.Screen.UPSELL_FREE_TRIAL_CLICKTHROUGH);
                            UpsellFragment.this.mDidGoForward = true;
                            EventBus.getDefault().post(new Upsell.OkEvent());
                        }
                    });
                    return view;
                }
                case TYPE_PREMIUM_REQUIRED: {
                    textView7.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                        public void onClick(final View view) {
                            Tracker.trackView((Context)UpsellFragment.this.getActivity(), Tracker.Screen.UPSELL_FREE_TRIAL_CLICKTHROUGH);
                            Tracker.premiumVideoAnonUpsellStartfreetrial((Context)UpsellFragment.this.getActivity(), startMedia);
                            UpsellFragment.this.mDidGoForward = true;
                            EventBus.getDefault().post(new Upsell.OkEvent());
                        }
                    });
                    return view;
                }
            }
        }
        return view;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (CrunchyrollApplication.getApp((Context)this.getActivity()).getPrepareToWatch() != null) {
            final Media startMedia = CrunchyrollApplication.getApp((Context)this.getActivity()).getPrepareToWatch().getStartMedia();
            if (!this.mDidGoForward) {
                switch (this.mUpsellType) {
                    case TYPE_ANONYMOUS: {
                        Tracker.freeVideoUpsellBack((Context)this.getActivity(), startMedia);
                    }
                    case TYPE_ALL_ACCESS_REQUIRED: {
                        if (this.mIsFreeTrial) {
                            Tracker.premiumVideoFreeUpsellBack((Context)this.getActivity(), startMedia);
                            return;
                        }
                        Tracker.premiumVideoUpsellUpgradeBack((Context)this.getActivity(), startMedia);
                    }
                    case TYPE_PREMIUM_REQUIRED: {
                        Tracker.premiumVideoAnonUpsellBack((Context)this.getActivity(), startMedia);
                    }
                }
            }
        }
    }
    
    @Override
    public void onViewCreated(final View view, final Bundle bundle) {
        final ViewGroup viewGroup = (ViewGroup)view.findViewById(2131624237);
        viewGroup.requestFocus();
        viewGroup.setOnKeyListener((View$OnKeyListener)new View$OnKeyListener() {
            public boolean onKey(final View view, int n, final KeyEvent keyEvent) {
                final boolean b = true;
                if (keyEvent.getAction() == 0) {
                    final ViewGroup viewGroup = (ViewGroup)view;
                    final int childCount = viewGroup.getChildCount();
                    boolean b2 = false;
                    Label_0067: {
                        switch (n) {
                            default: {
                                b2 = false;
                                break;
                            }
                            case 21:
                            case 22: {
                                n = 0;
                                View child;
                                while (true) {
                                    b2 = b;
                                    if (n >= childCount) {
                                        break Label_0067;
                                    }
                                    child = viewGroup.getChildAt(n);
                                    if (child.isSelected()) {
                                        break;
                                    }
                                    if (n == childCount - 1) {
                                        viewGroup.getChildAt(0).setSelected(true);
                                    }
                                    ++n;
                                }
                                child.setSelected(false);
                                viewGroup.getChildAt((n + 1) % childCount).setSelected(true);
                                return true;
                            }
                            case 96: {
                                n = 0;
                                View child2;
                                while (true) {
                                    b2 = b;
                                    if (n >= childCount) {
                                        break Label_0067;
                                    }
                                    child2 = viewGroup.getChildAt(n);
                                    if (child2.isSelected()) {
                                        break;
                                    }
                                    ++n;
                                }
                                child2.performClick();
                                return true;
                            }
                            case 97: {
                                EventBus.getDefault().post(new Upsell.UpsellDismissedEvent());
                                return true;
                            }
                        }
                    }
                    return b2;
                }
                return false;
            }
        });
        if (OuyaFacade.getInstance().isRunningOnOUYAHardware()) {
            viewGroup.getChildAt(0).setSelected(true);
        }
    }
    
    public enum Type
    {
        TYPE_ACCOUNT_REQUIRED, 
        TYPE_ALL_ACCESS_REQUIRED, 
        TYPE_ANONYMOUS, 
        TYPE_NONE, 
        TYPE_PREMIUM_REQUIRED;
    }
}
