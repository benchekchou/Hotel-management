// Reservation.java
package hotelmanagement.model;

import java.time.LocalDate;

public class Reservation {
    private int id;
    private int roomId;
    private int clientId;
    private LocalDate checkInDate;

    public void setId(int id) {
        this.id = id;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    private LocalDate checkOutDate;
    private boolean confirmed;

    public Reservation(int id, int roomId, int clientId, LocalDate checkInDate, LocalDate checkOutDate, boolean confirmed) {
        this.id = id;
        this.roomId = roomId;
        this.clientId = clientId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.confirmed = confirmed;
    }

    public int getId() {
        return id;
    }

    public int getRoomId() {
        return roomId;
    }

    public int getClientId() {
        return clientId;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    // Add these methods
    public LocalDate getStartDate() {
        return checkInDate;
    }

    public LocalDate getEndDate() {
        return checkOutDate;
    }
}