package ba.rpr.domain;

/**
 * class used to represent single source record from database
 */
public class Source implements Idable{
    private int id;
    private String name;

    public Source(){}
    public Source(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
