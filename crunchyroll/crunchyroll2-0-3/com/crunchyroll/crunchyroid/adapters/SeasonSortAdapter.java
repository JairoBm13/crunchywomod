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
import android.widget.ArrayAdapter;

public class SeasonSortAdapter extends ArrayAdapter<String>
{
    private Context mContext;
    private String[] mSorts;
    private Spinner mSpinner;
    
    public SeasonSortAdapter(final Context mContext, final int n, final Spinner mSpinner, final String[] mSorts) {
        super(mContext, n, (Object[])mSorts);
        this.mContext = mContext;
        this.mSpinner = mSpinner;
        this.mSorts = mSorts;
    }
    
    public View getDropDownView(final int n, View inflate, final ViewGroup viewGroup) {
        inflate = LayoutInflater.from(this.mContext).inflate(2130903185, viewGroup, false);
        ((TextView)inflate.findViewById(2131624349)).setText((CharSequence)this.mSorts[n]);
        if (n == this.mSpinner.getSelectedItemPosition()) {
            inflate.setBackgroundColor(this.mContext.getResources().getColor(2131558416));
        }
        return inflate;
    }
    
    public View getView(final int n, View inflate, final ViewGroup viewGroup) {
        inflate = LayoutInflater.from(this.mContext).inflate(2130903186, viewGroup, false);
        ((TextView)inflate.findViewById(2131624349)).setText((CharSequence)this.mSorts[n]);
        return inflate;
    }
}
