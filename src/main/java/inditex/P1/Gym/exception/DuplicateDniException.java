package inditex.P1.Gym.exception;

public class DuplicateDniException extends RuntimeException {

    public DuplicateDniException(String dni) {
        super("Ya existe un usuario con el DNI: " + dni);
    }
}