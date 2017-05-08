// 
// Decompiled by Procyon v0.5.30
// 

package com.google.gson.stream;

import java.io.EOFException;
import java.io.IOException;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.JsonReaderInternalAccess;
import java.io.Reader;
import java.io.Closeable;

public class JsonReader implements Closeable
{
    private static final char[] NON_EXECUTE_PREFIX;
    private final char[] buffer;
    private final Reader in;
    private boolean lenient;
    private int limit;
    private int lineNumber;
    private int lineStart;
    private int peeked;
    private long peekedLong;
    private int peekedNumberLength;
    private String peekedString;
    private int pos;
    private int[] stack;
    private int stackSize;
    
    static {
        NON_EXECUTE_PREFIX = ")]}'\n".toCharArray();
        JsonReaderInternalAccess.INSTANCE = new JsonReaderInternalAccess() {
            @Override
            public void promoteNameToValue(final JsonReader jsonReader) throws IOException {
                if (jsonReader instanceof JsonTreeReader) {
                    ((JsonTreeReader)jsonReader).promoteNameToValue();
                    return;
                }
                int n;
                if ((n = jsonReader.peeked) == 0) {
                    n = jsonReader.doPeek();
                }
                if (n == 13) {
                    jsonReader.peeked = 9;
                    return;
                }
                if (n == 12) {
                    jsonReader.peeked = 8;
                    return;
                }
                if (n == 14) {
                    jsonReader.peeked = 10;
                    return;
                }
                throw new IllegalStateException("Expected a name but was " + jsonReader.peek() + " " + " at line " + jsonReader.getLineNumber() + " column " + jsonReader.getColumnNumber());
            }
        };
    }
    
    public JsonReader(final Reader in) {
        this.lenient = false;
        this.buffer = new char[1024];
        this.pos = 0;
        this.limit = 0;
        this.lineNumber = 0;
        this.lineStart = 0;
        this.peeked = 0;
        this.stack = new int[32];
        this.stackSize = 0;
        this.stack[this.stackSize++] = 6;
        if (in == null) {
            throw new NullPointerException("in == null");
        }
        this.in = in;
    }
    
    private void checkLenient() throws IOException {
        if (!this.lenient) {
            throw this.syntaxError("Use JsonReader.setLenient(true) to accept malformed JSON");
        }
    }
    
    private void consumeNonExecutePrefix() throws IOException {
        this.nextNonWhitespace(true);
        --this.pos;
        if (this.pos + JsonReader.NON_EXECUTE_PREFIX.length <= this.limit || this.fillBuffer(JsonReader.NON_EXECUTE_PREFIX.length)) {
            for (int i = 0; i < JsonReader.NON_EXECUTE_PREFIX.length; ++i) {
                if (this.buffer[this.pos + i] != JsonReader.NON_EXECUTE_PREFIX[i]) {
                    return;
                }
            }
            this.pos += JsonReader.NON_EXECUTE_PREFIX.length;
        }
    }
    
    private int doPeek() throws IOException {
        final int n = this.stack[this.stackSize - 1];
        if (n == 1) {
            this.stack[this.stackSize - 1] = 2;
        }
        else if (n == 2) {
            switch (this.nextNonWhitespace(true)) {
                case 59: {
                    this.checkLenient();
                }
                case 44: {
                    break;
                }
                default: {
                    throw this.syntaxError("Unterminated array");
                }
                case 93: {
                    return this.peeked = 4;
                }
            }
        }
        else if (n == 3 || n == 5) {
            this.stack[this.stackSize - 1] = 4;
            if (n == 5) {
                switch (this.nextNonWhitespace(true)) {
                    default: {
                        throw this.syntaxError("Unterminated object");
                    }
                    case 125: {
                        return this.peeked = 2;
                    }
                    case 59: {
                        this.checkLenient();
                    }
                    case 44: {
                        break;
                    }
                }
            }
            final int nextNonWhitespace = this.nextNonWhitespace(true);
            switch (nextNonWhitespace) {
                default: {
                    this.checkLenient();
                    --this.pos;
                    if (this.isLiteral((char)nextNonWhitespace)) {
                        return this.peeked = 14;
                    }
                    throw this.syntaxError("Expected name");
                }
                case 34: {
                    return this.peeked = 13;
                }
                case 39: {
                    this.checkLenient();
                    return this.peeked = 12;
                }
                case 125: {
                    if (n != 5) {
                        return this.peeked = 2;
                    }
                    throw this.syntaxError("Expected name");
                }
            }
        }
        else if (n == 4) {
            this.stack[this.stackSize - 1] = 5;
            switch (this.nextNonWhitespace(true)) {
                case 58: {
                    break;
                }
                default: {
                    throw this.syntaxError("Expected ':'");
                }
                case 61: {
                    this.checkLenient();
                    if ((this.pos < this.limit || this.fillBuffer(1)) && this.buffer[this.pos] == '>') {
                        ++this.pos;
                        break;
                    }
                    break;
                }
            }
        }
        else if (n == 6) {
            if (this.lenient) {
                this.consumeNonExecutePrefix();
            }
            this.stack[this.stackSize - 1] = 7;
        }
        else if (n == 7) {
            if (this.nextNonWhitespace(false) == -1) {
                return this.peeked = 17;
            }
            this.checkLenient();
            --this.pos;
        }
        else if (n == 8) {
            throw new IllegalStateException("JsonReader is closed");
        }
        switch (this.nextNonWhitespace(true)) {
            default: {
                --this.pos;
                if (this.stackSize == 1) {
                    this.checkLenient();
                }
                int n2 = this.peekKeyword();
                if (n2 != 0 || (n2 = this.peekNumber()) != 0) {
                    return n2;
                }
                if (!this.isLiteral(this.buffer[this.pos])) {
                    throw this.syntaxError("Expected value");
                }
                this.checkLenient();
                return this.peeked = 10;
            }
            case 93: {
                if (n == 1) {
                    return this.peeked = 4;
                }
            }
            case 44:
            case 59: {
                if (n == 1 || n == 2) {
                    this.checkLenient();
                    --this.pos;
                    return this.peeked = 7;
                }
                throw this.syntaxError("Unexpected value");
            }
            case 39: {
                this.checkLenient();
                return this.peeked = 8;
            }
            case 34: {
                if (this.stackSize == 1) {
                    this.checkLenient();
                }
                return this.peeked = 9;
            }
            case 91: {
                return this.peeked = 3;
            }
            case 123: {
                return this.peeked = 1;
            }
        }
    }
    
    private boolean fillBuffer(int n) throws IOException {
        final boolean b = false;
        final char[] buffer = this.buffer;
        this.lineStart -= this.pos;
        if (this.limit != this.pos) {
            this.limit -= this.pos;
            System.arraycopy(buffer, this.pos, buffer, 0, this.limit);
        }
        else {
            this.limit = 0;
        }
        this.pos = 0;
        int n2;
        do {
            final int read = this.in.read(buffer, this.limit, buffer.length - this.limit);
            final boolean b2 = b;
            if (read == -1) {
                return b2;
            }
            this.limit += read;
            n2 = n;
            if (this.lineNumber != 0) {
                continue;
            }
            n2 = n;
            if (this.lineStart != 0) {
                continue;
            }
            n2 = n;
            if (this.limit <= 0) {
                continue;
            }
            n2 = n;
            if (buffer[0] != '\ufeff') {
                continue;
            }
            ++this.pos;
            ++this.lineStart;
            n2 = n + 1;
        } while (this.limit < (n = n2));
        return true;
    }
    
    private int getColumnNumber() {
        return this.pos - this.lineStart + 1;
    }
    
    private int getLineNumber() {
        return this.lineNumber + 1;
    }
    
    private boolean isLiteral(final char c) throws IOException {
        switch (c) {
            default: {
                return true;
            }
            case '#':
            case '/':
            case ';':
            case '=':
            case '\\': {
                this.checkLenient();
            }
            case '\t':
            case '\n':
            case '\f':
            case '\r':
            case ' ':
            case ',':
            case ':':
            case '[':
            case ']':
            case '{':
            case '}': {
                return false;
            }
        }
    }
    
    private int nextNonWhitespace(final boolean b) throws IOException {
        final char[] buffer = this.buffer;
        int pos = this.pos;
        int n = this.limit;
        while (true) {
            int limit = n;
            int pos2 = pos;
            if (pos == n) {
                this.pos = pos;
                if (!this.fillBuffer(1)) {
                    if (b) {
                        throw new EOFException("End of input at line " + this.getLineNumber() + " column " + this.getColumnNumber());
                    }
                    return -1;
                }
                else {
                    pos2 = this.pos;
                    limit = this.limit;
                }
            }
            pos = pos2 + 1;
            final char c = buffer[pos2];
            if (c == '\n') {
                ++this.lineNumber;
                this.lineStart = pos;
                n = limit;
            }
            else if (c != ' ' && c != '\r') {
                if (c == '\t') {
                    n = limit;
                }
                else if (c == '/') {
                    if ((this.pos = pos) == limit) {
                        --this.pos;
                        final boolean fillBuffer = this.fillBuffer(2);
                        ++this.pos;
                        if (!fillBuffer) {
                            return c;
                        }
                    }
                    this.checkLenient();
                    switch (buffer[this.pos]) {
                        default: {
                            return c;
                        }
                        case '*': {
                            ++this.pos;
                            if (!this.skipTo("*/")) {
                                throw this.syntaxError("Unterminated comment");
                            }
                            pos = this.pos + 2;
                            n = this.limit;
                            continue;
                        }
                        case '/': {
                            ++this.pos;
                            this.skipToEndOfLine();
                            pos = this.pos;
                            n = this.limit;
                            continue;
                        }
                    }
                }
                else {
                    if (c != '#') {
                        this.pos = pos;
                        return c;
                    }
                    this.pos = pos;
                    this.checkLenient();
                    this.skipToEndOfLine();
                    pos = this.pos;
                    n = this.limit;
                }
            }
            else {
                n = limit;
            }
        }
    }
    
    private String nextQuotedValue(final char c) throws IOException {
        final char[] buffer = this.buffer;
        final StringBuilder sb = new StringBuilder();
        do {
            int i = this.pos;
            int limit = this.limit;
            int n = i;
            while (i < limit) {
                final int lineStart = i + 1;
                final char c2 = buffer[i];
                if (c2 == c) {
                    sb.append(buffer, n, (this.pos = lineStart) - n - 1);
                    return sb.toString();
                }
                int limit2;
                int n2;
                if (c2 == '\\') {
                    sb.append(buffer, n, (this.pos = lineStart) - n - 1);
                    sb.append(this.readEscapeCharacter());
                    i = this.pos;
                    limit2 = this.limit;
                    n2 = i;
                }
                else {
                    limit2 = limit;
                    i = lineStart;
                    n2 = n;
                    if (c2 == '\n') {
                        ++this.lineNumber;
                        this.lineStart = lineStart;
                        limit2 = limit;
                        i = lineStart;
                        n2 = n;
                    }
                }
                limit = limit2;
                n = n2;
            }
            sb.append(buffer, n, i - n);
            this.pos = i;
        } while (this.fillBuffer(1));
        throw this.syntaxError("Unterminated string");
    }
    
    private String nextUnquotedValue() throws IOException {
        StringBuilder sb = null;
        int n = 0;
    Label_0188:
        while (true) {
            StringBuilder sb2;
            int n2;
            while (true) {
                if (this.pos + n < this.limit) {
                    sb2 = sb;
                    n2 = n;
                    switch (this.buffer[this.pos + n]) {
                        default: {
                            ++n;
                            continue;
                        }
                        case '#':
                        case '/':
                        case ';':
                        case '=':
                        case '\\': {
                            this.checkLenient();
                            n2 = n;
                            sb2 = sb;
                        }
                        case '\t':
                        case '\n':
                        case '\f':
                        case '\r':
                        case ' ':
                        case ',':
                        case ':':
                        case '[':
                        case ']':
                        case '{':
                        case '}': {
                            break Label_0188;
                        }
                    }
                }
                else if (n < this.buffer.length) {
                    sb2 = sb;
                    n2 = n;
                    if (this.fillBuffer(n + 1)) {
                        continue;
                    }
                    break;
                }
                else {
                    if ((sb2 = sb) == null) {
                        sb2 = new StringBuilder();
                    }
                    sb2.append(this.buffer, this.pos, n);
                    this.pos += n;
                    n2 = 0;
                    n = 0;
                    sb = sb2;
                    if (!this.fillBuffer(1)) {
                        break;
                    }
                    continue;
                }
            }
            String string;
            if (sb2 == null) {
                string = new String(this.buffer, this.pos, n2);
            }
            else {
                sb2.append(this.buffer, this.pos, n2);
                string = sb2.toString();
            }
            this.pos += n2;
            return string;
            continue Label_0188;
        }
    }
    
    private int peekKeyword() throws IOException {
        final char c = this.buffer[this.pos];
        String s;
        String s2;
        int peeked;
        if (c == 't' || c == 'T') {
            s = "true";
            s2 = "TRUE";
            peeked = 5;
        }
        else if (c == 'f' || c == 'F') {
            s = "false";
            s2 = "FALSE";
            peeked = 6;
        }
        else {
            if (c != 'n' && c != 'N') {
                return 0;
            }
            s = "null";
            s2 = "NULL";
            peeked = 7;
        }
        final int length = s.length();
        for (int i = 1; i < length; ++i) {
            if (this.pos + i >= this.limit && !this.fillBuffer(i + 1)) {
                return 0;
            }
            final char c2 = this.buffer[this.pos + i];
            if (c2 != s.charAt(i) && c2 != s2.charAt(i)) {
                return 0;
            }
        }
        if ((this.pos + length < this.limit || this.fillBuffer(length + 1)) && this.isLiteral(this.buffer[this.pos + length])) {
            return 0;
        }
        this.pos += length;
        return this.peeked = peeked;
    }
    
    private int peekNumber() throws IOException {
        final char[] buffer = this.buffer;
        int pos = this.pos;
        int limit = this.limit;
        long peekedLong = 0L;
        int n = 0;
        boolean b = true;
        int n2 = 0;
        int peekedNumberLength = 0;
        while (true) {
            char c = '\0';
            Label_0208: {
                while (true) {
                    int limit2 = limit;
                    int pos2 = pos;
                    if (pos + peekedNumberLength == limit) {
                        if (peekedNumberLength == buffer.length) {
                            return 0;
                        }
                        if (!this.fillBuffer(peekedNumberLength + 1)) {
                            break;
                        }
                        pos2 = this.pos;
                        limit2 = this.limit;
                    }
                    c = buffer[pos2 + peekedNumberLength];
                    long n3 = 0L;
                    int n4 = 0;
                    boolean b2 = false;
                    int n5 = 0;
                    switch (c) {
                        default: {
                            if (c < '0' || c > '9') {
                                break Label_0208;
                            }
                            if (n2 == 1 || n2 == 0) {
                                n3 = -(c - '0');
                                n4 = 2;
                                b2 = b;
                                n5 = n;
                                break;
                            }
                            if (n2 == 2) {
                                if (peekedLong == 0L) {
                                    return 0;
                                }
                                n3 = 10L * peekedLong - (c - '0');
                                boolean b3;
                                if (peekedLong > -922337203685477580L || (peekedLong == -922337203685477580L && n3 < peekedLong)) {
                                    b3 = true;
                                }
                                else {
                                    b3 = false;
                                }
                                b2 = (b & b3);
                                n4 = n2;
                                n5 = n;
                                break;
                            }
                            else {
                                if (n2 == 3) {
                                    n4 = 4;
                                    b2 = b;
                                    n5 = n;
                                    n3 = peekedLong;
                                    break;
                                }
                                if (n2 != 5) {
                                    b2 = b;
                                    n4 = n2;
                                    n5 = n;
                                    n3 = peekedLong;
                                    if (n2 != 6) {
                                        break;
                                    }
                                }
                                n4 = 7;
                                b2 = b;
                                n5 = n;
                                n3 = peekedLong;
                                break;
                            }
                            break;
                        }
                        case 45: {
                            if (n2 == 0) {
                                n5 = 1;
                                n4 = 1;
                                n3 = peekedLong;
                                b2 = b;
                                break;
                            }
                            if (n2 == 5) {
                                n4 = 6;
                                b2 = b;
                                n5 = n;
                                n3 = peekedLong;
                                break;
                            }
                            return 0;
                        }
                        case 43: {
                            if (n2 == 5) {
                                n4 = 6;
                                b2 = b;
                                n5 = n;
                                n3 = peekedLong;
                                break;
                            }
                            return 0;
                        }
                        case 69:
                        case 101: {
                            if (n2 == 2 || n2 == 4) {
                                n4 = 5;
                                b2 = b;
                                n5 = n;
                                n3 = peekedLong;
                                break;
                            }
                            return 0;
                        }
                        case 46: {
                            if (n2 == 2) {
                                n4 = 3;
                                b2 = b;
                                n5 = n;
                                n3 = peekedLong;
                                break;
                            }
                            return 0;
                        }
                    }
                    ++peekedNumberLength;
                    b = b2;
                    limit = limit2;
                    n2 = n4;
                    n = n5;
                    pos = pos2;
                    peekedLong = n3;
                }
                if (n2 == 2 && b && (peekedLong != Long.MIN_VALUE || n != 0)) {
                    if (n == 0) {
                        peekedLong = -peekedLong;
                    }
                    this.peekedLong = peekedLong;
                    this.pos += peekedNumberLength;
                    return this.peeked = 15;
                }
                if (n2 == 2 || n2 == 4 || n2 == 7) {
                    this.peekedNumberLength = peekedNumberLength;
                    return this.peeked = 16;
                }
                return 0;
            }
            if (this.isLiteral(c)) {
                return 0;
            }
            continue;
        }
    }
    
    private void push(final int n) {
        if (this.stackSize == this.stack.length) {
            final int[] stack = new int[this.stackSize * 2];
            System.arraycopy(this.stack, 0, stack, 0, this.stackSize);
            this.stack = stack;
        }
        this.stack[this.stackSize++] = n;
    }
    
    private char readEscapeCharacter() throws IOException {
        if (this.pos == this.limit && !this.fillBuffer(1)) {
            throw this.syntaxError("Unterminated escape sequence");
        }
        final char c = this.buffer[this.pos++];
        switch (c) {
            case 117: {
                if (this.pos + 4 > this.limit && !this.fillBuffer(4)) {
                    throw this.syntaxError("Unterminated escape sequence");
                }
                char c2 = '\0';
                int i = 0;
                while (i < (i = this.pos) + 4) {
                    final char c3 = this.buffer[i];
                    final char c4 = (char)(c2 << 4);
                    if (c3 >= '0' && c3 <= '9') {
                        c2 = (char)(c3 - '0' + c4);
                    }
                    else if (c3 >= 'a' && c3 <= 'f') {
                        c2 = (char)(c3 - 'a' + '\n' + c4);
                    }
                    else {
                        if (c3 < 'A' || c3 > 'F') {
                            throw new NumberFormatException("\\u" + new String(this.buffer, this.pos, 4));
                        }
                        c2 = (char)(c3 - 'A' + '\n' + c4);
                    }
                    ++i;
                }
                this.pos += 4;
                return c2;
            }
            case 116: {
                return '\t';
            }
            case 98: {
                return '\b';
            }
            case 110: {
                return '\n';
            }
            case 114: {
                return '\r';
            }
            case 102: {
                return '\f';
            }
            case 10: {
                ++this.lineNumber;
                this.lineStart = this.pos;
                break;
            }
        }
        return c;
    }
    
    private void skipQuotedValue(final char c) throws IOException {
        final char[] buffer = this.buffer;
        do {
            int i = this.pos;
            int limit2;
            for (int limit = this.limit; i < limit; limit = limit2) {
                final int lineStart = i + 1;
                final char c2 = buffer[i];
                if (c2 == c) {
                    this.pos = lineStart;
                    return;
                }
                if (c2 == '\\') {
                    this.pos = lineStart;
                    this.readEscapeCharacter();
                    i = this.pos;
                    limit2 = this.limit;
                }
                else {
                    limit2 = limit;
                    i = lineStart;
                    if (c2 == '\n') {
                        ++this.lineNumber;
                        this.lineStart = lineStart;
                        limit2 = limit;
                        i = lineStart;
                    }
                }
            }
            this.pos = i;
        } while (this.fillBuffer(1));
        throw this.syntaxError("Unterminated string");
    }
    
    private boolean skipTo(final String s) throws IOException {
        while (this.pos + s.length() <= this.limit || this.fillBuffer(s.length())) {
            Label_0061: {
                if (this.buffer[this.pos] != '\n') {
                    for (int i = 0; i < s.length(); ++i) {
                        if (this.buffer[this.pos + i] != s.charAt(i)) {
                            break Label_0061;
                        }
                    }
                    return true;
                }
                ++this.lineNumber;
                this.lineStart = this.pos + 1;
            }
            ++this.pos;
        }
        return false;
    }
    
    private void skipToEndOfLine() throws IOException {
        while (this.pos < this.limit || this.fillBuffer(1)) {
            final char c = this.buffer[this.pos++];
            if (c == '\n') {
                ++this.lineNumber;
                this.lineStart = this.pos;
                break;
            }
            if (c == '\r') {
                return;
            }
        }
    }
    
    private void skipUnquotedValue() throws IOException {
        do {
            int n = 0;
            while (this.pos + n < this.limit) {
                switch (this.buffer[this.pos + n]) {
                    default: {
                        ++n;
                        continue;
                    }
                    case '#':
                    case '/':
                    case ';':
                    case '=':
                    case '\\': {
                        this.checkLenient();
                    }
                    case '\t':
                    case '\n':
                    case '\f':
                    case '\r':
                    case ' ':
                    case ',':
                    case ':':
                    case '[':
                    case ']':
                    case '{':
                    case '}': {
                        this.pos += n;
                        return;
                    }
                }
            }
            this.pos += n;
        } while (this.fillBuffer(1));
    }
    
    private IOException syntaxError(final String s) throws IOException {
        throw new MalformedJsonException(s + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
    }
    
    public void beginArray() throws IOException {
        int n;
        if ((n = this.peeked) == 0) {
            n = this.doPeek();
        }
        if (n == 3) {
            this.push(1);
            this.peeked = 0;
            return;
        }
        throw new IllegalStateException("Expected BEGIN_ARRAY but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
    }
    
    public void beginObject() throws IOException {
        int n;
        if ((n = this.peeked) == 0) {
            n = this.doPeek();
        }
        if (n == 1) {
            this.push(3);
            this.peeked = 0;
            return;
        }
        throw new IllegalStateException("Expected BEGIN_OBJECT but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
    }
    
    @Override
    public void close() throws IOException {
        this.peeked = 0;
        this.stack[0] = 8;
        this.stackSize = 1;
        this.in.close();
    }
    
    public void endArray() throws IOException {
        int n;
        if ((n = this.peeked) == 0) {
            n = this.doPeek();
        }
        if (n == 4) {
            --this.stackSize;
            this.peeked = 0;
            return;
        }
        throw new IllegalStateException("Expected END_ARRAY but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
    }
    
    public void endObject() throws IOException {
        int n;
        if ((n = this.peeked) == 0) {
            n = this.doPeek();
        }
        if (n == 2) {
            --this.stackSize;
            this.peeked = 0;
            return;
        }
        throw new IllegalStateException("Expected END_OBJECT but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
    }
    
    public boolean hasNext() throws IOException {
        int n;
        if ((n = this.peeked) == 0) {
            n = this.doPeek();
        }
        return n != 2 && n != 4;
    }
    
    public final boolean isLenient() {
        return this.lenient;
    }
    
    public boolean nextBoolean() throws IOException {
        int n;
        if ((n = this.peeked) == 0) {
            n = this.doPeek();
        }
        if (n == 5) {
            this.peeked = 0;
            return true;
        }
        if (n == 6) {
            this.peeked = 0;
            return false;
        }
        throw new IllegalStateException("Expected a boolean but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
    }
    
    public double nextDouble() throws IOException {
        int n;
        if ((n = this.peeked) == 0) {
            n = this.doPeek();
        }
        if (n == 15) {
            this.peeked = 0;
            return this.peekedLong;
        }
        if (n == 16) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        }
        else if (n == 8 || n == 9) {
            char c;
            if (n == 8) {
                c = '\'';
            }
            else {
                c = '\"';
            }
            this.peekedString = this.nextQuotedValue(c);
        }
        else if (n == 10) {
            this.peekedString = this.nextUnquotedValue();
        }
        else if (n != 11) {
            throw new IllegalStateException("Expected a double but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
        }
        this.peeked = 11;
        final double double1 = Double.parseDouble(this.peekedString);
        if (!this.lenient && (Double.isNaN(double1) || Double.isInfinite(double1))) {
            throw new MalformedJsonException("JSON forbids NaN and infinities: " + double1 + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
        }
        this.peekedString = null;
        this.peeked = 0;
        return double1;
    }
    
    public int nextInt() throws IOException {
        int n;
        if ((n = this.peeked) == 0) {
            n = this.doPeek();
        }
        if (n == 15) {
            final int n2 = (int)this.peekedLong;
            if (this.peekedLong != n2) {
                throw new NumberFormatException("Expected an int but was " + this.peekedLong + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
            }
            this.peeked = 0;
            return n2;
        }
        else {
            while (true) {
                Label_0152: {
                    if (n == 16) {
                        this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
                        this.pos += this.peekedNumberLength;
                        break Label_0152;
                    }
                    Label_0289: {
                        if (n != 8 && n != 9) {
                            break Label_0289;
                        }
                        Label_0283: {
                            if (n != 8) {
                                break Label_0283;
                            }
                            char c = '\'';
                            while (true) {
                                this.peekedString = this.nextQuotedValue(c);
                                try {
                                    final int int1 = Integer.parseInt(this.peekedString);
                                    this.peeked = 0;
                                    return int1;
                                    c = '\"';
                                    continue;
                                    throw new IllegalStateException("Expected an int but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
                                    this.peekedString = null;
                                    this.peeked = 0;
                                    return;
                                }
                                catch (NumberFormatException ex) {
                                    break;
                                }
                            }
                        }
                    }
                }
                this.peeked = 11;
                final double double1 = Double.parseDouble(this.peekedString);
                final int n3 = (int)double1;
                if (n3 != double1) {
                    throw new NumberFormatException("Expected an int but was " + this.peekedString + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
                }
                continue;
            }
        }
    }
    
    public long nextLong() throws IOException {
        int n;
        if ((n = this.peeked) == 0) {
            n = this.doPeek();
        }
        if (n == 15) {
            this.peeked = 0;
            return this.peekedLong;
        }
        Label_0081: {
            if (n == 16) {
                this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
                this.pos += this.peekedNumberLength;
            }
            else {
                if (n == 8 || n == 9) {
                    while (true) {
                        Label_0217: {
                            if (n != 8) {
                                break Label_0217;
                            }
                            final char c = '\'';
                            this.peekedString = this.nextQuotedValue(c);
                            try {
                                final long long1 = Long.parseLong(this.peekedString);
                                this.peeked = 0;
                                return long1;
                            }
                            catch (NumberFormatException ex) {
                                break Label_0081;
                            }
                        }
                        final char c = '\"';
                        continue;
                    }
                }
                throw new IllegalStateException("Expected a long but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
            }
        }
        this.peeked = 11;
        final double double1 = Double.parseDouble(this.peekedString);
        final long n2 = (long)double1;
        if (n2 != double1) {
            throw new NumberFormatException("Expected a long but was " + this.peekedString + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
        }
        this.peekedString = null;
        this.peeked = 0;
        return n2;
    }
    
    public String nextName() throws IOException {
        int n;
        if ((n = this.peeked) == 0) {
            n = this.doPeek();
        }
        String s;
        if (n == 14) {
            s = this.nextUnquotedValue();
        }
        else if (n == 12) {
            s = this.nextQuotedValue('\'');
        }
        else {
            if (n != 13) {
                throw new IllegalStateException("Expected a name but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
            }
            s = this.nextQuotedValue('\"');
        }
        this.peeked = 0;
        return s;
    }
    
    public void nextNull() throws IOException {
        int n;
        if ((n = this.peeked) == 0) {
            n = this.doPeek();
        }
        if (n == 7) {
            this.peeked = 0;
            return;
        }
        throw new IllegalStateException("Expected null but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
    }
    
    public String nextString() throws IOException {
        int n;
        if ((n = this.peeked) == 0) {
            n = this.doPeek();
        }
        String s;
        if (n == 10) {
            s = this.nextUnquotedValue();
        }
        else if (n == 8) {
            s = this.nextQuotedValue('\'');
        }
        else if (n == 9) {
            s = this.nextQuotedValue('\"');
        }
        else if (n == 11) {
            s = this.peekedString;
            this.peekedString = null;
        }
        else if (n == 15) {
            s = Long.toString(this.peekedLong);
        }
        else {
            if (n != 16) {
                throw new IllegalStateException("Expected a string but was " + this.peek() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber());
            }
            s = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        }
        this.peeked = 0;
        return s;
    }
    
    public JsonToken peek() throws IOException {
        int n;
        if ((n = this.peeked) == 0) {
            n = this.doPeek();
        }
        switch (n) {
            default: {
                throw new AssertionError();
            }
            case 1: {
                return JsonToken.BEGIN_OBJECT;
            }
            case 2: {
                return JsonToken.END_OBJECT;
            }
            case 3: {
                return JsonToken.BEGIN_ARRAY;
            }
            case 4: {
                return JsonToken.END_ARRAY;
            }
            case 12:
            case 13:
            case 14: {
                return JsonToken.NAME;
            }
            case 5:
            case 6: {
                return JsonToken.BOOLEAN;
            }
            case 7: {
                return JsonToken.NULL;
            }
            case 8:
            case 9:
            case 10:
            case 11: {
                return JsonToken.STRING;
            }
            case 15:
            case 16: {
                return JsonToken.NUMBER;
            }
            case 17: {
                return JsonToken.END_DOCUMENT;
            }
        }
    }
    
    public final void setLenient(final boolean lenient) {
        this.lenient = lenient;
    }
    
    public void skipValue() throws IOException {
        int n = 0;
        int n3;
        do {
            int n2;
            if ((n2 = this.peeked) == 0) {
                n2 = this.doPeek();
            }
            if (n2 == 3) {
                this.push(1);
                n3 = n + 1;
            }
            else if (n2 == 1) {
                this.push(3);
                n3 = n + 1;
            }
            else if (n2 == 4) {
                --this.stackSize;
                n3 = n - 1;
            }
            else if (n2 == 2) {
                --this.stackSize;
                n3 = n - 1;
            }
            else if (n2 == 14 || n2 == 10) {
                this.skipUnquotedValue();
                n3 = n;
            }
            else if (n2 == 8 || n2 == 12) {
                this.skipQuotedValue('\'');
                n3 = n;
            }
            else if (n2 == 9 || n2 == 13) {
                this.skipQuotedValue('\"');
                n3 = n;
            }
            else {
                n3 = n;
                if (n2 == 16) {
                    this.pos += this.peekedNumberLength;
                    n3 = n;
                }
            }
            this.peeked = 0;
        } while ((n = n3) != 0);
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber();
    }
}
