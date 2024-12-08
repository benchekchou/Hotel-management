package hotelmanagement.model;

import javafx.beans.property.*;

public class Client {
    private IntegerProperty id;
    private StringProperty name;
    private StringProperty contactInfo;
    private StringProperty password;
    private StringProperty phoneNumber;

    public Client(int id, String name, String contactInfo, String password, String phoneNumber) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.contactInfo = new SimpleStringProperty(contactInfo);
        this.password = new SimpleStringProperty(password);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
    }

    // Getters and setters
    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getContactInfo() {
        return contactInfo.get();
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo.set(contactInfo);
    }

    public StringProperty contactInfoProperty() {
        return contactInfo;
    }

    public String getPassword() {
        return password.get();
    }

    public void setPassword(String password) {
        this.password.set(password);
    }

    public StringProperty passwordProperty() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }
}