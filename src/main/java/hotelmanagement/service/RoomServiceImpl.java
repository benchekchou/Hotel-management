package hotelmanagement.service;

import hotelmanagement.model.Room;
import hotelmanagement.util.DatabaseUtil;
import javafx.scene.control.Alert;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomServiceImpl extends UnicastRemoteObject implements RoomService {
    public RoomServiceImpl() throws RemoteException {
        super();
    }

    @Override
    public void addRoom(Room room) throws RemoteException {
        if (room.getType() == null || room.getType().isEmpty() || room.getPrice() <= 0) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Please fill in all fields.");
            alert.showAndWait();
            return;
        }

        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "INSERT INTO rooms (type, price, available) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, room.getType());
            statement.setInt(2, room.getPrice());
            statement.setBoolean(3, room.isAvailability());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteRoom(int roomId) throws RemoteException {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "DELETE FROM rooms WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roomId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateRoom(Room room) throws RemoteException {
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "UPDATE rooms SET type = ?, price = ?, available = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, room.getType());
            statement.setInt(2, room.getPrice());
            statement.setBoolean(3, room.isAvailability());
            statement.setInt(4, room.getId());

            // Log the query and parameters for debugging
            System.out.println("Executing query: " + query);
            System.out.println("Parameters: " + room.getType() + ", " + room.getPrice() + ", " + room.isAvailability() + ", " + room.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Room> getAllRooms() throws RemoteException {
        List<Room> rooms = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM rooms";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Room room = new Room(
                        resultSet.getInt("id"),
                        resultSet.getString("type"),
                        resultSet.getInt("price"),
                        resultSet.getBoolean("available")
                );
                rooms.add(room);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    @Override
    public Room getRoomById(int roomId) throws RemoteException {
        Room room = null;
        try (Connection connection = DatabaseUtil.getConnection()) {
            String query = "SELECT * FROM rooms WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, roomId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                room = new Room(
                        resultSet.getInt("id"),
                        resultSet.getString("type"),
                        resultSet.getInt("price"),
                        resultSet.getBoolean("available")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return room;
    }
    @Override
    public String getRoomTypeById(int roomId) {
        String roomType = "";
        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT type FROM rooms WHERE id = " + roomId;
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                roomType = rs.getString("type");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return roomType;
    }

}