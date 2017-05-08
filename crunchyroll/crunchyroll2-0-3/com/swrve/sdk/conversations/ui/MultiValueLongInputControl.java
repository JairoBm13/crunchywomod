// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations.ui;

import android.widget.AdapterView;
import com.swrve.sdk.conversations.engine.model.UserInputResult;
import java.util.Iterator;
import com.swrve.sdk.conversations.engine.model.ChoiceInputResponse;
import java.util.Map;
import android.graphics.Color;
import android.widget.AdapterView$OnItemSelectedListener;
import android.widget.SpinnerAdapter;
import android.view.View;
import java.util.List;
import android.widget.ArrayAdapter;
import com.swrve.sdk.common.R;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.TextView;
import android.widget.Spinner;
import java.util.ArrayList;
import com.swrve.sdk.conversations.engine.model.ChoiceInputItem;
import java.util.HashMap;
import com.swrve.sdk.conversations.engine.model.OnContentChangedListener;
import com.swrve.sdk.conversations.engine.model.MultiValueLongInput;
import android.widget.LinearLayout;

public class MultiValueLongInputControl extends LinearLayout implements ConversationInput
{
    private static String DEFAULT_ANSWER_ID;
    private MultiValueLongInput model;
    private OnContentChangedListener onContentChangedListener;
    private HashMap<String, ChoiceInputItem> responses;
    private ArrayList<Spinner> spinners;
    public ArrayList<TextView> textViews;
    
    static {
        MultiValueLongInputControl.DEFAULT_ANSWER_ID = "123-fake-id";
    }
    
    public MultiValueLongInputControl(final Context context) {
        super(context);
        this.responses = new HashMap<String, ChoiceInputItem>();
        this.textViews = new ArrayList<TextView>();
    }
    
    public MultiValueLongInputControl(final Context context, final AttributeSet set) {
        super(context, set);
        this.responses = new HashMap<String, ChoiceInputItem>();
        this.textViews = new ArrayList<TextView>();
    }
    
    public MultiValueLongInputControl(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.responses = new HashMap<String, ChoiceInputItem>();
        this.textViews = new ArrayList<TextView>();
    }
    
    private MILongItemListener createListener(final MultiValueLongInput.Item item) {
        return new MILongItemListener(item);
    }
    
    public static MultiValueLongInputControl inflate(final Context context, final ViewGroup viewGroup, final MultiValueLongInput model) {
        final LayoutInflater from = LayoutInflater.from(context);
        final MultiValueLongInputControl multiValueLongInputControl = (MultiValueLongInputControl)from.inflate(R.layout.cio__multiinputlong, viewGroup, false);
        final TextView textView = (TextView)multiValueLongInputControl.findViewById(R.id.cio__MIV_Header);
        textView.setText((CharSequence)model.getDescription());
        multiValueLongInputControl.textViews.add(textView);
        multiValueLongInputControl.model = model;
        multiValueLongInputControl.spinners = new ArrayList<Spinner>();
        final int textColorInt = multiValueLongInputControl.model.getStyle().getTextColorInt();
        for (int size = multiValueLongInputControl.model.getValues().size(), i = 0; i < size; ++i) {
            final MultiValueLongInput.Item item = multiValueLongInputControl.model.getValues().get(i);
            final ViewGroup viewGroup2 = (ViewGroup)from.inflate(R.layout.cio__multiinputlong_row, (ViewGroup)multiValueLongInputControl, false);
            multiValueLongInputControl.showHideError(item.hasError(), viewGroup2, item);
            final TextView textView2 = (TextView)viewGroup2.findViewById(R.id.cio__MIV_Item_Header);
            textView2.setText((CharSequence)item.getTitle());
            multiValueLongInputControl.textViews.add(textView2);
            final ArrayAdapter<ChoiceInputItem> adapter = new ArrayAdapter<ChoiceInputItem>(context, R.layout.cio__simple_spinner_item, item.getOptions()) {
                public View getView(final int n, View view, final ViewGroup viewGroup) {
                    view = super.getView(n, view, viewGroup);
                    ((TextView)view).setTextColor(textColorInt);
                    return view;
                }
            };
            final String string = context.getString(R.string.cio__spinner_prompt);
            final ChoiceInputItem choiceInputItem = new ChoiceInputItem(MultiValueLongInputControl.DEFAULT_ANSWER_ID, string);
            if (((ChoiceInputItem)adapter.getItem(0)).getAnswerText().equalsIgnoreCase(string)) {
                adapter.remove(adapter.getItem(0));
            }
            adapter.insert((Object)choiceInputItem, 0);
            final TextView textView3 = (TextView)viewGroup2.findViewById(R.id.cio__MIV_Item_Header);
            textView3.setTextColor(textColorInt);
            final Spinner spinner = (Spinner)viewGroup2.findViewById(R.id.cio__MIV_Item_Spinner);
            spinner.setAdapter((SpinnerAdapter)adapter);
            spinner.setOnItemSelectedListener((AdapterView$OnItemSelectedListener)multiValueLongInputControl.createListener(item));
            multiValueLongInputControl.spinners.add(spinner);
            multiValueLongInputControl.addView((View)viewGroup2);
            textView3.setTextColor(Color.parseColor("#ff00ff"));
        }
        return multiValueLongInputControl;
    }
    
    private void showHideError(final boolean error, final ViewGroup viewGroup, final MultiValueLongInput.Item item) {
        int cio__error;
        if (error) {
            cio__error = R.drawable.cio__error;
        }
        else {
            cio__error = 0;
        }
        viewGroup.setBackgroundResource(cio__error);
        item.setError(error);
    }
    
    public boolean isValid() {
        if (!this.model.isOptional() && this.responses.size() < this.model.getValues().size()) {
            for (int i = 0; i < this.spinners.size(); ++i) {
                final Spinner spinner = this.spinners.get(i);
                final MultiValueLongInput.Item item = this.model.getValues().get(i);
                final ChoiceInputItem choiceInputItem = (ChoiceInputItem)spinner.getSelectedItem();
                final ViewGroup viewGroup = (ViewGroup)spinner.getParent();
                if (MultiValueLongInputControl.DEFAULT_ANSWER_ID.equals(choiceInputItem.getAnswerID())) {
                    this.showHideError(true, viewGroup, item);
                }
                else {
                    this.showHideError(false, viewGroup, item);
                }
            }
            return false;
        }
        return true;
    }
    
    public void onReplyDataRequired(final Map<String, Object> map) {
        for (final String questionID : this.responses.keySet()) {
            final ChoiceInputItem choiceInputItem = this.responses.get(questionID);
            final ChoiceInputResponse choiceInputResponse = new ChoiceInputResponse();
            choiceInputResponse.setQuestionID(questionID);
            choiceInputResponse.setFragmentTag(this.model.getTag());
            choiceInputResponse.setAnswerID(choiceInputItem.getAnswerID());
            choiceInputResponse.setAnswerText(choiceInputItem.getAnswerText());
            map.put(this.model.getTag() + "-" + questionID, choiceInputResponse);
        }
    }
    
    public void setHeaderTextColors(final int textColor) {
        final Iterator<TextView> iterator = this.textViews.iterator();
        while (iterator.hasNext()) {
            iterator.next().setTextColor(textColor);
        }
    }
    
    public void setOnContentChangedListener(final OnContentChangedListener onContentChangedListener) {
        this.onContentChangedListener = onContentChangedListener;
    }
    
    public void setUserInput(final UserInputResult userInputResult) {
        for (final Spinner spinner : this.spinners) {
            final ChoiceInputResponse choiceInputResponse = (ChoiceInputResponse)userInputResult.getResult();
            final SpinnerAdapter adapter = spinner.getAdapter();
            for (int i = 0; i < adapter.getCount(); ++i) {
                if (((ChoiceInputItem)adapter.getItem(i)).getAnswerID().equalsIgnoreCase(choiceInputResponse.getAnswerID())) {
                    spinner.setSelection(i);
                }
            }
        }
    }
    
    private class MILongItemListener implements AdapterView$OnItemSelectedListener
    {
        private MultiValueLongInput.Item item;
        
        public MILongItemListener(final MultiValueLongInput.Item item) {
            this.item = item;
        }
        
        public void onItemSelected(final AdapterView<?> adapterView, final View view, final int n, final long n2) {
            if (n == 0) {
                MultiValueLongInputControl.this.responses.remove(this.item.getTitle());
            }
            else {
                MultiValueLongInputControl.this.responses.put(this.item.getQuestionId(), this.item.getOptions().get(n));
                MultiValueLongInputControl.this.showHideError(false, (ViewGroup)view.getParent().getParent(), this.item);
                if (MultiValueLongInputControl.this.onContentChangedListener != null) {
                    MultiValueLongInputControl.this.onContentChangedListener.onContentChanged();
                }
            }
        }
        
        public void onNothingSelected(final AdapterView<?> adapterView) {
            MultiValueLongInputControl.this.responses.remove(this.item.getTitle());
        }
    }
}
