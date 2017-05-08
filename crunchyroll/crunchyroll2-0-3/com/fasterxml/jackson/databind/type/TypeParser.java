// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.type;

import java.util.StringTokenizer;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.io.Serializable;

public class TypeParser implements Serializable
{
    protected final TypeFactory _factory;
    
    public TypeParser(final TypeFactory factory) {
        this._factory = factory;
    }
    
    protected IllegalArgumentException _problem(final MyTokenizer myTokenizer, final String s) {
        return new IllegalArgumentException("Failed to parse type '" + myTokenizer.getAllInput() + "' (remaining: '" + myTokenizer.getRemainingInput() + "'): " + s);
    }
    
    protected Class<?> findClass(final String s, final MyTokenizer myTokenizer) {
        try {
            return ClassUtil.findClass(s);
        }
        catch (Exception ex) {
            if (ex instanceof RuntimeException) {
                throw (RuntimeException)ex;
            }
            throw this._problem(myTokenizer, "Can not locate class '" + s + "', problem: " + ex.getMessage());
        }
    }
    
    public JavaType parse(final String s) throws IllegalArgumentException {
        final MyTokenizer myTokenizer = new MyTokenizer(s.trim());
        final JavaType type = this.parseType(myTokenizer);
        if (myTokenizer.hasMoreTokens()) {
            throw this._problem(myTokenizer, "Unexpected tokens after complete type");
        }
        return type;
    }
    
    protected JavaType parseType(final MyTokenizer myTokenizer) throws IllegalArgumentException {
        if (!myTokenizer.hasMoreTokens()) {
            throw this._problem(myTokenizer, "Unexpected end-of-string");
        }
        final Class<?> class1 = this.findClass(myTokenizer.nextToken(), myTokenizer);
        if (myTokenizer.hasMoreTokens()) {
            final String nextToken = myTokenizer.nextToken();
            if ("<".equals(nextToken)) {
                return this._factory._fromParameterizedClass(class1, this.parseTypes(myTokenizer));
            }
            myTokenizer.pushBack(nextToken);
        }
        return this._factory._fromClass(class1, null);
    }
    
    protected List<JavaType> parseTypes(final MyTokenizer myTokenizer) throws IllegalArgumentException {
        final ArrayList<JavaType> list = new ArrayList<JavaType>();
        while (myTokenizer.hasMoreTokens()) {
            list.add(this.parseType(myTokenizer));
            if (!myTokenizer.hasMoreTokens()) {
                break;
            }
            final String nextToken = myTokenizer.nextToken();
            if (">".equals(nextToken)) {
                return list;
            }
            if (!",".equals(nextToken)) {
                throw this._problem(myTokenizer, "Unexpected token '" + nextToken + "', expected ',' or '>')");
            }
        }
        throw this._problem(myTokenizer, "Unexpected end-of-string");
    }
    
    static final class MyTokenizer extends StringTokenizer
    {
        protected int _index;
        protected final String _input;
        protected String _pushbackToken;
        
        public MyTokenizer(final String input) {
            super(input, "<,>", true);
            this._input = input;
        }
        
        public String getAllInput() {
            return this._input;
        }
        
        public String getRemainingInput() {
            return this._input.substring(this._index);
        }
        
        @Override
        public boolean hasMoreTokens() {
            return this._pushbackToken != null || super.hasMoreTokens();
        }
        
        @Override
        public String nextToken() {
            String s;
            if (this._pushbackToken != null) {
                s = this._pushbackToken;
                this._pushbackToken = null;
            }
            else {
                s = super.nextToken();
            }
            this._index += s.length();
            return s;
        }
        
        public void pushBack(final String pushbackToken) {
            this._pushbackToken = pushbackToken;
            this._index -= pushbackToken.length();
        }
    }
}
