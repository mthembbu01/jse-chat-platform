package za.co.jse.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import java.time.ZonedDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ApiErrorResponse handleDuplicate(UserAlreadyExistsException ex, HttpServletRequest request, HandlerMethod method) {


        return new ApiErrorResponse(HttpStatus.CONFLICT, ex.getMessage(),
                request.getRequestURI(),
                method.getMethod().getName(),
                ZonedDateTime.now());}
}
