// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.util;

import java.util.concurrent.CountDownLatch;
import java.io.InterruptedIOException;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import android.util.Log;
import java.util.concurrent.Executors;
import android.os.Handler;
import java.util.concurrent.FutureTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Callable;

public abstract class SafeAsyncTask<ResultT> implements Callable<ResultT>
{
    protected static final Executor DEFAULT_EXECUTOR;
    public static final int DEFAULT_POOL_SIZE = 25;
    protected Executor executor;
    protected FutureTask<Void> future;
    protected Handler handler;
    protected StackTraceElement[] launchLocation;
    
    static {
        DEFAULT_EXECUTOR = Executors.newFixedThreadPool(25);
    }
    
    public SafeAsyncTask() {
        this.executor = SafeAsyncTask.DEFAULT_EXECUTOR;
    }
    
    public SafeAsyncTask(final Handler handler) {
        this.handler = handler;
        this.executor = SafeAsyncTask.DEFAULT_EXECUTOR;
    }
    
    public SafeAsyncTask(final Handler handler, final Executor executor) {
        this.handler = handler;
        this.executor = executor;
    }
    
    public SafeAsyncTask(final Executor executor) {
        this.executor = executor;
    }
    
    public boolean cancel(final boolean b) {
        if (this.future == null) {
            throw new UnsupportedOperationException("You cannot cancel this task before calling future()");
        }
        return this.future.cancel(b);
    }
    
    public void execute() {
        this.execute(Thread.currentThread().getStackTrace());
    }
    
    protected void execute(final StackTraceElement[] launchLocation) {
        this.launchLocation = launchLocation;
        this.executor.execute(this.future());
    }
    
    public SafeAsyncTask<ResultT> executor(final Executor executor) {
        this.executor = executor;
        return this;
    }
    
    public Executor executor() {
        return this.executor;
    }
    
    public FutureTask<Void> future() {
        return this.future = new FutureTask<Void>(this.newTask());
    }
    
    public Handler handler() {
        return this.handler;
    }
    
    public SafeAsyncTask<ResultT> handler(final Handler handler) {
        this.handler = handler;
        return this;
    }
    
    protected boolean isCancelled() {
        return this.future.isCancelled();
    }
    
    protected Task<ResultT> newTask() {
        return new Task<ResultT>(this);
    }
    
    protected void onException(final Exception ex) throws RuntimeException {
        Log.e("roboguice", "Exception caught during background processing", (Throwable)ex);
    }
    
    protected void onFinally() throws RuntimeException {
    }
    
    protected void onInterrupted(final Exception ex) {
        this.onException(ex);
    }
    
    protected void onPreExecute() throws Exception {
    }
    
    protected void onSuccess(final ResultT resultT) throws Exception {
    }
    
    public static class Task<ResultT> implements Callable<Void>
    {
        protected Handler handler;
        protected SafeAsyncTask<ResultT> parent;
        
        public Task(final SafeAsyncTask parent) {
            this.parent = (SafeAsyncTask<ResultT>)parent;
            Handler handler;
            if (parent.handler != null) {
                handler = parent.handler;
            }
            else {
                handler = new Handler();
            }
            this.handler = handler;
        }
        
        @Override
        public Void call() throws Exception {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     0: aload_0        
            //     1: invokevirtual   com/crunchyroll/android/util/SafeAsyncTask$Task.doPreExecute:()V
            //     4: aload_0        
            //     5: aload_0        
            //     6: invokevirtual   com/crunchyroll/android/util/SafeAsyncTask$Task.doCall:()Ljava/lang/Object;
            //     9: invokevirtual   com/crunchyroll/android/util/SafeAsyncTask$Task.doSuccess:(Ljava/lang/Object;)V
            //    12: aload_0        
            //    13: invokevirtual   com/crunchyroll/android/util/SafeAsyncTask$Task.doFinally:()V
            //    16: aconst_null    
            //    17: areturn        
            //    18: astore_1       
            //    19: aload_0        
            //    20: aload_1        
            //    21: invokevirtual   com/crunchyroll/android/util/SafeAsyncTask$Task.doException:(Ljava/lang/Exception;)V
            //    24: aload_0        
            //    25: invokevirtual   com/crunchyroll/android/util/SafeAsyncTask$Task.doFinally:()V
            //    28: goto            16
            //    31: astore_1       
            //    32: aload_0        
            //    33: invokevirtual   com/crunchyroll/android/util/SafeAsyncTask$Task.doFinally:()V
            //    36: aload_1        
            //    37: athrow         
            //    38: astore_1       
            //    39: goto            24
            //    Exceptions:
            //  throws java.lang.Exception
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                 
            //  -----  -----  -----  -----  ---------------------
            //  0      12     18     31     Ljava/lang/Exception;
            //  0      12     31     38     Any
            //  19     24     38     42     Ljava/lang/Exception;
            //  19     24     31     38     Any
            // 
            // The error that occurred was:
            // 
            // java.lang.IllegalStateException: Expression is linked from several locations: Label_0024:
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
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:556)
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
        
        protected ResultT doCall() throws Exception {
            return this.parent.call();
        }
        
        protected void doException(final Exception ex) throws Exception {
            if (this.parent.launchLocation != null) {
                final ArrayList list = new ArrayList((Collection<? extends E>)Arrays.asList(ex.getStackTrace()));
                list.addAll(Arrays.asList(this.parent.launchLocation));
                ex.setStackTrace(list.toArray(new StackTraceElement[list.size()]));
            }
            this.postToUiThreadAndWait(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    if (ex instanceof InterruptedException || ex instanceof InterruptedIOException) {
                        Task.this.parent.onInterrupted(ex);
                    }
                    else {
                        Task.this.parent.onException(ex);
                    }
                    return null;
                }
            });
        }
        
        protected void doFinally() throws Exception {
            this.postToUiThreadAndWait(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    Task.this.parent.onFinally();
                    return null;
                }
            });
        }
        
        protected void doPreExecute() throws Exception {
            this.postToUiThreadAndWait(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    Task.this.parent.onPreExecute();
                    return null;
                }
            });
        }
        
        protected void doSuccess(final ResultT resultT) throws Exception {
            this.postToUiThreadAndWait(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    Task.this.parent.onSuccess(resultT);
                    return null;
                }
            });
        }
        
        protected void postToUiThreadAndWait(final Callable callable) throws Exception {
            final CountDownLatch countDownLatch = new CountDownLatch(1);
            final Exception[] array = { null };
            this.handler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    try {
                        callable.call();
                    }
                    catch (Exception ex) {
                        array[0] = ex;
                    }
                    finally {
                        countDownLatch.countDown();
                    }
                }
            });
            countDownLatch.await();
            if (array[0] != null) {
                throw array[0];
            }
        }
    }
}
