// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.viewHolders;

import android.widget.TextView;
import android.widget.RelativeLayout;
import android.view.View;
import android.widget.ImageView;
import android.support.v7.widget.RecyclerView;

public class SeriesCardViewHolder extends ViewHolder
{
    public ImageView image;
    private View mCardLarge;
    private int mCardSize;
    private View mCardSmall;
    private View mList;
    private RelativeLayout mainView;
    public TextView more;
    public TextView seriesDescription;
    public TextView subtitle;
    public TextView title;
    
    public SeriesCardViewHolder(final RelativeLayout mainView, final View mList, final View mCardSmall, final View mCardLarge) {
        super((View)mainView);
        this.mCardSize = 1;
        this.mainView = mainView;
        this.mList = mList;
        this.mCardSmall = mCardSmall;
        this.mCardLarge = mCardLarge;
        this.image = (ImageView)mList.findViewById(2131624016);
        this.title = (TextView)mList.findViewById(2131624090);
        this.subtitle = (TextView)mList.findViewById(2131624091);
        this.seriesDescription = (TextView)mList.findViewById(2131624273);
        this.more = (TextView)mList.findViewById(2131624284);
    }
    
    public int getCardSize() {
        return this.mCardSize;
    }
    
    public void switchView(final int mCardSize) {
        this.mCardSize = mCardSize;
        this.mainView.removeAllViews();
        switch (this.mCardSize) {
            default: {}
            case 1: {
                this.mainView.addView(this.mList);
                this.image = (ImageView)this.mList.findViewById(2131624016);
                this.title = (TextView)this.mList.findViewById(2131624090);
                this.subtitle = (TextView)this.mList.findViewById(2131624091);
                this.seriesDescription = (TextView)this.mList.findViewById(2131624273);
                this.more = (TextView)this.mList.findViewById(2131624284);
            }
            case 2: {
                this.mainView.addView(this.mCardSmall);
                this.image = (ImageView)this.mCardSmall.findViewById(2131624016);
                this.title = (TextView)this.mCardSmall.findViewById(2131624090);
                this.subtitle = (TextView)this.mCardSmall.findViewById(2131624091);
                this.seriesDescription = (TextView)this.mCardSmall.findViewById(2131624273);
                this.more = (TextView)this.mCardSmall.findViewById(2131624284);
            }
            case 3: {
                this.mainView.addView(this.mCardLarge);
                this.image = (ImageView)this.mCardLarge.findViewById(2131624016);
                this.title = (TextView)this.mCardLarge.findViewById(2131624090);
                this.subtitle = (TextView)this.mCardLarge.findViewById(2131624091);
                this.seriesDescription = (TextView)this.mCardLarge.findViewById(2131624273);
                this.more = (TextView)this.mCardLarge.findViewById(2131624284);
            }
        }
    }
}
