package inditex.P1.Gym.exception;

import org.springframework.http.HttpStatus;

public class ObjectNotFoundException extends RuntimeException {

    private final HttpStatus status;

    // 404 - No se encuentra el objeto por id
    public ObjectNotFoundException(String objectName, Long id) {
        super("No se puede encontrar en la base de datos el objeto: " + objectName + ", con el id: " + id);
        this.status = HttpStatus.NOT_FOUND;
    }

    // 409 - DNI ya registrado
    public ObjectNotFoundException(String objectName, String dni) {
        super("Ya existe en la base de datos el objeto: " + objectName + ", con el DNI: " + dni);
        this.status = HttpStatus.CONFLICT;
    }

    // 409 - Usuario ya activo o inactivo
    public ObjectNotFoundException(String objectName, Long id, boolean active) {
        super("El objeto: " + objectName + " con id: " + id + " ya se encuentra " + (active ? "activo" : "inactivo"));
        this.status = HttpStatus.CONFLICT;
    }

    public HttpStatus getStatus() {
        return status;
    }
}