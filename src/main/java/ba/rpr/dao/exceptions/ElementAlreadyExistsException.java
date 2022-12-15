package ba.rpr.dao.exceptions;

public class ElementAlreadyExistsException extends RuntimeException{
    public ElementAlreadyExistsException(String msg) {
        super(msg);
    }
}
