// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics.internal;

import java.util.HashSet;
import android.text.TextUtils;
import java.util.Iterator;
import android.content.pm.ApplicationInfo;
import android.content.Context;
import android.app.ActivityManager$RunningAppProcessInfo;
import android.os.Process;
import android.app.ActivityManager;
import com.google.android.gms.common.internal.zzd;
import com.google.android.gms.common.internal.zzu;
import java.util.Set;

public class zzr
{
    private final zzf zzIa;
    private Boolean zzKO;
    private String zzKP;
    private Set<Integer> zzKQ;
    
    protected zzr(final zzf zzIa) {
        zzu.zzu(zzIa);
        this.zzIa = zzIa;
    }
    
    public boolean zziW() {
        return zzd.zzZR;
    }
    
    public boolean zziX() {
        Label_0158: {
            if (this.zzKO != null) {
                break Label_0158;
            }
            synchronized (this) {
                if (this.zzKO == null) {
                    final Context context = this.zzIa.getContext();
                    final ApplicationInfo applicationInfo = context.getApplicationInfo();
                    if (applicationInfo != null) {
                        final String processName = applicationInfo.processName;
                        final ActivityManager activityManager = (ActivityManager)context.getSystemService("activity");
                        if (activityManager != null) {
                            final int myPid = Process.myPid();
                            for (final ActivityManager$RunningAppProcessInfo activityManager$RunningAppProcessInfo : activityManager.getRunningAppProcesses()) {
                                if (myPid == activityManager$RunningAppProcessInfo.pid) {
                                    this.zzKO = (processName != null && processName.equals(activityManager$RunningAppProcessInfo.processName));
                                    break;
                                }
                            }
                        }
                    }
                    if (this.zzKO == null) {
                        this.zzKO = Boolean.TRUE;
                        this.zzIa.zzhQ().zzaX("My process not in the list of running processes");
                    }
                }
                // monitorexit(this)
                return this.zzKO;
            }
        }
    }
    
    public boolean zziY() {
        return zzy.zzLa.get();
    }
    
    public int zziZ() {
        return zzy.zzLt.get();
    }
    
    public int zzjA() {
        return zzy.zzLE.get();
    }
    
    public long zzjB() {
        return zzy.zzLF.get();
    }
    
    public long zzjC() {
        return zzy.zzLO.get();
    }
    
    public int zzja() {
        return zzy.zzLx.get();
    }
    
    public int zzjb() {
        return zzy.zzLy.get();
    }
    
    public int zzjc() {
        return zzy.zzLz.get();
    }
    
    public long zzjd() {
        return zzy.zzLi.get();
    }
    
    public long zzje() {
        return zzy.zzLh.get();
    }
    
    public long zzjf() {
        return zzy.zzLl.get();
    }
    
    public long zzjg() {
        return zzy.zzLm.get();
    }
    
    public int zzjh() {
        return zzy.zzLn.get();
    }
    
    public int zzji() {
        return zzy.zzLo.get();
    }
    
    public long zzjj() {
        return zzy.zzLB.get();
    }
    
    public String zzjk() {
        return zzy.zzLq.get();
    }
    
    public String zzjl() {
        return zzy.zzLp.get();
    }
    
    public String zzjm() {
        return zzy.zzLr.get();
    }
    
    public String zzjn() {
        return zzy.zzLs.get();
    }
    
    public zzm zzjo() {
        return zzm.zzbc(zzy.zzLu.get());
    }
    
    public zzo zzjp() {
        return zzo.zzbd(zzy.zzLv.get());
    }
    
    public Set<Integer> zzjq() {
        final String zzKP = zzy.zzLA.get();
        Label_0104: {
            if (this.zzKQ != null && this.zzKP != null && this.zzKP.equals(zzKP)) {
                break Label_0104;
            }
            final String[] split = TextUtils.split(zzKP, ",");
            final HashSet<Integer> zzKQ = new HashSet<Integer>();
            final int length = split.length;
            int n = 0;
        Label_0086_Outer:
            while (true) {
                Label_0093: {
                    if (n >= length) {
                        break Label_0093;
                    }
                    final String s = split[n];
                    while (true) {
                        try {
                            zzKQ.add(Integer.parseInt(s));
                            ++n;
                            continue Label_0086_Outer;
                            return this.zzKQ;
                            this.zzKP = zzKP;
                            this.zzKQ = zzKQ;
                            return this.zzKQ;
                        }
                        catch (NumberFormatException ex) {
                            continue;
                        }
                        break;
                    }
                }
                break;
            }
        }
    }
    
    public long zzjr() {
        return zzy.zzLJ.get();
    }
    
    public long zzjs() {
        return zzy.zzLK.get();
    }
    
    public long zzjt() {
        return zzy.zzLN.get();
    }
    
    public int zzju() {
        return zzy.zzLe.get();
    }
    
    public int zzjv() {
        return zzy.zzLg.get();
    }
    
    public String zzjw() {
        return "google_analytics_v4.db";
    }
    
    public String zzjx() {
        return "google_analytics2_v4.db";
    }
    
    public long zzjy() {
        return 86400000L;
    }
    
    public int zzjz() {
        return zzy.zzLD.get();
    }
}
