// 
// Decompiled by Procyon v0.5.30
// 

package com.nostra13.universalimageloader.core;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.utils.L;
import android.os.Handler;
import android.graphics.Bitmap;

final class ProcessAndDisplayImageTask implements Runnable
{
    private static final String LOG_POSTPROCESS_IMAGE = "PostProcess image before displaying [%s]";
    private final Bitmap bitmap;
    private final ImageLoaderEngine engine;
    private final Handler handler;
    private final ImageLoadingInfo imageLoadingInfo;
    
    public ProcessAndDisplayImageTask(final ImageLoaderEngine engine, final Bitmap bitmap, final ImageLoadingInfo imageLoadingInfo, final Handler handler) {
        this.engine = engine;
        this.bitmap = bitmap;
        this.imageLoadingInfo = imageLoadingInfo;
        this.handler = handler;
    }
    
    @Override
    public void run() {
        L.d("PostProcess image before displaying [%s]", this.imageLoadingInfo.memoryCacheKey);
        LoadAndDisplayImageTask.runTask(new DisplayBitmapTask(this.imageLoadingInfo.options.getPostProcessor().process(this.bitmap), this.imageLoadingInfo, this.engine, LoadedFrom.MEMORY_CACHE), this.imageLoadingInfo.options.isSyncLoading(), this.handler, this.engine);
    }
}
