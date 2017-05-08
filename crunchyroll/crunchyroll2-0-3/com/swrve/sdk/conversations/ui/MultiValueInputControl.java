// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations.ui;

import java.util.Iterator;
import com.swrve.sdk.conversations.engine.model.UserInputResult;
import com.swrve.sdk.conversations.engine.model.ChoiceInputResponse;
import java.util.Map;
import android.widget.CompoundButton;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.content.ContextCompat;
import android.content.res.ColorStateList;
import android.os.Build$VERSION;
import android.view.View;
import com.swrve.sdk.conversations.engine.model.ChoiceInputItem;
import android.view.ViewGroup$LayoutParams;
import android.widget.LinearLayout$LayoutParams;
import com.swrve.sdk.common.R;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.util.AttributeSet;
import android.content.Context;
import android.widget.RadioButton;
import java.util.ArrayList;
import com.swrve.sdk.conversations.engine.model.OnContentChangedListener;
import com.swrve.sdk.conversations.engine.model.MultiValueInput;
import android.widget.TextView;
import java.io.Serializable;
import android.widget.CompoundButton$OnCheckedChangeListener;
import android.widget.LinearLayout;

public class MultiValueInputControl extends LinearLayout implements CompoundButton$OnCheckedChangeListener, ConversationInput, Serializable
{
    private TextView descLbl;
    private MultiValueInput model;
    private OnContentChangedListener onContentChangedListener;
    private ArrayList<RadioButton> radioButtons;
    private int selectedIndex;
    
    public MultiValueInputControl(final Context context) {
        super(context);
        this.selectedIndex = -1;
    }
    
    public MultiValueInputControl(final Context context, final AttributeSet set) {
        super(context, set);
        this.selectedIndex = -1;
    }
    
    public MultiValueInputControl(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.selectedIndex = -1;
    }
    
    public static MultiValueInputControl inflate(final Context context, final ViewGroup viewGroup, final MultiValueInput model) {
        final MultiValueInputControl onCheckedChangeListener = (MultiValueInputControl)LayoutInflater.from(context).inflate(R.layout.cio__multiinput, viewGroup, false);
        (onCheckedChangeListener.descLbl = (TextView)onCheckedChangeListener.findViewById(R.id.cio__MIV_Header)).setText((CharSequence)model.getDescription());
        final int textColorInt = model.getStyle().getTextColorInt();
        onCheckedChangeListener.showHideError(model.hasError(), (ViewGroup)onCheckedChangeListener, model);
        onCheckedChangeListener.model = model;
        onCheckedChangeListener.radioButtons = new ArrayList<RadioButton>();
        for (int i = 0; i < model.getValues().size(); ++i) {
            final RadioButton radioButton = new RadioButton(context);
            radioButton.setLayoutParams((ViewGroup$LayoutParams)new LinearLayout$LayoutParams(-1, -2));
            radioButton.setText((CharSequence)model.getValues().get(i).getAnswerText());
            radioButton.setTextColor(textColorInt);
            radioButton.setChecked(i == onCheckedChangeListener.selectedIndex);
            if (!onCheckedChangeListener.isInEditMode()) {
                radioButton.setTag(R.string.cio__indexTag, (Object)i);
            }
            setTint(radioButton, textColorInt);
            onCheckedChangeListener.addView((View)radioButton);
            radioButton.setOnCheckedChangeListener((CompoundButton$OnCheckedChangeListener)onCheckedChangeListener);
            onCheckedChangeListener.radioButtons.add(radioButton);
        }
        return onCheckedChangeListener;
    }
    
    private static void setTint(final RadioButton radioButton, final int n) {
        if (Build$VERSION.SDK_INT >= 21) {
            radioButton.setButtonTintList(ColorStateList.valueOf(n));
            return;
        }
        final ColorStateList list = new ColorStateList(new int[][] { { 16842910 }, { -16842910 }, { -16842912 }, { 16842919 } }, new int[] { n, n, n, n });
        final Drawable wrap = DrawableCompat.wrap(ContextCompat.getDrawable(radioButton.getContext(), R.drawable.abc_btn_radio_material));
        DrawableCompat.setTintList(wrap, list);
        radioButton.setButtonDrawable(wrap);
    }
    
    private void showHideError(final boolean error, final ViewGroup viewGroup, final MultiValueInput multiValueInput) {
        int cio__error;
        if (error) {
            cio__error = R.drawable.cio__error;
        }
        else {
            cio__error = 0;
        }
        viewGroup.setBackgroundResource(cio__error);
        multiValueInput.setError(error);
    }
    
    public boolean isValid() {
        if (this.model.isOptional()) {
            return true;
        }
        if (this.selectedIndex < 0) {
            this.showHideError(true, (ViewGroup)this, this.model);
            return false;
        }
        this.showHideError(false, (ViewGroup)this, this.model);
        return true;
    }
    
    public void onCheckedChanged(final CompoundButton compoundButton, final boolean b) {
        final int intValue = (int)compoundButton.getTag(R.string.cio__indexTag);
        if (this.selectedIndex > -1 && this.selectedIndex != intValue) {
            final RadioButton radioButton = (RadioButton)this.getChildAt(this.selectedIndex + 1);
            if (radioButton.isChecked()) {
                radioButton.setOnCheckedChangeListener((CompoundButton$OnCheckedChangeListener)null);
                radioButton.setChecked(false);
                radioButton.setOnCheckedChangeListener((CompoundButton$OnCheckedChangeListener)this);
            }
        }
        if (!b) {
            this.selectedIndex = -1;
        }
        else {
            this.selectedIndex = intValue;
        }
        if (this.onContentChangedListener != null) {
            this.onContentChangedListener.onContentChanged();
        }
        this.showHideError(false, (ViewGroup)this, this.model);
    }
    
    public void onReplyDataRequired(final Map<String, Object> map) {
        if (this.selectedIndex > -1) {
            final ChoiceInputItem choiceInputItem = this.model.getValues().get(this.selectedIndex);
            final ChoiceInputResponse choiceInputResponse = new ChoiceInputResponse();
            choiceInputResponse.setQuestionID(this.model.getTag());
            choiceInputResponse.setFragmentTag(this.model.getTag());
            choiceInputResponse.setAnswerID(choiceInputItem.getAnswerID());
            choiceInputResponse.setAnswerText(choiceInputItem.getAnswerText());
            map.put(this.model.getTag(), choiceInputResponse);
        }
    }
    
    public void setOnContentChangedListener(final OnContentChangedListener onContentChangedListener) {
        this.onContentChangedListener = onContentChangedListener;
    }
    
    public void setTextColor(final int textColor) {
        this.descLbl.setTextColor(textColor);
    }
    
    public void setUserInput(final UserInputResult userInputResult) {
        final ChoiceInputResponse choiceInputResponse = (ChoiceInputResponse)userInputResult.getResult();
        for (final RadioButton radioButton : this.radioButtons) {
            if (radioButton.getText().toString().equalsIgnoreCase(choiceInputResponse.getAnswerText())) {
                radioButton.setChecked(true);
            }
        }
    }
}
