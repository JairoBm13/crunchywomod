// 
// Decompiled by Procyon v0.5.30
// 

package com.facebook.model;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Collection;
import org.json.JSONException;
import com.facebook.internal.Validate;
import java.util.AbstractList;
import java.lang.reflect.Method;
import com.facebook.internal.Utility;
import java.lang.annotation.Annotation;
import com.facebook.FacebookGraphObjectException;
import java.util.Iterator;
import org.json.JSONArray;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.lang.reflect.ParameterizedType;
import java.util.Locale;
import java.util.HashSet;
import java.text.SimpleDateFormat;
import org.json.JSONObject;
import java.util.Map;

public interface GraphObject
{
    Map<String, Object> asMap();
    
     <T extends GraphObject> T cast(final Class<T> p0);
    
    JSONObject getInnerJSONObject();
    
    Object getProperty(final String p0);
    
     <T extends GraphObject> T getPropertyAs(final String p0, final Class<T> p1);
    
     <T extends GraphObject> GraphObjectList<T> getPropertyAsList(final String p0, final Class<T> p1);
    
    void removeProperty(final String p0);
    
    void setProperty(final String p0, final Object p1);
    
    public static final class Factory
    {
        private static final SimpleDateFormat[] dateFormats;
        private static final HashSet<Class<?>> verifiedGraphObjectClasses;
        
        static {
            verifiedGraphObjectClasses = new HashSet<Class<?>>();
            dateFormats = new SimpleDateFormat[] { new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US), new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US), new SimpleDateFormat("yyyy-MM-dd", Locale.US) };
        }
        
        static <U> U coerceValueToExpectedType(final Object p0, final Class<U> p1, final ParameterizedType p2) {
            // 
            // This method could not be decompiled.
            // 
            // Original Bytecode:
            // 
            //     0: aload_0        
            //     1: ifnonnull       48
            //     4: getstatic       java/lang/Boolean.TYPE:Ljava/lang/Class;
            //     7: aload_1        
            //     8: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
            //    11: ifeq            19
            //    14: iconst_0       
            //    15: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
            //    18: areturn        
            //    19: getstatic       java/lang/Character.TYPE:Ljava/lang/Class;
            //    22: aload_1        
            //    23: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
            //    26: ifeq            34
            //    29: iconst_0       
            //    30: invokestatic    java/lang/Character.valueOf:(C)Ljava/lang/Character;
            //    33: areturn        
            //    34: aload_1        
            //    35: invokevirtual   java/lang/Class.isPrimitive:()Z
            //    38: ifeq            46
            //    41: iconst_0       
            //    42: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
            //    45: areturn        
            //    46: aconst_null    
            //    47: areturn        
            //    48: aload_0        
            //    49: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
            //    52: astore          5
            //    54: aload_1        
            //    55: aload           5
            //    57: invokevirtual   java/lang/Class.isAssignableFrom:(Ljava/lang/Class;)Z
            //    60: ifeq            65
            //    63: aload_0        
            //    64: areturn        
            //    65: aload_1        
            //    66: invokevirtual   java/lang/Class.isPrimitive:()Z
            //    69: ifeq            74
            //    72: aload_0        
            //    73: areturn        
            //    74: ldc             Lcom/facebook/model/GraphObject;.class
            //    76: aload_1        
            //    77: invokevirtual   java/lang/Class.isAssignableFrom:(Ljava/lang/Class;)Z
            //    80: ifeq            154
            //    83: ldc             Lorg/json/JSONObject;.class
            //    85: aload           5
            //    87: invokevirtual   java/lang/Class.isAssignableFrom:(Ljava/lang/Class;)Z
            //    90: ifeq            102
            //    93: aload_1        
            //    94: aload_0        
            //    95: checkcast       Lorg/json/JSONObject;
            //    98: invokestatic    com/facebook/model/GraphObject$Factory.createGraphObjectProxy:(Ljava/lang/Class;Lorg/json/JSONObject;)Lcom/facebook/model/GraphObject;
            //   101: areturn        
            //   102: ldc             Lcom/facebook/model/GraphObject;.class
            //   104: aload           5
            //   106: invokevirtual   java/lang/Class.isAssignableFrom:(Ljava/lang/Class;)Z
            //   109: ifeq            123
            //   112: aload_0        
            //   113: checkcast       Lcom/facebook/model/GraphObject;
            //   116: aload_1        
            //   117: invokeinterface com/facebook/model/GraphObject.cast:(Ljava/lang/Class;)Lcom/facebook/model/GraphObject;
            //   122: areturn        
            //   123: new             Lcom/facebook/FacebookGraphObjectException;
            //   126: dup            
            //   127: new             Ljava/lang/StringBuilder;
            //   130: dup            
            //   131: invokespecial   java/lang/StringBuilder.<init>:()V
            //   134: ldc             "Can't create GraphObject from "
            //   136: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   139: aload           5
            //   141: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
            //   144: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   147: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   150: invokespecial   com/facebook/FacebookGraphObjectException.<init>:(Ljava/lang/String;)V
            //   153: athrow         
            //   154: ldc             Ljava/lang/Iterable;.class
            //   156: aload_1        
            //   157: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
            //   160: ifne            190
            //   163: ldc             Ljava/util/Collection;.class
            //   165: aload_1        
            //   166: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
            //   169: ifne            190
            //   172: ldc             Ljava/util/List;.class
            //   174: aload_1        
            //   175: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
            //   178: ifne            190
            //   181: ldc             Lcom/facebook/model/GraphObjectList;.class
            //   183: aload_1        
            //   184: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
            //   187: ifeq            317
            //   190: aload_2        
            //   191: ifnonnull       224
            //   194: new             Lcom/facebook/FacebookGraphObjectException;
            //   197: dup            
            //   198: new             Ljava/lang/StringBuilder;
            //   201: dup            
            //   202: invokespecial   java/lang/StringBuilder.<init>:()V
            //   205: ldc             "can't infer generic type of: "
            //   207: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   210: aload_1        
            //   211: invokevirtual   java/lang/Class.toString:()Ljava/lang/String;
            //   214: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   217: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   220: invokespecial   com/facebook/FacebookGraphObjectException.<init>:(Ljava/lang/String;)V
            //   223: athrow         
            //   224: aload_2        
            //   225: invokeinterface java/lang/reflect/ParameterizedType.getActualTypeArguments:()[Ljava/lang/reflect/Type;
            //   230: astore_1       
            //   231: aload_1        
            //   232: ifnull          250
            //   235: aload_1        
            //   236: arraylength    
            //   237: iconst_1       
            //   238: if_icmpne       250
            //   241: aload_1        
            //   242: iconst_0       
            //   243: aaload         
            //   244: instanceof      Ljava/lang/Class;
            //   247: ifne            260
            //   250: new             Lcom/facebook/FacebookGraphObjectException;
            //   253: dup            
            //   254: ldc             "Expect collection properties to be of a type with exactly one generic parameter."
            //   256: invokespecial   com/facebook/FacebookGraphObjectException.<init>:(Ljava/lang/String;)V
            //   259: athrow         
            //   260: aload_1        
            //   261: iconst_0       
            //   262: aaload         
            //   263: checkcast       Ljava/lang/Class;
            //   266: astore_1       
            //   267: ldc             Lorg/json/JSONArray;.class
            //   269: aload           5
            //   271: invokevirtual   java/lang/Class.isAssignableFrom:(Ljava/lang/Class;)Z
            //   274: ifeq            286
            //   277: aload_0        
            //   278: checkcast       Lorg/json/JSONArray;
            //   281: aload_1        
            //   282: invokestatic    com/facebook/model/GraphObject$Factory.createList:(Lorg/json/JSONArray;Ljava/lang/Class;)Lcom/facebook/model/GraphObjectList;
            //   285: areturn        
            //   286: new             Lcom/facebook/FacebookGraphObjectException;
            //   289: dup            
            //   290: new             Ljava/lang/StringBuilder;
            //   293: dup            
            //   294: invokespecial   java/lang/StringBuilder.<init>:()V
            //   297: ldc             "Can't create Collection from "
            //   299: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   302: aload           5
            //   304: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
            //   307: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   310: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   313: invokespecial   com/facebook/FacebookGraphObjectException.<init>:(Ljava/lang/String;)V
            //   316: athrow         
            //   317: ldc             Ljava/lang/String;.class
            //   319: aload_1        
            //   320: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
            //   323: ifeq            384
            //   326: ldc             Ljava/lang/Double;.class
            //   328: aload           5
            //   330: invokevirtual   java/lang/Class.isAssignableFrom:(Ljava/lang/Class;)Z
            //   333: ifne            346
            //   336: ldc             Ljava/lang/Float;.class
            //   338: aload           5
            //   340: invokevirtual   java/lang/Class.isAssignableFrom:(Ljava/lang/Class;)Z
            //   343: ifeq            360
            //   346: ldc             "%f"
            //   348: iconst_1       
            //   349: anewarray       Ljava/lang/Object;
            //   352: dup            
            //   353: iconst_0       
            //   354: aload_0        
            //   355: aastore        
            //   356: invokestatic    java/lang/String.format:(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
            //   359: areturn        
            //   360: ldc             Ljava/lang/Number;.class
            //   362: aload           5
            //   364: invokevirtual   java/lang/Class.isAssignableFrom:(Ljava/lang/Class;)Z
            //   367: ifeq            452
            //   370: ldc             "%d"
            //   372: iconst_1       
            //   373: anewarray       Ljava/lang/Object;
            //   376: dup            
            //   377: iconst_0       
            //   378: aload_0        
            //   379: aastore        
            //   380: invokestatic    java/lang/String.format:(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;
            //   383: areturn        
            //   384: ldc             Ljava/util/Date;.class
            //   386: aload_1        
            //   387: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
            //   390: ifeq            452
            //   393: ldc             Ljava/lang/String;.class
            //   395: aload           5
            //   397: invokevirtual   java/lang/Class.isAssignableFrom:(Ljava/lang/Class;)Z
            //   400: ifeq            452
            //   403: getstatic       com/facebook/model/GraphObject$Factory.dateFormats:[Ljava/text/SimpleDateFormat;
            //   406: astore_2       
            //   407: aload_2        
            //   408: arraylength    
            //   409: istore          4
            //   411: iconst_0       
            //   412: istore_3       
            //   413: iload_3        
            //   414: iload           4
            //   416: if_icmpge       452
            //   419: aload_2        
            //   420: iload_3        
            //   421: aaload         
            //   422: astore          6
            //   424: aload           6
            //   426: aload_0        
            //   427: checkcast       Ljava/lang/String;
            //   430: invokevirtual   java/text/SimpleDateFormat.parse:(Ljava/lang/String;)Ljava/util/Date;
            //   433: astore          6
            //   435: aload           6
            //   437: ifnull          445
            //   440: aload           6
            //   442: areturn        
            //   443: astore          6
            //   445: iload_3        
            //   446: iconst_1       
            //   447: iadd           
            //   448: istore_3       
            //   449: goto            413
            //   452: new             Lcom/facebook/FacebookGraphObjectException;
            //   455: dup            
            //   456: new             Ljava/lang/StringBuilder;
            //   459: dup            
            //   460: invokespecial   java/lang/StringBuilder.<init>:()V
            //   463: ldc             "Can't convert type"
            //   465: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   468: aload           5
            //   470: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
            //   473: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   476: ldc             " to "
            //   478: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   481: aload_1        
            //   482: invokevirtual   java/lang/Class.getName:()Ljava/lang/String;
            //   485: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
            //   488: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
            //   491: invokespecial   com/facebook/FacebookGraphObjectException.<init>:(Ljava/lang/String;)V
            //   494: athrow         
            //    Signature:
            //  <U:Ljava/lang/Object;>(Ljava/lang/Object;Ljava/lang/Class<TU;>;Ljava/lang/reflect/ParameterizedType;)TU;
            //    Exceptions:
            //  Try           Handler
            //  Start  End    Start  End    Type                      
            //  -----  -----  -----  -----  --------------------------
            //  424    435    443    445    Ljava/text/ParseException;
            // 
            // The error that occurred was:
            // 
            // java.lang.NullPointerException
            //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
            //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
            //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
            //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
            //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:757)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:655)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:532)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:556)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:499)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:141)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:130)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:105)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:317)
            //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:238)
            //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:123)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        static String convertCamelCaseToLowercaseWithUnderscores(final String s) {
            return s.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase(Locale.US);
        }
        
        public static GraphObject create() {
            return create(GraphObject.class);
        }
        
        public static <T extends GraphObject> T create(final Class<T> clazz) {
            return createGraphObjectProxy(clazz, new JSONObject());
        }
        
        public static GraphObject create(final JSONObject jsonObject) {
            return create(jsonObject, GraphObject.class);
        }
        
        public static <T extends GraphObject> T create(final JSONObject jsonObject, final Class<T> clazz) {
            return createGraphObjectProxy(clazz, jsonObject);
        }
        
        private static <T extends GraphObject> T createGraphObjectProxy(final Class<T> clazz, final JSONObject jsonObject) {
            verifyCanProxyClass(clazz);
            return (T)Proxy.newProxyInstance(GraphObject.class.getClassLoader(), new Class[] { clazz }, new GraphObjectProxy(jsonObject, clazz));
        }
        
        private static Map<String, Object> createGraphObjectProxyForMap(final JSONObject jsonObject) {
            return (Map<String, Object>)Proxy.newProxyInstance(GraphObject.class.getClassLoader(), new Class[] { Map.class }, new GraphObjectProxy(jsonObject, Map.class));
        }
        
        public static <T> GraphObjectList<T> createList(final Class<T> clazz) {
            return createList(new JSONArray(), clazz);
        }
        
        public static <T> GraphObjectList<T> createList(final JSONArray jsonArray, final Class<T> clazz) {
            return new GraphObjectListImpl<T>(jsonArray, clazz);
        }
        
        private static Object getUnderlyingJSONObject(Object next) {
            Object o;
            if (next == null) {
                o = null;
            }
            else {
                final Class<?> class1 = next.getClass();
                if (GraphObject.class.isAssignableFrom(class1)) {
                    return ((GraphObject)next).getInnerJSONObject();
                }
                if (GraphObjectList.class.isAssignableFrom(class1)) {
                    return ((GraphObjectList)next).getInnerJSONArray();
                }
                if (!Iterable.class.isAssignableFrom(class1)) {
                    return next;
                }
                final JSONArray jsonArray = new JSONArray();
                final Iterator iterator = ((Iterable)next).iterator();
                while (true) {
                    o = jsonArray;
                    if (!iterator.hasNext()) {
                        break;
                    }
                    next = iterator.next();
                    if (GraphObject.class.isAssignableFrom(next.getClass())) {
                        jsonArray.put((Object)((GraphObject)next).getInnerJSONObject());
                    }
                    else {
                        jsonArray.put(next);
                    }
                }
            }
            return o;
        }
        
        private static <T extends GraphObject> boolean hasClassBeenVerified(final Class<T> clazz) {
            synchronized (Factory.class) {
                return Factory.verifiedGraphObjectClasses.contains(clazz);
            }
        }
        
        public static boolean hasSameId(final GraphObject graphObject, final GraphObject graphObject2) {
            if (graphObject != null && graphObject2 != null && graphObject.asMap().containsKey("id") && graphObject2.asMap().containsKey("id")) {
                if (graphObject.equals(graphObject2)) {
                    return true;
                }
                final Object property = graphObject.getProperty("id");
                final Object property2 = graphObject2.getProperty("id");
                if (property != null && property2 != null && property instanceof String && property2 instanceof String) {
                    return property.equals(property2);
                }
            }
            return false;
        }
        
        private static <T extends GraphObject> void recordClassHasBeenVerified(final Class<T> clazz) {
            synchronized (Factory.class) {
                Factory.verifiedGraphObjectClasses.add(clazz);
            }
        }
        
        private static <T extends GraphObject> void verifyCanProxyClass(final Class<T> clazz) {
            if (hasClassBeenVerified(clazz)) {
                return;
            }
            if (!clazz.isInterface()) {
                throw new FacebookGraphObjectException("Factory can only wrap interfaces, not class: " + clazz.getName());
            }
            final Method[] methods = clazz.getMethods();
            for (int length = methods.length, i = 0; i < length; ++i) {
                final Method method = methods[i];
                final String name = method.getName();
                final int length2 = method.getParameterTypes().length;
                final Class<?> returnType = method.getReturnType();
                final boolean annotationPresent = method.isAnnotationPresent(PropertyName.class);
                if (!method.getDeclaringClass().isAssignableFrom(GraphObject.class)) {
                    if (length2 == 1 && returnType == Void.TYPE) {
                        if (annotationPresent) {
                            if (!Utility.isNullOrEmpty(method.getAnnotation(PropertyName.class).value())) {
                                continue;
                            }
                        }
                        else if (name.startsWith("set") && name.length() > 3) {
                            continue;
                        }
                    }
                    else if (length2 == 0 && returnType != Void.TYPE) {
                        if (annotationPresent) {
                            if (!Utility.isNullOrEmpty(method.getAnnotation(PropertyName.class).value())) {
                                continue;
                            }
                        }
                        else if (name.startsWith("get") && name.length() > 3) {
                            continue;
                        }
                    }
                    throw new FacebookGraphObjectException("Factory can't proxy method: " + method.toString());
                }
            }
            recordClassHasBeenVerified((Class<GraphObject>)clazz);
        }
        
        private static final class GraphObjectListImpl<T> extends AbstractList<T> implements GraphObjectList<T>
        {
            private final Class<?> itemType;
            private final JSONArray state;
            
            public GraphObjectListImpl(final JSONArray state, final Class<?> itemType) {
                Validate.notNull(state, "state");
                Validate.notNull(itemType, "itemType");
                this.state = state;
                this.itemType = itemType;
            }
            
            private void checkIndex(final int n) {
                if (n < 0 || n >= this.state.length()) {
                    throw new IndexOutOfBoundsException();
                }
            }
            
            private void put(final int n, final T t) {
                final Object access$200 = getUnderlyingJSONObject(t);
                try {
                    this.state.put(n, access$200);
                }
                catch (JSONException ex) {
                    throw new IllegalArgumentException((Throwable)ex);
                }
            }
            
            @Override
            public void add(final int n, final T t) {
                if (n < 0) {
                    throw new IndexOutOfBoundsException();
                }
                if (n < this.size()) {
                    throw new UnsupportedOperationException("Only adding items at the end of the list is supported.");
                }
                this.put(n, t);
            }
            
            @Override
            public final <U extends GraphObject> GraphObjectList<U> castToListOf(final Class<U> clazz) {
                if (!GraphObject.class.isAssignableFrom(this.itemType)) {
                    throw new FacebookGraphObjectException("Can't cast GraphObjectCollection of non-GraphObject type " + this.itemType);
                }
                if (clazz.isAssignableFrom(this.itemType)) {
                    return (GraphObjectList<U>)this;
                }
                return Factory.createList(this.state, clazz);
            }
            
            @Override
            public void clear() {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public boolean equals(final Object o) {
                if (o != null) {
                    if (this == o) {
                        return true;
                    }
                    if (this.getClass() == o.getClass()) {
                        return this.state.equals((Object)((GraphObjectListImpl)o).state);
                    }
                }
                return false;
            }
            
            @Override
            public T get(final int n) {
                this.checkIndex(n);
                return Factory.coerceValueToExpectedType(this.state.opt(n), this.itemType, null);
            }
            
            @Override
            public final JSONArray getInnerJSONArray() {
                return this.state;
            }
            
            @Override
            public int hashCode() {
                return this.state.hashCode();
            }
            
            @Override
            public boolean remove(final Object o) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public boolean removeAll(final Collection<?> collection) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public boolean retainAll(final Collection<?> collection) {
                throw new UnsupportedOperationException();
            }
            
            @Override
            public T set(final int n, final T t) {
                this.checkIndex(n);
                final T value = this.get(n);
                this.put(n, t);
                return value;
            }
            
            @Override
            public int size() {
                return this.state.length();
            }
            
            @Override
            public String toString() {
                return String.format("GraphObjectList{itemType=%s, state=%s}", this.itemType.getSimpleName(), this.state);
            }
        }
        
        private static final class GraphObjectProxy extends ProxyBase<JSONObject>
        {
            private static final String CASTTOMAP_METHOD = "asMap";
            private static final String CAST_METHOD = "cast";
            private static final String CLEAR_METHOD = "clear";
            private static final String CONTAINSKEY_METHOD = "containsKey";
            private static final String CONTAINSVALUE_METHOD = "containsValue";
            private static final String ENTRYSET_METHOD = "entrySet";
            private static final String GETINNERJSONOBJECT_METHOD = "getInnerJSONObject";
            private static final String GETPROPERTYASLIST_METHOD = "getPropertyAsList";
            private static final String GETPROPERTYAS_METHOD = "getPropertyAs";
            private static final String GETPROPERTY_METHOD = "getProperty";
            private static final String GET_METHOD = "get";
            private static final String ISEMPTY_METHOD = "isEmpty";
            private static final String KEYSET_METHOD = "keySet";
            private static final String PUTALL_METHOD = "putAll";
            private static final String PUT_METHOD = "put";
            private static final String REMOVEPROPERTY_METHOD = "removeProperty";
            private static final String REMOVE_METHOD = "remove";
            private static final String SETPROPERTY_METHOD = "setProperty";
            private static final String SIZE_METHOD = "size";
            private static final String VALUES_METHOD = "values";
            private final Class<?> graphObjectClass;
            
            public GraphObjectProxy(final JSONObject jsonObject, final Class<?> graphObjectClass) {
                super(jsonObject);
                this.graphObjectClass = graphObjectClass;
            }
            
            private Object createGraphObjectsFromParameters(final CreateGraphObject createGraphObject, final Object o) {
                Object list = o;
                if (createGraphObject != null) {
                    list = o;
                    if (!Utility.isNullOrEmpty(createGraphObject.value())) {
                        final String value = createGraphObject.value();
                        if (!List.class.isAssignableFrom(o.getClass())) {
                            final GraphObject create = Factory.create();
                            create.setProperty(value, o);
                            return create;
                        }
                        list = Factory.createList(GraphObject.class);
                        for (final Object next : (List)o) {
                            final GraphObject create2 = Factory.create();
                            create2.setProperty(value, next);
                            ((List<GraphObject>)list).add(create2);
                        }
                    }
                }
                return list;
            }
            
            private final Object proxyGraphObjectGettersAndSetters(final Method method, final Object[] array) throws JSONException {
                final String name = method.getName();
                final int length = method.getParameterTypes().length;
                final PropertyName propertyName = method.getAnnotation(PropertyName.class);
                String s;
                if (propertyName != null) {
                    s = propertyName.value();
                }
                else {
                    s = Factory.convertCamelCaseToLowercaseWithUnderscores(name.substring(3));
                }
                if (length == 0) {
                    final Object opt = ((JSONObject)this.state).opt(s);
                    final Class<?> returnType = method.getReturnType();
                    final Type genericReturnType = method.getGenericReturnType();
                    ParameterizedType parameterizedType = null;
                    if (genericReturnType instanceof ParameterizedType) {
                        parameterizedType = (ParameterizedType)genericReturnType;
                    }
                    return Factory.coerceValueToExpectedType(opt, returnType, parameterizedType);
                }
                if (length == 1) {
                    ((JSONObject)this.state).putOpt(s, getUnderlyingJSONObject(this.createGraphObjectsFromParameters(method.getAnnotation(CreateGraphObject.class), array[0])));
                    return null;
                }
                return ((ProxyBase)this).throwUnexpectedMethodSignature(method);
            }
            
            private final Object proxyGraphObjectMethods(final Object o, final Method method, final Object[] jsonProperty) {
                final String name = method.getName();
                if (name.equals("cast")) {
                    final Class clazz = (Class)jsonProperty[0];
                    if (clazz != null && clazz.isAssignableFrom(this.graphObjectClass)) {
                        return o;
                    }
                    return createGraphObjectProxy((Class<GraphObject>)clazz, (JSONObject)this.state);
                }
                else {
                    if (name.equals("getInnerJSONObject")) {
                        return ((GraphObjectProxy)Proxy.getInvocationHandler(o)).state;
                    }
                    if (name.equals("asMap")) {
                        return createGraphObjectProxyForMap((JSONObject)this.state);
                    }
                    if (name.equals("getProperty")) {
                        return ((JSONObject)this.state).opt((String)jsonProperty[0]);
                    }
                    if (name.equals("getPropertyAs")) {
                        return Factory.coerceValueToExpectedType(((JSONObject)this.state).opt((String)jsonProperty[0]), (Class<Object>)jsonProperty[1], null);
                    }
                    if (name.equals("getPropertyAsList")) {
                        return Factory.coerceValueToExpectedType(((JSONObject)this.state).opt((String)jsonProperty[0]), GraphObjectList.class, new ParameterizedType() {
                            final /* synthetic */ Class val$expectedType = (Class)jsonProperty[1];
                            
                            @Override
                            public Type[] getActualTypeArguments() {
                                return new Type[] { this.val$expectedType };
                            }
                            
                            @Override
                            public Type getOwnerType() {
                                return null;
                            }
                            
                            @Override
                            public Type getRawType() {
                                return GraphObjectList.class;
                            }
                        });
                    }
                    if (name.equals("setProperty")) {
                        return this.setJSONProperty(jsonProperty);
                    }
                    if (name.equals("removeProperty")) {
                        ((JSONObject)this.state).remove((String)jsonProperty[0]);
                        return null;
                    }
                    return ((ProxyBase)this).throwUnexpectedMethodSignature(method);
                }
            }
            
            private final Object proxyMapMethods(final Method method, final Object[] jsonProperty) {
                final String name = method.getName();
                if (name.equals("clear")) {
                    JsonUtil.jsonObjectClear((JSONObject)this.state);
                    return null;
                }
                if (name.equals("containsKey")) {
                    return ((JSONObject)this.state).has((String)jsonProperty[0]);
                }
                if (name.equals("containsValue")) {
                    return JsonUtil.jsonObjectContainsValue((JSONObject)this.state, jsonProperty[0]);
                }
                if (name.equals("entrySet")) {
                    return JsonUtil.jsonObjectEntrySet((JSONObject)this.state);
                }
                if (name.equals("get")) {
                    return ((JSONObject)this.state).opt((String)jsonProperty[0]);
                }
                if (name.equals("isEmpty")) {
                    return ((JSONObject)this.state).length() == 0;
                }
                if (name.equals("keySet")) {
                    return JsonUtil.jsonObjectKeySet((JSONObject)this.state);
                }
                if (name.equals("put")) {
                    return this.setJSONProperty(jsonProperty);
                }
                if (name.equals("putAll")) {
                    Map<String, Object> map;
                    if (jsonProperty[0] instanceof Map) {
                        map = (Map<String, Object>)jsonProperty[0];
                    }
                    else {
                        if (!(jsonProperty[0] instanceof GraphObject)) {
                            return null;
                        }
                        map = ((GraphObject)jsonProperty[0]).asMap();
                    }
                    JsonUtil.jsonObjectPutAll((JSONObject)this.state, map);
                    return null;
                }
                if (name.equals("remove")) {
                    ((JSONObject)this.state).remove((String)jsonProperty[0]);
                    return null;
                }
                if (name.equals("size")) {
                    return ((JSONObject)this.state).length();
                }
                if (name.equals("values")) {
                    return JsonUtil.jsonObjectValues((JSONObject)this.state);
                }
                return ((ProxyBase)this).throwUnexpectedMethodSignature(method);
            }
            
            private Object setJSONProperty(final Object[] array) {
                final String s = (String)array[0];
                final Object access$200 = getUnderlyingJSONObject(array[1]);
                try {
                    ((JSONObject)this.state).putOpt(s, access$200);
                    return null;
                }
                catch (JSONException ex) {
                    throw new IllegalArgumentException((Throwable)ex);
                }
            }
            
            @Override
            public final Object invoke(final Object o, final Method method, final Object[] array) throws Throwable {
                final Class<?> declaringClass = method.getDeclaringClass();
                if (declaringClass == Object.class) {
                    return ((ProxyBase)this).proxyObjectMethods(o, method, array);
                }
                if (declaringClass == Map.class) {
                    return this.proxyMapMethods(method, array);
                }
                if (declaringClass == GraphObject.class) {
                    return this.proxyGraphObjectMethods(o, method, array);
                }
                if (GraphObject.class.isAssignableFrom(declaringClass)) {
                    return this.proxyGraphObjectGettersAndSetters(method, array);
                }
                return ((ProxyBase)this).throwUnexpectedMethodSignature(method);
            }
            
            @Override
            public String toString() {
                return String.format("GraphObject{graphObjectClass=%s, state=%s}", this.graphObjectClass.getSimpleName(), this.state);
            }
        }
        
        private abstract static class ProxyBase<STATE> implements InvocationHandler
        {
            private static final String EQUALS_METHOD = "equals";
            private static final String TOSTRING_METHOD = "toString";
            protected final STATE state;
            
            protected ProxyBase(final STATE state) {
                this.state = state;
            }
            
            protected final Object proxyObjectMethods(Object o, final Method method, final Object[] array) throws Throwable {
                final String name = method.getName();
                if (name.equals("equals")) {
                    o = array[0];
                    if (o == null) {
                        return false;
                    }
                    final InvocationHandler invocationHandler = Proxy.getInvocationHandler(o);
                    if (!(invocationHandler instanceof GraphObjectProxy)) {
                        return false;
                    }
                    return this.state.equals(((GraphObjectProxy)invocationHandler).state);
                }
                else {
                    if (name.equals("toString")) {
                        return this.toString();
                    }
                    return method.invoke(this.state, array);
                }
            }
            
            protected final Object throwUnexpectedMethodSignature(final Method method) {
                throw new FacebookGraphObjectException(this.getClass().getName() + " got an unexpected method signature: " + method.toString());
            }
        }
    }
}
