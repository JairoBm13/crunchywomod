// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api;

import java.util.Iterator;
import java.util.ArrayList;
import com.crunchyroll.android.api.models.Categories;
import com.crunchyroll.crunchyroid.app.LocalizedStrings;
import com.crunchyroll.android.api.models.Category;
import com.crunchyroll.crunchyroid.app.CrunchyrollApplication;
import android.content.Context;

public final class Filters
{
    public static final String FILTER_ALPHA = "alpha";
    public static final String FILTER_FEATURED = "featured";
    public static final String FILTER_NEWEST = "newest";
    public static final String FILTER_POPULAR = "popular";
    public static final String FILTER_SIMULCAST = "simulcast";
    public static final String FILTER_UPDATED = "updated";
    public static final String TAG_PREFIX = "prefix:";
    public static final String TAG_SEASON = "season:";
    public static final String TAG_TAG = "tag:";
    
    public static final String addSeason(final String s) {
        return "season:" + s;
    }
    
    public static final String addTag(final String s) {
        return "tag:" + s;
    }
    
    public static final String filterPrefix(final String s) {
        return "prefix:" + s;
    }
    
    public static final String getTitle(final Context context, final String s, String removeTag) {
        if (removeTag != null) {
            if (removeTag.contains("tag:")) {
                removeTag = removeTag(removeTag);
                final Categories categories = CrunchyrollApplication.getApp(context).getCategories(s);
                if (categories != null) {
                    ArrayList<Category> list;
                    if (removeTag.contains("season:")) {
                        list = categories.getSeasons();
                    }
                    else {
                        list = categories.getGenres();
                    }
                    for (final Category category : list) {
                        if (category.getTag().equalsIgnoreCase(removeTag)) {
                            return category.getLabel();
                        }
                    }
                }
            }
            else {
                if ("popular".equalsIgnoreCase(removeTag)) {
                    return LocalizedStrings.POPULAR.get();
                }
                if ("updated".equalsIgnoreCase(removeTag)) {
                    return LocalizedStrings.UPDATED.get();
                }
                if ("simulcast".equalsIgnoreCase(removeTag)) {
                    return LocalizedStrings.SIMULCASTS.get();
                }
                if ("alpha".equalsIgnoreCase(removeTag)) {
                    return LocalizedStrings.A_TO_Z.get();
                }
            }
        }
        return "";
    }
    
    public static boolean isSeason(final String s) {
        return s != null && s.contains("season:");
    }
    
    public static boolean isTag(final String s) {
        return s != null && s.contains("tag:");
    }
    
    public static final String removeSeason(final String s) {
        return s.replace("season:", "");
    }
    
    public static final String removeTag(final String s) {
        return s.replace("tag:", "");
    }
}
