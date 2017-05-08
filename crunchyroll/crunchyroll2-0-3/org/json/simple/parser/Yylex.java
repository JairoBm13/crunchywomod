// 
// Decompiled by Procyon v0.5.30
// 

package org.json.simple.parser;

import java.io.IOException;
import java.io.Reader;

class Yylex
{
    private static final int[] ZZ_ACTION;
    private static final int[] ZZ_ATTRIBUTE;
    private static final char[] ZZ_CMAP;
    private static final String[] ZZ_ERROR_MSG;
    private static final int[] ZZ_LEXSTATE;
    private static final int[] ZZ_ROWMAP;
    private static final int[] ZZ_TRANS;
    private StringBuffer sb;
    private int yychar;
    private int yycolumn;
    private int yyline;
    private boolean zzAtBOL;
    private boolean zzAtEOF;
    private char[] zzBuffer;
    private int zzCurrentPos;
    private int zzEndRead;
    private int zzLexicalState;
    private int zzMarkedPos;
    private Reader zzReader;
    private int zzStartRead;
    private int zzState;
    
    static {
        ZZ_LEXSTATE = new int[] { 0, 0, 1, 1 };
        ZZ_CMAP = zzUnpackCMap("\t\u0000\u0001\u0007\u0001\u0007\u0002\u0000\u0001\u0007\u0012\u0000\u0001\u0007\u0001\u0000\u0001\t\b\u0000\u0001\u0006\u0001\u0019\u0001\u0002\u0001\u0004\u0001\n\n\u0003\u0001\u001a\u0006\u0000\u0004\u0001\u0001\u0005\u0001\u0001\u0014\u0000\u0001\u0017\u0001\b\u0001\u0018\u0003\u0000\u0001\u0012\u0001\u000b\u0002\u0001\u0001\u0011\u0001\f\u0005\u0000\u0001\u0013\u0001\u0000\u0001\r\u0003\u0000\u0001\u000e\u0001\u0014\u0001\u000f\u0001\u0010\u0005\u0000\u0001\u0015\u0001\u0000\u0001\u0016\uff82\u0000");
        ZZ_ACTION = zzUnpackAction();
        ZZ_ROWMAP = zzUnpackRowMap();
        ZZ_TRANS = new int[] { 2, 2, 3, 4, 2, 2, 2, 5, 2, 6, 2, 2, 7, 8, 2, 9, 2, 2, 2, 2, 2, 10, 11, 12, 13, 14, 15, 16, 16, 16, 16, 16, 16, 16, 16, 17, 18, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 4, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 4, 19, 20, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 20, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 5, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 21, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 22, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 23, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 16, 16, 16, 16, 16, 16, 16, 16, -1, -1, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, 16, -1, -1, -1, -1, -1, -1, -1, -1, 24, 25, 26, 27, 28, 29, 30, 31, 32, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 33, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 34, 35, -1, -1, 34, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 36, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 37, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 38, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 39, -1, 39, -1, 39, -1, -1, -1, -1, -1, 39, 39, -1, -1, -1, -1, 39, 39, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 33, -1, 20, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 20, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 35, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 38, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 40, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 41, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 42, -1, 42, -1, 42, -1, -1, -1, -1, -1, 42, 42, -1, -1, -1, -1, 42, 42, -1, -1, -1, -1, -1, -1, -1, -1, -1, 43, -1, 43, -1, 43, -1, -1, -1, -1, -1, 43, 43, -1, -1, -1, -1, 43, 43, -1, -1, -1, -1, -1, -1, -1, -1, -1, 44, -1, 44, -1, 44, -1, -1, -1, -1, -1, 44, 44, -1, -1, -1, -1, 44, 44, -1, -1, -1, -1, -1, -1, -1, -1 };
        ZZ_ERROR_MSG = new String[] { "Unkown internal scanner error", "Error: could not match input", "Error: pushback value was too large" };
        ZZ_ATTRIBUTE = zzUnpackAttribute();
    }
    
    Yylex(final Reader zzReader) {
        this.zzLexicalState = 0;
        this.zzBuffer = new char[16384];
        this.zzAtBOL = true;
        this.sb = new StringBuffer();
        this.zzReader = zzReader;
    }
    
    private boolean zzRefill() throws IOException {
        if (this.zzStartRead > 0) {
            System.arraycopy(this.zzBuffer, this.zzStartRead, this.zzBuffer, 0, this.zzEndRead - this.zzStartRead);
            this.zzEndRead -= this.zzStartRead;
            this.zzCurrentPos -= this.zzStartRead;
            this.zzMarkedPos -= this.zzStartRead;
            this.zzStartRead = 0;
        }
        if (this.zzCurrentPos >= this.zzBuffer.length) {
            final char[] zzBuffer = new char[this.zzCurrentPos * 2];
            System.arraycopy(this.zzBuffer, 0, zzBuffer, 0, this.zzBuffer.length);
            this.zzBuffer = zzBuffer;
        }
        final int read = this.zzReader.read(this.zzBuffer, this.zzEndRead, this.zzBuffer.length - this.zzEndRead);
        if (read > 0) {
            this.zzEndRead += read;
            return false;
        }
        if (read != 0) {
            return true;
        }
        final int read2 = this.zzReader.read();
        if (read2 == -1) {
            return true;
        }
        this.zzBuffer[this.zzEndRead++] = (char)read2;
        return false;
    }
    
    private void zzScanError(final int n) {
        try {
            final String s = Yylex.ZZ_ERROR_MSG[n];
            throw new Error(s);
        }
        catch (ArrayIndexOutOfBoundsException ex) {
            final String s = Yylex.ZZ_ERROR_MSG[0];
            throw new Error(s);
        }
    }
    
    private static int zzUnpackAction(final String s, int n, final int[] array) {
        int n3;
        for (int length = s.length(), i = 0; i < length; i = n3) {
            final int n2 = i + 1;
            final int char1 = s.charAt(i);
            n3 = n2 + 1;
            final char char2 = s.charAt(n2);
            int n4 = n;
            n = char1;
            int n5;
            while (true) {
                n5 = n4 + 1;
                array[n4] = char2;
                --n;
                if (n <= 0) {
                    break;
                }
                n4 = n5;
            }
            n = n5;
        }
        return n;
    }
    
    private static int[] zzUnpackAction() {
        final int[] array = new int[45];
        zzUnpackAction("\u0002\u0000\u0002\u0001\u0001\u0002\u0001\u0003\u0001\u0004\u0003\u0001\u0001\u0005\u0001\u0006\u0001\u0007\u0001\b\u0001\t\u0001\n\u0001\u000b\u0001\f\u0001\r\u0005\u0000\u0001\f\u0001\u000e\u0001\u000f\u0001\u0010\u0001\u0011\u0001\u0012\u0001\u0013\u0001\u0014\u0001\u0000\u0001\u0015\u0001\u0000\u0001\u0015\u0004\u0000\u0001\u0016\u0001\u0017\u0002\u0000\u0001\u0018", 0, array);
        return array;
    }
    
    private static int zzUnpackAttribute(final String s, int n, final int[] array) {
        int n3;
        for (int length = s.length(), i = 0; i < length; i = n3) {
            final int n2 = i + 1;
            final int char1 = s.charAt(i);
            n3 = n2 + 1;
            final char char2 = s.charAt(n2);
            int n4 = n;
            n = char1;
            int n5;
            while (true) {
                n5 = n4 + 1;
                array[n4] = char2;
                --n;
                if (n <= 0) {
                    break;
                }
                n4 = n5;
            }
            n = n5;
        }
        return n;
    }
    
    private static int[] zzUnpackAttribute() {
        final int[] array = new int[45];
        zzUnpackAttribute("\u0002\u0000\u0001\t\u0003\u0001\u0001\t\u0003\u0001\u0006\t\u0002\u0001\u0001\t\u0005\u0000\b\t\u0001\u0000\u0001\u0001\u0001\u0000\u0001\u0001\u0004\u0000\u0002\t\u0002\u0000\u0001\t", 0, array);
        return array;
    }
    
    private static char[] zzUnpackCMap(final String s) {
        final char[] array = new char[65536];
        int n = 0;
        int n3;
        for (int i = 0; i < 90; i = n3) {
            final int n2 = i + 1;
            final char char1 = s.charAt(i);
            n3 = n2 + 1;
            final char char2 = s.charAt(n2);
            int n4 = n;
            int n5 = char1;
            int n6;
            while (true) {
                n6 = n4 + 1;
                array[n4] = char2;
                --n5;
                if (n5 <= 0) {
                    break;
                }
                n4 = n6;
            }
            n = n6;
        }
        return array;
    }
    
    private static int zzUnpackRowMap(final String s, int n, final int[] array) {
        int n2;
        char char1;
        for (int length = s.length(), i = 0; i < length; i = n2 + 1, array[n] = (s.charAt(n2) | char1 << 16), ++n) {
            n2 = i + 1;
            char1 = s.charAt(i);
        }
        return n;
    }
    
    private static int[] zzUnpackRowMap() {
        final int[] array = new int[45];
        zzUnpackRowMap("\u0000\u0000\u0000\u001b\u00006\u0000Q\u0000l\u0000\u0087\u00006\u0000¢\u0000½\u0000\u00d8\u00006\u00006\u00006\u00006\u00006\u00006\u0000\u00f3\u0000\u010e\u00006\u0000\u0129\u0000\u0144\u0000\u015f\u0000\u017a\u0000\u0195\u00006\u00006\u00006\u00006\u00006\u00006\u00006\u00006\u0000\u01b0\u0000\u01cb\u0000\u01e6\u0000\u01e6\u0000\u0201\u0000\u021c\u0000\u0237\u0000\u0252\u00006\u00006\u0000\u026d\u0000\u0288\u00006", 0, array);
        return array;
    }
    
    int getPosition() {
        return this.yychar;
    }
    
    public final void yybegin(final int zzLexicalState) {
        this.zzLexicalState = zzLexicalState;
    }
    
    public final char yycharat(final int n) {
        return this.zzBuffer[this.zzStartRead + n];
    }
    
    public Yytoken yylex() throws IOException, ParseException {
        int zzEndRead = this.zzEndRead;
        char[] zzBuffer = this.zzBuffer;
        final char[] zz_CMAP = Yylex.ZZ_CMAP;
        final int[] zz_TRANS = Yylex.ZZ_TRANS;
        final int[] zz_ROWMAP = Yylex.ZZ_ROWMAP;
        final int[] zz_ATTRIBUTE = Yylex.ZZ_ATTRIBUTE;
    Label_0140_Outer:
        while (true) {
            int zzMarkedPos = this.zzMarkedPos;
            this.yychar += zzMarkedPos - this.zzStartRead;
            int n = -1;
            this.zzStartRead = zzMarkedPos;
            this.zzCurrentPos = zzMarkedPos;
            this.zzState = Yylex.ZZ_LEXSTATE[this.zzLexicalState];
            int zzCurrentPos = zzMarkedPos;
            char[] zzBuffer2 = zzBuffer;
            while (true) {
                Block_6: {
                    Block_5: {
                        int n3;
                        int zzMarkedPos2;
                        int zzEndRead2;
                        while (true) {
                            int n4;
                            int n5;
                            char[] array;
                            if (zzCurrentPos < zzEndRead) {
                                final int n2 = zzCurrentPos + 1;
                                n3 = zzBuffer2[zzCurrentPos];
                                n4 = zzMarkedPos;
                                n5 = n2;
                                array = zzBuffer2;
                            }
                            else {
                                if (this.zzAtEOF) {
                                    break Block_5;
                                }
                                this.zzCurrentPos = zzCurrentPos;
                                this.zzMarkedPos = zzMarkedPos;
                                final boolean zzRefill = this.zzRefill();
                                final int zzCurrentPos2 = this.zzCurrentPos;
                                zzMarkedPos2 = this.zzMarkedPos;
                                zzBuffer2 = this.zzBuffer;
                                zzEndRead2 = this.zzEndRead;
                                if (zzRefill) {
                                    break Block_6;
                                }
                                n3 = zzBuffer2[zzCurrentPos2];
                                n5 = zzCurrentPos2 + 1;
                                array = zzBuffer2;
                                zzEndRead = zzEndRead2;
                                n4 = zzMarkedPos2;
                            }
                            final int zzState = zz_TRANS[zz_ROWMAP[this.zzState] + zz_CMAP[n3]];
                            if (zzState == -1) {
                                zzMarkedPos2 = n4;
                                zzEndRead2 = zzEndRead;
                                zzBuffer2 = array;
                                break;
                            }
                            this.zzState = zzState;
                            final int n6 = zz_ATTRIBUTE[this.zzState];
                            if ((n6 & 0x1) == 0x1) {
                                final int zzState2 = this.zzState;
                                final int n7 = n5;
                                n = zzState2;
                                zzBuffer2 = array;
                                zzEndRead2 = zzEndRead;
                                zzMarkedPos2 = n7;
                                if ((n6 & 0x8) == 0x8) {
                                    break;
                                }
                                n4 = n7;
                                n = zzState2;
                            }
                            zzBuffer2 = array;
                            zzCurrentPos = n5;
                            zzMarkedPos = n4;
                        }
                        this.zzMarkedPos = zzMarkedPos2;
                        if (n >= 0) {
                            n = Yylex.ZZ_ACTION[n];
                        }
                        zzBuffer = zzBuffer2;
                        zzEndRead = zzEndRead2;
                        switch (n) {
                            case 18: {
                                this.sb.append('\n');
                                zzBuffer = zzBuffer2;
                                zzEndRead = zzEndRead2;
                                continue Label_0140_Outer;
                            }
                            case 24: {
                                try {
                                    this.sb.append((char)Integer.parseInt(this.yytext().substring(2), 16));
                                    zzBuffer = zzBuffer2;
                                    zzEndRead = zzEndRead2;
                                    continue Label_0140_Outer;
                                }
                                catch (Exception ex) {
                                    throw new ParseException(this.yychar, 2, ex);
                                }
                            }
                            case 20: {
                                this.sb.append('\t');
                                zzBuffer = zzBuffer2;
                                zzEndRead = zzEndRead2;
                                continue Label_0140_Outer;
                            }
                            case 17: {
                                this.sb.append('\f');
                                zzBuffer = zzBuffer2;
                                zzEndRead = zzEndRead2;
                                continue Label_0140_Outer;
                            }
                            case 14: {
                                this.sb.append('\"');
                                zzBuffer = zzBuffer2;
                                zzEndRead = zzEndRead2;
                                continue Label_0140_Outer;
                            }
                            case 15: {
                                this.sb.append('/');
                                zzBuffer = zzBuffer2;
                                zzEndRead = zzEndRead2;
                                continue Label_0140_Outer;
                            }
                            case 19: {
                                this.sb.append('\r');
                                zzBuffer = zzBuffer2;
                                zzEndRead = zzEndRead2;
                                continue Label_0140_Outer;
                            }
                            case 12: {
                                this.sb.append('\\');
                                zzBuffer = zzBuffer2;
                                zzEndRead = zzEndRead2;
                                continue Label_0140_Outer;
                            }
                            case 16: {
                                this.sb.append('\b');
                                zzBuffer = zzBuffer2;
                                zzEndRead = zzEndRead2;
                                continue Label_0140_Outer;
                            }
                            case 4: {
                                this.sb.delete(0, this.sb.length());
                                this.yybegin(2);
                                zzBuffer = zzBuffer2;
                                zzEndRead = zzEndRead2;
                                continue Label_0140_Outer;
                            }
                            case 11: {
                                this.sb.append(this.yytext());
                                zzBuffer = zzBuffer2;
                                zzEndRead = zzEndRead2;
                            }
                            case 3:
                            case 25:
                            case 26:
                            case 27:
                            case 28:
                            case 29:
                            case 30:
                            case 31:
                            case 32:
                            case 33:
                            case 34:
                            case 35:
                            case 36:
                            case 37:
                            case 38:
                            case 39:
                            case 40:
                            case 41:
                            case 42:
                            case 43:
                            case 44:
                            case 45:
                            case 46:
                            case 47:
                            case 48: {
                                continue Label_0140_Outer;
                            }
                            default: {
                                if (n3 == -1 && this.zzStartRead == this.zzCurrentPos) {
                                    this.zzAtEOF = true;
                                    return null;
                                }
                                this.zzScanError(1);
                                zzBuffer = zzBuffer2;
                                zzEndRead = zzEndRead2;
                                continue Label_0140_Outer;
                            }
                            case 6: {
                                return new Yytoken(2, null);
                            }
                            case 23: {
                                return new Yytoken(0, Boolean.valueOf(this.yytext()));
                            }
                            case 22: {
                                return new Yytoken(0, null);
                            }
                            case 13: {
                                this.yybegin(0);
                                return new Yytoken(0, this.sb.toString());
                            }
                            case 21: {
                                return new Yytoken(0, Double.valueOf(this.yytext()));
                            }
                            case 1: {
                                throw new ParseException(this.yychar, 0, new Character(this.yycharat(0)));
                            }
                            case 8: {
                                return new Yytoken(4, null);
                            }
                            case 10: {
                                return new Yytoken(6, null);
                            }
                            case 5: {
                                return new Yytoken(1, null);
                            }
                            case 7: {
                                return new Yytoken(3, null);
                            }
                            case 2: {
                                return new Yytoken(0, Long.valueOf(this.yytext()));
                            }
                            case 9: {
                                return new Yytoken(5, null);
                            }
                        }
                    }
                    int n3 = -1;
                    int zzEndRead2 = zzEndRead;
                    int zzMarkedPos2 = zzMarkedPos;
                    continue;
                }
                int n3 = -1;
                continue;
            }
        }
    }
    
    public final void yyreset(final Reader zzReader) {
        this.zzReader = zzReader;
        this.zzAtBOL = true;
        this.zzAtEOF = false;
        this.zzStartRead = 0;
        this.zzEndRead = 0;
        this.zzMarkedPos = 0;
        this.zzCurrentPos = 0;
        this.yycolumn = 0;
        this.yychar = 0;
        this.yyline = 0;
        this.zzLexicalState = 0;
    }
    
    public final String yytext() {
        return new String(this.zzBuffer, this.zzStartRead, this.zzMarkedPos - this.zzStartRead);
    }
}
