// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.tagmanager;

import java.util.concurrent.CountedCompleter;
import java.lang.reflect.Array;
import java.util.Spliterator;
import java.util.NoSuchElementException;
import java.util.concurrent.locks.LockSupport;
import sun.misc.Contended;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.function.ToDoubleFunction;
import java.util.function.IntBinaryOperator;
import java.util.function.ToIntBiFunction;
import java.util.function.LongBinaryOperator;
import java.util.function.ToLongBiFunction;
import java.util.function.DoubleBinaryOperator;
import java.util.function.ToDoubleBiFunction;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Enumeration;
import java.util.function.Function;
import java.util.function.BiFunction;
import java.util.function.BiConsumer;
import java.io.ObjectInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Set;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import sun.misc.Unsafe;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.util.concurrent.ConcurrentMap;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.Iterator;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

public class DataLayer
{
    public static final Object OBJECT_NOT_PRESENT;
    static final String[] zzaLf;
    private static final Pattern zzaLg;
    private final ConcurrentHashMap<zzb, Integer> zzaLh;
    private final Map<String, Object> zzaLi;
    private final ReentrantLock zzaLj;
    private final LinkedList<Map<String, Object>> zzaLk;
    private final zzc zzaLl;
    private final CountDownLatch zzaLm;
    
    static {
        OBJECT_NOT_PRESENT = new Object();
        zzaLf = "gtm.lifetime".toString().split("\\.");
        zzaLg = Pattern.compile("(\\d+)\\s*([smhd]?)");
    }
    
    DataLayer() {
        this((zzc)new zzc() {
            @Override
            public void zza(final zzc.zza zza) {
                zza.zzo(new ArrayList<DataLayer.zza>());
            }
            
            @Override
            public void zza(final List<DataLayer.zza> list, final long n) {
            }
        });
    }
    
    DataLayer(final zzc zzaLl) {
        this.zzaLl = zzaLl;
        this.zzaLh = new ConcurrentHashMap<zzb, Integer>();
        this.zzaLi = new HashMap<String, Object>();
        this.zzaLj = new ReentrantLock();
        this.zzaLk = new LinkedList<Map<String, Object>>();
        this.zzaLm = new CountDownLatch(1);
        this.zzyy();
    }
    
    private void zzH(final Map<String, Object> map) {
        this.zzaLj.lock();
        try {
            this.zzaLk.offer(map);
            if (this.zzaLj.getHoldCount() == 1) {
                this.zzyz();
            }
            this.zzI(map);
        }
        finally {
            this.zzaLj.unlock();
        }
    }
    
    private void zzI(final Map<String, Object> map) {
        final Long zzJ = this.zzJ(map);
        if (zzJ == null) {
            return;
        }
        final List<zza> zzL = this.zzL(map);
        zzL.remove("gtm.lifetime");
        this.zzaLl.zza(zzL, zzJ);
    }
    
    private Long zzJ(final Map<String, Object> map) {
        final Object zzK = this.zzK(map);
        if (zzK == null) {
            return null;
        }
        return zzeo(zzK.toString());
    }
    
    private Object zzK(Map<String, Object> value) {
        final String[] zzaLf = DataLayer.zzaLf;
        final int length = zzaLf.length;
        int n = 0;
        Object o;
        while (true) {
            o = value;
            if (n >= length) {
                break;
            }
            final String s = zzaLf[n];
            if (!(value instanceof Map)) {
                o = null;
                break;
            }
            value = ((Map<String, Object>)value).get(s);
            ++n;
        }
        return o;
    }
    
    private List<zza> zzL(final Map<String, Object> map) {
        final ArrayList<zza> list = new ArrayList<zza>();
        this.zza(map, "", list);
        return list;
    }
    
    private void zzM(final Map<String, Object> map) {
        synchronized (this.zzaLi) {
            for (final String s : map.keySet()) {
                this.zzc(this.zzj(s, map.get(s)), this.zzaLi);
            }
        }
        // monitorexit(map2)
        final Map<String, Object> map3;
        this.zzN(map3);
    }
    
    private void zzN(final Map<String, Object> map) {
        final Iterator<zzb> iterator = this.zzaLh.keySet().iterator();
        while (iterator.hasNext()) {
            iterator.next().zzF(map);
        }
    }
    
    private void zza(final Map<String, Object> map, final String s, final Collection<zza> collection) {
        for (final Map.Entry<String, Object> entry : map.entrySet()) {
            final StringBuilder append = new StringBuilder().append(s);
            String s2;
            if (s.length() == 0) {
                s2 = "";
            }
            else {
                s2 = ".";
            }
            final String string = append.append(s2).append(entry.getKey()).toString();
            if (entry.getValue() instanceof Map) {
                this.zza(entry.getValue(), string, collection);
            }
            else {
                if (string.equals("gtm.lifetime")) {
                    continue;
                }
                collection.add(new zza(string, entry.getValue()));
            }
        }
    }
    
    static Long zzeo(final String s) {
        final Matcher matcher = DataLayer.zzaLg.matcher(s);
        if (!matcher.matches()) {
            zzbg.zzaA("unknown _lifetime: " + s);
            return null;
        }
        long long1;
        while (true) {
            try {
                long1 = Long.parseLong(matcher.group(1));
                if (long1 <= 0L) {
                    zzbg.zzaA("non-positive _lifetime: " + s);
                    return null;
                }
            }
            catch (NumberFormatException ex) {
                zzbg.zzaC("illegal number in _lifetime value: " + s);
                long1 = 0L;
                continue;
            }
            break;
        }
        final String group = matcher.group(2);
        if (group.length() == 0) {
            return long1;
        }
        switch (group.charAt(0)) {
            default: {
                zzbg.zzaC("unknown units in _lifetime: " + s);
                return null;
            }
            case 's': {
                return long1 * 1000L;
            }
            case 'm': {
                return long1 * 1000L * 60L;
            }
            case 'h': {
                return long1 * 1000L * 60L * 60L;
            }
            case 'd': {
                return long1 * 1000L * 60L * 60L * 24L;
            }
        }
    }
    
    private void zzyy() {
        this.zzaLl.zza((zzc.zza)new zzc.zza() {
            @Override
            public void zzo(final List<DataLayer.zza> list) {
                for (final DataLayer.zza zza : list) {
                    DataLayer.this.zzH(DataLayer.this.zzj(zza.zztw, zza.zzGK));
                }
                DataLayer.this.zzaLm.countDown();
            }
        });
    }
    
    private void zzyz() {
        int n = 0;
        while (true) {
            final Map<String, Object> map = this.zzaLk.poll();
            if (map == null) {
                return;
            }
            this.zzM(map);
            ++n;
            if (n > 500) {
                this.zzaLk.clear();
                throw new RuntimeException("Seems like an infinite loop of pushing to the data layer");
            }
        }
    }
    
    public void push(final Map<String, Object> map) {
        while (true) {
            try {
                this.zzaLm.await();
                this.zzH(map);
            }
            catch (InterruptedException ex) {
                zzbg.zzaC("DataLayer.push: unexpected InterruptedException");
                continue;
            }
            break;
        }
    }
    
    @Override
    public String toString() {
        synchronized (this.zzaLi) {
            final StringBuilder sb = new StringBuilder();
            for (final Map.Entry<String, Object> entry : this.zzaLi.entrySet()) {
                sb.append(String.format("{\n\tKey: %s\n\tValue: %s\n}\n", entry.getKey(), entry.getValue()));
            }
        }
        final StringBuilder sb2;
        // monitorexit(map)
        return sb2.toString();
    }
    
    void zza(final zzb zzb) {
        this.zzaLh.put(zzb, 0);
    }
    
    void zzb(final List<Object> list, final List<Object> list2) {
        while (list2.size() < list.size()) {
            list2.add(null);
        }
        for (int i = 0; i < list.size(); ++i) {
            final List<Object> value = list.get(i);
            if (value instanceof List) {
                if (!(list2.get(i) instanceof List)) {
                    list2.set(i, new ArrayList());
                }
                this.zzb(value, (List)list2.get(i));
            }
            else if (value instanceof Map) {
                if (!(list2.get(i) instanceof Map)) {
                    list2.set(i, new HashMap<String, Object>());
                }
                this.zzc((Map<String, Object>)value, list2.get(i));
            }
            else if (value != DataLayer.OBJECT_NOT_PRESENT) {
                list2.set(i, value);
            }
        }
    }
    
    void zzc(final Map<String, Object> map, final Map<String, Object> map2) {
        for (final String s : map.keySet()) {
            final ArrayList<Object> value = map.get(s);
            if (value instanceof List) {
                if (!(map2.get(s) instanceof List)) {
                    map2.put(s, new ArrayList<Object>());
                }
                this.zzb(value, map2.get(s));
            }
            else if (value instanceof Map) {
                if (!(map2.get(s) instanceof Map)) {
                    map2.put(s, new HashMap());
                }
                this.zzc((Map<String, Object>)value, (Map)map2.get(s));
            }
            else {
                map2.put(s, value);
            }
        }
    }
    
    Map<String, Object> zzj(final String s, final Object o) {
        final HashMap<String, Map<String, Object>> hashMap = (HashMap<String, Map<String, Object>>)new HashMap<String, Map<String, Map<String, Object>>>();
        final String[] split = s.toString().split("\\.");
        int i = 0;
        Map<String, Object> map = (Map<String, Object>)hashMap;
        while (i < split.length - 1) {
            final HashMap<String, Map<String, Map<String, Object>>> hashMap2 = new HashMap<String, Map<String, Map<String, Object>>>();
            map.put(split[i], hashMap2);
            ++i;
            map = (Map<String, Object>)hashMap2;
        }
        map.put(split[split.length - 1], o);
        return (Map<String, Object>)hashMap;
    }
    
    static final class zza
    {
        public final Object zzGK;
        public final String zztw;
        
        zza(final String zztw, final Object zzGK) {
            this.zztw = zztw;
            this.zzGK = zzGK;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o instanceof zza) {
                final zza zza = (zza)o;
                if (this.zztw.equals(zza.zztw) && this.zzGK.equals(zza.zzGK)) {
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return Arrays.hashCode(new Integer[] { this.zztw.hashCode(), this.zzGK.hashCode() });
        }
        
        @Override
        public String toString() {
            return "Key: " + this.zztw + " value: " + this.zzGK.toString();
        }
    }
    
    interface zzb
    {
        void zzF(final Map<String, Object> p0);
    }
    
    interface zzc
    {
        void zza(final zza p0);
        
        void zza(final List<DataLayer.zza> p0, final long p1);
        
        public interface zza
        {
            void zzo(final List<DataLayer.zza> p0);
        }
    }
}
