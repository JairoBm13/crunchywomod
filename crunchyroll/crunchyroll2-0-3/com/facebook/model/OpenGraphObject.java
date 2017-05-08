// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.model;

import java.util.Arrays;
import java.util.List;
import java.util.Date;

public interface OpenGraphObject extends GraphObject
{
    GraphObject getApplication();
    
    GraphObjectList<GraphObject> getAudio();
    
    @PropertyName("fbsdk:create_object")
    boolean getCreateObject();
    
    Date getCreatedTime();
    
    GraphObject getData();
    
    String getDescription();
    
    String getDeterminer();
    
    String getId();
    
    GraphObjectList<GraphObject> getImage();
    
    boolean getIsScraped();
    
    String getPostActionId();
    
    List<String> getSeeAlso();
    
    String getSiteName();
    
    String getTitle();
    
    String getType();
    
    Date getUpdatedTime();
    
    String getUrl();
    
    GraphObjectList<GraphObject> getVideo();
    
    void setApplication(final GraphObject p0);
    
    void setAudio(final GraphObjectList<GraphObject> p0);
    
    @PropertyName("fbsdk:create_object")
    void setCreateObject(final boolean p0);
    
    void setCreatedTime(final Date p0);
    
    void setData(final GraphObject p0);
    
    void setDescription(final String p0);
    
    void setDeterminer(final String p0);
    
    void setId(final String p0);
    
    void setImage(final GraphObjectList<GraphObject> p0);
    
    @CreateGraphObject("url")
    @PropertyName("image")
    void setImageUrls(final List<String> p0);
    
    void setIsScraped(final boolean p0);
    
    void setPostActionId(final String p0);
    
    void setSeeAlso(final List<String> p0);
    
    void setSiteName(final String p0);
    
    void setTitle(final String p0);
    
    void setType(final String p0);
    
    void setUpdatedTime(final Date p0);
    
    void setUrl(final String p0);
    
    void setVideo(final GraphObjectList<GraphObject> p0);
    
    public static final class Factory
    {
        public static <T extends OpenGraphObject> T createForPost(final Class<T> clazz, final String s) {
            return createForPost(clazz, s, null, null, null, null);
        }
        
        public static <T extends OpenGraphObject> T createForPost(final Class<T> clazz, final String type, final String title, final String s, final String url, final String description) {
            final OpenGraphObject openGraphObject = GraphObject.Factory.create(clazz);
            if (type != null) {
                openGraphObject.setType(type);
            }
            if (title != null) {
                openGraphObject.setTitle(title);
            }
            if (s != null) {
                openGraphObject.setImageUrls(Arrays.asList(s));
            }
            if (url != null) {
                openGraphObject.setUrl(url);
            }
            if (description != null) {
                openGraphObject.setDescription(description);
            }
            openGraphObject.setCreateObject(true);
            openGraphObject.setData(GraphObject.Factory.create());
            return (T)openGraphObject;
        }
        
        public static OpenGraphObject createForPost(final String s) {
            return createForPost(OpenGraphObject.class, s);
        }
    }
}
