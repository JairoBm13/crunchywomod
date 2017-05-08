// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonToken;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import com.fasterxml.jackson.core.JsonParser;

public class JsonParserSequence extends JsonParserDelegate
{
    protected int _nextParser;
    protected final JsonParser[] _parsers;
    
    protected JsonParserSequence(final JsonParser[] parsers) {
        super(parsers[0]);
        this._parsers = parsers;
        this._nextParser = 1;
    }
    
    public static JsonParserSequence createFlattened(final JsonParser jsonParser, final JsonParser jsonParser2) {
        if (!(jsonParser instanceof JsonParserSequence) && !(jsonParser2 instanceof JsonParserSequence)) {
            return new JsonParserSequence(new JsonParser[] { jsonParser, jsonParser2 });
        }
        final ArrayList<JsonParser> list = new ArrayList<JsonParser>();
        if (jsonParser instanceof JsonParserSequence) {
            ((JsonParserSequence)jsonParser).addFlattenedActiveParsers(list);
        }
        else {
            list.add(jsonParser);
        }
        if (jsonParser2 instanceof JsonParserSequence) {
            ((JsonParserSequence)jsonParser2).addFlattenedActiveParsers(list);
        }
        else {
            list.add(jsonParser2);
        }
        return new JsonParserSequence(list.toArray(new JsonParser[list.size()]));
    }
    
    protected void addFlattenedActiveParsers(final List<JsonParser> list) {
        final int nextParser = this._nextParser;
        for (int length = this._parsers.length, i = nextParser - 1; i < length; ++i) {
            final JsonParser jsonParser = this._parsers[i];
            if (jsonParser instanceof JsonParserSequence) {
                ((JsonParserSequence)jsonParser).addFlattenedActiveParsers(list);
            }
            else {
                list.add(jsonParser);
            }
        }
    }
    
    @Override
    public void close() throws IOException {
        do {
            this.delegate.close();
        } while (this.switchToNext());
    }
    
    @Override
    public JsonToken nextToken() throws IOException, JsonParseException {
        final JsonToken nextToken = this.delegate.nextToken();
        if (nextToken != null) {
            return nextToken;
        }
        while (this.switchToNext()) {
            final JsonToken nextToken2 = this.delegate.nextToken();
            if (nextToken2 != null) {
                return nextToken2;
            }
        }
        return null;
    }
    
    protected boolean switchToNext() {
        if (this._nextParser >= this._parsers.length) {
            return false;
        }
        this.delegate = this._parsers[this._nextParser++];
        return true;
    }
}
