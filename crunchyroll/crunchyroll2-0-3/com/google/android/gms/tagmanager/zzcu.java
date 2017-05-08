// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.tagmanager;

class zzcu extends zzct
{
    private static final Object zzaND;
    private static zzcu zzaNO;
    private boolean connected;
    private zzau zzaNF;
    private volatile zzas zzaNG;
    private int zzaNH;
    private boolean zzaNI;
    private boolean zzaNJ;
    private boolean zzaNK;
    private zzav zzaNL;
    private boolean zzaNN;
    
    static {
        zzaND = new Object();
    }
    
    private zzcu() {
        this.zzaNH = 1800000;
        this.zzaNI = true;
        this.zzaNJ = false;
        this.connected = true;
        this.zzaNK = true;
        this.zzaNL = new zzav() {};
        this.zzaNN = false;
    }
    
    public static zzcu zzzz() {
        if (zzcu.zzaNO == null) {
            zzcu.zzaNO = new zzcu();
        }
        return zzcu.zzaNO;
    }
    
    public void dispatch() {
        synchronized (this) {
            if (!this.zzaNJ) {
                zzbg.zzaB("Dispatch call queued. Dispatch will run once initialization is complete.");
                this.zzaNI = true;
            }
            else {
                this.zzaNG.zzf(new Runnable() {
                    @Override
                    public void run() {
                        zzcu.this.zzaNF.dispatch();
                    }
                });
            }
        }
    }
}
