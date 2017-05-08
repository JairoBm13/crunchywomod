// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.fragments;

import android.support.annotation.Nullable;
import com.crunchyroll.crunchyroid.events.Upsell;
import de.greenrobot.event.EventBus;
import android.view.View$OnClickListener;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import java.io.Serializable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.TextView;
import android.view.View;

public class CreateAccountLoginPillTabsPopupFragment extends BaseFragment
{
    private View mBack;
    private View mButtonLeft;
    private TextView mButtonLeftText;
    private View mButtonRight;
    private TextView mButtonRightText;
    private View mClose;
    private Fragment mCreateAccountFragment;
    private Fragment mLoginFragment;
    private int mPosition;
    private TextView mTitle;
    private Type mType;
    
    public static CreateAccountLoginPillTabsPopupFragment newInstance(final Type type, final boolean b) {
        final CreateAccountLoginPillTabsPopupFragment createAccountLoginPillTabsPopupFragment = new CreateAccountLoginPillTabsPopupFragment();
        final Bundle arguments = new Bundle();
        arguments.putSerializable("type", (Serializable)type);
        arguments.putBoolean("showLogin", b);
        createAccountLoginPillTabsPopupFragment.setArguments(arguments);
        return createAccountLoginPillTabsPopupFragment;
    }
    
    private void selectCreateAccount() {
        this.mButtonLeft.setSelected(false);
        this.mButtonLeftText.setTextColor(this.getResources().getColor(2131558477));
        this.mButtonRight.setSelected(true);
        this.mButtonRightText.setTextColor(this.getResources().getColor(2131558537));
        switch (this.mType) {
            default: {}
            case TYPE_NOT_BACKABLE:
            case TYPE_BACKABLE_SINGLE_STEP: {
                this.mTitle.setText((CharSequence)LocalizedStrings.CREATE_ACCOUNT.get());
            }
        }
    }
    
    private void selectLogin() {
        this.mButtonRight.setSelected(false);
        this.mButtonRightText.setTextColor(this.getResources().getColor(2131558477));
        this.mButtonLeft.setSelected(true);
        this.mButtonLeftText.setTextColor(this.getResources().getColor(2131558537));
        switch (this.mType) {
            default: {}
            case TYPE_NOT_BACKABLE:
            case TYPE_BACKABLE_SINGLE_STEP: {
                this.mTitle.setText((CharSequence)LocalizedStrings.LOG_IN.get());
            }
        }
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.trackView();
        Tracker.swrveScreenView("login-sign-up");
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final View inflate = layoutInflater.inflate(2130903122, viewGroup, false);
        this.mType = (Type)this.getArguments().getSerializable("type");
        this.mBack = inflate.findViewById(2131624197);
        this.mClose = inflate.findViewById(2131624081);
        this.mTitle = (TextView)inflate.findViewById(2131624020);
        this.mButtonLeft = inflate.findViewById(2131624190);
        this.mButtonRight = inflate.findViewById(2131624193);
        this.mButtonLeftText = (TextView)inflate.findViewById(2131624192);
        this.mButtonRightText = (TextView)inflate.findViewById(2131624195);
        switch (this.mType) {
            case TYPE_NOT_BACKABLE: {
                this.mBack.setVisibility(8);
                this.mClose.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        EventBus.getDefault().post(new Upsell.CloseEvent());
                    }
                });
                this.mTitle.setText((CharSequence)LocalizedStrings.CREATE_ACCOUNT.get());
                break;
            }
            case TYPE_BACKABLE: {
                this.mBack.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        EventBus.getDefault().post(new Upsell.BackEvent());
                    }
                });
                this.mClose.setVisibility(8);
                this.mTitle.setText((CharSequence)(LocalizedStrings.STEP_1_OF_2.get() + " " + LocalizedStrings.REGISTRATION.get()));
                break;
            }
            case TYPE_BACKABLE_SINGLE_STEP: {
                this.mBack.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        EventBus.getDefault().post(new Upsell.BackEvent());
                    }
                });
                this.mClose.setVisibility(8);
                this.mTitle.setText((CharSequence)(LocalizedStrings.CREATE_ACCOUNT.get() + " " + LocalizedStrings.REGISTRATION.get()));
                break;
            }
        }
        this.mButtonLeft.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                CreateAccountLoginPillTabsPopupFragment.this.mPosition = 0;
                switch (CreateAccountLoginPillTabsPopupFragment.this.mType) {
                    case TYPE_NOT_BACKABLE: {
                        CreateAccountLoginPillTabsPopupFragment.this.trackViewExtras("free_login");
                        break;
                    }
                    case TYPE_BACKABLE: {
                        CreateAccountLoginPillTabsPopupFragment.this.trackViewExtras("signup_login");
                        break;
                    }
                    case TYPE_BACKABLE_SINGLE_STEP: {
                        CreateAccountLoginPillTabsPopupFragment.this.trackViewExtras("free_login");
                        break;
                    }
                }
                CreateAccountLoginPillTabsPopupFragment.this.selectLogin();
                if (CreateAccountLoginPillTabsPopupFragment.this.mLoginFragment == null) {
                    CreateAccountLoginPillTabsPopupFragment.this.mLoginFragment = SignupFragment.newInstance(false, CreateAccountLoginPillTabsPopupFragment.this.mType);
                }
                CreateAccountLoginPillTabsPopupFragment.this.getChildFragmentManager().beginTransaction().replace(2131624196, CreateAccountLoginPillTabsPopupFragment.this.mLoginFragment).commit();
            }
        });
        this.mButtonRight.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                switch (CreateAccountLoginPillTabsPopupFragment.this.mType) {
                    case TYPE_NOT_BACKABLE: {
                        CreateAccountLoginPillTabsPopupFragment.this.trackViewExtras("free_account_create");
                        break;
                    }
                    case TYPE_BACKABLE: {
                        CreateAccountLoginPillTabsPopupFragment.this.trackViewExtras("signup_account_create");
                        break;
                    }
                    case TYPE_BACKABLE_SINGLE_STEP: {
                        CreateAccountLoginPillTabsPopupFragment.this.trackViewExtras("free_account_create");
                        break;
                    }
                }
                CreateAccountLoginPillTabsPopupFragment.this.mPosition = 1;
                CreateAccountLoginPillTabsPopupFragment.this.selectCreateAccount();
                if (CreateAccountLoginPillTabsPopupFragment.this.mCreateAccountFragment == null) {
                    CreateAccountLoginPillTabsPopupFragment.this.mCreateAccountFragment = SignupFragment.newInstance(true, CreateAccountLoginPillTabsPopupFragment.this.mType);
                }
                CreateAccountLoginPillTabsPopupFragment.this.getChildFragmentManager().beginTransaction().replace(2131624196, CreateAccountLoginPillTabsPopupFragment.this.mCreateAccountFragment).commit();
            }
        });
        this.mButtonLeftText.setText((CharSequence)LocalizedStrings.LOG_IN.get().toUpperCase());
        this.mButtonRightText.setText((CharSequence)LocalizedStrings.CREATE_ACCOUNT.get().toUpperCase());
        return inflate;
    }
    
    @Override
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("position", this.mPosition);
    }
    
    @Override
    public void onViewCreated(final View view, @Nullable final Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (bundle == null) {
            if (this.getArguments().getBoolean("showLogin")) {
                this.mButtonLeft.performClick();
                return;
            }
            this.mButtonRight.performClick();
        }
        else {
            switch (this.mPosition = bundle.getInt("position")) {
                default: {}
                case 0: {
                    this.selectLogin();
                }
                case 1: {
                    this.selectCreateAccount();
                }
            }
        }
    }
    
    public enum Type
    {
        TYPE_BACKABLE, 
        TYPE_BACKABLE_SINGLE_STEP, 
        TYPE_NOT_BACKABLE;
    }
}
