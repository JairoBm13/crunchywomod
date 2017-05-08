// 
// Decompiled by Procyon v0.5.30
// 

package tv.ouya.console.api;

import android.os.Parcel;
import android.os.Parcelable$Creator;
import android.os.Parcelable;

public class UserInfo implements Parcelable
{
    public static final Parcelable$Creator<UserInfo> CREATOR;
    private String email;
    private boolean founder;
    private String password;
    private String password2;
    private String username;
    
    static {
        CREATOR = (Parcelable$Creator)new Parcelable$Creator<UserInfo>() {
            public UserInfo createFromParcel(final Parcel parcel) {
                boolean b = true;
                final String string = parcel.readString();
                final String string2 = parcel.readString();
                final String string3 = parcel.readString();
                final String string4 = parcel.readString();
                if (parcel.readByte() != 1) {
                    b = false;
                }
                return new UserInfo(string, string2, b, string3, string4);
            }
            
            public UserInfo[] newArray(final int n) {
                return new UserInfo[n];
            }
        };
    }
    
    public UserInfo() {
    }
    
    public UserInfo(final String username, final String email, final boolean founder) {
        this.username = username;
        this.email = email;
        this.founder = founder;
    }
    
    public UserInfo(final String s, final String s2, final boolean b, final String password, final String password2) {
        this(s, s2, b);
        this.password = password;
        this.password2 = password2;
    }
    
    public int describeContents() {
        return 0;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this != o) {
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            final UserInfo userInfo = (UserInfo)o;
            Label_0059: {
                if (this.email != null) {
                    if (this.email.equals(userInfo.email)) {
                        break Label_0059;
                    }
                }
                else if (userInfo.email == null) {
                    break Label_0059;
                }
                return false;
            }
            Label_0089: {
                if (this.password != null) {
                    if (this.password.equals(userInfo.password)) {
                        break Label_0089;
                    }
                }
                else if (userInfo.password == null) {
                    break Label_0089;
                }
                return false;
            }
            Label_0119: {
                if (this.password2 != null) {
                    if (this.password2.equals(userInfo.password2)) {
                        break Label_0119;
                    }
                }
                else if (userInfo.password2 == null) {
                    break Label_0119;
                }
                return false;
            }
            Label_0149: {
                if (this.username != null) {
                    if (this.username.equals(userInfo.username)) {
                        break Label_0149;
                    }
                }
                else if (userInfo.username == null) {
                    break Label_0149;
                }
                return false;
            }
            if (this.founder != userInfo.founder) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int hashCode = 0;
        int hashCode2;
        if (this.username != null) {
            hashCode2 = this.username.hashCode();
        }
        else {
            hashCode2 = 0;
        }
        int hashCode3;
        if (this.email != null) {
            hashCode3 = this.email.hashCode();
        }
        else {
            hashCode3 = 0;
        }
        int hashCode4;
        if (this.password != null) {
            hashCode4 = this.password.hashCode();
        }
        else {
            hashCode4 = 0;
        }
        if (this.password2 != null) {
            hashCode = this.password2.hashCode();
        }
        return (((hashCode2 * 31 + hashCode3) * 31 + hashCode4) * 31 + hashCode) * 31 + new Boolean(this.founder).hashCode();
    }
    
    @Override
    public String toString() {
        final StringBuilder append = new StringBuilder().append("UserInfo{username='").append(this.username).append('\'').append(", email='").append(this.email).append('\'').append(", password='").append(this.password).append('\'').append(", password2='").append(this.password2).append('\'').append(", founder='");
        int n;
        if (this.founder) {
            n = 1;
        }
        else {
            n = 0;
        }
        return append.append(n).append('\'').append('}').toString();
    }
    
    public void writeToParcel(final Parcel parcel, int n) {
        parcel.writeString(this.username);
        parcel.writeString(this.email);
        parcel.writeString(this.password);
        parcel.writeString(this.password2);
        if (this.founder) {
            n = 1;
        }
        else {
            n = 0;
        }
        parcel.writeByte((byte)n);
    }
}
