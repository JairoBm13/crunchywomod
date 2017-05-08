// 
// Decompiled by Procyon v0.5.30
// 

package oauth.signpost.http;

import java.util.Collection;
import java.util.TreeSet;
import oauth.signpost.OAuth;
import java.util.Set;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.SortedSet;
import java.util.Map;
import java.io.Serializable;

public class HttpParameters implements Serializable, Map<String, SortedSet<String>>
{
    private TreeMap<String, SortedSet<String>> wrappedMap;
    
    public HttpParameters() {
        this.wrappedMap = new TreeMap<String, SortedSet<String>>();
    }
    
    @Override
    public void clear() {
        this.wrappedMap.clear();
    }
    
    @Override
    public boolean containsKey(final Object o) {
        return this.wrappedMap.containsKey(o);
    }
    
    @Override
    public boolean containsValue(final Object o) {
        final Iterator<SortedSet<String>> iterator = this.wrappedMap.values().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().contains(o)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Set<Entry<String, SortedSet<String>>> entrySet() {
        return this.wrappedMap.entrySet();
    }
    
    @Override
    public SortedSet<String> get(final Object o) {
        return this.wrappedMap.get(o);
    }
    
    public String getAsHeaderElement(final String s) {
        final String first = this.getFirst(s);
        if (first == null) {
            return null;
        }
        return s + "=\"" + first + "\"";
    }
    
    public String getAsQueryString(final Object o, final boolean b) {
        final StringBuilder sb = new StringBuilder();
        Object percentEncode = o;
        if (b) {
            percentEncode = OAuth.percentEncode((String)o);
        }
        final SortedSet<String> set = this.wrappedMap.get(percentEncode);
        if (set == null) {
            return percentEncode + "=";
        }
        final Iterator<Object> iterator = set.iterator();
        while (iterator.hasNext()) {
            sb.append(percentEncode + "=" + iterator.next());
            if (iterator.hasNext()) {
                sb.append("&");
            }
        }
        return sb.toString();
    }
    
    public String getFirst(final Object o) {
        return this.getFirst(o, false);
    }
    
    public String getFirst(final Object o, final boolean b) {
        final SortedSet<String> set = this.wrappedMap.get(o);
        String s;
        if (set == null || set.isEmpty()) {
            s = null;
        }
        else {
            s = set.first();
            if (b) {
                return OAuth.percentDecode(s);
            }
        }
        return s;
    }
    
    public HttpParameters getOAuthParameters() {
        final HttpParameters httpParameters = new HttpParameters();
        for (final Entry<String, SortedSet<String>> entry : this.entrySet()) {
            final String s = entry.getKey();
            if (s.startsWith("oauth_") || s.startsWith("x_oauth_")) {
                httpParameters.put(s, (SortedSet<String>)entry.getValue());
            }
        }
        return httpParameters;
    }
    
    @Override
    public boolean isEmpty() {
        return this.wrappedMap.isEmpty();
    }
    
    @Override
    public Set<String> keySet() {
        return this.wrappedMap.keySet();
    }
    
    public String put(final String s, final String s2) {
        return this.put(s, s2, false);
    }
    
    public String put(String percentEncode, final String s, final boolean b) {
        String percentEncode2 = percentEncode;
        if (b) {
            percentEncode2 = OAuth.percentEncode(percentEncode);
        }
        SortedSet<String> set;
        if ((set = this.wrappedMap.get(percentEncode2)) == null) {
            set = new TreeSet<String>();
            this.wrappedMap.put(percentEncode2, set);
        }
        if ((percentEncode = s) != null) {
            percentEncode = s;
            if (b) {
                percentEncode = OAuth.percentEncode(s);
            }
            set.add(percentEncode);
        }
        return percentEncode;
    }
    
    @Override
    public SortedSet<String> put(final String s, final SortedSet<String> set) {
        return this.wrappedMap.put(s, set);
    }
    
    public SortedSet<String> put(final String s, final SortedSet<String> set, final boolean b) {
        if (b) {
            this.remove((Object)s);
            final Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()) {
                this.put(s, iterator.next(), true);
            }
            return this.get((Object)s);
        }
        return this.wrappedMap.put(s, set);
    }
    
    @Override
    public void putAll(final Map<? extends String, ? extends SortedSet<String>> map) {
        this.wrappedMap.putAll(map);
    }
    
    public void putAll(final Map<? extends String, ? extends SortedSet<String>> map, final boolean b) {
        if (b) {
            for (final String s : map.keySet()) {
                this.put(s, (SortedSet<String>)map.get(s), true);
            }
        }
        else {
            this.wrappedMap.putAll(map);
        }
    }
    
    @Override
    public SortedSet<String> remove(final Object o) {
        return this.wrappedMap.remove(o);
    }
    
    @Override
    public int size() {
        int n = 0;
        final Iterator<String> iterator = this.wrappedMap.keySet().iterator();
        while (iterator.hasNext()) {
            n += this.wrappedMap.get(iterator.next()).size();
        }
        return n;
    }
    
    @Override
    public Collection<SortedSet<String>> values() {
        return this.wrappedMap.values();
    }
}
