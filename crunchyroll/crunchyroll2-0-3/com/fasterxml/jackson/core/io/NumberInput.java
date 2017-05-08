// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.io;

public final class NumberInput
{
    static final String MAX_LONG_STR;
    static final String MIN_LONG_STR_NO_SIGN;
    
    static {
        MIN_LONG_STR_NO_SIGN = String.valueOf(Long.MIN_VALUE).substring(1);
        MAX_LONG_STR = String.valueOf(Long.MAX_VALUE);
    }
    
    public static boolean inLongRange(final String s, final boolean b) {
        String s2;
        if (b) {
            s2 = NumberInput.MIN_LONG_STR_NO_SIGN;
        }
        else {
            s2 = NumberInput.MAX_LONG_STR;
        }
        final int length = s2.length();
        final int length2 = s.length();
        if (length2 >= length) {
            if (length2 > length) {
                return false;
            }
            for (int i = 0; i < length; ++i) {
                final char c = (char)(s.charAt(i) - s2.charAt(i));
                if (c != '\0') {
                    return c < '\0';
                }
            }
        }
        return true;
    }
    
    public static boolean inLongRange(final char[] array, final int n, int i, final boolean b) {
        String s;
        if (b) {
            s = NumberInput.MIN_LONG_STR_NO_SIGN;
        }
        else {
            s = NumberInput.MAX_LONG_STR;
        }
        final int length = s.length();
        if (i >= length) {
            if (i > length) {
                return false;
            }
            char c;
            for (i = 0; i < length; ++i) {
                c = (char)(array[n + i] - s.charAt(i));
                if (c != '\0') {
                    return c < '\0';
                }
            }
        }
        return true;
    }
    
    public static int parseAsInt(final String p0, final int p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: iconst_0       
        //     1: istore          6
        //     3: aload_0        
        //     4: ifnonnull       9
        //     7: iload_1        
        //     8: ireturn        
        //     9: aload_0        
        //    10: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //    13: astore_0       
        //    14: aload_0        
        //    15: invokevirtual   java/lang/String.length:()I
        //    18: istore          4
        //    20: iload           4
        //    22: ifeq            7
        //    25: iload           4
        //    27: ifge            142
        //    30: aload_0        
        //    31: iconst_0       
        //    32: invokevirtual   java/lang/String.charAt:(I)C
        //    35: istore          5
        //    37: iload           5
        //    39: bipush          43
        //    41: if_icmpne       97
        //    44: aload_0        
        //    45: iconst_1       
        //    46: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //    49: astore_0       
        //    50: aload_0        
        //    51: invokevirtual   java/lang/String.length:()I
        //    54: istore          5
        //    56: iload           6
        //    58: istore          4
        //    60: iload           4
        //    62: iload           5
        //    64: if_icmpge       127
        //    67: aload_0        
        //    68: iload           4
        //    70: invokevirtual   java/lang/String.charAt:(I)C
        //    73: istore          6
        //    75: iload           6
        //    77: bipush          57
        //    79: if_icmpgt       89
        //    82: iload           6
        //    84: bipush          48
        //    86: if_icmpge       118
        //    89: aload_0        
        //    90: invokestatic    com/fasterxml/jackson/core/io/NumberInput.parseDouble:(Ljava/lang/String;)D
        //    93: dstore_2       
        //    94: dload_2        
        //    95: d2i            
        //    96: ireturn        
        //    97: iload           5
        //    99: bipush          45
        //   101: if_icmpne       142
        //   104: iconst_1       
        //   105: istore          6
        //   107: iload           4
        //   109: istore          5
        //   111: iload           6
        //   113: istore          4
        //   115: goto            60
        //   118: iload           4
        //   120: iconst_1       
        //   121: iadd           
        //   122: istore          4
        //   124: goto            60
        //   127: aload_0        
        //   128: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //   131: istore          4
        //   133: iload           4
        //   135: ireturn        
        //   136: astore_0       
        //   137: iload_1        
        //   138: ireturn        
        //   139: astore_0       
        //   140: iload_1        
        //   141: ireturn        
        //   142: iload           4
        //   144: istore          5
        //   146: iload           6
        //   148: istore          4
        //   150: goto            60
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                             
        //  -----  -----  -----  -----  ---------------------------------
        //  89     94     136    139    Ljava/lang/NumberFormatException;
        //  127    133    139    142    Ljava/lang/NumberFormatException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0127:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:317)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:238)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:123)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static long parseAsLong(final String p0, final long p1) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     0: iconst_0       
        //     1: istore          7
        //     3: aload_0        
        //     4: ifnonnull       9
        //     7: lload_1        
        //     8: lreturn        
        //     9: aload_0        
        //    10: invokevirtual   java/lang/String.trim:()Ljava/lang/String;
        //    13: astore_0       
        //    14: aload_0        
        //    15: invokevirtual   java/lang/String.length:()I
        //    18: istore          5
        //    20: iload           5
        //    22: ifeq            7
        //    25: iload           5
        //    27: ifge            142
        //    30: aload_0        
        //    31: iconst_0       
        //    32: invokevirtual   java/lang/String.charAt:(I)C
        //    35: istore          6
        //    37: iload           6
        //    39: bipush          43
        //    41: if_icmpne       97
        //    44: aload_0        
        //    45: iconst_1       
        //    46: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //    49: astore_0       
        //    50: aload_0        
        //    51: invokevirtual   java/lang/String.length:()I
        //    54: istore          6
        //    56: iload           7
        //    58: istore          5
        //    60: iload           5
        //    62: iload           6
        //    64: if_icmpge       127
        //    67: aload_0        
        //    68: iload           5
        //    70: invokevirtual   java/lang/String.charAt:(I)C
        //    73: istore          7
        //    75: iload           7
        //    77: bipush          57
        //    79: if_icmpgt       89
        //    82: iload           7
        //    84: bipush          48
        //    86: if_icmpge       118
        //    89: aload_0        
        //    90: invokestatic    com/fasterxml/jackson/core/io/NumberInput.parseDouble:(Ljava/lang/String;)D
        //    93: dstore_3       
        //    94: dload_3        
        //    95: d2l            
        //    96: lreturn        
        //    97: iload           6
        //    99: bipush          45
        //   101: if_icmpne       142
        //   104: iconst_1       
        //   105: istore          7
        //   107: iload           5
        //   109: istore          6
        //   111: iload           7
        //   113: istore          5
        //   115: goto            60
        //   118: iload           5
        //   120: iconst_1       
        //   121: iadd           
        //   122: istore          5
        //   124: goto            60
        //   127: aload_0        
        //   128: invokestatic    java/lang/Long.parseLong:(Ljava/lang/String;)J
        //   131: lstore          8
        //   133: lload           8
        //   135: lreturn        
        //   136: astore_0       
        //   137: lload_1        
        //   138: lreturn        
        //   139: astore_0       
        //   140: lload_1        
        //   141: lreturn        
        //   142: iload           5
        //   144: istore          6
        //   146: iload           7
        //   148: istore          5
        //   150: goto            60
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                             
        //  -----  -----  -----  -----  ---------------------------------
        //  89     94     136    139    Ljava/lang/NumberFormatException;
        //  127    133    139    142    Ljava/lang/NumberFormatException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0127:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2592)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:317)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:238)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:123)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public static double parseDouble(final String s) throws NumberFormatException {
        if ("2.2250738585072012e-308".equals(s)) {
            return Double.MIN_VALUE;
        }
        return Double.parseDouble(s);
    }
    
    public static int parseInt(final String s) {
        int n = 1;
        char c = s.charAt(0);
        final int length = s.length();
        boolean b;
        if (c == '-') {
            b = true;
        }
        else {
            b = false;
        }
        Label_0059: {
            final int int1;
            if (b) {
                if (length != 1 && length <= 10) {
                    c = s.charAt(1);
                    n = 2;
                    break Label_0059;
                }
                int1 = Integer.parseInt(s);
            }
            else {
                if (length > 9) {
                    return Integer.parseInt(s);
                }
                break Label_0059;
            }
            return int1;
        }
        if (c > '9' || c < '0') {
            return Integer.parseInt(s);
        }
        int n2;
        final char c2 = (char)(n2 = c - 48);
        if (n < length) {
            final int n3 = n + 1;
            final char char1 = s.charAt(n);
            if (char1 > '9' || char1 < '0') {
                return Integer.parseInt(s);
            }
            final char c3 = (char)(n2 = c2 * 10 + (char1 - 48));
            if (n3 < length) {
                int n4 = n3 + 1;
                final char char2 = s.charAt(n3);
                if (char2 > '9' || char2 < '0') {
                    return Integer.parseInt(s);
                }
                final char c4 = (char)(n2 = c3 * 10 + (char2 - 48));
                if (n4 < length) {
                    n2 = c4;
                    while (true) {
                        final int n5 = n4 + 1;
                        final char char3 = s.charAt(n4);
                        if (char3 > '9' || char3 < '0') {
                            return Integer.parseInt(s);
                        }
                        n2 = n2 * 10 + (char3 - '0');
                        if (n5 >= length) {
                            break;
                        }
                        n4 = n5;
                    }
                }
            }
        }
        int int1 = n2;
        if (b) {
            return -n2;
        }
        return int1;
    }
    
    public static int parseInt(final char[] array, int n, int n2) {
        final int n3 = array[n] - '0';
        final int n4 = n2 + n;
        final int n5 = n + 1;
        n = n3;
        if (n5 < n4) {
            n2 = n3 * 10 + (array[n5] - '0');
            final int n6 = n5 + 1;
            n = n2;
            if (n6 < n4) {
                n2 = n2 * 10 + (array[n6] - '0');
                final int n7 = n6 + 1;
                n = n2;
                if (n7 < n4) {
                    n2 = n2 * 10 + (array[n7] - '0');
                    final int n8 = n7 + 1;
                    n = n2;
                    if (n8 < n4) {
                        n2 = n2 * 10 + (array[n8] - '0');
                        final int n9 = n8 + 1;
                        n = n2;
                        if (n9 < n4) {
                            n2 = n2 * 10 + (array[n9] - '0');
                            final int n10 = n9 + 1;
                            n = n2;
                            if (n10 < n4) {
                                n2 = n2 * 10 + (array[n10] - '0');
                                final int n11 = n10 + 1;
                                n = n2;
                                if (n11 < n4) {
                                    n2 = n2 * 10 + (array[n11] - '0');
                                    final int n12 = n11 + 1;
                                    n = n2;
                                    if (n12 < n4) {
                                        n = n2 * 10 + (array[n12] - '0');
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return n;
    }
    
    public static long parseLong(final String s) {
        if (s.length() <= 9) {
            return parseInt(s);
        }
        return Long.parseLong(s);
    }
    
    public static long parseLong(final char[] array, final int n, int n2) {
        n2 -= 9;
        return parseInt(array, n2 + n, 9) + parseInt(array, n, n2) * 1000000000L;
    }
}
