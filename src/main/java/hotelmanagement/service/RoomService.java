package hotelmanagement.service;

import hotelmanagement.model.Room;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface RoomService extends Remote {
    void addRoom(Room room) throws RemoteException;
    void deleteRoom(int roomId) throws RemoteException;
    void updateRoom(Room room) throws RemoteException;
    List<Room> getAllRooms() throws RemoteException;
    Room getRoomById(int roomId) throws RemoteException;
    String getRoomTypeById(int roomId) throws RemoteException;
}