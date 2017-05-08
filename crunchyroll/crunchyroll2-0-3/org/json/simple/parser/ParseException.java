// 
// Decompiled by Procyon v0.5.30
// 

package org.json.simple.parser;

public class ParseException extends Exception
{
    private int errorType;
    private int position;
    private Object unexpectedObject;
    
    public ParseException(final int position, final int errorType, final Object unexpectedObject) {
        this.position = position;
        this.errorType = errorType;
        this.unexpectedObject = unexpectedObject;
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        switch (this.errorType) {
            default: {
                sb.append("Unkown error at position ").append(this.position).append(".");
                break;
            }
            case 0: {
                sb.append("Unexpected character (").append(this.unexpectedObject).append(") at position ").append(this.position).append(".");
                break;
            }
            case 1: {
                sb.append("Unexpected token ").append(this.unexpectedObject).append(" at position ").append(this.position).append(".");
                break;
            }
            case 2: {
                sb.append("Unexpected exception at position ").append(this.position).append(": ").append(this.unexpectedObject);
                break;
            }
        }
        return sb.toString();
    }
}
