package io.github.hossensyedriadh.restdemo.controller_advice.v1.resource;

import io.github.hossensyedriadh.restdemo.controller.v1.resource.CommentController;
import io.github.hossensyedriadh.restdemo.controller.v1.resource.PostController;
import io.github.hossensyedriadh.restdemo.controller.v1.resource.UserController;
import io.github.hossensyedriadh.restdemo.exception.GenericErrorResponse;
import io.github.hossensyedriadh.restdemo.exception.ResourceException;
import io.github.hossensyedriadh.restdemo.exception.ValidationErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(basePackageClasses = {UserController.class, PostController.class, CommentController.class})
public class ResourceControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ResourceException.class)
    public ResponseEntity<GenericErrorResponse> handleResourceException(ResourceException resourceException) {
        GenericErrorResponse errorResponse = new GenericErrorResponse(resourceException.getHttpServletRequest(),
                resourceException.getHttpStatus(), resourceException);

        return new ResponseEntity<>(errorResponse, resourceException.getHttpStatus());
    }

    /**
     * Customize the response for MethodArgumentNotValidException.
     * <p>This method delegates to {@link #handleExceptionInternal}.
     *
     * @param ex      the exception
     * @param headers the headers to be written to the response
     * @param status  the selected response status
     * @param request the current request
     * @return a {@code ResponseEntity} instance
     */
    @NonNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex, @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status, @NonNull WebRequest request) {
        List<String> details = new ArrayList<>();
        for (var error : ex.getBindingResult().getFieldErrors()) {
            details.add(error.getField() + ": " + error.getDefaultMessage());
        }

        HttpServletRequest httpServletRequest = ((ServletWebRequest) request).getRequest();

        ValidationErrorResponse validationErrorResponse = new ValidationErrorResponse(httpServletRequest,
                HttpStatus.NOT_ACCEPTABLE, "Validation failed", details);
        return new ResponseEntity<>(validationErrorResponse, HttpStatus.NOT_ACCEPTABLE);
    }
}
