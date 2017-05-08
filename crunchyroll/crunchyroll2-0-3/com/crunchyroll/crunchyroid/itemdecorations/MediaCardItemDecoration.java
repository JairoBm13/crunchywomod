// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.crunchyroid.itemdecorations;

import android.util.DisplayMetrics;
import android.view.View;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;

public class MediaCardItemDecoration extends ItemDecoration
{
    final int mNumColumns;
    
    public MediaCardItemDecoration(final int mNumColumns) {
        this.mNumColumns = mNumColumns;
    }
    
    @Override
    public void getItemOffsets(final Rect rect, final View view, final RecyclerView recyclerView, final State state) {
        final DisplayMetrics displayMetrics = view.getContext().getResources().getDisplayMetrics();
        rect.bottom = (int)(10.0f * displayMetrics.density);
        rect.left = 0;
        rect.right = 0;
        rect.top = 0;
        final int childPosition = recyclerView.getChildPosition(view);
        if (childPosition % this.mNumColumns == 0) {
            rect.right = (int)(displayMetrics.density * 5.0f);
            return;
        }
        if (childPosition % this.mNumColumns == this.mNumColumns - 1) {
            rect.left = (int)(displayMetrics.density * 5.0f);
            return;
        }
        rect.left = (int)(displayMetrics.density * 5.0f);
        rect.right = (int)(displayMetrics.density * 5.0f);
    }
}
