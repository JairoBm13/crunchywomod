// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.server.response;

import com.google.android.gms.common.internal.safeparcel.zzb;
import java.util.ArrayList;
import java.util.Set;
import android.os.Bundle;
import com.google.android.gms.internal.zzkx;
import com.google.android.gms.common.internal.safeparcel.zza;
import com.google.android.gms.internal.zzli;
import com.google.android.gms.internal.zzky;
import com.google.android.gms.internal.zzlh;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import com.google.android.gms.common.internal.zzu;
import android.os.Parcel;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;

public class SafeParcelResponse extends FastJsonResponse implements SafeParcelable
{
    public static final zze CREATOR;
    private final String mClassName;
    private final int zzCY;
    private final FieldMappingDictionary zzabO;
    private final Parcel zzabV;
    private final int zzabW;
    private int zzabX;
    private int zzabY;
    
    static {
        CREATOR = new zze();
    }
    
    SafeParcelResponse(final int zzCY, final Parcel parcel, final FieldMappingDictionary zzabO) {
        this.zzCY = zzCY;
        this.zzabV = zzu.zzu(parcel);
        this.zzabW = 2;
        this.zzabO = zzabO;
        if (this.zzabO == null) {
            this.mClassName = null;
        }
        else {
            this.mClassName = this.zzabO.zzoC();
        }
        this.zzabX = 2;
    }
    
    private static HashMap<Integer, Map.Entry<String, Field<?, ?>>> zzC(final Map<String, Field<?, ?>> map) {
        final HashMap<Integer, Map.Entry<K, Field>> hashMap = (HashMap<Integer, Map.Entry<K, Field>>)new HashMap<Integer, Map.Entry<String, Field<?, ?>>>();
        for (final Map.Entry<String, Field<?, ?>> entry : map.entrySet()) {
            hashMap.put(((Field)entry.getValue()).zzot(), entry);
        }
        return (HashMap<Integer, Map.Entry<String, Field<?, ?>>>)hashMap;
    }
    
    private void zza(final StringBuilder sb, final int n, final Object o) {
        switch (n) {
            default: {
                throw new IllegalArgumentException("Unknown type = " + n);
            }
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6: {
                sb.append(o);
            }
            case 7: {
                sb.append("\"").append(zzlh.zzcr(o.toString())).append("\"");
            }
            case 8: {
                sb.append("\"").append(zzky.zzi((byte[])o)).append("\"");
            }
            case 9: {
                sb.append("\"").append(zzky.zzj((byte[])o));
                sb.append("\"");
            }
            case 10: {
                zzli.zza(sb, (HashMap<String, String>)o);
            }
            case 11: {
                throw new IllegalArgumentException("Method does not accept concrete type.");
            }
        }
    }
    
    private void zza(final StringBuilder sb, final Field<?, ?> field, final Parcel parcel, final int n) {
        switch (field.zzol()) {
            default: {
                throw new IllegalArgumentException("Unknown field out type = " + field.zzol());
            }
            case 0: {
                this.zzb(sb, field, this.zza(field, com.google.android.gms.common.internal.safeparcel.zza.zzg(parcel, n)));
            }
            case 1: {
                this.zzb(sb, field, this.zza(field, com.google.android.gms.common.internal.safeparcel.zza.zzk(parcel, n)));
            }
            case 2: {
                this.zzb(sb, field, this.zza(field, com.google.android.gms.common.internal.safeparcel.zza.zzi(parcel, n)));
            }
            case 3: {
                this.zzb(sb, field, this.zza(field, com.google.android.gms.common.internal.safeparcel.zza.zzl(parcel, n)));
            }
            case 4: {
                this.zzb(sb, field, this.zza(field, com.google.android.gms.common.internal.safeparcel.zza.zzm(parcel, n)));
            }
            case 5: {
                this.zzb(sb, field, this.zza(field, com.google.android.gms.common.internal.safeparcel.zza.zzn(parcel, n)));
            }
            case 6: {
                this.zzb(sb, field, this.zza(field, com.google.android.gms.common.internal.safeparcel.zza.zzc(parcel, n)));
            }
            case 7: {
                this.zzb(sb, field, this.zza(field, com.google.android.gms.common.internal.safeparcel.zza.zzo(parcel, n)));
            }
            case 8:
            case 9: {
                this.zzb(sb, field, this.zza(field, com.google.android.gms.common.internal.safeparcel.zza.zzr(parcel, n)));
            }
            case 10: {
                this.zzb(sb, field, this.zza(field, zzh(com.google.android.gms.common.internal.safeparcel.zza.zzq(parcel, n))));
            }
            case 11: {
                throw new IllegalArgumentException("Method does not accept concrete type.");
            }
        }
    }
    
    private void zza(final StringBuilder sb, final String s, final Field<?, ?> field, final Parcel parcel, final int n) {
        sb.append("\"").append(s).append("\":");
        if (field.zzow()) {
            this.zza(sb, field, parcel, n);
            return;
        }
        this.zzb(sb, field, parcel, n);
    }
    
    private void zza(final StringBuilder sb, final Map<String, Field<?, ?>> map, final Parcel parcel) {
        final HashMap<Integer, Map.Entry<String, Field<?, ?>>> zzC = zzC(map);
        sb.append('{');
        final int zzab = com.google.android.gms.common.internal.safeparcel.zza.zzab(parcel);
        int n = 0;
        while (parcel.dataPosition() < zzab) {
            final int zzaa = com.google.android.gms.common.internal.safeparcel.zza.zzaa(parcel);
            final Map.Entry<String, V> entry = (Map.Entry<String, V>)zzC.get(com.google.android.gms.common.internal.safeparcel.zza.zzbA(zzaa));
            if (entry != null) {
                if (n != 0) {
                    sb.append(",");
                }
                this.zza(sb, entry.getKey(), (Field<?, ?>)entry.getValue(), parcel, zzaa);
                n = 1;
            }
        }
        if (parcel.dataPosition() != zzab) {
            throw new com.google.android.gms.common.internal.safeparcel.zza.zza("Overread allowed size end=" + zzab, parcel);
        }
        sb.append('}');
    }
    
    private void zzb(final StringBuilder sb, final Field<?, ?> field, Parcel zzD, int i) {
        if (field.zzor()) {
            sb.append("[");
            switch (field.zzol()) {
                default: {
                    throw new IllegalStateException("Unknown field type out.");
                }
                case 0: {
                    zzkx.zza(sb, com.google.android.gms.common.internal.safeparcel.zza.zzu(zzD, i));
                    break;
                }
                case 1: {
                    zzkx.zza(sb, com.google.android.gms.common.internal.safeparcel.zza.zzw(zzD, i));
                    break;
                }
                case 2: {
                    zzkx.zza(sb, com.google.android.gms.common.internal.safeparcel.zza.zzv(zzD, i));
                    break;
                }
                case 3: {
                    zzkx.zza(sb, com.google.android.gms.common.internal.safeparcel.zza.zzx(zzD, i));
                    break;
                }
                case 4: {
                    zzkx.zza(sb, com.google.android.gms.common.internal.safeparcel.zza.zzy(zzD, i));
                    break;
                }
                case 5: {
                    zzkx.zza(sb, com.google.android.gms.common.internal.safeparcel.zza.zzz(zzD, i));
                    break;
                }
                case 6: {
                    zzkx.zza(sb, com.google.android.gms.common.internal.safeparcel.zza.zzt(zzD, i));
                    break;
                }
                case 7: {
                    zzkx.zza(sb, com.google.android.gms.common.internal.safeparcel.zza.zzA(zzD, i));
                    break;
                }
                case 8:
                case 9:
                case 10: {
                    throw new UnsupportedOperationException("List of type BASE64, BASE64_URL_SAFE, or STRING_MAP is not supported");
                }
                case 11: {
                    final Parcel[] zzE = com.google.android.gms.common.internal.safeparcel.zza.zzE(zzD, i);
                    int length;
                    for (length = zzE.length, i = 0; i < length; ++i) {
                        if (i > 0) {
                            sb.append(",");
                        }
                        zzE[i].setDataPosition(0);
                        this.zza(sb, field.zzoy(), zzE[i]);
                    }
                    break;
                }
            }
            sb.append("]");
            return;
        }
        switch (field.zzol()) {
            default: {
                throw new IllegalStateException("Unknown field type out");
            }
            case 0: {
                sb.append(com.google.android.gms.common.internal.safeparcel.zza.zzg(zzD, i));
            }
            case 1: {
                sb.append(com.google.android.gms.common.internal.safeparcel.zza.zzk(zzD, i));
            }
            case 2: {
                sb.append(com.google.android.gms.common.internal.safeparcel.zza.zzi(zzD, i));
            }
            case 3: {
                sb.append(com.google.android.gms.common.internal.safeparcel.zza.zzl(zzD, i));
            }
            case 4: {
                sb.append(com.google.android.gms.common.internal.safeparcel.zza.zzm(zzD, i));
            }
            case 5: {
                sb.append(com.google.android.gms.common.internal.safeparcel.zza.zzn(zzD, i));
            }
            case 6: {
                sb.append(com.google.android.gms.common.internal.safeparcel.zza.zzc(zzD, i));
            }
            case 7: {
                sb.append("\"").append(zzlh.zzcr(com.google.android.gms.common.internal.safeparcel.zza.zzo(zzD, i))).append("\"");
            }
            case 8: {
                sb.append("\"").append(zzky.zzi(com.google.android.gms.common.internal.safeparcel.zza.zzr(zzD, i))).append("\"");
            }
            case 9: {
                sb.append("\"").append(zzky.zzj(com.google.android.gms.common.internal.safeparcel.zza.zzr(zzD, i)));
                sb.append("\"");
            }
            case 10: {
                final Bundle zzq = com.google.android.gms.common.internal.safeparcel.zza.zzq(zzD, i);
                final Set keySet = zzq.keySet();
                keySet.size();
                sb.append("{");
                final Iterator<String> iterator = keySet.iterator();
                i = 1;
                while (iterator.hasNext()) {
                    final String s = iterator.next();
                    if (i == 0) {
                        sb.append(",");
                    }
                    sb.append("\"").append(s).append("\"");
                    sb.append(":");
                    sb.append("\"").append(zzlh.zzcr(zzq.getString(s))).append("\"");
                    i = 0;
                }
                sb.append("}");
            }
            case 11: {
                zzD = com.google.android.gms.common.internal.safeparcel.zza.zzD(zzD, i);
                zzD.setDataPosition(0);
                this.zza(sb, field.zzoy(), zzD);
            }
        }
    }
    
    private void zzb(final StringBuilder sb, final Field<?, ?> field, final Object o) {
        if (field.zzoq()) {
            this.zzb(sb, field, (ArrayList<?>)o);
            return;
        }
        this.zza(sb, field.zzok(), o);
    }
    
    private void zzb(final StringBuilder sb, final Field<?, ?> field, final ArrayList<?> list) {
        sb.append("[");
        for (int size = list.size(), i = 0; i < size; ++i) {
            if (i != 0) {
                sb.append(",");
            }
            this.zza(sb, field.zzok(), list.get(i));
        }
        sb.append("]");
    }
    
    public static HashMap<String, String> zzh(final Bundle bundle) {
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        for (final String s : bundle.keySet()) {
            hashMap.put(s, bundle.getString(s));
        }
        return hashMap;
    }
    
    public int describeContents() {
        final zze creator = SafeParcelResponse.CREATOR;
        return 0;
    }
    
    public int getVersionCode() {
        return this.zzCY;
    }
    
    @Override
    public String toString() {
        zzu.zzb(this.zzabO, "Cannot convert to JSON on client side.");
        final Parcel zzoE = this.zzoE();
        zzoE.setDataPosition(0);
        final StringBuilder sb = new StringBuilder(100);
        this.zza(sb, this.zzabO.zzco(this.mClassName), zzoE);
        return sb.toString();
    }
    
    public void writeToParcel(final Parcel parcel, final int n) {
        final zze creator = SafeParcelResponse.CREATOR;
        zze.zza(this, parcel, n);
    }
    
    @Override
    protected Object zzck(final String s) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }
    
    @Override
    protected boolean zzcl(final String s) {
        throw new UnsupportedOperationException("Converting to JSON does not require this method.");
    }
    
    public Parcel zzoE() {
        switch (this.zzabX) {
            case 0: {
                this.zzabY = zzb.zzac(this.zzabV);
                zzb.zzH(this.zzabV, this.zzabY);
                this.zzabX = 2;
                break;
            }
            case 1: {
                zzb.zzH(this.zzabV, this.zzabY);
                this.zzabX = 2;
                break;
            }
        }
        return this.zzabV;
    }
    
    FieldMappingDictionary zzoF() {
        switch (this.zzabW) {
            default: {
                throw new IllegalStateException("Invalid creation type: " + this.zzabW);
            }
            case 0: {
                return null;
            }
            case 1: {
                return this.zzabO;
            }
            case 2: {
                return this.zzabO;
            }
        }
    }
    
    @Override
    public Map<String, Field<?, ?>> zzom() {
        if (this.zzabO == null) {
            return null;
        }
        return this.zzabO.zzco(this.mClassName);
    }
}
