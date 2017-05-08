// 
// Decompiled by Procyon v0.5.30
// 

package io.fabric.sdk.android;

import java.io.File;
import io.fabric.sdk.android.services.concurrency.Task;
import java.util.Collection;
import io.fabric.sdk.android.services.concurrency.DependsOn;
import io.fabric.sdk.android.services.common.IdManager;
import android.content.Context;

public abstract class Kit<Result> implements Comparable<Kit>
{
    Context context;
    Fabric fabric;
    IdManager idManager;
    InitializationCallback<Result> initializationCallback;
    InitializationTask<Result> initializationTask;
    
    public Kit() {
        this.initializationTask = new InitializationTask<Result>(this);
    }
    
    @Override
    public int compareTo(final Kit kit) {
        if (!this.containsAnnotatedDependency(kit)) {
            if (kit.containsAnnotatedDependency(this)) {
                return -1;
            }
            if (!this.hasAnnotatedDependency() || kit.hasAnnotatedDependency()) {
                if (!this.hasAnnotatedDependency() && kit.hasAnnotatedDependency()) {
                    return -1;
                }
                return 0;
            }
        }
        return 1;
    }
    
    boolean containsAnnotatedDependency(final Kit kit) {
        final DependsOn dependsOn = this.getClass().getAnnotation(DependsOn.class);
        if (dependsOn != null) {
            final Class<?>[] value = dependsOn.value();
            for (int length = value.length, i = 0; i < length; ++i) {
                if (value[i].equals(kit.getClass())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    protected abstract Result doInBackground();
    
    public Context getContext() {
        return this.context;
    }
    
    protected Collection<Task> getDependencies() {
        return this.initializationTask.getDependencies();
    }
    
    public Fabric getFabric() {
        return this.fabric;
    }
    
    protected IdManager getIdManager() {
        return this.idManager;
    }
    
    public abstract String getIdentifier();
    
    public String getPath() {
        return ".Fabric" + File.separator + this.getIdentifier();
    }
    
    public abstract String getVersion();
    
    boolean hasAnnotatedDependency() {
        return this.getClass().getAnnotation(DependsOn.class) != null;
    }
    
    final void initialize() {
        this.initializationTask.executeOnExecutor(this.fabric.getExecutorService(), null);
    }
    
    void injectParameters(final Context context, final Fabric fabric, final InitializationCallback<Result> initializationCallback, final IdManager idManager) {
        this.fabric = fabric;
        this.context = (Context)new FabricContext(context, this.getIdentifier(), this.getPath());
        this.initializationCallback = initializationCallback;
        this.idManager = idManager;
    }
    
    protected void onCancelled(final Result result) {
    }
    
    protected void onPostExecute(final Result result) {
    }
    
    protected boolean onPreExecute() {
        return true;
    }
}
