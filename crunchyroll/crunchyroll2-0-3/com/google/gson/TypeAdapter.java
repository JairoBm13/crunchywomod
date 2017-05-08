// 
// Decompiled by Procyon v0.5.30
// 

package com.google.gson;

import com.google.gson.stream.JsonWriter;
import com.google.gson.internal.bind.JsonTreeWriter;
import java.io.IOException;
import com.google.gson.stream.JsonReader;

public abstract class TypeAdapter<T>
{
    public abstract T read(final JsonReader p0) throws IOException;
    
    public final JsonElement toJsonTree(final T t) {
        try {
            final JsonTreeWriter jsonTreeWriter = new JsonTreeWriter();
            this.write(jsonTreeWriter, t);
            return jsonTreeWriter.get();
        }
        catch (IOException ex) {
            throw new JsonIOException(ex);
        }
    }
    
    public abstract void write(final JsonWriter p0, final T p1) throws IOException;
}
