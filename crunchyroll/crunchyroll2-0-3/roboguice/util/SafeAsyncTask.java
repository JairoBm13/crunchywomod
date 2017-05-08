// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.util;

import java.util.concurrent.CountDownLatch;
import java.io.InterruptedIOException;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import android.os.Looper;
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
    
    public Executor executor() {
        return this.executor;
    }
    
    public SafeAsyncTask<ResultT> executor(final Executor executor) {
        this.executor = executor;
        return this;
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
    
    protected Task<ResultT> newTask() {
        return new Task<ResultT>(this);
    }
    
    protected void onException(final Exception ex) throws RuntimeException {
        this.onThrowable(ex);
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
    
    protected void onThrowable(final Throwable t) throws RuntimeException {
        Log.e("roboguice", "Throwable caught during background processing", t);
    }
    
    public static class Task<ResultT> implements Callable<Void>
    {
        protected Handler handler;
        protected SafeAsyncTask<ResultT> parent;
        
        public Task(final SafeAsyncTask<ResultT> parent) {
            this.parent = parent;
            Handler handler;
            if (parent.handler != null) {
                handler = parent.handler;
            }
            else {
                handler = new Handler(Looper.getMainLooper());
            }
            this.handler = handler;
        }
        
        @Override
        public Void call() throws Exception {
            try {
                this.doPreExecute();
                this.doSuccess(this.doCall());
                return null;
            }
            catch (Exception ex) {
                try {
                    this.doException(ex);
                    this.doFinally();
                }
                catch (Exception ex2) {
                    Ln.e(ex2);
                }
            }
            catch (Throwable t) {
                try {
                    this.doThrowable(t);
                    this.doFinally();
                }
                catch (Exception ex3) {
                    Ln.e(ex3);
                }
            }
            finally {
                this.doFinally();
            }
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
        
        protected void doThrowable(final Throwable t) throws Exception {
            if (this.parent.launchLocation != null) {
                final ArrayList list = new ArrayList((Collection<? extends E>)Arrays.asList(t.getStackTrace()));
                list.addAll(Arrays.asList(this.parent.launchLocation));
                t.setStackTrace(list.toArray(new StackTraceElement[list.size()]));
            }
            this.postToUiThreadAndWait(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    Task.this.parent.onThrowable(t);
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
