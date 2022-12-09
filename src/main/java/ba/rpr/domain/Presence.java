package ba.rpr.domain;

/**
 * Shows the source of a micronutrient and the amount of a micronutrient (represented in mcg) inside 100 grams of the source
 */
public class Presence {
    private int id;
    private Micronutrient micronutrient;
    private Source source;
    private double amount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Micronutrient getMicronutrient() {
        return micronutrient;
    }

    public void setMicronutrient(Micronutrient micronutrient) {
        this.micronutrient = micronutrient;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
