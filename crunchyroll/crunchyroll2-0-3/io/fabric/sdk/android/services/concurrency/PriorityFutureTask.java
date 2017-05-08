// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.concurrency;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class PriorityFutureTask<V> extends FutureTask<V> implements Dependency<Task>, PriorityProvider, Task
{
    final Object delegate;
    
    public PriorityFutureTask(final Runnable runnable, final V v) {
        super(runnable, v);
        this.delegate = this.checkAndInitDelegate(runnable);
    }
    
    public PriorityFutureTask(final Callable<V> callable) {
        super(callable);
        this.delegate = this.checkAndInitDelegate(callable);
    }
    
    @Override
    public void addDependency(final Task task) {
        ((Dependency)this.getDelegate()).addDependency(task);
    }
    
    @Override
    public boolean areDependenciesMet() {
        return ((Dependency)this.getDelegate()).areDependenciesMet();
    }
    
    protected <T extends io.fabric.sdk.android.services.concurrency.Dependency> T checkAndInitDelegate(final Object o) {
        if (PriorityTask.isProperDelegate(o)) {
            return (T)o;
        }
        return (T)new PriorityTask();
    }
    
    @Override
    public int compareTo(final Object o) {
        return this.getDelegate().compareTo(o);
    }
    
    public <T extends io.fabric.sdk.android.services.concurrency.Dependency> T getDelegate() {
        return (T)this.delegate;
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
}
