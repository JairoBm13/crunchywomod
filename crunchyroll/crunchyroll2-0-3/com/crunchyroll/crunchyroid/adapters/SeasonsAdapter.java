// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.adapters;

import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.Spinner;
import android.content.Context;
import java.util.List;
import com.crunchyroll.android.api.models.Collection;
import android.widget.ArrayAdapter;

public class SeasonsAdapter extends ArrayAdapter<Collection>
{
    private List<Collection> mCollections;
    private Context mContext;
    private Spinner mSpinner;
    
    public SeasonsAdapter(final Context mContext, final int n, final Spinner mSpinner, final List<Collection> mCollections) {
        super(mContext, n, (List)mCollections);
        this.mContext = mContext;
        this.mSpinner = mSpinner;
        this.mCollections = mCollections;
    }
    
    public View getDropDownView(final int n, View inflate, final ViewGroup viewGroup) {
        inflate = LayoutInflater.from(this.mContext).inflate(2130903185, viewGroup, false);
        ((TextView)inflate.findViewById(2131624349)).setText((CharSequence)this.mCollections.get(n).getName());
        if (n == this.mSpinner.getSelectedItemPosition()) {
            inflate.setBackgroundColor(this.mContext.getResources().getColor(2131558416));
        }
        return inflate;
    }
    
    public View getView(final int n, View inflate, final ViewGroup viewGroup) {
        inflate = LayoutInflater.from(this.mContext).inflate(2130903184, viewGroup, false);
        ((TextView)inflate.findViewById(2131624349)).setText((CharSequence)this.mCollections.get(n).getName());
        return inflate;
    }
}
