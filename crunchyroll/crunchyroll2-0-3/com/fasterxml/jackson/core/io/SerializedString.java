// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.io;

import java.io.Serializable;
import com.fasterxml.jackson.core.SerializableString;

public class SerializedString implements SerializableString, Serializable
{
    protected char[] _quotedChars;
    protected byte[] _quotedUTF8Ref;
    protected byte[] _unquotedUTF8Ref;
    protected final String _value;
    
    public SerializedString(final String value) {
        if (value == null) {
            throw new IllegalStateException("Null String illegal for SerializedString");
        }
        this._value = value;
    }
    
    @Override
    public int appendQuotedUTF8(final byte[] array, final int n) {
        byte[] quotedUTF8Ref;
        if ((quotedUTF8Ref = this._quotedUTF8Ref) == null) {
            quotedUTF8Ref = JsonStringEncoder.getInstance().quoteAsUTF8(this._value);
            this._quotedUTF8Ref = quotedUTF8Ref;
        }
        final int length = quotedUTF8Ref.length;
        if (n + length > array.length) {
            return -1;
        }
        System.arraycopy(quotedUTF8Ref, 0, array, n, length);
        return length;
    }
    
    @Override
    public final char[] asQuotedChars() {
        char[] quotedChars;
        if ((quotedChars = this._quotedChars) == null) {
            quotedChars = JsonStringEncoder.getInstance().quoteAsString(this._value);
            this._quotedChars = quotedChars;
        }
        return quotedChars;
    }
    
    @Override
    public final byte[] asQuotedUTF8() {
        byte[] quotedUTF8Ref;
        if ((quotedUTF8Ref = this._quotedUTF8Ref) == null) {
            quotedUTF8Ref = JsonStringEncoder.getInstance().quoteAsUTF8(this._value);
            this._quotedUTF8Ref = quotedUTF8Ref;
        }
        return quotedUTF8Ref;
    }
    
    @Override
    public final byte[] asUnquotedUTF8() {
        byte[] unquotedUTF8Ref;
        if ((unquotedUTF8Ref = this._unquotedUTF8Ref) == null) {
            unquotedUTF8Ref = JsonStringEncoder.getInstance().encodeAsUTF8(this._value);
            this._unquotedUTF8Ref = unquotedUTF8Ref;
        }
        return unquotedUTF8Ref;
    }
    
    @Override
    public final boolean equals(final Object o) {
        return o == this || (o != null && o.getClass() == this.getClass() && this._value.equals(((SerializedString)o)._value));
    }
    
    @Override
    public final String getValue() {
        return this._value;
    }
    
    @Override
    public final int hashCode() {
        return this._value.hashCode();
    }
    
    @Override
    public final String toString() {
        return this._value;
    }
}
