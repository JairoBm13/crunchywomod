// 
// Decompiled by Procyon v0.5.30
// 

package com.fasterxml.jackson.databind.introspect;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.List;
import java.util.AbstractSequentialList;
import java.util.Map;
import com.fasterxml.jackson.databind.util.BeanUtil;
import java.lang.reflect.Modifier;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.PropertyName;
import java.util.Iterator;
import com.fasterxml.jackson.databind.cfg.HandlerInstantiator;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.JavaType;
import java.util.LinkedHashMap;
import java.util.HashSet;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import java.util.LinkedList;
import com.fasterxml.jackson.databind.AnnotationIntrospector;

public class POJOPropertiesCollector
{
    protected final AnnotationIntrospector _annotationIntrospector;
    protected LinkedList<AnnotatedMember> _anyGetters;
    protected LinkedList<AnnotatedMethod> _anySetters;
    protected final AnnotatedClass _classDef;
    protected final MapperConfig<?> _config;
    protected LinkedList<POJOPropertyBuilder> _creatorProperties;
    protected final boolean _forSerialization;
    protected HashSet<String> _ignoredPropertyNames;
    protected LinkedHashMap<Object, AnnotatedMember> _injectables;
    protected LinkedList<AnnotatedMethod> _jsonValueGetters;
    protected final String _mutatorPrefix;
    protected final LinkedHashMap<String, POJOPropertyBuilder> _properties;
    protected final JavaType _type;
    protected final VisibilityChecker<?> _visibilityChecker;
    
    protected POJOPropertiesCollector(final MapperConfig<?> config, final boolean forSerialization, final JavaType type, final AnnotatedClass classDef, final String s) {
        final AnnotationIntrospector annotationIntrospector = null;
        this._properties = new LinkedHashMap<String, POJOPropertyBuilder>();
        this._creatorProperties = null;
        this._anyGetters = null;
        this._anySetters = null;
        this._jsonValueGetters = null;
        this._config = config;
        this._forSerialization = forSerialization;
        this._type = type;
        this._classDef = classDef;
        String mutatorPrefix = s;
        if (s == null) {
            mutatorPrefix = "set";
        }
        this._mutatorPrefix = mutatorPrefix;
        AnnotationIntrospector annotationIntrospector2 = annotationIntrospector;
        if (config.isAnnotationProcessingEnabled()) {
            annotationIntrospector2 = this._config.getAnnotationIntrospector();
        }
        this._annotationIntrospector = annotationIntrospector2;
        if (this._annotationIntrospector == null) {
            this._visibilityChecker = this._config.getDefaultVisibilityChecker();
            return;
        }
        this._visibilityChecker = this._annotationIntrospector.findAutoDetectVisibility(classDef, this._config.getDefaultVisibilityChecker());
    }
    
    private void _addIgnored(final String s) {
        if (!this._forSerialization) {
            if (this._ignoredPropertyNames == null) {
                this._ignoredPropertyNames = new HashSet<String>();
            }
            this._ignoredPropertyNames.add(s);
        }
    }
    
    private PropertyNamingStrategy _findNamingStrategy() {
        Object namingStrategy;
        if (this._annotationIntrospector == null) {
            namingStrategy = null;
        }
        else {
            namingStrategy = this._annotationIntrospector.findNamingStrategy(this._classDef);
        }
        if (namingStrategy == null) {
            return this._config.getPropertyNamingStrategy();
        }
        if (namingStrategy instanceof PropertyNamingStrategy) {
            return (PropertyNamingStrategy)namingStrategy;
        }
        if (!(namingStrategy instanceof Class)) {
            throw new IllegalStateException("AnnotationIntrospector returned PropertyNamingStrategy definition of type " + ((Class<?>)namingStrategy).getClass().getName() + "; expected type PropertyNamingStrategy or Class<PropertyNamingStrategy> instead");
        }
        final Class<?> clazz = (Class<?>)namingStrategy;
        if (!PropertyNamingStrategy.class.isAssignableFrom(clazz)) {
            throw new IllegalStateException("AnnotationIntrospector returned Class " + clazz.getName() + "; expected Class<PropertyNamingStrategy>");
        }
        final HandlerInstantiator handlerInstantiator = this._config.getHandlerInstantiator();
        if (handlerInstantiator != null) {
            final PropertyNamingStrategy namingStrategyInstance = handlerInstantiator.namingStrategyInstance(this._config, this._classDef, clazz);
            if (namingStrategyInstance != null) {
                return namingStrategyInstance;
            }
        }
        return ClassUtil.createInstance(clazz, this._config.canOverrideAccessModifiers());
    }
    
    protected void _addCreators() {
        final AnnotationIntrospector annotationIntrospector = this._annotationIntrospector;
        if (annotationIntrospector != null) {
            for (final AnnotatedConstructor annotatedConstructor : this._classDef.getConstructors()) {
                if (this._creatorProperties == null) {
                    this._creatorProperties = new LinkedList<POJOPropertyBuilder>();
                }
                for (int parameterCount = annotatedConstructor.getParameterCount(), i = 0; i < parameterCount; ++i) {
                    final AnnotatedParameter parameter = annotatedConstructor.getParameter(i);
                    final PropertyName nameForDeserialization = annotationIntrospector.findNameForDeserialization(parameter);
                    String simpleName;
                    if (nameForDeserialization == null) {
                        simpleName = null;
                    }
                    else {
                        simpleName = nameForDeserialization.getSimpleName();
                    }
                    if (simpleName != null) {
                        final POJOPropertyBuilder property = this._property(simpleName);
                        property.addCtor(parameter, simpleName, true, false);
                        this._creatorProperties.add(property);
                    }
                }
            }
            for (final AnnotatedMethod annotatedMethod : this._classDef.getStaticMethods()) {
                if (this._creatorProperties == null) {
                    this._creatorProperties = new LinkedList<POJOPropertyBuilder>();
                }
                for (int parameterCount2 = annotatedMethod.getParameterCount(), j = 0; j < parameterCount2; ++j) {
                    final AnnotatedParameter parameter2 = annotatedMethod.getParameter(j);
                    final PropertyName nameForDeserialization2 = annotationIntrospector.findNameForDeserialization(parameter2);
                    String simpleName2;
                    if (nameForDeserialization2 == null) {
                        simpleName2 = null;
                    }
                    else {
                        simpleName2 = nameForDeserialization2.getSimpleName();
                    }
                    if (simpleName2 != null) {
                        final POJOPropertyBuilder property2 = this._property(simpleName2);
                        property2.addCtor(parameter2, simpleName2, true, false);
                        this._creatorProperties.add(property2);
                    }
                }
            }
        }
    }
    
    protected void _addFields() {
        final AnnotationIntrospector annotationIntrospector = this._annotationIntrospector;
        boolean b;
        if (!this._forSerialization && !this._config.isEnabled(MapperFeature.ALLOW_FINAL_FIELDS_AS_MUTATORS)) {
            b = true;
        }
        else {
            b = false;
        }
        for (final AnnotatedField annotatedField : this._classDef.fields()) {
            final String name = annotatedField.getName();
            String s;
            if (annotationIntrospector == null) {
                s = null;
            }
            else if (this._forSerialization) {
                final PropertyName nameForSerialization = annotationIntrospector.findNameForSerialization(annotatedField);
                if (nameForSerialization == null) {
                    s = null;
                }
                else {
                    s = nameForSerialization.getSimpleName();
                }
            }
            else {
                final PropertyName nameForDeserialization = annotationIntrospector.findNameForDeserialization(annotatedField);
                if (nameForDeserialization == null) {
                    s = null;
                }
                else {
                    s = nameForDeserialization.getSimpleName();
                }
            }
            if ("".equals(s)) {
                s = name;
            }
            boolean fieldVisible = s != null;
            if (!fieldVisible) {
                fieldVisible = this._visibilityChecker.isFieldVisible(annotatedField);
            }
            final boolean b2 = annotationIntrospector != null && annotationIntrospector.hasIgnoreMarker(annotatedField);
            if (!b || s != null || b2 || !Modifier.isFinal(annotatedField.getModifiers())) {
                this._property(name).addField(annotatedField, s, fieldVisible, b2);
            }
        }
    }
    
    protected void _addGetterMethod(final AnnotatedMethod annotatedMethod, final AnnotationIntrospector annotationIntrospector) {
        final String s = null;
        Label_0079: {
            if (annotationIntrospector == null) {
                break Label_0079;
            }
            if (annotationIntrospector.hasAnyGetterAnnotation(annotatedMethod)) {
                if (this._anyGetters == null) {
                    this._anyGetters = new LinkedList<AnnotatedMember>();
                }
                this._anyGetters.add(annotatedMethod);
            }
            else {
                if (annotationIntrospector.hasAsValueAnnotation(annotatedMethod)) {
                    if (this._jsonValueGetters == null) {
                        this._jsonValueGetters = new LinkedList<AnnotatedMethod>();
                    }
                    this._jsonValueGetters.add(annotatedMethod);
                    return;
                }
                break Label_0079;
            }
            return;
        }
        PropertyName nameForSerialization;
        if (annotationIntrospector == null) {
            nameForSerialization = null;
        }
        else {
            nameForSerialization = annotationIntrospector.findNameForSerialization(annotatedMethod);
        }
        String simpleName;
        if (nameForSerialization == null) {
            simpleName = s;
        }
        else {
            simpleName = nameForSerialization.getSimpleName();
        }
        String s2;
        boolean b;
        if (simpleName == null) {
            s2 = BeanUtil.okNameForRegularGetter(annotatedMethod, annotatedMethod.getName());
            if (s2 == null) {
                s2 = BeanUtil.okNameForIsGetter(annotatedMethod, annotatedMethod.getName());
                if (s2 == null) {
                    return;
                }
                b = this._visibilityChecker.isIsGetterVisible(annotatedMethod);
            }
            else {
                b = this._visibilityChecker.isGetterVisible(annotatedMethod);
            }
        }
        else {
            if ((s2 = BeanUtil.okNameForGetter(annotatedMethod)) == null) {
                s2 = annotatedMethod.getName();
            }
            String s3 = simpleName;
            if (simpleName.length() == 0) {
                s3 = s2;
            }
            b = true;
            simpleName = s3;
        }
        this._property(s2).addGetter(annotatedMethod, simpleName, b, annotationIntrospector != null && annotationIntrospector.hasIgnoreMarker(annotatedMethod));
    }
    
    protected void _addInjectables() {
        final AnnotationIntrospector annotationIntrospector = this._annotationIntrospector;
        if (annotationIntrospector != null) {
            for (final AnnotatedField annotatedField : this._classDef.fields()) {
                this._doAddInjectable(annotationIntrospector.findInjectableValueId(annotatedField), annotatedField);
            }
            for (final AnnotatedMethod annotatedMethod : this._classDef.memberMethods()) {
                if (annotatedMethod.getParameterCount() == 1) {
                    this._doAddInjectable(annotationIntrospector.findInjectableValueId(annotatedMethod), annotatedMethod);
                }
            }
        }
    }
    
    protected void _addMethods() {
        final AnnotationIntrospector annotationIntrospector = this._annotationIntrospector;
        for (final AnnotatedMethod annotatedMethod : this._classDef.memberMethods()) {
            final int parameterCount = annotatedMethod.getParameterCount();
            if (parameterCount == 0) {
                this._addGetterMethod(annotatedMethod, annotationIntrospector);
            }
            else if (parameterCount == 1) {
                this._addSetterMethod(annotatedMethod, annotationIntrospector);
            }
            else {
                if (parameterCount != 2 || annotationIntrospector == null || !annotationIntrospector.hasAnySetterAnnotation(annotatedMethod)) {
                    continue;
                }
                if (this._anySetters == null) {
                    this._anySetters = new LinkedList<AnnotatedMethod>();
                }
                this._anySetters.add(annotatedMethod);
            }
        }
    }
    
    protected void _addSetterMethod(final AnnotatedMethod annotatedMethod, final AnnotationIntrospector annotationIntrospector) {
        final String s = null;
        PropertyName nameForDeserialization;
        if (annotationIntrospector == null) {
            nameForDeserialization = null;
        }
        else {
            nameForDeserialization = annotationIntrospector.findNameForDeserialization(annotatedMethod);
        }
        String simpleName;
        if (nameForDeserialization == null) {
            simpleName = s;
        }
        else {
            simpleName = nameForDeserialization.getSimpleName();
        }
        String s2;
        boolean setterVisible;
        if (simpleName == null) {
            s2 = BeanUtil.okNameForMutator(annotatedMethod, this._mutatorPrefix);
            if (s2 == null) {
                return;
            }
            setterVisible = this._visibilityChecker.isSetterVisible(annotatedMethod);
        }
        else {
            if ((s2 = BeanUtil.okNameForMutator(annotatedMethod, this._mutatorPrefix)) == null) {
                s2 = annotatedMethod.getName();
            }
            String s3 = simpleName;
            if (simpleName.length() == 0) {
                s3 = s2;
            }
            setterVisible = true;
            simpleName = s3;
        }
        this._property(s2).addSetter(annotatedMethod, simpleName, setterVisible, annotationIntrospector != null && annotationIntrospector.hasIgnoreMarker(annotatedMethod));
    }
    
    protected void _doAddInjectable(final Object o, final AnnotatedMember annotatedMember) {
        if (o != null) {
            if (this._injectables == null) {
                this._injectables = new LinkedHashMap<Object, AnnotatedMember>();
            }
            if (this._injectables.put(o, annotatedMember) != null) {
                String name;
                if (o == null) {
                    name = "[null]";
                }
                else {
                    name = o.getClass().getName();
                }
                throw new IllegalArgumentException("Duplicate injectable value with id '" + String.valueOf(o) + "' (of type " + name + ")");
            }
        }
    }
    
    protected POJOPropertyBuilder _property(final String s) {
        POJOPropertyBuilder pojoPropertyBuilder;
        if ((pojoPropertyBuilder = this._properties.get(s)) == null) {
            pojoPropertyBuilder = new POJOPropertyBuilder(s, this._annotationIntrospector, this._forSerialization);
            this._properties.put(s, pojoPropertyBuilder);
        }
        return pojoPropertyBuilder;
    }
    
    protected void _removeUnwantedProperties() {
        final Iterator<Map.Entry<String, POJOPropertyBuilder>> iterator = this._properties.entrySet().iterator();
        final boolean b = !this._config.isEnabled(MapperFeature.INFER_PROPERTY_MUTATORS);
        while (iterator.hasNext()) {
            final POJOPropertyBuilder pojoPropertyBuilder = iterator.next().getValue();
            if (!pojoPropertyBuilder.anyVisible()) {
                iterator.remove();
            }
            else {
                if (pojoPropertyBuilder.anyIgnorals()) {
                    if (!pojoPropertyBuilder.isExplicitlyIncluded()) {
                        iterator.remove();
                        this._addIgnored(pojoPropertyBuilder.getName());
                        continue;
                    }
                    pojoPropertyBuilder.removeIgnored();
                    if (!this._forSerialization && !pojoPropertyBuilder.couldDeserialize()) {
                        this._addIgnored(pojoPropertyBuilder.getName());
                    }
                }
                pojoPropertyBuilder.removeNonVisible(b);
            }
        }
    }
    
    protected void _renameProperties() {
        final Iterator<Map.Entry<String, POJOPropertyBuilder>> iterator = this._properties.entrySet().iterator();
        AbstractSequentialList<POJOPropertyBuilder> list = null;
        while (iterator.hasNext()) {
            final POJOPropertyBuilder pojoPropertyBuilder = iterator.next().getValue();
            final String newName = pojoPropertyBuilder.findNewName();
            if (newName != null) {
                List<E> list2;
                if ((list2 = (List<E>)list) == null) {
                    list2 = (List<E>)new LinkedList<Object>();
                }
                ((LinkedList<POJOPropertyBuilder>)list2).add(pojoPropertyBuilder.withName(newName));
                iterator.remove();
                list = (AbstractSequentialList<POJOPropertyBuilder>)list2;
            }
        }
        if (list != null) {
            for (final POJOPropertyBuilder pojoPropertyBuilder2 : list) {
                final String name = pojoPropertyBuilder2.getName();
                final POJOPropertyBuilder pojoPropertyBuilder3 = this._properties.get(name);
                if (pojoPropertyBuilder3 == null) {
                    this._properties.put(name, pojoPropertyBuilder2);
                }
                else {
                    pojoPropertyBuilder3.addAll(pojoPropertyBuilder2);
                }
                if (this._creatorProperties != null) {
                    for (int i = 0; i < this._creatorProperties.size(); ++i) {
                        if (this._creatorProperties.get(i).getInternalName() == pojoPropertyBuilder2.getInternalName()) {
                            this._creatorProperties.set(i, pojoPropertyBuilder2);
                            break;
                        }
                    }
                }
            }
        }
    }
    
    protected void _renameUsing(final PropertyNamingStrategy propertyNamingStrategy) {
        final POJOPropertyBuilder[] array = this._properties.values().toArray(new POJOPropertyBuilder[this._properties.size()]);
        this._properties.clear();
        final int length = array.length;
        int i = 0;
    Label_0090_Outer:
        while (i < length) {
            final POJOPropertyBuilder pojoPropertyBuilder = array[i];
            String s = pojoPropertyBuilder.getName();
            if (this._forSerialization) {
                if (pojoPropertyBuilder.hasGetter()) {
                    s = propertyNamingStrategy.nameForGetterMethod(this._config, pojoPropertyBuilder.getGetter(), s);
                }
                else if (pojoPropertyBuilder.hasField()) {
                    s = propertyNamingStrategy.nameForField(this._config, pojoPropertyBuilder.getField(), s);
                }
            }
            else if (pojoPropertyBuilder.hasSetter()) {
                s = propertyNamingStrategy.nameForSetterMethod(this._config, pojoPropertyBuilder.getSetter(), s);
            }
            else if (pojoPropertyBuilder.hasConstructorParameter()) {
                s = propertyNamingStrategy.nameForConstructorParameter(this._config, pojoPropertyBuilder.getConstructorParameter(), s);
            }
            else if (pojoPropertyBuilder.hasField()) {
                s = propertyNamingStrategy.nameForField(this._config, pojoPropertyBuilder.getField(), s);
            }
            else if (pojoPropertyBuilder.hasGetter()) {
                s = propertyNamingStrategy.nameForGetterMethod(this._config, pojoPropertyBuilder.getGetter(), s);
            }
            while (true) {
                POJOPropertyBuilder withName = pojoPropertyBuilder;
                if (!s.equals(pojoPropertyBuilder.getName())) {
                    withName = pojoPropertyBuilder.withName(s);
                }
                final POJOPropertyBuilder pojoPropertyBuilder2 = this._properties.get(s);
                if (pojoPropertyBuilder2 == null) {
                    this._properties.put(s, withName);
                }
                else {
                    pojoPropertyBuilder2.addAll(withName);
                }
                ++i;
                continue Label_0090_Outer;
                continue;
            }
        }
    }
    
    protected void _renameWithWrappers() {
        final Iterator<Map.Entry<String, POJOPropertyBuilder>> iterator = this._properties.entrySet().iterator();
        AbstractSequentialList<POJOPropertyBuilder> list = null;
        while (iterator.hasNext()) {
            final POJOPropertyBuilder pojoPropertyBuilder = iterator.next().getValue();
            final AnnotatedMember primaryMember = pojoPropertyBuilder.getPrimaryMember();
            if (primaryMember != null) {
                final PropertyName wrapperName = this._annotationIntrospector.findWrapperName(primaryMember);
                if (wrapperName == null || !wrapperName.hasSimpleName()) {
                    continue;
                }
                final String simpleName = wrapperName.getSimpleName();
                if (simpleName.equals(pojoPropertyBuilder.getName())) {
                    continue;
                }
                List<E> list2;
                if ((list2 = (List<E>)list) == null) {
                    list2 = (List<E>)new LinkedList<Object>();
                }
                ((LinkedList<POJOPropertyBuilder>)list2).add(pojoPropertyBuilder.withName(simpleName));
                iterator.remove();
                list = (AbstractSequentialList<POJOPropertyBuilder>)list2;
            }
        }
        if (list != null) {
            for (final POJOPropertyBuilder pojoPropertyBuilder2 : list) {
                final String name = pojoPropertyBuilder2.getName();
                final POJOPropertyBuilder pojoPropertyBuilder3 = this._properties.get(name);
                if (pojoPropertyBuilder3 == null) {
                    this._properties.put(name, pojoPropertyBuilder2);
                }
                else {
                    pojoPropertyBuilder3.addAll(pojoPropertyBuilder2);
                }
            }
        }
    }
    
    protected void _sortProperties() {
        final AnnotationIntrospector annotationIntrospector = this._annotationIntrospector;
        Boolean serializationSortAlphabetically;
        if (annotationIntrospector == null) {
            serializationSortAlphabetically = null;
        }
        else {
            serializationSortAlphabetically = annotationIntrospector.findSerializationSortAlphabetically(this._classDef);
        }
        boolean b;
        if (serializationSortAlphabetically == null) {
            b = this._config.shouldSortPropertiesAlphabetically();
        }
        else {
            b = serializationSortAlphabetically;
        }
        String[] serializationPropertyOrder;
        if (annotationIntrospector == null) {
            serializationPropertyOrder = null;
        }
        else {
            serializationPropertyOrder = annotationIntrospector.findSerializationPropertyOrder(this._classDef);
        }
        if (!b && this._creatorProperties == null && serializationPropertyOrder == null) {
            return;
        }
        final int size = this._properties.size();
        Cloneable cloneable;
        if (b) {
            cloneable = new TreeMap<Object, Object>();
        }
        else {
            cloneable = new LinkedHashMap<Object, Object>(size + size);
        }
        for (final POJOPropertyBuilder pojoPropertyBuilder : this._properties.values()) {
            ((Map<String, POJOPropertyBuilder>)cloneable).put(pojoPropertyBuilder.getName(), pojoPropertyBuilder);
        }
        final LinkedHashMap linkedHashMap = new LinkedHashMap<String, POJOPropertyBuilder>(size + size);
        if (serializationPropertyOrder != null) {
            final int length = serializationPropertyOrder.length;
            int i = 0;
        Label_0296_Outer:
            while (i < length) {
                final String s = serializationPropertyOrder[i];
                POJOPropertyBuilder pojoPropertyBuilder2 = ((Map<String, POJOPropertyBuilder>)cloneable).get(s);
            Label_0296:
                while (true) {
                    if (pojoPropertyBuilder2 == null) {
                        for (final POJOPropertyBuilder pojoPropertyBuilder3 : this._properties.values()) {
                            if (s.equals(pojoPropertyBuilder3.getInternalName())) {
                                final String name = pojoPropertyBuilder3.getName();
                                pojoPropertyBuilder2 = pojoPropertyBuilder3;
                                final String s2 = name;
                                break Label_0296;
                            }
                        }
                    }
                    Label_0402: {
                        break Label_0402;
                        if (pojoPropertyBuilder2 != null) {
                            final String s2;
                            linkedHashMap.put(s2, pojoPropertyBuilder2);
                        }
                        ++i;
                        continue Label_0296_Outer;
                    }
                    final String s2 = s;
                    continue Label_0296;
                }
            }
        }
        if (this._creatorProperties != null) {
            for (final POJOPropertyBuilder pojoPropertyBuilder4 : this._creatorProperties) {
                linkedHashMap.put(pojoPropertyBuilder4.getName(), pojoPropertyBuilder4);
            }
        }
        linkedHashMap.putAll((Map<?, ?>)cloneable);
        this._properties.clear();
        this._properties.putAll((Map<?, ?>)linkedHashMap);
    }
    
    public POJOPropertiesCollector collect() {
        this._properties.clear();
        this._addFields();
        this._addMethods();
        this._addCreators();
        this._addInjectables();
        this._removeUnwantedProperties();
        this._renameProperties();
        final PropertyNamingStrategy findNamingStrategy = this._findNamingStrategy();
        if (findNamingStrategy != null) {
            this._renameUsing(findNamingStrategy);
        }
        final Iterator<POJOPropertyBuilder> iterator = this._properties.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().trimByVisibility();
        }
        final Iterator<POJOPropertyBuilder> iterator2 = this._properties.values().iterator();
        while (iterator2.hasNext()) {
            iterator2.next().mergeAnnotations(this._forSerialization);
        }
        if (this._config.isEnabled(MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME)) {
            this._renameWithWrappers();
        }
        this._sortProperties();
        return this;
    }
    
    public AnnotatedMember getAnyGetter() {
        if (this._anyGetters != null) {
            if (this._anyGetters.size() > 1) {
                this.reportProblem("Multiple 'any-getters' defined (" + this._anyGetters.get(0) + " vs " + this._anyGetters.get(1) + ")");
            }
            return this._anyGetters.getFirst();
        }
        return null;
    }
    
    public AnnotatedMethod getAnySetterMethod() {
        if (this._anySetters != null) {
            if (this._anySetters.size() > 1) {
                this.reportProblem("Multiple 'any-setters' defined (" + this._anySetters.get(0) + " vs " + this._anySetters.get(1) + ")");
            }
            return this._anySetters.getFirst();
        }
        return null;
    }
    
    public AnnotatedClass getClassDef() {
        return this._classDef;
    }
    
    public MapperConfig<?> getConfig() {
        return this._config;
    }
    
    public Set<String> getIgnoredPropertyNames() {
        return this._ignoredPropertyNames;
    }
    
    public Map<Object, AnnotatedMember> getInjectables() {
        return this._injectables;
    }
    
    public AnnotatedMethod getJsonValueMethod() {
        if (this._jsonValueGetters != null) {
            if (this._jsonValueGetters.size() > 1) {
                this.reportProblem("Multiple value properties defined (" + this._jsonValueGetters.get(0) + " vs " + this._jsonValueGetters.get(1) + ")");
            }
            return this._jsonValueGetters.get(0);
        }
        return null;
    }
    
    public ObjectIdInfo getObjectIdInfo() {
        ObjectIdInfo objectIdInfo;
        if (this._annotationIntrospector == null) {
            objectIdInfo = null;
        }
        else {
            final ObjectIdInfo objectIdInfo2 = this._annotationIntrospector.findObjectIdInfo(this._classDef);
            if ((objectIdInfo = objectIdInfo2) != null) {
                return this._annotationIntrospector.findObjectReferenceInfo(this._classDef, objectIdInfo2);
            }
        }
        return objectIdInfo;
    }
    
    public List<BeanPropertyDefinition> getProperties() {
        return new ArrayList<BeanPropertyDefinition>(this._properties.values());
    }
    
    public JavaType getType() {
        return this._type;
    }
    
    protected void reportProblem(final String s) {
        throw new IllegalArgumentException("Problem with definition of " + this._classDef + ": " + s);
    }
}
