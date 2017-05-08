// 
// Decompiled by Procyon v0.5.30
// 

package com.swrve.sdk.messaging;

import java.util.concurrent.TimeUnit;
import com.swrve.sdk.conversations.ISwrveConversationListener;
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
import com.swrve.sdk.SwrveResourceManager;
import android.view.Display;
import java.util.GregorianCalendar;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.os.Build$VERSION;
import android.annotation.SuppressLint;
import java.util.Collections;
import android.content.Context;
import com.swrve.sdk.conversations.SwrveConversation;
import java.util.Map;
import java.util.HashMap;
import java.util.Date;
import com.swrve.sdk.ISwrveBase;
import com.swrve.sdk.SwrveImp;
import com.swrve.sdk.config.SwrveConfigBase;
import android.util.Log;
import java.util.Iterator;
import java.util.Set;
import com.swrve.sdk.SwrveHelper;
import org.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import com.swrve.sdk.SwrveBase;
import java.util.List;
import java.io.File;

public class SwrveMessage
{
    protected File cacheDir;
    protected SwrveCampaign campaign;
    protected List<SwrveMessageFormat> formats;
    protected int id;
    protected SwrveBase<?, ?> messageController;
    protected String name;
    protected int priority;
    
    public SwrveMessage(final SwrveBase<?, ?> messageController, final SwrveCampaign campaign) {
        this.priority = 9999;
        this.campaign = campaign;
        this.formats = new ArrayList<SwrveMessageFormat>();
        this.setMessageController(messageController);
    }
    
    public SwrveMessage(final SwrveBase<?, ?> swrveBase, final SwrveCampaign swrveCampaign, final JSONObject jsonObject) throws JSONException {
        this(swrveBase, swrveCampaign);
        this.setId(jsonObject.getInt("id"));
        this.setName(jsonObject.getString("name"));
        if (jsonObject.has("priority")) {
            this.setPriority(jsonObject.getInt("priority"));
        }
        final JSONArray jsonArray = jsonObject.getJSONObject("template").getJSONArray("formats");
        for (int i = 0; i < jsonArray.length(); ++i) {
            this.getFormats().add(this.createMessageFormat(swrveBase, this, jsonArray.getJSONObject(i)));
        }
    }
    
    protected boolean assetInCache(final String s) {
        final Set assetsOnDisk = this.messageController.getAssetsOnDisk();
        return SwrveHelper.isNullOrEmpty(s) || assetsOnDisk.contains(s);
    }
    
    protected SwrveMessageFormat createMessageFormat(final SwrveBase<?, ?> swrveBase, final SwrveMessage swrveMessage, final JSONObject jsonObject) throws JSONException {
        return new SwrveMessageFormat(swrveBase, swrveMessage, jsonObject);
    }
    
    public File getCacheDir() {
        return this.cacheDir;
    }
    
    public SwrveCampaign getCampaign() {
        return this.campaign;
    }
    
    public SwrveMessageFormat getFormat(final SwrveOrientation swrveOrientation) {
        if (this.formats != null) {
            for (final SwrveMessageFormat swrveMessageFormat : this.formats) {
                if (swrveMessageFormat.getOrientation() == swrveOrientation) {
                    return swrveMessageFormat;
                }
            }
        }
        return null;
    }
    
    public List<SwrveMessageFormat> getFormats() {
        return this.formats;
    }
    
    public int getId() {
        return this.id;
    }
    
    public SwrveBase<?, ?> getMessageController() {
        return this.messageController;
    }
    
    public int getPriority() {
        return this.priority;
    }
    
    public boolean isDownloaded() {
        if (this.formats != null) {
            for (final SwrveMessageFormat swrveMessageFormat : this.formats) {
                final Iterator<SwrveButton> iterator2 = swrveMessageFormat.buttons.iterator();
                while (iterator2.hasNext()) {
                    final String image = iterator2.next().getImage();
                    if (!this.assetInCache(image)) {
                        Log.i("SwrveSDK", "Button asset not yet downloaded: " + image);
                        return false;
                    }
                }
                final Iterator<SwrveImage> iterator3 = swrveMessageFormat.images.iterator();
                while (iterator3.hasNext()) {
                    final String file = iterator3.next().getFile();
                    if (!this.assetInCache(file)) {
                        Log.i("SwrveSDK", "Image asset not yet downloaded: " + file);
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    protected void setCacheDir(final File cacheDir) {
        this.cacheDir = cacheDir;
    }
    
    protected void setId(final int id) {
        this.id = id;
    }
    
    public void setMessageController(final SwrveBase<?, ?> messageController) {
        this.messageController = messageController;
        if (messageController != null) {
            this.setCacheDir(messageController.getCacheDir());
        }
    }
    
    protected void setName(final String name) {
        this.name = name;
    }
    
    public void setPriority(final int priority) {
        this.priority = priority;
    }
    
    public boolean supportsOrientation(final SwrveOrientation swrveOrientation) {
        return swrveOrientation == SwrveOrientation.Both || this.getFormat(swrveOrientation) != null;
    }
}
