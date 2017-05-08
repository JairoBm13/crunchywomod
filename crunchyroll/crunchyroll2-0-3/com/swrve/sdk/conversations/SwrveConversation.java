// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.conversations;

import java.util.concurrent.TimeUnit;
import com.swrve.sdk.messaging.ISwrveMessageListener;
import com.swrve.sdk.messaging.SwrveEventListener;
import java.util.Collection;
import com.swrve.sdk.localstorage.ILocalStorage;
import com.swrve.sdk.IPostBatchRequestListener;
import com.swrve.sdk.EventHelper;
import java.util.LinkedHashMap;
import java.io.UnsupportedEncodingException;
import android.content.SharedPreferences$Editor;
import com.swrve.sdk.rest.RESTResponse;
import com.swrve.sdk.rest.IRESTResponseListener;
import java.util.Locale;
import com.swrve.sdk.conversations.engine.model.ChoiceInputResponse;
import com.swrve.sdk.conversations.engine.model.UserInputResult;
import android.app.Activity;
import com.swrve.sdk.messaging.SwrveMessageFormat;
import com.swrve.sdk.SwrveResourceManager;
import com.swrve.sdk.messaging.SwrveCampaign;
import com.swrve.sdk.messaging.SwrveOrientation;
import com.swrve.sdk.messaging.SwrveMessage;
import android.view.Display;
import java.util.GregorianCalendar;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.os.Build$VERSION;
import android.annotation.SuppressLint;
import java.util.List;
import java.util.Collections;
import com.swrve.sdk.messaging.SwrveBaseCampaign;
import android.content.Context;
import java.util.Map;
import java.util.HashMap;
import com.swrve.sdk.messaging.SwrveActionType;
import com.swrve.sdk.messaging.SwrveButton;
import java.util.Date;
import com.swrve.sdk.ISwrveBase;
import com.swrve.sdk.SwrveImp;
import com.swrve.sdk.config.SwrveConfigBase;
import com.swrve.sdk.conversations.engine.model.Content;
import com.swrve.sdk.conversations.engine.model.ConversationAtom;
import java.util.Iterator;
import com.swrve.sdk.conversations.engine.model.ControlBase;
import java.util.Set;
import com.swrve.sdk.SwrveHelper;
import org.json.JSONException;
import org.json.JSONArray;
import android.util.Log;
import org.json.JSONObject;
import com.swrve.sdk.conversations.engine.model.ConversationPage;
import java.util.ArrayList;
import com.swrve.sdk.SwrveBase;
import com.swrve.sdk.messaging.SwrveConversationCampaign;
import java.io.File;
import java.io.Serializable;

public class SwrveConversation implements Serializable
{
    private final String LOG_TAG;
    protected File cacheDir;
    protected transient SwrveConversationCampaign campaign;
    protected transient SwrveBase<?, ?> conversationController;
    protected int id;
    protected String name;
    protected ArrayList<ConversationPage> pages;
    
    public SwrveConversation(final SwrveBase<?, ?> conversationController, final SwrveConversationCampaign campaign) {
        this.LOG_TAG = "SwrveConversation";
        this.setCampaign(campaign);
        this.setConversationController(conversationController);
    }
    
    public SwrveConversation(final SwrveBase<?, ?> swrveBase, final SwrveConversationCampaign swrveConversationCampaign, final JSONObject jsonObject) throws JSONException {
        this(swrveBase, swrveConversationCampaign);
        ArrayList<ConversationPage> pages = null;
        try {
            this.setId(jsonObject.getInt("id"));
            this.setName(jsonObject.getString("id"));
            final JSONArray jsonArray = jsonObject.getJSONArray("pages");
            pages = new ArrayList<ConversationPage>();
            for (int i = 0; i < jsonArray.length(); ++i) {
                pages.add(ConversationPage.fromJson(jsonArray.getJSONObject(i)));
            }
        }
        catch (Exception ex) {
            try {
                this.setId(Integer.valueOf(jsonObject.getString("id")));
            }
            catch (Exception ex2) {
                Log.e("SwrveConversation", "Could not cast String into ID");
            }
        }
        this.setPages(pages);
    }
    
    private void setConversationController(final SwrveBase<?, ?> conversationController) {
        this.conversationController = conversationController;
        if (conversationController != null) {
            this.setCacheDir(conversationController.getCacheDir());
        }
    }
    
    protected boolean assetInCache(final String s) {
        final Set assetsOnDisk = this.conversationController.getAssetsOnDisk();
        return !SwrveHelper.isNullOrEmpty(s) && assetsOnDisk.contains(s);
    }
    
    public File getCacheDir() {
        return this.cacheDir;
    }
    
    public SwrveConversationCampaign getCampaign() {
        return this.campaign;
    }
    
    public ConversationPage getFirstPage() {
        return this.pages.get(0);
    }
    
    public int getId() {
        return this.id;
    }
    
    public ConversationPage getPageForControl(final ControlBase controlBase) {
        final ConversationPage conversationPage = null;
        final Iterator<ConversationPage> iterator = this.pages.iterator();
        ConversationPage conversationPage2;
        do {
            conversationPage2 = conversationPage;
            if (!iterator.hasNext()) {
                break;
            }
            conversationPage2 = iterator.next();
        } while (!conversationPage2.hasTag(controlBase.getTarget()));
        return conversationPage2;
    }
    
    public ArrayList<ConversationPage> getPages() {
        return this.pages;
    }
    
    public boolean isDownloaded() {
        if (this.pages != null) {
            final Iterator<ConversationPage> iterator = this.pages.iterator();
            while (iterator.hasNext()) {
                for (final ConversationAtom conversationAtom : iterator.next().getContent()) {
                    if ("image".equalsIgnoreCase(conversationAtom.getType().toString())) {
                        final Content content = (Content)conversationAtom;
                        if (!this.assetInCache(content.getValue())) {
                            Log.i("SwrveConversation", "Conversation asset not yet downloaded: " + content.getValue());
                            return false;
                        }
                        continue;
                    }
                }
            }
        }
        return true;
    }
    
    protected void setCacheDir(final File cacheDir) {
        this.cacheDir = cacheDir;
    }
    
    protected void setCampaign(final SwrveConversationCampaign campaign) {
        this.campaign = campaign;
    }
    
    protected void setId(final int id) {
        this.id = id;
    }
    
    protected void setName(final String name) {
        this.name = name;
    }
    
    public void setPages(final ArrayList<ConversationPage> pages) {
        this.pages = pages;
    }
}
