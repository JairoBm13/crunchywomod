// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations.engine.deserialisers;

import com.google.gson.JsonParseException;
import com.google.gson.JsonObject;
import java.util.Iterator;
import android.util.Log;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.JsonDeserializationContext;
import java.lang.reflect.Type;
import com.google.gson.JsonElement;
import com.swrve.sdk.conversations.engine.model.ControlActions;
import com.google.gson.JsonDeserializer;

public class ControlActionsDeserialiser implements JsonDeserializer<ControlActions>
{
    @Override
    public ControlActions deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        ControlActions controlActions2;
        if (jsonElement.isJsonObject()) {
            final ControlActions controlActions = new ControlActions();
            final Iterator<Map.Entry<String, JsonElement>> iterator = jsonElement.getAsJsonObject().entrySet().iterator();
            while (true) {
                controlActions2 = controlActions;
                if (!iterator.hasNext()) {
                    break;
                }
                final Map.Entry<String, JsonElement> entry = iterator.next();
                final String s = entry.getKey();
                if (s.equalsIgnoreCase(ControlActions.CALL_ACTION.toString())) {
                    controlActions.includeAction(s, entry.getValue().getAsString());
                }
                else if (s.equalsIgnoreCase(ControlActions.VISIT_URL_ACTION.toString())) {
                    final JsonObject asJsonObject = entry.getValue().getAsJsonObject();
                    final HashMap<String, String> hashMap = new HashMap<String, String>();
                    String s2 = "http://www.google.ie";
                    String replaceAll = "http://swrve.com";
                    if (asJsonObject.has("url")) {
                        s2 = asJsonObject.get("url").getAsString().replaceAll("\\s", "");
                        if (!s2.startsWith("http")) {
                            s2 = "http://" + s2;
                        }
                    }
                    if (asJsonObject.has("refer")) {
                        replaceAll = asJsonObject.get("refer").getAsString().replaceAll("\\s", "");
                    }
                    hashMap.put("url", s2);
                    hashMap.put("refer", replaceAll);
                    controlActions.includeAction(s, hashMap);
                }
                else if (s.equalsIgnoreCase(ControlActions.DEEPLINK_ACTION.toString())) {
                    final JsonObject asJsonObject2 = entry.getValue().getAsJsonObject();
                    final HashMap<String, String> hashMap2 = new HashMap<String, String>();
                    String asString = "twitter://";
                    if (asJsonObject2.has("url")) {
                        asString = asJsonObject2.get("url").getAsString();
                    }
                    hashMap2.put("url", asString);
                    controlActions.includeAction(s, hashMap2);
                }
                else {
                    Log.e("SwrveSDK", "Unrecognized Action in json");
                    Log.e("SwrveSDK", "JSON :: " + entry.getValue().getAsJsonObject().toString());
                }
            }
        }
        else {
            controlActions2 = null;
        }
        return controlActions2;
    }
}
