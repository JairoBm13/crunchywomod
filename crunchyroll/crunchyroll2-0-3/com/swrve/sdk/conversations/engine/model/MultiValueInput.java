// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations.engine.model;

import java.util.ArrayList;
import java.io.Serializable;

public class MultiValueInput extends InputBase implements Serializable
{
    private String description;
    private ArrayList<ChoiceInputItem> values;
    
    public String getDescription() {
        return this.description;
    }
    
    public ArrayList<ChoiceInputItem> getValues() {
        return this.values;
    }
}
