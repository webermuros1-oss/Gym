package inditex.P1.Gym.exception;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(String objectName, Long id) {
        super("No se puede encontrar en la base de datos el objeto: " + objectName + ", con el id: " + id);
    }
}

