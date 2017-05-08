// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.internal;

import java.io.IOException;
import java.nio.BufferOverflowException;
import java.nio.ReadOnlyBufferException;
import java.nio.ByteBuffer;

public final class zzrg
{
    private final ByteBuffer zzaVT;
    
    private zzrg(final ByteBuffer zzaVT) {
        this.zzaVT = zzaVT;
    }
    
    private zzrg(final byte[] array, final int n, final int n2) {
        this(ByteBuffer.wrap(array, n, n2));
    }
    
    public static int zzA(final int n, final int n2) {
        return zzkM(n) + zzkJ(n2);
    }
    
    public static zzrg zzA(final byte[] array) {
        return zzb(array, 0, array.length);
    }
    
    public static int zzY(final long n) {
        return zzab(n);
    }
    
    private static int zza(final CharSequence charSequence) {
        int length;
        int n;
        for (length = charSequence.length(), n = '\0'; n < length && charSequence.charAt(n) < '\u0080'; ++n) {}
        int n2 = n;
        int n3 = length;
        int n4;
        while (true) {
            n4 = n3;
            if (n2 >= length) {
                break;
            }
            final char char1 = charSequence.charAt(n2);
            if (char1 >= '\u0800') {
                n4 = n3 + zza(charSequence, n2);
                break;
            }
            ++n2;
            n3 += '\u007f' - char1 >>> 31;
        }
        if (n4 < length) {
            throw new IllegalArgumentException("UTF-8 length does not fit in int: " + (n4 + 4294967296L));
        }
        return n4;
    }
    
    private static int zza(final CharSequence charSequence, int i) {
        final int length = charSequence.length();
        char c = '\0';
        while (i < length) {
            final char char1 = charSequence.charAt(i);
            int n;
            if (char1 < '\u0800') {
                c += (char)('\u007f' - char1 >>> 31);
                n = i;
            }
            else {
                final char c2 = (char)(c + '\u0002');
                n = i;
                c = c2;
                if ('\ud800' <= char1) {
                    n = i;
                    c = c2;
                    if (char1 <= '\udfff') {
                        if (Character.codePointAt(charSequence, i) < 65536) {
                            throw new IllegalArgumentException("Unpaired surrogate at index " + i);
                        }
                        n = i + 1;
                        c = c2;
                    }
                }
            }
            i = n + 1;
        }
        return c;
    }
    
    private static int zza(final CharSequence charSequence, final byte[] array, int n, int i) {
        final int length = charSequence.length();
        final int n2 = 0;
        int n3;
        char char1;
        for (n3 = n + i, i = n2; i < length && i + n < n3; ++i) {
            char1 = charSequence.charAt(i);
            if (char1 >= '\u0080') {
                break;
            }
            array[n + i] = (byte)char1;
        }
        if (i == length) {
            return n + length;
        }
        n += i;
        while (i < length) {
            final char char2 = charSequence.charAt(i);
            Label_0123: {
                if (char2 < '\u0080' && n < n3) {
                    final int n4 = n + 1;
                    array[n] = (byte)char2;
                    n = n4;
                }
                else if (char2 < '\u0800' && n <= n3 - 2) {
                    final int n5 = n + 1;
                    array[n] = (byte)(char2 >>> 6 | '\u03c0');
                    n = n5 + 1;
                    array[n5] = (byte)((char2 & '?') | '\u0080');
                }
                else if ((char2 < '\ud800' || '\udfff' < char2) && n <= n3 - 3) {
                    final int n6 = n + 1;
                    array[n] = (byte)(char2 >>> 12 | '\u01e0');
                    final int n7 = n6 + 1;
                    array[n6] = (byte)((char2 >>> 6 & '?') | '\u0080');
                    n = n7 + 1;
                    array[n7] = (byte)((char2 & '?') | '\u0080');
                }
                else {
                    if (n <= n3 - 4) {
                        int n8 = i;
                        if (i + 1 != charSequence.length()) {
                            ++i;
                            final char char3 = charSequence.charAt(i);
                            if (Character.isSurrogatePair(char2, char3)) {
                                final int codePoint = Character.toCodePoint(char2, char3);
                                final int n9 = n + 1;
                                array[n] = (byte)(codePoint >>> 18 | 0xF0);
                                n = n9 + 1;
                                array[n9] = (byte)((codePoint >>> 12 & 0x3F) | 0x80);
                                final int n10 = n + 1;
                                array[n] = (byte)((codePoint >>> 6 & 0x3F) | 0x80);
                                n = n10 + 1;
                                array[n10] = (byte)((codePoint & 0x3F) | 0x80);
                                break Label_0123;
                            }
                            n8 = i;
                        }
                        throw new IllegalArgumentException("Unpaired surrogate at index " + (n8 - 1));
                    }
                    throw new ArrayIndexOutOfBoundsException("Failed writing " + char2 + " at index " + n);
                }
            }
            ++i;
        }
        return n;
    }
    
    private static void zza(final CharSequence charSequence, final ByteBuffer byteBuffer) {
        if (byteBuffer.isReadOnly()) {
            throw new ReadOnlyBufferException();
        }
        if (byteBuffer.hasArray()) {
            try {
                byteBuffer.position(zza(charSequence, byteBuffer.array(), byteBuffer.arrayOffset() + byteBuffer.position(), byteBuffer.remaining()) - byteBuffer.arrayOffset());
                return;
            }
            catch (ArrayIndexOutOfBoundsException ex2) {
                final BufferOverflowException ex = new BufferOverflowException();
                ex.initCause(ex2);
                throw ex;
            }
        }
        zzb(charSequence, byteBuffer);
    }
    
    public static int zzab(final long n) {
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
    
    public static int zzas(final boolean b) {
        return 1;
    }
    
    public static int zzb(final int n, final double n2) {
        return zzkM(n) + zzi(n2);
    }
    
    public static int zzb(final int n, final zzrn zzrn) {
        return zzkM(n) * 2 + zzd(zzrn);
    }
    
    public static zzrg zzb(final byte[] array, final int n, final int n2) {
        return new zzrg(array, n, n2);
    }
    
    private static void zzb(final CharSequence charSequence, final ByteBuffer byteBuffer) {
        for (int length = charSequence.length(), i = 0; i < length; ++i) {
            final char char1 = charSequence.charAt(i);
            if (char1 < '\u0080') {
                byteBuffer.put((byte)char1);
            }
            else if (char1 < '\u0800') {
                byteBuffer.put((byte)(char1 >>> 6 | '\u03c0'));
                byteBuffer.put((byte)((char1 & '?') | '\u0080'));
            }
            else {
                if (char1 >= '\ud800' && '\udfff' >= char1) {
                    int n = i;
                    if (i + 1 != charSequence.length()) {
                        ++i;
                        final char char2 = charSequence.charAt(i);
                        if (Character.isSurrogatePair(char1, char2)) {
                            final int codePoint = Character.toCodePoint(char1, char2);
                            byteBuffer.put((byte)(codePoint >>> 18 | 0xF0));
                            byteBuffer.put((byte)((codePoint >>> 12 & 0x3F) | 0x80));
                            byteBuffer.put((byte)((codePoint >>> 6 & 0x3F) | 0x80));
                            byteBuffer.put((byte)((codePoint & 0x3F) | 0x80));
                            continue;
                        }
                        n = i;
                    }
                    throw new IllegalArgumentException("Unpaired surrogate at index " + (n - 1));
                }
                byteBuffer.put((byte)(char1 >>> 12 | '\u01e0'));
                byteBuffer.put((byte)((char1 >>> 6 & '?') | '\u0080'));
                byteBuffer.put((byte)((char1 & '?') | '\u0080'));
            }
        }
    }
    
    public static int zzc(final int n, final zzrn zzrn) {
        return zzkM(n) + zze(zzrn);
    }
    
    public static int zzc(final int n, final boolean b) {
        return zzkM(n) + zzas(b);
    }
    
    public static int zzd(final int n, final long n2) {
        return zzkM(n) + zzY(n2);
    }
    
    public static int zzd(final zzrn zzrn) {
        return zzrn.zzBV();
    }
    
    public static int zze(final zzrn zzrn) {
        final int zzBV = zzrn.zzBV();
        return zzBV + zzkO(zzBV);
    }
    
    public static int zzfj(final String s) {
        final int zza = zza(s);
        return zza + zzkO(zza);
    }
    
    public static int zzi(final double n) {
        return 8;
    }
    
    public static int zzk(final int n, final String s) {
        return zzkM(n) + zzfj(s);
    }
    
    public static int zzkJ(final int n) {
        if (n >= 0) {
            return zzkO(n);
        }
        return 10;
    }
    
    public static int zzkM(final int n) {
        return zzkO(zzrq.zzD(n, 0));
    }
    
    public static int zzkO(final int n) {
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
    
    public int zzBG() {
        return this.zzaVT.remaining();
    }
    
    public void zzBH() {
        if (this.zzBG() != 0) {
            throw new IllegalStateException("Did not write as much data as expected.");
        }
    }
    
    public void zzC(final int n, final int n2) throws IOException {
        this.zzkN(zzrq.zzD(n, n2));
    }
    
    public void zzD(final byte[] array) throws IOException {
        this.zzc(array, 0, array.length);
    }
    
    public void zzW(final long n) throws IOException {
        this.zzaa(n);
    }
    
    public void zza(final int n, final double n2) throws IOException {
        this.zzC(n, 1);
        this.zzh(n2);
    }
    
    public void zza(final int n, final zzrn zzrn) throws IOException {
        this.zzC(n, 2);
        this.zzc(zzrn);
    }
    
    public void zzaa(long n) throws IOException {
        while ((0xFFFFFFFFFFFFFF80L & n) != 0x0L) {
            this.zzkL(((int)n & 0x7F) | 0x80);
            n >>>= 7;
        }
        this.zzkL((int)n);
    }
    
    public void zzac(final long n) throws IOException {
        this.zzkL((int)n & 0xFF);
        this.zzkL((int)(n >> 8) & 0xFF);
        this.zzkL((int)(n >> 16) & 0xFF);
        this.zzkL((int)(n >> 24) & 0xFF);
        this.zzkL((int)(n >> 32) & 0xFF);
        this.zzkL((int)(n >> 40) & 0xFF);
        this.zzkL((int)(n >> 48) & 0xFF);
        this.zzkL((int)(n >> 56) & 0xFF);
    }
    
    public void zzar(final boolean b) throws IOException {
        int n;
        if (b) {
            n = 1;
        }
        else {
            n = 0;
        }
        this.zzkL(n);
    }
    
    public void zzb(final byte b) throws IOException {
        if (!this.zzaVT.hasRemaining()) {
            throw new zza(this.zzaVT.position(), this.zzaVT.limit());
        }
        this.zzaVT.put(b);
    }
    
    public void zzb(final int n, final long n2) throws IOException {
        this.zzC(n, 0);
        this.zzW(n2);
    }
    
    public void zzb(final int n, final String s) throws IOException {
        this.zzC(n, 2);
        this.zzfi(s);
    }
    
    public void zzb(final int n, final boolean b) throws IOException {
        this.zzC(n, 0);
        this.zzar(b);
    }
    
    public void zzb(final zzrn zzrn) throws IOException {
        zzrn.zza(this);
    }
    
    public void zzc(final zzrn zzrn) throws IOException {
        this.zzkN(zzrn.zzBU());
        zzrn.zza(this);
    }
    
    public void zzc(final byte[] array, final int n, final int n2) throws IOException {
        if (this.zzaVT.remaining() >= n2) {
            this.zzaVT.put(array, n, n2);
            return;
        }
        throw new zza(this.zzaVT.position(), this.zzaVT.limit());
    }
    
    public void zzfi(final String s) throws IOException {
        try {
            final int zzkO = zzkO(s.length());
            if (zzkO == zzkO(s.length() * 3)) {
                final int position = this.zzaVT.position();
                this.zzaVT.position(position + zzkO);
                zza(s, this.zzaVT);
                final int position2 = this.zzaVT.position();
                this.zzaVT.position(position);
                this.zzkN(position2 - position - zzkO);
                this.zzaVT.position(position2);
                return;
            }
            this.zzkN(zza(s));
            zza(s, this.zzaVT);
        }
        catch (BufferOverflowException ex) {
            throw new zza(this.zzaVT.position(), this.zzaVT.limit());
        }
    }
    
    public void zzh(final double n) throws IOException {
        this.zzac(Double.doubleToLongBits(n));
    }
    
    public void zzkH(final int n) throws IOException {
        if (n >= 0) {
            this.zzkN(n);
            return;
        }
        this.zzaa(n);
    }
    
    public void zzkL(final int n) throws IOException {
        this.zzb((byte)n);
    }
    
    public void zzkN(int n) throws IOException {
        while ((n & 0xFFFFFF80) != 0x0) {
            this.zzkL((n & 0x7F) | 0x80);
            n >>>= 7;
        }
        this.zzkL(n);
    }
    
    public void zzy(final int n, final int n2) throws IOException {
        this.zzC(n, 0);
        this.zzkH(n2);
    }
    
    public static class zza extends IOException
    {
        zza(final int n, final int n2) {
            super("CodedOutputStream was writing to a flat byte array and ran out of space (pos " + n + " limit " + n2 + ").");
        }
    }
}
