// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.io;

import com.fasterxml.jackson.core.SerializableString;
import java.io.Serializable;

public abstract class CharacterEscapes implements Serializable
{
    public abstract int[] getEscapeCodesForAscii();
    
    public abstract SerializableString getEscapeSequence(final int p0);
}
