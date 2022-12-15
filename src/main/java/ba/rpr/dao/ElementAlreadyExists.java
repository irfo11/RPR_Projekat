package ba.rpr.dao;

public class ElementAlreadyExists extends RuntimeException{
    public ElementAlreadyExists(String msg) {
        super(msg);
    }
}
