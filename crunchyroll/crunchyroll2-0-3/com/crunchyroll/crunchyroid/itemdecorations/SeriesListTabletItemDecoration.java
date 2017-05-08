// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.itemdecorations;

import android.util.DisplayMetrics;
import android.view.View;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;

public class SeriesListTabletItemDecoration extends ItemDecoration
{
    public static final int MARGIN_LR = 8;
    final int mNumHeaders;
    
    public SeriesListTabletItemDecoration(final int mNumHeaders) {
        this.mNumHeaders = mNumHeaders;
    }
    
    @Override
    public void getItemOffsets(final Rect rect, final View view, final RecyclerView recyclerView, final State state) {
        if (recyclerView.getChildPosition(view) >= this.mNumHeaders) {
            final DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();
            rect.bottom = (int)(10.0f * displayMetrics.density);
            rect.left = (int)(displayMetrics.density * 8.0f);
            rect.right = (int)(displayMetrics.density * 8.0f);
            rect.top = 0;
        }
    }
}
