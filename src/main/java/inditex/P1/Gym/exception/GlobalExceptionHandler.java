package inditex.P1.Gym.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 404 / 409 - Objeto no encontrado o conflicto de datos
    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleObjectNotFound(ObjectNotFoundException exception) {
        return new ResponseEntity<>(new ErrorResponse(exception.getMessage()), exception.getStatus());
    }

    // 400 - Campos inválidos (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException exception) {
        String message = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return new ResponseEntity<>(new ErrorResponse(message), HttpStatus.BAD_REQUEST);
    }

    // 400 - JSON malformado
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadable(HttpMessageNotReadableException exception) {
        return new ResponseEntity<>(new ErrorResponse("El cuerpo de la petición no es válido"), HttpStatus.BAD_REQUEST);
    }

    // 405 - Método HTTP no permitido
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException exception) {
        return new ResponseEntity<>(new ErrorResponse("Método HTTP no permitido: " + exception.getMethod()), HttpStatus.METHOD_NOT_ALLOWED);
    }

    // 500 - Cualquier error inesperado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception exception) {
        log.error("Error inesperado: {}", exception.getMessage(), exception);
        return new ResponseEntity<>(new ErrorResponse("Error interno del servidor"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

