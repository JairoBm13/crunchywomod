// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations.engine;

import com.swrve.sdk.conversations.engine.deserialisers.MultiValueItemDeserialiser;
import com.swrve.sdk.conversations.engine.model.ChoiceInputItem;
import com.swrve.sdk.conversations.engine.deserialisers.ControlActionsDeserialiser;
import com.swrve.sdk.conversations.engine.model.ControlActions;
import java.lang.reflect.Type;
import com.swrve.sdk.conversations.engine.deserialisers.ConversationAtomDeserialiser;
import com.swrve.sdk.conversations.engine.model.ConversationAtom;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;

public class GsonHelper
{
    public static Gson getConfiguredGson() {
        final GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss");
        gsonBuilder.registerTypeAdapter(ConversationAtom.class, new ConversationAtomDeserialiser());
        gsonBuilder.registerTypeAdapter(ControlActions.class, new ControlActionsDeserialiser());
        gsonBuilder.registerTypeAdapter(ChoiceInputItem.class, new MultiValueItemDeserialiser());
        return gsonBuilder.create();
    }
}
