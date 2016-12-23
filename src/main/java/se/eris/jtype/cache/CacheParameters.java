/*
 *    Copyright 2016 Olle Sundblad - olle@eris.se
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package se.eris.jtype.cache;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class CacheParameters<K> {

    @NotNull
    private final Duration refetchSyncPeriod;
    @NotNull
    private final Duration refetchAsyncPeriod;
    @NotNull
    private final Optional<BiConsumer<K, Throwable>> supplierFailedAction;

    @NotNull
    public static <K> CacheParameters<K> of(@NotNull final Duration refetchSyncPeriod, @NotNull final Duration refetchAsyncPeriod) {
        return of(refetchSyncPeriod, refetchAsyncPeriod, null);
    }

    /**
     * Example:<code>
     * </code>
     */
    @NotNull
    public static <K> CacheParameters<K> of(@NotNull final Duration refetchSyncPeriod, @NotNull final Duration refetchAsyncPeriod, @Nullable final BiConsumer<K, Throwable> supplierFailedAction) {
        return new CacheParameters<>(refetchSyncPeriod, refetchAsyncPeriod, supplierFailedAction);
    }

    /*
     * Todo: make a CacheParametersBuilder.
     */
    private CacheParameters(@NotNull final Duration refetchSyncPeriod, @NotNull final Duration refetchAsyncPeriod, @Nullable final BiConsumer<K, Throwable> supplierFailedAction) {
        this.refetchSyncPeriod = refetchSyncPeriod;
        this.refetchAsyncPeriod = refetchAsyncPeriod;
        this.supplierFailedAction = Optional.ofNullable(supplierFailedAction);
    }

    @NotNull
    public Duration getRefetchSyncPeriod() {
        return refetchSyncPeriod;
    }

    /**
     * @return the time after which the value is refeteched after returning cached value. To prevent frequent
     * refetches.
     */
    @NotNull
    public Duration getRefetchAsyncPeriod() {
        return refetchAsyncPeriod;
    }

    /**
     * @return the {@link Consumer} that is called when the {@link java.util.function.Supplier} throws
     * an {@link Exception}. Useful for, for example, logging.
     */
    @NotNull
    public Optional<BiConsumer<K, Throwable>> getSupplierFailedAction() {
        return supplierFailedAction;
    }

}
