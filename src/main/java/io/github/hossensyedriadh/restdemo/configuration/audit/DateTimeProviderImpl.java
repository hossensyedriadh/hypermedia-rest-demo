package io.github.hossensyedriadh.restdemo.configuration.audit;

import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.util.Optional;

@Component
public class DateTimeProviderImpl implements DateTimeProvider {
    /**
     * Returns the current time to be used as modification or creation date.
     *
     * @return Optional<TemporalAccessor>
     */
    @Override
    @NonNull
    public Optional<TemporalAccessor> getNow() {
        return Optional.of(new TemporalAccessor() {
            @Override
            public boolean isSupported(TemporalField field) {
                return true;
            }

            @Override
            public long getLong(TemporalField field) {
                return Instant.now(Clock.systemDefaultZone()).getEpochSecond();
            }
        });
    }
}
