package ba.rpr.dao;

public class ElementAlreadyExistsException extends RuntimeException{
    public ElementAlreadyExistsException(String msg) {
        super(msg);
    }
}
