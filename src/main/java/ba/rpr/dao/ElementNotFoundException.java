package ba.rpr.dao;

public class ElementNotFoundException extends RuntimeException{
    ElementNotFoundException(String msg) {
        super(msg);
    }
}
