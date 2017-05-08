// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android.services.concurrency;

import java.util.Collections;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.List;

public class PriorityTask implements Dependency<Task>, PriorityProvider, Task
{
    private final List<Task> dependencies;
    private final AtomicBoolean hasRun;
    private final AtomicReference<Throwable> throwable;
    
    public PriorityTask() {
        this.dependencies = new ArrayList<Task>();
        this.hasRun = new AtomicBoolean(false);
        this.throwable = new AtomicReference<Throwable>(null);
    }
    
    public static boolean isProperDelegate(final Object o) {
        final boolean b = false;
        try {
            final Dependency dependency = (Dependency)o;
            final Task task = (Task)o;
            final PriorityProvider priorityProvider = (PriorityProvider)o;
            boolean b2 = b;
            if (dependency != null) {
                b2 = b;
                if (task != null) {
                    b2 = b;
                    if (priorityProvider != null) {
                        b2 = true;
                    }
                }
            }
            return b2;
        }
        catch (ClassCastException ex) {
            return false;
        }
    }
    
    @Override
    public void addDependency(final Task task) {
        synchronized (this) {
            this.dependencies.add(task);
        }
    }
    
    @Override
    public boolean areDependenciesMet() {
        final Iterator<Task> iterator = this.getDependencies().iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().isFinished()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int compareTo(final Object o) {
        return Priority.compareTo(this, o);
    }
    
    @Override
    public Collection<Task> getDependencies() {
        synchronized (this) {
            return Collections.unmodifiableCollection((Collection<? extends Task>)this.dependencies);
        }
    }
    
    @Override
    public Priority getPriority() {
        return Priority.NORMAL;
    }
    
    @Override
    public boolean isFinished() {
        return this.hasRun.get();
    }
    
    @Override
    public void setError(final Throwable t) {
        this.throwable.set(t);
    }
    
    @Override
    public void setFinished(final boolean b) {
        synchronized (this) {
            this.hasRun.set(b);
        }
    }
}
