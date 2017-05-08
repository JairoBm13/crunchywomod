// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android;

import io.fabric.sdk.android.Fabric;
import java.util.Collections;
import java.util.Arrays;
import java.util.Collection;
import com.crashlytics.android.core.CrashlyticsCore;
import com.crashlytics.android.beta.Beta;
import com.crashlytics.android.answers.Answers;
import io.fabric.sdk.android.KitGroup;
import io.fabric.sdk.android.Kit;

public class Crashlytics extends Kit<Void> implements KitGroup
{
    public final Answers answers;
    public final Beta beta;
    public final CrashlyticsCore core;
    public final Collection<? extends Kit> kits;
    
    public Crashlytics() {
        this(new Answers(), new Beta(), new CrashlyticsCore());
    }
    
    Crashlytics(final Answers answers, final Beta beta, final CrashlyticsCore core) {
        this.answers = answers;
        this.beta = beta;
        this.core = core;
        this.kits = Collections.unmodifiableCollection((Collection<? extends Kit>)Arrays.asList(answers, beta, core));
    }
    
    private static void checkInitialized() {
        if (getInstance() == null) {
            throw new IllegalStateException("Crashlytics must be initialized by calling Fabric.with(Context) prior to calling Crashlytics.getInstance()");
        }
    }
    
    public static Crashlytics getInstance() {
        return Fabric.getKit(Crashlytics.class);
    }
    
    public static void setString(final String s, final String s2) {
        checkInitialized();
        getInstance().core.setString(s, s2);
    }
    
    @Override
    protected Void doInBackground() {
        return null;
    }
    
    @Override
    public String getIdentifier() {
        return "com.crashlytics.sdk.android:crashlytics";
    }
    
    @Override
    public Collection<? extends Kit> getKits() {
        return this.kits;
    }
    
    @Override
    public String getVersion() {
        return "2.5.2.79";
    }
}
