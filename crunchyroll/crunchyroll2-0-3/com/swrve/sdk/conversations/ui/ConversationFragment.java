// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import com.swrve.sdk.conversations.engine.model.ControlActions;
import android.net.Uri;
import com.swrve.sdk.conversations.engine.ActionBehaviours;
import android.os.Build$VERSION;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import com.swrve.sdk.conversations.engine.model.ChoiceInputResponse;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.swrve.sdk.conversations.engine.model.ConversationReply;
import com.swrve.sdk.conversations.engine.model.ControlBase;
import com.swrve.sdk.conversations.engine.model.ButtonControl;
import android.graphics.Bitmap;
import com.swrve.sdk.conversations.engine.model.styles.BackgroundStyle;
import com.swrve.sdk.conversations.engine.model.styles.AtomStyle;
import com.swrve.sdk.conversations.engine.model.MultiValueLongInput;
import java.util.Map;
import com.swrve.sdk.conversations.engine.model.OnContentChangedListener;
import com.swrve.sdk.conversations.engine.model.MultiValueInput;
import com.swrve.sdk.conversations.engine.model.InputBase;
import android.view.MotionEvent;
import android.view.View$OnTouchListener;
import android.view.ViewGroup$LayoutParams;
import android.widget.ImageView$ScaleType;
import android.graphics.BitmapFactory;
import android.content.Context;
import com.swrve.sdk.conversations.engine.model.Content;
import com.swrve.sdk.conversations.engine.model.ConversationAtom;
import android.app.Activity;
import android.view.View;
import com.swrve.sdk.common.R;
import android.annotation.SuppressLint;
import java.util.Iterator;
import com.swrve.sdk.SwrveSDKBase;
import com.swrve.sdk.conversations.engine.model.UserInputResult;
import java.util.HashMap;
import com.swrve.sdk.conversations.SwrveConversation;
import android.view.ViewGroup;
import com.swrve.sdk.conversations.engine.model.ConversationPage;
import java.util.ArrayList;
import com.swrve.sdk.SwrveBase;
import android.widget.LinearLayout$LayoutParams;
import android.widget.LinearLayout;
import android.view.View$OnClickListener;
import android.support.v4.app.Fragment;

public class ConversationFragment extends Fragment implements View$OnClickListener
{
    private LinearLayout contentLayout;
    private LinearLayout controlLayout;
    private LinearLayout$LayoutParams controlLp;
    private SwrveBase controller;
    private ConversationFullScreenVideoFrame fullScreenFrame;
    private ArrayList<ConversationInput> inputs;
    private ConversationPage page;
    private ViewGroup root;
    private SwrveConversation swrveConversation;
    private boolean userInputValid;
    private HashMap<String, UserInputResult> userInteractionData;
    private ValidationDialog validationDialog;
    
    public ConversationFragment() {
        this.userInputValid = false;
    }
    
    public static ConversationFragment create(final SwrveConversation swrveConversation) {
        final ConversationFragment conversationFragment = new ConversationFragment();
        conversationFragment.swrveConversation = swrveConversation;
        conversationFragment.controller = (SwrveBase)SwrveSDKBase.getInstance();
        return conversationFragment;
    }
    
    private void enforceValidations() {
        boolean b = false;
        final Iterator<ConversationInput> iterator = this.inputs.iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().isValid()) {
                b = true;
            }
        }
        if (b) {
            this.userInputValid = false;
            (this.validationDialog = ValidationDialog.create()).show(this.getFragmentManager(), "validation_dialog");
            return;
        }
        this.userInputValid = true;
    }
    
    @SuppressLint({ "NewApi" })
    private LinearLayout$LayoutParams getContentLayoutParams(final int width, final int height) {
        LinearLayout$LayoutParams linearLayout$LayoutParams;
        if (this.getSDKBuildVersion() >= 19) {
            linearLayout$LayoutParams = new LinearLayout$LayoutParams(this.controlLp);
        }
        else {
            linearLayout$LayoutParams = new LinearLayout$LayoutParams(this.controlLp.width, this.controlLp.height);
        }
        linearLayout$LayoutParams.width = width;
        linearLayout$LayoutParams.height = height;
        return linearLayout$LayoutParams;
    }
    
    private void initLayout() {
        this.contentLayout = (LinearLayout)this.root.findViewById(R.id.cio__content);
        this.controlLayout = (LinearLayout)this.root.findViewById(R.id.cio__controls);
        this.fullScreenFrame = (ConversationFullScreenVideoFrame)this.root.findViewById(R.id.cio__full_screen);
        if (this.contentLayout.getChildCount() > 0) {
            this.contentLayout.removeAllViews();
        }
        if (this.controlLayout.getChildCount() > 0) {
            this.controlLayout.removeAllViews();
        }
        if (this.getSDKBuildVersion() >= 19) {
            this.controlLp = new LinearLayout$LayoutParams(this.root.getLayoutParams());
        }
        else {
            this.controlLp = new LinearLayout$LayoutParams(this.root.getLayoutParams().width, this.root.getLayoutParams().height);
        }
        this.controlLp.height = -2;
        this.setBackgroundDrawable((View)this.contentLayout, this.page.getBackground());
        this.setBackgroundDrawable((View)this.controlLayout, this.page.getBackground());
    }
    
    private boolean isOkToProceed() {
        return this.userInputValid;
    }
    
    private void renderContent(final Activity activity) {
        for (final ConversationAtom conversationAtom : this.page.getContent()) {
            final AtomStyle style = conversationAtom.getStyle();
            final BackgroundStyle bg = style.getBg();
            if (conversationAtom instanceof Content) {
                final Content content = (Content)conversationAtom;
                final String string = content.getType().toString();
                if (string.equalsIgnoreCase("image")) {
                    final ConversationImageView conversationImageView = new ConversationImageView((Context)activity, content);
                    final Bitmap decodeFile = BitmapFactory.decodeFile(this.swrveConversation.getCacheDir().getAbsolutePath() + "/" + content.getValue());
                    conversationImageView.setTag((Object)conversationAtom.getTag());
                    conversationImageView.setImageBitmap(decodeFile);
                    conversationImageView.setAdjustViewBounds(true);
                    conversationImageView.setScaleType(ImageView$ScaleType.FIT_CENTER);
                    this.setBackgroundDrawable((View)conversationImageView, bg.getPrimaryDrawable());
                    this.contentLayout.addView((View)conversationImageView);
                }
                else if (string.equalsIgnoreCase("html-fragment")) {
                    final HtmlSnippetView htmlSnippetView = new HtmlSnippetView((Context)activity, content);
                    htmlSnippetView.setTag((Object)conversationAtom.getTag());
                    htmlSnippetView.setLayoutParams((ViewGroup$LayoutParams)this.getContentLayoutParams(-1, -2));
                    htmlSnippetView.setBackgroundColor(0);
                    this.setBackgroundDrawable((View)htmlSnippetView, bg.getPrimaryDrawable());
                    this.contentLayout.addView((View)htmlSnippetView);
                }
                else if (string.equalsIgnoreCase("video")) {
                    final HtmlVideoView htmlVideoView = new HtmlVideoView((Context)activity, content, this.fullScreenFrame);
                    htmlVideoView.setTag((Object)conversationAtom.getTag());
                    htmlVideoView.setBackgroundColor(0);
                    this.setBackgroundDrawable((View)htmlVideoView, bg.getPrimaryDrawable());
                    htmlVideoView.setLayoutParams((ViewGroup$LayoutParams)this.getContentLayoutParams(-1, -2));
                    htmlVideoView.setOnTouchListener((View$OnTouchListener)new View$OnTouchListener() {
                        final /* synthetic */ String val$tag = conversationAtom.getTag();
                        
                        public boolean onTouch(final View view, final MotionEvent motionEvent) {
                            ConversationFragment.this.stashVideoViewed(ConversationFragment.this.page.getTag(), this.val$tag, htmlVideoView);
                            return false;
                        }
                    });
                    this.contentLayout.addView((View)htmlVideoView);
                }
                else {
                    if (!string.equalsIgnoreCase("spacer")) {
                        continue;
                    }
                    final View view = new View((Context)activity);
                    view.setTag((Object)conversationAtom.getTag());
                    view.setBackgroundColor(0);
                    this.setBackgroundDrawable(view, bg.getPrimaryDrawable());
                    view.setLayoutParams((ViewGroup$LayoutParams)this.getContentLayoutParams(-1, Integer.parseInt(((Content)conversationAtom).getHeight())));
                    this.contentLayout.addView(view);
                }
            }
            else {
                if (!(conversationAtom instanceof InputBase)) {
                    continue;
                }
                if (conversationAtom instanceof MultiValueInput) {
                    final MultiValueInputControl inflate = MultiValueInputControl.inflate((Context)activity, (ViewGroup)this.contentLayout, (MultiValueInput)conversationAtom);
                    inflate.setLayoutParams((ViewGroup$LayoutParams)this.getContentLayoutParams(-1, -2));
                    inflate.setTag((Object)conversationAtom.getTag());
                    inflate.setOnContentChangedListener(new OnContentChangedListener() {
                        final /* synthetic */ String val$tag = conversationAtom.getTag();
                        
                        @Override
                        public void onContentChanged() {
                            final HashMap<String, Object> hashMap = new HashMap<String, Object>();
                            inflate.onReplyDataRequired(hashMap);
                            ConversationFragment.this.stashMultiChoiceInputData(ConversationFragment.this.page.getTag(), this.val$tag, hashMap);
                        }
                    });
                    this.setBackgroundDrawable((View)inflate, bg.getPrimaryDrawable());
                    inflate.setTextColor(style.getTextColorInt());
                    this.contentLayout.addView((View)inflate);
                    this.inputs.add(inflate);
                }
                else {
                    if (!(conversationAtom instanceof MultiValueLongInput)) {
                        continue;
                    }
                    final MultiValueLongInputControl inflate2 = MultiValueLongInputControl.inflate((Context)activity, (ViewGroup)this.contentLayout, (MultiValueLongInput)conversationAtom);
                    inflate2.setLayoutParams((ViewGroup$LayoutParams)this.getContentLayoutParams(-1, -2));
                    inflate2.setOnContentChangedListener(new OnContentChangedListener() {
                        final /* synthetic */ String val$tag = conversationAtom.getTag();
                        
                        @Override
                        public void onContentChanged() {
                            final HashMap<String, Object> hashMap = new HashMap<String, Object>();
                            inflate2.onReplyDataRequired(hashMap);
                            ConversationFragment.this.stashMultiChoiceLongInputData(ConversationFragment.this.page.getTag(), this.val$tag, hashMap);
                        }
                    });
                    inflate2.setTag((Object)conversationAtom.getTag());
                    this.setBackgroundDrawable((View)inflate2, bg.getPrimaryDrawable());
                    inflate2.setHeaderTextColors(style.getTextColorInt());
                    this.contentLayout.addView((View)inflate2);
                    this.inputs.add(inflate2);
                }
            }
        }
    }
    
    @SuppressLint({ "NewApi" })
    private void renderControls(final Activity activity) {
        final int dimensionPixelSize = activity.getTheme().obtainStyledAttributes(new int[] { R.attr.conversationControlLayoutMargin }).getDimensionPixelSize(0, 0);
        for (int size = this.page.getControls().size(), i = 0; i < size; ++i) {
            final ConversationButton conversationButton = new ConversationButton((Context)activity, this.page.getControls().get(i), R.attr.conversationControlButtonStyle);
            LinearLayout$LayoutParams layoutParams;
            if (this.getSDKBuildVersion() >= 19) {
                layoutParams = new LinearLayout$LayoutParams(this.controlLp);
            }
            else {
                layoutParams = new LinearLayout$LayoutParams(this.controlLp.width, this.controlLp.height);
            }
            layoutParams.weight = 1.0f;
            layoutParams.leftMargin = dimensionPixelSize;
            layoutParams.rightMargin = dimensionPixelSize;
            layoutParams.topMargin = dimensionPixelSize;
            layoutParams.bottomMargin = dimensionPixelSize;
            conversationButton.setLayoutParams((ViewGroup$LayoutParams)layoutParams);
            this.controlLayout.addView((View)conversationButton);
            conversationButton.setOnClickListener((View$OnClickListener)this);
        }
    }
    
    private void sendCallActionEvent(final String s, final ConversationAtom conversationAtom) {
        if (this.controller != null) {
            this.controller.conversationCallActionCalledByUser(this.swrveConversation, s, conversationAtom.getTag());
        }
    }
    
    private void sendCancelNavigationEvent(final String s) {
        if (this.controller != null) {
            this.controller.conversationWasCancelledByUser(this.swrveConversation, s);
        }
    }
    
    private void sendDeepLinkActionEvent(final String s, final ConversationAtom conversationAtom) {
        if (this.controller != null) {
            this.controller.conversationDeeplinkActionCalledByUser(this.swrveConversation, s, conversationAtom.getTag());
        }
    }
    
    private void sendDoneNavigationEvent(final String s, final String s2) {
        if (this.controller != null) {
            this.controller.conversationWasFinishedByUser(this.swrveConversation, s, s2);
        }
    }
    
    private void sendErrorNavigationEvent(final String s, final Exception ex) {
        if (this.controller != null) {
            this.controller.conversationEncounteredError(this.swrveConversation, s, ex);
        }
    }
    
    private void sendLinkVisitActionEvent(final String s, final ConversationAtom conversationAtom) {
        if (this.controller != null) {
            this.controller.conversationLinkVisitActionCalledByUser(this.swrveConversation, s, conversationAtom.getTag());
        }
    }
    
    private void sendPageImpressionEvent(final String s) {
        if (this.controller != null) {
            this.controller.conversationPageWasViewedByUser(this.swrveConversation, s);
        }
    }
    
    private void sendReply(final ControlBase controlBase, final ConversationReply conversationReply) {
        conversationReply.setControl(controlBase.getTag());
        final Iterator<ConversationInput> iterator = this.inputs.iterator();
        while (iterator.hasNext()) {
            iterator.next().onReplyDataRequired(conversationReply.getData());
        }
        final ConversationPage pageForControl = this.swrveConversation.getPageForControl(controlBase);
        if (pageForControl != null) {
            this.sendTransitionPageEvent(this.page.getTag(), controlBase.getTarget(), controlBase.getTag());
            this.openConversationOnPage(pageForControl);
        }
        else if (controlBase.hasActions()) {
            Log.i("SwrveSDK", "User has selected an Action. They are now finished the conversation");
            this.sendDoneNavigationEvent(this.page.getTag(), controlBase.getTag());
            final FragmentActivity activity = this.getActivity();
            if (this.isAdded() && activity != null) {
                activity.finish();
            }
        }
        else {
            Log.e("SwrveSDK", "No more pages in this conversation");
            this.sendDoneNavigationEvent(this.page.getTag(), controlBase.getTag());
            final FragmentActivity activity2 = this.getActivity();
            if (this.isAdded() && activity2 != null) {
                activity2.finish();
            }
        }
    }
    
    private void sendStartNavigationEvent(final String s) {
        if (this.controller != null) {
            this.controller.conversationWasStartedByUser(this.swrveConversation, s);
        }
    }
    
    private void sendTransitionPageEvent(final String s, final String s2, final String s3) {
        if (this.controller != null) {
            this.controller.conversationTransitionedToOtherPage(this.swrveConversation, s, s2, s3);
        }
    }
    
    @SuppressLint({ "NewApi" })
    private void setBackgroundDrawable(final View view, final Drawable drawable) {
        if (this.getSDKBuildVersion() < 16) {
            view.setBackgroundDrawable(drawable);
            return;
        }
        view.setBackground(drawable);
    }
    
    private void stashMultiChoiceInputData(final String pageTag, final String fragmentTag, final HashMap<String, Object> hashMap) {
        final String string = pageTag + "-" + fragmentTag;
        for (final String s : hashMap.keySet()) {
            final UserInputResult userInputResult = new UserInputResult();
            userInputResult.type = "choice";
            userInputResult.conversationId = Integer.toString(this.swrveConversation.getId());
            userInputResult.fragmentTag = fragmentTag;
            userInputResult.pageTag = pageTag;
            userInputResult.result = hashMap.get(s);
            this.userInteractionData.put(string, userInputResult);
        }
    }
    
    private void stashMultiChoiceLongInputData(final String pageTag, final String fragmentTag, final HashMap<String, Object> hashMap) {
        final String string = pageTag + "-" + fragmentTag;
        for (final String s : hashMap.keySet()) {
            final ChoiceInputResponse choiceInputResponse = hashMap.get(s);
            final UserInputResult userInputResult = new UserInputResult();
            userInputResult.type = "multi-choice";
            userInputResult.conversationId = Integer.toString(this.swrveConversation.getId());
            userInputResult.fragmentTag = fragmentTag;
            userInputResult.pageTag = pageTag;
            userInputResult.result = hashMap.get(s);
            this.userInteractionData.put(string + "-" + choiceInputResponse.getQuestionID(), userInputResult);
        }
    }
    
    private void stashVideoViewed(final String pageTag, final String fragmentTag, final HtmlVideoView htmlVideoView) {
        final String string = pageTag + "-" + fragmentTag;
        final UserInputResult userInputResult = new UserInputResult();
        userInputResult.type = "play";
        userInputResult.conversationId = Integer.toString(this.swrveConversation.getId());
        userInputResult.fragmentTag = fragmentTag;
        userInputResult.pageTag = pageTag;
        userInputResult.result = "";
        this.userInteractionData.put(string, userInputResult);
    }
    
    public void commitConversationFragment(final FragmentManager fragmentManager) {
        final FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
        beginTransaction.replace(16908290, this, "conversation");
        beginTransaction.commit();
    }
    
    public void commitUserInputsToEvents() {
        Log.i("SwrveSDK", "Commiting all stashed events");
        final ArrayList<UserInputResult> list = new ArrayList<UserInputResult>();
        final Iterator<String> iterator = this.userInteractionData.keySet().iterator();
        while (iterator.hasNext()) {
            list.add(this.userInteractionData.get(iterator.next()));
        }
        this.controller.conversationEventsCommitedByUser(this.swrveConversation, list);
        this.userInteractionData.clear();
    }
    
    public ConversationPage getPage() {
        return this.page;
    }
    
    protected int getSDKBuildVersion() {
        return Build$VERSION.SDK_INT;
    }
    
    public HashMap<String, UserInputResult> getUserInteractionData() {
        return this.userInteractionData;
    }
    
    public boolean onBackPressed() {
        if (this.fullScreenFrame.getVisibility() != 8) {
            this.fullScreenFrame.disableFullScreen();
            return false;
        }
        this.sendCancelNavigationEvent(this.page.getTag());
        this.commitUserInputsToEvents();
        return true;
    }
    
    public void onClick(final View view) {
        final FragmentActivity activity = this.getActivity();
        if (this.isAdded() && activity != null && view instanceof ConversationControl) {
            this.commitUserInputsToEvents();
            if (view instanceof ConversationButton) {
                this.enforceValidations();
                if (!this.isOkToProceed()) {
                    Log.i("SwrveSDK", "User tried to go to the next piece of the conversation but it is not ok to proceed");
                    return;
                }
                final ConversationReply conversationReply = new ConversationReply();
                final ButtonControl model = ((ConversationButton)view).getModel();
                if (!((ConversationControl)view).getModel().hasActions()) {
                    this.sendReply(model, conversationReply);
                    return;
                }
                final ControlActions actions = ((ConversationControl)view).getModel().getActions();
                if (actions.isCall()) {
                    this.sendReply(model, conversationReply);
                    this.sendCallActionEvent(this.page.getTag(), model);
                    ActionBehaviours.openDialer(actions.getCallUri(), activity);
                    return;
                }
                if (actions.isVisit()) {
                    final HashMap<String, String> visitDetails = actions.getVisitDetails();
                    final String s = visitDetails.get("url");
                    final String s2 = visitDetails.get("refer");
                    final Uri parse = Uri.parse(s);
                    this.sendReply(model, conversationReply);
                    this.sendLinkVisitActionEvent(this.page.getTag(), model);
                    ActionBehaviours.openIntentWebView(parse, activity, s2);
                    return;
                }
                if (actions.isDeepLink()) {
                    final Uri parse2 = Uri.parse((String)actions.getDeepLinkDetails().get("url"));
                    this.sendReply(model, conversationReply);
                    this.sendDeepLinkActionEvent(this.page.getTag(), model);
                    ActionBehaviours.openDeepLink(parse2, activity);
                }
            }
        }
    }
    
    @Override
    public View onCreateView(final LayoutInflater layoutInflater, final ViewGroup viewGroup, final Bundle bundle) {
        return layoutInflater.inflate(R.layout.cio__conversation_fragment, viewGroup, false);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        ArrayList<ConversationInput> inputs;
        if (this.inputs == null) {
            inputs = new ArrayList<ConversationInput>();
        }
        else {
            inputs = this.inputs;
        }
        this.inputs = inputs;
        HashMap<String, UserInputResult> userInteractionData;
        if (this.userInteractionData == null) {
            userInteractionData = new HashMap<String, UserInputResult>();
        }
        else {
            userInteractionData = this.userInteractionData;
        }
        this.userInteractionData = userInteractionData;
        if (this.page != null) {
            final View view = this.getView();
            this.openConversationOnPage(this.page);
            final Iterator<String> iterator = this.userInteractionData.keySet().iterator();
            while (iterator.hasNext()) {
                final UserInputResult userInputResult = this.userInteractionData.get(iterator.next());
                final View viewWithTag = view.findViewWithTag((Object)userInputResult.getFragmentTag());
                if (userInputResult.isSingleChoice() && viewWithTag instanceof MultiValueInputControl) {
                    ((MultiValueInputControl)viewWithTag).setUserInput(userInputResult);
                }
                else {
                    if (!userInputResult.isMultiChoice() || !(viewWithTag instanceof MultiValueLongInputControl)) {
                        continue;
                    }
                    ((MultiValueLongInputControl)viewWithTag).setUserInput(userInputResult);
                }
            }
        }
        else {
            this.openFirstPage();
        }
    }
    
    public void openConversationOnPage(final ConversationPage page) {
        final FragmentActivity activity = this.getActivity();
        if (this.isAdded() && activity != null) {
            this.root = (ViewGroup)this.getView();
            if (this.root != null) {
                this.page = page;
                if (this.inputs.size() > 0) {
                    this.inputs.clear();
                }
                activity.setTitle((CharSequence)this.page.getTitle());
                try {
                    this.initLayout();
                    this.renderControls(activity);
                    this.renderContent(activity);
                    this.sendPageImpressionEvent(this.page.getTag());
                    this.root.requestFocus();
                }
                catch (Exception ex) {
                    Log.e("SwrveSDK", "Error rendering conversation page. Exiting conversation.", (Throwable)ex);
                    this.sendErrorNavigationEvent(this.page.getTag(), ex);
                    if (activity != null) {
                        activity.finish();
                    }
                }
            }
        }
    }
    
    public void openFirstPage() {
        this.page = this.swrveConversation.getFirstPage();
        this.sendStartNavigationEvent(this.page.getTag());
        this.openConversationOnPage(this.page);
    }
    
    public void setPage(final ConversationPage page) {
        this.page = page;
    }
    
    public void setUserInteractionData(final HashMap<String, UserInputResult> userInteractionData) {
        this.userInteractionData = userInteractionData;
    }
}
