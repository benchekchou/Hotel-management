package hotelmanagement.service;

import hotelmanagement.model.Reservation;
import hotelmanagement.util.DatabaseUtil;

import java.rmi.RemoteException;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ReservationServiceImpl implements ReservationService {

    private Connection connection;

    public ReservationServiceImpl() throws RemoteException {
        try {
            this.connection = DatabaseUtil.getConnection();
        } catch (SQLException e) {
            throw new RemoteException("Database connection error", e);
        }
    }

    @Override
    public List<Reservation> getAllReservations(int clientId) throws RemoteException {
        List<Reservation> reservations = new ArrayList<>();
        String sql = (clientId == 0) ? "SELECT * FROM reservations" : "SELECT * FROM reservations WHERE client_id = ?";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");  // Ensure this matches your input format

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            if (clientId != 0) {
                pstmt.setInt(1, clientId);
            }

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String startDateStr = rs.getString("start_date");
                String endDateStr = rs.getString("end_date");
                LocalDate startDate = (startDateStr != null) ? LocalDate.parse(startDateStr, formatter) : null;
                LocalDate endDate = (endDateStr != null) ? LocalDate.parse(endDateStr, formatter) : null;

                reservations.add(new Reservation(
                        rs.getInt("id"),
                        rs.getInt("room_id"),
                        rs.getInt("client_id"),
                        startDate,
                        endDate,
                        rs.getBoolean("confirmed")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DateTimeParseException e) {
            System.err.println("Date parsing error: " + e.getMessage());
        }
        return reservations;
    }


    @Override
    public Reservation getReservationById(int reservationId) throws RemoteException {
        String sql = "SELECT * FROM reservations WHERE id = ?";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String startDateStr = rs.getString("start_date");
                String endDateStr = rs.getString("end_date");
                LocalDate startDate = (startDateStr != null) ? LocalDate.parse(startDateStr, formatter) : null;
                LocalDate endDate = (endDateStr != null) ? LocalDate.parse(endDateStr, formatter) : null;

                return new Reservation(
                        rs.getInt("id"),
                        rs.getInt("room_id"),
                        rs.getInt("client_id"),
                        startDate,
                        endDate,
                        rs.getBoolean("confirmed")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if no reservation is found
    }

    @Override
    public boolean checkAvailability(int roomId, LocalDate checkInDate, LocalDate checkOutDate) throws RemoteException {
        String sql = "SELECT COUNT(*) FROM reservations WHERE room_id = ? AND (start_date < ? AND end_date > ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, roomId);
            pstmt.setDate(2, Date.valueOf(checkInDate));
            pstmt.setDate(3, Date.valueOf(checkOutDate));

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) == 0; // If count is 0, the room is available
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Default to not available if there's an error or room is already booked
    }

    @Override
    public List<Integer> fetchClientIds() throws RemoteException {
        List<Integer> clientIds = new ArrayList<>();
        String sql = "SELECT id FROM clients"; // Assuming you have a "clients" table

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                clientIds.add(rs.getInt("id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientIds;
    }

    @Override
    public List<String> fetchRooms() throws RemoteException {
        List<String> rooms = new ArrayList<>();
        String sql = "SELECT id, type FROM rooms"; // Assuming you have a "rooms" table

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                int roomId = rs.getInt("id");
                String roomType = rs.getString("type");
                rooms.add("Room " + roomId + " - " + roomType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    @Override
    public void addReservation(Reservation reservation) throws RemoteException {
        String sql = "INSERT INTO reservations (room_id, client_id, start_date, end_date, confirmed) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, reservation.getRoomId());
            pstmt.setInt(2, reservation.getClientId());
            pstmt.setString(3, reservation.getCheckInDate().toString());
            pstmt.setString(4, reservation.getCheckOutDate().toString());
            pstmt.setBoolean(5, reservation.isConfirmed());
            pstmt.executeUpdate();

            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                reservation.setId(generatedKeys.getInt(1)); // Set the generated ID for the reservation
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateReservation(Reservation reservation) throws RemoteException {
        String sql = "UPDATE reservations SET room_id = ?, client_id = ?, start_date = ?, end_date = ?, confirmed = ? WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, reservation.getRoomId());
            pstmt.setInt(2, reservation.getClientId());
            pstmt.setString(3, reservation.getCheckInDate().toString());
            pstmt.setString(4, reservation.getCheckOutDate().toString());
            pstmt.setBoolean(5, reservation.isConfirmed());
            pstmt.setInt(6, reservation.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteReservation(int reservationId) throws RemoteException {
        String sql = "DELETE FROM reservations WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, reservationId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
