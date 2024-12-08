package hotelmanagement.model;

import javafx.beans.property.*;

public class Room {
    private IntegerProperty id;
    private StringProperty type;
    private IntegerProperty price;
    private BooleanProperty availability;

    public Room(int id, String type, int price, boolean availability) {
        this.id = new SimpleIntegerProperty(id);
        this.type = new SimpleStringProperty(type);
        this.price = new SimpleIntegerProperty(price);
        this.availability = new SimpleBooleanProperty(availability);
    }




    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public StringProperty typeProperty() {
        return type;
    }

    public int getPrice() {
        return price.get();
    }

    public void setPrice(int price) {
        this.price.set(price);
    }

    public IntegerProperty priceProperty() {
        return price;
    }

    public boolean isAvailability() {
        return availability.get();
    }

    public void setAvailability(boolean availability) {
        this.availability.set(availability);
    }

    public BooleanProperty availabilityProperty() {
        return availability;
    }
}