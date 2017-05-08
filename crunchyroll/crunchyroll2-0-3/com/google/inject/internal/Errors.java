// 
// Decompiled by Procyon v0.5.30
// 

package com.google.inject.internal;

import com.google.inject.Provider;
import java.util.Collections;
import java.util.Comparator;
import com.google.inject.spi.TypeListenerBinding;
import com.google.inject.spi.InjectionListener;
import com.google.inject.MembersInjector;
import com.google.inject.Scope;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.Set;
import java.lang.reflect.Type;
import java.lang.reflect.Method;
import com.google.inject.spi.TypeConverterBinding;
import java.util.ArrayList;
import com.google.inject.internal.util.$Lists;
import com.google.inject.internal.util.$ImmutableSet;
import com.google.inject.CreationException;
import com.google.inject.ConfigurationException;
import com.google.inject.ProvisionException;
import com.google.inject.TypeLiteral;
import com.google.inject.internal.util.$StackTraceElements;
import java.lang.reflect.Field;
import com.google.inject.spi.InjectionPoint;
import com.google.inject.spi.Dependency;
import java.io.Writer;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Formatter;
import java.util.Iterator;
import com.google.inject.internal.util.$SourceProvider;
import com.google.inject.internal.util.$ImmutableList;
import com.google.inject.Key;
import com.google.inject.internal.util.$Classes;
import java.lang.reflect.Member;
import com.google.inject.spi.Message;
import java.util.List;
import java.util.Collection;
import java.io.Serializable;

public final class Errors implements Serializable
{
    private static final Collection<Converter<?>> converters;
    private List<Message> errors;
    private final Errors parent;
    private final Errors root;
    private final Object source;
    
    static {
        converters = $ImmutableList.of(new Converter<Class>((Class)Class.class) {
            public String toString(final Class clazz) {
                return clazz.getName();
            }
        }, new Converter<Member>((Class)Member.class) {
            public String toString(final Member member) {
                return $Classes.toString(member);
            }
        }, new Converter<Key>((Class)Key.class) {
            public String toString(final Key key) {
                if (key.getAnnotationType() != null) {
                    final StringBuilder append = new StringBuilder().append(key.getTypeLiteral()).append(" annotated with ");
                    Object o;
                    if (key.getAnnotation() != null) {
                        o = key.getAnnotation();
                    }
                    else {
                        o = key.getAnnotationType();
                    }
                    return append.append(o).toString();
                }
                return key.getTypeLiteral().toString();
            }
        });
    }
    
    public Errors() {
        this.root = this;
        this.parent = null;
        this.source = $SourceProvider.UNKNOWN_SOURCE;
    }
    
    private Errors(final Errors parent, final Object source) {
        this.root = parent.root;
        this.parent = parent;
        this.source = source;
    }
    
    public Errors(final Object source) {
        this.root = this;
        this.parent = null;
        this.source = source;
    }
    
    private Errors addMessage(final Throwable t, String format, final Object... array) {
        format = format(format, array);
        this.addMessage(new Message(this.getSources(), format, t));
        return this;
    }
    
    public static Object convert(final Object o) {
        final Iterator<Converter<?>> iterator = Errors.converters.iterator();
        Converter<?> converter;
        do {
            final Object convert = o;
            if (!iterator.hasNext()) {
                return convert;
            }
            converter = iterator.next();
        } while (!converter.appliesTo(o));
        return converter.convert(o);
    }
    
    public static String format(final String s, final Collection<Message> collection) {
        final Formatter format = new Formatter().format(s, new Object[0]).format(":%n%n", new Object[0]);
        int n = 1;
        final boolean b = getOnlyCause(collection) == null;
        for (final Message message : collection) {
            format.format("%s) %s%n", n, message.getMessage());
            final List<Object> sources = message.getSources();
            for (int i = sources.size() - 1; i >= 0; --i) {
                formatSource(format, sources.get(i));
            }
            final Throwable cause = message.getCause();
            if (b && cause != null) {
                final StringWriter stringWriter = new StringWriter();
                cause.printStackTrace(new PrintWriter(stringWriter));
                format.format("Caused by: %s", stringWriter.getBuffer());
            }
            format.format("%n", new Object[0]);
            ++n;
        }
        if (collection.size() == 1) {
            format.format("1 error", new Object[0]);
        }
        else {
            format.format("%s errors", collection.size());
        }
        return format.toString();
    }
    
    public static String format(final String s, final Object... array) {
        for (int i = 0; i < array.length; ++i) {
            array[i] = convert(array[i]);
        }
        return String.format(s, array);
    }
    
    public static void formatInjectionPoint(final Formatter formatter, final Dependency<?> dependency, final InjectionPoint injectionPoint) {
        final Member member = injectionPoint.getMember();
        if ($Classes.memberType(member) == Field.class) {
            formatter.format("  while locating %s%n", convert(injectionPoint.getDependencies().get(0).getKey()));
            formatter.format("    for field at %s%n", $StackTraceElements.forMember(member));
            return;
        }
        if (dependency != null) {
            formatter.format("  while locating %s%n", convert(dependency.getKey()));
            formatter.format("    for parameter %s at %s%n", dependency.getParameterIndex(), $StackTraceElements.forMember(member));
            return;
        }
        formatSource(formatter, injectionPoint.getMember());
    }
    
    public static void formatSource(final Formatter formatter, final Object o) {
        if (o instanceof Dependency) {
            final Dependency dependency = (Dependency)o;
            final InjectionPoint injectionPoint = dependency.getInjectionPoint();
            if (injectionPoint != null) {
                formatInjectionPoint(formatter, dependency, injectionPoint);
                return;
            }
            formatSource(formatter, dependency.getKey());
        }
        else {
            if (o instanceof InjectionPoint) {
                formatInjectionPoint(formatter, null, (InjectionPoint)o);
                return;
            }
            if (o instanceof Class) {
                formatter.format("  at %s%n", $StackTraceElements.forType((Class<?>)o));
                return;
            }
            if (o instanceof Member) {
                formatter.format("  at %s%n", $StackTraceElements.forMember((Member)o));
                return;
            }
            if (o instanceof TypeLiteral) {
                formatter.format("  while locating %s%n", o);
                return;
            }
            if (o instanceof Key) {
                formatter.format("  while locating %s%n", convert(o));
                return;
            }
            formatter.format("  at %s%n", o);
        }
    }
    
    public static Collection<Message> getMessagesFromThrowable(final Throwable t) {
        if (t instanceof ProvisionException) {
            return ((ProvisionException)t).getErrorMessages();
        }
        if (t instanceof ConfigurationException) {
            return ((ConfigurationException)t).getErrorMessages();
        }
        if (t instanceof CreationException) {
            return ((CreationException)t).getErrorMessages();
        }
        return (Collection<Message>)$ImmutableSet.of();
    }
    
    public static Throwable getOnlyCause(final Collection<Message> collection) {
        final Throwable t = null;
        final Iterator<Message> iterator = collection.iterator();
        Throwable t2 = t;
        Throwable t3;
        while (true) {
            t3 = t2;
            if (!iterator.hasNext()) {
                break;
            }
            final Throwable cause = iterator.next().getCause();
            if (cause == null) {
                continue;
            }
            if (t2 != null) {
                t3 = null;
                break;
            }
            t2 = cause;
        }
        return t3;
    }
    
    private Message merge(final Message message) {
        final ArrayList<Object> arrayList = $Lists.newArrayList();
        arrayList.addAll(this.getSources());
        arrayList.addAll(message.getSources());
        return new Message(arrayList, message.getMessage(), message.getCause());
    }
    
    private Throwable unwrap(final RuntimeException ex) {
        Throwable cause = ex;
        if (ex instanceof Exceptions.UnhandledCheckedUserException) {
            cause = ex.getCause();
        }
        return cause;
    }
    
    public Errors addMessage(final Message message) {
        if (this.root.errors == null) {
            this.root.errors = (List<Message>)$Lists.newArrayList();
        }
        this.root.errors.add(message);
        return this;
    }
    
    public Errors addMessage(final String s, final Object... array) {
        return this.addMessage(null, s, array);
    }
    
    public Errors ambiguousTypeConversion(final String s, final Object o, final TypeLiteral<?> typeLiteral, final TypeConverterBinding typeConverterBinding, final TypeConverterBinding typeConverterBinding2) {
        return this.addMessage("Multiple converters can convert '%s' (bound at %s) to %s:%n %s and%n %s.%n Please adjust your type converter configuration to avoid overlapping matches.", s, convert(o), typeLiteral, typeConverterBinding, typeConverterBinding2);
    }
    
    public Errors bindingAlreadySet(final Key<?> key, final Object o) {
        return this.addMessage("A binding to %s was already configured at %s.", key, convert(o));
    }
    
    public Errors bindingToProvider() {
        return this.addMessage("Binding to Provider is not allowed.", new Object[0]);
    }
    
    public Errors cannotBindToGuiceType(final String s) {
        return this.addMessage("Binding to core guice framework type is not allowed: %s.", s);
    }
    
    public Errors cannotInjectAbstractMethod(final Method method) {
        return this.addMessage("Injected method %s cannot be abstract.", method);
    }
    
    public Errors cannotInjectFinalField(final Field field) {
        return this.addMessage("Injected field %s cannot be final.", field);
    }
    
    public Errors cannotInjectInnerClass(final Class<?> clazz) {
        return this.addMessage("Injecting into inner classes is not supported.  Please use a 'static' class (top-level or nested) instead of %s.", clazz);
    }
    
    public Errors cannotInjectMethodWithTypeParameters(final Method method) {
        return this.addMessage("Injected method %s cannot declare type parameters of its own.", method);
    }
    
    public Errors cannotInjectRawMembersInjector() {
        return this.addMessage("Cannot inject a MembersInjector that has no type parameter", new Object[0]);
    }
    
    public Errors cannotInjectRawProvider() {
        return this.addMessage("Cannot inject a Provider that has no type parameter", new Object[0]);
    }
    
    public Errors cannotInjectRawTypeLiteral() {
        return this.addMessage("Cannot inject a TypeLiteral that has no type parameter", new Object[0]);
    }
    
    public Errors cannotInjectTypeLiteralOf(final Type type) {
        return this.addMessage("Cannot inject a TypeLiteral of %s", type);
    }
    
    public Errors cannotSatisfyCircularDependency(final Class<?> clazz) {
        return this.addMessage("Tried proxying %s to support a circular dependency, but it is not an interface.", clazz);
    }
    
    public <T> T checkForNull(final T t, final Object o, final Dependency<?> dependency) throws ErrorsException {
        if (t != null || dependency.isNullable()) {
            return t;
        }
        final int parameterIndex = dependency.getParameterIndex();
        String string;
        if (parameterIndex != -1) {
            string = "parameter " + parameterIndex + " of ";
        }
        else {
            string = "";
        }
        this.addMessage("null returned by binding at %s%n but %s%s is not @Nullable", o, string, dependency.getInjectionPoint().getMember());
        throw this.toException();
    }
    
    public Errors childBindingAlreadySet(final Key<?> key, final Set<Object> set) {
        final Formatter formatter = new Formatter();
        for (final Object next : set) {
            if (next == null) {
                formatter.format("%n    (bound by a just-in-time binding)", new Object[0]);
            }
            else {
                formatter.format("%n    bound at %s", next);
            }
        }
        return this.addMessage("Unable to create binding for %s. It was already configured on one or more child injectors or private modules%s%n  If it was in a PrivateModule, did you forget to expose the binding?", key, formatter.out());
    }
    
    public Errors circularProxiesDisabled(final Class<?> clazz) {
        return this.addMessage("Tried proxying %s to support a circular dependency, but circular proxies are disabled.", clazz);
    }
    
    public Errors constructorNotDefinedByType(final Constructor<?> constructor, final TypeLiteral<?> typeLiteral) {
        return this.addMessage("%s does not define %s", typeLiteral, constructor);
    }
    
    public Errors conversionError(final String s, final Object o, final TypeLiteral<?> typeLiteral, final TypeConverterBinding typeConverterBinding, final RuntimeException ex) {
        return this.errorInUserCode(ex, "Error converting '%s' (bound at %s) to %s%n using %s.%n Reason: %s", s, convert(o), typeLiteral, typeConverterBinding, ex);
    }
    
    public Errors conversionTypeError(final String s, final Object o, final TypeLiteral<?> typeLiteral, final TypeConverterBinding typeConverterBinding, final Object o2) {
        return this.addMessage("Type mismatch converting '%s' (bound at %s) to %s%n using %s.%n Converter returned %s.", s, convert(o), typeLiteral, typeConverterBinding, o2);
    }
    
    public Errors converterReturnedNull(final String s, final Object o, final TypeLiteral<?> typeLiteral, final TypeConverterBinding typeConverterBinding) {
        return this.addMessage("Received null converting '%s' (bound at %s) to %s%n using %s.", s, convert(o), typeLiteral, typeConverterBinding);
    }
    
    public Errors duplicateBindingAnnotations(final Member member, final Class<? extends Annotation> clazz, final Class<? extends Annotation> clazz2) {
        return this.addMessage("%s has more than one annotation annotated with @BindingAnnotation: %s and %s", member, clazz, clazz2);
    }
    
    public Errors duplicateScopeAnnotations(final Class<? extends Annotation> clazz, final Class<? extends Annotation> clazz2) {
        return this.addMessage("More than one scope annotation was found: %s and %s.", clazz, clazz2);
    }
    
    public Errors duplicateScopes(final Scope scope, final Class<? extends Annotation> clazz, final Scope scope2) {
        return this.addMessage("Scope %s is already bound to %s. Cannot bind %s.", scope, clazz, scope2);
    }
    
    public Errors errorCheckingDuplicateBinding(final Key<?> key, final Object o, final Throwable t) {
        return this.addMessage("A binding to %s was already configured at %s and an error was thrown while checking duplicate bindings.  Error: %s", key, convert(o), t);
    }
    
    public Errors errorInProvider(final RuntimeException ex) {
        final Throwable unwrap = this.unwrap(ex);
        return this.errorInUserCode(unwrap, "Error in custom provider, %s", unwrap);
    }
    
    public Errors errorInUserCode(final Throwable t, final String s, final Object... array) {
        final Collection<Message> messagesFromThrowable = getMessagesFromThrowable(t);
        if (!messagesFromThrowable.isEmpty()) {
            return this.merge(messagesFromThrowable);
        }
        return this.addMessage(t, s, array);
    }
    
    public Errors errorInUserInjector(final MembersInjector<?> membersInjector, final TypeLiteral<?> typeLiteral, final RuntimeException ex) {
        return this.errorInUserCode(ex, "Error injecting %s using %s.%n Reason: %s", typeLiteral, membersInjector, ex);
    }
    
    public Errors errorInjectingConstructor(final Throwable t) {
        return this.errorInUserCode(t, "Error injecting constructor, %s", t);
    }
    
    public Errors errorInjectingMethod(final Throwable t) {
        return this.errorInUserCode(t, "Error injecting method, %s", t);
    }
    
    public Errors errorNotifyingInjectionListener(final InjectionListener<?> injectionListener, final TypeLiteral<?> typeLiteral, final RuntimeException ex) {
        return this.errorInUserCode(ex, "Error notifying InjectionListener %s of %s.%n Reason: %s", injectionListener, typeLiteral, ex);
    }
    
    public Errors errorNotifyingTypeListener(final TypeListenerBinding typeListenerBinding, final TypeLiteral<?> typeLiteral, final Throwable t) {
        return this.errorInUserCode(t, "Error notifying TypeListener %s (bound at %s) of %s.%n Reason: %s", typeListenerBinding.getListener(), convert(typeListenerBinding.getSource()), typeLiteral, t);
    }
    
    public Errors exposedButNotBound(final Key<?> key) {
        return this.addMessage("Could not expose() %s, it must be explicitly bound.", key);
    }
    
    public List<Message> getMessages() {
        if (this.root.errors == null) {
            return (List<Message>)$ImmutableList.of();
        }
        final ArrayList<Object> arrayList = $Lists.newArrayList((Iterable<?>)this.root.errors);
        Collections.sort(arrayList, (Comparator<? super Object>)new Comparator<Message>() {
            @Override
            public int compare(final Message message, final Message message2) {
                return message.getSource().compareTo(message2.getSource());
            }
        });
        return (List<Message>)arrayList;
    }
    
    public List<Object> getSources() {
        final ArrayList<Object> arrayList = $Lists.newArrayList();
        for (Errors parent = this; parent != null; parent = parent.parent) {
            if (parent.source != $SourceProvider.UNKNOWN_SOURCE) {
                arrayList.add(0, parent.source);
            }
        }
        return arrayList;
    }
    
    public boolean hasErrors() {
        return this.root.errors != null;
    }
    
    public Errors jitBindingAlreadySet(final Key<?> key) {
        return this.addMessage("A just-in-time binding to %s was already configured on a parent injector.", key);
    }
    
    public Errors jitDisabled(final Key key) {
        return this.addMessage("Explicit bindings are required and %s is not explicitly bound.", key);
    }
    
    public Errors keyNotFullySpecified(final TypeLiteral<?> typeLiteral) {
        return this.addMessage("%s cannot be used as a key; It is not fully specified.", typeLiteral);
    }
    
    public Errors merge(final Errors errors) {
        if (errors.root == this.root || errors.root.errors == null) {
            return this;
        }
        this.merge(errors.root.errors);
        return this;
    }
    
    public Errors merge(final Collection<Message> collection) {
        final Iterator<Message> iterator = collection.iterator();
        while (iterator.hasNext()) {
            this.addMessage(this.merge(iterator.next()));
        }
        return this;
    }
    
    public Errors misplacedBindingAnnotation(final Member member, final Annotation annotation) {
        return this.addMessage("%s is annotated with %s, but binding annotations should be applied to its parameters instead.", member, annotation);
    }
    
    public Errors missingConstantValues() {
        return this.addMessage("Missing constant value. Please call to(...).", new Object[0]);
    }
    
    public Errors missingConstructor(final Class<?> clazz) {
        return this.addMessage("Could not find a suitable constructor in %s. Classes must have either one (and only one) constructor annotated with @Inject or a zero-argument constructor that is not private.", clazz);
    }
    
    public Errors missingImplementation(final Key key) {
        return this.addMessage("No implementation for %s was bound.", key);
    }
    
    public Errors missingRuntimeRetention(final Object o) {
        return this.addMessage("Please annotate with @Retention(RUNTIME).%n Bound at %s.", convert(o));
    }
    
    public Errors missingScopeAnnotation() {
        return this.addMessage("Please annotate with @ScopeAnnotation.", new Object[0]);
    }
    
    public Errors notASubtype(final Class<?> clazz, final Class<?> clazz2) {
        return this.addMessage("%s doesn't extend %s.", clazz, clazz2);
    }
    
    public Errors optionalConstructor(final Constructor constructor) {
        return this.addMessage("%s is annotated @Inject(optional=true), but constructors cannot be optional.", constructor);
    }
    
    public Errors recursiveBinding() {
        return this.addMessage("Binding points to itself.", new Object[0]);
    }
    
    public Errors recursiveImplementationType() {
        return this.addMessage("@ImplementedBy points to the same class it annotates.", new Object[0]);
    }
    
    public Errors recursiveProviderType() {
        return this.addMessage("@ProvidedBy points to the same class it annotates.", new Object[0]);
    }
    
    public Errors scopeAnnotationOnAbstractType(final Class<? extends Annotation> clazz, final Class<?> clazz2, final Object o) {
        return this.addMessage("%s is annotated with %s, but scope annotations are not supported for abstract types.%n Bound at %s.", clazz2, clazz, convert(o));
    }
    
    public Errors scopeNotFound(final Class<? extends Annotation> clazz) {
        return this.addMessage("No scope is bound to %s.", clazz);
    }
    
    public int size() {
        if (this.root.errors == null) {
            return 0;
        }
        return this.root.errors.size();
    }
    
    public Errors subtypeNotProvided(final Class<? extends Provider<?>> clazz, final Class<?> clazz2) {
        return this.addMessage("%s doesn't provide instances of %s.", clazz, clazz2);
    }
    
    public void throwConfigurationExceptionIfErrorsExist() {
        if (!this.hasErrors()) {
            return;
        }
        throw new ConfigurationException(this.getMessages());
    }
    
    public void throwCreationExceptionIfErrorsExist() {
        if (!this.hasErrors()) {
            return;
        }
        throw new CreationException(this.getMessages());
    }
    
    public void throwIfNewErrors(final int n) throws ErrorsException {
        if (this.size() == n) {
            return;
        }
        throw this.toException();
    }
    
    public void throwProvisionExceptionIfErrorsExist() {
        if (!this.hasErrors()) {
            return;
        }
        throw new ProvisionException(this.getMessages());
    }
    
    public ErrorsException toException() {
        return new ErrorsException(this);
    }
    
    public Errors tooManyConstructors(final Class<?> clazz) {
        return this.addMessage("%s has more than one constructor annotated with @Inject. Classes must have either one (and only one) constructor annotated with @Inject or a zero-argument constructor that is not private.", clazz);
    }
    
    public Errors voidProviderMethod() {
        return this.addMessage("Provider methods must return a value. Do not return void.", new Object[0]);
    }
    
    public Errors withSource(final Object o) {
        if (o == $SourceProvider.UNKNOWN_SOURCE) {
            return this;
        }
        return new Errors(this, o);
    }
    
    private abstract static class Converter<T>
    {
        final Class<T> type;
        
        Converter(final Class<T> type) {
            this.type = type;
        }
        
        boolean appliesTo(final Object o) {
            return o != null && this.type.isAssignableFrom(o.getClass());
        }
        
        String convert(final Object o) {
            return this.toString(this.type.cast(o));
        }
        
        abstract String toString(final T p0);
    }
}
