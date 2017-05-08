// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.widget;

import java.util.Iterator;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.app.Activity;
import com.facebook.AppEventsLogger;
import com.facebook.FacebookException;
import com.facebook.widget.PickerFragment$com.facebook.widget.PickerFragment$SelectionStrategy;
import com.facebook.widget.PickerFragment$com.facebook.widget.PickerFragment$LoadingStrategy;
import com.facebook.model.GraphObject;
import android.content.Context;
import com.facebook.widget.PickerFragment$com.facebook.widget.PickerFragment$PickerFragmentAdapter;
import android.text.TextUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import com.facebook.Request;
import com.facebook.Session;
import java.util.Set;
import java.util.ArrayList;
import com.facebook.android.R;
import android.os.Bundle;
import java.util.List;
import com.facebook.model.GraphUser;

public class FriendPickerFragment extends PickerFragment<GraphUser>
{
    public static final String FRIEND_PICKER_TYPE_KEY = "com.facebook.widget.FriendPickerFragment.FriendPickerType";
    private static final String ID = "id";
    public static final String MULTI_SELECT_BUNDLE_KEY = "com.facebook.widget.FriendPickerFragment.MultiSelect";
    private static final String NAME = "name";
    public static final String USER_ID_BUNDLE_KEY = "com.facebook.widget.FriendPickerFragment.UserId";
    private FriendPickerType friendPickerType;
    private boolean multiSelect;
    private List<String> preSelectedFriendIds;
    private String userId;
    
    public FriendPickerFragment() {
        this(null);
    }
    
    public FriendPickerFragment(final Bundle friendPickerSettingsFromBundle) {
        super(GraphUser.class, R.layout.com_facebook_friendpickerfragment, friendPickerSettingsFromBundle);
        this.multiSelect = true;
        this.friendPickerType = FriendPickerType.FRIENDS;
        this.preSelectedFriendIds = new ArrayList<String>();
        this.setFriendPickerSettingsFromBundle(friendPickerSettingsFromBundle);
    }
    
    private Request createRequest(final String s, final Set<String> set, final Session session) {
        final Request graphPathRequest = Request.newGraphPathRequest(session, s + this.friendPickerType.getRequestPath(), null);
        final HashSet<Object> set2 = new HashSet<Object>(set);
        set2.addAll(Arrays.asList("id", "name"));
        final String pictureFieldSpecifier = this.adapter.getPictureFieldSpecifier();
        if (pictureFieldSpecifier != null) {
            set2.add(pictureFieldSpecifier);
        }
        final Bundle parameters = graphPathRequest.getParameters();
        parameters.putString("fields", TextUtils.join((CharSequence)",", (Iterable)set2));
        graphPathRequest.setParameters(parameters);
        return graphPathRequest;
    }
    
    private void setFriendPickerSettingsFromBundle(final Bundle bundle) {
        if (bundle == null) {
            return;
        }
        if (bundle.containsKey("com.facebook.widget.FriendPickerFragment.UserId")) {
            this.setUserId(bundle.getString("com.facebook.widget.FriendPickerFragment.UserId"));
        }
        this.setMultiSelect(bundle.getBoolean("com.facebook.widget.FriendPickerFragment.MultiSelect", this.multiSelect));
        if (!bundle.containsKey("com.facebook.widget.FriendPickerFragment.FriendPickerType")) {
            return;
        }
        try {
            this.friendPickerType = FriendPickerType.valueOf(bundle.getString("com.facebook.widget.FriendPickerFragment.FriendPickerType"));
        }
        catch (Exception ex) {}
    }
    
    @Override
    PickerFragment$PickerFragmentAdapter<GraphUser> createAdapter() {
        final PickerFragment$PickerFragmentAdapter<GraphUser> pickerFragment$PickerFragmentAdapter = new PickerFragment$PickerFragmentAdapter<GraphUser>(this.getActivity()) {
            protected int getDefaultPicture() {
                return R.drawable.com_facebook_profile_default_icon;
            }
            
            protected int getGraphObjectRowLayoutId(final GraphUser graphUser) {
                return R.layout.com_facebook_picker_list_row;
            }
        };
        ((GraphObjectAdapter)pickerFragment$PickerFragmentAdapter).setShowCheckbox(true);
        ((GraphObjectAdapter)pickerFragment$PickerFragmentAdapter).setShowPicture(this.getShowPictures());
        ((GraphObjectAdapter)pickerFragment$PickerFragmentAdapter).setSortFields(Arrays.asList("name"));
        ((GraphObjectAdapter)pickerFragment$PickerFragmentAdapter).setGroupByField("name");
        return pickerFragment$PickerFragmentAdapter;
    }
    
    @Override
    PickerFragment$LoadingStrategy createLoadingStrategy() {
        return new ImmediateLoadingStrategy();
    }
    
    @Override
    PickerFragment$SelectionStrategy createSelectionStrategy() {
        if (this.multiSelect) {
            return new MultiSelectionStrategy(this);
        }
        return new SingleSelectionStrategy(this);
    }
    
    @Override
    String getDefaultTitleText() {
        return this.getString(R.string.com_facebook_choose_friends);
    }
    
    public boolean getMultiSelect() {
        return this.multiSelect;
    }
    
    @Override
    Request getRequestForLoadData(final Session session) {
        if (this.adapter == null) {
            throw new FacebookException("Can't issue requests until Fragment has been created.");
        }
        String userId;
        if (this.userId != null) {
            userId = this.userId;
        }
        else {
            userId = "me";
        }
        return this.createRequest(userId, this.extraFields, session);
    }
    
    public List<GraphUser> getSelection() {
        return this.getSelectedGraphObjects();
    }
    
    public String getUserId() {
        return this.userId;
    }
    
    @Override
    public void loadData(final boolean b) {
        super.loadData(b);
        this.setSelectedGraphObjects(this.preSelectedFriendIds);
    }
    
    @Override
    void logAppEvents(final boolean b) {
        final AppEventsLogger logger = AppEventsLogger.newLogger((Context)this.getActivity(), this.getSession());
        final Bundle bundle = new Bundle();
        String s;
        if (b) {
            s = "Completed";
        }
        else {
            s = "Unknown";
        }
        bundle.putString("fb_dialog_outcome", s);
        bundle.putInt("num_friends_picked", this.getSelection().size());
        logger.logSdkEvent("fb_friend_picker_usage", null, bundle);
    }
    
    @Override
    public void onInflate(final Activity activity, final AttributeSet set, final Bundle bundle) {
        super.onInflate(activity, set, bundle);
        final TypedArray obtainStyledAttributes = activity.obtainStyledAttributes(set, R.styleable.com_facebook_friend_picker_fragment);
        this.setMultiSelect(obtainStyledAttributes.getBoolean(R.styleable.com_facebook_friend_picker_fragment_multi_select, this.multiSelect));
        obtainStyledAttributes.recycle();
    }
    
    @Override
    void saveSettingsToBundle(final Bundle bundle) {
        super.saveSettingsToBundle(bundle);
        bundle.putString("com.facebook.widget.FriendPickerFragment.UserId", this.userId);
        bundle.putBoolean("com.facebook.widget.FriendPickerFragment.MultiSelect", this.multiSelect);
    }
    
    public void setFriendPickerType(final FriendPickerType friendPickerType) {
        this.friendPickerType = friendPickerType;
    }
    
    public void setMultiSelect(final boolean multiSelect) {
        if (this.multiSelect != multiSelect) {
            this.multiSelect = multiSelect;
            this.setSelectionStrategy(this.createSelectionStrategy());
        }
    }
    
    public void setSelection(final List<GraphUser> list) {
        final ArrayList<String> selectionByIds = new ArrayList<String>();
        final Iterator<GraphUser> iterator = list.iterator();
        while (iterator.hasNext()) {
            selectionByIds.add(iterator.next().getId());
        }
        this.setSelectionByIds(selectionByIds);
    }
    
    public void setSelection(final GraphUser... array) {
        this.setSelection(Arrays.asList(array));
    }
    
    public void setSelectionByIds(final List<String> list) {
        this.preSelectedFriendIds.addAll(list);
    }
    
    public void setSelectionByIds(final String... array) {
        this.setSelectionByIds(Arrays.asList(array));
    }
    
    @Override
    public void setSettingsFromBundle(final Bundle bundle) {
        super.setSettingsFromBundle(bundle);
        this.setFriendPickerSettingsFromBundle(bundle);
    }
    
    public void setUserId(final String userId) {
        this.userId = userId;
    }
    
    public enum FriendPickerType
    {
        FRIENDS("/friends", true), 
        INVITABLE_FRIENDS("/invitable_friends", false), 
        TAGGABLE_FRIENDS("/taggable_friends", false);
        
        private final boolean requestIsCacheable;
        private final String requestPath;
        
        private FriendPickerType(final String requestPath, final boolean requestIsCacheable) {
            this.requestPath = requestPath;
            this.requestIsCacheable = requestIsCacheable;
        }
        
        String getRequestPath() {
            return this.requestPath;
        }
        
        boolean isCacheable() {
            return this.requestIsCacheable;
        }
    }
    
    private class ImmediateLoadingStrategy extends PickerFragment$LoadingStrategy
    {
        private void followNextLink() {
            FriendPickerFragment.this.displayActivityCircle();
            this.loader.followNextLink();
        }
        
        protected boolean canSkipRoundTripIfCached() {
            return FriendPickerFragment.this.friendPickerType.isCacheable();
        }
        
        protected void onLoadFinished(final GraphObjectPagingLoader<GraphUser> graphObjectPagingLoader, final SimpleGraphObjectCursor<GraphUser> simpleGraphObjectCursor) {
            super.onLoadFinished((GraphObjectPagingLoader<T>)graphObjectPagingLoader, (SimpleGraphObjectCursor<T>)simpleGraphObjectCursor);
            if (simpleGraphObjectCursor != null && !graphObjectPagingLoader.isLoading()) {
                if (simpleGraphObjectCursor.areMoreObjectsAvailable()) {
                    this.followNextLink();
                    return;
                }
                FriendPickerFragment.this.hideActivityCircle();
                if (simpleGraphObjectCursor.isFromCache()) {
                    long n;
                    if (simpleGraphObjectCursor.getCount() == 0) {
                        n = 2000L;
                    }
                    else {
                        n = 0L;
                    }
                    graphObjectPagingLoader.refreshOriginalRequest(n);
                }
            }
        }
    }
}
