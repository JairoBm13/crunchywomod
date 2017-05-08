// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.fragments;

import com.crunchyroll.crunchyroid.tracking.Tracker;
import android.support.annotation.Nullable;
import de.greenrobot.event.EventBus;
import com.crunchyroll.crunchyroid.events.Empty;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import android.view.View$OnClickListener;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.content.Context;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import android.os.Bundle;
import android.content.BroadcastReceiver;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;

public class QueueHistoryPillTabsFragment extends BaseFragment
{
    public static final int POSITION_HISTORY = 1;
    public static final int POSITION_QUEUE = 0;
    private View mButtonLeft;
    private ImageView mButtonLeftImage;
    private TextView mButtonLeftText;
    private View mButtonRight;
    private TextView mButtonRightText;
    private HistoryFragment mHistoryFragment;
    private BroadcastReceiver mHistoryUpdatedReceiver;
    private boolean mIsHistoryEmpty;
    private boolean mIsQueueEmpty;
    private int mPosition;
    private QueueFragment mQueueFragment;
    private BroadcastReceiver mQueueUpdatedReceiver;
    private boolean mReloadHistoryOnResume;
    private boolean mShowQueueOnResume;
    
    public static QueueHistoryPillTabsFragment newInstance(final int n) {
        final QueueHistoryPillTabsFragment queueHistoryPillTabsFragment = new QueueHistoryPillTabsFragment();
        final Bundle arguments = new Bundle();
        arguments.putInt("position", n);
        queueHistoryPillTabsFragment.setArguments(arguments);
        return queueHistoryPillTabsFragment;
    }
    
    private void selectHistoryButton() {
        this.mButtonLeft.setSelected(false);
        this.mButtonLeftText.setTextColor(this.getResources().getColor(2131558477));
        this.mButtonRight.setSelected(true);
        this.mButtonRightText.setTextColor(this.getResources().getColor(2131558537));
        this.mButtonLeftImage.setImageResource(2130837717);
    }
    
    private void selectQueueButton() {
        this.mButtonRight.setSelected(false);
        this.mButtonRightText.setTextColor(this.getResources().getColor(2131558477));
        this.mButtonLeft.setSelected(true);
        this.mButtonLeftText.setTextColor(this.getResources().getColor(2131558537));
        this.mButtonLeftImage.setImageResource(2130837718);
    }
    
    private void showHistoryFragment() {
        BaseFragment baseFragment;
        if (CrunchyrollApplication.getApp((Context)this.getActivity()).getApplicationState().hasLoggedInUser()) {
            if (this.mIsHistoryEmpty) {
                baseFragment = EmptyListFragment.newInstance(EmptyListFragment.Type.HISTORY_DISCOVER);
            }
            else if (this.mHistoryFragment == null) {
                baseFragment = HistoryFragment.newInstance();
                this.mHistoryFragment = (HistoryFragment)baseFragment;
            }
            else {
                baseFragment = this.mHistoryFragment;
            }
        }
        else {
            baseFragment = EmptyListFragment.newInstance(EmptyListFragment.Type.HISTORY_SIGNUP);
        }
        this.getChildFragmentManager().beginTransaction().replace(2131624196, baseFragment).commit();
    }
    
    private void showQueueFragment() {
        BaseFragment baseFragment;
        if (CrunchyrollApplication.getApp((Context)this.getActivity()).getApplicationState().hasLoggedInUser()) {
            if (this.mIsQueueEmpty) {
                baseFragment = EmptyListFragment.newInstance(EmptyListFragment.Type.QUEUE_DISCOVER);
            }
            else if (this.mQueueFragment == null) {
                baseFragment = QueueFragment.newInstance();
                this.mQueueFragment = (QueueFragment)baseFragment;
            }
            else {
                baseFragment = this.mQueueFragment;
            }
        }
        else {
            baseFragment = EmptyListFragment.newInstance(EmptyListFragment.Type.QUEUE_SIGNUP);
        }
        this.getChildFragmentManager().beginTransaction().replace(2131624196, baseFragment).commit();
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.mIsQueueEmpty = false;
        this.mIsHistoryEmpty = false;
        this.mShowQueueOnResume = false;
        this.mQueueUpdatedReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if (intent.getAction().equals("QUEUE_ADD")) {
                    QueueHistoryPillTabsFragment.this.mShowQueueOnResume = true;
                    if (QueueHistoryPillTabsFragment.this.mQueueFragment != null) {
                        QueueHistoryPillTabsFragment.this.mQueueFragment.setReloadAfterAttach();
                    }
                }
            }
        };
        LocalBroadcastManager.getInstance((Context)this.getActivity()).registerReceiver(this.mQueueUpdatedReceiver, new IntentFilter("QUEUE_ADD"));
        this.mReloadHistoryOnResume = false;
        this.mHistoryUpdatedReceiver = new BroadcastReceiver() {
            public void onReceive(final Context context, final Intent intent) {
                if (intent.getAction().equals("HISTORY_UPDATED")) {
                    QueueHistoryPillTabsFragment.this.mReloadHistoryOnResume = true;
                    if (QueueHistoryPillTabsFragment.this.mHistoryFragment != null) {
                        QueueHistoryPillTabsFragment.this.mHistoryFragment.setReloadAfterAttach();
                    }
                }
            }
        };
        LocalBroadcastManager.getInstance((Context)this.getActivity()).registerReceiver(this.mHistoryUpdatedReceiver, new IntentFilter("HISTORY_UPDATED"));
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        final View inflate = layoutInflater.inflate(2130903118, viewGroup, false);
        this.mButtonLeft = inflate.findViewById(2131624190);
        this.mButtonRight = inflate.findViewById(2131624193);
        this.mButtonLeftText = (TextView)inflate.findViewById(2131624192);
        this.mButtonRightText = (TextView)inflate.findViewById(2131624195);
        this.mButtonLeftImage = (ImageView)inflate.findViewById(2131624191);
        this.mButtonLeft.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                QueueHistoryPillTabsFragment.this.mPosition = 0;
                QueueHistoryPillTabsFragment.this.getArguments().putInt("position", 0);
                QueueHistoryPillTabsFragment.this.selectQueueButton();
                QueueHistoryPillTabsFragment.this.showQueueFragment();
                QueueHistoryPillTabsFragment.this.trackView();
            }
        });
        this.mButtonRight.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                QueueHistoryPillTabsFragment.this.mPosition = 1;
                QueueHistoryPillTabsFragment.this.getArguments().putInt("position", 1);
                QueueHistoryPillTabsFragment.this.selectHistoryButton();
                QueueHistoryPillTabsFragment.this.showHistoryFragment();
                QueueHistoryPillTabsFragment.this.trackView();
            }
        });
        this.mButtonLeftText.setText((CharSequence)LocalizedStrings.MY_QUEUE.get());
        this.mButtonRightText.setText((CharSequence)LocalizedStrings.MY_HISTORY.get());
        return inflate;
    }
    
    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance((Context)this.getActivity()).unregisterReceiver(this.mQueueUpdatedReceiver);
        LocalBroadcastManager.getInstance((Context)this.getActivity()).unregisterReceiver(this.mHistoryUpdatedReceiver);
    }
    
    public void onEvent(final Empty.HistoryEvent historyEvent) {
        this.mIsHistoryEmpty = true;
        this.showHistoryFragment();
    }
    
    public void onEvent(final Empty.QueueEvent queueEvent) {
        this.mIsQueueEmpty = true;
        this.showQueueFragment();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        if (this.mShowQueueOnResume) {
            this.mShowQueueOnResume = false;
            this.mIsQueueEmpty = false;
            if (this.mPosition == 0) {
                this.showQueueFragment();
            }
        }
        if (this.mReloadHistoryOnResume) {
            this.mReloadHistoryOnResume = false;
            this.mIsHistoryEmpty = false;
            if (this.mPosition == 1) {
                this.showHistoryFragment();
            }
        }
    }
    
    @Override
    public void onSaveInstanceState(final Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("position", this.mPosition);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }
    
    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    
    @Override
    public void onViewCreated(final View view, @Nullable final Bundle bundle) {
        super.onViewCreated(view, bundle);
        if (bundle == null) {
            this.mPosition = this.getArguments().getInt("position");
            if (this.mPosition == 0) {
                this.selectQueueButton();
                this.showQueueFragment();
                return;
            }
            this.selectHistoryButton();
            this.showHistoryFragment();
        }
        else {
            switch (this.mPosition = bundle.getInt("position")) {
                default: {}
                case 0: {
                    this.selectQueueButton();
                }
                case 1: {
                    this.selectHistoryButton();
                }
            }
        }
    }
    
    @Override
    public void trackView() {
        if (this.mPosition == 0) {
            Tracker.swrveScreenView("home-queue");
            Tracker.trackView((Context)this.getActivity(), Tracker.Screen.QUEUE);
            return;
        }
        Tracker.swrveScreenView("home-history");
        Tracker.trackView((Context)this.getActivity(), Tracker.Screen.HISTORY);
    }
}
