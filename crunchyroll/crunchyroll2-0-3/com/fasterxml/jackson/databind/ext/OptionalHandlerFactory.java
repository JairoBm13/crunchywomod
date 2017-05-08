// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.ext;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JavaType;
import java.io.Serializable;

public class OptionalHandlerFactory implements Serializable
{
    public static final OptionalHandlerFactory instance;
    
    static {
        instance = new OptionalHandlerFactory();
    }
    
    private boolean doesImplement(Class<?> superclass, final String s) {
        while (superclass != null) {
            if (superclass.getName().equals(s) || this.hasInterface(superclass, s)) {
                return true;
            }
            superclass = superclass.getSuperclass();
        }
        return false;
    }
    
    private boolean hasInterface(final Class<?> clazz, final String s) {
        final Class[] interfaces = clazz.getInterfaces();
        for (int length = interfaces.length, i = 0; i < length; ++i) {
            if (interfaces[i].getName().equals(s)) {
                return true;
            }
        }
        for (int length2 = interfaces.length, j = 0; j < length2; ++j) {
            if (this.hasInterface(interfaces[j], s)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean hasInterfaceStartingWith(final Class<?> clazz, final String s) {
        final Class[] interfaces = clazz.getInterfaces();
        for (int length = interfaces.length, i = 0; i < length; ++i) {
            if (interfaces[i].getName().startsWith(s)) {
                return true;
            }
        }
        for (int length2 = interfaces.length, j = 0; j < length2; ++j) {
            if (this.hasInterfaceStartingWith(interfaces[j], s)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean hasSupertypeStartingWith(final Class<?> clazz, final String s) {
        Class<?> clazz2 = clazz.getSuperclass();
        Class<?> superclass;
        while (true) {
            superclass = clazz;
            if (clazz2 == null) {
                break;
            }
            if (clazz2.getName().startsWith(s)) {
                return true;
            }
            clazz2 = clazz2.getSuperclass();
        }
        while (superclass != null) {
            if (this.hasInterfaceStartingWith(superclass, s)) {
                return true;
            }
            superclass = superclass.getSuperclass();
        }
        return false;
    }
    
    private Object instantiate(final String s) {
        try {
            return Class.forName(s).newInstance();
        }
        catch (Exception ex) {}
        catch (LinkageError linkageError) {
            goto Label_0011;
        }
    }
    
    public JsonDeserializer<?> findDeserializer(final JavaType javaType, final DeserializationConfig deserializationConfig, final BeanDescription beanDescription) throws JsonMappingException {
        final Class<?> rawClass = javaType.getRawClass();
        if (rawClass.getName().startsWith("javax.xml.") || this.hasSupertypeStartingWith(rawClass, "javax.xml.")) {
            final Object instantiate = this.instantiate("com.fasterxml.jackson.databind.ext.CoreXMLDeserializers");
            if (instantiate == null) {
                return null;
            }
            return ((Deserializers)instantiate).findBeanDeserializer(javaType, deserializationConfig, beanDescription);
        }
        else {
            if (this.doesImplement(rawClass, "org.w3c.dom.Node")) {
                return (JsonDeserializer<?>)this.instantiate("com.fasterxml.jackson.databind.ext.DOMDeserializer$DocumentDeserializer");
            }
            if (this.doesImplement(rawClass, "org.w3c.dom.Node")) {
                return (JsonDeserializer<?>)this.instantiate("com.fasterxml.jackson.databind.ext.DOMDeserializer$NodeDeserializer");
            }
            return null;
        }
    }
    
    public JsonSerializer<?> findSerializer(final SerializationConfig serializationConfig, final JavaType javaType, final BeanDescription beanDescription) {
        final Class<?> rawClass = javaType.getRawClass();
        if (rawClass.getName().startsWith("javax.xml.") || this.hasSupertypeStartingWith(rawClass, "javax.xml.")) {
            final Object instantiate = this.instantiate("com.fasterxml.jackson.databind.ext.CoreXMLSerializers");
            if (instantiate == null) {
                return null;
            }
            return ((Serializers)instantiate).findSerializer(serializationConfig, javaType, beanDescription);
        }
        else {
            if (this.doesImplement(rawClass, "org.w3c.dom.Node")) {
                return (JsonSerializer<?>)this.instantiate("com.fasterxml.jackson.databind.ext.DOMSerializer");
            }
            return null;
        }
    }
}
