// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.common.server.response;

import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import android.os.Parcel;
import com.google.android.gms.common.server.converter.ConverterWrapper;
import com.google.android.gms.common.internal.safeparcel.SafeParcelable;
import com.google.android.gms.common.internal.zzu;
import java.util.Iterator;
import java.util.Map;
import com.google.android.gms.internal.zzli;
import com.google.android.gms.internal.zzky;
import java.util.HashMap;
import java.util.ArrayList;
import com.google.android.gms.internal.zzlh;

public abstract class FastJsonResponse
{
    private void zza(final StringBuilder sb, final Field field, final Object o) {
        if (field.zzok() == 11) {
            sb.append(((FastJsonResponse)field.zzou().cast(o)).toString());
            return;
        }
        if (field.zzok() == 7) {
            sb.append("\"");
            sb.append(zzlh.zzcr((String)o));
            sb.append("\"");
            return;
        }
        sb.append(o);
    }
    
    private void zza(final StringBuilder sb, final Field field, final ArrayList<Object> list) {
        sb.append("[");
        for (int i = 0; i < list.size(); ++i) {
            if (i > 0) {
                sb.append(",");
            }
            final Object value = list.get(i);
            if (value != null) {
                this.zza(sb, field, value);
            }
        }
        sb.append("]");
    }
    
    @Override
    public String toString() {
        final Map<String, Field<?, ?>> zzom = this.zzom();
        final StringBuilder sb = new StringBuilder(100);
        for (final String s : zzom.keySet()) {
            final Field<Object, Object> field = zzom.get(s);
            if (this.zza(field)) {
                final HashMap<String, String> zza = this.zza((Field<HashMap<String, String>, Object>)field, this.zzb(field));
                if (sb.length() == 0) {
                    sb.append("{");
                }
                else {
                    sb.append(",");
                }
                sb.append("\"").append(s).append("\":");
                if (zza == null) {
                    sb.append("null");
                }
                else {
                    switch (field.zzol()) {
                        default: {
                            if (field.zzoq()) {
                                this.zza(sb, field, (ArrayList<Object>)zza);
                                continue;
                            }
                            this.zza(sb, field, zza);
                            continue;
                        }
                        case 8: {
                            sb.append("\"").append(zzky.zzi((byte[])(Object)zza)).append("\"");
                            continue;
                        }
                        case 9: {
                            sb.append("\"").append(zzky.zzj((byte[])(Object)zza)).append("\"");
                            continue;
                        }
                        case 10: {
                            zzli.zza(sb, zza);
                            continue;
                        }
                    }
                }
            }
        }
        if (sb.length() > 0) {
            sb.append("}");
        }
        else {
            sb.append("{}");
        }
        return sb.toString();
    }
    
    protected <O, I> I zza(final Field<I, O> field, final Object o) {
        Object convertBack = o;
        if (((Field<Object, Object>)field).zzabP != null) {
            convertBack = field.convertBack(o);
        }
        return (I)convertBack;
    }
    
    protected boolean zza(final Field field) {
        if (field.zzol() != 11) {
            return this.zzcl(field.zzos());
        }
        if (field.zzor()) {
            return this.zzcn(field.zzos());
        }
        return this.zzcm(field.zzos());
    }
    
    protected Object zzb(final Field field) {
        final String zzos = field.zzos();
        if (field.zzou() != null) {
            zzu.zza(this.zzck(field.zzos()) == null, "Concrete field shouldn't be value object: %s", field.zzos());
            HashMap<String, Object> hashMap;
            if (field.zzor()) {
                hashMap = this.zzoo();
            }
            else {
                hashMap = this.zzon();
            }
            if (hashMap != null) {
                return hashMap.get(zzos);
            }
            try {
                return this.getClass().getMethod("get" + Character.toUpperCase(zzos.charAt(0)) + zzos.substring(1), (Class<?>[])new Class[0]).invoke(this, new Object[0]);
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        return this.zzck(field.zzos());
    }
    
    protected abstract Object zzck(final String p0);
    
    protected abstract boolean zzcl(final String p0);
    
    protected boolean zzcm(final String s) {
        throw new UnsupportedOperationException("Concrete types not supported");
    }
    
    protected boolean zzcn(final String s) {
        throw new UnsupportedOperationException("Concrete type arrays not supported");
    }
    
    public abstract Map<String, Field<?, ?>> zzom();
    
    public HashMap<String, Object> zzon() {
        return null;
    }
    
    public HashMap<String, Object> zzoo() {
        return null;
    }
    
    public static class Field<I, O> implements SafeParcelable
    {
        public static final com.google.android.gms.common.server.response.zza CREATOR;
        private final int zzCY;
        protected final int zzabG;
        protected final boolean zzabH;
        protected final int zzabI;
        protected final boolean zzabJ;
        protected final String zzabK;
        protected final int zzabL;
        protected final Class<? extends FastJsonResponse> zzabM;
        protected final String zzabN;
        private FieldMappingDictionary zzabO;
        private zza<I, O> zzabP;
        
        static {
            CREATOR = new com.google.android.gms.common.server.response.zza();
        }
        
        Field(final int zzCY, final int zzabG, final boolean zzabH, final int zzabI, final boolean zzabJ, final String zzabK, final int zzabL, final String zzabN, final ConverterWrapper converterWrapper) {
            this.zzCY = zzCY;
            this.zzabG = zzabG;
            this.zzabH = zzabH;
            this.zzabI = zzabI;
            this.zzabJ = zzabJ;
            this.zzabK = zzabK;
            this.zzabL = zzabL;
            if (zzabN == null) {
                this.zzabM = null;
                this.zzabN = null;
            }
            else {
                this.zzabM = SafeParcelResponse.class;
                this.zzabN = zzabN;
            }
            if (converterWrapper == null) {
                this.zzabP = null;
                return;
            }
            this.zzabP = (zza<I, O>)converterWrapper.zzoi();
        }
        
        public I convertBack(final O o) {
            return this.zzabP.convertBack(o);
        }
        
        public int describeContents() {
            final com.google.android.gms.common.server.response.zza creator = Field.CREATOR;
            return 0;
        }
        
        public int getVersionCode() {
            return this.zzCY;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("Field\n");
            sb.append("            versionCode=").append(this.zzCY).append('\n');
            sb.append("                 typeIn=").append(this.zzabG).append('\n');
            sb.append("            typeInArray=").append(this.zzabH).append('\n');
            sb.append("                typeOut=").append(this.zzabI).append('\n');
            sb.append("           typeOutArray=").append(this.zzabJ).append('\n');
            sb.append("        outputFieldName=").append(this.zzabK).append('\n');
            sb.append("      safeParcelFieldId=").append(this.zzabL).append('\n');
            sb.append("       concreteTypeName=").append(this.zzov()).append('\n');
            if (this.zzou() != null) {
                sb.append("     concreteType.class=").append(this.zzou().getCanonicalName()).append('\n');
            }
            final StringBuilder append = sb.append("          converterName=");
            String canonicalName;
            if (this.zzabP == null) {
                canonicalName = "null";
            }
            else {
                canonicalName = this.zzabP.getClass().getCanonicalName();
            }
            append.append(canonicalName).append('\n');
            return sb.toString();
        }
        
        public void writeToParcel(final Parcel parcel, final int n) {
            final com.google.android.gms.common.server.response.zza creator = Field.CREATOR;
            com.google.android.gms.common.server.response.zza.zza(this, parcel, n);
        }
        
        public void zza(final FieldMappingDictionary zzabO) {
            this.zzabO = zzabO;
        }
        
        public int zzok() {
            return this.zzabG;
        }
        
        public int zzol() {
            return this.zzabI;
        }
        
        public boolean zzoq() {
            return this.zzabH;
        }
        
        public boolean zzor() {
            return this.zzabJ;
        }
        
        public String zzos() {
            return this.zzabK;
        }
        
        public int zzot() {
            return this.zzabL;
        }
        
        public Class<? extends FastJsonResponse> zzou() {
            return this.zzabM;
        }
        
        String zzov() {
            if (this.zzabN == null) {
                return null;
            }
            return this.zzabN;
        }
        
        public boolean zzow() {
            return this.zzabP != null;
        }
        
        ConverterWrapper zzox() {
            if (this.zzabP == null) {
                return null;
            }
            return ConverterWrapper.zza(this.zzabP);
        }
        
        public Map<String, Field<?, ?>> zzoy() {
            zzu.zzu(this.zzabN);
            zzu.zzu(this.zzabO);
            return this.zzabO.zzco(this.zzabN);
        }
    }
    
    public interface zza<I, O>
    {
        I convertBack(final O p0);
    }
}
