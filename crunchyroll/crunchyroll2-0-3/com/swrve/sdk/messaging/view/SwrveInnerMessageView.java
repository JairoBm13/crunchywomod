// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.messaging.view;

import android.graphics.Point;
import org.json.JSONException;
import org.json.JSONObject;
import com.swrve.sdk.messaging.SwrveWidget;
import android.view.MotionEvent;
import android.view.Display;
import android.util.Log;
import android.view.View;
import android.widget.ImageView$ScaleType;
import com.swrve.sdk.messaging.SwrveImage;
import android.view.ViewGroup$LayoutParams;
import android.widget.RelativeLayout$LayoutParams;
import android.view.WindowManager;
import java.util.HashSet;
import java.util.Iterator;
import com.swrve.sdk.messaging.SwrveActionType;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory$Options;
import com.swrve.sdk.messaging.SwrveMessage;
import android.content.Context;
import com.swrve.sdk.messaging.ISwrveInstallButtonListener;
import com.swrve.sdk.messaging.SwrveMessageFormat;
import com.swrve.sdk.messaging.ISwrveCustomButtonListener;
import android.graphics.Bitmap;
import java.lang.ref.WeakReference;
import java.util.Set;
import android.widget.RelativeLayout;

public class SwrveInnerMessageView extends RelativeLayout
{
    protected Set<WeakReference<Bitmap>> bitmapCache;
    protected ISwrveCustomButtonListener customButtonListener;
    protected SwrveMessageFormat format;
    protected ISwrveInstallButtonListener installButtonListener;
    protected int minSampleSize;
    protected SwrveMessageView parent;
    protected float scale;
    
    public SwrveInnerMessageView(final Context context, final SwrveMessageView parent, final SwrveMessage swrveMessage, final SwrveMessageFormat format, final ISwrveInstallButtonListener installButtonListener, final ISwrveCustomButtonListener customButtonListener, final int minSampleSize) throws SwrveMessageViewBuildException {
        super(context);
        this.minSampleSize = 1;
        this.parent = parent;
        this.format = format;
        this.installButtonListener = installButtonListener;
        this.customButtonListener = customButtonListener;
        if (minSampleSize > 0 && minSampleSize % 2 == 0) {
            this.minSampleSize = minSampleSize;
        }
        this.initializeLayout(context, swrveMessage, format);
    }
    
    private static int calculateInSampleSize(final BitmapFactory$Options bitmapFactory$Options, final int n, final int n2) {
        final int outHeight = bitmapFactory$Options.outHeight;
        final int outWidth = bitmapFactory$Options.outWidth;
        int n3 = 1;
        int n4 = 1;
        if (outHeight > n2 || outWidth > n) {
            final int n5 = outHeight / 2;
            final int n6 = outWidth / 2;
            while (true) {
                n3 = n4;
                if (n5 / n4 <= n2) {
                    break;
                }
                n3 = n4;
                if (n6 / n4 <= n) {
                    break;
                }
                n4 *= 2;
            }
        }
        return n3;
    }
    
    private static BitmapResult decodeSampledBitmapFromFile(final String s, final int n, final int n2, final int n3) {
        try {
            final BitmapFactory$Options bitmapFactory$Options = new BitmapFactory$Options();
            bitmapFactory$Options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(s, bitmapFactory$Options);
            final int outWidth = bitmapFactory$Options.outWidth;
            final int outHeight = bitmapFactory$Options.outHeight;
            bitmapFactory$Options.inSampleSize = Math.max(calculateInSampleSize(bitmapFactory$Options, n, n2), n3);
            bitmapFactory$Options.inJustDecodeBounds = false;
            return new BitmapResult(BitmapFactory.decodeFile(s, bitmapFactory$Options), outWidth, outHeight);
        }
        catch (OutOfMemoryError outOfMemoryError) {
            outOfMemoryError.printStackTrace();
        }
        catch (Exception ex) {
            ex.printStackTrace();
            goto Label_0083;
        }
    }
    
    private void dismiss() {
        this.parent.dismiss();
    }
    
    protected SwrveButtonView createSwrveButton(final Context context, final SwrveActionType swrveActionType) {
        return new SwrveButtonView(context, swrveActionType);
    }
    
    protected SwrveImageView createSwrveImage(final Context context) {
        return new SwrveImageView(context);
    }
    
    public void destroy() {
        try {
            if (this.bitmapCache != null) {
                final Iterator<WeakReference<Bitmap>> iterator = this.bitmapCache.iterator();
                while (iterator.hasNext()) {
                    final Bitmap bitmap = iterator.next().get();
                    if (bitmap != null) {
                        bitmap.recycle();
                    }
                }
                this.bitmapCache.clear();
                this.bitmapCache = null;
            }
            System.gc();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    protected void finalize() throws Throwable {
        super.finalize();
        this.destroy();
    }
    
    protected void initializeLayout(final Context context, final SwrveMessage swrveMessage, final SwrveMessageFormat swrveMessageFormat) throws SwrveMessageViewBuildException {
        try {
            this.bitmapCache = new HashSet<WeakReference<Bitmap>>();
            final Display defaultDisplay = ((WindowManager)context.getSystemService("window")).getDefaultDisplay();
            final int width = defaultDisplay.getWidth();
            final int height = defaultDisplay.getHeight();
            this.scale = swrveMessageFormat.getScale();
            this.setMinimumWidth(swrveMessageFormat.getSize().x);
            this.setMinimumHeight(swrveMessageFormat.getSize().y);
            this.setLayoutParams((ViewGroup$LayoutParams)new RelativeLayout$LayoutParams(-1, -1));
            for (final SwrveImage swrveImage : swrveMessageFormat.getImages()) {
                final BitmapResult decodeSampledBitmapFromFile = decodeSampledBitmapFromFile(swrveMessage.getCacheDir().getAbsolutePath() + "/" + swrveImage.getFile(), width, height, this.minSampleSize);
                if (decodeSampledBitmapFromFile == null || decodeSampledBitmapFromFile.getBitmap() == null) {
                    goto Label_0384;
                }
                final Bitmap bitmap = decodeSampledBitmapFromFile.getBitmap();
                final SwrveImageView swrveImage2 = this.createSwrveImage(context);
                this.bitmapCache.add(new WeakReference<Bitmap>(bitmap));
                final RelativeLayout$LayoutParams layoutParams = new RelativeLayout$LayoutParams(decodeSampledBitmapFromFile.getWidth(), decodeSampledBitmapFromFile.getHeight());
                layoutParams.leftMargin = swrveImage.getPosition().x;
                layoutParams.topMargin = swrveImage.getPosition().y;
                layoutParams.width = decodeSampledBitmapFromFile.getWidth();
                layoutParams.height = decodeSampledBitmapFromFile.getHeight();
                swrveImage2.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
                swrveImage2.setImageBitmap(bitmap);
                swrveImage2.setScaleType(ImageView$ScaleType.FIT_XY);
                this.addView((View)swrveImage2);
            }
            goto Label_0387;
        }
        catch (Exception ex) {
            Log.e("SwrveMessagingSDK", "Error while initializing SwrveMessageView layout", (Throwable)ex);
        }
        catch (OutOfMemoryError outOfMemoryError) {
            Log.e("SwrveMessagingSDK", "Error while initializing SwrveMessageView layout", (Throwable)outOfMemoryError);
            goto Label_0334;
        }
        goto Label_0334;
    }
    
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.destroy();
    }
    
    protected void onLayout(final boolean b, int n, int n2, int n3, int n4) {
        while (true) {
            while (true) {
                Label_0240: {
                    try {
                        final int childCount = this.getChildCount();
                        n3 = (int)(n + (n3 - n) / 2.0);
                        n2 += (int)((n4 - n2) / 2.0);
                        n = 0;
                        if (n < childCount) {
                            final View child = this.getChildAt(n);
                            if (child.getVisibility() == 8) {
                                break Label_0240;
                            }
                            final RelativeLayout$LayoutParams relativeLayout$LayoutParams = (RelativeLayout$LayoutParams)child.getLayoutParams();
                            n4 = relativeLayout$LayoutParams.width / 2;
                            final int n5 = relativeLayout$LayoutParams.height / 2;
                            if (this.scale != 1.0f) {
                                child.layout((int)(this.scale * (relativeLayout$LayoutParams.leftMargin - n4)) + n3, (int)(this.scale * (relativeLayout$LayoutParams.topMargin - n5)) + n2, (int)(this.scale * (relativeLayout$LayoutParams.leftMargin + n4)) + n3, (int)(this.scale * (relativeLayout$LayoutParams.topMargin + n5)) + n2);
                                break Label_0240;
                            }
                            child.layout(relativeLayout$LayoutParams.leftMargin - n4 + n3, relativeLayout$LayoutParams.topMargin - n5 + n2, relativeLayout$LayoutParams.leftMargin + n4 + n3, relativeLayout$LayoutParams.topMargin + n5 + n2);
                            break Label_0240;
                        }
                    }
                    catch (Exception ex) {
                        Log.e("SwrveMessagingSDK", "Error while onLayout in SwrveMessageView", (Throwable)ex);
                    }
                    break;
                }
                ++n;
                continue;
            }
        }
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        return true;
    }
    
    public void setCustomButtonListener(final ISwrveCustomButtonListener customButtonListener) {
        this.customButtonListener = customButtonListener;
    }
    
    public void setInstallButtonListener(final ISwrveInstallButtonListener installButtonListener) {
        this.installButtonListener = installButtonListener;
    }
    
    private static class BitmapResult
    {
        private Bitmap bitmap;
        private int height;
        private int width;
        
        public BitmapResult(final Bitmap bitmap, final int width, final int height) {
            this.bitmap = bitmap;
            this.width = width;
            this.height = height;
        }
        
        public Bitmap getBitmap() {
            return this.bitmap;
        }
        
        public int getHeight() {
            return this.height;
        }
        
        public int getWidth() {
            return this.width;
        }
    }
}
