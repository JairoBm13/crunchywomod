// 
// Decompiled by Procyon v0.5.30
// 

package com.google.android.gms.cast;

import org.json.JSONException;
import com.google.android.gms.common.internal.zzt;
import com.google.android.gms.cast.internal.zzf;
import com.google.android.gms.internal.zzlh;
import android.graphics.Color;
import org.json.JSONObject;

public final class TextTrackStyle
{
    private JSONObject zzRJ;
    private float zzTc;
    private int zzTd;
    private int zzTe;
    private int zzTf;
    private int zzTg;
    private int zzTh;
    private int zzTi;
    private String zzTj;
    private int zzTk;
    private int zzTl;
    private int zzvc;
    
    public TextTrackStyle() {
        this.clear();
    }
    
    private void clear() {
        this.zzTc = 1.0f;
        this.zzTd = 0;
        this.zzvc = 0;
        this.zzTe = -1;
        this.zzTf = 0;
        this.zzTg = -1;
        this.zzTh = 0;
        this.zzTi = 0;
        this.zzTj = null;
        this.zzTk = -1;
        this.zzTl = -1;
        this.zzRJ = null;
    }
    
    private String zzG(final int n) {
        return String.format("#%02X%02X%02X%02X", Color.red(n), Color.green(n), Color.blue(n), Color.alpha(n));
    }
    
    private int zzbz(final String s) {
        int argb;
        final int n = argb = 0;
        if (s == null) {
            return argb;
        }
        argb = n;
        if (s.length() != 9) {
            return argb;
        }
        argb = n;
        if (s.charAt(0) != '#') {
            return argb;
        }
        try {
            argb = Color.argb(Integer.parseInt(s.substring(7, 9), 16), Integer.parseInt(s.substring(1, 3), 16), Integer.parseInt(s.substring(3, 5), 16), Integer.parseInt(s.substring(5, 7), 16));
            return argb;
        }
        catch (NumberFormatException ex) {
            return 0;
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = true;
        final boolean b2 = false;
        boolean b3;
        if (this == o) {
            b3 = true;
        }
        else {
            b3 = b2;
            if (o instanceof TextTrackStyle) {
                final TextTrackStyle textTrackStyle = (TextTrackStyle)o;
                int n;
                if (this.zzRJ == null) {
                    n = 1;
                }
                else {
                    n = 0;
                }
                int n2;
                if (textTrackStyle.zzRJ == null) {
                    n2 = 1;
                }
                else {
                    n2 = 0;
                }
                b3 = b2;
                if (n == n2) {
                    if (this.zzRJ != null && textTrackStyle.zzRJ != null) {
                        b3 = b2;
                        if (!zzlh.zzd(this.zzRJ, textTrackStyle.zzRJ)) {
                            return b3;
                        }
                    }
                    return this.zzTc == textTrackStyle.zzTc && this.zzTd == textTrackStyle.zzTd && this.zzvc == textTrackStyle.zzvc && this.zzTe == textTrackStyle.zzTe && this.zzTf == textTrackStyle.zzTf && this.zzTg == textTrackStyle.zzTg && this.zzTi == textTrackStyle.zzTi && zzf.zza(this.zzTj, textTrackStyle.zzTj) && this.zzTk == textTrackStyle.zzTk && this.zzTl == textTrackStyle.zzTl && b;
                }
            }
        }
        return b3;
    }
    
    @Override
    public int hashCode() {
        return zzt.hashCode(this.zzTc, this.zzTd, this.zzvc, this.zzTe, this.zzTf, this.zzTg, this.zzTh, this.zzTi, this.zzTj, this.zzTk, this.zzTl, this.zzRJ);
    }
    
    public JSONObject toJson() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("fontScale", (double)this.zzTc);
            if (this.zzTd != 0) {
                jsonObject.put("foregroundColor", (Object)this.zzG(this.zzTd));
            }
            if (this.zzvc != 0) {
                jsonObject.put("backgroundColor", (Object)this.zzG(this.zzvc));
            }
            switch (this.zzTe) {
                case 0: {
                    jsonObject.put("edgeType", (Object)"NONE");
                    break;
                }
                case 1: {
                    jsonObject.put("edgeType", (Object)"OUTLINE");
                    break;
                }
                case 2: {
                    jsonObject.put("edgeType", (Object)"DROP_SHADOW");
                    break;
                }
                case 3: {
                    jsonObject.put("edgeType", (Object)"RAISED");
                    break;
                }
                case 4: {
                    jsonObject.put("edgeType", (Object)"DEPRESSED");
                    break;
                }
            }
            if (this.zzTf != 0) {
                jsonObject.put("edgeColor", (Object)this.zzG(this.zzTf));
            }
            switch (this.zzTg) {
                case 0: {
                    jsonObject.put("windowType", (Object)"NONE");
                    break;
                }
                case 1: {
                    jsonObject.put("windowType", (Object)"NORMAL");
                    break;
                }
                case 2: {
                    jsonObject.put("windowType", (Object)"ROUNDED_CORNERS");
                    break;
                }
            }
            if (this.zzTh != 0) {
                jsonObject.put("windowColor", (Object)this.zzG(this.zzTh));
            }
            if (this.zzTg == 2) {
                jsonObject.put("windowRoundedCornerRadius", this.zzTi);
            }
            if (this.zzTj != null) {
                jsonObject.put("fontFamily", (Object)this.zzTj);
            }
            switch (this.zzTk) {
                case 0: {
                    jsonObject.put("fontGenericFamily", (Object)"SANS_SERIF");
                    break;
                }
                case 1: {
                    jsonObject.put("fontGenericFamily", (Object)"MONOSPACED_SANS_SERIF");
                    break;
                }
                case 2: {
                    jsonObject.put("fontGenericFamily", (Object)"SERIF");
                    break;
                }
                case 3: {
                    jsonObject.put("fontGenericFamily", (Object)"MONOSPACED_SERIF");
                    break;
                }
                case 4: {
                    jsonObject.put("fontGenericFamily", (Object)"CASUAL");
                    break;
                }
                case 5: {
                    jsonObject.put("fontGenericFamily", (Object)"CURSIVE");
                    break;
                }
                case 6: {
                    jsonObject.put("fontGenericFamily", (Object)"SMALL_CAPITALS");
                    break;
                }
            }
            switch (this.zzTl) {
                case 0: {
                    jsonObject.put("fontStyle", (Object)"NORMAL");
                    break;
                }
                case 1: {
                    jsonObject.put("fontStyle", (Object)"BOLD");
                    break;
                }
                case 2: {
                    jsonObject.put("fontStyle", (Object)"ITALIC");
                    break;
                }
                case 3: {
                    jsonObject.put("fontStyle", (Object)"BOLD_ITALIC");
                    break;
                }
            }
            if (this.zzRJ != null) {
                jsonObject.put("customData", (Object)this.zzRJ);
                return jsonObject;
            }
            return jsonObject;
        }
        catch (JSONException ex) {
            return jsonObject;
        }
    }
    
    public void zzf(final JSONObject jsonObject) throws JSONException {
        this.clear();
        this.zzTc = (float)jsonObject.optDouble("fontScale", 1.0);
        this.zzTd = this.zzbz(jsonObject.optString("foregroundColor"));
        this.zzvc = this.zzbz(jsonObject.optString("backgroundColor"));
        if (jsonObject.has("edgeType")) {
            final String string = jsonObject.getString("edgeType");
            if ("NONE".equals(string)) {
                this.zzTe = 0;
            }
            else if ("OUTLINE".equals(string)) {
                this.zzTe = 1;
            }
            else if ("DROP_SHADOW".equals(string)) {
                this.zzTe = 2;
            }
            else if ("RAISED".equals(string)) {
                this.zzTe = 3;
            }
            else if ("DEPRESSED".equals(string)) {
                this.zzTe = 4;
            }
        }
        this.zzTf = this.zzbz(jsonObject.optString("edgeColor"));
        if (jsonObject.has("windowType")) {
            final String string2 = jsonObject.getString("windowType");
            if ("NONE".equals(string2)) {
                this.zzTg = 0;
            }
            else if ("NORMAL".equals(string2)) {
                this.zzTg = 1;
            }
            else if ("ROUNDED_CORNERS".equals(string2)) {
                this.zzTg = 2;
            }
        }
        this.zzTh = this.zzbz(jsonObject.optString("windowColor"));
        if (this.zzTg == 2) {
            this.zzTi = jsonObject.optInt("windowRoundedCornerRadius", 0);
        }
        this.zzTj = jsonObject.optString("fontFamily", (String)null);
        if (jsonObject.has("fontGenericFamily")) {
            final String string3 = jsonObject.getString("fontGenericFamily");
            if ("SANS_SERIF".equals(string3)) {
                this.zzTk = 0;
            }
            else if ("MONOSPACED_SANS_SERIF".equals(string3)) {
                this.zzTk = 1;
            }
            else if ("SERIF".equals(string3)) {
                this.zzTk = 2;
            }
            else if ("MONOSPACED_SERIF".equals(string3)) {
                this.zzTk = 3;
            }
            else if ("CASUAL".equals(string3)) {
                this.zzTk = 4;
            }
            else if ("CURSIVE".equals(string3)) {
                this.zzTk = 5;
            }
            else if ("SMALL_CAPITALS".equals(string3)) {
                this.zzTk = 6;
            }
        }
        if (jsonObject.has("fontStyle")) {
            final String string4 = jsonObject.getString("fontStyle");
            if ("NORMAL".equals(string4)) {
                this.zzTl = 0;
            }
            else if ("BOLD".equals(string4)) {
                this.zzTl = 1;
            }
            else if ("ITALIC".equals(string4)) {
                this.zzTl = 2;
            }
            else if ("BOLD_ITALIC".equals(string4)) {
                this.zzTl = 3;
            }
        }
        this.zzRJ = jsonObject.optJSONObject("customData");
    }
}
