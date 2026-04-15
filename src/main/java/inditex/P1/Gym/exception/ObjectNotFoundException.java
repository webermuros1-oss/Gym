package inditex.P1.Gym.exception;

import org.springframework.http.HttpStatus;

public class ObjectNotFoundException extends RuntimeException {

    private final HttpStatus status;

    public ObjectNotFoundException(String objectName, Object id) {
        super("No se puede encontrar en la base de datos el objeto: " + objectName + ", con el id: " + id);
        this.status = HttpStatus.NOT_FOUND;
    }

    public ObjectNotFoundException(String objectName, String uniqueValue) {
        super("Ya existe en la base de datos el objeto: " + objectName + ", con el valor: " + uniqueValue);
        this.status = HttpStatus.CONFLICT;
    }

    public ObjectNotFoundException(String objectName, Long id, boolean active) {
        super("El objeto: " + objectName + " con id: " + id + " ya se encuentra " + (active ? "activo" : "inactivo"));
        this.status = HttpStatus.CONFLICT;
    }

    public ObjectNotFoundException(String message) {
        super(message);
        this.status = HttpStatus.NOT_FOUND;
    }

    public ObjectNotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
