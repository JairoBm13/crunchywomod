// 
// Decompiled by Procyon v0.5.30
// 

package bolts;

import java.util.Iterator;
import bolts.Task$bolts.Task$TaskCompletionSource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public class Task<TResult>
{
    public static final ExecutorService BACKGROUND_EXECUTOR;
    private static final Executor IMMEDIATE_EXECUTOR;
    public static final Executor UI_THREAD_EXECUTOR;
    private boolean cancelled;
    private boolean complete;
    private List<Continuation<TResult, Void>> continuations;
    private Exception error;
    private final Object lock;
    private TResult result;
    
    static {
        BACKGROUND_EXECUTOR = BoltsExecutors.background();
        IMMEDIATE_EXECUTOR = BoltsExecutors.immediate();
        UI_THREAD_EXECUTOR = AndroidExecutors.uiThread();
    }
    
    private Task() {
        this.lock = new Object();
        this.continuations = new ArrayList<Continuation<TResult, Void>>();
    }
    
    static /* synthetic */ void access$100(final TaskCompletionSource taskCompletionSource, final Continuation continuation, final Task task, final Executor executor) {
        completeImmediately(taskCompletionSource, continuation, task, executor);
    }
    
    static /* synthetic */ void access$200(final TaskCompletionSource taskCompletionSource, final Continuation continuation, final Task task, final Executor executor) {
        completeAfterTask(taskCompletionSource, continuation, task, executor);
    }
    
    public static <TResult> Task<TResult> cancelled() {
        final TaskCompletionSource create = create();
        create.setCancelled();
        return (Task<TResult>)create.getTask();
    }
    
    private static <TContinuationResult, TResult> void completeAfterTask(final Task$TaskCompletionSource task$TaskCompletionSource, final Continuation<TResult, Task<TContinuationResult>> continuation, final Task<TResult> task, final Executor executor) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final Task<Object> task = continuation.then(task);
                    if (task == null) {
                        ((TaskCompletionSource)task$TaskCompletionSource).setResult(null);
                        return;
                    }
                    task.continueWith((Continuation<Object, Object>)new(this) {});
                }
                catch (Exception error) {
                    ((TaskCompletionSource)task$TaskCompletionSource).setError(error);
                }
            }
        });
    }
    
    private static <TContinuationResult, TResult> void completeImmediately(final Task$TaskCompletionSource task$TaskCompletionSource, final Continuation<TResult, TContinuationResult> continuation, final Task<TResult> task, final Executor executor) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    ((TaskCompletionSource)task$TaskCompletionSource).setResult(continuation.then(task));
                }
                catch (Exception error) {
                    ((TaskCompletionSource)task$TaskCompletionSource).setError(error);
                }
            }
        });
    }
    
    public static <TResult> Task$TaskCompletionSource create() {
        final Task task = new Task();
        task.getClass();
        return (Task$TaskCompletionSource)new TaskCompletionSource();
    }
    
    public static <TResult> Task<TResult> forError(final Exception error) {
        final TaskCompletionSource create = create();
        create.setError(error);
        return (Task<TResult>)create.getTask();
    }
    
    public static <TResult> Task<TResult> forResult(final TResult result) {
        final TaskCompletionSource create = create();
        create.setResult(result);
        return (Task<TResult>)create.getTask();
    }
    
    private void runContinuations() {
        final Object lock = this.lock;
        // monitorenter(lock)
        try {
            for (final Continuation<TResult, Void> continuation : this.continuations) {
                try {
                    continuation.then(this);
                }
                catch (RuntimeException ex) {
                    throw ex;
                }
                catch (Exception ex2) {
                    throw new RuntimeException(ex2);
                }
            }
        }
        finally {}
        this.continuations = null;
    }
    // monitorexit(lock)
    
    public <TContinuationResult> Task<TContinuationResult> continueWith(final Continuation<TResult, TContinuationResult> continuation) {
        return this.continueWith(continuation, Task.IMMEDIATE_EXECUTOR);
    }
    
    public <TContinuationResult> Task<TContinuationResult> continueWith(final Continuation<TResult, TContinuationResult> continuation, final Executor executor) {
        final TaskCompletionSource create = create();
        synchronized (this.lock) {
            final boolean completed = this.isCompleted();
            if (!completed) {
                this.continuations.add(new Continuation<TResult, Void>() {
                    @Override
                    public Void then(final Task<TResult> task) {
                        Task.access$100(create, continuation, task, executor);
                        return null;
                    }
                });
            }
            // monitorexit(this.lock)
            if (completed) {
                completeImmediately(create, (Continuation)continuation, this, executor);
            }
            return (Task<TContinuationResult>)create.getTask();
        }
    }
    
    public <TContinuationResult> Task<TContinuationResult> continueWithTask(final Continuation<TResult, Task<TContinuationResult>> continuation, final Executor executor) {
        final TaskCompletionSource create = create();
        synchronized (this.lock) {
            final boolean completed = this.isCompleted();
            if (!completed) {
                this.continuations.add(new Continuation<TResult, Void>() {
                    @Override
                    public Void then(final Task<TResult> task) {
                        Task.access$200(create, continuation, task, executor);
                        return null;
                    }
                });
            }
            // monitorexit(this.lock)
            if (completed) {
                completeAfterTask(create, (Continuation)continuation, this, executor);
            }
            return (Task<TContinuationResult>)create.getTask();
        }
    }
    
    public Exception getError() {
        synchronized (this.lock) {
            return this.error;
        }
    }
    
    public TResult getResult() {
        synchronized (this.lock) {
            return this.result;
        }
    }
    
    public boolean isCancelled() {
        synchronized (this.lock) {
            return this.cancelled;
        }
    }
    
    public boolean isCompleted() {
        synchronized (this.lock) {
            return this.complete;
        }
    }
    
    public boolean isFaulted() {
        while (true) {
            synchronized (this.lock) {
                if (this.error != null) {
                    return true;
                }
            }
            return false;
        }
    }
    
    public <TContinuationResult> Task<TContinuationResult> onSuccess(final Continuation<TResult, TContinuationResult> continuation) {
        return this.onSuccess(continuation, Task.IMMEDIATE_EXECUTOR);
    }
    
    public <TContinuationResult> Task<TContinuationResult> onSuccess(final Continuation<TResult, TContinuationResult> continuation, final Executor executor) {
        return (Task<TContinuationResult>)this.continueWithTask((Continuation<TResult, Task<Object>>)new Continuation<TResult, Task<TContinuationResult>>() {
            @Override
            public Task<TContinuationResult> then(final Task<TResult> task) {
                if (task.isFaulted()) {
                    return Task.forError(task.getError());
                }
                if (task.isCancelled()) {
                    return Task.cancelled();
                }
                return task.continueWith(continuation);
            }
        }, executor);
    }
    
    public class TaskCompletionSource
    {
        public Task<TResult> getTask() {
            return Task.this;
        }
        
        public void setCancelled() {
            if (!this.trySetCancelled()) {
                throw new IllegalStateException("Cannot cancel a completed task.");
            }
        }
        
        public void setError(final Exception ex) {
            if (!this.trySetError(ex)) {
                throw new IllegalStateException("Cannot set the error on a completed task.");
            }
        }
        
        public void setResult(final TResult tResult) {
            if (!this.trySetResult(tResult)) {
                throw new IllegalStateException("Cannot set the result of a completed task.");
            }
        }
        
        public boolean trySetCancelled() {
            synchronized (Task.this.lock) {
                if (Task.this.complete) {
                    return false;
                }
                Task.this.complete = true;
                Task.this.cancelled = true;
                Task.this.lock.notifyAll();
                Task.this.runContinuations();
                return true;
            }
        }
        
        public boolean trySetError(final Exception ex) {
            synchronized (Task.this.lock) {
                if (Task.this.complete) {
                    return false;
                }
                Task.this.complete = true;
                Task.this.error = ex;
                Task.this.lock.notifyAll();
                Task.this.runContinuations();
                return true;
            }
        }
        
        public boolean trySetResult(final TResult tResult) {
            synchronized (Task.this.lock) {
                if (Task.this.complete) {
                    return false;
                }
                Task.this.complete = true;
                Task.this.result = tResult;
                Task.this.lock.notifyAll();
                Task.this.runContinuations();
                return true;
            }
        }
    }
}
