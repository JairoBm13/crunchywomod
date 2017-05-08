// 
// Decompiled by Procyon v0.5.30
// 

package android.support.design.widget;

import java.lang.ref.WeakReference;
import android.os.Message;
import android.os.Handler$Callback;
import android.os.Looper;
import android.os.Handler;

class SnackbarManager
{
    private static final int LONG_DURATION_MS = 2750;
    private static final int MSG_TIMEOUT = 0;
    private static final int SHORT_DURATION_MS = 1500;
    private static SnackbarManager sSnackbarManager;
    private SnackbarRecord mCurrentSnackbar;
    private final Handler mHandler;
    private final Object mLock;
    private SnackbarRecord mNextSnackbar;
    
    private SnackbarManager() {
        this.mLock = new Object();
        this.mHandler = new Handler(Looper.getMainLooper(), (Handler$Callback)new Handler$Callback() {
            public boolean handleMessage(final Message message) {
                switch (message.what) {
                    default: {
                        return false;
                    }
                    case 0: {
                        SnackbarManager.this.handleTimeout((SnackbarRecord)message.obj);
                        return true;
                    }
                }
            }
        });
    }
    
    private boolean cancelSnackbarLocked(final SnackbarRecord snackbarRecord) {
        final Callback callback = (Callback)snackbarRecord.callback.get();
        if (callback != null) {
            callback.dismiss();
            return true;
        }
        return false;
    }
    
    static SnackbarManager getInstance() {
        if (SnackbarManager.sSnackbarManager == null) {
            SnackbarManager.sSnackbarManager = new SnackbarManager();
        }
        return SnackbarManager.sSnackbarManager;
    }
    
    private void handleTimeout(final SnackbarRecord snackbarRecord) {
        synchronized (this.mLock) {
            if (this.mCurrentSnackbar == snackbarRecord || this.mNextSnackbar == snackbarRecord) {
                this.cancelSnackbarLocked(snackbarRecord);
            }
        }
    }
    
    private boolean isCurrentSnackbar(final Callback callback) {
        return this.mCurrentSnackbar != null && this.mCurrentSnackbar.isSnackbar(callback);
    }
    
    private boolean isNextSnackbar(final Callback callback) {
        return this.mNextSnackbar != null && this.mNextSnackbar.isSnackbar(callback);
    }
    
    private void scheduleTimeoutLocked(final SnackbarRecord snackbarRecord) {
        this.mHandler.removeCallbacksAndMessages((Object)snackbarRecord);
        final Handler mHandler = this.mHandler;
        final Message obtain = Message.obtain(this.mHandler, 0, (Object)snackbarRecord);
        long n;
        if (snackbarRecord.duration == 0) {
            n = 2750L;
        }
        else {
            n = 1500L;
        }
        mHandler.sendMessageDelayed(obtain, n);
    }
    
    private void showNextSnackbarLocked() {
        if (this.mNextSnackbar != null) {
            this.mCurrentSnackbar = this.mNextSnackbar;
            this.mNextSnackbar = null;
            final Callback callback = (Callback)this.mCurrentSnackbar.callback.get();
            if (callback == null) {
                this.mCurrentSnackbar = null;
                return;
            }
            callback.show();
        }
    }
    
    public void cancelTimeout(final Callback callback) {
        synchronized (this.mLock) {
            if (this.isCurrentSnackbar(callback)) {
                this.mHandler.removeCallbacksAndMessages((Object)this.mCurrentSnackbar);
            }
        }
    }
    
    public void dismiss(final Callback callback) {
        synchronized (this.mLock) {
            if (this.isCurrentSnackbar(callback)) {
                this.cancelSnackbarLocked(this.mCurrentSnackbar);
            }
            if (this.isNextSnackbar(callback)) {
                this.cancelSnackbarLocked(this.mNextSnackbar);
            }
        }
    }
    
    public void onDismissed(final Callback callback) {
        synchronized (this.mLock) {
            if (this.isCurrentSnackbar(callback)) {
                this.mCurrentSnackbar = null;
                if (this.mNextSnackbar != null) {
                    this.showNextSnackbarLocked();
                }
            }
        }
    }
    
    public void onShown(final Callback callback) {
        synchronized (this.mLock) {
            if (this.isCurrentSnackbar(callback)) {
                this.scheduleTimeoutLocked(this.mCurrentSnackbar);
            }
        }
    }
    
    public void restoreTimeout(final Callback callback) {
        synchronized (this.mLock) {
            if (this.isCurrentSnackbar(callback)) {
                this.scheduleTimeoutLocked(this.mCurrentSnackbar);
            }
        }
    }
    
    public void show(final int n, final Callback callback) {
        while (true) {
            while (true) {
                synchronized (this.mLock) {
                    if (this.isCurrentSnackbar(callback)) {
                        this.mCurrentSnackbar.duration = n;
                        this.mHandler.removeCallbacksAndMessages((Object)this.mCurrentSnackbar);
                        this.scheduleTimeoutLocked(this.mCurrentSnackbar);
                        return;
                    }
                    if (this.isNextSnackbar(callback)) {
                        this.mNextSnackbar.duration = n;
                        if (this.mCurrentSnackbar != null && this.cancelSnackbarLocked(this.mCurrentSnackbar)) {
                            return;
                        }
                        break;
                    }
                }
                final Callback callback2;
                this.mNextSnackbar = new SnackbarRecord(n, callback2);
                continue;
            }
        }
        this.mCurrentSnackbar = null;
        this.showNextSnackbarLocked();
    }
    // monitorexit(o)
    
    interface Callback
    {
        void dismiss();
        
        void show();
    }
    
    private static class SnackbarRecord
    {
        private final WeakReference<Callback> callback;
        private int duration;
        
        SnackbarRecord(final int duration, final Callback callback) {
            this.callback = new WeakReference<Callback>(callback);
            this.duration = duration;
        }
        
        boolean isSnackbar(final Callback callback) {
            return callback != null && this.callback.get() == callback;
        }
    }
}
