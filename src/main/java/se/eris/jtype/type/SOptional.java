package se.eris.jtype.type;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Serializable version of {@link java.util.Optional} with some of Guava's Optional methods.
 *
 * Useful until Java designers realize the stupidity and makes Optional {@link Serializable}.
 *
 * @param <T>
 */
public final class SOptional<T> implements Serializable {

    private static final SOptional<?> EMPTY = new SOptional<>(null);

    public static <T> SOptional<T> empty(final T value) {
        //noinspection unchecked,CastToConcreteClass
        return (SOptional<T>) EMPTY;
    }

    public static <T> SOptional<T> of(final T value) {
        return new SOptional<>(value);
    }

    public static <T> SOptional<T> ofNullable(@Nullable final T value) {
        return new SOptional<>(value);
    }

    public static <T> SOptional<T> from(final Optional<T> value) {
        return ofNullable(value.orElse(null));
    }

    @Nullable
    private final T value;

    private SOptional(@Nullable final T value) {
        this.value = value;
    }

    public T get() {
        if (value == null) {
            throw new NoSuchElementException();
        }
        return value;
    }

    @Nullable
    public T orElse(@Nullable final T elseValue) {
        if (value == null) {
            return elseValue;
        }
        return value;
    }

    @Nullable
    public T orNull() {
        if (value == null) {
            return null;
        }
        return value;
    }

    @Nullable
    public T orElseGet(final Supplier<? extends T> supplier) {
        if (value == null) {
            return supplier.get();
        }
        return value;
    }

    public <X extends Throwable> T orElseThrow(final Supplier<? extends X> exceptionSupplier) throws X {
        if (value == null) {
            throw exceptionSupplier.get();
        }
        return value;
    }


    @Contract(pure = true)
    public boolean isAbsent() {
        return value == null;
    }

    @Contract(pure = true)
    public boolean isPresent() {
        return value != null;
    }

    public <U> Optional<U> flatMap(final Function<? super T,Optional<U>> mapper) {
        if (value == null) {
            return Optional.empty();
        }
        return mapper.apply(value);
    }

    public Optional<T> filter(final Predicate<? super T> predicate) {
        if (value == null) {
            return Optional.empty();
        }
        return predicate.test(value) ? Optional.of(value) : Optional.empty();
    }

    public void ifPresent(final Consumer<? super T> consumer) {
        if (value != null) {
            consumer.accept(value);
        }
    }

    public <U> Optional<U> map(final Function<? super T,? extends U> mapper) {
        if (value == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(mapper.apply(value));
    }


    @SuppressWarnings("ControlFlowStatementWithoutBraces")
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if ((o == null) || (getClass() != o.getClass())) return false;

        final SOptional<?> that = (SOptional<?>) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return (value != null) ? value.hashCode() : 0;
    }

    public Optional<T> asOptional() {
        return Optional.ofNullable(value);
    }

    public Set<T> asSet() {
        return (value == null) ? Collections.emptySet() : Collections.singleton(value);
    }

    @Override
    public String toString() {
        return "SOptional{value=" + value + '}';
    }

}