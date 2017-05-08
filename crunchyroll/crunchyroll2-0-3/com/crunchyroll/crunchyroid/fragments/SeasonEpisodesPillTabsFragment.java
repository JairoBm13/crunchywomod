// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.fragments;

import android.content.Context;
import com.crunchyroll.crunchyroid.tracking.Tracker;
import android.support.annotation.Nullable;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import android.view.View$OnClickListener;
import android.view.LayoutInflater;
import com.crunchyroll.crunchyroid.activities.MainActivity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View;

public class SeasonEpisodesPillTabsFragment extends BaseFragment
{
    public static final int POSITION_THIS_SEASON = 0;
    public static final int POSITION_UPDATED_EPISODES = 1;
    private View mButtonLeft;
    private TextView mButtonLeftText;
    private View mButtonRight;
    private TextView mButtonRightText;
    private ViewGroup mParent;
    private int mPosition;
    private Fragment mThisSeasonFragment;
    private Fragment mUpdatedEpisodesFragment;
    
    public static SeasonEpisodesPillTabsFragment newInstance(final int n) {
        final SeasonEpisodesPillTabsFragment seasonEpisodesPillTabsFragment = new SeasonEpisodesPillTabsFragment();
        final Bundle arguments = new Bundle();
        arguments.putInt("position", n);
        seasonEpisodesPillTabsFragment.setArguments(arguments);
        return seasonEpisodesPillTabsFragment;
    }
    
    private void selectThisSeasonButton() {
        this.mButtonRight.setSelected(false);
        this.mButtonRightText.setTextColor(this.getResources().getColor(2131558477));
        this.mButtonLeft.setSelected(true);
        this.mButtonLeftText.setTextColor(this.getResources().getColor(2131558537));
    }
    
    private void selectUpdatedEpisodesButton() {
        this.mButtonLeft.setSelected(false);
        this.mButtonLeftText.setTextColor(this.getResources().getColor(2131558477));
        this.mButtonRight.setSelected(true);
        this.mButtonRightText.setTextColor(this.getResources().getColor(2131558537));
    }
    
    private void setSwitchViewMenuVisibility(final boolean switchViewMenuVisibility) {
        if (this.getActivity() instanceof MainActivity) {
            ((MainActivity)this.getActivity()).setSwitchViewMenuVisibility(switchViewMenuVisibility);
        }
    }
    
    private void showThisSeasonFragment() {
        if (this.mThisSeasonFragment == null) {
            this.mThisSeasonFragment = this.getChildFragmentManager().findFragmentByTag(ThisSeasonListFragment.class.getSimpleName());
        }
        if (this.mThisSeasonFragment == null) {
            this.mThisSeasonFragment = ThisSeasonListFragment.newInstance();
        }
        this.getChildFragmentManager().beginTransaction().replace(2131624196, this.mThisSeasonFragment, ThisSeasonListFragment.class.getSimpleName()).commit();
    }
    
    private void showUpdatedEpisodesFragment() {
        if (this.mUpdatedEpisodesFragment == null) {
            this.mUpdatedEpisodesFragment = this.getChildFragmentManager().findFragmentByTag(UpdatedEpisodesFragment.class.getSimpleName());
        }
        if (this.mUpdatedEpisodesFragment == null) {
            this.mUpdatedEpisodesFragment = UpdatedEpisodesFragment.newInstance();
        }
        this.getChildFragmentManager().beginTransaction().replace(2131624196, this.mUpdatedEpisodesFragment, UpdatedEpisodesFragment.class.getSimpleName()).commit();
    }
    
    public boolean getSwitchViewMenuVisibility() {
        switch (this.mPosition) {
            default: {
                return true;
            }
            case 1: {
                return false;
            }
        }
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        if (this.mParent != null) {
            return (View)this.mParent;
        }
        this.mParent = (ViewGroup)layoutInflater.inflate(2130903118, viewGroup, false);
        this.mButtonLeft = this.mParent.findViewById(2131624190);
        this.mButtonRight = this.mParent.findViewById(2131624193);
        this.mButtonLeftText = (TextView)this.mParent.findViewById(2131624192);
        this.mButtonRightText = (TextView)this.mParent.findViewById(2131624195);
        this.mButtonLeft.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                SeasonEpisodesPillTabsFragment.this.mPosition = 0;
                SeasonEpisodesPillTabsFragment.this.getArguments().putInt("position", 0);
                SeasonEpisodesPillTabsFragment.this.selectThisSeasonButton();
                SeasonEpisodesPillTabsFragment.this.showThisSeasonFragment();
                SeasonEpisodesPillTabsFragment.this.setSwitchViewMenuVisibility(true);
                SeasonEpisodesPillTabsFragment.this.trackView();
            }
        });
        this.mButtonRight.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                SeasonEpisodesPillTabsFragment.this.mPosition = 1;
                SeasonEpisodesPillTabsFragment.this.getArguments().putInt("position", 1);
                SeasonEpisodesPillTabsFragment.this.selectUpdatedEpisodesButton();
                SeasonEpisodesPillTabsFragment.this.showUpdatedEpisodesFragment();
                SeasonEpisodesPillTabsFragment.this.setSwitchViewMenuVisibility(false);
                SeasonEpisodesPillTabsFragment.this.trackView();
                if (SeasonEpisodesPillTabsFragment.this.mUpdatedEpisodesFragment == null) {
                    SeasonEpisodesPillTabsFragment.this.mUpdatedEpisodesFragment = EmptyListFragment.newInstance(EmptyListFragment.Type.HISTORY_DISCOVER);
                }
                SeasonEpisodesPillTabsFragment.this.getChildFragmentManager().beginTransaction().replace(2131624196, SeasonEpisodesPillTabsFragment.this.mUpdatedEpisodesFragment).commit();
            }
        });
        this.mButtonLeftText.setText((CharSequence)LocalizedStrings.THIS_SEASON.get());
        this.mButtonRightText.setText((CharSequence)LocalizedStrings.UPDATED_EPISODES.get());
        return (View)this.mParent;
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
            this.mPosition = this.getArguments().getInt("position");
            if (this.mPosition == 0) {
                this.selectThisSeasonButton();
                this.showThisSeasonFragment();
                return;
            }
            this.selectUpdatedEpisodesButton();
            this.showUpdatedEpisodesFragment();
        }
        else {
            switch (this.mPosition = bundle.getInt("position")) {
                default: {}
                case 0: {
                    this.selectThisSeasonButton();
                }
                case 1: {
                    this.selectUpdatedEpisodesButton();
                }
            }
        }
    }
    
    @Override
    public void trackView() {
        if (this.mPosition == 0) {
            Tracker.swrveScreenView("new-season");
            Tracker.trackView((Context)this.getActivity(), Tracker.Screen.THIS_SEASON);
            return;
        }
        Tracker.swrveScreenView("new-episodes");
        Tracker.trackView((Context)this.getActivity(), Tracker.Screen.NEW_EPISODES);
    }
}
