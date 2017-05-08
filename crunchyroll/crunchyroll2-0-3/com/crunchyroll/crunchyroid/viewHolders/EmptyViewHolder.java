// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.viewHolders;

import android.view.View;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

public class EmptyViewHolder extends ViewHolder
{
    public TextView mEmptyDescription;
    public TextView mEmptyText;
    
    public EmptyViewHolder(final View view) {
        super(view);
        this.mEmptyText = (TextView)view.findViewById(2131624182);
        this.mEmptyDescription = (TextView)view.findViewById(2131624183);
    }
}
