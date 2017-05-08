// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.introspect;

import java.lang.reflect.Type;
import com.fasterxml.jackson.databind.util.Annotations;
import java.lang.reflect.AnnotatedElement;
import java.util.LinkedHashMap;
import java.util.Collection;
import java.util.Map;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import com.fasterxml.jackson.databind.util.ClassUtil;
import java.lang.reflect.Modifier;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.LinkedList;
import java.lang.annotation.Annotation;
import java.util.List;
import com.fasterxml.jackson.databind.AnnotationIntrospector;

public final class AnnotatedClass extends Annotated
{
    private static final AnnotationMap[] NO_ANNOTATION_MAPS;
    protected final AnnotationIntrospector _annotationIntrospector;
    protected final Class<?> _class;
    protected AnnotationMap _classAnnotations;
    protected List<AnnotatedConstructor> _constructors;
    protected List<AnnotatedMethod> _creatorMethods;
    protected boolean _creatorsResolved;
    protected AnnotatedConstructor _defaultConstructor;
    protected List<AnnotatedField> _fields;
    protected AnnotatedMethodMap _memberMethods;
    protected final ClassIntrospector.MixInResolver _mixInResolver;
    protected final Class<?> _primaryMixIn;
    protected final List<Class<?>> _superTypes;
    
    static {
        NO_ANNOTATION_MAPS = new AnnotationMap[0];
    }
    
    private AnnotatedClass(final Class<?> class1, final List<Class<?>> superTypes, final AnnotationIntrospector annotationIntrospector, final ClassIntrospector.MixInResolver mixInResolver, final AnnotationMap classAnnotations) {
        this._creatorsResolved = false;
        this._class = class1;
        this._superTypes = superTypes;
        this._annotationIntrospector = annotationIntrospector;
        this._mixInResolver = mixInResolver;
        Class<?> mixInClass;
        if (this._mixInResolver == null) {
            mixInClass = null;
        }
        else {
            mixInClass = this._mixInResolver.findMixInClassFor(this._class);
        }
        this._primaryMixIn = mixInClass;
        this._classAnnotations = classAnnotations;
    }
    
    private void _addAnnotationsIfNotPresent(final AnnotatedMember annotatedMember, final Annotation[] array) {
        if (array != null) {
            final int length = array.length;
            List<Annotation[]> list = null;
            for (int i = 0; i < length; ++i) {
                final Annotation annotation = array[i];
                if (this._isAnnotationBundle(annotation)) {
                    List<Annotation[]> list2;
                    if ((list2 = list) == null) {
                        list2 = new LinkedList<Annotation[]>();
                    }
                    list2.add(annotation.annotationType().getDeclaredAnnotations());
                    list = list2;
                }
                else {
                    annotatedMember.addIfNotPresent(annotation);
                }
            }
            if (list != null) {
                final Iterator<Annotation[]> iterator = list.iterator();
                while (iterator.hasNext()) {
                    this._addAnnotationsIfNotPresent(annotatedMember, iterator.next());
                }
            }
        }
    }
    
    private void _addAnnotationsIfNotPresent(final AnnotationMap annotationMap, final Annotation[] array) {
        if (array != null) {
            final int length = array.length;
            List<Annotation[]> list = null;
            for (int i = 0; i < length; ++i) {
                final Annotation annotation = array[i];
                if (this._isAnnotationBundle(annotation)) {
                    List<Annotation[]> list2;
                    if ((list2 = list) == null) {
                        list2 = new LinkedList<Annotation[]>();
                    }
                    list2.add(annotation.annotationType().getDeclaredAnnotations());
                    list = list2;
                }
                else {
                    annotationMap.addIfNotPresent(annotation);
                }
            }
            if (list != null) {
                final Iterator<Annotation[]> iterator = list.iterator();
                while (iterator.hasNext()) {
                    this._addAnnotationsIfNotPresent(annotationMap, iterator.next());
                }
            }
        }
    }
    
    private void _addOrOverrideAnnotations(final AnnotatedMember annotatedMember, final Annotation[] array) {
        if (array != null) {
            final int length = array.length;
            List<Annotation[]> list = null;
            for (int i = 0; i < length; ++i) {
                final Annotation annotation = array[i];
                if (this._isAnnotationBundle(annotation)) {
                    List<Annotation[]> list2;
                    if ((list2 = list) == null) {
                        list2 = new LinkedList<Annotation[]>();
                    }
                    list2.add(annotation.annotationType().getDeclaredAnnotations());
                    list = list2;
                }
                else {
                    annotatedMember.addOrOverride(annotation);
                }
            }
            if (list != null) {
                final Iterator<Annotation[]> iterator = list.iterator();
                while (iterator.hasNext()) {
                    this._addOrOverrideAnnotations(annotatedMember, iterator.next());
                }
            }
        }
    }
    
    private AnnotationMap _emptyAnnotationMap() {
        return new AnnotationMap();
    }
    
    private AnnotationMap[] _emptyAnnotationMaps(final int n) {
        AnnotationMap[] no_ANNOTATION_MAPS;
        if (n == 0) {
            no_ANNOTATION_MAPS = AnnotatedClass.NO_ANNOTATION_MAPS;
        }
        else {
            final AnnotationMap[] array = new AnnotationMap[n];
            int n2 = 0;
            while (true) {
                no_ANNOTATION_MAPS = array;
                if (n2 >= n) {
                    break;
                }
                array[n2] = this._emptyAnnotationMap();
                ++n2;
            }
        }
        return no_ANNOTATION_MAPS;
    }
    
    private final boolean _isAnnotationBundle(final Annotation annotation) {
        return this._annotationIntrospector != null && this._annotationIntrospector.isAnnotationBundle(annotation);
    }
    
    private boolean _isIncludableField(final Field field) {
        if (!field.isSynthetic()) {
            final int modifiers = field.getModifiers();
            if (!Modifier.isStatic(modifiers) && !Modifier.isTransient(modifiers)) {
                return true;
            }
        }
        return false;
    }
    
    public static AnnotatedClass construct(final Class<?> clazz, final AnnotationIntrospector annotationIntrospector, final ClassIntrospector.MixInResolver mixInResolver) {
        return new AnnotatedClass(clazz, ClassUtil.findSuperTypes(clazz, null), annotationIntrospector, mixInResolver, null);
    }
    
    public static AnnotatedClass constructWithoutSuperTypes(final Class<?> clazz, final AnnotationIntrospector annotationIntrospector, final ClassIntrospector.MixInResolver mixInResolver) {
        return new AnnotatedClass(clazz, Collections.emptyList(), annotationIntrospector, mixInResolver, null);
    }
    
    private void resolveClassAnnotations() {
        this._classAnnotations = new AnnotationMap();
        if (this._annotationIntrospector != null) {
            if (this._primaryMixIn != null) {
                this._addClassMixIns(this._classAnnotations, this._class, this._primaryMixIn);
            }
            this._addAnnotationsIfNotPresent(this._classAnnotations, this._class.getDeclaredAnnotations());
            for (final Class<?> clazz : this._superTypes) {
                this._addClassMixIns(this._classAnnotations, clazz);
                this._addAnnotationsIfNotPresent(this._classAnnotations, clazz.getDeclaredAnnotations());
            }
            this._addClassMixIns(this._classAnnotations, Object.class);
        }
    }
    
    private void resolveCreators() {
        final int n = 0;
        final Constructor<?>[] declaredConstructors = this._class.getDeclaredConstructors();
        final int length = declaredConstructors.length;
        int i = 0;
        List<AnnotatedConstructor> constructors = null;
        while (i < length) {
            final Constructor<?> constructor = declaredConstructors[i];
            if (constructor.getParameterTypes().length == 0) {
                this._defaultConstructor = this._constructConstructor(constructor, true);
            }
            else {
                List<AnnotatedConstructor> list;
                if ((list = constructors) == null) {
                    list = new ArrayList<AnnotatedConstructor>(Math.max(10, declaredConstructors.length));
                }
                list.add(this._constructConstructor(constructor, false));
                constructors = list;
            }
            ++i;
        }
        if (constructors == null) {
            this._constructors = Collections.emptyList();
        }
        else {
            this._constructors = constructors;
        }
        if (this._primaryMixIn != null && (this._defaultConstructor != null || !this._constructors.isEmpty())) {
            this._addConstructorMixIns(this._primaryMixIn);
        }
        if (this._annotationIntrospector != null) {
            if (this._defaultConstructor != null && this._annotationIntrospector.hasIgnoreMarker(this._defaultConstructor)) {
                this._defaultConstructor = null;
            }
            if (this._constructors != null) {
                int size = this._constructors.size();
                while (true) {
                    --size;
                    if (size < 0) {
                        break;
                    }
                    if (!this._annotationIntrospector.hasIgnoreMarker(this._constructors.get(size))) {
                        continue;
                    }
                    this._constructors.remove(size);
                }
            }
        }
        final Method[] declaredMethods = this._class.getDeclaredMethods();
        final int length2 = declaredMethods.length;
        List<AnnotatedMethod> creatorMethods = null;
        for (int j = n; j < length2; ++j) {
            final Method method = declaredMethods[j];
            if (Modifier.isStatic(method.getModifiers())) {
                List<AnnotatedMethod> list2;
                if ((list2 = creatorMethods) == null) {
                    list2 = new ArrayList<AnnotatedMethod>(8);
                }
                list2.add(this._constructCreatorMethod(method));
                creatorMethods = list2;
            }
        }
        if (creatorMethods == null) {
            this._creatorMethods = Collections.emptyList();
        }
        else {
            this._creatorMethods = creatorMethods;
            if (this._primaryMixIn != null) {
                this._addFactoryMixIns(this._primaryMixIn);
            }
            if (this._annotationIntrospector != null) {
                int size2 = this._creatorMethods.size();
                while (true) {
                    --size2;
                    if (size2 < 0) {
                        break;
                    }
                    if (!this._annotationIntrospector.hasIgnoreMarker(this._creatorMethods.get(size2))) {
                        continue;
                    }
                    this._creatorMethods.remove(size2);
                }
            }
        }
        this._creatorsResolved = true;
    }
    
    private void resolveFields() {
        final Map<String, AnnotatedField> findFields = this._findFields(this._class, null);
        if (findFields == null || findFields.size() == 0) {
            this._fields = Collections.emptyList();
            return;
        }
        (this._fields = new ArrayList<AnnotatedField>(findFields.size())).addAll(findFields.values());
    }
    
    private void resolveMemberMethods() {
        this._memberMethods = new AnnotatedMethodMap();
        final AnnotatedMethodMap annotatedMethodMap = new AnnotatedMethodMap();
        this._addMemberMethods(this._class, this._memberMethods, this._primaryMixIn, annotatedMethodMap);
        for (final Class<?> clazz : this._superTypes) {
            Class<?> mixInClass;
            if (this._mixInResolver == null) {
                mixInClass = null;
            }
            else {
                mixInClass = this._mixInResolver.findMixInClassFor(clazz);
            }
            this._addMemberMethods(clazz, this._memberMethods, mixInClass, annotatedMethodMap);
        }
        if (this._mixInResolver != null) {
            final Class<?> mixInClass2 = this._mixInResolver.findMixInClassFor(Object.class);
            if (mixInClass2 != null) {
                this._addMethodMixIns(this._class, this._memberMethods, mixInClass2, annotatedMethodMap);
            }
        }
        if (this._annotationIntrospector != null && !annotatedMethodMap.isEmpty()) {
            for (final AnnotatedMethod annotatedMethod : annotatedMethodMap) {
                try {
                    final Method declaredMethod = Object.class.getDeclaredMethod(annotatedMethod.getName(), annotatedMethod.getRawParameterTypes());
                    if (declaredMethod == null) {
                        continue;
                    }
                    final AnnotatedMethod constructMethod = this._constructMethod(declaredMethod);
                    this._addMixOvers(annotatedMethod.getAnnotated(), constructMethod, false);
                    this._memberMethods.add(constructMethod);
                }
                catch (Exception ex) {}
            }
        }
    }
    
    protected void _addClassMixIns(final AnnotationMap annotationMap, final Class<?> clazz) {
        if (this._mixInResolver != null) {
            this._addClassMixIns(annotationMap, clazz, this._mixInResolver.findMixInClassFor(clazz));
        }
    }
    
    protected void _addClassMixIns(final AnnotationMap annotationMap, final Class<?> clazz, final Class<?> clazz2) {
        if (clazz2 != null) {
            this._addAnnotationsIfNotPresent(annotationMap, clazz2.getDeclaredAnnotations());
            final Iterator<Class<?>> iterator = ClassUtil.findSuperTypes(clazz2, clazz).iterator();
            while (iterator.hasNext()) {
                this._addAnnotationsIfNotPresent(annotationMap, iterator.next().getDeclaredAnnotations());
            }
        }
    }
    
    protected void _addConstructorMixIns(final Class<?> clazz) {
        int size;
        if (this._constructors == null) {
            size = 0;
        }
        else {
            size = this._constructors.size();
        }
        final Constructor[] declaredConstructors = clazz.getDeclaredConstructors();
        final int length = declaredConstructors.length;
        int i = 0;
        MemberKey[] array = null;
        while (i < length) {
            final Constructor constructor = declaredConstructors[i];
            MemberKey[] array2 = null;
            Label_0069: {
                if (constructor.getParameterTypes().length == 0) {
                    array2 = array;
                    if (this._defaultConstructor != null) {
                        this._addMixOvers(constructor, this._defaultConstructor, false);
                        array2 = array;
                    }
                }
                else {
                    if (array == null) {
                        final MemberKey[] array3 = new MemberKey[size];
                        int n = 0;
                        while (true) {
                            array = array3;
                            if (n >= size) {
                                break;
                            }
                            array3[n] = new MemberKey(this._constructors.get(n).getAnnotated());
                            ++n;
                        }
                    }
                    final MemberKey memberKey = new MemberKey(constructor);
                    for (int j = 0; j < size; ++j) {
                        if (memberKey.equals(array[j])) {
                            this._addMixOvers(constructor, this._constructors.get(j), true);
                            array2 = array;
                            break Label_0069;
                        }
                    }
                    array2 = array;
                }
            }
            ++i;
            array = array2;
        }
    }
    
    protected void _addFactoryMixIns(final Class<?> clazz) {
        final MemberKey[] array = null;
        final int size = this._creatorMethods.size();
        final Method[] declaredMethods = clazz.getDeclaredMethods();
        final int length = declaredMethods.length;
        int i = 0;
        MemberKey[] array2 = array;
        while (i < length) {
            final Method method = declaredMethods[i];
            MemberKey[] array3 = null;
            Label_0056: {
                if (!Modifier.isStatic(method.getModifiers())) {
                    array3 = array2;
                }
                else {
                    array3 = array2;
                    if (method.getParameterTypes().length != 0) {
                        if (array2 == null) {
                            final MemberKey[] array4 = new MemberKey[size];
                            int n = 0;
                            while (true) {
                                array2 = array4;
                                if (n >= size) {
                                    break;
                                }
                                array4[n] = new MemberKey(this._creatorMethods.get(n).getAnnotated());
                                ++n;
                            }
                        }
                        final MemberKey memberKey = new MemberKey(method);
                        for (int j = 0; j < size; ++j) {
                            if (memberKey.equals(array2[j])) {
                                this._addMixOvers(method, this._creatorMethods.get(j), true);
                                array3 = array2;
                                break Label_0056;
                            }
                        }
                        array3 = array2;
                    }
                }
            }
            ++i;
            array2 = array3;
        }
    }
    
    protected void _addFieldMixIns(final Class<?> clazz, final Class<?> clazz2, final Map<String, AnnotatedField> map) {
        final ArrayList<Class> list = new ArrayList<Class>();
        list.add(clazz2);
        ClassUtil.findSuperTypes(clazz2, clazz, (List<Class<?>>)list);
        final Iterator<Object> iterator = list.iterator();
        while (iterator.hasNext()) {
            final Field[] declaredFields = iterator.next().getDeclaredFields();
            for (int length = declaredFields.length, i = 0; i < length; ++i) {
                final Field field = declaredFields[i];
                if (this._isIncludableField(field)) {
                    final AnnotatedField annotatedField = map.get(field.getName());
                    if (annotatedField != null) {
                        this._addOrOverrideAnnotations(annotatedField, field.getDeclaredAnnotations());
                    }
                }
            }
        }
    }
    
    protected void _addMemberMethods(final Class<?> clazz, final AnnotatedMethodMap annotatedMethodMap, final Class<?> clazz2, final AnnotatedMethodMap annotatedMethodMap2) {
        if (clazz2 != null) {
            this._addMethodMixIns(clazz, annotatedMethodMap, clazz2, annotatedMethodMap2);
        }
        if (clazz != null) {
            final Method[] declaredMethods = clazz.getDeclaredMethods();
            for (int length = declaredMethods.length, i = 0; i < length; ++i) {
                final Method method = declaredMethods[i];
                if (this._isIncludableMemberMethod(method)) {
                    final AnnotatedMethod find = annotatedMethodMap.find(method);
                    if (find == null) {
                        final AnnotatedMethod constructMethod = this._constructMethod(method);
                        annotatedMethodMap.add(constructMethod);
                        final AnnotatedMethod remove = annotatedMethodMap2.remove(method);
                        if (remove != null) {
                            this._addMixOvers(remove.getAnnotated(), constructMethod, false);
                        }
                    }
                    else {
                        this._addMixUnders(method, find);
                        if (find.getDeclaringClass().isInterface() && !method.getDeclaringClass().isInterface()) {
                            annotatedMethodMap.add(find.withMethod(method));
                        }
                    }
                }
            }
        }
    }
    
    protected void _addMethodMixIns(final Class<?> clazz, final AnnotatedMethodMap annotatedMethodMap, final Class<?> clazz2, final AnnotatedMethodMap annotatedMethodMap2) {
        final ArrayList<Class> list = new ArrayList<Class>();
        list.add(clazz2);
        ClassUtil.findSuperTypes(clazz2, clazz, (List<Class<?>>)list);
        final Iterator<Object> iterator = list.iterator();
        while (iterator.hasNext()) {
            final Method[] declaredMethods = iterator.next().getDeclaredMethods();
            for (int length = declaredMethods.length, i = 0; i < length; ++i) {
                final Method method = declaredMethods[i];
                if (this._isIncludableMemberMethod(method)) {
                    final AnnotatedMethod find = annotatedMethodMap.find(method);
                    if (find != null) {
                        this._addMixUnders(method, find);
                    }
                    else {
                        annotatedMethodMap2.add(this._constructMethod(method));
                    }
                }
            }
        }
    }
    
    protected void _addMixOvers(final Constructor<?> constructor, final AnnotatedConstructor annotatedConstructor, final boolean b) {
        this._addOrOverrideAnnotations(annotatedConstructor, constructor.getDeclaredAnnotations());
        if (b) {
            final Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
            for (int length = parameterAnnotations.length, i = 0; i < length; ++i) {
                final Annotation[] array = parameterAnnotations[i];
                for (int length2 = array.length, j = 0; j < length2; ++j) {
                    annotatedConstructor.addOrOverrideParam(i, array[j]);
                }
            }
        }
    }
    
    protected void _addMixOvers(final Method method, final AnnotatedMethod annotatedMethod, final boolean b) {
        this._addOrOverrideAnnotations(annotatedMethod, method.getDeclaredAnnotations());
        if (b) {
            final Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            for (int length = parameterAnnotations.length, i = 0; i < length; ++i) {
                final Annotation[] array = parameterAnnotations[i];
                for (int length2 = array.length, j = 0; j < length2; ++j) {
                    annotatedMethod.addOrOverrideParam(i, array[j]);
                }
            }
        }
    }
    
    protected void _addMixUnders(final Method method, final AnnotatedMethod annotatedMethod) {
        this._addAnnotationsIfNotPresent(annotatedMethod, method.getDeclaredAnnotations());
    }
    
    protected AnnotationMap _collectRelevantAnnotations(final Annotation[] array) {
        final AnnotationMap annotationMap = new AnnotationMap();
        this._addAnnotationsIfNotPresent(annotationMap, array);
        return annotationMap;
    }
    
    protected AnnotationMap[] _collectRelevantAnnotations(final Annotation[][] array) {
        final int length = array.length;
        final AnnotationMap[] array2 = new AnnotationMap[length];
        for (int i = 0; i < length; ++i) {
            array2[i] = this._collectRelevantAnnotations(array[i]);
        }
        return array2;
    }
    
    protected AnnotatedConstructor _constructConstructor(final Constructor<?> constructor, final boolean b) {
        if (this._annotationIntrospector == null) {
            return new AnnotatedConstructor(constructor, this._emptyAnnotationMap(), this._emptyAnnotationMaps(constructor.getParameterTypes().length));
        }
        if (b) {
            return new AnnotatedConstructor(constructor, this._collectRelevantAnnotations(constructor.getDeclaredAnnotations()), null);
        }
        Annotation[][] parameterAnnotations = constructor.getParameterAnnotations();
        final int length = constructor.getParameterTypes().length;
        AnnotationMap[] collectRelevantAnnotations3;
        if (length != parameterAnnotations.length) {
            final Class<?> declaringClass = constructor.getDeclaringClass();
            AnnotationMap[] collectRelevantAnnotations2;
            if (declaringClass.isEnum() && length == parameterAnnotations.length + 2) {
                final Annotation[][] array = new Annotation[parameterAnnotations.length + 2][];
                System.arraycopy(parameterAnnotations, 0, array, 2, parameterAnnotations.length);
                final AnnotationMap[] collectRelevantAnnotations = this._collectRelevantAnnotations(array);
                parameterAnnotations = array;
                collectRelevantAnnotations2 = collectRelevantAnnotations;
            }
            else if (declaringClass.isMemberClass() && length == parameterAnnotations.length + 1) {
                final Annotation[][] array2 = new Annotation[parameterAnnotations.length + 1][];
                System.arraycopy(parameterAnnotations, 0, array2, 1, parameterAnnotations.length);
                collectRelevantAnnotations2 = this._collectRelevantAnnotations(array2);
                parameterAnnotations = array2;
            }
            else {
                collectRelevantAnnotations2 = null;
            }
            collectRelevantAnnotations3 = collectRelevantAnnotations2;
            if (collectRelevantAnnotations2 == null) {
                throw new IllegalStateException("Internal error: constructor for " + constructor.getDeclaringClass().getName() + " has mismatch: " + length + " parameters; " + parameterAnnotations.length + " sets of annotations");
            }
        }
        else {
            collectRelevantAnnotations3 = this._collectRelevantAnnotations(parameterAnnotations);
        }
        return new AnnotatedConstructor(constructor, this._collectRelevantAnnotations(constructor.getDeclaredAnnotations()), collectRelevantAnnotations3);
    }
    
    protected AnnotatedMethod _constructCreatorMethod(final Method method) {
        if (this._annotationIntrospector == null) {
            return new AnnotatedMethod(method, this._emptyAnnotationMap(), this._emptyAnnotationMaps(method.getParameterTypes().length));
        }
        return new AnnotatedMethod(method, this._collectRelevantAnnotations(method.getDeclaredAnnotations()), this._collectRelevantAnnotations(method.getParameterAnnotations()));
    }
    
    protected AnnotatedField _constructField(final Field field) {
        if (this._annotationIntrospector == null) {
            return new AnnotatedField(field, this._emptyAnnotationMap());
        }
        return new AnnotatedField(field, this._collectRelevantAnnotations(field.getDeclaredAnnotations()));
    }
    
    protected AnnotatedMethod _constructMethod(final Method method) {
        if (this._annotationIntrospector == null) {
            return new AnnotatedMethod(method, this._emptyAnnotationMap(), null);
        }
        return new AnnotatedMethod(method, this._collectRelevantAnnotations(method.getDeclaredAnnotations()), null);
    }
    
    protected Map<String, AnnotatedField> _findFields(final Class<?> clazz, final Map<String, AnnotatedField> map) {
        final Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            Map<String, AnnotatedField> findFields = this._findFields(superclass, map);
            final Field[] declaredFields = clazz.getDeclaredFields();
            for (int length = declaredFields.length, i = 0; i < length; ++i) {
                final Field field = declaredFields[i];
                if (this._isIncludableField(field)) {
                    Map<String, AnnotatedField> map2;
                    if ((map2 = findFields) == null) {
                        map2 = new LinkedHashMap<String, AnnotatedField>();
                    }
                    map2.put(field.getName(), this._constructField(field));
                    findFields = map2;
                }
            }
            if (this._mixInResolver != null) {
                final Class<?> mixInClass = this._mixInResolver.findMixInClassFor(clazz);
                if (mixInClass != null) {
                    this._addFieldMixIns(superclass, mixInClass, findFields);
                }
            }
            return findFields;
        }
        return map;
    }
    
    protected boolean _isIncludableMemberMethod(final Method method) {
        return !Modifier.isStatic(method.getModifiers()) && !method.isSynthetic() && !method.isBridge() && method.getParameterTypes().length <= 2;
    }
    
    public Iterable<AnnotatedField> fields() {
        if (this._fields == null) {
            this.resolveFields();
        }
        return this._fields;
    }
    
    public AnnotatedMethod findMethod(final String s, final Class<?>[] array) {
        if (this._memberMethods == null) {
            this.resolveMemberMethods();
        }
        return this._memberMethods.find(s, array);
    }
    
    @Override
    protected AnnotationMap getAllAnnotations() {
        if (this._classAnnotations == null) {
            this.resolveClassAnnotations();
        }
        return this._classAnnotations;
    }
    
    @Override
    public Class<?> getAnnotated() {
        return this._class;
    }
    
    @Override
    public <A extends Annotation> A getAnnotation(final Class<A> clazz) {
        if (this._classAnnotations == null) {
            this.resolveClassAnnotations();
        }
        return this._classAnnotations.get(clazz);
    }
    
    public Annotations getAnnotations() {
        if (this._classAnnotations == null) {
            this.resolveClassAnnotations();
        }
        return this._classAnnotations;
    }
    
    public List<AnnotatedConstructor> getConstructors() {
        if (!this._creatorsResolved) {
            this.resolveCreators();
        }
        return this._constructors;
    }
    
    public AnnotatedConstructor getDefaultConstructor() {
        if (!this._creatorsResolved) {
            this.resolveCreators();
        }
        return this._defaultConstructor;
    }
    
    @Override
    public Type getGenericType() {
        return this._class;
    }
    
    @Override
    public String getName() {
        return this._class.getName();
    }
    
    @Override
    public Class<?> getRawType() {
        return this._class;
    }
    
    public List<AnnotatedMethod> getStaticMethods() {
        if (!this._creatorsResolved) {
            this.resolveCreators();
        }
        return this._creatorMethods;
    }
    
    public boolean hasAnnotations() {
        if (this._classAnnotations == null) {
            this.resolveClassAnnotations();
        }
        return this._classAnnotations.size() > 0;
    }
    
    public Iterable<AnnotatedMethod> memberMethods() {
        if (this._memberMethods == null) {
            this.resolveMemberMethods();
        }
        return this._memberMethods;
    }
    
    @Override
    public String toString() {
        return "[AnnotedClass " + this._class.getName() + "]";
    }
}
