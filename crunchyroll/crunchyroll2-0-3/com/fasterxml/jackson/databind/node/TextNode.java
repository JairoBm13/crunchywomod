// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.node;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.ByteArrayBuilder;
import java.io.IOException;
import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.io.NumberInput;
import com.fasterxml.jackson.core.Base64Variant;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.io.CharTypes;

public final class TextNode extends ValueNode
{
    static final TextNode EMPTY_STRING_NODE;
    final String _value;
    
    static {
        EMPTY_STRING_NODE = new TextNode("");
    }
    
    public TextNode(final String value) {
        this._value = value;
    }
    
    protected static void appendQuoted(final StringBuilder sb, final String s) {
        sb.append('\"');
        CharTypes.appendQuoted(sb, s);
        sb.append('\"');
    }
    
    public static TextNode valueOf(final String s) {
        if (s == null) {
            return null;
        }
        if (s.length() == 0) {
            return TextNode.EMPTY_STRING_NODE;
        }
        return new TextNode(s);
    }
    
    protected void _reportBase64EOF() throws JsonParseException {
        throw new JsonParseException("Unexpected end-of-String when base64 content", JsonLocation.NA);
    }
    
    protected void _reportInvalidBase64(final Base64Variant base64Variant, final char c, final int n) throws JsonParseException {
        this._reportInvalidBase64(base64Variant, c, n, null);
    }
    
    protected void _reportInvalidBase64(final Base64Variant base64Variant, final char c, final int n, final String s) throws JsonParseException {
        String s2;
        if (c <= ' ') {
            s2 = "Illegal white space character (code 0x" + Integer.toHexString(c) + ") as character #" + (n + 1) + " of 4-char base64 unit: can only used between units";
        }
        else if (base64Variant.usesPaddingChar(c)) {
            s2 = "Unexpected padding character ('" + base64Variant.getPaddingChar() + "') as character #" + (n + 1) + " of 4-char base64 unit: padding only legal as 3rd or 4th character";
        }
        else if (!Character.isDefined(c) || Character.isISOControl(c)) {
            s2 = "Illegal character (code 0x" + Integer.toHexString(c) + ") in base64 content";
        }
        else {
            s2 = "Illegal character '" + c + "' (code 0x" + Integer.toHexString(c) + ") in base64 content";
        }
        String string = s2;
        if (s != null) {
            string = s2 + ": " + s;
        }
        throw new JsonParseException(string, JsonLocation.NA);
    }
    
    @Override
    public boolean asBoolean(final boolean b) {
        boolean b2 = b;
        if (this._value != null) {
            b2 = b;
            if ("true".equals(this._value.trim())) {
                b2 = true;
            }
        }
        return b2;
    }
    
    @Override
    public int asInt(final int n) {
        return NumberInput.parseAsInt(this._value, n);
    }
    
    @Override
    public String asText() {
        return this._value;
    }
    
    @Override
    public JsonToken asToken() {
        return JsonToken.VALUE_STRING;
    }
    
    @Override
    public byte[] binaryValue() throws IOException {
        return this.getBinaryValue(Base64Variants.getDefaultVariant());
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = false;
        boolean b2;
        if (o == this) {
            b2 = true;
        }
        else {
            b2 = b;
            if (o != null) {
                b2 = b;
                if (o.getClass() == this.getClass()) {
                    return ((TextNode)o)._value.equals(this._value);
                }
            }
        }
        return b2;
    }
    
    public byte[] getBinaryValue(final Base64Variant base64Variant) throws IOException {
        final ByteArrayBuilder byteArrayBuilder = new ByteArrayBuilder(100);
        final String value = this._value;
        final int length = value.length();
        int i = 0;
    Label_0051:
        while (i < length) {
            int n;
            char char1;
            while (true) {
                n = i + 1;
                char1 = value.charAt(i);
                if (n >= length) {
                    break Label_0051;
                }
                if (char1 > ' ') {
                    break;
                }
                i = n;
            }
            final int decodeBase64Char = base64Variant.decodeBase64Char(char1);
            if (decodeBase64Char < 0) {
                this._reportInvalidBase64(base64Variant, char1, 0);
            }
            if (n >= length) {
                this._reportBase64EOF();
            }
            final int n2 = n + 1;
            final char char2 = value.charAt(n);
            final int decodeBase64Char2 = base64Variant.decodeBase64Char(char2);
            if (decodeBase64Char2 < 0) {
                this._reportInvalidBase64(base64Variant, char2, 1);
            }
            final int n3 = decodeBase64Char << 6 | decodeBase64Char2;
            if (n2 >= length) {
                if (!base64Variant.usesPadding()) {
                    byteArrayBuilder.append(n3 >> 4);
                    break;
                }
                this._reportBase64EOF();
            }
            final int n4 = n2 + 1;
            final char char3 = value.charAt(n2);
            final int decodeBase64Char3 = base64Variant.decodeBase64Char(char3);
            if (decodeBase64Char3 < 0) {
                if (decodeBase64Char3 != -2) {
                    this._reportInvalidBase64(base64Variant, char3, 2);
                }
                if (n4 >= length) {
                    this._reportBase64EOF();
                }
                i = n4 + 1;
                final char char4 = value.charAt(n4);
                if (!base64Variant.usesPaddingChar(char4)) {
                    this._reportInvalidBase64(base64Variant, char4, 3, "expected padding character '" + base64Variant.getPaddingChar() + "'");
                }
                byteArrayBuilder.append(n3 >> 4);
            }
            else {
                final int n5 = n3 << 6 | decodeBase64Char3;
                if (n4 >= length) {
                    if (!base64Variant.usesPadding()) {
                        byteArrayBuilder.appendTwoBytes(n5 >> 2);
                        break;
                    }
                    this._reportBase64EOF();
                }
                i = n4 + 1;
                final char char5 = value.charAt(n4);
                final int decodeBase64Char4 = base64Variant.decodeBase64Char(char5);
                if (decodeBase64Char4 < 0) {
                    if (decodeBase64Char4 != -2) {
                        this._reportInvalidBase64(base64Variant, char5, 3);
                    }
                    byteArrayBuilder.appendTwoBytes(n5 >> 2);
                }
                else {
                    byteArrayBuilder.appendThreeBytes(n5 << 6 | decodeBase64Char4);
                }
            }
        }
        return byteArrayBuilder.toByteArray();
    }
    
    @Override
    public JsonNodeType getNodeType() {
        return JsonNodeType.STRING;
    }
    
    @Override
    public int hashCode() {
        return this._value.hashCode();
    }
    
    @Override
    public final void serialize(final JsonGenerator jsonGenerator, final SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        if (this._value == null) {
            jsonGenerator.writeNull();
            return;
        }
        jsonGenerator.writeString(this._value);
    }
    
    @Override
    public String textValue() {
        return this._value;
    }
    
    @Override
    public String toString() {
        final int length = this._value.length();
        final StringBuilder sb = new StringBuilder((length >> 4) + (length + 2));
        appendQuoted(sb, this._value);
        return sb.toString();
    }
}
