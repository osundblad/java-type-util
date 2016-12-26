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

import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class CacheParameters<K> {

    private final Duration asyncFetchPeriod;
    private final Duration syncFetchPeriod;
    private final Optional<BiConsumer<K, Throwable>> supplierFailedAction;

    private CacheParameters(final Duration asyncFetchPeriod, final Duration syncFetchPeriod, @Nullable final BiConsumer<K, Throwable> supplierFailedAction) {
        this.syncFetchPeriod = syncFetchPeriod;
        this.asyncFetchPeriod = asyncFetchPeriod;
        this.supplierFailedAction = Optional.ofNullable(supplierFailedAction);
    }

    /**
     * @return the time after which the value is re-fetched after returning current cached value. To
     * prevent frequent remote calls.
     */
    public Duration getAsyncFetchPeriod() {
        return asyncFetchPeriod;
    }

    /**
     * @return the time after which the value is fetched synchronously to prevent too old values.
     */
    public Duration getSyncFetchPeriod() {
        return syncFetchPeriod;
    }

    /**
     * @return the {@link Consumer} that is called when the cache {@link java.util.function.Supplier} throws
     * an {@link Exception}. Useful for, for example, logging.
     */
    public Optional<BiConsumer<K, Throwable>> getSupplierFailedAction() {
        return supplierFailedAction;
    }

    public static class Builder<K> {

        public static final Duration DEFAULT_ASYNC_REFETCH_TIME = Duration.ofSeconds(10);
        public static final Duration DEFAUL_SYNC_REFETCH_TIME = Duration.ofMinutes(1);

        public static <K> Builder<K> init() {
            return new Builder<K>();
        }

        private Duration asyncFetchPeriod = DEFAULT_ASYNC_REFETCH_TIME;
        private Duration syncFetchPeriod = DEFAUL_SYNC_REFETCH_TIME;
        @Nullable
        private BiConsumer<K, Throwable> supplierFailedAction;

        public Builder<K> asyncFetchPeriod(final Duration asyncFetchPeriod) {
            this.asyncFetchPeriod = asyncFetchPeriod;
            return this;
        }

        public Builder<K> syncFetchPeriod(final Duration syncFetchPeriod) {
            this.syncFetchPeriod = syncFetchPeriod;
            return this;
        }

        public Builder<K> supplierFailedAction(final BiConsumer<K, Throwable> supplierFailedAction) {
            this.supplierFailedAction = supplierFailedAction;
            return this;
        }

        public CacheParameters<K> build() {
            return new CacheParameters<K>(asyncFetchPeriod, syncFetchPeriod, supplierFailedAction);
        }

    }
}
