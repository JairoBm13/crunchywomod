// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.api;

import android.util.Pair;
import android.os.Message;
import android.os.Handler;
import com.google.android.gms.common.internal.zzu;
import android.util.Log;
import java.util.Iterator;
import android.os.Looper;
import java.util.concurrent.CountDownLatch;
import com.google.android.gms.common.internal.ICancelToken;
import java.util.ArrayList;

public abstract class AbstractPendingResult<R extends Result> implements PendingResult<R>
{
    protected final CallbackHandler<R> mHandler;
    private boolean zzL;
    private final Object zzWb;
    private final ArrayList<BatchCallback> zzWc;
    private ResultCallback<R> zzWd;
    private volatile R zzWe;
    private volatile boolean zzWf;
    private boolean zzWg;
    private ICancelToken zzWh;
    private final CountDownLatch zzoD;
    
    protected AbstractPendingResult(final Looper looper) {
        this.zzWb = new Object();
        this.zzoD = new CountDownLatch(1);
        this.zzWc = new ArrayList<BatchCallback>();
        this.mHandler = new CallbackHandler<R>(looper);
    }
    
    private void zza(final R zzWe) {
        this.zzWe = zzWe;
        this.zzWh = null;
        this.zzoD.countDown();
        final Status status = this.zzWe.getStatus();
        if (this.zzWd != null) {
            this.mHandler.removeTimeoutMessages();
            if (!this.zzL) {
                this.mHandler.sendResultCallback(this.zzWd, this.zzmo());
            }
        }
        final Iterator<BatchCallback> iterator = this.zzWc.iterator();
        while (iterator.hasNext()) {
            iterator.next().zzs(status);
        }
        this.zzWc.clear();
    }
    
    static void zzb(final Result result) {
        if (!(result instanceof Releasable)) {
            return;
        }
        try {
            ((Releasable)result).release();
        }
        catch (RuntimeException ex) {
            Log.w("AbstractPendingResult", "Unable to release " + result, (Throwable)ex);
        }
    }
    
    private R zzmo() {
        boolean b = true;
        synchronized (this.zzWb) {
            if (this.zzWf) {
                b = false;
            }
            zzu.zza(b, (Object)"Result has already been consumed.");
            zzu.zza(this.isReady(), (Object)"Result is not ready.");
            final Result zzWe = this.zzWe;
            this.zzWe = null;
            this.zzWd = null;
            this.zzWf = true;
            // monitorexit(this.zzWb)
            this.onResultConsumed();
            return (R)zzWe;
        }
    }
    
    public void cancel() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: aload_0        
        //     1: getfield        com/google/android/gms/common/api/AbstractPendingResult.zzWb:Ljava/lang/Object;
        //     4: astore_1       
        //     5: aload_1        
        //     6: monitorenter   
        //     7: aload_0        
        //     8: getfield        com/google/android/gms/common/api/AbstractPendingResult.zzL:Z
        //    11: ifne            21
        //    14: aload_0        
        //    15: getfield        com/google/android/gms/common/api/AbstractPendingResult.zzWf:Z
        //    18: ifeq            24
        //    21: aload_1        
        //    22: monitorexit    
        //    23: return         
        //    24: aload_0        
        //    25: getfield        com/google/android/gms/common/api/AbstractPendingResult.zzWh:Lcom/google/android/gms/common/internal/ICancelToken;
        //    28: astore_2       
        //    29: aload_2        
        //    30: ifnull          42
        //    33: aload_0        
        //    34: getfield        com/google/android/gms/common/api/AbstractPendingResult.zzWh:Lcom/google/android/gms/common/internal/ICancelToken;
        //    37: invokeinterface com/google/android/gms/common/internal/ICancelToken.cancel:()V
        //    42: aload_0        
        //    43: getfield        com/google/android/gms/common/api/AbstractPendingResult.zzWe:Lcom/google/android/gms/common/api/Result;
        //    46: invokestatic    com/google/android/gms/common/api/AbstractPendingResult.zzb:(Lcom/google/android/gms/common/api/Result;)V
        //    49: aload_0        
        //    50: aconst_null    
        //    51: putfield        com/google/android/gms/common/api/AbstractPendingResult.zzWd:Lcom/google/android/gms/common/api/ResultCallback;
        //    54: aload_0        
        //    55: iconst_1       
        //    56: putfield        com/google/android/gms/common/api/AbstractPendingResult.zzL:Z
        //    59: aload_0        
        //    60: aload_0        
        //    61: getstatic       com/google/android/gms/common/api/Status.zzXT:Lcom/google/android/gms/common/api/Status;
        //    64: invokevirtual   com/google/android/gms/common/api/AbstractPendingResult.createFailedResult:(Lcom/google/android/gms/common/api/Status;)Lcom/google/android/gms/common/api/Result;
        //    67: invokespecial   com/google/android/gms/common/api/AbstractPendingResult.zza:(Lcom/google/android/gms/common/api/Result;)V
        //    70: aload_1        
        //    71: monitorexit    
        //    72: return         
        //    73: astore_2       
        //    74: aload_1        
        //    75: monitorexit    
        //    76: aload_2        
        //    77: athrow         
        //    78: astore_2       
        //    79: goto            42
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                        
        //  -----  -----  -----  -----  ----------------------------
        //  7      21     73     78     Any
        //  21     23     73     78     Any
        //  24     29     73     78     Any
        //  33     42     78     82     Landroid/os/RemoteException;
        //  33     42     73     78     Any
        //  42     72     73     78     Any
        //  74     76     73     78     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0042:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:317)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:238)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:123)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    protected abstract R createFailedResult(final Status p0);
    
    public final void forceFailureUnlessReady(final Status status) {
        synchronized (this.zzWb) {
            if (!this.isReady()) {
                this.setResult(this.createFailedResult(status));
                this.zzWg = true;
            }
        }
    }
    
    public boolean isCanceled() {
        synchronized (this.zzWb) {
            return this.zzL;
        }
    }
    
    public final boolean isReady() {
        return this.zzoD.getCount() == 0L;
    }
    
    protected void onResultConsumed() {
    }
    
    public final void setResult(final R r) {
    Label_0057_Outer:
        while (true) {
            final boolean b = true;
            while (true) {
            Label_0083:
                while (true) {
                    synchronized (this.zzWb) {
                        if (this.zzWg || this.zzL) {
                            zzb(r);
                            return;
                        }
                        if (!this.isReady()) {
                            final boolean b2 = true;
                            zzu.zza(b2, (Object)"Results have already been set");
                            if (!this.zzWf) {
                                final boolean b3 = b;
                                zzu.zza(b3, (Object)"Result has already been consumed");
                                this.zza(r);
                                return;
                            }
                            break Label_0083;
                        }
                    }
                    final boolean b2 = false;
                    continue Label_0057_Outer;
                }
                final boolean b3 = false;
                continue;
            }
        }
    }
    
    @Override
    public final void setResultCallback(final ResultCallback<R> resultCallback) {
        while (true) {
            zzu.zza(!this.zzWf, (Object)"Result has already been consumed.");
            synchronized (this.zzWb) {
                if (this.isCanceled()) {
                    return;
                }
                if (this.isReady()) {
                    this.mHandler.sendResultCallback(resultCallback, this.zzmo());
                    return;
                }
            }
            final ResultCallback<R> zzWd;
            this.zzWd = zzWd;
        }
    }
    
    public static class CallbackHandler<R extends Result> extends Handler
    {
        public CallbackHandler() {
            this(Looper.getMainLooper());
        }
        
        public CallbackHandler(final Looper looper) {
            super(looper);
        }
        
        protected void deliverResultCallback(final ResultCallback<R> resultCallback, final R r) {
            try {
                resultCallback.onResult(r);
            }
            catch (RuntimeException ex) {
                AbstractPendingResult.zzb(r);
                throw ex;
            }
        }
        
        public void handleMessage(final Message message) {
            switch (message.what) {
                default: {
                    Log.wtf("AbstractPendingResult", "Don't know how to handle this message.");
                }
                case 1: {
                    final Pair pair = (Pair)message.obj;
                    this.deliverResultCallback((ResultCallback<Result>)pair.first, (Result)pair.second);
                }
                case 2: {
                    ((AbstractPendingResult)message.obj).forceFailureUnlessReady(Status.zzXS);
                }
            }
        }
        
        public void removeTimeoutMessages() {
            this.removeMessages(2);
        }
        
        public void sendResultCallback(final ResultCallback<R> resultCallback, final R r) {
            this.sendMessage(this.obtainMessage(1, (Object)new Pair((Object)resultCallback, (Object)r)));
        }
    }
}
