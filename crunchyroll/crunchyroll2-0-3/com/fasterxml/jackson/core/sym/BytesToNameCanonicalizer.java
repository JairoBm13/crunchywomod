// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.sym;

import com.fasterxml.jackson.core.util.InternCache;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReference;

public final class BytesToNameCanonicalizer
{
    protected int _collCount;
    protected int _collEnd;
    protected Bucket[] _collList;
    private boolean _collListShared;
    protected int _count;
    private final int _hashSeed;
    protected final boolean _intern;
    protected int _longestCollisionList;
    protected int[] _mainHash;
    protected int _mainHashMask;
    private boolean _mainHashShared;
    protected Name[] _mainNames;
    private boolean _mainNamesShared;
    private transient boolean _needRehash;
    protected final BytesToNameCanonicalizer _parent;
    protected final AtomicReference<TableInfo> _tableInfo;
    
    private BytesToNameCanonicalizer(final int n, final boolean intern, int i) {
        final int n2 = 16;
        this._parent = null;
        this._hashSeed = i;
        this._intern = intern;
        if (n < 16) {
            i = 16;
        }
        else {
            i = n;
            if ((n - 1 & n) != 0x0) {
                for (i = n2; i < n; i += i) {}
            }
        }
        this._tableInfo = new AtomicReference<TableInfo>(this.initTableInfo(i));
    }
    
    private void _addSymbol(int n, final Name name) {
        if (this._mainHashShared) {
            this.unshareMain();
        }
        if (this._needRehash) {
            this.rehash();
        }
        ++this._count;
        final int n2 = n & this._mainHashMask;
        if (this._mainNames[n2] == null) {
            this._mainHash[n2] = n << 8;
            if (this._mainNamesShared) {
                this.unshareNames();
            }
            this._mainNames[n2] = name;
        }
        else {
            if (this._collListShared) {
                this.unshareCollision();
            }
            ++this._collCount;
            final int n3 = this._mainHash[n2];
            n = (n3 & 0xFF);
            if (n == 0) {
                if (this._collEnd <= 254) {
                    final int collEnd = this._collEnd;
                    ++this._collEnd;
                    if ((n = collEnd) >= this._collList.length) {
                        this.expandCollision();
                        n = collEnd;
                    }
                }
                else {
                    n = this.findBestBucket();
                }
                this._mainHash[n2] = ((n3 & 0xFFFFFF00) | n + 1);
            }
            else {
                --n;
            }
            final Bucket bucket = new Bucket(name, this._collList[n]);
            this._collList[n] = bucket;
            this._longestCollisionList = Math.max(bucket.length(), this._longestCollisionList);
            if (this._longestCollisionList > 255) {
                this.reportTooManyCollisions(255);
            }
        }
        n = this._mainHash.length;
        if (this._count > n >> 1) {
            final int n4 = n >> 2;
            if (this._count > n - n4) {
                this._needRehash = true;
            }
            else if (this._collCount >= n4) {
                this._needRehash = true;
            }
        }
    }
    
    private static Name constructName(final int n, final String s, final int[] array, final int n2) {
        if (n2 < 4) {
            switch (n2) {
                case 1: {
                    return new Name1(s, n, array[0]);
                }
                case 2: {
                    return new Name2(s, n, array[0], array[1]);
                }
                case 3: {
                    return new Name3(s, n, array[0], array[1], array[2]);
                }
            }
        }
        final int[] array2 = new int[n2];
        for (int i = 0; i < n2; ++i) {
            array2[i] = array[i];
        }
        return new NameN(s, n, array2, n2);
    }
    
    public static BytesToNameCanonicalizer createRoot() {
        final long currentTimeMillis = System.currentTimeMillis();
        return createRoot((int)(currentTimeMillis >>> 32) + (int)currentTimeMillis | 0x1);
    }
    
    protected static BytesToNameCanonicalizer createRoot(final int n) {
        return new BytesToNameCanonicalizer(64, true, n);
    }
    
    private void expandCollision() {
        final Bucket[] collList = this._collList;
        final int length = collList.length;
        System.arraycopy(collList, 0, this._collList = new Bucket[length + length], 0, length);
    }
    
    private int findBestBucket() {
        final Bucket[] collList = this._collList;
        int n = Integer.MAX_VALUE;
        int n2 = -1;
        for (int i = 0; i < this._collEnd; ++i) {
            final int length = collList[i].length();
            if (length < n) {
                if (length == 1) {
                    return i;
                }
                n2 = i;
                n = length;
            }
        }
        return n2;
    }
    
    public static Name getEmptyName() {
        return Name1.getEmptyName();
    }
    
    private TableInfo initTableInfo(final int n) {
        return new TableInfo(0, n - 1, new int[n], new Name[n], null, 0, 0, 0);
    }
    
    private void mergeChild(final TableInfo tableInfo) {
        final int count = tableInfo.count;
        final TableInfo tableInfo2 = this._tableInfo.get();
        if (count <= tableInfo2.count) {
            return;
        }
        TableInfo initTableInfo = null;
        Label_0052: {
            if (count <= 6000) {
                initTableInfo = tableInfo;
                if (tableInfo.longestCollisionList <= 63) {
                    break Label_0052;
                }
            }
            initTableInfo = this.initTableInfo(64);
        }
        this._tableInfo.compareAndSet(tableInfo2, initTableInfo);
    }
    
    private void nukeSymbols() {
        this._count = 0;
        this._longestCollisionList = 0;
        Arrays.fill(this._mainHash, 0);
        Arrays.fill(this._mainNames, null);
        Arrays.fill(this._collList, null);
        this._collCount = 0;
        this._collEnd = 0;
    }
    
    private void rehash() {
        final int n = 0;
        this._needRehash = false;
        this._mainNamesShared = false;
        final int length = this._mainHash.length;
        final int n2 = length + length;
        if (n2 > 65536) {
            this.nukeSymbols();
        }
        else {
            this._mainHash = new int[n2];
            this._mainHashMask = n2 - 1;
            final Name[] mainNames = this._mainNames;
            this._mainNames = new Name[n2];
            int i = 0;
            int n3 = 0;
            while (i < length) {
                final Name name = mainNames[i];
                int n4 = n3;
                if (name != null) {
                    n4 = n3 + 1;
                    final int hashCode = name.hashCode();
                    final int n5 = this._mainHashMask & hashCode;
                    this._mainNames[n5] = name;
                    this._mainHash[n5] = hashCode << 8;
                }
                ++i;
                n3 = n4;
            }
            final int collEnd = this._collEnd;
            if (collEnd == 0) {
                this._longestCollisionList = 0;
                return;
            }
            this._collCount = 0;
            this._collEnd = 0;
            this._collListShared = false;
            final Bucket[] collList = this._collList;
            this._collList = new Bucket[collList.length];
            int j = 0;
            int max = n;
            while (j < collEnd) {
                for (Bucket next = collList[j]; next != null; next = next._next, ++n3) {
                    final Name name2 = next._name;
                    final int hashCode2 = name2.hashCode();
                    final int n6 = this._mainHashMask & hashCode2;
                    final int n7 = this._mainHash[n6];
                    if (this._mainNames[n6] == null) {
                        this._mainHash[n6] = hashCode2 << 8;
                        this._mainNames[n6] = name2;
                    }
                    else {
                        ++this._collCount;
                        final int n8 = n7 & 0xFF;
                        int bestBucket;
                        if (n8 == 0) {
                            if (this._collEnd <= 254) {
                                final int collEnd2 = this._collEnd;
                                ++this._collEnd;
                                if ((bestBucket = collEnd2) >= this._collList.length) {
                                    this.expandCollision();
                                    bestBucket = collEnd2;
                                }
                            }
                            else {
                                bestBucket = this.findBestBucket();
                            }
                            this._mainHash[n6] = ((n7 & 0xFFFFFF00) | bestBucket + 1);
                        }
                        else {
                            bestBucket = n8 - 1;
                        }
                        final Bucket bucket = new Bucket(name2, this._collList[bestBucket]);
                        this._collList[bestBucket] = bucket;
                        max = Math.max(max, bucket.length());
                    }
                }
                ++j;
            }
            this._longestCollisionList = max;
            if (n3 != this._count) {
                throw new RuntimeException("Internal error: count after rehash " + n3 + "; should be " + this._count);
            }
        }
    }
    
    private void unshareCollision() {
        final Bucket[] collList = this._collList;
        if (collList == null) {
            this._collList = new Bucket[32];
        }
        else {
            final int length = collList.length;
            System.arraycopy(collList, 0, this._collList = new Bucket[length], 0, length);
        }
        this._collListShared = false;
    }
    
    private void unshareMain() {
        final int[] mainHash = this._mainHash;
        final int length = this._mainHash.length;
        System.arraycopy(mainHash, 0, this._mainHash = new int[length], 0, length);
        this._mainHashShared = false;
    }
    
    private void unshareNames() {
        final Name[] mainNames = this._mainNames;
        final int length = mainNames.length;
        System.arraycopy(mainNames, 0, this._mainNames = new Name[length], 0, length);
        this._mainNamesShared = false;
    }
    
    public Name addName(final String s, final int[] array, final int n) {
        String intern = s;
        if (this._intern) {
            intern = InternCache.instance.intern(s);
        }
        int n2;
        if (n < 3) {
            if (n == 1) {
                n2 = this.calcHash(array[0]);
            }
            else {
                n2 = this.calcHash(array[0], array[1]);
            }
        }
        else {
            n2 = this.calcHash(array, n);
        }
        final Name constructName = constructName(n2, intern, array, n);
        this._addSymbol(n2, constructName);
        return constructName;
    }
    
    public int calcHash(int n) {
        n ^= this._hashSeed;
        n += n >>> 15;
        return n ^ n >>> 9;
    }
    
    public int calcHash(int n, final int n2) {
        n = ((n >>> 15 ^ n) + n2 * 33 ^ this._hashSeed);
        return n + (n >>> 7);
    }
    
    public int calcHash(final int[] array, int n) {
        int i = 3;
        if (n < 3) {
            throw new IllegalArgumentException();
        }
        final int n2 = array[0] ^ this._hashSeed;
        final int n3 = ((n2 + (n2 >>> 9)) * 33 + array[1]) * 65599;
        final int n4 = n3 + (n3 >>> 15) ^ array[2];
        int n5 = n4 + (n4 >>> 17);
        while (i < n) {
            final int n6 = n5 * 31 ^ array[i];
            final int n7 = n6 + (n6 >>> 3);
            n5 = (n7 ^ n7 << 7);
            ++i;
        }
        n = (n5 >>> 15) + n5;
        return n ^ n << 9;
    }
    
    public Name findName(final int n) {
        final int calcHash = this.calcHash(n);
        final int n2 = this._mainHashMask & calcHash;
        final int n3 = this._mainHash[n2];
        Label_0066: {
            if ((n3 >> 8 ^ calcHash) << 8 == 0) {
                final Name name = this._mainNames[n2];
                if (name != null) {
                    if (name.equals(n)) {
                        return name;
                    }
                    break Label_0066;
                }
            }
            else if (n3 != 0) {
                break Label_0066;
            }
            return null;
        }
        final int n4 = n3 & 0xFF;
        if (n4 <= 0) {
            return null;
        }
        final Bucket bucket = this._collList[n4 - 1];
        if (bucket != null) {
            return bucket.find(calcHash, n, 0);
        }
        return null;
    }
    
    public Name findName(final int n, final int n2) {
        int n3;
        if (n2 == 0) {
            n3 = this.calcHash(n);
        }
        else {
            n3 = this.calcHash(n, n2);
        }
        final int n4 = this._mainHashMask & n3;
        final int n5 = this._mainHash[n4];
        if ((n5 >> 8 ^ n3) << 8 == 0) {
            final Name name = this._mainNames[n4];
            if (name == null) {
                return null;
            }
            if (name.equals(n, n2)) {
                return name;
            }
        }
        else if (n5 == 0) {
            return null;
        }
        final int n6 = n5 & 0xFF;
        if (n6 > 0) {
            final Bucket bucket = this._collList[n6 - 1];
            if (bucket != null) {
                return bucket.find(n3, n, n2);
            }
        }
        return null;
    }
    
    public Name findName(final int[] array, int n) {
        final int n2 = 0;
        if (n >= 3) {
            final int calcHash = this.calcHash(array, n);
            final int n3 = this._mainHashMask & calcHash;
            final int n4 = this._mainHash[n3];
            if ((n4 >> 8 ^ calcHash) << 8 == 0) {
                final Name name = this._mainNames[n3];
                Name name2;
                if ((name2 = name) == null) {
                    return name2;
                }
                name2 = name;
                if (name.equals(array, n)) {
                    return name2;
                }
            }
            else if (n4 == 0) {
                return null;
            }
            final int n5 = n4 & 0xFF;
            if (n5 > 0) {
                final Bucket bucket = this._collList[n5 - 1];
                if (bucket != null) {
                    return bucket.find(calcHash, array, n);
                }
            }
            return null;
        }
        final int n6 = array[0];
        if (n < 2) {
            n = n2;
        }
        else {
            n = array[1];
        }
        return this.findName(n6, n);
    }
    
    public boolean maybeDirty() {
        return !this._mainHashShared;
    }
    
    public void release() {
        if (this._parent != null && this.maybeDirty()) {
            this._parent.mergeChild(new TableInfo(this));
            this._mainHashShared = true;
            this._mainNamesShared = true;
            this._collListShared = true;
        }
    }
    
    protected void reportTooManyCollisions(final int n) {
        throw new IllegalStateException("Longest collision chain in symbol table (of size " + this._count + ") now exceeds maximum, " + n + " -- suspect a DoS attack based on hash collisions");
    }
    
    static final class Bucket
    {
        private final int _length;
        protected final Name _name;
        protected final Bucket _next;
        
        Bucket(final Name name, final Bucket next) {
            this._name = name;
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
        
        public Name find(final int n, final int n2, final int n3) {
            if (this._name.hashCode() != n || !this._name.equals(n2, n3)) {
                for (Bucket bucket = this._next; bucket != null; bucket = bucket._next) {
                    final Name name = bucket._name;
                    if (name.hashCode() == n) {
                        final Name name2 = name;
                        if (name.equals(n2, n3)) {
                            return name2;
                        }
                    }
                }
                return null;
            }
            return this._name;
        }
        
        public Name find(final int n, final int[] array, final int n2) {
            if (this._name.hashCode() != n || !this._name.equals(array, n2)) {
                for (Bucket bucket = this._next; bucket != null; bucket = bucket._next) {
                    final Name name = bucket._name;
                    if (name.hashCode() == n) {
                        final Name name2 = name;
                        if (name.equals(array, n2)) {
                            return name2;
                        }
                    }
                }
                return null;
            }
            return this._name;
        }
        
        public int length() {
            return this._length;
        }
    }
    
    private static final class TableInfo
    {
        public final int collCount;
        public final int collEnd;
        public final Bucket[] collList;
        public final int count;
        public final int longestCollisionList;
        public final int[] mainHash;
        public final int mainHashMask;
        public final Name[] mainNames;
        
        public TableInfo(final int count, final int mainHashMask, final int[] mainHash, final Name[] mainNames, final Bucket[] collList, final int collCount, final int collEnd, final int longestCollisionList) {
            this.count = count;
            this.mainHashMask = mainHashMask;
            this.mainHash = mainHash;
            this.mainNames = mainNames;
            this.collList = collList;
            this.collCount = collCount;
            this.collEnd = collEnd;
            this.longestCollisionList = longestCollisionList;
        }
        
        public TableInfo(final BytesToNameCanonicalizer bytesToNameCanonicalizer) {
            this.count = bytesToNameCanonicalizer._count;
            this.mainHashMask = bytesToNameCanonicalizer._mainHashMask;
            this.mainHash = bytesToNameCanonicalizer._mainHash;
            this.mainNames = bytesToNameCanonicalizer._mainNames;
            this.collList = bytesToNameCanonicalizer._collList;
            this.collCount = bytesToNameCanonicalizer._collCount;
            this.collEnd = bytesToNameCanonicalizer._collEnd;
            this.longestCollisionList = bytesToNameCanonicalizer._longestCollisionList;
        }
    }
}
