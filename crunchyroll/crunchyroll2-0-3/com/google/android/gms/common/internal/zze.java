// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.internal;

import java.util.Iterator;
import java.util.HashSet;
import java.util.Collections;
import java.util.Collection;
import com.google.android.gms.internal.zzpt;
import com.google.android.gms.common.api.Api;
import java.util.Map;
import android.view.View;
import com.google.android.gms.common.api.Scope;
import java.util.Set;
import android.accounts.Account;

public final class zze
{
    private final Account zzMY;
    private final String zzOd;
    private final String zzOe;
    private final Set<Scope> zzWv;
    private final int zzWw;
    private final View zzWx;
    private final Set<Scope> zzZS;
    private final Map<Api<?>, zza> zzZT;
    private final zzpt zzZU;
    private Integer zzZV;
    
    public zze(final Account zzMY, final Collection<Scope> collection, final Map<Api<?>, zza> map, final int zzWw, final View zzWx, final String zzOe, final String zzOd, final zzpt zzZU) {
        this.zzMY = zzMY;
        Set<Scope> zzWv;
        if (collection == null) {
            zzWv = (Set<Scope>)Collections.EMPTY_SET;
        }
        else {
            zzWv = Collections.unmodifiableSet((Set<? extends Scope>)new HashSet<Scope>(collection));
        }
        this.zzWv = zzWv;
        Map<Api<?>, zza> empty_MAP = map;
        if (map == null) {
            empty_MAP = (Map<Api<?>, zza>)Collections.EMPTY_MAP;
        }
        this.zzZT = empty_MAP;
        this.zzWx = zzWx;
        this.zzWw = zzWw;
        this.zzOe = zzOe;
        this.zzOd = zzOd;
        this.zzZU = zzZU;
        final HashSet<Scope> set = new HashSet<Scope>(this.zzWv);
        final Iterator<zza> iterator = this.zzZT.values().iterator();
        while (iterator.hasNext()) {
            set.addAll((Collection<?>)iterator.next().zzWJ);
        }
        this.zzZS = (Set<Scope>)Collections.unmodifiableSet((Set<?>)set);
    }
    
    public Account getAccount() {
        return this.zzMY;
    }
    
    public void zza(final Integer zzZV) {
        this.zzZV = zzZV;
    }
    
    public zzpt zznB() {
        return this.zzZU;
    }
    
    public Integer zznC() {
        return this.zzZV;
    }
    
    public Account zznt() {
        if (this.zzMY != null) {
            return this.zzMY;
        }
        return new Account("<<default account>>", "com.google");
    }
    
    public Set<Scope> zznv() {
        return this.zzWv;
    }
    
    public Set<Scope> zznw() {
        return this.zzZS;
    }
    
    public Map<Api<?>, zza> zznx() {
        return this.zzZT;
    }
    
    public String zzny() {
        return this.zzOe;
    }
    
    public String zznz() {
        return this.zzOd;
    }
    
    public static final class zza
    {
        public final Set<Scope> zzWJ;
        public final boolean zzZW;
    }
}
