// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ListView;
import android.view.ViewGroup;
import android.os.Parcelable;
import com.facebook.internal.Utility;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.app.Activity;
import com.facebook.AppEventsLogger;
import java.util.List;
import com.facebook.widget.PickerFragment$com.facebook.widget.PickerFragment$SelectionStrategy;
import com.facebook.widget.PickerFragment$com.facebook.widget.PickerFragment$LoadingStrategy;
import com.facebook.model.GraphObject;
import android.content.Context;
import com.facebook.widget.PickerFragment$com.facebook.widget.PickerFragment$PickerFragmentAdapter;
import com.facebook.FacebookException;
import com.facebook.internal.Logger;
import com.facebook.LoggingBehavior;
import android.os.Handler;
import android.os.Looper;
import java.util.TimerTask;
import android.text.TextUtils;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import com.facebook.Request;
import com.facebook.Session;
import java.util.Set;
import com.facebook.android.R;
import android.os.Bundle;
import java.util.Timer;
import android.widget.EditText;
import android.location.Location;
import com.facebook.model.GraphPlace;

public class PlacePickerFragment extends PickerFragment<GraphPlace>
{
    private static final String CATEGORY = "category";
    public static final int DEFAULT_RADIUS_IN_METERS = 1000;
    public static final int DEFAULT_RESULTS_LIMIT = 100;
    private static final String ID = "id";
    private static final String LOCATION = "location";
    public static final String LOCATION_BUNDLE_KEY = "com.facebook.widget.PlacePickerFragment.Location";
    private static final String NAME = "name";
    public static final String RADIUS_IN_METERS_BUNDLE_KEY = "com.facebook.widget.PlacePickerFragment.RadiusInMeters";
    public static final String RESULTS_LIMIT_BUNDLE_KEY = "com.facebook.widget.PlacePickerFragment.ResultsLimit";
    public static final String SEARCH_TEXT_BUNDLE_KEY = "com.facebook.widget.PlacePickerFragment.SearchText";
    public static final String SHOW_SEARCH_BOX_BUNDLE_KEY = "com.facebook.widget.PlacePickerFragment.ShowSearchBox";
    private static final String TAG = "PlacePickerFragment";
    private static final String WERE_HERE_COUNT = "were_here_count";
    private static final int searchTextTimerDelayInMilliseconds = 2000;
    private boolean hasSearchTextChangedSinceLastQuery;
    private Location location;
    private int radiusInMeters;
    private int resultsLimit;
    private EditText searchBox;
    private String searchText;
    private Timer searchTextTimer;
    private boolean showSearchBox;
    
    public PlacePickerFragment() {
        this(null);
    }
    
    public PlacePickerFragment(final Bundle placePickerSettingsFromBundle) {
        super(GraphPlace.class, R.layout.com_facebook_placepickerfragment, placePickerSettingsFromBundle);
        this.radiusInMeters = 1000;
        this.resultsLimit = 100;
        this.showSearchBox = true;
        this.setPlacePickerSettingsFromBundle(placePickerSettingsFromBundle);
    }
    
    private Request createRequest(final Location location, final int n, final int n2, final String s, final Set<String> set, final Session session) {
        final Request placesSearchRequest = Request.newPlacesSearchRequest(session, location, n, n2, s, null);
        final HashSet<Object> set2 = new HashSet<Object>(set);
        set2.addAll(Arrays.asList("id", "name", "location", "category", "were_here_count"));
        final String pictureFieldSpecifier = this.adapter.getPictureFieldSpecifier();
        if (pictureFieldSpecifier != null) {
            set2.add(pictureFieldSpecifier);
        }
        final Bundle parameters = placesSearchRequest.getParameters();
        parameters.putString("fields", TextUtils.join((CharSequence)",", (Iterable)set2));
        placesSearchRequest.setParameters(parameters);
        return placesSearchRequest;
    }
    
    private Timer createSearchTextTimer() {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                PlacePickerFragment.this.onSearchTextTimerTriggered();
            }
        }, 0L, 2000L);
        return timer;
    }
    
    private void onSearchTextTimerTriggered() {
        if (this.hasSearchTextChangedSinceLastQuery) {
            new Handler(Looper.getMainLooper()).post((Runnable)new Runnable() {
                @Override
                public void run() {
                    try {
                        PlacePickerFragment.this.loadData(true);
                        if (false) {
                            final OnErrorListener onErrorListener = PlacePickerFragment.this.getOnErrorListener();
                            if (onErrorListener == null) {
                                Logger.log(LoggingBehavior.REQUESTS, "PlacePickerFragment", "Error loading data : %s", null);
                                return;
                            }
                            onErrorListener.onError(PlacePickerFragment.this, null);
                        }
                    }
                    catch (FacebookException ex) {
                        if (ex == null) {
                            return;
                        }
                        final OnErrorListener onErrorListener2 = PlacePickerFragment.this.getOnErrorListener();
                        if (onErrorListener2 != null) {
                            onErrorListener2.onError(PlacePickerFragment.this, ex);
                            return;
                        }
                        Logger.log(LoggingBehavior.REQUESTS, "PlacePickerFragment", "Error loading data : %s", ex);
                    }
                    catch (Exception ex3) {
                        final FacebookException ex2 = new FacebookException(ex3);
                        if (ex2 != null) {
                            final OnErrorListener onErrorListener3 = PlacePickerFragment.this.getOnErrorListener();
                            if (onErrorListener3 != null) {
                                onErrorListener3.onError(PlacePickerFragment.this, ex2);
                            }
                            else {
                                Logger.log(LoggingBehavior.REQUESTS, "PlacePickerFragment", "Error loading data : %s", ex2);
                            }
                        }
                    }
                    finally {
                        while (true) {
                            if (!false) {
                                break Label_0190;
                            }
                            final OnErrorListener onErrorListener4 = PlacePickerFragment.this.getOnErrorListener();
                            if (onErrorListener4 != null) {
                                onErrorListener4.onError(PlacePickerFragment.this, null);
                                break Label_0190;
                            }
                            Logger.log(LoggingBehavior.REQUESTS, "PlacePickerFragment", "Error loading data : %s", null);
                            break Label_0190;
                            continue;
                        }
                    }
                }
            });
            return;
        }
        this.searchTextTimer.cancel();
        this.searchTextTimer = null;
    }
    
    private void setPlacePickerSettingsFromBundle(final Bundle bundle) {
        if (bundle != null) {
            this.setRadiusInMeters(bundle.getInt("com.facebook.widget.PlacePickerFragment.RadiusInMeters", this.radiusInMeters));
            this.setResultsLimit(bundle.getInt("com.facebook.widget.PlacePickerFragment.ResultsLimit", this.resultsLimit));
            if (bundle.containsKey("com.facebook.widget.PlacePickerFragment.SearchText")) {
                this.setSearchText(bundle.getString("com.facebook.widget.PlacePickerFragment.SearchText"));
            }
            if (bundle.containsKey("com.facebook.widget.PlacePickerFragment.Location")) {
                this.setLocation((Location)bundle.getParcelable("com.facebook.widget.PlacePickerFragment.Location"));
            }
            this.showSearchBox = bundle.getBoolean("com.facebook.widget.PlacePickerFragment.ShowSearchBox", this.showSearchBox);
        }
    }
    
    @Override
    PickerFragment$PickerFragmentAdapter<GraphPlace> createAdapter() {
        final PickerFragment$PickerFragmentAdapter<GraphPlace> pickerFragment$PickerFragmentAdapter = new PickerFragment$PickerFragmentAdapter<GraphPlace>(this.getActivity()) {
            protected int getDefaultPicture() {
                return R.drawable.com_facebook_place_default_icon;
            }
            
            protected int getGraphObjectRowLayoutId(final GraphPlace graphPlace) {
                return R.layout.com_facebook_placepickerfragment_list_row;
            }
            
            protected CharSequence getSubTitleOfGraphObject(final GraphPlace graphPlace) {
                final String category = graphPlace.getCategory();
                final Integer n = (Integer)graphPlace.getProperty("were_here_count");
                final String s = null;
                String string;
                if (category != null && n != null) {
                    string = PlacePickerFragment.this.getString(R.string.com_facebook_placepicker_subtitle_format, category, n);
                }
                else {
                    if (category == null && n != null) {
                        return PlacePickerFragment.this.getString(R.string.com_facebook_placepicker_subtitle_were_here_only_format, n);
                    }
                    string = s;
                    if (category != null) {
                        string = s;
                        if (n == null) {
                            return PlacePickerFragment.this.getString(R.string.com_facebook_placepicker_subtitle_catetory_only_format, category);
                        }
                    }
                }
                return string;
            }
        };
        ((GraphObjectAdapter)pickerFragment$PickerFragmentAdapter).setShowCheckbox(false);
        ((GraphObjectAdapter)pickerFragment$PickerFragmentAdapter).setShowPicture(this.getShowPictures());
        return pickerFragment$PickerFragmentAdapter;
    }
    
    @Override
    PickerFragment$LoadingStrategy createLoadingStrategy() {
        return new AsNeededLoadingStrategy();
    }
    
    @Override
    PickerFragment$SelectionStrategy createSelectionStrategy() {
        return new SingleSelectionStrategy(this);
    }
    
    @Override
    String getDefaultTitleText() {
        return this.getString(R.string.com_facebook_nearby);
    }
    
    public Location getLocation() {
        return this.location;
    }
    
    public int getRadiusInMeters() {
        return this.radiusInMeters;
    }
    
    @Override
    Request getRequestForLoadData(final Session session) {
        return this.createRequest(this.location, this.radiusInMeters, this.resultsLimit, this.searchText, this.extraFields, session);
    }
    
    public int getResultsLimit() {
        return this.resultsLimit;
    }
    
    public String getSearchText() {
        return this.searchText;
    }
    
    public GraphPlace getSelection() {
        final List<GraphPlace> selectedGraphObjects = this.getSelectedGraphObjects();
        if (selectedGraphObjects != null && !selectedGraphObjects.isEmpty()) {
            return selectedGraphObjects.iterator().next();
        }
        return null;
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
        int n;
        if (this.getSelection() != null) {
            n = 1;
        }
        else {
            n = 0;
        }
        bundle.putInt("num_places_picked", n);
        logger.logSdkEvent("fb_place_picker_usage", null, bundle);
    }
    
    @Override
    public void onAttach(final Activity activity) {
        super.onAttach(activity);
        if (this.searchBox != null) {
            ((InputMethodManager)this.getActivity().getSystemService("input_method")).showSoftInput((View)this.searchBox, 1);
        }
    }
    
    @Override
    public void onDetach() {
        super.onDetach();
        if (this.searchBox != null) {
            ((InputMethodManager)this.getActivity().getSystemService("input_method")).hideSoftInputFromWindow(this.searchBox.getWindowToken(), 0);
        }
    }
    
    @Override
    public void onInflate(final Activity activity, final AttributeSet set, final Bundle bundle) {
        super.onInflate(activity, set, bundle);
        final TypedArray obtainStyledAttributes = activity.obtainStyledAttributes(set, R.styleable.com_facebook_place_picker_fragment);
        this.setRadiusInMeters(obtainStyledAttributes.getInt(R.styleable.com_facebook_place_picker_fragment_radius_in_meters, this.radiusInMeters));
        this.setResultsLimit(obtainStyledAttributes.getInt(R.styleable.com_facebook_place_picker_fragment_results_limit, this.resultsLimit));
        if (obtainStyledAttributes.hasValue(R.styleable.com_facebook_place_picker_fragment_results_limit)) {
            this.setSearchText(obtainStyledAttributes.getString(R.styleable.com_facebook_place_picker_fragment_search_text));
        }
        this.showSearchBox = obtainStyledAttributes.getBoolean(R.styleable.com_facebook_place_picker_fragment_show_search_box, this.showSearchBox);
        obtainStyledAttributes.recycle();
    }
    
    @Override
    void onLoadingData() {
        this.hasSearchTextChangedSinceLastQuery = false;
    }
    
    public void onSearchBoxTextChanged(final String s, final boolean b) {
        if (b || !Utility.stringsEqualOrEmpty(this.searchText, s)) {
            String searchText = s;
            if (TextUtils.isEmpty((CharSequence)s)) {
                searchText = null;
            }
            this.searchText = searchText;
            this.hasSearchTextChangedSinceLastQuery = true;
            if (this.searchTextTimer == null) {
                this.searchTextTimer = this.createSearchTextTimer();
            }
        }
    }
    
    @Override
    void saveSettingsToBundle(final Bundle bundle) {
        super.saveSettingsToBundle(bundle);
        bundle.putInt("com.facebook.widget.PlacePickerFragment.RadiusInMeters", this.radiusInMeters);
        bundle.putInt("com.facebook.widget.PlacePickerFragment.ResultsLimit", this.resultsLimit);
        bundle.putString("com.facebook.widget.PlacePickerFragment.SearchText", this.searchText);
        bundle.putParcelable("com.facebook.widget.PlacePickerFragment.Location", (Parcelable)this.location);
        bundle.putBoolean("com.facebook.widget.PlacePickerFragment.ShowSearchBox", this.showSearchBox);
    }
    
    public void setLocation(final Location location) {
        this.location = location;
    }
    
    public void setRadiusInMeters(final int radiusInMeters) {
        this.radiusInMeters = radiusInMeters;
    }
    
    public void setResultsLimit(final int resultsLimit) {
        this.resultsLimit = resultsLimit;
    }
    
    public void setSearchText(final String s) {
        String s2 = s;
        if (TextUtils.isEmpty((CharSequence)s)) {
            s2 = null;
        }
        this.searchText = s2;
        if (this.searchBox != null) {
            this.searchBox.setText((CharSequence)s2);
        }
    }
    
    @Override
    public void setSettingsFromBundle(final Bundle bundle) {
        super.setSettingsFromBundle(bundle);
        this.setPlacePickerSettingsFromBundle(bundle);
    }
    
    @Override
    void setupViews(final ViewGroup viewGroup) {
        if (this.showSearchBox) {
            final ListView listView = (ListView)viewGroup.findViewById(R.id.com_facebook_picker_list_view);
            listView.addHeaderView(this.getActivity().getLayoutInflater().inflate(R.layout.com_facebook_picker_search_box, (ViewGroup)listView, false), (Object)null, false);
            (this.searchBox = (EditText)viewGroup.findViewById(R.id.com_facebook_picker_search_text)).addTextChangedListener((TextWatcher)new SearchTextWatcher());
            if (!TextUtils.isEmpty((CharSequence)this.searchText)) {
                this.searchBox.setText((CharSequence)this.searchText);
            }
        }
    }
    
    private class AsNeededLoadingStrategy extends PickerFragment$LoadingStrategy
    {
        public void attach(final GraphObjectAdapter<GraphPlace> graphObjectAdapter) {
            super.attach((GraphObjectAdapter<T>)graphObjectAdapter);
            this.adapter.setDataNeededListener((GraphObjectAdapter.DataNeededListener)new GraphObjectAdapter.DataNeededListener() {
                @Override
                public void onDataNeeded() {
                    if (!AsNeededLoadingStrategy.this.loader.isLoading()) {
                        AsNeededLoadingStrategy.this.loader.followNextLink();
                    }
                }
            });
        }
        
        protected void onLoadFinished(final GraphObjectPagingLoader<GraphPlace> graphObjectPagingLoader, final SimpleGraphObjectCursor<GraphPlace> simpleGraphObjectCursor) {
            super.onLoadFinished((GraphObjectPagingLoader<T>)graphObjectPagingLoader, (SimpleGraphObjectCursor<T>)simpleGraphObjectCursor);
            if (simpleGraphObjectCursor != null && !graphObjectPagingLoader.isLoading()) {
                PlacePickerFragment.this.hideActivityCircle();
                if (simpleGraphObjectCursor.isFromCache()) {
                    long n;
                    if (simpleGraphObjectCursor.areMoreObjectsAvailable()) {
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
    
    private class SearchTextWatcher implements TextWatcher
    {
        public void afterTextChanged(final Editable editable) {
        }
        
        public void beforeTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
        }
        
        public void onTextChanged(final CharSequence charSequence, final int n, final int n2, final int n3) {
            PlacePickerFragment.this.onSearchBoxTextChanged(charSequence.toString(), false);
        }
    }
}
