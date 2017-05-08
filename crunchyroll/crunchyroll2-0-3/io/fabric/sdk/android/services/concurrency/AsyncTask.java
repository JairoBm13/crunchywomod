// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.concurrency;

import java.util.LinkedList;
import android.os.Message;
import android.os.Looper;
import android.os.Handler;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import android.util.Log;
import java.util.concurrent.Callable;
import android.os.Process;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;

public abstract class AsyncTask<Params, Progress, Result>
{
    private static final int CORE_POOL_SIZE;
    private static final int CPU_COUNT;
    private static final int MAXIMUM_POOL_SIZE;
    public static final Executor SERIAL_EXECUTOR;
    public static final Executor THREAD_POOL_EXECUTOR;
    private static volatile Executor defaultExecutor;
    private static final InternalHandler handler;
    private static final BlockingQueue<Runnable> poolWorkQueue;
    private static final ThreadFactory threadFactory;
    private final AtomicBoolean cancelled;
    private final FutureTask<Result> future;
    private volatile Status status;
    private final AtomicBoolean taskInvoked;
    private final WorkerRunnable<Params, Result> worker;
    
    static {
        CPU_COUNT = Runtime.getRuntime().availableProcessors();
        CORE_POOL_SIZE = AsyncTask.CPU_COUNT + 1;
        MAXIMUM_POOL_SIZE = AsyncTask.CPU_COUNT * 2 + 1;
        threadFactory = new ThreadFactory() {
            private final AtomicInteger count = new AtomicInteger(1);
            
            @Override
            public Thread newThread(final Runnable runnable) {
                return new Thread(runnable, "AsyncTask #" + this.count.getAndIncrement());
            }
        };
        poolWorkQueue = new LinkedBlockingQueue<Runnable>(128);
        THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(AsyncTask.CORE_POOL_SIZE, AsyncTask.MAXIMUM_POOL_SIZE, 1L, TimeUnit.SECONDS, AsyncTask.poolWorkQueue, AsyncTask.threadFactory);
        SERIAL_EXECUTOR = new SerialExecutor();
        handler = new InternalHandler();
        AsyncTask.defaultExecutor = AsyncTask.SERIAL_EXECUTOR;
    }
    
    public AsyncTask() {
        this.status = Status.PENDING;
        this.cancelled = new AtomicBoolean();
        this.taskInvoked = new AtomicBoolean();
        this.worker = (WorkerRunnable<Params, Result>)new WorkerRunnable<Params, Result>() {
            @Override
            public Result call() throws Exception {
                AsyncTask.this.taskInvoked.set(true);
                Process.setThreadPriority(10);
                return (Result)AsyncTask.this.postResult(AsyncTask.this.doInBackground(this.params));
            }
        };
        this.future = new FutureTask<Result>(this.worker) {
            @Override
            protected void done() {
                try {
                    AsyncTask.this.postResultIfNotInvoked(((FutureTask<Object>)this).get());
                }
                catch (InterruptedException ex) {
                    Log.w("AsyncTask", (Throwable)ex);
                }
                catch (ExecutionException ex2) {
                    throw new RuntimeException("An error occured while executing doInBackground()", ex2.getCause());
                }
                catch (CancellationException ex3) {
                    AsyncTask.this.postResultIfNotInvoked(null);
                }
            }
        };
    }
    
    private void finish(final Result result) {
        if (this.isCancelled()) {
            this.onCancelled(result);
        }
        else {
            this.onPostExecute(result);
        }
        this.status = Status.FINISHED;
    }
    
    private Result postResult(final Result result) {
        AsyncTask.handler.obtainMessage(1, (Object)new AsyncTaskResult(this, new Object[] { result })).sendToTarget();
        return result;
    }
    
    private void postResultIfNotInvoked(final Result result) {
        if (!this.taskInvoked.get()) {
            this.postResult(result);
        }
    }
    
    public final boolean cancel(final boolean b) {
        this.cancelled.set(true);
        return this.future.cancel(b);
    }
    
    protected abstract Result doInBackground(final Params... p0);
    
    public final AsyncTask<Params, Progress, Result> executeOnExecutor(final Executor executor, final Params... params) {
        if (this.status != Status.PENDING) {
            switch (this.status) {
                case RUNNING: {
                    throw new IllegalStateException("Cannot execute task: the task is already running.");
                }
                case FINISHED: {
                    throw new IllegalStateException("Cannot execute task: the task has already been executed (a task can be executed only once)");
                }
            }
        }
        this.status = Status.RUNNING;
        this.onPreExecute();
        this.worker.params = params;
        executor.execute(this.future);
        return this;
    }
    
    public final Status getStatus() {
        return this.status;
    }
    
    public final boolean isCancelled() {
        return this.cancelled.get();
    }
    
    protected void onCancelled() {
    }
    
    protected void onCancelled(final Result result) {
        this.onCancelled();
    }
    
    protected void onPostExecute(final Result result) {
    }
    
    protected void onPreExecute() {
    }
    
    protected void onProgressUpdate(final Progress... array) {
    }
    
    private static class AsyncTaskResult<Data>
    {
        final Data[] data;
        final AsyncTask task;
        
        AsyncTaskResult(final AsyncTask task, final Data... data) {
            this.task = task;
            this.data = data;
        }
    }
    
    private static class InternalHandler extends Handler
    {
        public InternalHandler() {
            super(Looper.getMainLooper());
        }
        
        public void handleMessage(final Message message) {
            final AsyncTaskResult asyncTaskResult = (AsyncTaskResult)message.obj;
            switch (message.what) {
                default: {}
                case 1: {
                    asyncTaskResult.task.finish(asyncTaskResult.data[0]);
                }
                case 2: {
                    asyncTaskResult.task.onProgressUpdate(asyncTaskResult.data);
                }
            }
        }
    }
    
    private static class SerialExecutor implements Executor
    {
        Runnable active;
        final LinkedList<Runnable> tasks;
        
        private SerialExecutor() {
            this.tasks = new LinkedList<Runnable>();
        }
        
        @Override
        public void execute(final Runnable runnable) {
            synchronized (this) {
                this.tasks.offer(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            runnable.run();
                        }
                        finally {
                            SerialExecutor.this.scheduleNext();
                        }
                    }
                });
                if (this.active == null) {
                    this.scheduleNext();
                }
            }
        }
        
        protected void scheduleNext() {
            synchronized (this) {
                final Runnable active = this.tasks.poll();
                this.active = active;
                if (active != null) {
                    AsyncTask.THREAD_POOL_EXECUTOR.execute(this.active);
                }
            }
        }
    }
    
    public enum Status
    {
        FINISHED, 
        PENDING, 
        RUNNING;
    }
    
    private abstract static class WorkerRunnable<Params, Result> implements Callable<Result>
    {
        Params[] params;
    }
}
