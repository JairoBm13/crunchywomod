// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations.engine.deserialisers;

import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.swrve.sdk.conversations.engine.model.ChoiceInputItem;
import com.google.gson.JsonDeserializer;

public class MultiValueItemDeserialiser implements JsonDeserializer<ChoiceInputItem>
{
    @Override
    public ChoiceInputItem deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (jsonElement.isJsonObject()) {
            final JsonObject asJsonObject = jsonElement.getAsJsonObject();
            return new ChoiceInputItem(asJsonObject.get("answer_id").getAsString(), asJsonObject.get("answer_text").getAsString());
        }
        return null;
    }
}
