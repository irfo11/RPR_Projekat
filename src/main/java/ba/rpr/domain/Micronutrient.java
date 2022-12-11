package ba.rpr.domain;

/**
 * Class used to represent single micronutrient record from database
 */
public class Micronutrient {
    private int id;
    private String name;
    private String role;
    private boolean isVitamin;

    public Micronutrient(){}
    public Micronutrient(int id, String name, String role, boolean isVitamin) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.isVitamin = isVitamin;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isVitamin() {
        return isVitamin;
    }

    public void setVitamin(boolean vitamin) {
        isVitamin = vitamin;
    }
}
