// 
// Decompiled by Procyon v0.5.30
// 

package com.crashlytics.android.core;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Flushable;

final class CodedOutputStream implements Flushable
{
    private final byte[] buffer;
    private final int limit;
    private final OutputStream output;
    private int position;
    
    private CodedOutputStream(final OutputStream output, final byte[] buffer) {
        this.output = output;
        this.buffer = buffer;
        this.position = 0;
        this.limit = buffer.length;
    }
    
    public static int computeBoolSize(final int n, final boolean b) {
        return computeTagSize(n) + computeBoolSizeNoTag(b);
    }
    
    public static int computeBoolSizeNoTag(final boolean b) {
        return 1;
    }
    
    public static int computeBytesSize(final int n, final ByteString byteString) {
        return computeTagSize(n) + computeBytesSizeNoTag(byteString);
    }
    
    public static int computeBytesSizeNoTag(final ByteString byteString) {
        return computeRawVarint32Size(byteString.size()) + byteString.size();
    }
    
    public static int computeEnumSize(final int n, final int n2) {
        return computeTagSize(n) + computeEnumSizeNoTag(n2);
    }
    
    public static int computeEnumSizeNoTag(final int n) {
        return computeInt32SizeNoTag(n);
    }
    
    public static int computeFloatSize(final int n, final float n2) {
        return computeTagSize(n) + computeFloatSizeNoTag(n2);
    }
    
    public static int computeFloatSizeNoTag(final float n) {
        return 4;
    }
    
    public static int computeInt32SizeNoTag(final int n) {
        if (n >= 0) {
            return computeRawVarint32Size(n);
        }
        return 10;
    }
    
    public static int computeRawVarint32Size(final int n) {
        if ((n & 0xFFFFFF80) == 0x0) {
            return 1;
        }
        if ((n & 0xFFFFC000) == 0x0) {
            return 2;
        }
        if ((0xFFE00000 & n) == 0x0) {
            return 3;
        }
        if ((0xF0000000 & n) == 0x0) {
            return 4;
        }
        return 5;
    }
    
    public static int computeRawVarint64Size(final long n) {
        if ((0xFFFFFFFFFFFFFF80L & n) == 0x0L) {
            return 1;
        }
        if ((0xFFFFFFFFFFFFC000L & n) == 0x0L) {
            return 2;
        }
        if ((0xFFFFFFFFFFE00000L & n) == 0x0L) {
            return 3;
        }
        if ((0xFFFFFFFFF0000000L & n) == 0x0L) {
            return 4;
        }
        if ((0xFFFFFFF800000000L & n) == 0x0L) {
            return 5;
        }
        if ((0xFFFFFC0000000000L & n) == 0x0L) {
            return 6;
        }
        if ((0xFFFE000000000000L & n) == 0x0L) {
            return 7;
        }
        if ((0xFF00000000000000L & n) == 0x0L) {
            return 8;
        }
        if ((Long.MIN_VALUE & n) == 0x0L) {
            return 9;
        }
        return 10;
    }
    
    public static int computeSInt32Size(final int n, final int n2) {
        return computeTagSize(n) + computeSInt32SizeNoTag(n2);
    }
    
    public static int computeSInt32SizeNoTag(final int n) {
        return computeRawVarint32Size(encodeZigZag32(n));
    }
    
    public static int computeTagSize(final int n) {
        return computeRawVarint32Size(WireFormat.makeTag(n, 0));
    }
    
    public static int computeUInt32Size(final int n, final int n2) {
        return computeTagSize(n) + computeUInt32SizeNoTag(n2);
    }
    
    public static int computeUInt32SizeNoTag(final int n) {
        return computeRawVarint32Size(n);
    }
    
    public static int computeUInt64Size(final int n, final long n2) {
        return computeTagSize(n) + computeUInt64SizeNoTag(n2);
    }
    
    public static int computeUInt64SizeNoTag(final long n) {
        return computeRawVarint64Size(n);
    }
    
    public static int encodeZigZag32(final int n) {
        return n << 1 ^ n >> 31;
    }
    
    public static CodedOutputStream newInstance(final OutputStream outputStream) {
        return newInstance(outputStream, 4096);
    }
    
    public static CodedOutputStream newInstance(final OutputStream outputStream, final int n) {
        return new CodedOutputStream(outputStream, new byte[n]);
    }
    
    private void refreshBuffer() throws IOException {
        if (this.output == null) {
            throw new OutOfSpaceException();
        }
        this.output.write(this.buffer, 0, this.position);
        this.position = 0;
    }
    
    @Override
    public void flush() throws IOException {
        if (this.output != null) {
            this.refreshBuffer();
        }
    }
    
    public void writeBool(final int n, final boolean b) throws IOException {
        this.writeTag(n, 0);
        this.writeBoolNoTag(b);
    }
    
    public void writeBoolNoTag(final boolean b) throws IOException {
        int n;
        if (b) {
            n = 1;
        }
        else {
            n = 0;
        }
        this.writeRawByte(n);
    }
    
    public void writeBytes(final int n, final ByteString byteString) throws IOException {
        this.writeTag(n, 2);
        this.writeBytesNoTag(byteString);
    }
    
    public void writeBytesNoTag(final ByteString byteString) throws IOException {
        this.writeRawVarint32(byteString.size());
        this.writeRawBytes(byteString);
    }
    
    public void writeEnum(final int n, final int n2) throws IOException {
        this.writeTag(n, 0);
        this.writeEnumNoTag(n2);
    }
    
    public void writeEnumNoTag(final int n) throws IOException {
        this.writeInt32NoTag(n);
    }
    
    public void writeFloat(final int n, final float n2) throws IOException {
        this.writeTag(n, 5);
        this.writeFloatNoTag(n2);
    }
    
    public void writeFloatNoTag(final float n) throws IOException {
        this.writeRawLittleEndian32(Float.floatToRawIntBits(n));
    }
    
    public void writeInt32NoTag(final int n) throws IOException {
        if (n >= 0) {
            this.writeRawVarint32(n);
            return;
        }
        this.writeRawVarint64(n);
    }
    
    public void writeRawByte(final byte b) throws IOException {
        if (this.position == this.limit) {
            this.refreshBuffer();
        }
        this.buffer[this.position++] = b;
    }
    
    public void writeRawByte(final int n) throws IOException {
        this.writeRawByte((byte)n);
    }
    
    public void writeRawBytes(final ByteString byteString) throws IOException {
        this.writeRawBytes(byteString, 0, byteString.size());
    }
    
    public void writeRawBytes(final ByteString byteString, int i, int min) throws IOException {
        if (this.limit - this.position >= min) {
            byteString.copyTo(this.buffer, i, this.position, min);
            this.position += min;
        }
        else {
            final int n = this.limit - this.position;
            byteString.copyTo(this.buffer, i, this.position, n);
            final int n2 = i + n;
            i = min - n;
            this.position = this.limit;
            this.refreshBuffer();
            if (i <= this.limit) {
                byteString.copyTo(this.buffer, n2, 0, i);
                this.position = i;
                return;
            }
            final InputStream input = byteString.newInput();
            if (n2 != input.skip(n2)) {
                throw new IllegalStateException("Skip failed.");
            }
            while (i > 0) {
                min = Math.min(i, this.limit);
                final int read = input.read(this.buffer, 0, min);
                if (read != min) {
                    throw new IllegalStateException("Read failed.");
                }
                this.output.write(this.buffer, 0, read);
                i -= read;
            }
        }
    }
    
    public void writeRawBytes(final byte[] array) throws IOException {
        this.writeRawBytes(array, 0, array.length);
    }
    
    public void writeRawBytes(final byte[] array, int n, int position) throws IOException {
        if (this.limit - this.position >= position) {
            System.arraycopy(array, n, this.buffer, this.position, position);
            this.position += position;
            return;
        }
        final int n2 = this.limit - this.position;
        System.arraycopy(array, n, this.buffer, this.position, n2);
        n += n2;
        position -= n2;
        this.position = this.limit;
        this.refreshBuffer();
        if (position <= this.limit) {
            System.arraycopy(array, n, this.buffer, 0, position);
            this.position = position;
            return;
        }
        this.output.write(array, n, position);
    }
    
    public void writeRawLittleEndian32(final int n) throws IOException {
        this.writeRawByte(n & 0xFF);
        this.writeRawByte(n >> 8 & 0xFF);
        this.writeRawByte(n >> 16 & 0xFF);
        this.writeRawByte(n >> 24 & 0xFF);
    }
    
    public void writeRawVarint32(int n) throws IOException {
        while ((n & 0xFFFFFF80) != 0x0) {
            this.writeRawByte((n & 0x7F) | 0x80);
            n >>>= 7;
        }
        this.writeRawByte(n);
    }
    
    public void writeRawVarint64(long n) throws IOException {
        while ((0xFFFFFFFFFFFFFF80L & n) != 0x0L) {
            this.writeRawByte(((int)n & 0x7F) | 0x80);
            n >>>= 7;
        }
        this.writeRawByte((int)n);
    }
    
    public void writeSInt32(final int n, final int n2) throws IOException {
        this.writeTag(n, 0);
        this.writeSInt32NoTag(n2);
    }
    
    public void writeSInt32NoTag(final int n) throws IOException {
        this.writeRawVarint32(encodeZigZag32(n));
    }
    
    public void writeString(final int n, final String s) throws IOException {
        this.writeTag(n, 2);
        this.writeStringNoTag(s);
    }
    
    public void writeStringNoTag(final String s) throws IOException {
        final byte[] bytes = s.getBytes("UTF-8");
        this.writeRawVarint32(bytes.length);
        this.writeRawBytes(bytes);
    }
    
    public void writeTag(final int n, final int n2) throws IOException {
        this.writeRawVarint32(WireFormat.makeTag(n, n2));
    }
    
    public void writeUInt32(final int n, final int n2) throws IOException {
        this.writeTag(n, 0);
        this.writeUInt32NoTag(n2);
    }
    
    public void writeUInt32NoTag(final int n) throws IOException {
        this.writeRawVarint32(n);
    }
    
    public void writeUInt64(final int n, final long n2) throws IOException {
        this.writeTag(n, 0);
        this.writeUInt64NoTag(n2);
    }
    
    public void writeUInt64NoTag(final long n) throws IOException {
        this.writeRawVarint64(n);
    }
    
    static class OutOfSpaceException extends IOException
    {
        OutOfSpaceException() {
            super("CodedOutputStream was writing to a flat byte array and ran out of space.");
        }
    }
}
