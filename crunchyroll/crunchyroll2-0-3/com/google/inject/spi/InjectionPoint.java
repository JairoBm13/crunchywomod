// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.spi;

import java.util.Iterator;
import java.util.HashMap;
import java.util.Map;
import com.google.inject.internal.util.$Classes;
import java.util.List;
import com.google.inject.internal.util.$ImmutableSet;
import java.util.Collections;
import java.util.logging.Level;
import java.util.ArrayList;
import com.google.inject.internal.util.$Lists;
import java.util.Arrays;
import java.util.Set;
import java.lang.reflect.Modifier;
import com.google.inject.Inject;
import com.google.inject.internal.MoreTypes;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.lang.annotation.Annotation;
import com.google.inject.internal.ErrorsException;
import com.google.inject.ConfigurationException;
import com.google.inject.Key;
import com.google.inject.internal.Nullability;
import com.google.inject.internal.Annotations;
import com.google.inject.internal.Errors;
import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import com.google.inject.internal.util.$ImmutableList;
import com.google.inject.TypeLiteral;
import java.util.logging.Logger;

public final class InjectionPoint
{
    private static final Logger logger;
    private final TypeLiteral<?> declaringType;
    private final $ImmutableList<Dependency<?>> dependencies;
    private final Member member;
    private final boolean optional;
    
    static {
        logger = Logger.getLogger(InjectionPoint.class.getName());
    }
    
    InjectionPoint(final TypeLiteral<?> declaringType, final Constructor<?> member) {
        this.member = member;
        this.declaringType = declaringType;
        this.optional = false;
        this.dependencies = this.forMember(member, declaringType, member.getParameterAnnotations());
    }
    
    InjectionPoint(final TypeLiteral<?> declaringType, final Field member, final boolean optional) {
        this.member = member;
        this.declaringType = declaringType;
        this.optional = optional;
        final Annotation[] annotations = member.getAnnotations();
        final Errors errors = new Errors(member);
        final Key<Object> key = null;
        while (true) {
            try {
                final Key<?> key2 = Annotations.getKey(declaringType.getFieldType(member), member, annotations, errors);
                errors.throwConfigurationExceptionIfErrorsExist();
                this.dependencies = ($ImmutableList<Dependency<?>>)$ImmutableList.of(this.newDependency(key2, Nullability.allowsNull(annotations), -1));
            }
            catch (ConfigurationException ex) {
                errors.merge(ex.getErrorMessages());
                final Key<?> key2 = key;
                continue;
            }
            catch (ErrorsException ex2) {
                errors.merge(ex2.getErrors());
                final Key<?> key2 = key;
                continue;
            }
            break;
        }
    }
    
    InjectionPoint(final TypeLiteral<?> declaringType, final Method member, final boolean optional) {
        this.member = member;
        this.declaringType = declaringType;
        this.optional = optional;
        this.dependencies = this.forMember(member, declaringType, member.getParameterAnnotations());
    }
    
    private static boolean checkForMisplacedBindingAnnotations(final Member member, final Errors errors) {
        final Annotation bindingAnnotation = Annotations.findBindingAnnotation(errors, member, ((AnnotatedElement)member).getAnnotations());
        if (bindingAnnotation == null) {
            return false;
        }
        if (member instanceof Method) {
            try {
                if (member.getDeclaringClass().getDeclaredField(member.getName()) != null) {
                    return false;
                }
            }
            catch (NoSuchFieldException ex) {}
        }
        errors.misplacedBindingAnnotation(member, bindingAnnotation);
        return true;
    }
    
    public static <T> InjectionPoint forConstructor(final Constructor<T> constructor, final TypeLiteral<? extends T> typeLiteral) {
        if (typeLiteral.getRawType() != constructor.getDeclaringClass()) {
            new Errors(typeLiteral).constructorNotDefinedByType(constructor, typeLiteral).throwConfigurationExceptionIfErrorsExist();
        }
        return new InjectionPoint(typeLiteral, constructor);
    }
    
    public static InjectionPoint forConstructorOf(final TypeLiteral<?> typeLiteral) {
        final Class<?> rawType = MoreTypes.getRawType(typeLiteral.getType());
        final Errors errors = new Errors(rawType);
        Constructor<?> constructor = null;
        final Constructor[] declaredConstructors = rawType.getDeclaredConstructors();
        final int length = declaredConstructors.length;
        int i = 0;
    Label_0077_Outer:
        while (i < length) {
            final Constructor constructor2 = declaredConstructors[i];
            final Inject inject = constructor2.getAnnotation(Inject.class);
            while (true) {
                boolean optional = false;
                Label_0086: {
                    if (inject != null) {
                        optional = inject.optional();
                        break Label_0086;
                    }
                    if (constructor2.getAnnotation(javax.inject.Inject.class) != null) {
                        optional = false;
                        break Label_0086;
                    }
                    ++i;
                    continue Label_0077_Outer;
                }
                if (optional) {
                    errors.optionalConstructor(constructor2);
                }
                if (constructor != null) {
                    errors.tooManyConstructors(rawType);
                }
                constructor = (Constructor<?>)constructor2;
                checkForMisplacedBindingAnnotations(constructor, errors);
                continue;
            }
        }
        errors.throwConfigurationExceptionIfErrorsExist();
        if (constructor != null) {
            return new InjectionPoint(typeLiteral, constructor);
        }
        Constructor<?> declaredConstructor;
        try {
            declaredConstructor = rawType.getDeclaredConstructor((Class<?>[])new Class[0]);
            if (Modifier.isPrivate(declaredConstructor.getModifiers()) && !Modifier.isPrivate(rawType.getModifiers())) {
                errors.missingConstructor(rawType);
                throw new ConfigurationException(errors.getMessages());
            }
        }
        catch (NoSuchMethodException ex) {
            errors.missingConstructor(rawType);
            throw new ConfigurationException(errors.getMessages());
        }
        checkForMisplacedBindingAnnotations(declaredConstructor, errors);
        return new InjectionPoint(typeLiteral, declaredConstructor);
    }
    
    public static Set<InjectionPoint> forInstanceMethodsAndFields(final TypeLiteral<?> typeLiteral) {
        final Errors errors = new Errors();
        final Set<InjectionPoint> injectionPoints = getInjectionPoints(typeLiteral, false, errors);
        if (errors.hasErrors()) {
            throw new ConfigurationException(errors.getMessages()).withPartialValue(injectionPoints);
        }
        return injectionPoints;
    }
    
    public static Set<InjectionPoint> forInstanceMethodsAndFields(final Class<?> clazz) {
        return forInstanceMethodsAndFields(TypeLiteral.get(clazz));
    }
    
    private $ImmutableList<Dependency<?>> forMember(final Member member, TypeLiteral<?> iterator, Annotation[][] iterator2) {
        final Errors errors = new Errors(member);
        iterator2 = (Annotation[][])(Object)Arrays.asList(iterator2).iterator();
        final ArrayList<Object> arrayList = $Lists.newArrayList();
        int n = 0;
        iterator = ((TypeLiteral)iterator).getParameterTypes(member).iterator();
        while (iterator.hasNext()) {
            final TypeLiteral<?> typeLiteral = iterator.next();
            try {
                final Annotation[] array = ((Iterator<Annotation[]>)(Object)iterator2).next();
                arrayList.add(this.newDependency(Annotations.getKey(typeLiteral, member, array, errors), Nullability.allowsNull(array), n));
                ++n;
            }
            catch (ConfigurationException ex) {
                errors.merge(ex.getErrorMessages());
            }
            catch (ErrorsException ex2) {
                errors.merge(ex2.getErrors());
            }
        }
        errors.throwConfigurationExceptionIfErrorsExist();
        return $ImmutableList.copyOf((Iterable<? extends Dependency<?>>)arrayList);
    }
    
    public static Set<InjectionPoint> forStaticMethodsAndFields(final TypeLiteral<?> typeLiteral) {
        final Errors errors = new Errors();
        final Set<InjectionPoint> injectionPoints = getInjectionPoints(typeLiteral, true, errors);
        if (errors.hasErrors()) {
            throw new ConfigurationException(errors.getMessages()).withPartialValue(injectionPoints);
        }
        return injectionPoints;
    }
    
    public static Set<InjectionPoint> forStaticMethodsAndFields(final Class<?> clazz) {
        return forStaticMethodsAndFields(TypeLiteral.get(clazz));
    }
    
    static Annotation getAtInject(final AnnotatedElement annotatedElement) {
        Object o;
        if ((o = annotatedElement.getAnnotation(javax.inject.Inject.class)) == null) {
            o = annotatedElement.getAnnotation(Inject.class);
        }
        return (Annotation)o;
    }
    
    private static Set<InjectionPoint> getInjectionPoints(TypeLiteral<?> o, final boolean b, final Errors errors) {
        final InjectableMembers injectableMembers = new InjectableMembers();
        final OverrideIndex overrideIndex = null;
        final List<TypeLiteral<?>> hierarchy = hierarchyFor((TypeLiteral<?>)o);
        int i;
        final int n = i = hierarchy.size() - 1;
        OverrideIndex overrideIndex2 = overrideIndex;
        while (i >= 0) {
            if (overrideIndex2 != null && i < n) {
                if (i == 0) {
                    overrideIndex2.position = Position.BOTTOM;
                }
                else {
                    overrideIndex2.position = Position.MIDDLE;
                }
            }
            final TypeLiteral<?> typeLiteral = hierarchy.get(i);
            final Field[] declaredFields = typeLiteral.getRawType().getDeclaredFields();
            for (int length = declaredFields.length, j = 0; j < length; ++j) {
                final Field field = declaredFields[j];
                if (Modifier.isStatic(field.getModifiers()) == b) {
                    final Annotation atInject = getAtInject(field);
                    if (atInject != null) {
                        final InjectableField injectableField = new InjectableField(typeLiteral, field, atInject);
                        if (injectableField.jsr330 && Modifier.isFinal(field.getModifiers())) {
                            errors.cannotInjectFinalField(field);
                        }
                        injectableMembers.add(injectableField);
                    }
                }
            }
            final Method[] declaredMethods = typeLiteral.getRawType().getDeclaredMethods();
            OverrideIndex overrideIndex3;
            for (int length2 = declaredMethods.length, k = 0; k < length2; ++k, overrideIndex2 = overrideIndex3) {
                final Method method = declaredMethods[k];
                overrideIndex3 = overrideIndex2;
                if (Modifier.isStatic(method.getModifiers()) == b) {
                    final Annotation atInject2 = getAtInject(method);
                    if (atInject2 != null) {
                        final InjectableMethod injectableMethod = new InjectableMethod(typeLiteral, method, atInject2);
                        if (!isValidMethod(injectableMethod, errors) | checkForMisplacedBindingAnnotations(method, errors)) {
                            if ((overrideIndex3 = overrideIndex2) != null) {
                                overrideIndex3 = overrideIndex2;
                                if (overrideIndex2.removeIfOverriddenBy(method, false, injectableMethod)) {
                                    InjectionPoint.logger.log(Level.WARNING, "Method: {0} is not a valid injectable method (because it either has misplaced binding annotations or specifies type parameters) but is overriding a method that is valid. Because it is not valid, the method will not be injected. To fix this, make the method a valid injectable method.", method);
                                    overrideIndex3 = overrideIndex2;
                                }
                            }
                        }
                        else if (b) {
                            injectableMembers.add(injectableMethod);
                            overrideIndex3 = overrideIndex2;
                        }
                        else {
                            if (overrideIndex2 == null) {
                                overrideIndex2 = new OverrideIndex(injectableMembers);
                            }
                            else {
                                overrideIndex2.removeIfOverriddenBy(method, true, injectableMethod);
                            }
                            overrideIndex2.add(injectableMethod);
                            overrideIndex3 = overrideIndex2;
                        }
                    }
                    else if ((overrideIndex3 = overrideIndex2) != null) {
                        overrideIndex3 = overrideIndex2;
                        if (overrideIndex2.removeIfOverriddenBy(method, false, null)) {
                            InjectionPoint.logger.log(Level.WARNING, "Method: {0} is not annotated with @Inject but is overriding a method that is annotated with @javax.inject.Inject.  Because it is not annotated with @Inject, the method will not be injected. To fix this, annotate the method with @Inject.", method);
                            overrideIndex3 = overrideIndex2;
                        }
                    }
                }
            }
            --i;
        }
        if (injectableMembers.isEmpty()) {
            return Collections.emptySet();
        }
        final $ImmutableSet.Builder<InjectionPoint> builder = $ImmutableSet.builder();
        o = injectableMembers.head;
    Label_0496_Outer:
        while (o != null) {
            while (true) {
                try {
                    builder.add(((InjectableMember)o).toInjectionPoint());
                    o = ((InjectableMember)o).next;
                    continue Label_0496_Outer;
                }
                catch (ConfigurationException ex) {
                    if (!((InjectableMember)o).optional) {
                        errors.merge(ex.getErrorMessages());
                    }
                    continue;
                }
                break;
            }
            break;
        }
        return builder.build();
    }
    
    private static List<TypeLiteral<?>> hierarchyFor(TypeLiteral<?> supertype) {
        final ArrayList<TypeLiteral<?>> list = new ArrayList<TypeLiteral<?>>();
        while (supertype.getRawType() != Object.class) {
            list.add(supertype);
            supertype = supertype.getSupertype(supertype.getRawType().getSuperclass());
        }
        return list;
    }
    
    private static boolean isValidMethod(final InjectableMethod injectableMethod, final Errors errors) {
        boolean b = true;
        final boolean b2 = true;
        if (injectableMethod.jsr330) {
            final Method method = injectableMethod.method;
            b = b2;
            if (Modifier.isAbstract(method.getModifiers())) {
                errors.cannotInjectAbstractMethod(method);
                b = false;
            }
            if (method.getTypeParameters().length > 0) {
                errors.cannotInjectMethodWithTypeParameters(method);
                b = false;
            }
        }
        return b;
    }
    
    private <T> Dependency<T> newDependency(final Key<T> key, final boolean b, final int n) {
        return new Dependency<T>(this, key, b, n);
    }
    
    private static boolean overrides(final Method method, final Method method2) {
        final int modifiers = method2.getModifiers();
        return Modifier.isPublic(modifiers) || Modifier.isProtected(modifiers) || (!Modifier.isPrivate(modifiers) && method.getDeclaringClass().getPackage().equals(method2.getDeclaringClass().getPackage()));
    }
    
    @Override
    public boolean equals(final Object o) {
        return o instanceof InjectionPoint && this.member.equals(((InjectionPoint)o).member) && this.declaringType.equals(((InjectionPoint)o).declaringType);
    }
    
    public TypeLiteral<?> getDeclaringType() {
        return this.declaringType;
    }
    
    public List<Dependency<?>> getDependencies() {
        return this.dependencies;
    }
    
    public Member getMember() {
        return this.member;
    }
    
    @Override
    public int hashCode() {
        return this.member.hashCode() ^ this.declaringType.hashCode();
    }
    
    public boolean isOptional() {
        return this.optional;
    }
    
    public boolean isToolable() {
        return ((AnnotatedElement)this.member).isAnnotationPresent(Toolable.class);
    }
    
    @Override
    public String toString() {
        return $Classes.toString(this.member);
    }
    
    static class InjectableField extends InjectableMember
    {
        final Field field;
        
        InjectableField(final TypeLiteral<?> typeLiteral, final Field field, final Annotation annotation) {
            super(typeLiteral, annotation);
            this.field = field;
        }
        
        @Override
        InjectionPoint toInjectionPoint() {
            return new InjectionPoint(this.declaringType, this.field, this.optional);
        }
    }
    
    abstract static class InjectableMember
    {
        final TypeLiteral<?> declaringType;
        final boolean jsr330;
        InjectableMember next;
        final boolean optional;
        InjectableMember previous;
        
        InjectableMember(final TypeLiteral<?> declaringType, final Annotation annotation) {
            this.declaringType = declaringType;
            if (annotation.annotationType() == javax.inject.Inject.class) {
                this.optional = false;
                this.jsr330 = true;
                return;
            }
            this.jsr330 = false;
            this.optional = ((Inject)annotation).optional();
        }
        
        abstract InjectionPoint toInjectionPoint();
    }
    
    static class InjectableMembers
    {
        InjectableMember head;
        InjectableMember tail;
        
        void add(final InjectableMember injectableMember) {
            if (this.head == null) {
                this.tail = injectableMember;
                this.head = injectableMember;
                return;
            }
            injectableMember.previous = this.tail;
            this.tail.next = injectableMember;
            this.tail = injectableMember;
        }
        
        boolean isEmpty() {
            return this.head == null;
        }
        
        void remove(final InjectableMember injectableMember) {
            if (injectableMember.previous != null) {
                injectableMember.previous.next = injectableMember.next;
            }
            if (injectableMember.next != null) {
                injectableMember.next.previous = injectableMember.previous;
            }
            if (this.head == injectableMember) {
                this.head = injectableMember.next;
            }
            if (this.tail == injectableMember) {
                this.tail = injectableMember.previous;
            }
        }
    }
    
    static class InjectableMethod extends InjectableMember
    {
        final Method method;
        boolean overrodeGuiceInject;
        
        InjectableMethod(final TypeLiteral<?> typeLiteral, final Method method, final Annotation annotation) {
            super(typeLiteral, annotation);
            this.method = method;
        }
        
        public boolean isFinal() {
            return Modifier.isFinal(this.method.getModifiers());
        }
        
        @Override
        InjectionPoint toInjectionPoint() {
            return new InjectionPoint(this.declaringType, this.method, this.optional);
        }
    }
    
    static class OverrideIndex
    {
        Map<Signature, List<InjectableMethod>> bySignature;
        final InjectableMembers injectableMembers;
        Method lastMethod;
        Signature lastSignature;
        Position position;
        
        OverrideIndex(final InjectableMembers injectableMembers) {
            this.position = Position.TOP;
            this.injectableMembers = injectableMembers;
        }
        
        void add(final InjectableMethod injectableMethod) {
            this.injectableMembers.add(injectableMethod);
            if (this.position != Position.BOTTOM && !injectableMethod.isFinal() && this.bySignature != null) {
                Signature lastSignature;
                if (injectableMethod.method == this.lastMethod) {
                    lastSignature = this.lastSignature;
                }
                else {
                    lastSignature = new Signature(injectableMethod.method);
                }
                List<InjectableMethod> list;
                if ((list = this.bySignature.get(lastSignature)) == null) {
                    list = new ArrayList<InjectableMethod>();
                    this.bySignature.put(lastSignature, list);
                }
                list.add(injectableMethod);
            }
        }
        
        boolean removeIfOverriddenBy(final Method lastMethod, final boolean b, final InjectableMethod injectableMethod) {
            boolean b2;
            if (this.position == Position.TOP) {
                b2 = false;
            }
            else {
                if (this.bySignature == null) {
                    this.bySignature = new HashMap<Signature, List<InjectableMethod>>();
                    for (Object o = this.injectableMembers.head; o != null; o = ((InjectableMember)o).next) {
                        if (o instanceof InjectableMethod) {
                            final InjectableMethod injectableMethod2 = (InjectableMethod)o;
                            if (!injectableMethod2.isFinal()) {
                                final ArrayList<InjectableMethod> list = new ArrayList<InjectableMethod>();
                                list.add(injectableMethod2);
                                this.bySignature.put(new Signature(injectableMethod2.method), list);
                            }
                        }
                    }
                }
                this.lastMethod = lastMethod;
                final Signature lastSignature = new Signature(lastMethod);
                this.lastSignature = lastSignature;
                final List<InjectableMethod> list2 = this.bySignature.get(lastSignature);
                boolean b3 = false;
                b2 = false;
                if (list2 != null) {
                    final Iterator<InjectableMethod> iterator = list2.iterator();
                    while (true) {
                        b2 = b3;
                        if (!iterator.hasNext()) {
                            break;
                        }
                        final InjectableMethod injectableMethod3 = iterator.next();
                        if (!overrides(lastMethod, injectableMethod3.method)) {
                            continue;
                        }
                        final boolean overrodeGuiceInject = !injectableMethod3.jsr330 || injectableMethod3.overrodeGuiceInject;
                        if (injectableMethod != null) {
                            injectableMethod.overrodeGuiceInject = overrodeGuiceInject;
                        }
                        if (!b && overrodeGuiceInject) {
                            continue;
                        }
                        b3 = true;
                        iterator.remove();
                        this.injectableMembers.remove(injectableMethod3);
                    }
                }
            }
            return b2;
        }
    }
    
    enum Position
    {
        BOTTOM, 
        MIDDLE, 
        TOP;
    }
    
    static class Signature
    {
        final int hash;
        final String name;
        final Class[] parameterTypes;
        
        Signature(final Method method) {
            this.name = method.getName();
            this.parameterTypes = method.getParameterTypes();
            int hash = this.name.hashCode() * 31 + this.parameterTypes.length;
            final Class[] parameterTypes = this.parameterTypes;
            for (int length = parameterTypes.length, i = 0; i < length; ++i) {
                hash = hash * 31 + parameterTypes[i].hashCode();
            }
            this.hash = hash;
        }
        
        @Override
        public boolean equals(final Object o) {
            if (o instanceof Signature) {
                final Signature signature = (Signature)o;
                if (this.name.equals(signature.name) && this.parameterTypes.length == signature.parameterTypes.length) {
                    for (int i = 0; i < this.parameterTypes.length; ++i) {
                        if (this.parameterTypes[i] != signature.parameterTypes[i]) {
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        }
        
        @Override
        public int hashCode() {
            return this.hash;
        }
    }
}
