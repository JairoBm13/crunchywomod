// 
// Decompiled by Procyon v0.5.30
// 

package com.google.common.base;

import java.util.Arrays;

public abstract class CharMatcher implements Predicate<Character>
{
    public static final CharMatcher ANY;
    public static final CharMatcher ASCII;
    public static final CharMatcher BREAKING_WHITESPACE;
    public static final CharMatcher DIGIT;
    public static final CharMatcher INVISIBLE;
    public static final CharMatcher JAVA_DIGIT;
    public static final CharMatcher JAVA_ISO_CONTROL;
    public static final CharMatcher JAVA_LETTER;
    public static final CharMatcher JAVA_LETTER_OR_DIGIT;
    public static final CharMatcher JAVA_LOWER_CASE;
    public static final CharMatcher JAVA_UPPER_CASE;
    public static final CharMatcher NONE;
    public static final CharMatcher SINGLE_WIDTH;
    public static final CharMatcher WHITESPACE;
    final String description;
    
    static {
        BREAKING_WHITESPACE = anyOf("\t\n\u000b\f\r \u0085\u1680\u2028\u2029\u205f\u3000").or(inRange('\u2000', '\u2006')).or(inRange('\u2008', '\u200a')).withToString("CharMatcher.BREAKING_WHITESPACE").precomputed();
        ASCII = inRange('\0', '\u007f', "CharMatcher.ASCII");
        CharMatcher charMatcher = inRange('0', '9');
        final char[] charArray = "\u0660\u06f0\u07c0\u0966\u09e6\u0a66\u0ae6\u0b66\u0be6\u0c66\u0ce6\u0d66\u0e50\u0ed0\u0f20\u1040\u1090\u17e0\u1810\u1946\u19d0\u1b50\u1bb0\u1c40\u1c50\ua620\ua8d0\ua900\uaa50\uff10".toCharArray();
        for (int length = charArray.length, i = 0; i < length; ++i) {
            final char c = charArray[i];
            charMatcher = charMatcher.or(inRange(c, (char)(c + '\t')));
        }
        DIGIT = charMatcher.withToString("CharMatcher.DIGIT").precomputed();
        JAVA_DIGIT = new CharMatcher("CharMatcher.JAVA_DIGIT") {
            @Override
            public boolean matches(final char c) {
                return Character.isDigit(c);
            }
        };
        JAVA_LETTER = new CharMatcher("CharMatcher.JAVA_LETTER") {
            @Override
            public boolean matches(final char c) {
                return Character.isLetter(c);
            }
            
            @Override
            public CharMatcher precomputed() {
                return this;
            }
        };
        JAVA_LETTER_OR_DIGIT = new CharMatcher("CharMatcher.JAVA_LETTER_OR_DIGIT") {
            @Override
            public boolean matches(final char c) {
                return Character.isLetterOrDigit(c);
            }
        };
        JAVA_UPPER_CASE = new CharMatcher("CharMatcher.JAVA_UPPER_CASE") {
            @Override
            public boolean matches(final char c) {
                return Character.isUpperCase(c);
            }
        };
        JAVA_LOWER_CASE = new CharMatcher("CharMatcher.JAVA_LOWER_CASE") {
            @Override
            public boolean matches(final char c) {
                return Character.isLowerCase(c);
            }
        };
        JAVA_ISO_CONTROL = inRange('\0', '\u001f').or(inRange('\u007f', '\u009f')).withToString("CharMatcher.JAVA_ISO_CONTROL");
        INVISIBLE = inRange('\0', ' ').or(inRange('\u007f', ' ')).or(is('\u00ad')).or(inRange('\u0600', '\u0604')).or(anyOf("\u06dd\u070f\u1680\u180e")).or(inRange('\u2000', '\u200f')).or(inRange('\u2028', '\u202f')).or(inRange('\u205f', '\u2064')).or(inRange('\u206a', '\u206f')).or(is('\u3000')).or(inRange('\ud800', '\uf8ff')).or(anyOf("\ufeff\ufff9\ufffa\ufffb")).withToString("CharMatcher.INVISIBLE").precomputed();
        SINGLE_WIDTH = inRange('\0', '\u04f9').or(is('\u05be')).or(inRange('\u05d0', '\u05ea')).or(is('\u05f3')).or(is('\u05f4')).or(inRange('\u0600', '\u06ff')).or(inRange('\u0750', '\u077f')).or(inRange('\u0e00', '\u0e7f')).or(inRange('\u1e00', '\u20af')).or(inRange('\u2100', '\u213a')).or(inRange('\ufb50', '\ufdff')).or(inRange('\ufe70', '\ufeff')).or(inRange('\uff61', '\uffdc')).withToString("CharMatcher.SINGLE_WIDTH").precomputed();
        ANY = new CharMatcher("CharMatcher.ANY") {
            @Override
            public boolean matches(final char c) {
                return true;
            }
            
            @Override
            public CharMatcher or(final CharMatcher charMatcher) {
                Preconditions.checkNotNull(charMatcher);
                return this;
            }
            
            @Override
            public CharMatcher precomputed() {
                return this;
            }
        };
        NONE = new CharMatcher("CharMatcher.NONE") {
            @Override
            public boolean matches(final char c) {
                return false;
            }
            
            @Override
            public CharMatcher or(final CharMatcher charMatcher) {
                return Preconditions.checkNotNull(charMatcher);
            }
            
            @Override
            public CharMatcher precomputed() {
                return this;
            }
            
            @Override
            void setBits(final LookupTable lookupTable) {
            }
        };
        WHITESPACE = new CharMatcher("CharMatcher.WHITESPACE") {
            private final char[] table = { '\u0001', '\0', ' ', '\0', '\0', '\0', '\0', '\0', '\0', '\t', '\n', '\u000b', '\f', '\r', '\0', '\0', '\u2028', '\u2029', '\0', '\0', '\0', '\0', '\0', '\u202f', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', ' ', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\u3000', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\0', '\u0085', '\u2000', '\u2001', '\u2002', '\u2003', '\u2004', '\u2005', '\u2006', '\u2007', '\u2008', '\u2009', '\u200a', '\0', '\0', '\0', '\0', '\0', '\u205f', '\u1680', '\0', '\0', '\u180e', '\0', '\0', '\0' };
            
            @Override
            public boolean matches(final char c) {
                return this.table[c % 'O'] == c;
            }
            
            @Override
            public CharMatcher precomputed() {
                return this;
            }
        };
    }
    
    protected CharMatcher() {
        this.description = "UnknownCharMatcher";
    }
    
    CharMatcher(final String description) {
        this.description = description;
    }
    
    public static CharMatcher anyOf(final CharSequence charSequence) {
        switch (charSequence.length()) {
            default: {
                final char[] charArray = charSequence.toString().toCharArray();
                Arrays.sort(charArray);
                return new CharMatcher("CharMatcher.anyOf(\"" + charArray + "\")") {
                    @Override
                    public boolean matches(final char c) {
                        return Arrays.binarySearch(charArray, c) >= 0;
                    }
                };
            }
            case 0: {
                return CharMatcher.NONE;
            }
            case 1: {
                return is(charSequence.charAt(0));
            }
            case 2: {
                return new CharMatcher("CharMatcher.anyOf(\"" + charSequence + "\")") {
                    final /* synthetic */ char val$match1 = charSequence.charAt(0);
                    final /* synthetic */ char val$match2 = charSequence.charAt(1);
                    
                    @Override
                    public boolean matches(final char c) {
                        return c == this.val$match1 || c == this.val$match2;
                    }
                    
                    @Override
                    public CharMatcher precomputed() {
                        return this;
                    }
                    
                    @Override
                    void setBits(final LookupTable lookupTable) {
                        lookupTable.set(this.val$match1);
                        lookupTable.set(this.val$match2);
                    }
                };
            }
        }
    }
    
    public static CharMatcher inRange(final char c, final char c2) {
        Preconditions.checkArgument(c2 >= c);
        return inRange(c, c2, "CharMatcher.inRange(" + Integer.toHexString(c) + ", " + Integer.toHexString(c2) + ")");
    }
    
    static CharMatcher inRange(final char c, final char c2, final String s) {
        return new CharMatcher(s) {
            @Override
            public boolean matches(final char c) {
                return c <= c && c <= c2;
            }
            
            @Override
            public CharMatcher precomputed() {
                return this;
            }
            
            @Override
            void setBits(final LookupTable lookupTable) {
                char val$startInclusive = c;
                while (true) {
                    lookupTable.set(val$startInclusive);
                    final char c = (char)(val$startInclusive + '\u0001');
                    if (val$startInclusive == c2) {
                        break;
                    }
                    val$startInclusive = c;
                }
            }
        };
    }
    
    public static CharMatcher is(final char c) {
        return new CharMatcher("CharMatcher.is(" + Integer.toHexString(c) + ")") {
            @Override
            public boolean matches(final char c) {
                return c == c;
            }
            
            @Override
            public CharMatcher or(final CharMatcher charMatcher) {
                if (charMatcher.matches(c)) {
                    return charMatcher;
                }
                return super.or(charMatcher);
            }
            
            @Override
            public CharMatcher precomputed() {
                return this;
            }
            
            @Override
            void setBits(final LookupTable lookupTable) {
                lookupTable.set(c);
            }
        };
    }
    
    @Override
    public boolean apply(final Character c) {
        return this.matches(c);
    }
    
    public abstract boolean matches(final char p0);
    
    public CharMatcher or(final CharMatcher charMatcher) {
        return new Or(this, Preconditions.checkNotNull(charMatcher));
    }
    
    public CharMatcher precomputed() {
        return Platform.precomputeCharMatcher(this);
    }
    
    CharMatcher precomputedInternal() {
        final char[] slowGetChars = this.slowGetChars();
        final int length = slowGetChars.length;
        if (length == 0) {
            return CharMatcher.NONE;
        }
        if (length == 1) {
            return is(slowGetChars[0]);
        }
        if (length < 63) {
            return SmallCharMatcher.from(slowGetChars, this.toString());
        }
        if (length < 1023) {
            return MediumCharMatcher.from(slowGetChars, this.toString());
        }
        final LookupTable bits = new LookupTable();
        this.setBits(bits);
        return new CharMatcher(this.toString()) {
            @Override
            public boolean matches(final char c) {
                return bits.get(c);
            }
            
            @Override
            public CharMatcher precomputed() {
                return this;
            }
        };
    }
    
    void setBits(final LookupTable lookupTable) {
        char c = '\0';
        while (true) {
            if (this.matches(c)) {
                lookupTable.set(c);
            }
            final char c2 = (char)(c + '\u0001');
            if (c == '\uffff') {
                break;
            }
            c = c2;
        }
    }
    
    char[] slowGetChars() {
        final char[] array = new char[65536];
        int i = 0;
        int n = 0;
        while (i <= 65535) {
            if (this.matches((char)i)) {
                final int n2 = n + 1;
                array[n] = (char)i;
                n = n2;
            }
            ++i;
        }
        final char[] array2 = new char[n];
        System.arraycopy(array, 0, array2, 0, n);
        return array2;
    }
    
    @Override
    public String toString() {
        return this.description;
    }
    
    CharMatcher withToString(final String s) {
        throw new UnsupportedOperationException();
    }
    
    private static final class LookupTable
    {
        int[] data;
        
        private LookupTable() {
            this.data = new int[2048];
        }
        
        boolean get(final char c) {
            return (this.data[c >> 5] & 1 << c) != 0x0;
        }
        
        void set(final char c) {
            final int[] data = this.data;
            final char c2 = (char)(c >> 5);
            data[c2] |= 1 << c;
        }
    }
    
    private static class Or extends CharMatcher
    {
        final CharMatcher first;
        final CharMatcher second;
        
        Or(final CharMatcher charMatcher, final CharMatcher charMatcher2) {
            this(charMatcher, charMatcher2, "CharMatcher.or(" + charMatcher + ", " + charMatcher2 + ")");
        }
        
        Or(final CharMatcher charMatcher, final CharMatcher charMatcher2, final String s) {
            super(s);
            this.first = Preconditions.checkNotNull(charMatcher);
            this.second = Preconditions.checkNotNull(charMatcher2);
        }
        
        @Override
        public boolean matches(final char c) {
            return this.first.matches(c) || this.second.matches(c);
        }
        
        @Override
        public CharMatcher or(final CharMatcher charMatcher) {
            return new Or(this, Preconditions.checkNotNull(charMatcher));
        }
        
        @Override
        CharMatcher withToString(final String s) {
            return new Or(this.first, this.second, s);
        }
    }
}
