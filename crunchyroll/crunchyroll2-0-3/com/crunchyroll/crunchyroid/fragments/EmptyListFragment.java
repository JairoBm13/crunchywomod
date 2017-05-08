// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.fragments;

import com.crunchyroll.crunchyroid.tracking.Tracker;
import android.view.View$OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ImageView;
import android.view.View;
import android.view.LayoutInflater;
import com.crunchyroll.crunchyroid.util.Util;
import com.crunchyroll.crunchyroid.events.ErrorEvent;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import de.greenrobot.event.EventBus;
import com.crunchyroll.android.api.exceptions.ApiNetworkException;
import com.crunchyroll.android.api.tasks.BaseListener;
import android.app.Activity;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import java.io.Serializable;
import android.os.Bundle;
import com.crunchyroll.crunchyroid.app.Extras;
import android.content.Context;
import android.content.Intent;
import com.crunchyroll.crunchyroid.activities.MainActivity;
import com.crunchyroll.crunchyroid.app.PrepareToWatch;
import android.view.ViewGroup;

public class EmptyListFragment extends BaseFragment
{
    private Type mType;
    private ViewGroup mView;
    
    private void goToAnime() {
        final Intent intent = new Intent((Context)this.getActivity(), (Class)MainActivity.class);
        Extras.putSerializable(intent, "mainType", MainActivity.Type.TYPE_ANIME);
        intent.setFlags(536870912);
        this.getActivity().startActivity(intent);
    }
    
    public static EmptyListFragment newInstance(final Type type) {
        final EmptyListFragment emptyListFragment = new EmptyListFragment();
        final Bundle arguments = new Bundle();
        arguments.putSerializable("type", (Serializable)type);
        emptyListFragment.setArguments(arguments);
        return emptyListFragment;
    }
    
    private void signup(final PrepareToWatch.Type type) {
        if (!CrunchyrollApplication.getApp((Context)this.getActivity()).isPrepareToWatchLoading()) {
            final PrepareToWatch prepareToWatchNoMedia = CrunchyrollApplication.getApp((Context)this.getActivity()).prepareToWatchNoMedia(this.getActivity(), type, false, 0, null);
            prepareToWatchNoMedia.prepare(new BaseListener<Void>() {
                @Override
                public void onException(final Exception ex) {
                    if (ex instanceof ApiNetworkException) {
                        EventBus.getDefault().post(new ErrorEvent(LocalizedStrings.ERROR_NETWORK.get()));
                        return;
                    }
                    EventBus.getDefault().post(new ErrorEvent(LocalizedStrings.ERROR_UNKNOWN.get()));
                }
                
                @Override
                public void onFinally() {
                    Util.hideProgressBar((Context)EmptyListFragment.this.getActivity(), EmptyListFragment.this.mView);
                }
                
                @Override
                public void onPreExecute() {
                    Util.showProgressBar((Context)EmptyListFragment.this.getActivity(), EmptyListFragment.this.mView, EmptyListFragment.this.getResources().getColor(2131558411));
                }
                
                @Override
                public void onSuccess(final Void void1) {
                    prepareToWatchNoMedia.go(PrepareToWatch.Event.NONE);
                }
            });
        }
    }
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        this.mType = (Type)this.getArguments().getSerializable("type");
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        this.mView = (ViewGroup)layoutInflater.inflate(2130903116, viewGroup, false);
        final ImageView imageView = (ImageView)this.mView.findViewById(2131624181);
        final TextView textView = (TextView)this.mView.findViewById(2131624182);
        final TextView textView2 = (TextView)this.mView.findViewById(2131624183);
        final Button button = (Button)this.mView.findViewById(2131624184);
        final TextView textView3 = (TextView)this.mView.findViewById(2131624185);
        textView3.setText((CharSequence)LocalizedStrings.LOG_IN.get());
        textView3.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
            }
        });
        switch (this.mType) {
            default: {
                imageView.setImageResource(2130837747);
                textView.setText((CharSequence)LocalizedStrings.EMPTY_HISTORY.get());
                textView2.setText((CharSequence)LocalizedStrings.EMPTY_HISTORY_DISCOVER.get());
                button.setText((CharSequence)LocalizedStrings.DISCOVER_SHOWS_TO_WATCH.get());
                button.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        EmptyListFragment.this.goToAnime();
                    }
                });
                textView3.setVisibility(8);
                break;
            }
            case QUEUE_SIGNUP: {
                imageView.setImageResource(2130837748);
                textView.setText((CharSequence)LocalizedStrings.EMPTY_QUEUE.get());
                textView2.setText((CharSequence)LocalizedStrings.EMPTY_QUEUE_DESCRIPTION.get());
                button.setText((CharSequence)LocalizedStrings.SIGN_UP.get());
                button.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        EmptyListFragment.this.signup(PrepareToWatch.Type.PREPARE_SIGNUP);
                        Tracker.homeQueue("sign-up");
                    }
                });
                textView3.setVisibility(0);
                textView3.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        EmptyListFragment.this.signup(PrepareToWatch.Type.PREPARE_LOGIN);
                        Tracker.homeQueue("log-in");
                    }
                });
                break;
            }
            case QUEUE_DISCOVER: {
                imageView.setImageResource(2130837748);
                textView.setText((CharSequence)LocalizedStrings.EMPTY_QUEUE.get());
                textView2.setText((CharSequence)LocalizedStrings.EMPTY_QUEUE_DISCOVER.get());
                button.setText((CharSequence)LocalizedStrings.DISCOVER_SHOWS_TO_ADD.get());
                button.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        EmptyListFragment.this.goToAnime();
                    }
                });
                textView3.setVisibility(8);
                break;
            }
            case HISTORY_SIGNUP: {
                imageView.setImageResource(2130837747);
                textView.setText((CharSequence)LocalizedStrings.EMPTY_HISTORY.get());
                textView2.setText((CharSequence)LocalizedStrings.EMPTY_HISTORY_DESCRIPTION.get());
                button.setText((CharSequence)LocalizedStrings.SIGN_UP.get());
                button.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        EmptyListFragment.this.signup(PrepareToWatch.Type.PREPARE_SIGNUP);
                        Tracker.homeHistory("sign-up");
                    }
                });
                textView3.setVisibility(0);
                textView3.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                    public void onClick(final View view) {
                        EmptyListFragment.this.signup(PrepareToWatch.Type.PREPARE_LOGIN);
                        Tracker.homeHistory("log-in");
                    }
                });
                break;
            }
        }
        return (View)this.mView;
    }
    
    public enum Type
    {
        HISTORY_DISCOVER, 
        HISTORY_SIGNUP, 
        QUEUE_DISCOVER, 
        QUEUE_SIGNUP;
    }
}
