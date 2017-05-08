// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.analytics.internal;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.math.BigInteger;
import java.util.Locale;
import android.text.TextUtils;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;

public class zza extends zzd
{
    public static boolean zzJk;
    private AdvertisingIdClient.Info zzJl;
    private final zzaj zzJm;
    private String zzJn;
    private boolean zzJo;
    private Object zzJp;
    
    zza(final zzf zzf) {
        super(zzf);
        this.zzJo = false;
        this.zzJp = new Object();
        this.zzJm = new zzaj(zzf.zzhP());
    }
    
    private boolean zza(final AdvertisingIdClient.Info info, AdvertisingIdClient.Info id) {
        final String s = null;
        if (id == null) {
            id = null;
        }
        else {
            id = ((AdvertisingIdClient.Info)id).getId();
        }
        if (TextUtils.isEmpty((CharSequence)id)) {
            return true;
        }
        while (true) {
            final String zziP = this.zzhV().zziP();
            final AdvertisingIdClient.Info info2;
        Label_0192:
            while (true) {
                synchronized (this.zzJp) {
                    if (!this.zzJo) {
                        this.zzJn = this.zzhF();
                        this.zzJo = true;
                        if (TextUtils.isEmpty((CharSequence)zzaR((String)id + zziP))) {
                            return false;
                        }
                        break Label_0192;
                    }
                }
                if (!TextUtils.isEmpty((CharSequence)this.zzJn)) {
                    continue;
                }
                String id2;
                if (info2 == null) {
                    id2 = s;
                }
                else {
                    id2 = info2.getId();
                }
                if (id2 == null) {
                    // monitorexit(o)
                    return this.zzaS((String)id + zziP);
                }
                this.zzJn = zzaR(id2 + zziP);
                continue;
            }
            if (((String)info2).equals(this.zzJn)) {
                // monitorexit(o)
                return true;
            }
            String zziQ;
            if (!TextUtils.isEmpty((CharSequence)this.zzJn)) {
                this.zzaT("Resetting the client id because Advertising Id changed.");
                zziQ = this.zzhV().zziQ();
                this.zza("New client Id", zziQ);
            }
            else {
                zziQ = zziP;
            }
            // monitorexit(o)
            return this.zzaS((String)id + zziQ);
        }
    }
    
    private static String zzaR(final String s) {
        final MessageDigest zzbl = zzam.zzbl("MD5");
        if (zzbl == null) {
            return null;
        }
        return String.format(Locale.US, "%032X", new BigInteger(1, zzbl.digest(s.getBytes())));
    }
    
    private boolean zzaS(String zzaR) {
        try {
            zzaR = zzaR(zzaR);
            this.zzaT("Storing hashed adid.");
            final FileOutputStream openFileOutput = this.getContext().openFileOutput("gaClientIdData", 0);
            openFileOutput.write(zzaR.getBytes());
            openFileOutput.close();
            this.zzJn = zzaR;
            return true;
        }
        catch (IOException ex) {
            this.zze("Error creating hash file", ex);
            return false;
        }
    }
    
    private AdvertisingIdClient.Info zzhD() {
        synchronized (this) {
            if (this.zzJm.zzv(1000L)) {
                this.zzJm.start();
                final AdvertisingIdClient.Info zzhE = this.zzhE();
                if (this.zza(this.zzJl, zzhE)) {
                    this.zzJl = zzhE;
                }
                else {
                    this.zzaX("Failed to reset client id on adid change. Not using adid");
                    this.zzJl = new AdvertisingIdClient.Info("", false);
                }
            }
            return this.zzJl;
        }
    }
    
    public String zzhC() {
        this.zzia();
        final AdvertisingIdClient.Info zzhD = this.zzhD();
        String id;
        if (zzhD != null) {
            id = zzhD.getId();
        }
        else {
            id = null;
        }
        if (TextUtils.isEmpty((CharSequence)id)) {
            return null;
        }
        return id;
    }
    
    protected AdvertisingIdClient.Info zzhE() {
        Object advertisingIdInfo = null;
        try {
            advertisingIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(this.getContext());
            return (AdvertisingIdClient.Info)advertisingIdInfo;
        }
        catch (IllegalStateException ex) {
            this.zzaW("IllegalStateException getting Ad Id Info. If you would like to see Audience reports, please ensure that you have added '<meta-data android:name=\"com.google.android.gms.version\" android:value=\"@integer/google_play_services_version\" />' to your application manifest file. See http://goo.gl/naFqQk for details.");
            return null;
        }
        catch (Throwable t) {
            if (!zza.zzJk) {
                zza.zzJk = true;
                this.zzd("Error getting advertiser id", t);
                return null;
            }
            return (AdvertisingIdClient.Info)advertisingIdInfo;
        }
    }
    
    protected String zzhF() {
        FileInputStream openFileInput = null;
        Object o = null;
        try {
            openFileInput = this.getContext().openFileInput("gaClientIdData");
            o = new byte[128];
            final int read = openFileInput.read((byte[])o, 0, 128);
            if (openFileInput.available() > 0) {
                this.zzaW("Hash file seems corrupted, deleting it.");
                openFileInput.close();
                this.getContext().deleteFile("gaClientIdData");
                return null;
            }
            if (read <= 0) {
                this.zzaT("Hash file is empty.");
                openFileInput.close();
                return null;
            }
            o = new String((byte[])o, 0, read);
            final FileInputStream fileInputStream = openFileInput;
            fileInputStream.close();
            final byte[] array = (byte[])o;
            return (String)(Object)array;
        }
        catch (IOException ex) {}
        catch (FileNotFoundException ex2) {
            return null;
        }
        try {
            final FileInputStream fileInputStream = openFileInput;
            fileInputStream.close();
            final byte[] array = (byte[])o;
            return (String)(Object)array;
        }
        catch (IOException ex3) {}
        catch (FileNotFoundException ex4) {}
    }
    
    @Override
    protected void zzhn() {
    }
    
    public boolean zzhy() {
        final boolean b = false;
        this.zzia();
        final AdvertisingIdClient.Info zzhD = this.zzhD();
        boolean b2 = b;
        if (zzhD != null) {
            b2 = b;
            if (!zzhD.isLimitAdTrackingEnabled()) {
                b2 = true;
            }
        }
        return b2;
    }
}
