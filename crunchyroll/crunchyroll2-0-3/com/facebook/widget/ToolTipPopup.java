// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.widget;

import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.view.View$OnClickListener;
import android.view.View$MeasureSpec;
import android.app.Activity;
import com.facebook.android.R;
import android.widget.TextView;
import android.view.ViewTreeObserver$OnScrollChangedListener;
import android.widget.PopupWindow;
import android.content.Context;
import android.view.View;
import java.lang.ref.WeakReference;

public class ToolTipPopup
{
    public static final long DEFAULT_POPUP_DISPLAY_TIME = 6000L;
    private final WeakReference<View> mAnchorViewRef;
    private final Context mContext;
    private long mNuxDisplayTime;
    private PopupContentView mPopupContent;
    private PopupWindow mPopupWindow;
    private final ViewTreeObserver$OnScrollChangedListener mScrollListener;
    private Style mStyle;
    private final String mText;
    
    public ToolTipPopup(final String mText, final View view) {
        this.mStyle = Style.BLUE;
        this.mNuxDisplayTime = 6000L;
        this.mScrollListener = (ViewTreeObserver$OnScrollChangedListener)new ViewTreeObserver$OnScrollChangedListener() {
            public void onScrollChanged() {
                if (ToolTipPopup.this.mAnchorViewRef.get() != null && ToolTipPopup.this.mPopupWindow != null && ToolTipPopup.this.mPopupWindow.isShowing()) {
                    if (!ToolTipPopup.this.mPopupWindow.isAboveAnchor()) {
                        ToolTipPopup.this.mPopupContent.showTopArrow();
                        return;
                    }
                    ToolTipPopup.this.mPopupContent.showBottomArrow();
                }
            }
        };
        this.mText = mText;
        this.mAnchorViewRef = new WeakReference<View>(view);
        this.mContext = view.getContext();
    }
    
    private void registerObserver() {
        this.unregisterObserver();
        if (this.mAnchorViewRef.get() != null) {
            this.mAnchorViewRef.get().getViewTreeObserver().addOnScrollChangedListener(this.mScrollListener);
        }
    }
    
    private void unregisterObserver() {
        if (this.mAnchorViewRef.get() != null) {
            this.mAnchorViewRef.get().getViewTreeObserver().removeOnScrollChangedListener(this.mScrollListener);
        }
    }
    
    private void updateArrows() {
        if (this.mPopupWindow != null && this.mPopupWindow.isShowing()) {
            if (!this.mPopupWindow.isAboveAnchor()) {
                this.mPopupContent.showTopArrow();
                return;
            }
            this.mPopupContent.showBottomArrow();
        }
    }
    
    public void dismiss() {
        this.unregisterObserver();
        if (this.mPopupWindow != null) {
            this.mPopupWindow.dismiss();
        }
    }
    
    public void setNuxDisplayTime(final long mNuxDisplayTime) {
        this.mNuxDisplayTime = mNuxDisplayTime;
    }
    
    public void setStyle(final Style mStyle) {
        this.mStyle = mStyle;
    }
    
    public void show() {
        if (this.mAnchorViewRef.get() != null) {
            this.mPopupContent = new PopupContentView(this.mContext);
            ((TextView)this.mPopupContent.findViewById(R.id.com_facebook_tooltip_bubble_view_text_body)).setText((CharSequence)this.mText);
            if (this.mStyle == Style.BLUE) {
                this.mPopupContent.bodyFrame.setBackgroundResource(R.drawable.com_facebook_tooltip_blue_background);
                this.mPopupContent.bottomArrow.setImageResource(R.drawable.com_facebook_tooltip_blue_bottomnub);
                this.mPopupContent.topArrow.setImageResource(R.drawable.com_facebook_tooltip_blue_topnub);
                this.mPopupContent.xOut.setImageResource(R.drawable.com_facebook_tooltip_blue_xout);
            }
            else {
                this.mPopupContent.bodyFrame.setBackgroundResource(R.drawable.com_facebook_tooltip_black_background);
                this.mPopupContent.bottomArrow.setImageResource(R.drawable.com_facebook_tooltip_black_bottomnub);
                this.mPopupContent.topArrow.setImageResource(R.drawable.com_facebook_tooltip_black_topnub);
                this.mPopupContent.xOut.setImageResource(R.drawable.com_facebook_tooltip_black_xout);
            }
            final View decorView = ((Activity)this.mContext).getWindow().getDecorView();
            final int width = decorView.getWidth();
            final int height = decorView.getHeight();
            this.registerObserver();
            this.mPopupContent.onMeasure(View$MeasureSpec.makeMeasureSpec(width, Integer.MIN_VALUE), View$MeasureSpec.makeMeasureSpec(height, Integer.MIN_VALUE));
            (this.mPopupWindow = new PopupWindow((View)this.mPopupContent, this.mPopupContent.getMeasuredWidth(), this.mPopupContent.getMeasuredHeight())).showAsDropDown((View)this.mAnchorViewRef.get());
            this.updateArrows();
            if (this.mNuxDisplayTime > 0L) {
                this.mPopupContent.postDelayed((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        ToolTipPopup.this.dismiss();
                    }
                }, this.mNuxDisplayTime);
            }
            this.mPopupWindow.setTouchable(true);
            this.mPopupContent.setOnClickListener((View$OnClickListener)new View$OnClickListener() {
                public void onClick(final View view) {
                    ToolTipPopup.this.dismiss();
                }
            });
        }
    }
    
    private class PopupContentView extends FrameLayout
    {
        private View bodyFrame;
        private ImageView bottomArrow;
        private ImageView topArrow;
        private ImageView xOut;
        
        public PopupContentView(final Context context) {
            super(context);
            this.init();
        }
        
        private void init() {
            LayoutInflater.from(this.getContext()).inflate(R.layout.com_facebook_tooltip_bubble, (ViewGroup)this);
            this.topArrow = (ImageView)this.findViewById(R.id.com_facebook_tooltip_bubble_view_top_pointer);
            this.bottomArrow = (ImageView)this.findViewById(R.id.com_facebook_tooltip_bubble_view_bottom_pointer);
            this.bodyFrame = this.findViewById(R.id.com_facebook_body_frame);
            this.xOut = (ImageView)this.findViewById(R.id.com_facebook_button_xout);
        }
        
        public void onMeasure(final int n, final int n2) {
            super.onMeasure(n, n2);
        }
        
        public void showBottomArrow() {
            this.topArrow.setVisibility(4);
            this.bottomArrow.setVisibility(0);
        }
        
        public void showTopArrow() {
            this.topArrow.setVisibility(0);
            this.bottomArrow.setVisibility(4);
        }
    }
    
    public enum Style
    {
        BLACK, 
        BLUE;
    }
}
