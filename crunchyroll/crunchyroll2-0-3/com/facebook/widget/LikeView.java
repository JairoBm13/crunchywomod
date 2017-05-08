// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.widget;

import android.content.ContextWrapper;
import android.app.Activity;
import android.content.res.TypedArray;
import android.widget.LinearLayout$LayoutParams;
import android.view.View$OnClickListener;
import android.view.View;
import android.view.ViewGroup$LayoutParams;
import android.widget.FrameLayout$LayoutParams;
import com.facebook.android.R;
import android.content.Intent;
import com.facebook.internal.Utility;
import android.os.Bundle;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.TextView;
import com.facebook.internal.LikeButton;
import com.facebook.internal.LikeBoxCountView;
import com.facebook.internal.LikeActionController;
import android.widget.LinearLayout;
import android.content.BroadcastReceiver;
import android.widget.FrameLayout;

public class LikeView extends FrameLayout
{
    private static final int NO_FOREGROUND_COLOR = -1;
    private AuxiliaryViewPosition auxiliaryViewPosition;
    private BroadcastReceiver broadcastReceiver;
    private LinearLayout containerView;
    private LikeActionControllerCreationCallback creationCallback;
    private int edgePadding;
    private int foregroundColor;
    private HorizontalAlignment horizontalAlignment;
    private int internalPadding;
    private LikeActionController likeActionController;
    private LikeBoxCountView likeBoxCountView;
    private LikeButton likeButton;
    private Style likeViewStyle;
    private String objectId;
    private OnErrorListener onErrorListener;
    private TextView socialSentenceView;
    
    public LikeView(final Context context) {
        super(context);
        this.likeViewStyle = Style.DEFAULT;
        this.horizontalAlignment = HorizontalAlignment.DEFAULT;
        this.auxiliaryViewPosition = AuxiliaryViewPosition.DEFAULT;
        this.foregroundColor = -1;
        this.initialize(context);
    }
    
    public LikeView(final Context context, final AttributeSet set) {
        super(context, set);
        this.likeViewStyle = Style.DEFAULT;
        this.horizontalAlignment = HorizontalAlignment.DEFAULT;
        this.auxiliaryViewPosition = AuxiliaryViewPosition.DEFAULT;
        this.foregroundColor = -1;
        this.parseAttributes(set);
        this.initialize(context);
    }
    
    private void associateWithLikeActionController(final LikeActionController likeActionController) {
        this.likeActionController = likeActionController;
        this.broadcastReceiver = new LikeControllerBroadcastReceiver();
        final LocalBroadcastManager instance = LocalBroadcastManager.getInstance(this.getContext());
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.facebook.sdk.LikeActionController.UPDATED");
        intentFilter.addAction("com.facebook.sdk.LikeActionController.DID_ERROR");
        intentFilter.addAction("com.facebook.sdk.LikeActionController.DID_RESET");
        instance.registerReceiver(this.broadcastReceiver, intentFilter);
    }
    
    private Bundle getAnalyticsParameters() {
        final Bundle bundle = new Bundle();
        bundle.putString("style", this.likeViewStyle.toString());
        bundle.putString("auxiliary_position", this.auxiliaryViewPosition.toString());
        bundle.putString("horizontal_alignment", this.horizontalAlignment.toString());
        bundle.putString("object_id", Utility.coerceValueIfNullOrEmpty(this.objectId, ""));
        return bundle;
    }
    
    public static boolean handleOnActivityResult(final Context context, final int n, final int n2, final Intent intent) {
        return LikeActionController.handleOnActivityResult(context, n, n2, intent);
    }
    
    private void initialize(final Context context) {
        this.edgePadding = this.getResources().getDimensionPixelSize(R.dimen.com_facebook_likeview_edge_padding);
        this.internalPadding = this.getResources().getDimensionPixelSize(R.dimen.com_facebook_likeview_internal_padding);
        if (this.foregroundColor == -1) {
            this.foregroundColor = this.getResources().getColor(R.color.com_facebook_likeview_text_color);
        }
        this.setBackgroundColor(0);
        (this.containerView = new LinearLayout(context)).setLayoutParams((ViewGroup$LayoutParams)new FrameLayout$LayoutParams(-2, -2));
        this.initializeLikeButton(context);
        this.initializeSocialSentenceView(context);
        this.initializeLikeCountView(context);
        this.containerView.addView((View)this.likeButton);
        this.containerView.addView((View)this.socialSentenceView);
        this.containerView.addView((View)this.likeBoxCountView);
        this.addView((View)this.containerView);
        this.setObjectIdForced(this.objectId);
        this.updateLikeStateAndLayout();
    }
    
    private void initializeLikeButton(final Context context) {
        (this.likeButton = new LikeButton(context, this.likeActionController != null && this.likeActionController.isObjectLiked())).setOnClickListener((View$OnClickListener)new View$OnClickListener() {
            public void onClick(final View view) {
                LikeView.this.toggleLike();
            }
        });
        this.likeButton.setLayoutParams((ViewGroup$LayoutParams)new LinearLayout$LayoutParams(-2, -2));
    }
    
    private void initializeLikeCountView(final Context context) {
        (this.likeBoxCountView = new LikeBoxCountView(context)).setLayoutParams((ViewGroup$LayoutParams)new LinearLayout$LayoutParams(-1, -1));
    }
    
    private void initializeSocialSentenceView(final Context context) {
        (this.socialSentenceView = new TextView(context)).setTextSize(0, this.getResources().getDimension(R.dimen.com_facebook_likeview_text_size));
        this.socialSentenceView.setMaxLines(2);
        this.socialSentenceView.setTextColor(this.foregroundColor);
        this.socialSentenceView.setGravity(17);
        this.socialSentenceView.setLayoutParams((ViewGroup$LayoutParams)new LinearLayout$LayoutParams(-2, -1));
    }
    
    private void parseAttributes(final AttributeSet set) {
        if (set != null && this.getContext() != null) {
            final TypedArray obtainStyledAttributes = this.getContext().obtainStyledAttributes(set, R.styleable.com_facebook_like_view);
            if (obtainStyledAttributes != null) {
                this.objectId = Utility.coerceValueIfNullOrEmpty(obtainStyledAttributes.getString(R.styleable.com_facebook_like_view_object_id), null);
                this.likeViewStyle = Style.fromInt(obtainStyledAttributes.getInt(R.styleable.com_facebook_like_view_style, Style.DEFAULT.getValue()));
                if (this.likeViewStyle == null) {
                    throw new IllegalArgumentException("Unsupported value for LikeView 'style'");
                }
                this.auxiliaryViewPosition = AuxiliaryViewPosition.fromInt(obtainStyledAttributes.getInt(R.styleable.com_facebook_like_view_auxiliary_view_position, AuxiliaryViewPosition.DEFAULT.getValue()));
                if (this.auxiliaryViewPosition == null) {
                    throw new IllegalArgumentException("Unsupported value for LikeView 'auxiliary_view_position'");
                }
                this.horizontalAlignment = HorizontalAlignment.fromInt(obtainStyledAttributes.getInt(R.styleable.com_facebook_like_view_horizontal_alignment, HorizontalAlignment.DEFAULT.getValue()));
                if (this.horizontalAlignment == null) {
                    throw new IllegalArgumentException("Unsupported value for LikeView 'horizontal_alignment'");
                }
                this.foregroundColor = obtainStyledAttributes.getColor(R.styleable.com_facebook_like_view_foreground_color, -1);
                obtainStyledAttributes.recycle();
            }
        }
    }
    
    private void setObjectIdForced(final String objectId) {
        this.tearDownObjectAssociations();
        this.objectId = objectId;
        if (Utility.isNullOrEmpty(objectId)) {
            return;
        }
        this.creationCallback = new LikeActionControllerCreationCallback();
        LikeActionController.getControllerForObjectId(this.getContext(), objectId, (LikeActionController.CreationCallback)this.creationCallback);
    }
    
    private void tearDownObjectAssociations() {
        if (this.broadcastReceiver != null) {
            LocalBroadcastManager.getInstance(this.getContext()).unregisterReceiver(this.broadcastReceiver);
            this.broadcastReceiver = null;
        }
        if (this.creationCallback != null) {
            this.creationCallback.cancel();
            this.creationCallback = null;
        }
        this.likeActionController = null;
    }
    
    private void toggleLike() {
        if (this.likeActionController != null) {
            final Context context = this.getContext();
            final Activity activity = null;
            Activity activity2;
            if (context instanceof Activity) {
                activity2 = (Activity)context;
            }
            else {
                activity2 = activity;
                if (context instanceof ContextWrapper) {
                    final Context baseContext = ((ContextWrapper)context).getBaseContext();
                    activity2 = activity;
                    if (baseContext instanceof Activity) {
                        activity2 = (Activity)baseContext;
                    }
                }
            }
            if (activity2 != null) {
                this.likeActionController.toggleLike(activity2, this.getAnalyticsParameters());
            }
        }
    }
    
    private void updateBoxCountCaretPosition() {
        switch (this.auxiliaryViewPosition) {
            default: {}
            case TOP: {
                this.likeBoxCountView.setCaretPosition(LikeBoxCountView.LikeBoxCountViewCaretPosition.BOTTOM);
            }
            case BOTTOM: {
                this.likeBoxCountView.setCaretPosition(LikeBoxCountView.LikeBoxCountViewCaretPosition.TOP);
            }
            case INLINE: {
                final LikeBoxCountView likeBoxCountView = this.likeBoxCountView;
                LikeBoxCountView.LikeBoxCountViewCaretPosition caretPosition;
                if (this.horizontalAlignment == HorizontalAlignment.RIGHT) {
                    caretPosition = LikeBoxCountView.LikeBoxCountViewCaretPosition.RIGHT;
                }
                else {
                    caretPosition = LikeBoxCountView.LikeBoxCountViewCaretPosition.LEFT;
                }
                likeBoxCountView.setCaretPosition(caretPosition);
            }
        }
    }
    
    private void updateLayout() {
        final boolean b = true;
        final FrameLayout$LayoutParams frameLayout$LayoutParams = (FrameLayout$LayoutParams)this.containerView.getLayoutParams();
        final LinearLayout$LayoutParams linearLayout$LayoutParams = (LinearLayout$LayoutParams)this.likeButton.getLayoutParams();
        int n;
        if (this.horizontalAlignment == HorizontalAlignment.LEFT) {
            n = 3;
        }
        else if (this.horizontalAlignment == HorizontalAlignment.CENTER) {
            n = 1;
        }
        else {
            n = 5;
        }
        frameLayout$LayoutParams.gravity = (n | 0x30);
        linearLayout$LayoutParams.gravity = n;
        this.socialSentenceView.setVisibility(8);
        this.likeBoxCountView.setVisibility(8);
        Object o;
        if (this.likeViewStyle == Style.STANDARD && this.likeActionController != null && !Utility.isNullOrEmpty(this.likeActionController.getSocialSentence())) {
            o = this.socialSentenceView;
        }
        else {
            if (this.likeViewStyle != Style.BOX_COUNT || this.likeActionController == null || Utility.isNullOrEmpty(this.likeActionController.getLikeCountString())) {
                return;
            }
            this.updateBoxCountCaretPosition();
            o = this.likeBoxCountView;
        }
        ((View)o).setVisibility(0);
        ((LinearLayout$LayoutParams)((View)o).getLayoutParams()).gravity = n;
        final LinearLayout containerView = this.containerView;
        int orientation = b ? 1 : 0;
        if (this.auxiliaryViewPosition == AuxiliaryViewPosition.INLINE) {
            orientation = 0;
        }
        containerView.setOrientation(orientation);
        if (this.auxiliaryViewPosition == AuxiliaryViewPosition.TOP || (this.auxiliaryViewPosition == AuxiliaryViewPosition.INLINE && this.horizontalAlignment == HorizontalAlignment.RIGHT)) {
            this.containerView.removeView((View)this.likeButton);
            this.containerView.addView((View)this.likeButton);
        }
        else {
            this.containerView.removeView((View)o);
            this.containerView.addView((View)o);
        }
        switch (this.auxiliaryViewPosition) {
            default: {}
            case TOP: {
                ((View)o).setPadding(this.edgePadding, this.edgePadding, this.edgePadding, this.internalPadding);
            }
            case BOTTOM: {
                ((View)o).setPadding(this.edgePadding, this.internalPadding, this.edgePadding, this.edgePadding);
            }
            case INLINE: {
                if (this.horizontalAlignment == HorizontalAlignment.RIGHT) {
                    ((View)o).setPadding(this.edgePadding, this.edgePadding, this.internalPadding, this.edgePadding);
                    return;
                }
                ((View)o).setPadding(this.internalPadding, this.edgePadding, this.edgePadding, this.edgePadding);
            }
        }
    }
    
    private void updateLikeStateAndLayout() {
        if (this.likeActionController == null) {
            this.likeButton.setLikeState(false);
            this.socialSentenceView.setText((CharSequence)null);
            this.likeBoxCountView.setText(null);
        }
        else {
            this.likeButton.setLikeState(this.likeActionController.isObjectLiked());
            this.socialSentenceView.setText((CharSequence)this.likeActionController.getSocialSentence());
            this.likeBoxCountView.setText(this.likeActionController.getLikeCountString());
        }
        this.updateLayout();
    }
    
    public OnErrorListener getOnErrorListener() {
        return this.onErrorListener;
    }
    
    protected void onDetachedFromWindow() {
        this.setObjectId(null);
        super.onDetachedFromWindow();
    }
    
    public void setAuxiliaryViewPosition(AuxiliaryViewPosition default1) {
        if (default1 == null) {
            default1 = AuxiliaryViewPosition.DEFAULT;
        }
        if (this.auxiliaryViewPosition != default1) {
            this.auxiliaryViewPosition = default1;
            this.updateLayout();
        }
    }
    
    public void setForegroundColor(final int textColor) {
        if (this.foregroundColor != textColor) {
            this.socialSentenceView.setTextColor(textColor);
        }
    }
    
    public void setHorizontalAlignment(HorizontalAlignment default1) {
        if (default1 == null) {
            default1 = HorizontalAlignment.DEFAULT;
        }
        if (this.horizontalAlignment != default1) {
            this.horizontalAlignment = default1;
            this.updateLayout();
        }
    }
    
    public void setLikeViewStyle(Style default1) {
        if (default1 == null) {
            default1 = Style.DEFAULT;
        }
        if (this.likeViewStyle != default1) {
            this.likeViewStyle = default1;
            this.updateLayout();
        }
    }
    
    public void setObjectId(String coerceValueIfNullOrEmpty) {
        coerceValueIfNullOrEmpty = Utility.coerceValueIfNullOrEmpty(coerceValueIfNullOrEmpty, null);
        if (!Utility.areObjectsEqual(coerceValueIfNullOrEmpty, this.objectId)) {
            this.setObjectIdForced(coerceValueIfNullOrEmpty);
            this.updateLikeStateAndLayout();
        }
    }
    
    public void setOnErrorListener(final OnErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
    }
    
    public enum AuxiliaryViewPosition
    {
        BOTTOM("bottom", 0);
        
        static AuxiliaryViewPosition DEFAULT;
        
        INLINE("inline", 1), 
        TOP("top", 2);
        
        private int intValue;
        private String stringValue;
        
        static {
            AuxiliaryViewPosition.DEFAULT = AuxiliaryViewPosition.BOTTOM;
        }
        
        private AuxiliaryViewPosition(final String stringValue, final int intValue) {
            this.stringValue = stringValue;
            this.intValue = intValue;
        }
        
        static AuxiliaryViewPosition fromInt(final int n) {
            final AuxiliaryViewPosition[] values = values();
            for (int length = values.length, i = 0; i < length; ++i) {
                final AuxiliaryViewPosition auxiliaryViewPosition = values[i];
                if (auxiliaryViewPosition.getValue() == n) {
                    return auxiliaryViewPosition;
                }
            }
            return null;
        }
        
        private int getValue() {
            return this.intValue;
        }
        
        @Override
        public String toString() {
            return this.stringValue;
        }
    }
    
    public enum HorizontalAlignment
    {
        CENTER("center", 0);
        
        static HorizontalAlignment DEFAULT;
        
        LEFT("left", 1), 
        RIGHT("right", 2);
        
        private int intValue;
        private String stringValue;
        
        static {
            HorizontalAlignment.DEFAULT = HorizontalAlignment.CENTER;
        }
        
        private HorizontalAlignment(final String stringValue, final int intValue) {
            this.stringValue = stringValue;
            this.intValue = intValue;
        }
        
        static HorizontalAlignment fromInt(final int n) {
            final HorizontalAlignment[] values = values();
            for (int length = values.length, i = 0; i < length; ++i) {
                final HorizontalAlignment horizontalAlignment = values[i];
                if (horizontalAlignment.getValue() == n) {
                    return horizontalAlignment;
                }
            }
            return null;
        }
        
        private int getValue() {
            return this.intValue;
        }
        
        @Override
        public String toString() {
            return this.stringValue;
        }
    }
    
    private class LikeActionControllerCreationCallback implements CreationCallback
    {
        private boolean isCancelled;
        
        public void cancel() {
            this.isCancelled = true;
        }
        
        @Override
        public void onComplete(final LikeActionController likeActionController) {
            if (this.isCancelled) {
                return;
            }
            LikeView.this.associateWithLikeActionController(likeActionController);
            LikeView.this.updateLikeStateAndLayout();
            LikeView.this.creationCallback = null;
        }
    }
    
    private class LikeControllerBroadcastReceiver extends BroadcastReceiver
    {
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            final Bundle extras = intent.getExtras();
            int n = 1;
            if (extras != null) {
                final String string = extras.getString("com.facebook.sdk.LikeActionController.OBJECT_ID");
                if (Utility.isNullOrEmpty(string) || Utility.areObjectsEqual(LikeView.this.objectId, string)) {
                    n = 1;
                }
                else {
                    n = 0;
                }
            }
            if (n != 0) {
                if ("com.facebook.sdk.LikeActionController.UPDATED".equals(action)) {
                    LikeView.this.updateLikeStateAndLayout();
                    return;
                }
                if ("com.facebook.sdk.LikeActionController.DID_ERROR".equals(action)) {
                    if (LikeView.this.onErrorListener != null) {
                        LikeView.this.onErrorListener.onError(extras);
                    }
                }
                else if ("com.facebook.sdk.LikeActionController.DID_RESET".equals(action)) {
                    LikeView.this.setObjectIdForced(LikeView.this.objectId);
                    LikeView.this.updateLikeStateAndLayout();
                }
            }
        }
    }
    
    public interface OnErrorListener
    {
        void onError(final Bundle p0);
    }
    
    public enum Style
    {
        BOX_COUNT("box_count", 2), 
        BUTTON("button", 1);
        
        static Style DEFAULT;
        
        STANDARD("standard", 0);
        
        private int intValue;
        private String stringValue;
        
        static {
            Style.DEFAULT = Style.STANDARD;
        }
        
        private Style(final String stringValue, final int intValue) {
            this.stringValue = stringValue;
            this.intValue = intValue;
        }
        
        static Style fromInt(final int n) {
            final Style[] values = values();
            for (int length = values.length, i = 0; i < length; ++i) {
                final Style style = values[i];
                if (style.getValue() == n) {
                    return style;
                }
            }
            return null;
        }
        
        private int getValue() {
            return this.intValue;
        }
        
        @Override
        public String toString() {
            return this.stringValue;
        }
    }
}
