// 
// Decompiled by Procyon v0.5.30
// 

package org.json.simple.parser;

import java.io.IOException;
import org.json.simple.JSONObject;
import java.util.Map;
import org.json.simple.JSONArray;
import java.util.List;
import java.io.Reader;
import java.util.LinkedList;

public class JSONParser
{
    private LinkedList handlerStatusStack;
    private Yylex lexer;
    private int status;
    private Yytoken token;
    
    public JSONParser() {
        this.lexer = new Yylex(null);
        this.token = null;
        this.status = 0;
    }
    
    private List createArrayContainer(final ContainerFactory containerFactory) {
        List creatArrayContainer;
        if (containerFactory == null) {
            creatArrayContainer = new JSONArray();
        }
        else if ((creatArrayContainer = containerFactory.creatArrayContainer()) == null) {
            return new JSONArray();
        }
        return creatArrayContainer;
    }
    
    private Map createObjectContainer(final ContainerFactory containerFactory) {
        Map objectContainer;
        if (containerFactory == null) {
            objectContainer = new JSONObject();
        }
        else if ((objectContainer = containerFactory.createObjectContainer()) == null) {
            return new JSONObject();
        }
        return objectContainer;
    }
    
    private void nextToken() throws ParseException, IOException {
        this.token = this.lexer.yylex();
        if (this.token == null) {
            this.token = new Yytoken(-1, null);
        }
    }
    
    private int peekStatus(final LinkedList list) {
        if (list.size() == 0) {
            return -1;
        }
        return list.getFirst();
    }
    
    public int getPosition() {
        return this.lexer.getPosition();
    }
    
    public Object parse(final Reader reader) throws IOException, ParseException {
        return this.parse(reader, null);
    }
    
    public Object parse(final Reader reader, final ContainerFactory containerFactory) throws IOException, ParseException {
        this.reset(reader);
        final LinkedList<Integer> list = new LinkedList<Integer>();
        final LinkedList<String> list2 = new LinkedList<String>();
    Label_0068_Outer:
        while (true) {
            Label_0241: {
                while (true) {
                    Label_0961: {
                        Label_0682: {
                            Label_0427: {
                                Label_0275: {
                                    Label_0096: {
                                        try {
                                            this.nextToken();
                                            switch (this.status) {
                                                case 0: {
                                                    break Label_0096;
                                                }
                                                case 1: {
                                                    break Label_0241;
                                                }
                                                case 2: {
                                                    break Label_0275;
                                                }
                                                case 4: {
                                                    break Label_0427;
                                                }
                                                case 3: {
                                                    break Label_0682;
                                                }
                                                case -1: {
                                                    throw new ParseException(this.getPosition(), 1, this.token);
                                                }
                                                default: {
                                                    break Label_0961;
                                                }
                                            }
                                            throw new ParseException(this.getPosition(), 1, this.token);
                                            // iftrue(Label_0931:, this.status != -1)
                                            throw new ParseException(this.getPosition(), 1, this.token);
                                        }
                                        catch (IOException ex) {
                                            throw ex;
                                        }
                                    }
                                    switch (this.token.type) {
                                        default:
                                        case 2: {
                                            this.status = -1;
                                            continue;
                                        }
                                        case 0: {
                                            this.status = 1;
                                            list.addFirst(new Integer(this.status));
                                            list2.addFirst((String)this.token.value);
                                            continue;
                                        }
                                        case 1: {
                                            this.status = 2;
                                            list.addFirst(new Integer(this.status));
                                            list2.addFirst((String)this.createObjectContainer(containerFactory));
                                            continue;
                                        }
                                        case 3: {
                                            this.status = 3;
                                            list.addFirst(new Integer(this.status));
                                            list2.addFirst((String)this.createArrayContainer(containerFactory));
                                            continue;
                                        }
                                    }
                                }
                                switch (this.token.type) {
                                    default:
                                    case 1:
                                    case 3:
                                    case 4: {
                                        this.status = -1;
                                        continue;
                                    }
                                    case 5: {
                                        continue;
                                    }
                                    case 0: {
                                        if (this.token.value instanceof String) {
                                            list2.addFirst((String)this.token.value);
                                            this.status = 4;
                                            list.addFirst(new Integer(this.status));
                                            continue;
                                        }
                                        this.status = -1;
                                        continue;
                                    }
                                    case 2: {
                                        if (list2.size() > 1) {
                                            list.removeFirst();
                                            list2.removeFirst();
                                            this.status = this.peekStatus(list);
                                            continue;
                                        }
                                        this.status = 1;
                                        continue;
                                    }
                                }
                            }
                            switch (this.token.type) {
                                case 1: {
                                    list.removeFirst();
                                    final String s = list2.removeFirst();
                                    final Map<String, Object> map = (Map<String, Object>)list2.getFirst();
                                    final Map objectContainer = this.createObjectContainer(containerFactory);
                                    map.put(s, objectContainer);
                                    this.status = 2;
                                    list.addFirst(new Integer(this.status));
                                    list2.addFirst((String)objectContainer);
                                    continue;
                                }
                                case 3: {
                                    list.removeFirst();
                                    final String s2 = list2.removeFirst();
                                    final Map<String, Object> map2 = (Map<String, Object>)list2.getFirst();
                                    final List arrayContainer = this.createArrayContainer(containerFactory);
                                    map2.put(s2, (Map<String, Object>)arrayContainer);
                                    this.status = 3;
                                    list.addFirst(new Integer(this.status));
                                    list2.addFirst((String)arrayContainer);
                                    continue;
                                }
                                case 0: {
                                    list.removeFirst();
                                    ((Map<String, Object>)list2.getFirst()).put(list2.removeFirst(), this.token.value);
                                    this.status = this.peekStatus(list);
                                    continue;
                                }
                                default:
                                case 2:
                                case 4:
                                case 5: {
                                    this.status = -1;
                                    continue;
                                }
                                case 6: {
                                    continue;
                                }
                            }
                        }
                        switch (this.token.type) {
                            case 3: {
                                final List<Object> list3 = (List<Object>)list2.getFirst();
                                final List arrayContainer2 = this.createArrayContainer(containerFactory);
                                list3.add(arrayContainer2);
                                this.status = 3;
                                list.addFirst(new Integer(this.status));
                                list2.addFirst((String)arrayContainer2);
                                continue;
                            }
                            case 1: {
                                final List<Object> list4 = (List<Object>)list2.getFirst();
                                final Map objectContainer2 = this.createObjectContainer(containerFactory);
                                list4.add(objectContainer2);
                                this.status = 2;
                                list.addFirst(new Integer(this.status));
                                list2.addFirst((String)objectContainer2);
                                continue;
                            }
                            case 0: {
                                ((List<Object>)list2.getFirst()).add(this.token.value);
                                continue;
                            }
                            default:
                            case 2: {
                                this.status = -1;
                                continue;
                            }
                            case 5: {
                                continue;
                            }
                            case 4: {
                                if (list2.size() > 1) {
                                    list.removeFirst();
                                    list2.removeFirst();
                                    this.status = this.peekStatus(list);
                                    continue;
                                }
                                this.status = 1;
                                continue;
                            }
                        }
                        Label_0931: {
                            if (this.token.type == -1) {
                                throw new ParseException(this.getPosition(), 1, this.token);
                            }
                        }
                        continue Label_0068_Outer;
                    }
                    continue;
                }
            }
            if (this.token.type == -1) {
                return list2.removeFirst();
            }
            throw new ParseException(this.getPosition(), 1, this.token);
        }
    }
    
    public void reset() {
        this.token = null;
        this.status = 0;
        this.handlerStatusStack = null;
    }
    
    public void reset(final Reader reader) {
        this.lexer.yyreset(reader);
        this.reset();
    }
}
