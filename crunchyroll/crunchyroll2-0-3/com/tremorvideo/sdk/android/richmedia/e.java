// 
// Decompiled by Procyon v0.5.30
// 

package com.tremorvideo.sdk.android.richmedia;

import java.io.IOException;
import java.io.InputStream;
import java.io.DataInputStream;

public class e
{
    DataInputStream a;
    
    public e(final InputStream inputStream) {
        this.a = new DataInputStream(inputStream);
    }
    
    public int a() throws IOException {
        return Integer.reverseBytes(this.a.readInt());
    }
    
    public int b() throws IOException {
        return this.a.readUnsignedByte();
    }
    
    public String c() throws IOException {
        final byte[] array = new byte[this.e()];
        this.a.readFully(array);
        return new String(array, "UTF8");
    }
    
    public float d() throws IOException {
        return Float.intBitsToFloat(Integer.reverseBytes(this.a.readInt()));
    }
    
    public int e() throws IOException {
        return this.a.readUnsignedByte() | this.a.readUnsignedByte() << 8;
    }
    
    public boolean f() throws IOException {
        return this.a.readBoolean();
    }
}
