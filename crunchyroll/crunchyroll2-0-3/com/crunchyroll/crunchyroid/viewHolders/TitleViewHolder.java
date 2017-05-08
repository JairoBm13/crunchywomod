// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.viewHolders;

import android.view.View;
import android.widget.TextView;
import android.widget.ImageView;
import android.support.v7.widget.RecyclerView;

public class TitleViewHolder extends ViewHolder
{
    private ImageView mDropDown;
    private TextView mTitle;
    
    public TitleViewHolder(final View view) {
        super(view);
        this.mTitle = (TextView)view.findViewById(2131624020);
        this.mDropDown = (ImageView)view.findViewById(2131624283);
    }
    
    public TextView getTitle() {
        return this.mTitle;
    }
    
    public void hideDropDownIcon() {
        this.mDropDown.setVisibility(4);
    }
    
    public void showDropDownIcon() {
        this.mDropDown.setVisibility(0);
    }
}
