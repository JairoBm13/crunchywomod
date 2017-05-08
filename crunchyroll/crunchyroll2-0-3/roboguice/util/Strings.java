// 
// Decompiled by Procyon v0.5.30
// 

package roboguice.util;

import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.InputStream;
import java.util.HashMap;
import java.security.InvalidParameterException;
import java.util.Map;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Collection;
import java.io.IOException;
import java.io.Writer;
import java.io.Reader;

public class Strings
{
    private static final int DEFAULT_BUFFER_SIZE = 4096;
    
    public static String capitalize(String string) {
        final String string2 = toString(string);
        if (string2.length() >= 2) {
            string = string2.substring(0, 1).toUpperCase() + string2.substring(1);
        }
        else {
            string = string2;
            if (string2.length() >= 1) {
                return string2.toUpperCase();
            }
        }
        return string;
    }
    
    public static String[] chunk(final String s, final int n) {
        String[] array;
        if (isEmpty(s) || n == 0) {
            array = new String[0];
        }
        else {
            final int length = s.length();
            final int n2 = (length - 1) / n + 1;
            final String[] array2 = new String[n2];
            int n3 = 0;
            while (true) {
                array = array2;
                if (n3 >= n2) {
                    break;
                }
                int n4;
                if (n3 * n + n < length) {
                    n4 = n3 * n + n;
                }
                else {
                    n4 = length;
                }
                array2[n3] = s.substring(n3 * n, n4);
                ++n3;
            }
        }
        return array;
    }
    
    public static int copy(final Reader reader, final Writer writer) {
        final long copyLarge = copyLarge(reader, writer);
        if (copyLarge > 2147483647L) {
            return -1;
        }
        return (int)copyLarge;
    }
    
    public static long copyLarge(final Reader reader, final Writer writer) throws RuntimeException {
        long n;
        try {
            final char[] array = new char[4096];
            n = 0L;
            while (true) {
                final int read = reader.read(array);
                if (-1 == read) {
                    break;
                }
                writer.write(array, 0, read);
                n += read;
            }
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return n;
    }
    
    public static boolean equals(final Object o, final Object o2) {
        return toString(o).equals(toString(o2));
    }
    
    public static boolean equalsIgnoreCase(final Object o, final Object o2) {
        return toString(o).toLowerCase().equals(toString(o2).toLowerCase());
    }
    
    public static boolean isEmpty(final Object o) {
        return toString(o).trim().length() == 0;
    }
    
    public static <T> String join(final String s, final Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
            return "";
        }
        final Iterator<T> iterator = collection.iterator();
        final StringBuilder sb = new StringBuilder(toString(iterator.next()));
        while (iterator.hasNext()) {
            final T next = iterator.next();
            if (notEmpty(next)) {
                sb.append(s).append(toString(next));
            }
        }
        return sb.toString();
    }
    
    public static <T> String join(final String s, final T... array) {
        return join(s, Arrays.asList(array));
    }
    
    public static <T> String joinAnd(final String s, final String s2, final Collection<T> collection) {
        if (collection == null || collection.isEmpty()) {
            return "";
        }
        final Iterator<T> iterator = collection.iterator();
        final StringBuilder sb = new StringBuilder(toString(iterator.next()));
        int n = 1;
        while (iterator.hasNext()) {
            final T next = iterator.next();
            if (notEmpty(next)) {
                ++n;
                String s3;
                if (n == collection.size()) {
                    s3 = s2;
                }
                else {
                    s3 = s;
                }
                sb.append(s3).append(toString(next));
            }
        }
        return sb.toString();
    }
    
    public static <T> String joinAnd(final String s, final String s2, final T... array) {
        return joinAnd(s, s2, Arrays.asList(array));
    }
    
    public static String md5(String string) {
        StringBuilder sb;
        while (true) {
            while (true) {
                int n = 0;
                Label_0114: {
                    try {
                        final byte[] digest = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
                        sb = new StringBuilder();
                        final int length = digest.length;
                        n = 0;
                        if (n < length) {
                            final String hexString = Integer.toHexString(digest[n]);
                            if (hexString.length() == 1) {
                                sb.append('0');
                                sb.append(hexString.charAt(hexString.length() - 1));
                                break Label_0114;
                            }
                            sb.append(hexString.substring(hexString.length() - 2));
                            break Label_0114;
                        }
                    }
                    catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }
                    break;
                }
                ++n;
                continue;
            }
        }
        string = sb.toString();
        return string;
    }
    
    public static String namedFormat(String replace, final Map<String, String> map) {
        for (final String s : map.keySet()) {
            replace = replace.replace('$' + s, map.get(s));
        }
        return replace;
    }
    
    public static String namedFormat(final String s, final Object... array) {
        if (array.length % 2 != 0) {
            throw new InvalidParameterException("You must include one value for each parameter");
        }
        final HashMap<String, String> hashMap = new HashMap<String, String>(array.length / 2);
        for (int i = 0; i < array.length; i += 2) {
            hashMap.put(toString(array[i]), toString(array[i + 1]));
        }
        return namedFormat(s, hashMap);
    }
    
    public static boolean notEmpty(final Object o) {
        return toString(o).trim().length() != 0;
    }
    
    public static String toString(final InputStream inputStream) {
        final StringWriter stringWriter = new StringWriter();
        copy(new InputStreamReader(inputStream), stringWriter);
        return stringWriter.toString();
    }
    
    public static String toString(final Reader reader) {
        final StringWriter stringWriter = new StringWriter();
        copy(reader, stringWriter);
        return stringWriter.toString();
    }
    
    public static String toString(final Object o) {
        return toString(o, "");
    }
    
    public static String toString(final Object o, final String s) {
        if (o == null) {
            return s;
        }
        if (o instanceof InputStream) {
            return toString((InputStream)o);
        }
        if (o instanceof Reader) {
            return toString((Reader)o);
        }
        if (o instanceof Object[]) {
            return join(", ", (Object[])o);
        }
        if (o instanceof Collection) {
            return join(", ", (Collection<Object>)o);
        }
        return o.toString();
    }
}
