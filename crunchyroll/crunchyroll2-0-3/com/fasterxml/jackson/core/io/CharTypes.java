// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.io;

import java.util.Arrays;

public final class CharTypes
{
    private static final byte[] HEX_BYTES;
    private static final char[] HEX_CHARS;
    static final int[] sHexValues;
    static final int[] sInputCodes;
    static final int[] sInputCodesComment;
    static final int[] sInputCodesJsNames;
    static final int[] sInputCodesUtf8;
    static final int[] sInputCodesUtf8JsNames;
    static final int[] sOutputEscapes128;
    
    static {
        HEX_CHARS = "0123456789ABCDEF".toCharArray();
        final int length = CharTypes.HEX_CHARS.length;
        HEX_BYTES = new byte[length];
        for (int i = 0; i < length; ++i) {
            CharTypes.HEX_BYTES[i] = (byte)CharTypes.HEX_CHARS[i];
        }
        final int[] sInputCodes2 = new int[256];
        for (int j = 0; j < 32; ++j) {
            sInputCodes2[j] = -1;
        }
        sInputCodes2[92] = (sInputCodes2[34] = 1);
        sInputCodes = sInputCodes2;
        final int[] sInputCodesUtf9 = new int[CharTypes.sInputCodes.length];
        System.arraycopy(CharTypes.sInputCodes, 0, sInputCodesUtf9, 0, CharTypes.sInputCodes.length);
        for (int k = 128; k < 256; ++k) {
            int n;
            if ((k & 0xE0) == 0xC0) {
                n = 2;
            }
            else if ((k & 0xF0) == 0xE0) {
                n = 3;
            }
            else if ((k & 0xF8) == 0xF0) {
                n = 4;
            }
            else {
                n = -1;
            }
            sInputCodesUtf9[k] = n;
        }
        sInputCodesUtf8 = sInputCodesUtf9;
        final int[] sInputCodesJsNames2 = new int[256];
        Arrays.fill(sInputCodesJsNames2, -1);
        for (int l = 33; l < 256; ++l) {
            if (Character.isJavaIdentifierPart((char)l)) {
                sInputCodesJsNames2[l] = 0;
            }
        }
        sInputCodesJsNames2[64] = 0;
        sInputCodesJsNames2[42] = (sInputCodesJsNames2[35] = 0);
        sInputCodesJsNames2[43] = (sInputCodesJsNames2[45] = 0);
        sInputCodesJsNames = sInputCodesJsNames2;
        final int[] sInputCodesUtf8JsNames2 = new int[256];
        System.arraycopy(CharTypes.sInputCodesJsNames, 0, sInputCodesUtf8JsNames2, 0, CharTypes.sInputCodesJsNames.length);
        Arrays.fill(sInputCodesUtf8JsNames2, 128, 128, 0);
        sInputCodesUtf8JsNames = sInputCodesUtf8JsNames2;
        sInputCodesComment = new int[256];
        System.arraycopy(CharTypes.sInputCodesUtf8, 128, CharTypes.sInputCodesComment, 128, 128);
        Arrays.fill(CharTypes.sInputCodesComment, 0, 32, -1);
        CharTypes.sInputCodesComment[9] = 0;
        CharTypes.sInputCodesComment[10] = 10;
        CharTypes.sInputCodesComment[13] = 13;
        CharTypes.sInputCodesComment[42] = 42;
        final int[] sOutputEscapes129 = new int[128];
        for (int n2 = 0; n2 < 32; ++n2) {
            sOutputEscapes129[n2] = -1;
        }
        sOutputEscapes129[34] = 34;
        sOutputEscapes129[92] = 92;
        sOutputEscapes129[8] = 98;
        sOutputEscapes129[9] = 116;
        sOutputEscapes129[12] = 102;
        sOutputEscapes129[10] = 110;
        sOutputEscapes129[13] = 114;
        sOutputEscapes128 = sOutputEscapes129;
        Arrays.fill(sHexValues = new int[128], -1);
        for (int n3 = 0; n3 < 10; ++n3) {
            CharTypes.sHexValues[n3 + 48] = n3;
        }
        for (int n4 = 0; n4 < 6; ++n4) {
            CharTypes.sHexValues[n4 + 97] = n4 + 10;
            CharTypes.sHexValues[n4 + 65] = n4 + 10;
        }
    }
    
    public static void appendQuoted(final StringBuilder sb, final String s) {
        final int[] sOutputEscapes128 = CharTypes.sOutputEscapes128;
        final int length = sOutputEscapes128.length;
        for (int i = 0; i < s.length(); ++i) {
            final char char1 = s.charAt(i);
            if (char1 >= length || sOutputEscapes128[char1] == 0) {
                sb.append(char1);
            }
            else {
                sb.append('\\');
                final int n = sOutputEscapes128[char1];
                if (n < 0) {
                    sb.append('u');
                    sb.append('0');
                    sb.append('0');
                    sb.append(CharTypes.HEX_CHARS[char1 >> 4]);
                    sb.append(CharTypes.HEX_CHARS[char1 & '\u000f']);
                }
                else {
                    sb.append((char)n);
                }
            }
        }
    }
    
    public static int charToHex(final int n) {
        if (n > 127) {
            return -1;
        }
        return CharTypes.sHexValues[n];
    }
    
    public static byte[] copyHexBytes() {
        return CharTypes.HEX_BYTES.clone();
    }
    
    public static char[] copyHexChars() {
        return CharTypes.HEX_CHARS.clone();
    }
    
    public static int[] get7BitOutputEscapes() {
        return CharTypes.sOutputEscapes128;
    }
    
    public static int[] getInputCodeComment() {
        return CharTypes.sInputCodesComment;
    }
    
    public static int[] getInputCodeLatin1() {
        return CharTypes.sInputCodes;
    }
    
    public static int[] getInputCodeLatin1JsNames() {
        return CharTypes.sInputCodesJsNames;
    }
    
    public static int[] getInputCodeUtf8() {
        return CharTypes.sInputCodesUtf8;
    }
    
    public static int[] getInputCodeUtf8JsNames() {
        return CharTypes.sInputCodesUtf8JsNames;
    }
}
