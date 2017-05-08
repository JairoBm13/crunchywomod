// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations.engine.model;

import java.util.ArrayList;
import java.io.Serializable;

public class MultiValueLongInput extends InputBase implements Serializable
{
    private String description;
    private ArrayList<Item> values;
    
    public String getDescription() {
        return this.description;
    }
    
    public ArrayList<Item> getValues() {
        return this.values;
    }
    
    public static class Item implements Serializable
    {
        private boolean error;
        private ArrayList<ChoiceInputItem> options;
        private String question_id;
        private String title;
        
        public ArrayList<ChoiceInputItem> getOptions() {
            return this.options;
        }
        
        public String getQuestionId() {
            return this.question_id;
        }
        
        public String getTitle() {
            return this.title;
        }
        
        public boolean hasError() {
            return this.error;
        }
        
        public void setError(final boolean error) {
            this.error = error;
        }
    }
}
