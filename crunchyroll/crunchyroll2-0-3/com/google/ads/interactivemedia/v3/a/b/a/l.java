// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a.b.a;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.google.ads.interactivemedia.v3.a.o;
import com.google.ads.interactivemedia.v3.a.i;
import com.google.ads.interactivemedia.v3.a.n;
import com.google.ads.interactivemedia.v3.a.q;
import java.util.StringTokenizer;
import java.util.GregorianCalendar;
import java.util.Date;
import java.sql.Timestamp;
import java.net.URISyntaxException;
import com.google.ads.interactivemedia.v3.a.m;
import com.google.ads.interactivemedia.v3.a.b.f;
import com.google.ads.interactivemedia.v3.a.t;
import com.google.ads.interactivemedia.v3.a.d.c;
import java.io.IOException;
import com.google.ads.interactivemedia.v3.a.d.b;
import com.google.ads.interactivemedia.v3.a.d.a;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.BitSet;
import java.util.Locale;
import java.util.Calendar;
import java.util.UUID;
import java.net.InetAddress;
import java.net.URI;
import java.net.URL;
import com.google.ads.interactivemedia.v3.a.x;
import com.google.ads.interactivemedia.v3.a.w;

public final class l
{
    public static final w<StringBuffer> A;
    public static final x B;
    public static final w<URL> C;
    public static final x D;
    public static final w<URI> E;
    public static final x F;
    public static final w<InetAddress> G;
    public static final x H;
    public static final w<UUID> I;
    public static final x J;
    public static final x K;
    public static final w<Calendar> L;
    public static final x M;
    public static final w<Locale> N;
    public static final x O;
    public static final w<com.google.ads.interactivemedia.v3.a.l> P;
    public static final x Q;
    public static final x R;
    public static final w<Class> a;
    public static final x b;
    public static final w<BitSet> c;
    public static final x d;
    public static final w<Boolean> e;
    public static final w<Boolean> f;
    public static final x g;
    public static final w<Number> h;
    public static final x i;
    public static final w<Number> j;
    public static final x k;
    public static final w<Number> l;
    public static final x m;
    public static final w<Number> n;
    public static final w<Number> o;
    public static final w<Number> p;
    public static final w<Number> q;
    public static final x r;
    public static final w<Character> s;
    public static final x t;
    public static final w<String> u;
    public static final w<BigDecimal> v;
    public static final w<BigInteger> w;
    public static final x x;
    public static final w<StringBuilder> y;
    public static final x z;
    
    static {
        a = new w<Class>() {
            public Class a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
                if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
                    a.j();
                    return null;
                }
                throw new UnsupportedOperationException("Attempted to deserialize a java.lang.Class. Forgot to register a type adapter?");
            }
            
            @Override
            public void a(final c c, final Class clazz) throws IOException {
                if (clazz == null) {
                    c.f();
                    return;
                }
                throw new UnsupportedOperationException("Attempted to serialize java.lang.Class: " + clazz.getName() + ". Forgot to register a type adapter?");
            }
        };
        b = a(Class.class, com.google.ads.interactivemedia.v3.a.b.a.l.a);
        c = new w<BitSet>() {
            public BitSet a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
                if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
                    a.j();
                    return null;
                }
                final BitSet set = new BitSet();
                a.a();
                b b = a.f();
                int n = 0;
            Label_0214:
                while (b != com.google.ads.interactivemedia.v3.a.d.b.b) {
                    int i = 0;
                    switch (l$26.a[b.ordinal()]) {
                        default: {
                            throw new t("Invalid bitset value type: " + b);
                        }
                        case 1: {
                            if (a.m() != 0) {
                                i = 1;
                                break;
                            }
                            i = 0;
                            break;
                        }
                        case 2: {
                            i = (a.i() ? 1 : 0);
                            break;
                        }
                        case 3: {
                            final String h = a.h();
                            try {
                                if (Integer.parseInt(h) != 0) {
                                    i = 1;
                                    break;
                                }
                                i = 0;
                                break;
                            }
                            catch (NumberFormatException ex) {
                                throw new t("Error: Expecting: bitset number value (1, 0), Found: " + h);
                            }
                            break Label_0214;
                        }
                    }
                    if (i != 0) {
                        set.set(n);
                    }
                    ++n;
                    b = a.f();
                }
                a.b();
                return set;
            }
            
            @Override
            public void a(final c c, final BitSet set) throws IOException {
                if (set == null) {
                    c.f();
                    return;
                }
                c.b();
                for (int i = 0; i < set.length(); ++i) {
                    boolean b;
                    if (set.get(i)) {
                        b = true;
                    }
                    else {
                        b = false;
                    }
                    c.a((long)(b ? 1 : 0));
                }
                c.c();
            }
        };
        d = a(BitSet.class, com.google.ads.interactivemedia.v3.a.b.a.l.c);
        e = new w<Boolean>() {
            public Boolean a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
                if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
                    a.j();
                    return null;
                }
                if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.f) {
                    return Boolean.parseBoolean(a.h());
                }
                return a.i();
            }
            
            @Override
            public void a(final c c, final Boolean b) throws IOException {
                if (b == null) {
                    c.f();
                    return;
                }
                c.a(b);
            }
        };
        f = new w<Boolean>() {
            public Boolean a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
                if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
                    a.j();
                    return null;
                }
                return Boolean.valueOf(a.h());
            }
            
            @Override
            public void a(final c c, final Boolean b) throws IOException {
                String string;
                if (b == null) {
                    string = "null";
                }
                else {
                    string = b.toString();
                }
                c.b(string);
            }
        };
        g = a(Boolean.TYPE, Boolean.class, com.google.ads.interactivemedia.v3.a.b.a.l.e);
        h = new w<Number>() {
            public Number a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
                if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
                    a.j();
                    return null;
                }
                try {
                    return (byte)a.m();
                }
                catch (NumberFormatException ex) {
                    throw new t(ex);
                }
            }
            
            @Override
            public void a(final c c, final Number n) throws IOException {
                c.a(n);
            }
        };
        i = a(Byte.TYPE, Byte.class, com.google.ads.interactivemedia.v3.a.b.a.l.h);
        j = new w<Number>() {
            public Number a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
                if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
                    a.j();
                    return null;
                }
                try {
                    return (short)a.m();
                }
                catch (NumberFormatException ex) {
                    throw new t(ex);
                }
            }
            
            @Override
            public void a(final c c, final Number n) throws IOException {
                c.a(n);
            }
        };
        k = a(Short.TYPE, Short.class, com.google.ads.interactivemedia.v3.a.b.a.l.j);
        l = new w<Number>() {
            public Number a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
                if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
                    a.j();
                    return null;
                }
                try {
                    return a.m();
                }
                catch (NumberFormatException ex) {
                    throw new t(ex);
                }
            }
            
            @Override
            public void a(final c c, final Number n) throws IOException {
                c.a(n);
            }
        };
        m = a(Integer.TYPE, Integer.class, com.google.ads.interactivemedia.v3.a.b.a.l.l);
        n = new w<Number>() {
            public Number a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
                if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
                    a.j();
                    return null;
                }
                try {
                    return a.l();
                }
                catch (NumberFormatException ex) {
                    throw new t(ex);
                }
            }
            
            @Override
            public void a(final c c, final Number n) throws IOException {
                c.a(n);
            }
        };
        o = new w<Number>() {
            public Number a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
                if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
                    a.j();
                    return null;
                }
                return (float)a.k();
            }
            
            @Override
            public void a(final c c, final Number n) throws IOException {
                c.a(n);
            }
        };
        p = new w<Number>() {
            public Number a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
                if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
                    a.j();
                    return null;
                }
                return a.k();
            }
            
            @Override
            public void a(final c c, final Number n) throws IOException {
                c.a(n);
            }
        };
        q = new w<Number>() {
            public Number a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
                final b f = a.f();
                switch (l$26.a[f.ordinal()]) {
                    default: {
                        throw new t("Expecting number, got: " + f);
                    }
                    case 4: {
                        a.j();
                        return null;
                    }
                    case 1: {
                        return new f(a.h());
                    }
                }
            }
            
            @Override
            public void a(final c c, final Number n) throws IOException {
                c.a(n);
            }
        };
        r = a(Number.class, com.google.ads.interactivemedia.v3.a.b.a.l.q);
        s = new w<Character>() {
            public Character a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
                if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
                    a.j();
                    return null;
                }
                final String h = a.h();
                if (h.length() != 1) {
                    throw new t("Expecting character, got: " + h);
                }
                return h.charAt(0);
            }
            
            @Override
            public void a(final c c, final Character c2) throws IOException {
                String value;
                if (c2 == null) {
                    value = null;
                }
                else {
                    value = String.valueOf(c2);
                }
                c.b(value);
            }
        };
        t = a(Character.TYPE, Character.class, com.google.ads.interactivemedia.v3.a.b.a.l.s);
        u = new w<String>() {
            public String a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
                final b f = a.f();
                if (f == com.google.ads.interactivemedia.v3.a.d.b.i) {
                    a.j();
                    return null;
                }
                if (f == com.google.ads.interactivemedia.v3.a.d.b.h) {
                    return Boolean.toString(a.i());
                }
                return a.h();
            }
            
            @Override
            public void a(final c c, final String s) throws IOException {
                c.b(s);
            }
        };
        v = new w<BigDecimal>() {
            public BigDecimal a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
                if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
                    a.j();
                    return null;
                }
                try {
                    return new BigDecimal(a.h());
                }
                catch (NumberFormatException ex) {
                    throw new t(ex);
                }
            }
            
            @Override
            public void a(final c c, final BigDecimal bigDecimal) throws IOException {
                c.a(bigDecimal);
            }
        };
        w = new w<BigInteger>() {
            public BigInteger a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
                if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
                    a.j();
                    return null;
                }
                try {
                    return new BigInteger(a.h());
                }
                catch (NumberFormatException ex) {
                    throw new t(ex);
                }
            }
            
            @Override
            public void a(final c c, final BigInteger bigInteger) throws IOException {
                c.a(bigInteger);
            }
        };
        x = a(String.class, com.google.ads.interactivemedia.v3.a.b.a.l.u);
        y = new w<StringBuilder>() {
            public StringBuilder a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
                if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
                    a.j();
                    return null;
                }
                return new StringBuilder(a.h());
            }
            
            @Override
            public void a(final c c, final StringBuilder sb) throws IOException {
                String string;
                if (sb == null) {
                    string = null;
                }
                else {
                    string = sb.toString();
                }
                c.b(string);
            }
        };
        z = a(StringBuilder.class, com.google.ads.interactivemedia.v3.a.b.a.l.y);
        A = new w<StringBuffer>() {
            public StringBuffer a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
                if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
                    a.j();
                    return null;
                }
                return new StringBuffer(a.h());
            }
            
            @Override
            public void a(final c c, final StringBuffer sb) throws IOException {
                String string;
                if (sb == null) {
                    string = null;
                }
                else {
                    string = sb.toString();
                }
                c.b(string);
            }
        };
        B = a(StringBuffer.class, com.google.ads.interactivemedia.v3.a.b.a.l.A);
        C = new w<URL>() {
            public URL a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
                if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
                    a.j();
                }
                else {
                    final String h = a.h();
                    if (!"null".equals(h)) {
                        return new URL(h);
                    }
                }
                return null;
            }
            
            @Override
            public void a(final c c, final URL url) throws IOException {
                String externalForm;
                if (url == null) {
                    externalForm = null;
                }
                else {
                    externalForm = url.toExternalForm();
                }
                c.b(externalForm);
            }
        };
        D = a(URL.class, com.google.ads.interactivemedia.v3.a.b.a.l.C);
        E = new w<URI>() {
            public URI a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
                if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
                    a.j();
                }
                else {
                    try {
                        final String h = a.h();
                        if (!"null".equals(h)) {
                            return new URI(h);
                        }
                    }
                    catch (URISyntaxException ex) {
                        throw new m(ex);
                    }
                }
                return null;
            }
            
            @Override
            public void a(final c c, final URI uri) throws IOException {
                String asciiString;
                if (uri == null) {
                    asciiString = null;
                }
                else {
                    asciiString = uri.toASCIIString();
                }
                c.b(asciiString);
            }
        };
        F = a(URI.class, com.google.ads.interactivemedia.v3.a.b.a.l.E);
        G = new w<InetAddress>() {
            public InetAddress a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
                if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
                    a.j();
                    return null;
                }
                return InetAddress.getByName(a.h());
            }
            
            @Override
            public void a(final c c, final InetAddress inetAddress) throws IOException {
                String hostAddress;
                if (inetAddress == null) {
                    hostAddress = null;
                }
                else {
                    hostAddress = inetAddress.getHostAddress();
                }
                c.b(hostAddress);
            }
        };
        H = b(InetAddress.class, com.google.ads.interactivemedia.v3.a.b.a.l.G);
        I = new w<UUID>() {
            public UUID a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
                if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
                    a.j();
                    return null;
                }
                return UUID.fromString(a.h());
            }
            
            @Override
            public void a(final c c, final UUID uuid) throws IOException {
                String string;
                if (uuid == null) {
                    string = null;
                }
                else {
                    string = uuid.toString();
                }
                c.b(string);
            }
        };
        J = a(UUID.class, com.google.ads.interactivemedia.v3.a.b.a.l.I);
        K = new x() {
            @Override
            public <T> w<T> a(final com.google.ads.interactivemedia.v3.a.f f, final com.google.ads.interactivemedia.v3.a.c.a<T> a) {
                if (a.a() != Timestamp.class) {
                    return null;
                }
                return (w<T>)new w<Timestamp>() {
                    final /* synthetic */ w a = f.a(Date.class);
                    
                    public Timestamp a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
                        final Date date = this.a.b(a);
                        if (date != null) {
                            return new Timestamp(date.getTime());
                        }
                        return null;
                    }
                    
                    @Override
                    public void a(final c c, final Timestamp timestamp) throws IOException {
                        this.a.a(c, timestamp);
                    }
                };
            }
        };
        L = new w<Calendar>() {
            public Calendar a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
                int n = 0;
                if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
                    a.j();
                    return null;
                }
                a.c();
                int n2 = 0;
                int n3 = 0;
                int n4 = 0;
                int n5 = 0;
                int n6 = 0;
                while (a.f() != com.google.ads.interactivemedia.v3.a.d.b.d) {
                    final String g = a.g();
                    final int m = a.m();
                    if ("year".equals(g)) {
                        n6 = m;
                    }
                    else if ("month".equals(g)) {
                        n5 = m;
                    }
                    else if ("dayOfMonth".equals(g)) {
                        n4 = m;
                    }
                    else if ("hourOfDay".equals(g)) {
                        n3 = m;
                    }
                    else if ("minute".equals(g)) {
                        n2 = m;
                    }
                    else {
                        if (!"second".equals(g)) {
                            continue;
                        }
                        n = m;
                    }
                }
                a.d();
                return new GregorianCalendar(n6, n5, n4, n3, n2, n);
            }
            
            @Override
            public void a(final c c, final Calendar calendar) throws IOException {
                if (calendar == null) {
                    c.f();
                    return;
                }
                c.d();
                c.a("year");
                c.a((long)calendar.get(1));
                c.a("month");
                c.a((long)calendar.get(2));
                c.a("dayOfMonth");
                c.a((long)calendar.get(5));
                c.a("hourOfDay");
                c.a((long)calendar.get(11));
                c.a("minute");
                c.a((long)calendar.get(12));
                c.a("second");
                c.a((long)calendar.get(13));
                c.e();
            }
        };
        M = b(Calendar.class, GregorianCalendar.class, com.google.ads.interactivemedia.v3.a.b.a.l.L);
        N = new w<Locale>() {
            public Locale a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
                if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
                    a.j();
                    return null;
                }
                final StringTokenizer stringTokenizer = new StringTokenizer(a.h(), "_");
                String nextToken;
                if (stringTokenizer.hasMoreElements()) {
                    nextToken = stringTokenizer.nextToken();
                }
                else {
                    nextToken = null;
                }
                String nextToken2;
                if (stringTokenizer.hasMoreElements()) {
                    nextToken2 = stringTokenizer.nextToken();
                }
                else {
                    nextToken2 = null;
                }
                String nextToken3;
                if (stringTokenizer.hasMoreElements()) {
                    nextToken3 = stringTokenizer.nextToken();
                }
                else {
                    nextToken3 = null;
                }
                if (nextToken2 == null && nextToken3 == null) {
                    return new Locale(nextToken);
                }
                if (nextToken3 == null) {
                    return new Locale(nextToken, nextToken2);
                }
                return new Locale(nextToken, nextToken2, nextToken3);
            }
            
            @Override
            public void a(final c c, final Locale locale) throws IOException {
                String string;
                if (locale == null) {
                    string = null;
                }
                else {
                    string = locale.toString();
                }
                c.b(string);
            }
        };
        O = a(Locale.class, com.google.ads.interactivemedia.v3.a.b.a.l.N);
        P = new w<com.google.ads.interactivemedia.v3.a.l>() {
            public com.google.ads.interactivemedia.v3.a.l a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
                switch (l$26.a[a.f().ordinal()]) {
                    default: {
                        throw new IllegalArgumentException();
                    }
                    case 3: {
                        return new q(a.h());
                    }
                    case 1: {
                        return new q(new f(a.h()));
                    }
                    case 2: {
                        return new q(Boolean.valueOf(a.i()));
                    }
                    case 4: {
                        a.j();
                        return com.google.ads.interactivemedia.v3.a.n.a;
                    }
                    case 5: {
                        final i i = new i();
                        a.a();
                        while (a.e()) {
                            i.a(this.a(a));
                        }
                        a.b();
                        return i;
                    }
                    case 6: {
                        final o o = new o();
                        a.c();
                        while (a.e()) {
                            o.a(a.g(), this.a(a));
                        }
                        a.d();
                        return o;
                    }
                }
            }
            
            @Override
            public void a(final c c, final com.google.ads.interactivemedia.v3.a.l l) throws IOException {
                if (l == null || l.j()) {
                    c.f();
                    return;
                }
                if (l.i()) {
                    final q m = l.m();
                    if (m.p()) {
                        c.a(m.a());
                        return;
                    }
                    if (m.o()) {
                        c.a(m.f());
                        return;
                    }
                    c.b(m.b());
                }
                else {
                    if (l.g()) {
                        c.b();
                        final Iterator<com.google.ads.interactivemedia.v3.a.l> iterator = l.l().iterator();
                        while (iterator.hasNext()) {
                            this.a(c, iterator.next());
                        }
                        c.c();
                        return;
                    }
                    if (l.h()) {
                        c.d();
                        for (final Map.Entry<String, com.google.ads.interactivemedia.v3.a.l> entry : l.k().o()) {
                            c.a(entry.getKey());
                            this.a(c, entry.getValue());
                        }
                        c.e();
                        return;
                    }
                    throw new IllegalArgumentException("Couldn't write " + l.getClass());
                }
            }
        };
        Q = b(com.google.ads.interactivemedia.v3.a.l.class, com.google.ads.interactivemedia.v3.a.b.a.l.P);
        R = a();
    }
    
    public static x a() {
        return new x() {
            @Override
            public <T> w<T> a(final com.google.ads.interactivemedia.v3.a.f f, final com.google.ads.interactivemedia.v3.a.c.a<T> a) {
                final Class<? super T> a2 = a.a();
                if (!Enum.class.isAssignableFrom(a2) || a2 == Enum.class) {
                    return null;
                }
                Class<? super T> superclass = a2;
                if (!a2.isEnum()) {
                    superclass = a2.getSuperclass();
                }
                return new a<T>((Class<T>)superclass);
            }
        };
    }
    
    public static <TT> x a(final com.google.ads.interactivemedia.v3.a.c.a<TT> a, final w<TT> w) {
        return new x() {
            @Override
            public <T> w<T> a(final com.google.ads.interactivemedia.v3.a.f f, final com.google.ads.interactivemedia.v3.a.c.a<T> a) {
                if (a.equals(a)) {
                    return (w<T>)w;
                }
                return null;
            }
        };
    }
    
    public static <TT> x a(final Class<TT> clazz, final w<TT> w) {
        return new x() {
            @Override
            public <T> w<T> a(final com.google.ads.interactivemedia.v3.a.f f, final com.google.ads.interactivemedia.v3.a.c.a<T> a) {
                if (a.a() == clazz) {
                    return (w<T>)w;
                }
                return null;
            }
            
            @Override
            public String toString() {
                return "Factory[type=" + clazz.getName() + ",adapter=" + w + "]";
            }
        };
    }
    
    public static <TT> x a(final Class<TT> clazz, final Class<TT> clazz2, final w<? super TT> w) {
        return new x() {
            @Override
            public <T> w<T> a(final com.google.ads.interactivemedia.v3.a.f f, final com.google.ads.interactivemedia.v3.a.c.a<T> a) {
                final Class<? super T> a2 = a.a();
                if (a2 == clazz || a2 == clazz2) {
                    return (w<T>)w;
                }
                return null;
            }
            
            @Override
            public String toString() {
                return "Factory[type=" + clazz2.getName() + "+" + clazz.getName() + ",adapter=" + w + "]";
            }
        };
    }
    
    public static <TT> x b(final Class<TT> clazz, final w<TT> w) {
        return new x() {
            @Override
            public <T> w<T> a(final com.google.ads.interactivemedia.v3.a.f f, final com.google.ads.interactivemedia.v3.a.c.a<T> a) {
                if (clazz.isAssignableFrom(a.a())) {
                    return (w<T>)w;
                }
                return null;
            }
            
            @Override
            public String toString() {
                return "Factory[typeHierarchy=" + clazz.getName() + ",adapter=" + w + "]";
            }
        };
    }
    
    public static <TT> x b(final Class<TT> clazz, final Class<? extends TT> clazz2, final w<? super TT> w) {
        return new x() {
            @Override
            public <T> w<T> a(final com.google.ads.interactivemedia.v3.a.f f, final com.google.ads.interactivemedia.v3.a.c.a<T> a) {
                final Class<? super T> a2 = a.a();
                if (a2 == clazz || a2 == clazz2) {
                    return (w<T>)w;
                }
                return null;
            }
            
            @Override
            public String toString() {
                return "Factory[type=" + clazz.getName() + "+" + clazz2.getName() + ",adapter=" + w + "]";
            }
        };
    }
    
    private static final class a<T extends Enum<T>> extends w<T>
    {
        private final Map<String, T> a;
        private final Map<T, String> b;
        
        public a(final Class<T> clazz) {
            while (true) {
                this.a = new HashMap<String, T>();
                this.b = new HashMap<T, String>();
                while (true) {
                    Label_0134: {
                        try {
                            final T[] array = clazz.getEnumConstants();
                            for (int length = array.length, i = 0; i < length; ++i) {
                                final Enum<T> enum1 = array[i];
                                String s = enum1.name();
                                final com.google.ads.interactivemedia.v3.a.a.b b = clazz.getField(s).getAnnotation(com.google.ads.interactivemedia.v3.a.a.b.class);
                                if (b == null) {
                                    break Label_0134;
                                }
                                s = b.a();
                                this.a.put(s, (T)enum1);
                                this.b.put((T)enum1, s);
                            }
                        }
                        catch (NoSuchFieldException ex) {
                            throw new AssertionError();
                        }
                        break;
                    }
                    continue;
                }
            }
        }
        
        public T a(final com.google.ads.interactivemedia.v3.a.d.a a) throws IOException {
            if (a.f() == com.google.ads.interactivemedia.v3.a.d.b.i) {
                a.j();
                return null;
            }
            return this.a.get(a.h());
        }
        
        @Override
        public void a(final c c, final T t) throws IOException {
            String s;
            if (t == null) {
                s = null;
            }
            else {
                s = this.b.get(t);
            }
            c.b(s);
        }
    }
}
