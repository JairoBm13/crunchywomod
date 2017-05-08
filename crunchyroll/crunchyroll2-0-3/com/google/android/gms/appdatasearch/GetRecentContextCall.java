// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.appdatasearch;

import java.util.List;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.Result;
import android.os.Parcel;
import android.accounts.Account;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class GetRecentContextCall
{
    public static class Request implements SafeParcelable
    {
        public static final zzf CREATOR;
        final int zzCY;
        public final Account zzNj;
        public final boolean zzNk;
        public final boolean zzNl;
        public final boolean zzNm;
        
        static {
            CREATOR = new zzf();
        }
        
        public Request() {
            this(null, false, false, false);
        }
        
        Request(final int zzCY, final Account zzNj, final boolean zzNk, final boolean zzNl, final boolean zzNm) {
            this.zzCY = zzCY;
            this.zzNj = zzNj;
            this.zzNk = zzNk;
            this.zzNl = zzNl;
            this.zzNm = zzNm;
        }
        
        public Request(final Account account, final boolean b, final boolean b2, final boolean b3) {
            this(1, account, b, b2, b3);
        }
        
        public int describeContents() {
            final zzf creator = Request.CREATOR;
            return 0;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            final zzf creator = Request.CREATOR;
            zzf.zza(this, parcel, n);
        }
    }
    
    public static class Response implements Result, SafeParcelable
    {
        public static final zzg CREATOR;
        final int zzCY;
        public Status zzNn;
        public List<UsageInfo> zzNo;
        public String[] zzNp;
        
        static {
            CREATOR = new zzg();
        }
        
        public Response() {
            this.zzCY = 1;
        }
        
        Response(final int zzCY, final Status zzNn, final List<UsageInfo> zzNo, final String[] zzNp) {
            this.zzCY = zzCY;
            this.zzNn = zzNn;
            this.zzNo = zzNo;
            this.zzNp = zzNp;
        }
        
        public int describeContents() {
            final zzg creator = Response.CREATOR;
            return 0;
        }
        
        @Override
        public Status getStatus() {
            return this.zzNn;
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            final zzg creator = Response.CREATOR;
            zzg.zza(this, parcel, n);
        }
    }
}
