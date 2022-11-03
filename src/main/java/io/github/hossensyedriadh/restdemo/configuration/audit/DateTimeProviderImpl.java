package io.github.hossensyedriadh.restdemo.configuration.audit;

import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
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
        return Optional.of(LocalDateTime.now(ZoneId.systemDefault()));
    }
}
