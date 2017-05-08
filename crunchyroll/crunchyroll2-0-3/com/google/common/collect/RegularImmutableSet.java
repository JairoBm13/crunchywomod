// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.collect;

final class RegularImmutableSet<E> extends ArrayImmutableSet<E>
{
    private final transient int hashCode;
    private final transient int mask;
    final transient Object[] table;
    
    RegularImmutableSet(final Object[] array, final int hashCode, final Object[] table, final int mask) {
        super(array);
        this.table = table;
        this.mask = mask;
        this.hashCode = hashCode;
    }
    
    @Override
    public boolean contains(final Object o) {
        if (o != null) {
            int smear = Hashing.smear(o.hashCode());
            while (true) {
                final Object o2 = this.table[this.mask & smear];
                if (o2 == null) {
                    return false;
                }
                if (o2.equals(o)) {
                    break;
                }
                ++smear;
            }
            return true;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.hashCode;
    }
    
    @Override
    boolean isHashCodeFast() {
        return true;
    }
}
