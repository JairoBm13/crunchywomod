// 
// Decompiled by Procyon v0.5.30
// 

package com.crunchyroll.android.api.models;

import java.util.Iterator;
import java.util.List;
import java.io.Serializable;

public class ClientOptions implements Serializable
{
    private List<NameValuePair> nameValuePairList;
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o == null) {
                return false;
            }
            if (this.getClass() != o.getClass()) {
                return false;
            }
            final ClientOptions clientOptions = (ClientOptions)o;
            if (this.nameValuePairList == null) {
                if (clientOptions.nameValuePairList != null) {
                    return false;
                }
            }
            else if (this.hashCode() != clientOptions.hashCode()) {
                return false;
            }
        }
        return true;
    }
    
    public List<NameValuePair> getNameValuePairList() {
        return this.nameValuePairList;
    }
    
    @Override
    public int hashCode() {
        int n = 1;
        int n2 = 1;
        if (this.nameValuePairList != null) {
            final Iterator<NameValuePair> iterator = this.nameValuePairList.iterator();
            while (true) {
                n = n2;
                if (!iterator.hasNext()) {
                    break;
                }
                final NameValuePair nameValuePair = iterator.next();
                int hashCode;
                if (nameValuePair.getName() == null) {
                    hashCode = 0;
                }
                else {
                    hashCode = nameValuePair.getName().hashCode();
                }
                int hashCode2;
                if (nameValuePair.getValue().toString() == null) {
                    hashCode2 = 0;
                }
                else {
                    hashCode2 = nameValuePair.getValue().toString().hashCode();
                }
                n2 = (n2 * 31 + hashCode) * 31 + hashCode2;
            }
        }
        return n;
    }
    
    public void setNameValuePairList(final List<NameValuePair> nameValuePairList) {
        this.nameValuePairList = nameValuePairList;
    }
    
    @Override
    public String toString() {
        String string = "ClientOptions [";
        for (final NameValuePair nameValuePair : this.nameValuePairList) {
            final StringBuilder append = new StringBuilder().append(string).append("{name=");
            String name;
            if (nameValuePair.getName() == null) {
                name = "null";
            }
            else {
                name = nameValuePair.getName();
            }
            final StringBuilder append2 = new StringBuilder().append(append.append(name).toString()).append(", value=");
            String string2;
            if (nameValuePair.getValue().toString() == null) {
                string2 = "null";
            }
            else {
                string2 = nameValuePair.getValue().toString();
            }
            string = append2.append(string2).append("}").toString();
        }
        return string;
    }
}
