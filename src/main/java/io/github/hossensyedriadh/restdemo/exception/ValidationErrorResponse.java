package io.github.hossensyedriadh.restdemo.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
public final class ValidationErrorResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = 6073678555180321958L;

    private int status;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss a")
    private LocalDateTime timestamp;

    private String message;

    private String error;

    private String path;

    private List<String> details;

    public ValidationErrorResponse(HttpServletRequest httpServletRequest, HttpStatus status, String message, List<String> details) {
        this.timestamp = RequestContextUtils.getTimeZone(httpServletRequest) != null ?
                LocalDateTime.ofInstant(Instant.now(), ZoneId.of(Objects.requireNonNull(RequestContextUtils.getTimeZone(httpServletRequest)).toZoneId().getId()))
                : LocalDateTime.now(ZoneId.systemDefault());
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.details = details;
        this.path = httpServletRequest.getRequestURI();
    }
}
