// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations.ui;

import android.view.View$MeasureSpec;
import android.content.Context;
import com.swrve.sdk.conversations.engine.model.Content;
import android.widget.ImageView;

public class ConversationImageView extends ImageView
{
    private final Content model;
    
    public ConversationImageView(final Context context, final Content model) {
        super(context);
        this.model = model;
    }
    
    protected void onMeasure(int size, int n) {
        n = (size = View$MeasureSpec.getSize(size));
        final int intrinsicWidth = this.getDrawable().getIntrinsicWidth();
        if (intrinsicWidth > 0) {
            size = this.getDrawable().getIntrinsicHeight() * n / intrinsicWidth;
        }
        this.setMeasuredDimension(n, size);
    }
}
