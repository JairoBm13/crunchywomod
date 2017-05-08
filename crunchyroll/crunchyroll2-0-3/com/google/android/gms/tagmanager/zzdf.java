// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.tagmanager;

import java.util.Set;
import java.util.Iterator;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import com.google.android.gms.internal.zzag;
import java.util.Map;

public class zzdf
{
    private static Map<Object, Object> zzaOA;
    private static zzag.zza zzaOB;
    private static final Object zzaOt;
    private static Long zzaOu;
    private static Double zzaOv;
    private static zzde zzaOw;
    private static String zzaOx;
    private static Boolean zzaOy;
    private static List<Object> zzaOz;
    
    static {
        zzaOt = null;
        zzdf.zzaOu = new Long(0L);
        zzdf.zzaOv = new Double(0.0);
        zzdf.zzaOw = zzde.zzT(0L);
        zzdf.zzaOx = new String("");
        zzdf.zzaOy = new Boolean(false);
        zzdf.zzaOz = new ArrayList<Object>(0);
        zzdf.zzaOA = new HashMap<Object, Object>();
        zzdf.zzaOB = zzI(zzdf.zzaOx);
    }
    
    public static String zzD(final Object o) {
        if (o == null) {
            return zzdf.zzaOx;
        }
        return o.toString();
    }
    
    public static Boolean zzH(final Object o) {
        if (o instanceof Boolean) {
            return (Boolean)o;
        }
        return zzeN(zzD(o));
    }
    
    public static zzag.zza zzI(final Object o) {
        boolean zzjb = false;
        final zzag.zza zza = new zzag.zza();
        if (o instanceof zzag.zza) {
            return (zzag.zza)o;
        }
        if (o instanceof String) {
            zza.type = 1;
            zza.zziR = (String)o;
        }
        else if (o instanceof List) {
            zza.type = 2;
            final List list = (List)o;
            final ArrayList list2 = new ArrayList<zzag.zza>(list.size());
            final Iterator<Object> iterator = list.iterator();
            zzjb = false;
            while (iterator.hasNext()) {
                final zzag.zza zzI = zzI(iterator.next());
                if (zzI == zzdf.zzaOB) {
                    return zzdf.zzaOB;
                }
                zzjb = (zzjb || zzI.zzjb);
                list2.add(zzI);
            }
            zza.zziS = list2.toArray(new zzag.zza[0]);
        }
        else if (o instanceof Map) {
            zza.type = 3;
            final Set<Map.Entry<Object, V>> entrySet = (Set<Map.Entry<Object, V>>)((Map)o).entrySet();
            final ArrayList list3 = new ArrayList<zzag.zza>(entrySet.size());
            final ArrayList list4 = new ArrayList<zzag.zza>(entrySet.size());
            final Iterator<Map.Entry<Object, V>> iterator2 = entrySet.iterator();
            zzjb = false;
            while (iterator2.hasNext()) {
                final Map.Entry<Object, V> entry = iterator2.next();
                final zzag.zza zzI2 = zzI(entry.getKey());
                final zzag.zza zzI3 = zzI(entry.getValue());
                if (zzI2 == zzdf.zzaOB || zzI3 == zzdf.zzaOB) {
                    return zzdf.zzaOB;
                }
                zzjb = (zzjb || zzI2.zzjb || zzI3.zzjb);
                list3.add(zzI2);
                list4.add(zzI3);
            }
            zza.zziT = list3.toArray(new zzag.zza[0]);
            zza.zziU = list4.toArray(new zzag.zza[0]);
        }
        else if (zzJ(o)) {
            zza.type = 1;
            zza.zziR = o.toString();
        }
        else if (zzK(o)) {
            zza.type = 6;
            zza.zziX = zzL(o);
        }
        else {
            if (!(o instanceof Boolean)) {
                final StringBuilder append = new StringBuilder().append("Converting to Value from unknown object type: ");
                String string;
                if (o == null) {
                    string = "null";
                }
                else {
                    string = o.getClass().toString();
                }
                zzbg.zzaz(append.append(string).toString());
                return zzdf.zzaOB;
            }
            zza.type = 8;
            zza.zziY = (boolean)o;
        }
        zza.zzjb = zzjb;
        return zza;
    }
    
    private static boolean zzJ(final Object o) {
        return o instanceof Double || o instanceof Float || (o instanceof zzde && ((zzde)o).zzzF());
    }
    
    private static boolean zzK(final Object o) {
        return o instanceof Byte || o instanceof Short || o instanceof Integer || o instanceof Long || (o instanceof zzde && ((zzde)o).zzzG());
    }
    
    private static long zzL(final Object o) {
        if (o instanceof Number) {
            return ((Number)o).longValue();
        }
        zzbg.zzaz("getInt64 received non-Number");
        return 0L;
    }
    
    private static Boolean zzeN(final String s) {
        if ("true".equalsIgnoreCase(s)) {
            return Boolean.TRUE;
        }
        if ("false".equalsIgnoreCase(s)) {
            return Boolean.FALSE;
        }
        return zzdf.zzaOy;
    }
    
    public static String zzg(final zzag.zza zza) {
        return zzD(zzl(zza));
    }
    
    public static Boolean zzk(final zzag.zza zza) {
        return zzH(zzl(zza));
    }
    
    public static Object zzl(final zzag.zza zza) {
        final int n = 0;
        final int n2 = 0;
        int i = 0;
        if (zza == null) {
            return zzdf.zzaOt;
        }
        switch (zza.type) {
            default: {
                zzbg.zzaz("Failed to convert a value of type: " + zza.type);
                return zzdf.zzaOt;
            }
            case 1: {
                return zza.zziR;
            }
            case 2: {
                final ArrayList<Object> list = new ArrayList<Object>(zza.zziS.length);
                for (zzag.zza[] zziS = zza.zziS; i < zziS.length; ++i) {
                    final Object zzl = zzl(zziS[i]);
                    if (zzl == zzdf.zzaOt) {
                        return zzdf.zzaOt;
                    }
                    list.add(zzl);
                }
                return list;
            }
            case 3: {
                if (zza.zziT.length != zza.zziU.length) {
                    zzbg.zzaz("Converting an invalid value to object: " + zza.toString());
                    return zzdf.zzaOt;
                }
                final HashMap<Object, Object> hashMap = new HashMap<Object, Object>(zza.zziU.length);
                for (int j = n; j < zza.zziT.length; ++j) {
                    final Object zzl2 = zzl(zza.zziT[j]);
                    final Object zzl3 = zzl(zza.zziU[j]);
                    if (zzl2 == zzdf.zzaOt || zzl3 == zzdf.zzaOt) {
                        return zzdf.zzaOt;
                    }
                    hashMap.put(zzl2, zzl3);
                }
                return hashMap;
            }
            case 4: {
                zzbg.zzaz("Trying to convert a macro reference to object");
                return zzdf.zzaOt;
            }
            case 5: {
                zzbg.zzaz("Trying to convert a function id to object");
                return zzdf.zzaOt;
            }
            case 6: {
                return zza.zziX;
            }
            case 7: {
                final StringBuffer sb = new StringBuffer();
                final zzag.zza[] zziZ = zza.zziZ;
                for (int length = zziZ.length, k = n2; k < length; ++k) {
                    final String zzg = zzg(zziZ[k]);
                    if (zzg == zzdf.zzaOx) {
                        return zzdf.zzaOt;
                    }
                    sb.append(zzg);
                }
                return sb.toString();
            }
            case 8: {
                return zza.zziY;
            }
        }
    }
    
    public static zzag.zza zzzQ() {
        return zzdf.zzaOB;
    }
}
