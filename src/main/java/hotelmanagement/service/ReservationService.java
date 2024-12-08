package hotelmanagement.service;

import hotelmanagement.model.Reservation;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

public interface ReservationService extends Remote {
    void addReservation(Reservation reservation) throws RemoteException;
    void updateReservation(Reservation reservation) throws RemoteException;
    void deleteReservation(int reservationId) throws RemoteException;
    List<Reservation> getAllReservations(int client) throws RemoteException;
    Reservation getReservationById(int reservationId) throws RemoteException;
    boolean checkAvailability(int roomId, LocalDate checkInDate, LocalDate checkOutDate) throws RemoteException;
    List<Integer> fetchClientIds() throws RemoteException;
    List<String> fetchRooms() throws RemoteException;
}