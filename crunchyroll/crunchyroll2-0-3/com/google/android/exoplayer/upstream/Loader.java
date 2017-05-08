// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.exoplayer.upstream;

import android.util.Log;
import com.google.android.exoplayer.util.TraceUtil;
import android.os.Message;
import android.annotation.SuppressLint;
import android.os.Handler;
import java.io.IOException;
import android.os.Looper;
import com.google.android.exoplayer.util.Assertions;
import com.google.android.exoplayer.util.Util;
import java.util.concurrent.ExecutorService;

public final class Loader
{
    private LoadTask currentTask;
    private final ExecutorService downloadExecutorService;
    private boolean loading;
    
    public Loader(final String s) {
        this.downloadExecutorService = Util.newSingleThreadExecutor(s);
    }
    
    public void cancelLoading() {
        Assertions.checkState(this.loading);
        this.currentTask.quit();
    }
    
    public boolean isLoading() {
        return this.loading;
    }
    
    public void release() {
        if (this.loading) {
            this.cancelLoading();
        }
        this.downloadExecutorService.shutdown();
    }
    
    public void startLoading(final Looper looper, final Loadable loadable, final Callback callback) {
        Assertions.checkState(!this.loading);
        this.loading = true;
        this.currentTask = new LoadTask(looper, loadable, callback);
        this.downloadExecutorService.submit(this.currentTask);
    }
    
    public void startLoading(final Loadable loadable, final Callback callback) {
        final Looper myLooper = Looper.myLooper();
        Assertions.checkState(myLooper != null);
        this.startLoading(myLooper, loadable, callback);
    }
    
    public interface Callback
    {
        void onLoadCanceled(final Loadable p0);
        
        void onLoadCompleted(final Loadable p0);
        
        void onLoadError(final Loadable p0, final IOException p1);
    }
    
    @SuppressLint({ "HandlerLeak" })
    private final class LoadTask extends Handler implements Runnable
    {
        private final Callback callback;
        private volatile Thread executorThread;
        private final Loadable loadable;
        
        public LoadTask(final Looper looper, final Loadable loadable, final Callback callback) {
            super(looper);
            this.loadable = loadable;
            this.callback = callback;
        }
        
        private void onFinished() {
            Loader.this.loading = false;
            Loader.this.currentTask = null;
        }
        
        public void handleMessage(final Message message) {
            if (message.what == 2) {
                throw (Error)message.obj;
            }
            this.onFinished();
            if (this.loadable.isLoadCanceled()) {
                this.callback.onLoadCanceled(this.loadable);
                return;
            }
            switch (message.what) {
                default: {}
                case 0: {
                    this.callback.onLoadCompleted(this.loadable);
                }
                case 1: {
                    this.callback.onLoadError(this.loadable, (IOException)message.obj);
                }
            }
        }
        
        public void quit() {
            this.loadable.cancelLoad();
            if (this.executorThread != null) {
                this.executorThread.interrupt();
            }
        }
        
        public void run() {
            try {
                this.executorThread = Thread.currentThread();
                if (!this.loadable.isLoadCanceled()) {
                    TraceUtil.beginSection(this.loadable.getClass().getSimpleName() + ".load()");
                    this.loadable.load();
                    TraceUtil.endSection();
                }
                this.sendEmptyMessage(0);
            }
            catch (IOException ex) {
                this.obtainMessage(1, (Object)ex).sendToTarget();
            }
            catch (InterruptedException ex3) {
                Assertions.checkState(this.loadable.isLoadCanceled());
                this.sendEmptyMessage(0);
            }
            catch (Exception ex2) {
                Log.e("LoadTask", "Unexpected exception loading stream", (Throwable)ex2);
                this.obtainMessage(1, (Object)new UnexpectedLoaderException(ex2)).sendToTarget();
            }
            catch (Error error) {
                Log.e("LoadTask", "Unexpected error loading stream", (Throwable)error);
                this.obtainMessage(2, (Object)error).sendToTarget();
                throw error;
            }
        }
    }
    
    public interface Loadable
    {
        void cancelLoad();
        
        boolean isLoadCanceled();
        
        void load() throws IOException, InterruptedException;
    }
    
    public static final class UnexpectedLoaderException extends IOException
    {
        public UnexpectedLoaderException(final Exception ex) {
            super("Unexpected " + ex.getClass().getSimpleName() + ": " + ex.getMessage(), ex);
        }
    }
}
