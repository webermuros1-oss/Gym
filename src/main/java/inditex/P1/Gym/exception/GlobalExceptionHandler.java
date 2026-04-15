package inditex.P1.Gym.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleObjectNotFound(ObjectNotFoundException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.BAD_REQUEST);
    }
}

