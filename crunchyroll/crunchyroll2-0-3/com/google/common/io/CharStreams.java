// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.io;

import java.io.IOException;
import java.nio.CharBuffer;

public final class CharStreams
{
    public static long copy(final Readable readable, final Appendable appendable) throws IOException {
        final CharBuffer allocate = CharBuffer.allocate(2048);
        long n = 0L;
        while (readable.read(allocate) != -1) {
            allocate.flip();
            appendable.append(allocate);
            n += allocate.remaining();
            allocate.clear();
        }
        return n;
    }
    
    public static String toString(final Readable readable) throws IOException {
        return toStringBuilder(readable).toString();
    }
    
    private static StringBuilder toStringBuilder(final Readable readable) throws IOException {
        final StringBuilder sb = new StringBuilder();
        copy(readable, sb);
        return sb;
    }
}
