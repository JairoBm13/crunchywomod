// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.appdatasearch;

import android.os.Parcel;
import com.google.android.gms.internal.zzrn;
import com.google.android.gms.internal.zznj;
import java.io.UnsupportedEncodingException;
import java.util.zip.CRC32;
import android.os.Bundle;
import android.content.ComponentName;
import com.google.android.gms.appindexing.AppIndexApi;
import java.util.List;
import android.net.Uri;
import android.content.Intent;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class UsageInfo implements SafeParcelable
{
    public static final zzj CREATOR;
    final int zzCY;
    final DocumentId zzNH;
    final long zzNI;
    int zzNJ;
    final DocumentContents zzNK;
    final boolean zzNL;
    int zzNM;
    int zzNN;
    public final String zztt;
    
    static {
        CREATOR = new zzj();
    }
    
    UsageInfo(final int zzCY, final DocumentId zzNH, final long zzNI, final int zzNJ, final String zztt, final DocumentContents zzNK, final boolean zzNL, final int zzNM, final int zzNN) {
        this.zzCY = zzCY;
        this.zzNH = zzNH;
        this.zzNI = zzNI;
        this.zzNJ = zzNJ;
        this.zztt = zztt;
        this.zzNK = zzNK;
        this.zzNL = zzNL;
        this.zzNM = zzNM;
        this.zzNN = zzNN;
    }
    
    private UsageInfo(final DocumentId documentId, final long n, final int n2, final String s, final DocumentContents documentContents, final boolean b, final int n3, final int n4) {
        this(1, documentId, n, n2, s, documentContents, b, n3, n4);
    }
    
    public static DocumentContents.zza zza(final Intent intent, String s, final Uri uri, final String s2, final List<AppIndexApi.AppIndexingLink> list) {
        final DocumentContents.zza zza = new DocumentContents.zza();
        zza.zza(zzbt(s));
        if (uri != null) {
            zza.zza(zzh(uri));
        }
        if (list != null) {
            zza.zza(zzh(list));
        }
        s = intent.getAction();
        if (s != null) {
            zza.zza(zzp("intent_action", s));
        }
        s = intent.getDataString();
        if (s != null) {
            zza.zza(zzp("intent_data", s));
        }
        final ComponentName component = intent.getComponent();
        if (component != null) {
            zza.zza(zzp("intent_activity", component.getClassName()));
        }
        final Bundle extras = intent.getExtras();
        if (extras != null) {
            final String string = extras.getString("intent_extra_data_key");
            if (string != null) {
                zza.zza(zzp("intent_extra_data", string));
            }
        }
        return zza.zzbp(s2).zzI(true);
    }
    
    public static DocumentId zza(final String s, final Intent intent) {
        return zzo(s, zzg(intent));
    }
    
    private static DocumentSection zzbt(final String s) {
        return new DocumentSection(s, new RegisterSectionInfo.zza("title").zzaj(1).zzK(true).zzbs("name").zzkM(), "text1");
    }
    
    private static String zzg(final Intent intent) {
        final String uri = intent.toUri(1);
        final CRC32 crc32 = new CRC32();
        try {
            crc32.update(uri.getBytes("UTF-8"));
            return Long.toHexString(crc32.getValue());
        }
        catch (UnsupportedEncodingException ex) {
            throw new IllegalStateException(ex);
        }
    }
    
    private static DocumentSection zzh(final Uri uri) {
        return new DocumentSection(uri.toString(), new RegisterSectionInfo.zza("web_url").zzaj(4).zzJ(true).zzbs("url").zzkM());
    }
    
    private static DocumentSection zzh(final List<AppIndexApi.AppIndexingLink> list) {
        final zznj.zza zza = new zznj.zza();
        final zznj.zza.zza[] zzawk = new zznj.zza.zza[list.size()];
        for (int i = 0; i < zzawk.length; ++i) {
            zzawk[i] = new zznj.zza.zza();
            final AppIndexApi.AppIndexingLink appIndexingLink = list.get(i);
            zzawk[i].zzawm = appIndexingLink.appIndexingUrl.toString();
            zzawk[i].viewId = appIndexingLink.viewId;
            if (appIndexingLink.webUrl != null) {
                zzawk[i].zzawn = appIndexingLink.webUrl.toString();
            }
        }
        zza.zzawk = zzawk;
        return new DocumentSection(zzrn.zzf(zza), new RegisterSectionInfo.zza("outlinks").zzJ(true).zzbs(".private:outLinks").zzbr("blob").zzkM());
    }
    
    private static DocumentId zzo(final String s, final String s2) {
        return new DocumentId(s, "", s2);
    }
    
    private static DocumentSection zzp(final String s, final String s2) {
        return new DocumentSection(s2, new RegisterSectionInfo.zza(s).zzJ(true).zzkM(), s);
    }
    
    public int describeContents() {
        final zzj creator = UsageInfo.CREATOR;
        return 0;
    }
    
    @Override
    public String toString() {
        return String.format("UsageInfo[documentId=%s, timestamp=%d, usageType=%d, status=%d]", this.zzNH, this.zzNI, this.zzNJ, this.zzNN);
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        final zzj creator = UsageInfo.CREATOR;
        zzj.zza(this, parcel, n);
    }
    
    public static final class zza
    {
        private String zzHZ;
        private DocumentId zzNH;
        private long zzNI;
        private int zzNJ;
        private DocumentContents zzNK;
        private boolean zzNL;
        private int zzNM;
        private int zzNN;
        
        public zza() {
            this.zzNI = -1L;
            this.zzNJ = -1;
            this.zzNM = -1;
            this.zzNL = false;
            this.zzNN = 0;
        }
        
        public zza zzL(final boolean zzNL) {
            this.zzNL = zzNL;
            return this;
        }
        
        public zza zza(final DocumentContents zzNK) {
            this.zzNK = zzNK;
            return this;
        }
        
        public zza zza(final DocumentId zzNH) {
            this.zzNH = zzNH;
            return this;
        }
        
        public zza zzal(final int zzNJ) {
            this.zzNJ = zzNJ;
            return this;
        }
        
        public zza zzam(final int zzNN) {
            this.zzNN = zzNN;
            return this;
        }
        
        public UsageInfo zzkN() {
            return new UsageInfo(this.zzNH, this.zzNI, this.zzNJ, this.zzHZ, this.zzNK, this.zzNL, this.zzNM, this.zzNN, null);
        }
        
        public zza zzw(final long zzNI) {
            this.zzNI = zzNI;
            return this;
        }
    }
}
