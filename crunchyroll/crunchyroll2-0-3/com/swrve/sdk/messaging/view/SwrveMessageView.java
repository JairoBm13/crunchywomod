// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.messaging.view;

import android.view.ViewGroup;
import android.view.MotionEvent;
import android.view.animation.RotateAnimation;
import android.view.View;
import android.view.ViewGroup$LayoutParams;
import android.widget.RelativeLayout$LayoutParams;
import android.graphics.Canvas;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Animation$AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.ViewParent;
import com.swrve.sdk.messaging.ISwrveCustomButtonListener;
import com.swrve.sdk.messaging.ISwrveInstallButtonListener;
import android.util.AttributeSet;
import android.content.Context;
import com.swrve.sdk.messaging.SwrveMessage;
import com.swrve.sdk.messaging.SwrveMessageFormat;
import android.app.Dialog;
import android.widget.RelativeLayout;

public class SwrveMessageView extends RelativeLayout
{
    protected Dialog containerDialog;
    protected int dismissAnimation;
    protected boolean firstDraw;
    protected boolean firstTime;
    protected SwrveMessageFormat format;
    protected SwrveInnerMessageView innerMessageView;
    protected SwrveMessage message;
    protected int showAnimation;
    
    public SwrveMessageView(final Context context) {
        super(context);
        this.firstTime = true;
        this.firstDraw = true;
    }
    
    public SwrveMessageView(final Context context, final AttributeSet set) {
        super(context, set);
        this.firstTime = true;
        this.firstDraw = true;
    }
    
    public SwrveMessageView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.firstTime = true;
        this.firstDraw = true;
    }
    
    public SwrveMessageView(final Context context, final SwrveMessage message, final SwrveMessageFormat format, final ISwrveInstallButtonListener swrveInstallButtonListener, final ISwrveCustomButtonListener swrveCustomButtonListener, final boolean firstTime, final int n, final int n2) throws SwrveMessageViewBuildException {
        super(context);
        this.firstTime = true;
        this.firstDraw = true;
        this.message = message;
        this.format = format;
        this.firstTime = firstTime;
        this.initializeLayout(context, message, format, swrveInstallButtonListener, swrveCustomButtonListener, n, n2);
    }
    
    private void dismissView() {
        final ViewParent parent = this.getParent();
        if (this.containerDialog == null) {
            this.removeView(parent);
            return;
        }
        this.containerDialog.dismiss();
    }
    
    public void destroy() {
        if (this.innerMessageView != null) {
            this.innerMessageView.destroy();
        }
    }
    
    public void dismiss() {
        try {
            if (this.dismissAnimation != 0) {
                final Animation loadAnimation = AnimationUtils.loadAnimation(this.getContext(), this.dismissAnimation);
                loadAnimation.setStartOffset(0L);
                loadAnimation.setAnimationListener((Animation$AnimationListener)new Animation$AnimationListener() {
                    public void onAnimationEnd(final Animation animation) {
                        SwrveMessageView.this.dismissView();
                    }
                    
                    public void onAnimationRepeat(final Animation animation) {
                    }
                    
                    public void onAnimationStart(final Animation animation) {
                    }
                });
                this.startAnimation(loadAnimation);
                return;
            }
            this.dismissView();
        }
        catch (Exception ex) {
            Log.e("SwrveMessagingSDK", "Error while dismissing message", (Throwable)ex);
        }
    }
    
    public void draw(final Canvas canvas) {
        super.draw(canvas);
        try {
            if (this.firstTime && this.firstDraw) {
                this.firstDraw = false;
                this.message.getMessageController().messageWasShownToUser(this.format);
            }
        }
        catch (Exception ex) {
            Log.e("SwrveMessagingSDK", "Error while processing first impression", (Throwable)ex);
        }
    }
    
    protected void finalize() throws Throwable {
        super.finalize();
        this.destroy();
    }
    
    protected void initializeLayout(final Context context, final SwrveMessage swrveMessage, final SwrveMessageFormat swrveMessageFormat, final ISwrveInstallButtonListener swrveInstallButtonListener, final ISwrveCustomButtonListener swrveCustomButtonListener, final int n, final int n2) throws SwrveMessageViewBuildException {
        this.innerMessageView = new SwrveInnerMessageView(context, this, swrveMessage, swrveMessageFormat, swrveInstallButtonListener, swrveCustomButtonListener, n2);
        this.setBackgroundColor(swrveMessageFormat.getBackgroundColor());
        this.setGravity(17);
        this.setLayoutParams((ViewGroup$LayoutParams)new RelativeLayout$LayoutParams(-1, -1));
        this.addView((View)this.innerMessageView);
        this.setClipChildren(false);
        if (n != 0) {
            final RotateAnimation rotateAnimation = new RotateAnimation(0.0f, (float)n, 1, 0.5f, 1, 0.5f);
            rotateAnimation.setFillAfter(true);
            this.innerMessageView.startAnimation((Animation)rotateAnimation);
        }
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.destroy();
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        return true;
    }
    
    protected void removeView(final ViewParent viewParent) {
        if (viewParent != null) {
            ((ViewGroup)viewParent).removeView((View)this);
        }
        this.destroy();
    }
    
    public void setContainerDialog(final Dialog containerDialog) {
        this.containerDialog = containerDialog;
    }
    
    public void setCustomButtonListener(final ISwrveCustomButtonListener customButtonListener) {
        this.innerMessageView.setCustomButtonListener(customButtonListener);
    }
    
    public void setDismissAnimation(final int dismissAnimation) {
        this.dismissAnimation = dismissAnimation;
    }
    
    public void setInstallButtonListener(final ISwrveInstallButtonListener installButtonListener) {
        this.innerMessageView.setInstallButtonListener(installButtonListener);
    }
    
    public void setShowAnimation(final int showAnimation) {
        this.showAnimation = showAnimation;
    }
}
