// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.concurrency;

import java.util.Collection;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public abstract class PriorityAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> implements Dependency<Task>, PriorityProvider, Task
{
    private final PriorityTask priorityTask;
    
    public PriorityAsyncTask() {
        this.priorityTask = new PriorityTask();
    }
    
    @Override
    public void addDependency(final Task task) {
        if (this.getStatus() != Status.PENDING) {
            throw new IllegalStateException("Must not add Dependency after task is running");
        }
        ((Dependency)this.getDelegate()).addDependency(task);
    }
    
    @Override
    public boolean areDependenciesMet() {
        return ((Dependency)this.getDelegate()).areDependenciesMet();
    }
    
    @Override
    public int compareTo(final Object o) {
        return Priority.compareTo(this, o);
    }
    
    public final void executeOnExecutor(final ExecutorService executorService, final Params... array) {
        super.executeOnExecutor(new ProxyExecutor<Object>(executorService, this), array);
    }
    
    public <T extends io.fabric.sdk.android.services.concurrency.Dependency> T getDelegate() {
        return (T)this.priorityTask;
    }
    
    @Override
    public Collection<Task> getDependencies() {
        return ((Dependency)this.getDelegate()).getDependencies();
    }
    
    @Override
    public Priority getPriority() {
        return this.getDelegate().getPriority();
    }
    
    @Override
    public boolean isFinished() {
        return ((Task)this.getDelegate()).isFinished();
    }
    
    @Override
    public void setError(final Throwable error) {
        ((Task)this.getDelegate()).setError(error);
    }
    
    @Override
    public void setFinished(final boolean finished) {
        ((Task)this.getDelegate()).setFinished(finished);
    }
    
    private static class ProxyExecutor<Result> implements Executor
    {
        private final Executor executor;
        private final PriorityAsyncTask task;
        
        public ProxyExecutor(final Executor executor, final PriorityAsyncTask task) {
            this.executor = executor;
            this.task = task;
        }
        
        @Override
        public void execute(final Runnable runnable) {
            this.executor.execute(new PriorityFutureTask<Result>(runnable, null) {
                @Override
                public <T extends io.fabric.sdk.android.services.concurrency.Dependency> T getDelegate() {
                    return (T)ProxyExecutor.this.task;
                }
            });
        }
    }
}
