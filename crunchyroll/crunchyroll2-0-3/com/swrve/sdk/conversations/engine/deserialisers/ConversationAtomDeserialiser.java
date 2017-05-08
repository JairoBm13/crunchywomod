// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations.engine.deserialisers;

import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import com.swrve.sdk.conversations.engine.model.MultiValueLongInput;
import com.swrve.sdk.conversations.engine.model.MultiValueInput;
import com.swrve.sdk.conversations.engine.model.Content;
import com.swrve.sdk.conversations.engine.model.ButtonControl;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.swrve.sdk.conversations.engine.model.ConversationAtom;
import com.google.gson.JsonDeserializer;

public class ConversationAtomDeserialiser implements JsonDeserializer<ConversationAtom>
{
    @Override
    public ConversationAtom deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final String s = null;
        if (!jsonElement.isJsonObject()) {
            return null;
        }
        final JsonObject asJsonObject = jsonElement.getAsJsonObject();
        if (!asJsonObject.has("type")) {
            return (ConversationAtom)jsonDeserializationContext.deserialize(asJsonObject, ButtonControl.class);
        }
        final String asString = asJsonObject.get("type").getAsString();
        String asString2 = s;
        if (asJsonObject.has("tag")) {
            asString2 = asJsonObject.get("tag").getAsString();
        }
        if (asString.equalsIgnoreCase("html-fragment") || asString.equalsIgnoreCase("image") || asString.equalsIgnoreCase("video") || asString.equalsIgnoreCase("spacer")) {
            return (ConversationAtom)jsonDeserializationContext.deserialize(asJsonObject, Content.class);
        }
        if (asString.equalsIgnoreCase("multi-value-input")) {
            return (ConversationAtom)jsonDeserializationContext.deserialize(asJsonObject, MultiValueInput.class);
        }
        if (asString.equalsIgnoreCase("multi-value-long-input")) {
            return (ConversationAtom)jsonDeserializationContext.deserialize(asJsonObject, MultiValueLongInput.class);
        }
        return ConversationAtom.create(asString2, asString);
    }
}
