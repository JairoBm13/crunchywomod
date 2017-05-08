// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.sym;

import com.fasterxml.jackson.core.util.InternCache;
import java.util.Arrays;

public final class CharsToNameCanonicalizer
{
    static final CharsToNameCanonicalizer sBootstrapSymbolTable;
    protected Bucket[] _buckets;
    protected final boolean _canonicalize;
    protected boolean _dirty;
    private final int _hashSeed;
    protected int _indexMask;
    protected final boolean _intern;
    protected int _longestCollisionList;
    protected CharsToNameCanonicalizer _parent;
    protected int _size;
    protected int _sizeThreshold;
    protected String[] _symbols;
    
    static {
        sBootstrapSymbolTable = new CharsToNameCanonicalizer();
    }
    
    private CharsToNameCanonicalizer() {
        this._canonicalize = true;
        this._intern = true;
        this._dirty = true;
        this._hashSeed = 0;
        this._longestCollisionList = 0;
        this.initTables(64);
    }
    
    private CharsToNameCanonicalizer(final CharsToNameCanonicalizer parent, final boolean canonicalize, final boolean intern, final String[] symbols, final Bucket[] buckets, int length, final int hashSeed, final int longestCollisionList) {
        this._parent = parent;
        this._canonicalize = canonicalize;
        this._intern = intern;
        this._symbols = symbols;
        this._buckets = buckets;
        this._size = length;
        this._hashSeed = hashSeed;
        length = symbols.length;
        this._sizeThreshold = _thresholdSize(length);
        this._indexMask = length - 1;
        this._longestCollisionList = longestCollisionList;
        this._dirty = false;
    }
    
    private static int _thresholdSize(final int n) {
        return n - (n >> 2);
    }
    
    private void copyArrays() {
        final String[] symbols = this._symbols;
        final int length = symbols.length;
        System.arraycopy(symbols, 0, this._symbols = new String[length], 0, length);
        final Bucket[] buckets = this._buckets;
        final int length2 = buckets.length;
        System.arraycopy(buckets, 0, this._buckets = new Bucket[length2], 0, length2);
    }
    
    public static CharsToNameCanonicalizer createRoot() {
        final long currentTimeMillis = System.currentTimeMillis();
        return createRoot((int)(currentTimeMillis >>> 32) + (int)currentTimeMillis | 0x1);
    }
    
    protected static CharsToNameCanonicalizer createRoot(final int n) {
        return CharsToNameCanonicalizer.sBootstrapSymbolTable.makeOrphan(n);
    }
    
    private void initTables(final int n) {
        this._symbols = new String[n];
        this._buckets = new Bucket[n >> 1];
        this._indexMask = n - 1;
        this._size = 0;
        this._longestCollisionList = 0;
        this._sizeThreshold = _thresholdSize(n);
    }
    
    private CharsToNameCanonicalizer makeOrphan(final int n) {
        return new CharsToNameCanonicalizer(null, true, true, this._symbols, this._buckets, this._size, n, this._longestCollisionList);
    }
    
    private void mergeChild(final CharsToNameCanonicalizer charsToNameCanonicalizer) {
        if (charsToNameCanonicalizer.size() > 12000 || charsToNameCanonicalizer._longestCollisionList > 63) {
            synchronized (this) {
                this.initTables(64);
                this._dirty = false;
                return;
            }
        }
        if (charsToNameCanonicalizer.size() > this.size()) {
            synchronized (this) {
                this._symbols = charsToNameCanonicalizer._symbols;
                this._buckets = charsToNameCanonicalizer._buckets;
                this._size = charsToNameCanonicalizer._size;
                this._sizeThreshold = charsToNameCanonicalizer._sizeThreshold;
                this._indexMask = charsToNameCanonicalizer._indexMask;
                this._longestCollisionList = charsToNameCanonicalizer._longestCollisionList;
                this._dirty = false;
            }
        }
    }
    
    private void rehash() {
        final int length = this._symbols.length;
        final int n = length + length;
        if (n > 65536) {
            this._size = 0;
            Arrays.fill(this._symbols, null);
            Arrays.fill(this._buckets, null);
            this._dirty = true;
        }
        else {
            final String[] symbols = this._symbols;
            final Bucket[] buckets = this._buckets;
            this._symbols = new String[n];
            this._buckets = new Bucket[n >> 1];
            this._indexMask = n - 1;
            this._sizeThreshold = _thresholdSize(n);
            int i = 0;
            int max = 0;
            int n2 = 0;
            while (i < length) {
                final String s = symbols[i];
                int max2 = max;
                int n3 = n2;
                if (s != null) {
                    n3 = n2 + 1;
                    final int hashToIndex = this._hashToIndex(this.calcHash(s));
                    if (this._symbols[hashToIndex] == null) {
                        this._symbols[hashToIndex] = s;
                        max2 = max;
                    }
                    else {
                        final int n4 = hashToIndex >> 1;
                        final Bucket bucket = new Bucket(s, this._buckets[n4]);
                        this._buckets[n4] = bucket;
                        max2 = Math.max(max, bucket.length());
                    }
                }
                ++i;
                max = max2;
                n2 = n3;
            }
            final int n5 = 0;
            int n6 = n2;
            for (int j = n5; j < length >> 1; ++j) {
                for (Bucket next = buckets[j]; next != null; next = next.getNext()) {
                    ++n6;
                    final String symbol = next.getSymbol();
                    final int hashToIndex2 = this._hashToIndex(this.calcHash(symbol));
                    if (this._symbols[hashToIndex2] == null) {
                        this._symbols[hashToIndex2] = symbol;
                    }
                    else {
                        final int n7 = hashToIndex2 >> 1;
                        final Bucket bucket2 = new Bucket(symbol, this._buckets[n7]);
                        this._buckets[n7] = bucket2;
                        max = Math.max(max, bucket2.length());
                    }
                }
            }
            this._longestCollisionList = max;
            if (n6 != this._size) {
                throw new Error("Internal error on SymbolTable.rehash(): had " + this._size + " entries; now have " + n6 + ".");
            }
        }
    }
    
    public int _hashToIndex(final int n) {
        return (n >>> 15) + n & this._indexMask;
    }
    
    public int calcHash(final String s) {
        final int length = s.length();
        int hashSeed = this._hashSeed;
        char char1;
        for (int i = 0; i < length; ++i, hashSeed = char1 + hashSeed * 33) {
            char1 = s.charAt(i);
        }
        int n;
        if ((n = hashSeed) == 0) {
            n = 1;
        }
        return n;
    }
    
    public int calcHash(final char[] array, int hashSeed, int n) {
        hashSeed = this._hashSeed;
        char c;
        for (int i = 0; i < n; ++i, hashSeed = c + hashSeed * 33) {
            c = array[i];
        }
        if ((n = hashSeed) == 0) {
            n = 1;
        }
        return n;
    }
    
    public String findSymbol(final char[] array, int n, final int n2, int hashToIndex) {
        String s;
        if (n2 < 1) {
            s = "";
        }
        else {
            if (!this._canonicalize) {
                return new String(array, n, n2);
            }
            final int hashToIndex2 = this._hashToIndex(hashToIndex);
            final String s2 = this._symbols[hashToIndex2];
            if (s2 != null) {
                if (s2.length() == n2) {
                    hashToIndex = 0;
                    while (s2.charAt(hashToIndex) == array[n + hashToIndex]) {
                        final int n3 = hashToIndex + 1;
                        if ((hashToIndex = n3) >= n2) {
                            hashToIndex = n3;
                            break;
                        }
                    }
                    if (hashToIndex == n2) {
                        return s2;
                    }
                }
                final Bucket bucket = this._buckets[hashToIndex2 >> 1];
                if (bucket != null) {
                    final String find = bucket.find(array, n, n2);
                    if (find != null) {
                        return find;
                    }
                }
            }
            if (!this._dirty) {
                this.copyArrays();
                this._dirty = true;
                hashToIndex = hashToIndex2;
            }
            else if (this._size >= this._sizeThreshold) {
                this.rehash();
                hashToIndex = this._hashToIndex(this.calcHash(array, n, n2));
            }
            else {
                hashToIndex = hashToIndex2;
            }
            String intern;
            final String s3 = intern = new String(array, n, n2);
            if (this._intern) {
                intern = InternCache.instance.intern(s3);
            }
            ++this._size;
            if (this._symbols[hashToIndex] == null) {
                return this._symbols[hashToIndex] = intern;
            }
            n = hashToIndex >> 1;
            final Bucket bucket2 = new Bucket(intern, this._buckets[n]);
            this._buckets[n] = bucket2;
            this._longestCollisionList = Math.max(bucket2.length(), this._longestCollisionList);
            s = intern;
            if (this._longestCollisionList > 255) {
                this.reportTooManyCollisions(255);
                return intern;
            }
        }
        return s;
    }
    
    public int hashSeed() {
        return this._hashSeed;
    }
    
    public CharsToNameCanonicalizer makeChild(final boolean b, final boolean b2) {
        synchronized (this) {
            final String[] symbols = this._symbols;
            final Bucket[] buckets = this._buckets;
            final int size = this._size;
            final int hashSeed = this._hashSeed;
            final int longestCollisionList = this._longestCollisionList;
            // monitorexit(this)
            return new CharsToNameCanonicalizer(this, b, b2, symbols, buckets, size, hashSeed, longestCollisionList);
        }
    }
    
    public boolean maybeDirty() {
        return this._dirty;
    }
    
    public void release() {
        if (this.maybeDirty() && this._parent != null) {
            this._parent.mergeChild(this);
            this._dirty = false;
        }
    }
    
    protected void reportTooManyCollisions(final int n) {
        throw new IllegalStateException("Longest collision chain in symbol table (of size " + this._size + ") now exceeds maximum, " + n + " -- suspect a DoS attack based on hash collisions");
    }
    
    public int size() {
        return this._size;
    }
    
    static final class Bucket
    {
        private final int _length;
        private final Bucket _next;
        private final String _symbol;
        
        public Bucket(final String symbol, final Bucket next) {
            this._symbol = symbol;
            this._next = next;
            int length;
            if (next == null) {
                length = 1;
            }
            else {
                length = next._length + 1;
            }
            this._length = length;
        }
        
        public String find(final char[] array, final int n, final int n2) {
            String s = this._symbol;
            Bucket bucket = this._next;
            while (true) {
                if (s.length() == n2) {
                    int n3 = 0;
                    while (s.charAt(n3) == array[n + n3]) {
                        final int n4 = n3 + 1;
                        if ((n3 = n4) >= n2) {
                            n3 = n4;
                            break;
                        }
                    }
                    if (n3 == n2) {
                        return s;
                    }
                }
                if (bucket == null) {
                    return null;
                }
                s = bucket.getSymbol();
                bucket = bucket.getNext();
            }
        }
        
        public Bucket getNext() {
            return this._next;
        }
        
        public String getSymbol() {
            return this._symbol;
        }
        
        public int length() {
            return this._length;
        }
    }
}
