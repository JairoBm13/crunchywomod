// 
// Decompiled by Procyon v0.5.30
// 

package com.google.ads.interactivemedia.v3.a.d;

import java.io.EOFException;
import java.io.IOException;
import com.google.ads.interactivemedia.v3.a.b.a.d;
import com.google.ads.interactivemedia.v3.a.b.e;
import java.io.Reader;
import java.io.Closeable;

public class a implements Closeable
{
    private static final char[] a;
    private final Reader b;
    private boolean c;
    private final char[] d;
    private int e;
    private int f;
    private int g;
    private int h;
    private int i;
    private long j;
    private int k;
    private String l;
    private int[] m;
    private int n;
    
    static {
        a = ")]}'\n".toCharArray();
        e.a = new e() {
            @Override
            public void a(final a a) throws IOException {
                if (a instanceof d) {
                    ((d)a).o();
                    return;
                }
                int n;
                if ((n = a.i) == 0) {
                    n = a.o();
                }
                if (n == 13) {
                    a.i = 9;
                    return;
                }
                if (n == 12) {
                    a.i = 8;
                    return;
                }
                if (n == 14) {
                    a.i = 10;
                    return;
                }
                throw new IllegalStateException("Expected a name but was " + a.f() + " " + " at line " + a.u() + " column " + a.v());
            }
        };
    }
    
    public a(final Reader b) {
        this.c = false;
        this.d = new char[1024];
        this.e = 0;
        this.f = 0;
        this.g = 0;
        this.h = 0;
        this.i = 0;
        this.m = new int[32];
        this.n = 0;
        this.m[this.n++] = 6;
        if (b == null) {
            throw new NullPointerException("in == null");
        }
        this.b = b;
    }
    
    private void a(final int n) {
        if (this.n == this.m.length) {
            final int[] m = new int[this.n * 2];
            System.arraycopy(this.m, 0, m, 0, this.n);
            this.m = m;
        }
        this.m[this.n++] = n;
    }
    
    private boolean a(final char c) throws IOException {
        switch (c) {
            default: {
                return true;
            }
            case '#':
            case '/':
            case ';':
            case '=':
            case '\\': {
                this.w();
            }
            case '\t':
            case '\n':
            case '\f':
            case '\r':
            case ' ':
            case ',':
            case ':':
            case '[':
            case ']':
            case '{':
            case '}': {
                return false;
            }
        }
    }
    
    private boolean a(final String s) throws IOException {
        final boolean b = false;
        boolean b2;
        while (true) {
            if (this.e + s.length() > this.f) {
                b2 = b;
                if (!this.b(s.length())) {
                    break;
                }
            }
            Label_0067: {
                if (this.d[this.e] != '\n') {
                    for (int i = 0; i < s.length(); ++i) {
                        if (this.d[this.e + i] != s.charAt(i)) {
                            break Label_0067;
                        }
                    }
                    b2 = true;
                    break;
                }
                ++this.g;
                this.h = this.e + 1;
            }
            ++this.e;
        }
        return b2;
    }
    
    private int b(final boolean b) throws IOException {
        final char[] d = this.d;
        int e = this.e;
        int n = this.f;
        while (true) {
            int f = n;
            int e2 = e;
            if (e == n) {
                this.e = e;
                if (!this.b(1)) {
                    if (b) {
                        throw new EOFException("End of input at line " + this.u() + " column " + this.v());
                    }
                    return -1;
                }
                else {
                    e2 = this.e;
                    f = this.f;
                }
            }
            e = e2 + 1;
            final char c = d[e2];
            if (c == '\n') {
                ++this.g;
                this.h = e;
                n = f;
            }
            else if (c != ' ' && c != '\r') {
                if (c == '\t') {
                    n = f;
                }
                else if (c == '/') {
                    if ((this.e = e) == f) {
                        --this.e;
                        final boolean b2 = this.b(2);
                        ++this.e;
                        if (!b2) {
                            return c;
                        }
                    }
                    this.w();
                    switch (d[this.e]) {
                        default: {
                            return c;
                        }
                        case '*': {
                            ++this.e;
                            if (!this.a("*/")) {
                                throw this.b("Unterminated comment");
                            }
                            e = this.e + 2;
                            n = this.f;
                            continue;
                        }
                        case '/': {
                            ++this.e;
                            this.x();
                            e = this.e;
                            n = this.f;
                            continue;
                        }
                    }
                }
                else {
                    if (c != '#') {
                        this.e = e;
                        return c;
                    }
                    this.e = e;
                    this.w();
                    this.x();
                    e = this.e;
                    n = this.f;
                }
            }
            else {
                n = f;
            }
        }
    }
    
    private IOException b(final String s) throws IOException {
        throw new com.google.ads.interactivemedia.v3.a.d.d(s + " at line " + this.u() + " column " + this.v());
    }
    
    private String b(final char c) throws IOException {
        final char[] d = this.d;
        final StringBuilder sb = new StringBuilder();
        do {
            int e = this.e;
            int f;
            int i;
            int f2;
            for (f = this.f, i = e; i < f; f = f2) {
                final int h = i + 1;
                final char c2 = d[i];
                if (c2 == c) {
                    sb.append(d, e, (this.e = h) - e - 1);
                    return sb.toString();
                }
                int e2;
                if (c2 == '\\') {
                    sb.append(d, e, (this.e = h) - e - 1);
                    sb.append(this.y());
                    e2 = this.e;
                    f2 = this.f;
                    i = e2;
                }
                else {
                    e2 = e;
                    f2 = f;
                    i = h;
                    if (c2 == '\n') {
                        ++this.g;
                        this.h = h;
                        e2 = e;
                        f2 = f;
                        i = h;
                    }
                }
                e = e2;
            }
            sb.append(d, e, i - e);
            this.e = i;
        } while (this.b(1));
        throw this.b("Unterminated string");
    }
    
    private boolean b(int n) throws IOException {
        final boolean b = false;
        final char[] d = this.d;
        this.h -= this.e;
        if (this.f != this.e) {
            this.f -= this.e;
            System.arraycopy(d, this.e, d, 0, this.f);
        }
        else {
            this.f = 0;
        }
        this.e = 0;
        int n2;
        do {
            final int read = this.b.read(d, this.f, d.length - this.f);
            final boolean b2 = b;
            if (read == -1) {
                return b2;
            }
            this.f += read;
            n2 = n;
            if (this.g != 0) {
                continue;
            }
            n2 = n;
            if (this.h != 0) {
                continue;
            }
            n2 = n;
            if (this.f <= 0) {
                continue;
            }
            n2 = n;
            if (d[0] != '\ufeff') {
                continue;
            }
            ++this.e;
            ++this.h;
            n2 = n + 1;
        } while (this.f < (n = n2));
        return true;
    }
    
    private void c(final char c) throws IOException {
        final char[] d = this.d;
        do {
            int i = this.e;
            int f2;
            for (int f = this.f; i < f; f = f2) {
                final int h = i + 1;
                final char c2 = d[i];
                if (c2 == c) {
                    this.e = h;
                    return;
                }
                if (c2 == '\\') {
                    this.e = h;
                    this.y();
                    i = this.e;
                    f2 = this.f;
                }
                else {
                    f2 = f;
                    i = h;
                    if (c2 == '\n') {
                        ++this.g;
                        this.h = h;
                        f2 = f;
                        i = h;
                    }
                }
            }
            this.e = i;
        } while (this.b(1));
        throw this.b("Unterminated string");
    }
    
    private int o() throws IOException {
        final int n = this.m[this.n - 1];
        if (n == 1) {
            this.m[this.n - 1] = 2;
        }
        else if (n == 2) {
            switch (this.b(true)) {
                case 59: {
                    this.w();
                }
                case 44: {
                    break;
                }
                default: {
                    throw this.b("Unterminated array");
                }
                case 93: {
                    return this.i = 4;
                }
            }
        }
        else if (n == 3 || n == 5) {
            this.m[this.n - 1] = 4;
            if (n == 5) {
                switch (this.b(true)) {
                    default: {
                        throw this.b("Unterminated object");
                    }
                    case 125: {
                        return this.i = 2;
                    }
                    case 59: {
                        this.w();
                    }
                    case 44: {
                        break;
                    }
                }
            }
            final int b = this.b(true);
            switch (b) {
                default: {
                    this.w();
                    --this.e;
                    if (this.a((char)b)) {
                        return this.i = 14;
                    }
                    throw this.b("Expected name");
                }
                case 34: {
                    return this.i = 13;
                }
                case 39: {
                    this.w();
                    return this.i = 12;
                }
                case 125: {
                    if (n != 5) {
                        return this.i = 2;
                    }
                    throw this.b("Expected name");
                }
            }
        }
        else if (n == 4) {
            this.m[this.n - 1] = 5;
            switch (this.b(true)) {
                case 58: {
                    break;
                }
                default: {
                    throw this.b("Expected ':'");
                }
                case 61: {
                    this.w();
                    if ((this.e < this.f || this.b(1)) && this.d[this.e] == '>') {
                        ++this.e;
                        break;
                    }
                    break;
                }
            }
        }
        else if (n == 6) {
            if (this.c) {
                this.z();
            }
            this.m[this.n - 1] = 7;
        }
        else if (n == 7) {
            if (this.b(false) == -1) {
                return this.i = 17;
            }
            this.w();
            --this.e;
        }
        else if (n == 8) {
            throw new IllegalStateException("JsonReader is closed");
        }
        switch (this.b(true)) {
            default: {
                --this.e;
                if (this.n == 1) {
                    this.w();
                }
                int n2 = this.q();
                if (n2 != 0 || (n2 = this.r()) != 0) {
                    return n2;
                }
                if (!this.a(this.d[this.e])) {
                    throw this.b("Expected value");
                }
                this.w();
                return this.i = 10;
            }
            case 93: {
                if (n == 1) {
                    return this.i = 4;
                }
            }
            case 44:
            case 59: {
                if (n == 1 || n == 2) {
                    this.w();
                    --this.e;
                    return this.i = 7;
                }
                throw this.b("Unexpected value");
            }
            case 39: {
                this.w();
                return this.i = 8;
            }
            case 34: {
                if (this.n == 1) {
                    this.w();
                }
                return this.i = 9;
            }
            case 91: {
                return this.i = 3;
            }
            case 123: {
                return this.i = 1;
            }
        }
    }
    
    private int q() throws IOException {
        final char c = this.d[this.e];
        String s;
        String s2;
        int i;
        if (c == 't' || c == 'T') {
            s = "true";
            s2 = "TRUE";
            i = 5;
        }
        else if (c == 'f' || c == 'F') {
            s = "false";
            s2 = "FALSE";
            i = 6;
        }
        else {
            if (c != 'n' && c != 'N') {
                return 0;
            }
            s = "null";
            s2 = "NULL";
            i = 7;
        }
        final int length = s.length();
        for (int j = 1; j < length; ++j) {
            if (this.e + j >= this.f && !this.b(j + 1)) {
                return 0;
            }
            final char c2 = this.d[this.e + j];
            if (c2 != s.charAt(j) && c2 != s2.charAt(j)) {
                return 0;
            }
        }
        if ((this.e + length < this.f || this.b(length + 1)) && this.a(this.d[this.e + length])) {
            return 0;
        }
        this.e += length;
        return this.i = i;
    }
    
    private int r() throws IOException {
        final char[] d = this.d;
        int e = this.e;
        final int f = this.f;
        long j = 0L;
        int n = 0;
        boolean b = true;
        int n2 = 0;
        int k = 0;
        int n3 = f;
        while (true) {
            char c = '\0';
            Label_0212: {
                while (true) {
                    int f2 = n3;
                    int e2 = e;
                    if (e + k == n3) {
                        if (k == d.length) {
                            return 0;
                        }
                        if (!this.b(k + 1)) {
                            break;
                        }
                        e2 = this.e;
                        f2 = this.f;
                    }
                    c = d[e2 + k];
                    int n5 = 0;
                    int n6 = 0;
                    switch (c) {
                        default: {
                            if (c < '0' || c > '9') {
                                break Label_0212;
                            }
                            if (n2 == 1 || n2 == 0) {
                                j = -(c - '0');
                                final int n4 = 2;
                                n5 = n;
                                n6 = n4;
                                break;
                            }
                            if (n2 == 2) {
                                if (j == 0L) {
                                    return 0;
                                }
                                final long n7 = 10L * j - (c - '0');
                                boolean b2;
                                if (j > -922337203685477580L || (j == -922337203685477580L && n7 < j)) {
                                    b2 = true;
                                }
                                else {
                                    b2 = false;
                                }
                                final int n8 = n;
                                j = n7;
                                b &= b2;
                                n6 = n2;
                                n5 = n8;
                                break;
                            }
                            else {
                                if (n2 == 3) {
                                    final int n9 = 4;
                                    n5 = n;
                                    n6 = n9;
                                    break;
                                }
                                if (n2 == 5 || n2 == 6) {
                                    final int n10 = 7;
                                    n5 = n;
                                    n6 = n10;
                                    break;
                                }
                                final int n11 = n;
                                n6 = n2;
                                n5 = n11;
                                break;
                            }
                            break;
                        }
                        case 45: {
                            if (n2 == 0) {
                                n6 = 1;
                                n5 = 1;
                                break;
                            }
                            if (n2 == 5) {
                                final int n12 = 6;
                                n5 = n;
                                n6 = n12;
                                break;
                            }
                            return 0;
                        }
                        case 43: {
                            if (n2 == 5) {
                                final int n13 = 6;
                                n5 = n;
                                n6 = n13;
                                break;
                            }
                            return 0;
                        }
                        case 69:
                        case 101: {
                            if (n2 == 2 || n2 == 4) {
                                final int n14 = 5;
                                n5 = n;
                                n6 = n14;
                                break;
                            }
                            return 0;
                        }
                        case 46: {
                            if (n2 == 2) {
                                final int n15 = 3;
                                n5 = n;
                                n6 = n15;
                                break;
                            }
                            return 0;
                        }
                    }
                    final int n16 = k + 1;
                    final int n17 = n5;
                    n3 = f2;
                    e = e2;
                    n2 = n6;
                    n = n17;
                    k = n16;
                }
                if (n2 == 2 && b && (j != Long.MIN_VALUE || n != 0)) {
                    if (n == 0) {
                        j = -j;
                    }
                    this.j = j;
                    this.e += k;
                    return this.i = 15;
                }
                if (n2 == 2 || n2 == 4 || n2 == 7) {
                    this.k = k;
                    return this.i = 16;
                }
                return 0;
            }
            if (this.a(c)) {
                return 0;
            }
            continue;
        }
    }
    
    private String s() throws IOException {
        StringBuilder sb = null;
        int n = 0;
    Label_0188:
        while (true) {
            Block_6: {
                StringBuilder sb2;
                int n2;
                while (true) {
                    if (this.e + n < this.f) {
                        sb2 = sb;
                        n2 = n;
                        switch (this.d[this.e + n]) {
                            default: {
                                ++n;
                                continue;
                            }
                            case '#':
                            case '/':
                            case ';':
                            case '=':
                            case '\\': {
                                this.w();
                                n2 = n;
                                sb2 = sb;
                            }
                            case '\t':
                            case '\n':
                            case '\f':
                            case '\r':
                            case ' ':
                            case ',':
                            case ':':
                            case '[':
                            case ']':
                            case '{':
                            case '}': {
                                break Label_0188;
                            }
                        }
                    }
                    else if (n < this.d.length) {
                        sb2 = sb;
                        n2 = n;
                        if (this.b(n + 1)) {
                            continue;
                        }
                        break;
                    }
                    else {
                        if ((sb2 = sb) == null) {
                            sb2 = new StringBuilder();
                        }
                        sb2.append(this.d, this.e, n);
                        this.e += n;
                        if (!this.b(1)) {
                            break Block_6;
                        }
                        n = 0;
                        sb = sb2;
                    }
                }
                String string;
                if (sb2 == null) {
                    string = new String(this.d, this.e, n2);
                }
                else {
                    sb2.append(this.d, this.e, n2);
                    string = sb2.toString();
                }
                this.e += n2;
                return string;
            }
            int n2 = 0;
            continue Label_0188;
        }
    }
    
    private void t() throws IOException {
        do {
            int n = 0;
            while (this.e + n < this.f) {
                switch (this.d[this.e + n]) {
                    default: {
                        ++n;
                        continue;
                    }
                    case '#':
                    case '/':
                    case ';':
                    case '=':
                    case '\\': {
                        this.w();
                    }
                    case '\t':
                    case '\n':
                    case '\f':
                    case '\r':
                    case ' ':
                    case ',':
                    case ':':
                    case '[':
                    case ']':
                    case '{':
                    case '}': {
                        this.e += n;
                        return;
                    }
                }
            }
            this.e += n;
        } while (this.b(1));
    }
    
    private int u() {
        return this.g + 1;
    }
    
    private int v() {
        return this.e - this.h + 1;
    }
    
    private void w() throws IOException {
        if (!this.c) {
            throw this.b("Use JsonReader.setLenient(true) to accept malformed JSON");
        }
    }
    
    private void x() throws IOException {
        while (this.e < this.f || this.b(1)) {
            final char c = this.d[this.e++];
            if (c == '\n') {
                ++this.g;
                this.h = this.e;
                break;
            }
            if (c == '\r') {
                return;
            }
        }
    }
    
    private char y() throws IOException {
        if (this.e == this.f && !this.b(1)) {
            throw this.b("Unterminated escape sequence");
        }
        final char c = this.d[this.e++];
        switch (c) {
            default: {
                return c;
            }
            case 117: {
                if (this.e + 4 > this.f && !this.b(4)) {
                    throw this.b("Unterminated escape sequence");
                }
                final int e = this.e;
                char c2 = '\0';
                for (int i = e; i < e + 4; ++i) {
                    final char c3 = this.d[i];
                    final char c4 = (char)(c2 << 4);
                    if (c3 >= '0' && c3 <= '9') {
                        c2 = (char)(c4 + (c3 - '0'));
                    }
                    else if (c3 >= 'a' && c3 <= 'f') {
                        c2 = (char)(c4 + (c3 - 'a' + '\n'));
                    }
                    else {
                        if (c3 < 'A' || c3 > 'F') {
                            throw new NumberFormatException("\\u" + new String(this.d, this.e, 4));
                        }
                        c2 = (char)(c4 + (c3 - 'A' + '\n'));
                    }
                }
                this.e += 4;
                return c2;
            }
            case 116: {
                return '\t';
            }
            case 98: {
                return '\b';
            }
            case 110: {
                return '\n';
            }
            case 114: {
                return '\r';
            }
            case 102: {
                return '\f';
            }
            case 10: {
                ++this.g;
                this.h = this.e;
                return c;
            }
        }
    }
    
    private void z() throws IOException {
        this.b(true);
        --this.e;
        if (this.e + com.google.ads.interactivemedia.v3.a.d.a.a.length <= this.f || this.b(com.google.ads.interactivemedia.v3.a.d.a.a.length)) {
            for (int i = 0; i < com.google.ads.interactivemedia.v3.a.d.a.a.length; ++i) {
                if (this.d[this.e + i] != com.google.ads.interactivemedia.v3.a.d.a.a[i]) {
                    return;
                }
            }
            this.e += com.google.ads.interactivemedia.v3.a.d.a.a.length;
        }
    }
    
    public void a() throws IOException {
        int n;
        if ((n = this.i) == 0) {
            n = this.o();
        }
        if (n == 3) {
            this.a(1);
            this.i = 0;
            return;
        }
        throw new IllegalStateException("Expected BEGIN_ARRAY but was " + this.f() + " at line " + this.u() + " column " + this.v());
    }
    
    public final void a(final boolean c) {
        this.c = c;
    }
    
    public void b() throws IOException {
        int n;
        if ((n = this.i) == 0) {
            n = this.o();
        }
        if (n == 4) {
            --this.n;
            this.i = 0;
            return;
        }
        throw new IllegalStateException("Expected END_ARRAY but was " + this.f() + " at line " + this.u() + " column " + this.v());
    }
    
    public void c() throws IOException {
        int n;
        if ((n = this.i) == 0) {
            n = this.o();
        }
        if (n == 1) {
            this.a(3);
            this.i = 0;
            return;
        }
        throw new IllegalStateException("Expected BEGIN_OBJECT but was " + this.f() + " at line " + this.u() + " column " + this.v());
    }
    
    @Override
    public void close() throws IOException {
        this.i = 0;
        this.m[0] = 8;
        this.n = 1;
        this.b.close();
    }
    
    public void d() throws IOException {
        int n;
        if ((n = this.i) == 0) {
            n = this.o();
        }
        if (n == 2) {
            --this.n;
            this.i = 0;
            return;
        }
        throw new IllegalStateException("Expected END_OBJECT but was " + this.f() + " at line " + this.u() + " column " + this.v());
    }
    
    public boolean e() throws IOException {
        int n;
        if ((n = this.i) == 0) {
            n = this.o();
        }
        return n != 2 && n != 4;
    }
    
    public b f() throws IOException {
        int n;
        if ((n = this.i) == 0) {
            n = this.o();
        }
        switch (n) {
            default: {
                throw new AssertionError();
            }
            case 1: {
                return com.google.ads.interactivemedia.v3.a.d.b.c;
            }
            case 2: {
                return com.google.ads.interactivemedia.v3.a.d.b.d;
            }
            case 3: {
                return com.google.ads.interactivemedia.v3.a.d.b.a;
            }
            case 4: {
                return com.google.ads.interactivemedia.v3.a.d.b.b;
            }
            case 12:
            case 13:
            case 14: {
                return com.google.ads.interactivemedia.v3.a.d.b.e;
            }
            case 5:
            case 6: {
                return com.google.ads.interactivemedia.v3.a.d.b.h;
            }
            case 7: {
                return com.google.ads.interactivemedia.v3.a.d.b.i;
            }
            case 8:
            case 9:
            case 10:
            case 11: {
                return com.google.ads.interactivemedia.v3.a.d.b.f;
            }
            case 15:
            case 16: {
                return com.google.ads.interactivemedia.v3.a.d.b.g;
            }
            case 17: {
                return com.google.ads.interactivemedia.v3.a.d.b.j;
            }
        }
    }
    
    public String g() throws IOException {
        int n;
        if ((n = this.i) == 0) {
            n = this.o();
        }
        String s;
        if (n == 14) {
            s = this.s();
        }
        else if (n == 12) {
            s = this.b('\'');
        }
        else {
            if (n != 13) {
                throw new IllegalStateException("Expected a name but was " + this.f() + " at line " + this.u() + " column " + this.v());
            }
            s = this.b('\"');
        }
        this.i = 0;
        return s;
    }
    
    public String h() throws IOException {
        int n;
        if ((n = this.i) == 0) {
            n = this.o();
        }
        String s;
        if (n == 10) {
            s = this.s();
        }
        else if (n == 8) {
            s = this.b('\'');
        }
        else if (n == 9) {
            s = this.b('\"');
        }
        else if (n == 11) {
            s = this.l;
            this.l = null;
        }
        else if (n == 15) {
            s = Long.toString(this.j);
        }
        else {
            if (n != 16) {
                throw new IllegalStateException("Expected a string but was " + this.f() + " at line " + this.u() + " column " + this.v());
            }
            s = new String(this.d, this.e, this.k);
            this.e += this.k;
        }
        this.i = 0;
        return s;
    }
    
    public boolean i() throws IOException {
        int n;
        if ((n = this.i) == 0) {
            n = this.o();
        }
        if (n == 5) {
            this.i = 0;
            return true;
        }
        if (n == 6) {
            this.i = 0;
            return false;
        }
        throw new IllegalStateException("Expected a boolean but was " + this.f() + " at line " + this.u() + " column " + this.v());
    }
    
    public void j() throws IOException {
        int n;
        if ((n = this.i) == 0) {
            n = this.o();
        }
        if (n == 7) {
            this.i = 0;
            return;
        }
        throw new IllegalStateException("Expected null but was " + this.f() + " at line " + this.u() + " column " + this.v());
    }
    
    public double k() throws IOException {
        int n;
        if ((n = this.i) == 0) {
            n = this.o();
        }
        if (n == 15) {
            this.i = 0;
            return this.j;
        }
        if (n == 16) {
            this.l = new String(this.d, this.e, this.k);
            this.e += this.k;
        }
        else if (n == 8 || n == 9) {
            char c;
            if (n == 8) {
                c = '\'';
            }
            else {
                c = '\"';
            }
            this.l = this.b(c);
        }
        else if (n == 10) {
            this.l = this.s();
        }
        else if (n != 11) {
            throw new IllegalStateException("Expected a double but was " + this.f() + " at line " + this.u() + " column " + this.v());
        }
        this.i = 11;
        final double double1 = Double.parseDouble(this.l);
        if (!this.c && (Double.isNaN(double1) || Double.isInfinite(double1))) {
            throw new com.google.ads.interactivemedia.v3.a.d.d("JSON forbids NaN and infinities: " + double1 + " at line " + this.u() + " column " + this.v());
        }
        this.l = null;
        this.i = 0;
        return double1;
    }
    
    public long l() throws IOException {
        int n;
        if ((n = this.i) == 0) {
            n = this.o();
        }
        if (n == 15) {
            this.i = 0;
            return this.j;
        }
        Label_0081: {
            if (n == 16) {
                this.l = new String(this.d, this.e, this.k);
                this.e += this.k;
            }
            else {
                if (n == 8 || n == 9) {
                    while (true) {
                        Label_0217: {
                            if (n != 8) {
                                break Label_0217;
                            }
                            final char c = '\'';
                            this.l = this.b(c);
                            try {
                                final long long1 = Long.parseLong(this.l);
                                this.i = 0;
                                return long1;
                            }
                            catch (NumberFormatException ex) {
                                break Label_0081;
                            }
                        }
                        final char c = '\"';
                        continue;
                    }
                }
                throw new IllegalStateException("Expected a long but was " + this.f() + " at line " + this.u() + " column " + this.v());
            }
        }
        this.i = 11;
        final double double1 = Double.parseDouble(this.l);
        final long n2 = (long)double1;
        if (n2 != double1) {
            throw new NumberFormatException("Expected a long but was " + this.l + " at line " + this.u() + " column " + this.v());
        }
        this.l = null;
        this.i = 0;
        return n2;
    }
    
    public int m() throws IOException {
        int n;
        if ((n = this.i) == 0) {
            n = this.o();
        }
        if (n == 15) {
            final int n2 = (int)this.j;
            if (this.j != n2) {
                throw new NumberFormatException("Expected an int but was " + this.j + " at line " + this.u() + " column " + this.v());
            }
            this.i = 0;
            return n2;
        }
        else {
            Label_0152: {
                if (n == 16) {
                    this.l = new String(this.d, this.e, this.k);
                    this.e += this.k;
                }
                else {
                    if (n == 8 || n == 9) {
                        while (true) {
                            Label_0288: {
                                if (n != 8) {
                                    break Label_0288;
                                }
                                final char c = '\'';
                                this.l = this.b(c);
                                try {
                                    final int int1 = Integer.parseInt(this.l);
                                    this.i = 0;
                                    return int1;
                                }
                                catch (NumberFormatException ex) {
                                    break Label_0152;
                                }
                            }
                            final char c = '\"';
                            continue;
                        }
                    }
                    throw new IllegalStateException("Expected an int but was " + this.f() + " at line " + this.u() + " column " + this.v());
                }
            }
            this.i = 11;
            final double double1 = Double.parseDouble(this.l);
            final int n3 = (int)double1;
            if (n3 != double1) {
                throw new NumberFormatException("Expected an int but was " + this.l + " at line " + this.u() + " column " + this.v());
            }
            this.l = null;
            this.i = 0;
            return n3;
        }
    }
    
    public void n() throws IOException {
        int n = 0;
        int n3;
        do {
            int n2;
            if ((n2 = this.i) == 0) {
                n2 = this.o();
            }
            if (n2 == 3) {
                this.a(1);
                n3 = n + 1;
            }
            else if (n2 == 1) {
                this.a(3);
                n3 = n + 1;
            }
            else if (n2 == 4) {
                --this.n;
                n3 = n - 1;
            }
            else if (n2 == 2) {
                --this.n;
                n3 = n - 1;
            }
            else if (n2 == 14 || n2 == 10) {
                this.t();
                n3 = n;
            }
            else if (n2 == 8 || n2 == 12) {
                this.c('\'');
                n3 = n;
            }
            else if (n2 == 9 || n2 == 13) {
                this.c('\"');
                n3 = n;
            }
            else {
                n3 = n;
                if (n2 == 16) {
                    this.e += this.k;
                    n3 = n;
                }
            }
            this.i = 0;
        } while ((n = n3) != 0);
    }
    
    public final boolean p() {
        return this.c;
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " at line " + this.u() + " column " + this.v();
    }
}
