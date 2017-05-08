// 
// Decompiled by Procyon v0.5.30
// 

package org.json.simple.parser;

public class Yytoken
{
    public int type;
    public Object value;
    
    public Yytoken(final int type, final Object value) {
        this.type = 0;
        this.value = null;
        this.type = type;
        this.value = value;
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        switch (this.type) {
            case 0: {
                sb.append("VALUE(").append(this.value).append(")");
                break;
            }
            case 1: {
                sb.append("LEFT BRACE({)");
                break;
            }
            case 2: {
                sb.append("RIGHT BRACE(})");
                break;
            }
            case 3: {
                sb.append("LEFT SQUARE([)");
                break;
            }
            case 4: {
                sb.append("RIGHT SQUARE(])");
                break;
            }
            case 5: {
                sb.append("COMMA(,)");
                break;
            }
            case 6: {
                sb.append("COLON(:)");
                break;
            }
            case -1: {
                sb.append("END OF FILE");
                break;
            }
        }
        return sb.toString();
    }
}
